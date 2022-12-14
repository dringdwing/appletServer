package com.vector.server.service.impl;

import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateRange;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.vector.server.constants.SystemConstants;
import com.vector.server.domain.entity.*;
import com.vector.server.exception.AppleServerException;
import com.vector.server.mapper.CheckinMapper;
import com.vector.server.service.*;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.*;

/**
 * 签到表(Checkin)表服务实现类
 * 先判断签到用户是否存在人脸模型
 *
 * @author makejava
 * @since 2022-12-03 21:01:22
 */
@Service("checkinService")
public class CheckinServiceImpl extends ServiceImpl<CheckinMapper, Checkin> implements CheckinService {

    @Resource
    private SystemConstants constants;

    @Resource
    private HolidaysService holidaysService;

    @Resource
    private WorkdayService workdayService;

    @Resource
    private FaceModelService faceModelService;

    @Resource
    private CityService cityService;

    @Value("${applet.face.createFaceModelUrl}")
    private String createFaceModelUrl;

    @Value("${applet.face.checkinUrl}")
    private String checkinUrl;

    @Value("${applet.email.hr}")
    private String hrEmail;

    @Value("${applet.code}")
    private String code;

    /**
     * 查看用户当天是否可以签到
     *
     * @param userId
     * @param today
     * @return
     */
    @Override
    @SuppressWarnings("all")
    public String validCanCheckIn(int userId, String today) {
        boolean bool_1 = holidaysService.getBaseMapper().selectOne(new QueryWrapper<Holidays>()
                .select("id")
                .eq("date", today)
                .last("limit 1")) != null ? true : false;
        boolean bool_2 = workdayService.getBaseMapper().selectOne(new QueryWrapper<Workday>()
                .select("id")
                .eq("date", today)
                .last("limit 1")) != null ? true : false;
        String type = "工作日";
        if (DateUtil.date().isWeekend()) {
            type = "节假日";
        }
        if (bool_1) {
            type = "节假日";
        } else if (bool_2) {
            type = "工作日";
        }
        if (type.equals("节假日")) {
            return "节假日不需要考勤";
        } else {
            DateTime now = DateUtil.date();
            String start = DateUtil.today() + " " + constants.attendanceStartTime;
            String end = DateUtil.today() + " " + constants.attendanceEndTime;
            DateTime attendanceStart = DateUtil.parse(start);
            DateTime attendanceEnd = DateUtil.parse(end);

            if (now.isBefore(attendanceStart)) {
                return "没到上班考勤开始时间";
            } else if (now.isAfter(attendanceEnd)) {
                return "超过了上班考勤结束时间";
            } else {
                boolean bool = this.getBaseMapper().selectOne(new QueryWrapper<Checkin>()
                        .select("id")
                        .eq("user_id", userId)
                        .eq("date", today)
                        .between("create_time", start, end)
                        .last("limit 1")) != null ? true : false;
                return bool ? "今日已经考勤，不用重复考勤" : "可以考勤";
            }
        }
    }

