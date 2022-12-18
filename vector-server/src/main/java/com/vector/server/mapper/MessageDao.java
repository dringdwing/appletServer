package com.vector.server.mapper;


import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateUtil;
import cn.hutool.json.JSONObject;
import com.vector.server.domain.pojo.MessageEntity;
import com.vector.server.domain.pojo.MessageRefEntity;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * @description:
 * @Title: MessageDao
 * @Package com.vector.server.mapper
 * @Author 芝士汉堡
 * @Date 2022/12/13 19:50
 */
@Repository
public class MessageDao {

    @Resource
    private MongoTemplate mongoTemplate;

    /**
     * 保存消息
     *
     * @param entity
     * @return
     */
    public String insert(MessageEntity entity) {
        Date sendTime = entity.getSendTime();
        sendTime = DateUtil.offset(sendTime, DateField.HOUR, 8);
        entity.setSendTime(sendTime);
        entity = mongoTemplate.save(entity);
        return entity.get_id();
    }

    /**
     * 查询分页数据
     *
     * @param userId
     * @param start
     * @param length
     * @return
     */
    public List<HashMap> searchMessageByPage(int userId, long start, int length) {
        JSONObject json = new JSONObject()
                .set("$toString", "$_id");
        Aggregation aggregation = Aggregation.newAggregation( // 1.先根据条件查询
                Aggregation.addFields().addField("id").withValue(json).build(), // 2.添加id字段
                Aggregation.lookup("message_ref", "id", "messageId", "ref"), // 3.关联查询
                Aggregation.match(Criteria.where("ref.receiverId").is(userId)), // 4.匹配条件
                Aggregation.sort(Sort.by(Sort.Direction.DESC, "sendTime")), // 5.排序
                Aggregation.skip(start), // 6.跳过
                Aggregation.limit(length) // 7.限制
        );
        AggregationResults<HashMap> result = mongoTemplate.aggregate(aggregation, "message", HashMap.class);
        List<HashMap> list = result.getMappedResults();
        list.forEach(one -> {
            List<MessageRefEntity> refList = (List<MessageRefEntity>) one.get("ref");
            MessageRefEntity entity = refList.get(0);
            boolean readFlag = entity.getReadFlag(); // 读取状态
            String refId = entity.get_id();
            one.put("readFlag", readFlag);
            one.put("refId", refId);
            one.remove("ref");
            one.remove("_id");
            Date sendTime = (Date) one.get("sendTime"); // 8.转换时间
            sendTime = DateUtil.offset(sendTime, DateField.HOUR, -8); // 时区转换 转为北京时间

            String today = DateUtil.today();
            if (today.equals(DateUtil.date(sendTime).toDateStr())) {
                one.put("sendTime", DateUtil.format(sendTime, "HH:mm"));
            } else {
                one.put("sendTime", DateUtil.format(sendTime, "yyyy/MM/dd"));
            }
        });

        return list;
    }

    /**
     * @param id
     * @return
     * @description: 根据id查询消息
     */
    public HashMap searchMessageById(String id) {
        HashMap map = mongoTemplate.findById(id, HashMap.class, "message");
        Date sendTime = (Date) map.get("sendTime");
        sendTime = DateUtil.offset(sendTime, DateField.HOUR, -8);
        map.replace("sendTime", DateUtil.format(sendTime, "yyyy-MM-dd HH:mm"));
        return map;
    }
}