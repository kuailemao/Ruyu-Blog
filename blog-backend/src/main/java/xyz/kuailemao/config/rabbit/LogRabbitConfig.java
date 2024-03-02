package xyz.kuailemao.config.rabbit;

import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author kuailemao
 * <p>
 * 创建时间：2023/12/11 9:21
 */
@Configuration
public class LogRabbitConfig {
    /**
     * 创建日志队列
     */
    @Value("${spring.rabbitmq.queue.log-login}")
    public String LOG_QUEUE;

    /**
     * 创建日志队列
     */
    @Value("${spring.rabbitmq.queue.log-system}")
    public String SYSTEM_QUEUE;

    /**
     * 创建日志交换机
     */
    @Value("${spring.rabbitmq.exchange.log}")
    public String LOG_EXCHANGE;

    /**
     * 登录日志路由键
     */
    @Value("${spring.rabbitmq.routingKey.log-login}")
    public String LOG_ROUTING_KEY_LOGIN;

    /**
     * 系统操作日志路由键
     */
    @Value("${spring.rabbitmq.routingKey.log-system}")
    public String LOG_ROUTING_KEY_SYSTEM;

    /**
     * 定义交换机
     */
    @Bean
    public DirectExchange logExchange() {
        return ExchangeBuilder.directExchange(LOG_EXCHANGE).durable(true).build();
    }

    /**
     * 声明队列
     */
    @Bean
    public Queue loginQueue() {
        return QueueBuilder.durable(LOG_QUEUE).build();
    }

    /**
     * 绑定队列跟交换机(登录日志)
     */
    @Bean
    public Binding loginBinding(DirectExchange logExchange, Queue loginQueue) {
        return BindingBuilder.bind(loginQueue).to(logExchange).with(LOG_ROUTING_KEY_LOGIN);
    }

    /**
     * 声明队列
     */
    @Bean
    public Queue systemQueue() {
        return QueueBuilder.durable(SYSTEM_QUEUE).build();
    }

    /**
     * 绑定队列跟交换机(系统操作日志)
     */
    @Bean
    public Binding systemBinding(DirectExchange logExchange, Queue systemQueue) {
        return BindingBuilder.bind(systemQueue).to(logExchange).with(LOG_ROUTING_KEY_SYSTEM);
    }
}
