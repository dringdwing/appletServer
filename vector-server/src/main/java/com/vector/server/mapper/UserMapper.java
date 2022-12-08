package com.vector.server.mapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.vector.server.domain.entity.User;

import java.util.HashMap;
import java.util.Set;

/**
 * 用户表(User)表数据库访问层
 *
 * @author makejava
 * @since 2022-12-03 20:55:39
 */
public interface UserMapper extends BaseMapper<User> {

    /**
     * 检查用户表的root字段 值为1则是超级管理员
     * @return
     */
    public boolean haveRootUser();

    /**
     * 保存用户信息
     * @param param
     * @return
     */
    public int insertUser(HashMap param);

    /**
     * 根据Openld查询用户ID
     * @param openId
     * @return
     */
    public String searchIdByOpenId(String openId);

    /**
     * 根据用户ID查询用户权限
     * @param userId
     * @return
     */
    public Set<String> searchUserPermissions(int userId);
}

