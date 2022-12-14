package com.vector.server.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.vector.server.domain.entity.Meeting;
import com.vector.server.exception.AppleServerException;
import com.vector.server.mapper.MeetingMapper;
import com.vector.server.service.MeetingService;
import org.springframework.stereotype.Service;

/**
 * 会议表(Meeting)表服务实现类
 *
 * @author makejava
 * @since 2022-12-03 21:01:22
 */
@Service("meetingService")
public class MeetingServiceImpl extends ServiceImpl<MeetingMapper, Meeting> implements MeetingService {
    @Override
    public void insertMeeting(Meeting entity) {
        boolean flag = this.save(entity);
        if (!flag) {
            throw new AppleServerException("会议创建失败");
        }

    }
}
