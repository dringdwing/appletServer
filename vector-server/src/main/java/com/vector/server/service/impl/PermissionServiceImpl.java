package com.vector.server.service.impl;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.vector.server.domain.entity.Permission;
import com.vector.server.mapper.PermissionMapper;
import com.vector.server.service.PermissionService;
import org.springframework.stereotype.Service;
/**
 * (Permission)表服务实现类
 *
 * @author makejava
 * @since 2022-12-03 21:01:22
 */
@Service("permissionService")
public class PermissionServiceImpl extends ServiceImpl<PermissionMapper, Permission> implements PermissionService {
}
