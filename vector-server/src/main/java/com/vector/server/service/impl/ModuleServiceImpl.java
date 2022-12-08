package com.vector.server.service.impl;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.vector.server.domain.entity.Module;
import com.vector.server.mapper.ModuleMapper;
import com.vector.server.service.ModuleService;
import org.springframework.stereotype.Service;
/**
 * 模块资源表(Module)表服务实现类
 *
 * @author makejava
 * @since 2022-12-03 21:01:22
 */
@Service("moduleService")
public class ModuleServiceImpl extends ServiceImpl<ModuleMapper, Module> implements ModuleService {
}
