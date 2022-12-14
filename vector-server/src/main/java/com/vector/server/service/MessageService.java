package com.vector.server.service;

import com.vector.server.domain.pojo.MessageEntity;
import com.vector.server.domain.pojo.MessageRefEntity;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;

/**
 * @description:
 * @Title: MessageService
 * @Package com.vector.server.service
 * @Author 芝士汉堡
 * @Date 2022/12/14 11:44
 */
@Service
public interface MessageService {
    /**
     * 向Message集合插入数据
     * @param entity
     * @return
     */
    public String insertMessage(MessageEntity entity);

    /**
     * 向MessageRef集合插入数据
     * @param entity
     * @return
     */
    public String insertRef(MessageRefEntity entity);

    /**
     * 查询未读消息的数量
     * @param userId
     * @return
     */
    public Long searchUnreadCount(int userId);

    /**
     * 查询接收到最新消息的数量
     * @param userId
     * @return
     */
    public Long searchLastCount(int userId);

    /**
     * 查询分页数据
     * @param userId
     * @param start
     * @param length
     * @return
     */
    public List<HashMap> searchMessageByPage(int userId, long start, int length);

    /**
     * 根据userId查询未读消息
     * @param userId
     * @return
     */
    public HashMap searchMessageById(String userId);

    /**
     * 将消息未读状态标记为已读状态
     * @param id
     * @return
     */
    public Long updateUnreadMessage(String id);

    /**
     * 根据_Id删除消息
     * @param id
     * @return
     */
    public Long deleteMessageRefById(String id);

    /**
     * 根据userId删除消息
     * @param userId
     * @return
     */
    public Long deleteUserMessageRef(int  userId);
}
