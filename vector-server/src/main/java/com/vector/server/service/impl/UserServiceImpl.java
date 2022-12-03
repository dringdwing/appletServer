package com.vector.server.service.impl;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.vector.server.entity.User;
import com.vector.server.mapper.UserMapper;
import com.vector.server.service.UserService;
import org.springframework.stereotype.Service;
/**
 * 用户表(User)表服务实现类
 *
 * @author makejava
 * @since 2022-12-03 21:01:22
 */
@Service("userService")
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
}
