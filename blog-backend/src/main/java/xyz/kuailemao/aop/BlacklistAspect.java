package xyz.kuailemao.aop;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import xyz.kuailemao.domain.response.ResponseResult;
import xyz.kuailemao.enums.RespEnum;
import xyz.kuailemao.mapper.BlackListMapper;
import xyz.kuailemao.service.BlackListService;
import xyz.kuailemao.utils.SecurityUtils;

/**
 * @author kuailemao
 * @since 2024/9/5 下午10:11
 * 黑名单拦截处理 aop
 * <p>
 * 封禁：
 * 1.禁止点赞、收藏、评论
 * 2.禁止留言
 * 3.禁止发送树洞
 * 4.禁止申请友链
 */
@Slf4j
@Aspect
@Component
public class BlacklistAspect {

    @Resource
    private BlackListService blackListService;

    @Resource
    private BlackListMapper blackListMapper;

    // 切点
    @Pointcut("@annotation(xyz.kuailemao.annotation.CheckBlacklist)")
    public void pt() {
    }

    @Around("pt()")
    public Object  checkBlacklist(ProceedingJoinPoint pjp) throws Throwable {
        if (blackListService.isUserInBlackList(SecurityUtils.getUserId())) {
            // 抛出异常
            String reason = blackListMapper.selectById(SecurityUtils.getUserId()).getReason();
            log.error("黑名单用户:{}({})", SecurityContextHolder.getContext().getAuthentication().getName(), reason);
            return ResponseResult.failure(RespEnum.BLACK_LIST_ERROR.getCode(), reason);
        }
        return pjp.proceed();
    }

}
