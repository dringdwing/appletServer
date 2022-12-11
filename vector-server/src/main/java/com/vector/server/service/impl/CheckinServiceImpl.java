package com.vector.server.service.impl;

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
import java.util.Date;
import java.util.HashMap;
import java.util.Optional;

/**
 * 签到表(Checkin)表服务实现类
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
     * 查看用户是否可以签到
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
     * 签到
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
        }
        int userId = (Integer) param.get("userId");
        FaceModel faceModel = faceModelService.getBaseMapper().selectOne(new QueryWrapper<FaceModel>()
                .select("face_model")
                .eq("user_id", userId));
        if (!Optional.ofNullable(faceModel.getFaceModel()).isPresent()) {
            throw new AppleServerException("请先上传人脸模型");
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
                // TODO 保存签到记录
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
                entity.setDate(DateUtil.today());
                entity.setCreateTime(d1);
                this.getBaseMapper().insert(entity);
            }
        }
    }

}
