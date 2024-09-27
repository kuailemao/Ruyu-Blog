package xyz.kuailemao.service;

/**
 * @author kuailemao
 * <p>
 * 创建时间：2023/10/22 15:17
 */
public interface RedisService {

    void articleCountClear();

    void articleVisitCount();

    void clearLimitCache();

    /**
     * 初始化收藏量，点赞量，评论量
     */
    void initCount();

    /**
     * 初始化黑名单
     */
    void initBlackList();
}