    /**
     * 实现人脸签到
     *
     * @param param
     */
    @Override
    public void checkin(HashMap param) {
        Date d1 = DateUtil.date();
        Date d2 = DateUtil.parse(DateUtil.today() + " " + constants.attendanceTime);
        Date d3 = DateUtil.parse(DateUtil.today() + " " + constants.attendanceEndTime);
        int status = 1;
        if (d1.compareTo(d2) <= 0) {
            status = 1;
        } else if (d1.compareTo(d2) > 0 && d1.compareTo(d3) < 0) {
            status = 2;//迟到
        } else {
            throw new AppleServerException("超出考勤时间段，无法考勤");
        }
        int userId = (Integer) param.get("userId");
        FaceModel faceModel = faceModelService.getBaseMapper().selectOne(new QueryWrapper<FaceModel>()
                .select("face_model")
                .eq("user_id", userId));
        if (!Optional.ofNullable(faceModel.getFaceModel()).isPresent()) {
            throw new AppleServerException("不存在人脸模型");
        } else {
            /*将图片上传到人脸识别程序*/
            String path = (String) param.get("path");
            HttpRequest request = HttpUtil.createPost(checkinUrl);
            request.form("path", FileUtil.file(path), "targetModel", faceModel.getFaceModel());
            HttpResponse response = request.execute();
            if (response.getStatus() == 200) {
                log.error("人脸识别程序返回结果：" + response.body());
                throw new AppleServerException("人脸识别失败");
            }
            String body = response.body();
            if ("无法识别出人脸".equals(body) || "照片中存在多张人脸".equals(body)) {
                throw new AppleServerException(body);
            } else if ("False".equals(body)) {
                throw new AppleServerException("签到无效，被本人签到");
            } else if ("True".equals(body)) {
                /*人脸签到成功*/
                //  查询疫情风险等级
                int risk = 1;
                String city = (String) param.get("city");
                String district = (String) param.get("district");
                if (!StrUtil.isEmpty(city) && !StrUtil.isEmpty(district)) {
                    City cityCode = cityService.getBaseMapper().selectOne(new QueryWrapper<City>()
                            .select("code")
                            .eq("city", city));
                    try {
                        /*向本地宝发送请求 获取该地区风险等级 从html解析出风险等级*/
                        String url = "http://m." + cityCode.getCode() + ".bendibao.com/news/yqdengji/?qu=" + district;
                        Document document = Jsoup.connect(url).get();
                        Elements elementsByClass = document.getElementsByClass("list-content");
                        if (elementsByClass.size() > 0) {
                            String result = elementsByClass.get(0).text();
                            if ("高风险".equals(result)) {
                                risk = 3;
                                // TODO 发送警告邮件

                            } else if ("中风险".equals(result)) {
                                risk = 2;
                            }
                        }

                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
                //  保存签到记录 每天只能签到1次
                String address = (String) param.get("address");
                String country = (String) param.get("country");
                String province = (String) param.get("province");
                Checkin entity = new Checkin();
                entity.setUserId((long) userId);
                entity.setAddress(address);
                entity.setCountry(country);
                entity.setProvince(province);
                entity.setCity(city);
                entity.setDistrict(district);
                entity.setStatus(status);
                entity.setRisk(risk);
                entity.setDate(DateUtil.today());
                entity.setCreateTime(d1);
                this.getBaseMapper().insert(entity);
            }
        }
    }

    /**
     * 先判断签到用户是否存在人脸模型 创建新员工人脸模型数据
     *
     * @param userId
     * @param path
     */
    @Override
    public void createFaceModel(int userId, String path) {
        HttpRequest request = HttpUtil.createPost(createFaceModelUrl)
                .form("photo", FileUtil.file(path))
                .form("code", code);
        HttpResponse response = request.execute();
        String body = response.body();
        if ("无法识别出人脸".equals(body) || "照片中存在多张人脸".equals(body)) {
            throw new AppleServerException(body);
        } else {
            FaceModel faceModel = new FaceModel();
            faceModel.setUserId((long) userId);
            faceModel.setFaceModel(body);
            faceModelService.getBaseMapper().insert(faceModel);
        }
    }

    /**
     * 查询用户当天是否已经签到
     *
     * @param userId
     * @return
     */
    @Override
    public HashMap searchTodayCheckin(int userId) {
        HashMap map = this.getBaseMapper().searchTodayCheckin(userId);
        return map;
    }

    /**
     * 查询员工签到记录
     *
     * @param userId
     * @return
     */
    @Override
    public long searchCheckinDays(int userId) {
        long days = this.getBaseMapper()
                .selectCount(new QueryWrapper<Checkin>()
                        .eq("user_id", userId));
        return days;
    }

    /**
     * 本周考勤统计 查询签到天数
     *
     * @param param
     * @return
     */
    @SuppressWarnings("all")
    @Override
    public ArrayList<HashMap> searchWeekCheckin(HashMap param) {
        ArrayList<HashMap> checkinList = this.getBaseMapper().searchWeekCheckin(param);
        List<Holidays> holidaysList = holidaysService.getBaseMapper()
                .selectList(new QueryWrapper<Holidays>()
                        .select("date")
                        .between("date", param.get("startDate"), param.get("endDate"))); // 查询节假日
        List<Workday> workdayList = workdayService.getBaseMapper()
                .selectList(new QueryWrapper<Workday>()
                        .select("date")
                        .between("date", param.get("startDate"), param.get("endDate"))); // 查询工作日
        DateTime startDate = DateUtil.parseDate(param.get("startDate").toString()); // 开始日期
        DateTime endDate = DateUtil.parseDate(param.get("endDate").toString()); // 结束日期
        DateRange range = DateUtil.range(startDate, endDate, DateField.DAY_OF_MONTH);   // 日期范围
        ArrayList<HashMap> list = new ArrayList<>();
        range.forEach(one -> {
            String date = one.toString("yyyy-MM-dd");
            String type = "工作日";
            if (one.isWeekend()) {
                type = "节假日";
            }
            if (holidaysList != null && holidaysList.contains(date)) { // 判断是否是节假日
                type = "节假日";
            } else if (workdayList != null && workdayList.contains(date)) { // 判断是否是工作日
                type = "工作日";
            }
            String status = "";
            if (type.equals("工作日") && DateUtil.compare(one, DateUtil.date()) <= 0) { // 如果是工作日并且日期小于等于今天
                status = "缺勤";
                boolean flag = false;
                for (HashMap<String, String> map : checkinList) {
                    if (map.containsValue(date)) {
                        status = map.get("status");
                        flag = true;
                        break;
                    }
                }
                DateTime endTime = DateUtil.parse(DateUtil.today() + " " + constants.attendanceEndTime); // 签退截止时间
                String today = DateUtil.today();
                if (date.equals(today) && DateUtil.date().isBefore(endTime) && flag == false) { // 如果是今天并且时间小于签退截止时间并且没有签到记录
                    status = "";
                }
            }
            HashMap map = new HashMap();
            map.put("date", date);
            map.put("status", status);
            map.put("type", type);
            map.put("day", one.dayOfWeekEnum().toChinese("周")); // 星期几
            list.add(map);
        });
        return list;
    }

    /**
     * 查询本月签到记录
     *
     * @param param
     * @return
     */
    @Override
    public ArrayList<HashMap> searchMonthCheckin(HashMap param) {
        return this.searchWeekCheckin(param);
    }

}
