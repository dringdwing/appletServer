package com.vector.server.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.vector.server.domain.entity.Checkin;

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
     * @param userId
     * @param today
     * @return
     */
    String validCanCheckIn(int userId, String today);

    /**
     * 签到
     * @param param
     */
    public void checkin(HashMap param);
}
