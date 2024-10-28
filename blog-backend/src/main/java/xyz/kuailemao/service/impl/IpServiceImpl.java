package xyz.kuailemao.service.impl;

import cn.hutool.core.lang.TypeReference;
import cn.hutool.core.thread.NamedThreadFactory;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONUtil;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.stereotype.Service;
import xyz.kuailemao.constants.ThirdPartyInterfaceConst;
import xyz.kuailemao.domain.dto.IpResult;
import xyz.kuailemao.domain.entity.BlackList;
import xyz.kuailemao.domain.entity.Log;
import xyz.kuailemao.domain.entity.LoginLog;
import xyz.kuailemao.domain.entity.User;
import xyz.kuailemao.domain.ip.IpDetail;
import xyz.kuailemao.handler.GlobalUncaughtExceptionHandler;
import xyz.kuailemao.mapper.BlackListMapper;
import xyz.kuailemao.mapper.LogMapper;
import xyz.kuailemao.mapper.LoginLogMapper;
import xyz.kuailemao.mapper.UserMapper;
import xyz.kuailemao.service.IpService;

import java.util.Date;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author kuailemao
 * @since 2024/9/25 上午11:06
 * ip 处理类
 */
@Slf4j
@Service
public class IpServiceImpl implements IpService, DisposableBean {

    private static final ExecutorService EXECUTOR = new ThreadPoolExecutor(1, 1,
            0L, TimeUnit.MILLISECONDS,
            new LinkedBlockingQueue<>(500),
            new NamedThreadFactory("refresh-ipDetail", null, false,
                    GlobalUncaughtExceptionHandler.getInstance()));

    @Resource
    private BlackListMapper blackListMapper;

    @Resource
    private UserMapper userMapper;

    @Resource
    private LoginLogMapper loginLogMapper;

    @Resource
    private LogMapper logMapper;

    /**
     * 异步刷新ip详情获取
     *
     * @param bid 黑名单id
     */
    @Override
    public void refreshIpDetailAsyncByBid(Long bid) {
        EXECUTOR.execute(() -> {
            BlackList blackList = blackListMapper.selectById(bid);
            if (Objects.isNull(blackList)) {
                return;
            }
            String ip = blackList.getIpInfo().getCreateIp();
            if (StrUtil.isBlank(ip)) {
                return;
            }
            IpDetail ipDetail = TryGetIpDetailOrNullTreeTimes(ip);
            if (Objects.nonNull(ipDetail)) {
                blackList.getIpInfo().setIpDetail(ipDetail);
                blackListMapper.updateById(blackList);
            } else {
                log.error("get ip detail fail ip:{},bid:{}", ip, bid);
            }
        });
    }

    /**
     * 异步刷新注册ip详情获取
     *
     * @param uid 用户id
     */
    @Override
    public void refreshIpDetailAsyncByUidAndRegister(Long uid) {
        EXECUTOR.execute(() -> {
            User user = userMapper.selectById(uid);
            if (Objects.isNull(user)) {
                return;
            }
            String ip = user.getRegisterIp();
            if (StrUtil.isBlank(ip)) {
                return;
            }
            IpDetail ipDetail = TryGetIpDetailOrNullTreeTimes(ip);
            if (Objects.nonNull(ipDetail)) {
                user.setRegisterAddress(buildAddr(ipDetail.getRegion(), ipDetail.getCity(), ipDetail.getCountry()));
            } else {
                user.setRegisterAddress("未知");
                log.error("register get ip detail fail ip:{},uid:{}", ip, uid);
            }
            userMapper.updateById(user);
        });
    }

    /**
     * 异步刷新登录ip详情获取
     *
     * @param uid 用户id
     */
    @Override
    public void refreshIpDetailAsyncByUidAndLogin(Long uid) {
        EXECUTOR.execute(() -> {
            User user = userMapper.selectById(uid);
            if (Objects.isNull(user)) {
                return;
            }
            String ip = user.getLoginIp();
            if (StrUtil.isBlank(ip)) {
                return;
            }
            IpDetail ipDetail = TryGetIpDetailOrNullTreeTimes(ip);
            if (Objects.nonNull(ipDetail)) {
                user.setLoginAddress(buildAddr(ipDetail.getRegion(), ipDetail.getCity(), ipDetail.getCountry()));
            } else {
                user.setRegisterAddress("未知");
                log.error("login get ip detail fail ip:{},uid:{}", ip, uid);
            }
            userMapper.updateById(user);
        });
    }

