package xyz.kuailemao.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.kuailemao.constants.Const;
import xyz.kuailemao.domain.dto.LoginLogDeleteDTO;
import xyz.kuailemao.domain.dto.LoginLogDTO;
import xyz.kuailemao.domain.entity.LoginLog;
import xyz.kuailemao.domain.response.ResponseResult;
import xyz.kuailemao.domain.vo.LoginLogVO;
import xyz.kuailemao.mapper.LoginLogMapper;
import xyz.kuailemao.service.LoginLogService;
import xyz.kuailemao.utils.AddressUtils;
import xyz.kuailemao.utils.BrowserUtil;
import xyz.kuailemao.utils.IpUtils;
import xyz.kuailemao.utils.StringUtils;

import java.util.List;
import java.util.Objects;

/**
 * (LoginLog)表服务实现类
 *
 * @author kuailemao
 * @since 2023-12-08 14:38:44
 */
@Slf4j
@Service("loginLogService")
public class LoginLogServiceImpl extends ServiceImpl<LoginLogMapper, LoginLog> implements LoginLogService {

    @Resource
    private LoginLogMapper loginLogMapper;

    @Resource
    private RabbitTemplate rabbitTemplate;

    @Value("${spring.rabbitmq.routingKey.log-login}")
    private String routingKey;

    @Value("${spring.rabbitmq.exchange.log}")
    private String exchange;

    @Override
    public void loginLog(HttpServletRequest request, String userName, Integer state, String message) {
        String browserName = BrowserUtil.browserName(request);
        String ipAddress = IpUtils.getIpAddr(request);
        String os = BrowserUtil.osName(request);
        int requestType;
        String typeHeader = request.getHeader(Const.TYPE_HEADER);
        if (StringUtils.isNotEmpty(typeHeader) && typeHeader.equals(Const.FRONTEND_REQUEST)) {
            requestType = 0;
        } else if (StringUtils.isNotEmpty(typeHeader) && typeHeader.equals(Const.BACKEND_REQUEST)) {
            requestType = 1;
        } else {
            requestType = 2;
        }
        if (userName == null) {
            userName = "未知用户";
        }
        LoginLog logEntity = LoginLog.builder()
                .userName(userName)
                .ip(ipAddress)
                .browser(browserName)
                .os(os)
                .type(requestType)
                .state(state)
                .message(message)
                .build();

        rabbitTemplate.convertAndSend(exchange, routingKey, logEntity);
        log.info("{}", "发送登录日志信息--rabbitMQ");
    }

    @Override
    public List<LoginLogVO> searchLoginLog(LoginLogDTO loginLogDTO) {
        LambdaQueryWrapper<LoginLog> wrapper = new LambdaQueryWrapper<>();
        if (Objects.nonNull(loginLogDTO)) {
            wrapper.like(StringUtils.isNotEmpty(loginLogDTO.getAddress()), LoginLog::getAddress, loginLogDTO.getAddress())
                    .like(StringUtils.isNotEmpty(loginLogDTO.getUserName()), LoginLog::getUserName, loginLogDTO.getUserName())
                    .eq(StringUtils.isNotNull(loginLogDTO.getState()), LoginLog::getState, loginLogDTO.getState());
            if (StringUtils.isNotNull(loginLogDTO.getLoginTimeStart()) && StringUtils.isNotNull(loginLogDTO.getLoginTimeEnd())) {
                wrapper.gt(LoginLog::getCreateTime, loginLogDTO.getLoginTimeStart()).and(a -> a.lt(LoginLog::getCreateTime, loginLogDTO.getLoginTimeEnd()));
            }
        }
        wrapper.orderByDesc(LoginLog::getCreateTime);
        return loginLogMapper.selectList(wrapper).stream().map(loginLog -> loginLog.asViewObject(LoginLogVO.class,v -> v.setLoginTime(loginLog.getCreateTime()))).toList();
    }

    @Transactional
    @Override
    public ResponseResult<Void> deleteLoginLog(LoginLogDeleteDTO loginLogDeleteDTO) {
        if (this.removeByIds(loginLogDeleteDTO.getIds())) {
            return ResponseResult.success();
        }
        return ResponseResult.failure();
    }
}
