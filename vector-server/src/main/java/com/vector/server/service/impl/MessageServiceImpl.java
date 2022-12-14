package com.vector.server.service.impl;

import com.vector.server.domain.pojo.MessageEntity;
import com.vector.server.domain.pojo.MessageRefEntity;
import com.vector.server.mapper.MessageDao;
import com.vector.server.mapper.MessageRefDao;
import com.vector.server.service.MessageService;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;

/**
 * @description:
 * @Title: MessageServiceImpl
 * @Package com.vector.server.service.impl
 * @Author 芝士汉堡
 * @Date 2022/12/14 11:57
 */
public class MessageServiceImpl implements MessageService {

    @Resource
    private MessageDao messageDao;

    @Resource
    private MessageRefDao messageRefDao;

    /**
     * 向Message集合插入数据
     *
     * @param entity
     * @return
     */
    @Override
    public String insertMessage(MessageEntity entity) {
        return messageDao.insert(entity);
    }

    /**
     * 向MessageRef集合插入数据
     *
     * @param entity
     * @return
     */
    @Override
    public String insertRef(MessageRefEntity entity) {
        return messageRefDao.insert(entity);
    }

    /**
     * 查询未读消息的数量
     *
     * @param userId
     * @return
     */
    @Override
    public Long searchUnreadCount(int userId) {
        return messageRefDao.searchUnreadCount(userId);
    }

    /**
     * 查询接收到最新消息的数量
     *
     * @param userId
     * @return
     */
    @Override
    public Long searchLastCount(int userId) {
        return messageRefDao.searchLastCount(userId);
    }

    /**
     * 查询分页数据
     *
     * @param userId
     * @param start
     * @param length
     * @return
     */
    @Override
    public List<HashMap> searchMessageByPage(int userId, long start, int length) {
        return messageDao.searchMessageByPage(userId, start, length);
    }

    /**
     * 根据userId查询未读消息
     *
     * @param userId
     * @return
     */
    @Override
    public HashMap searchMessageById(String userId) {
        return messageDao.searchMessageById(userId);
    }

    /**
     * 将消息未读状态标记为已读状态
     *
     * @param id
     * @return
     */
    @Override
    public Long updateUnreadMessage(String id) {
        return messageRefDao.updateUnreadMessage(id);
    }

    /**
     * 根据_Id删除消息
     *
     * @param id
     * @return
     */
    @Override
    public Long deleteMessageRefById(String id) {
        return messageRefDao.deleteMessageRefById(id);
    }

    /**
     * 根据userId删除消息
     *
     * @param userId
     * @return
     */
    @Override
    public Long deleteUserMessageRef(int userId) {
        return messageRefDao.deleteUserMessageRef(userId);
    }
}