    /**
     * 异步刷新登录日志ip详情获取
     *
     * @param loginLogId 登录日志id
     */
    @Override
    public void refreshIpDetailAsyncByLogIdAndLogin(Long loginLogId) {
        EXECUTOR.execute(() -> {
            LoginLog loginLog = loginLogMapper.selectById(loginLogId);
            if (Objects.isNull(loginLog)) {
                return;
            }
            String ip = loginLog.getIp();
            if (StrUtil.isBlank(ip)) {
                return;
            }
            IpDetail ipDetail = TryGetIpDetailOrNullTreeTimes(ip);
            if (Objects.nonNull(ipDetail)) {
                loginLog.setAddress(buildAddr(ipDetail.getRegion(), ipDetail.getCity(), ipDetail.getCountry()));
            } else {
                loginLog.setAddress("未知");
                log.error("loginLog get ip detail fail ip:{},loginLogId:{}", ip, loginLogId);
            }
            loginLogMapper.updateById(loginLog);
        });
    }

    /**
     * 异步刷新操作日志ip详情获取
     *
     * @param logId 操作日志id
     */
    @Override
    public void refreshIpDetailAsyncByLogId(Long logId) {
        EXECUTOR.execute(() -> {
            Log log = logMapper.selectById(logId);
            if (Objects.isNull(log)) {
                return;
            }
            String ip = log.getIp();
            if (StrUtil.isBlank(ip)) {
                return;
            }
            IpDetail ipDetail = TryGetIpDetailOrNullTreeTimes(ip);
            if (Objects.nonNull(ipDetail)) {
                log.setAddress(buildAddr(ipDetail.getRegion(), ipDetail.getCity(), ipDetail.getCountry()));
            } else {
                log.setAddress("未知");
                IpServiceImpl.log.error("log get ip detail fail ip:{},log:{}", ip, log);
            }
            logMapper.updateById(log);
        });
    }

    /**
     * 构建地址
     * @param region 区域
     * @param city 城市
     * @param country 国家
     * @return 地址
     */
    private String buildAddr(String region, String city, String country) {

        if ("内网IP".equals(city)) {
            return "内网IP";
        }

        if (!"中国".equals(country)) {
            return country;
        }

        if ("XX".equals(region) && "XX".equals(city)) {
            return "未知";
        }

        if ("XX".equals(region)){
            return city;
        }

        if ("XX".equals(city)){
            return region;
        }

        if (region.equals(city)) {
            return region;
        }

        return region + " " + city;
    }


    //测试耗时结果 100次查询总耗时约100s，平均一次成功查询需要1s,可以接受
    //第99次成功,目前耗时：111281ms
    public static void main(String[] args) {
        Date begin = new Date();
        for (int i = 0; i < 100; i++) {
            int finalI = i;
            EXECUTOR.execute(() -> {
                IpDetail ipDetail = TryGetIpDetailOrNullTreeTimes("");
                if (Objects.nonNull(ipDetail)) {
                    Date date = new Date();
                    System.out.printf("第%d次成功,目前耗时：%dms%n", finalI, (date.getTime() - begin.getTime()));
                    log.info(ipDetail.toString());
                }
            });
        }
        System.out.println(StrUtil.format(ThirdPartyInterfaceConst.TAOBAO_IP_DETAIL, "1433223"));
    }

    private static IpDetail TryGetIpDetailOrNullTreeTimes(String ip) {
        for (int i = 0; i < 3; i++) {
            IpDetail ipDetail = getIpDetailOrNull(ip);
            if (Objects.nonNull(ipDetail)) {
                return ipDetail;
            }
            log.info("进行重试：{}", i);
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public static IpDetail getIpDetailOrNull(String ip) {
        String body = HttpUtil.get(StrUtil.format(ThirdPartyInterfaceConst.TAOBAO_IP_DETAIL, ip));
        try {
            IpResult<IpDetail> result = JSONUtil.toBean(body, new TypeReference<IpResult<IpDetail>>() {
            }, false);
            if (result.isSuccess()) {
                return result.getData();
            }
        } catch (Exception ignored) {
        }
        return null;
    }

    @Override
    public void destroy() throws InterruptedException {
        EXECUTOR.shutdown();
        if (!EXECUTOR.awaitTermination(30, TimeUnit.SECONDS)) {//最多等30秒，处理不完就拉倒
            if (log.isErrorEnabled()) {
                log.error("Timed out while waiting for executor [{}] to terminate", EXECUTOR);
            }
        }
    }
}
