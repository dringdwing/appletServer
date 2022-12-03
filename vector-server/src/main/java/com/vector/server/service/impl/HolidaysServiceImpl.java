package com.vector.server.service.impl;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.vector.server.entity.Holidays;
import com.vector.server.mapper.HolidaysMapper;
import com.vector.server.service.HolidaysService;
import org.springframework.stereotype.Service;
/**
 * 节假日表(Holidays)表服务实现类
 *
 * @author makejava
 * @since 2022-12-03 21:01:22
 */
@Service("holidaysService")
public class HolidaysServiceImpl extends ServiceImpl<HolidaysMapper, Holidays> implements HolidaysService {
}
