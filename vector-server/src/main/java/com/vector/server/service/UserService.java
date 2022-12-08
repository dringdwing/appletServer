package com.vector.server.service;
import com.baomidou.mybatisplus.extension.service.IService;
import com.vector.server.domain.entity.User;

import java.util.Set;

/**
 * 用户表(User)表服务接⼝
 *
 * @author makejava
 * @since 2022-12-03 20:55:39
 */
public interface UserService extends IService<User> {
    /**
     * 注册超级管理员
     * @param registerCode
     * @param code
     * @param nickname
     * @param photo
     * @return
     */
    public int registerUser(String registerCode,String code,String nickname,String photo);

    /**
     * 根据用户ID查询用户权限
     * @param userId
     * @return
     */
    public Set<String> searchUserPermissions(int userId);
}
