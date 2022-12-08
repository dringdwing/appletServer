package com.vector.server.service.impl;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.vector.server.domain.entity.Checkin;
import com.vector.server.mapper.CheckinMapper;
import com.vector.server.service.CheckinService;
import org.springframework.stereotype.Service;
/**
 * 签到表(Checkin)表服务实现类
 *
 * @author makejava
 * @since 2022-12-03 21:01:22
 */
@Service("checkinService")
public class CheckinServiceImpl extends ServiceImpl<CheckinMapper, Checkin> implements CheckinService {
}
