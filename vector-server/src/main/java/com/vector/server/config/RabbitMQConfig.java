package com.vector.server.config;

import com.rabbitmq.client.ConnectionFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @description:
 * @Title: RabbitMQConfig
 * @Package com.vector.server.config
 * @Author 芝士汉堡
 * @Date 2022/12/14 13:26
 */
@Configuration
public class RabbitMQConfig {
    @Bean
    public ConnectionFactory getConnectionFactory(){
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setHost("175.178.216.62");
        connectionFactory.setPort(5672);
        return connectionFactory;
    }
}
