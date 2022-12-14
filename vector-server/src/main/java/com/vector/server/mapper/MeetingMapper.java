package com.vector.server.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.vector.server.domain.entity.Meeting;
import com.vector.server.domain.pojo.MessageEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Repository;

/**
 * 会议表(Meeting)表数据库访问层
 *
 * @author makejava
 * @since 2022-12-03 20:55:39
 */

public interface MeetingMapper extends BaseMapper<Meeting> {




}

