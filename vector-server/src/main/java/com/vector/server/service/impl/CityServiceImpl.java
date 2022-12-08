package com.vector.server.service.impl;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.vector.server.domain.entity.City;
import com.vector.server.mapper.CityMapper;
import com.vector.server.service.CityService;
import org.springframework.stereotype.Service;
/**
 * 疫情城市列表(City)表服务实现类
 *
 * @author makejava
 * @since 2022-12-03 21:01:22
 */
@Service("cityService")
public class CityServiceImpl extends ServiceImpl<CityMapper, City> implements CityService {
}
