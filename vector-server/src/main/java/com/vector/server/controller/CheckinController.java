package com.vector.server.controller;


import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import com.vector.server.config.shiro.JwtUtil;
import com.vector.server.constants.SystemConstants;
import com.vector.server.domain.vo.CheckinVo;
import com.vector.server.exception.AppleServerException;
import com.vector.server.service.CheckinService;
import com.vector.server.service.UserService;
import com.vector.server.util.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * 签到表(Checkin)表控制层
 *
 * @author makejava
 * @since 2022-12-04 07:00:09
 */
@RestController
@RequestMapping("/checkin")
@Api("签到模块")
@Slf4j
public class CheckinController {

    @Resource
    private JwtUtil jwtUtil;

    @Resource
    private CheckinService checkinService;

    @Resource
    private UserService userService;

    @Resource
    private SystemConstants constants;

    @Value("${applet.image-folder}")
    private String imageFolder;

    @GetMapping("/validCanCheckIn")
    @ApiOperation(value = "查看用户是否可以签到")
    public R validCanCheckIn(@RequestHeader("token") String token) {
        int userId = jwtUtil.getUserId(token);
        String result = checkinService.validCanCheckIn(userId, DateUtil.today());
        return R.ok(result);
    }

    @PostMapping("/checkin")
    @ApiOperation(value = "签到")
    public R checkIn(@Valid CheckinVo form, @RequestParam("photo") MultipartFile file, @RequestHeader("token") String token) {
        if (file.isEmpty()) {
            return R.error("上传文件为空");
        }
        int userId = jwtUtil.getUserId(token);
        String fileName = file.getOriginalFilename().toLowerCase();
        // 检查文件格式是否为jpg
        if (!fileName.endsWith(".jpg")) {
            return R.error("文件格式不正确");
        } else {
            // 保存文件到E盘
            String path = imageFolder + "/" + fileName;

            try {
                file.transferTo(Paths.get(path));
                HashMap param = new HashMap();
                param.put("userId", userId);
                param.put("path", path);
                param.put("city", form.getCity());
                param.put("district", form.getDistrict());
                param.put("address", form.getAddress());
                param.put("country", form.getCountry());
                param.put("province", form.getProvince());
                checkinService.checkin(param);
                R.ok("签到成功");
            } catch (IOException e) {
                log.error(e.getMessage(), e);
                return R.error("保存文件失败");
            } finally {
                FileUtil.del(path);
            }
        }
        return R.ok();
    }

    //创建新员工人脸模型数据
    @PostMapping("/createFaceModel")
    @ApiOperation(value = "创建新员工人脸模型数据")
    public R createFaceModel(@RequestParam("photo") MultipartFile file, @RequestHeader("token") String token) {
        if (file == null) {
            return R.error("没有上传文件");
        }
        int userId = jwtUtil.getUserId(token);
        String fileName = file.getOriginalFilename().toLowerCase();
        if (!fileName.endsWith(".jpg")) {
            return R.error("必须提交JPG格式图片");
        } else {
            String path = imageFolder + "/" + fileName;
            try {
                file.transferTo(Paths.get(path));
                checkinService.createFaceModel(userId, path);
                return R.ok("人脸建模成功");
            } catch (IOException e) {
                log.error(e.getMessage(), e);
                throw new AppleServerException("图片保存错误");
            } finally {
                FileUtil.del(path);
            }

        }
    }

    //查询用户当日签到数据
    @GetMapping("/searchTodayCheckin")
    @ApiOperation(value = "查询用户当日签到数据")
    public R getCheckinData(@RequestHeader("token") String token) {
        int userId = jwtUtil.getUserId(token);
        HashMap map = checkinService.searchTodayCheckin(userId);
        map.put("attendanceTime", constants.attendanceTime);
        map.put("closingTime", constants.closingTime);
        long days = checkinService.searchCheckinDays(userId);
        map.put("checkinDays", days);

        DateTime hiredate = DateUtil.parse(userService.searchUserHiredate(userId));
        DateTime startDate = DateUtil.beginOfDay(DateUtil.date()); // 今天开始时间
        if (startDate.isBefore(hiredate)) {
            startDate = hiredate;
        }
        DateTime endDate = DateUtil.endOfWeek(DateUtil.date()); //本周结束时间
        HashMap param = new HashMap();
        param.put("startDate", startDate.toString());
        param.put("endDate", endDate.toString());
        param.put("userId", userId);
        ArrayList<HashMap> list = checkinService.searchWeekCheckin(param);
        map.put("weekCheckin", list);
        return R.ok().put("result", map);
    }

}

