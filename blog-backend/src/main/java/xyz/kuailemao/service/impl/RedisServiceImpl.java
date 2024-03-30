package xyz.kuailemao.service.impl;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import xyz.kuailemao.constants.RedisConst;
import xyz.kuailemao.mapper.ArticleMapper;
import xyz.kuailemao.service.RedisService;
import xyz.kuailemao.utils.RedisCache;

import java.util.Collection;
/**
 * @author kuailemao
 * <p>
 * 创建时间：2023/10/22 15:18
 */
@Slf4j
@Service
public class RedisServiceImpl implements RedisService {

    @Resource
    private RedisCache redisCache;

    @Resource
    private ArticleMapper articleMapper;

    @Override
    public void articleCountClear() {
        log.info("--------执行清除redis文章相关数量缓存--------");
        boolean isDel = redisCache.deleteObject(RedisConst.ARTICLE_FAVORITE_COUNT);
        isDel = isDel && redisCache.deleteObject(RedisConst.ARTICLE_LIKE_COUNT);
        isDel = isDel && redisCache.deleteObject(RedisConst.ARTICLE_COMMENT_COUNT);
        if (isDel) log.info("--------清除redis文章相关数量缓存成功--------");
        else log.info("--------清除redis文章相关数量缓存失败--------");
    }

    @Override
    public void articleVisitCount() {
        try {
            articleMapper.selectList(null).forEach(article -> redisCache.setCacheObject(RedisConst.ARTICLE_VISIT_COUNT + article.getId(), article.getVisitCount()));
            log.info("--------执行redis文章访问量缓存成功--------");
        } catch (Exception e) {
            log.error("--------执行redis文章访问量缓存失败", e);
        }
    }

    @Override
    public void clearLimitCache() {
        log.info("--------执行清除redis限流缓存--------:");
        try {
            Collection<String> keys = redisCache.keys("limit*");
            if (redisCache.deleteObject(keys) > 0) log.info("--------清除redis限流缓存成功--------");
            else log.info("--------没有redis限流缓存，无法清除--------");
        } catch (Exception e) {
            log.error("--------执行清除redis限流缓存失败", e);
        }

    }

}
