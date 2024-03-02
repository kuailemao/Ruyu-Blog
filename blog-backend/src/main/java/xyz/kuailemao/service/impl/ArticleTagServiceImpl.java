package xyz.kuailemao.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import xyz.kuailemao.domain.entity.ArticleTag;
import xyz.kuailemao.mapper.ArticleTagMapper;
import xyz.kuailemao.service.ArticleTagService;

/**
 * (ArticleTag)表服务实现类
 *
 * @author kuailemao
 * @since 2023-10-15 02:29:13
 */
@Service("articleTagService")
public class ArticleTagServiceImpl extends ServiceImpl<ArticleTagMapper, ArticleTag> implements ArticleTagService {

}
