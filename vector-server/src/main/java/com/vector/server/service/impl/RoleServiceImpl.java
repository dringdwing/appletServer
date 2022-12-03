package com.vector.server.service.impl;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.vector.server.entity.Role;
import com.vector.server.mapper.RoleMapper;
import com.vector.server.service.RoleService;
import org.springframework.stereotype.Service;
/**
 * 角色表(Role)表服务实现类
 *
 * @author makejava
 * @since 2022-12-03 21:01:22
 */
@Service("roleService")
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements RoleService {
}
