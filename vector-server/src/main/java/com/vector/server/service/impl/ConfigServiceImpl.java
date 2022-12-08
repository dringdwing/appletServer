package com.vector.server.service.impl;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.vector.server.domain.entity.Config;
import com.vector.server.mapper.ConfigMapper;
import com.vector.server.service.ConfigService;
import org.springframework.stereotype.Service;
/**
 * (Config)表服务实现类
 *
 * @author makejava
 * @since 2022-12-03 21:01:06
 */
@Service("configService")
public class ConfigServiceImpl extends ServiceImpl<ConfigMapper, Config> implements ConfigService {
}
