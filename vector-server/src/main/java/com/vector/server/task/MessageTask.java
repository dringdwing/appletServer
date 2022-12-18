package com.vector.server.task;

import com.rabbitmq.client.*;
import com.vector.server.domain.pojo.MessageEntity;
import com.vector.server.domain.pojo.MessageRefEntity;
import com.vector.server.exception.AppleServerException;
import com.vector.server.service.MessageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * @description: 消息任务类
 * @Title: MessageTask
 * @Package com.vector.server.task
 * @Author 芝士汉堡
 * @Date 2022/12/14 14:06
 */
@Component
@Slf4j
public class MessageTask {
    @Resource
    private ConnectionFactory connectionFactory;

    @Resource
    private MessageService messageService;

    public void send(String topic, MessageEntity entity) {
        String id = messageService.insertMessage(entity);
        try (Connection connection = connectionFactory.newConnection();
             Channel channel = connection.createChannel()) {
            channel.queueDeclare(topic, true, false, false, null);  // durable:是否持久化 autoDelete:是否自动删除 exclusive:是否排他 queueArgs:队列参数
            HashMap map = new HashMap<>();
            map.put("messageId", id);
            AMQP.BasicProperties build = new AMQP.BasicProperties().builder().headers(map).build(); // 设置消息头 用于接收端获取消息id 用于更新消息状态
            channel.basicPublish("", topic, build, entity.getMsg().getBytes()); // exchange:交换机 routingKey:路由键 props:消息属性 body:消息体
            log.debug("消息发送成功");
        } catch (Exception e) {
            log.error("消息发送失败", e);
            throw new AppleServerException("向MQ消息发送失败");
        }
    }

    @Async // 异步执行 用于更新消息状态
    public void sendAsync(String topic, MessageEntity entity) {
        send(topic, entity);
    }

    public int receive(String topic) {
        int i = 0;
        try (Connection connection = connectionFactory.newConnection();
             Channel channel = connection.createChannel();) {
            channel.queueDeclare(topic, true, false, false, null);
            while (true) {
                GetResponse response = channel.basicGet(topic, false);
                if (response != null) {
                    AMQP.BasicProperties properties = response.getProps();
                    Map<String, Object> map = properties.getHeaders();
                    String messageId = map.get("messageId").toString();
                    byte[] body = response.getBody();
                    String message = new String(body);
                    log.debug("从RabbitMQ接收的消息：" + message);

                    MessageRefEntity entity = new MessageRefEntity();
                    entity.setMessageId(messageId);
                    entity.setReceiverId(Integer.parseInt(topic));
                    entity.setReadFlag(false);
                    entity.setLastFlag(true);
                    messageService.insertRef(entity);
                    long deliveryTag = response.getEnvelope().getDeliveryTag();
                    channel.basicAck(deliveryTag, false);
                    i++;
                } else {
                    break;
                }
            }
        } catch (Exception e) {
            log.error("执行异常", e);
            throw new AppleServerException("接收消息失败");
        }
        return i;
    }

    @Async
    public int receiveAsync(String topic) {
        return receive(topic);
    }

    public void deleteQueue(String topic) {
        try (Connection connection = connectionFactory.newConnection();
             Channel channel = connection.createChannel();) {
            channel.queueDelete(topic);
            log.debug("消息队列成功删除");
        } catch (Exception e) {
            log.error("删除队列失败", e);
            throw new AppleServerException("删除队列失败");
        }
    }

    @Async
    public void deleteQueueAsync(String topic) {
        deleteQueue(topic);
    }
}
