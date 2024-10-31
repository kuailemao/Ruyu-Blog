package xyz.kuailemao.service;

import com.baomidou.mybatisplus.extension.service.IService;
import jakarta.validation.constraints.NotEmpty;
import org.springframework.web.multipart.MultipartFile;
import xyz.kuailemao.domain.dto.ArticleDTO;
import xyz.kuailemao.domain.dto.SearchArticleDTO;
import xyz.kuailemao.domain.entity.Article;
import xyz.kuailemao.domain.response.ResponseResult;
import xyz.kuailemao.domain.vo.*;

import java.util.List;


/**
 * (Article)表服务接口
 *
 * @author kuailemao
 * @since 2023-10-15 02:29:13
 */
public interface ArticleService extends IService<Article> {
    /**
     * 查询所有文章列表
     */
    PageVO<List<ArticleVO>> listAllArticle(Integer pageNum, Integer pageSize);
    /**
     * 查询推荐文章
     */
    List<RecommendArticleVO> listRecommendArticle();

    /**
     * 随机文章接口
     */
    List<RandomArticleVO> listRandomArticle();

    /**
     * 获取文章详情
     * @param id 文章id
     * @return 文章相关数据
     */
    ArticleDetailVO getArticleDetail(Integer id);

    /**
     * 相关文章信息
     *
     * @param categoryId 文章分类id
     * @param articleId 文章id
     * @return 文章相关数据
     */
    List<RelatedArticleVO> relatedArticleList(Integer categoryId, Integer articleId);

    /**
     * 查询时间轴数据
     */
    List<TimeLineVO> listTimeLine();

    /**
     * 查询分类或标签下的文章
     *
     * @param type   类型
     * @param typeId 类型id
     * @return vo
     */
    List<CategoryArticleVO> listCategoryArticle(Integer type, Long typeId);

    /**
     * 增加文章访问量
     * @param id 文章id
     */
    void addVisitCount(Long id);

    /**
     * 上传文章封面
     *
     * @param articleCover 文章封面
     * @return 是否成功
     */
    ResponseResult<String> uploadArticleCover(MultipartFile articleCover);

    /**
     * 发布文章
     *
     * @param articleDTO 文章信息
     * @return 是否成功
     */
    ResponseResult<Void> publish(ArticleDTO articleDTO);

    /**
     * 发布错误删除封面
     * @param articleCoverUrl 文章封面
     * @return 是否成功
     */
    ResponseResult<Void> deleteArticleCover(String articleCoverUrl);

    /**
     * 文章图片上传
     * @param articleImage 文章图片
     * @return url
     */
    ResponseResult<String> uploadArticleImage(MultipartFile articleImage);

    /**
     * 后台文章列表
     * @return 文章列表
     */
    List<ArticleListVO> listArticle();

    /**
     * 搜索文章列表
     * @param searchArticleDTO 搜索条件
     * @return 结果
     */
    List<ArticleListVO> searchArticle(SearchArticleDTO searchArticleDTO);

    /**
     * 修改文章状态
     * @param id 文章id
     * @param status 状态
     * @return 是否成功
     */
    ResponseResult<Void> updateStatus(Long id, Integer status);

    /**
     * 修改文章是否顶置
     * @param id 文章id
     * @param isTop 是否顶置
     * @return 是否成功
     */
    ResponseResult<Void> updateIsTop(Long id, Integer isTop);

    /**
     * 回显文章数据
     * @param id 文章id
     * @return 数据
     */
    ArticleDTO getArticleDTO(Long id);

    /**
     * 删除文章
     * @param id 文章id
     * @return 是否成功
     */
    ResponseResult<Void> deleteArticle(List<Long> id);

    /**
     * 初始化搜索文章标题
     * @return 文章数据
     */
    List<InitSearchTitleVO> initSearchByTitle();

    /**
     * 热门推荐
     * @return 热门推荐前5
     */
    List<HotArticleVO> listHotArticle();

    /**
     * 根据内容搜索文章
     * @param content 文章内容
     * @return 文章
     */
    List<SearchArticleByContentVO> searchArticleByContent(String content);
}
