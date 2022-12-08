package com.vector.server.service.impl;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.vector.server.domain.entity.Workday;
import com.vector.server.mapper.WorkdayMapper;
import com.vector.server.service.WorkdayService;
import org.springframework.stereotype.Service;
/**
 * (Workday)表服务实现类
 *
 * @author makejava
 * @since 2022-12-03 21:01:22
 */
@Service("workdayService")
public class WorkdayServiceImpl extends ServiceImpl<WorkdayMapper, Workday> implements WorkdayService {
}
