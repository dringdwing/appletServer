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
     * 查看用户是否可以签到
     *
     * @param userId
     * @param today
     * @return
     */
    String validCanCheckIn(int userId, String today);

    /**
     * 签到
     *
     * @param param
     */
    public void checkin(HashMap param);

    /**
     * c
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
}
