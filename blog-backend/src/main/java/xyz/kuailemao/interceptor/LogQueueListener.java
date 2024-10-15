package xyz.kuailemao.interceptor;

import com.rabbitmq.client.Channel;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;
import xyz.kuailemao.constants.RabbitConst;
import xyz.kuailemao.domain.entity.Log;
import xyz.kuailemao.domain.entity.LoginLog;
import xyz.kuailemao.mapper.LogMapper;
import xyz.kuailemao.mapper.LoginLogMapper;
import xyz.kuailemao.service.IpService;

import java.io.IOException;

/**
 * @author kuailemao
 * <p>
 * 创建时间：2023/12/11 10:01
 * 日志队列
 */
@Component
@Slf4j
public class LogQueueListener {

    @Resource
    private LoginLogMapper loginLogMapper;

    @Resource
    private LogMapper logMapper;

    @Resource
    private IpService ipService;

    /**
     * 监听登录日志队列
     */
    @RabbitListener(queues = RabbitConst.LOG_LOGIN_QUEUE,concurrency = "5-10")
    public void handlerLoginLog(LoginLog loginLog, Channel channel, @Header(AmqpHeaders.DELIVERY_TAG) long tag) {
        log.info("监听登录日志队列,标识:{},数据：{}", tag, loginLog);
        if (loginLog.getBrowser().startsWith("Unknown")) {
            loginLog.setBrowser("未知");
        }
        if (loginLog.getOs().startsWith("Unknown")) {
            loginLog.setOs("未知");
        }
        if (loginLog.getType() == null) {
            loginLog.setType(2);
        }
        if (loginLogMapper.insert(loginLog) > 0) {
            ipService.refreshIpDetailAsyncByLogIdAndLogin(loginLog.getId());
        }
        log.info("登录日志标识:{}，数据库添加成功", tag);
    }

    /**
     * 监听系统操作日志队列
     * 开启手动确认
     */
    @RabbitListener(queues = RabbitConst.LOG_SYSTEM_QUEUE,concurrency = "5-10")
    public void handlerSystemLog(Log logEntity) {
        log.info("--------------消费系统操作日志--------------");
        if (logMapper.insert(logEntity) > 0) {
            ipService.refreshIpDetailAsyncByLogId(logEntity.getId());
        }
        log.info("--------------系统操作日志插入数据库成功--------------");
    }
}
