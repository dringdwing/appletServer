package com.vector.server.mapper;

import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import com.vector.server.domain.pojo.MessageRefEntity;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;

/**
 * @description:
 * @Title: MessageRefDao
 * @Package com.vector.server.mapper
 * @Author 芝士汉堡
 * @Date 2022/12/14 11:04
 */
@Repository
public class MessageRefDao {
    @Resource
    private MongoTemplate mongoTemplate;

    /**
     * 保存消息
     *
     * @param entity
     * @return
     */
    public String insert(MessageRefEntity entity) {
        entity = mongoTemplate.save(entity);
        return entity.get_id();
    }

    /**
     * 查询未读消息的数量
     *
     * @param userId
     * @return
     */
    public long searchUnreadCount(int userId) {
        Query query = new Query().addCriteria(Criteria
                .where("receiverId")
                .is(userId)
                .and("readFlag")
                .is(false));
        return mongoTemplate.count(query, MessageRefEntity.class);
    }

    /**
     *查询接收到最新消息的数量
     * @param userId
     * @return
     */
    public long searchLastCount(int userId) {
        Query query = new Query().addCriteria(Criteria
                .where("lastFlag")
                .is(true)
                .and("receiverId")
                .is(userId));
        Update update = new Update().set("lastFlag", false); // 1.将最后一条消息标记为false
        UpdateResult result = mongoTemplate.updateMulti(query, update, "message_ref"); // 2.更新
        long rows = result.getModifiedCount(); // 3.获取更新的行数
        return rows;
    }


    /**
     * 更新消息状态将ref消息未读变成已读
     * @param id
     * @return
     */
    public long updateUnreadMessage(String id) {
        Query query = new Query().addCriteria(Criteria
                .where("_id")
                .is(id));
        Update update = new Update().set("readFlag", true);
        UpdateResult result = mongoTemplate.updateFirst(query, update, "message_ref");
        long rows = result.getModifiedCount();
        return rows;
    }

    //根据主键值删除消息记录
    public long deleteMessageRefById(String id) {
        Query query = new Query().addCriteria(Criteria
                .where("_id")
                .is(id));
        DeleteResult result = mongoTemplate.remove(query, "message_ref");
        long rows = result.getDeletedCount();
        return rows;
    }


    /**
     * 根据userId删除用户所有的消息
     * @param userId
     * @return
     */
    public long deleteUserMessageRef(int userId) {
        Query query = new Query().addCriteria(Criteria
                .where("receiverId")
                .is(userId));
        DeleteResult result = mongoTemplate.remove(query, "message_ref");
        long rows = result.getDeletedCount();
        return rows;
    }
}
