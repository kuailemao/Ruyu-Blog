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
}
