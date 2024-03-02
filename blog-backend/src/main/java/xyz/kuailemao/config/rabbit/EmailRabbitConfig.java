package xyz.kuailemao.config.rabbit;

import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author kuailemao
 * <p>
 * 创建时间：2023/10/16 20:26
 * RabbitMq配置类
 */
@Configuration
public class EmailRabbitConfig {
    /**
     * 创建邮件队列
     */
    @Value("${spring.rabbitmq.queue.email}")
    public String MAIL_QUEUE;
    /**
     * 创建邮件交换机
     */
    @Value("${spring.rabbitmq.exchange.email}")
    public String MAIL_EXCHANGE;
    /**
     * 创建邮件路由键
     */
    @Value("${spring.rabbitmq.routingKey.email}")
    public String MAIL_ROUTING_KEY;

    /**
     * 定义交换机
     */
    @Bean
    public DirectExchange mailExchange() {
        return ExchangeBuilder.directExchange(MAIL_EXCHANGE).durable(true).build();
    }

    /**
     * 声明队列
     */
    @Bean
    public Queue mailQueue() {
        return QueueBuilder.durable(MAIL_QUEUE).build();
    }

    /**
     * 绑定队列跟交换机
     */
    @Bean
    public Binding mailBinding(DirectExchange mailExchange,Queue mailQueue) {
        return BindingBuilder.bind(mailQueue).to(mailExchange).with(MAIL_ROUTING_KEY);
    }

}
