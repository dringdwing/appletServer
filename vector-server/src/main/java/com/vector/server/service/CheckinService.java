package com.vector.server.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.vector.server.domain.entity.Checkin;

import java.util.ArrayList;
import java.util.HashMap;


/**
 * 签到表(Checkin)表服务接⼝
 *
 * @author makejava
 * @since 2022-12-03 20:55:39
 */
public interface CheckinService extends IService<Checkin> {
    /**
     * 查看用户当天是否可以签到
     *
     * @param userId
     * @param today
     * @return
     */
    String validCanCheckIn(int userId, String today);

    /**
     * 实现人脸签到
     *
     * @param param
     */
    public void checkin(HashMap param);

    /**
     * 先判断签到用户是否存在人脸模型？如果存在，直接签到，如果不存在，先注册人脸模型，再签到
     *
     * @param userId
     * @param path
     */
    public void createFaceModel(int userId, String path);

    public HashMap searchTodayCheckin(int userId);

    public long searchCheckinDays(int userId);

    /**
     * 查询本周签到记录
     * @param param
     * @return
     */
    public ArrayList<HashMap> searchWeekCheckin(HashMap param);

    /**
     * 查询本月签到记录
     * @param param
     * @return
     */
    public ArrayList<HashMap> searchMonthCheckin(HashMap param);
}
