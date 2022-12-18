package com.vector.server.service.impl;

import com.vector.server.domain.pojo.MessageEntity;
import com.vector.server.domain.pojo.MessageRefEntity;
import com.vector.server.mapper.MessageDao;
import com.vector.server.mapper.MessageRefDao;
import com.vector.server.service.MessageService;
import org.springframework.stereotype.Service;

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
@Service("messageService")
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
        String id = messageDao.insert(entity);
        return id;
    }

    /**
     * 向MessageRef集合插入数据
     *
     * @param entity
     * @return
     */
    @Override
    public String insertRef(MessageRefEntity entity) {
        String id = messageRefDao.insert(entity);
        return id;
    }

    /**
     * 查询未读消息的数量
     *
     * @param userId
     * @return
     */
    @Override
    public Long searchUnreadCount(int userId) {
        long count = messageRefDao.searchUnreadCount(userId);
        return count;
    }

    /**
     * 查询接收到最新消息的数量
     *
     * @param userId
     * @return
     */
    @Override
    public Long searchLastCount(int userId) {
        long count = messageRefDao.searchLastCount(userId);
        return count;
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
        List<HashMap> list = messageDao.searchMessageByPage(userId, start, length);
        return list;
    }

    /**
     * 根据userId查询未读消息
     *
     * @param userId
     * @return
     */
    @Override
    public HashMap searchMessageById(String userId) {
        HashMap map = messageDao.searchMessageById(userId);
        return map;
    }

    /**
     * 将消息未读状态标记为已读状态
     *
     * @param id
     * @return
     */
    @Override
    public Long updateUnreadMessage(String id) {
        long rows = messageRefDao.updateUnreadMessage(id);
        return rows;
    }

    /**
     * 根据_Id删除消息
     *
     * @param id
     * @return
     */
    @Override
    public Long deleteMessageRefById(String id) {
        long rows = messageRefDao.deleteMessageRefById(id);
        return rows;
    }

    /**
     * 根据userId删除消息
     *
     * @param userId
     * @return
     */
    @Override
    public Long deleteUserMessageRef(int userId) {
        long rows = messageRefDao.deleteUserMessageRef(userId);
        return rows;
    }
}
