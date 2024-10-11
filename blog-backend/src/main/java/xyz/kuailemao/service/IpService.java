package xyz.kuailemao.service;

/**
 * @author kuailemao
 * @since 2024/9/25 上午11:05
 */
public interface IpService {

    /**
     * 异步刷新ip详情获取
     * @param bid 黑名单id
     */
    void refreshIpDetailAsyncByBid(Long bid);

    /**
     * 异步刷新ip详情获取--注册
     * @param uid 用户id
     */
    void refreshIpDetailAsyncByUidAndRegister(Long uid);

    /**
     * 异步刷新ip详情获取--登录
     * @param uid 用户id
     */
    void refreshIpDetailAsyncByUidAndLogin(Long uid);
}
