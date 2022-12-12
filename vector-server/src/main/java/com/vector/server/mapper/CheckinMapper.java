package com.vector.server.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.vector.server.domain.entity.Checkin;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * 签到表(Checkin)表数据库访问层
 *
 * @author makejava
 * @since 2022-12-03 20:55:39
 */
public interface CheckinMapper extends BaseMapper<Checkin> {

    /**
     * 查询用户当天是否已经签到
     * @param userId
     * @return
     */
    public HashMap searchTodayCheckin(int userId);

    /**
     * 查询签到天数
     * @param param
     * @return
     */
    public ArrayList<HashMap> searchWeekCheckin(HashMap param);
}

