package xyz.kuailemao.constants;

/**
 * @author kuailemao
 * <p>
 * 创建时间：2023/10/16 0:01
 */
public class SQLConst {
    /**
     * 推荐文章的字段标识
     */
    public static final String RECOMMEND_ARTICLE = "1";

    /**
     * 随机文章数量
     */
    public static final Integer RANDOM_ARTICLE_COUNT = 5;

    /**
     * 相关文章数量
     */
    public static final Integer RELATED_ARTICLE_COUNT = 5;

    /**
     * 公开文章的字段标识
     */
    public static final Integer PUBLIC_ARTICLE = 1;

    /**
     * 公开
     */
    public static final Integer STATUS_PUBLIC = 1;

    /**
     * 评论是否通过(0,否)
     */
    public static final Integer COMMENT_IS_CHECK = 1;

    /**
     * 是否通过(0,否)
     */
    public static final String IS_CHECK = "is_check";

    /**
     * 通过
     */
    public static final Integer IS_CHECK_YES = 1;

    /**
     * 创建时间
     */
    public static final String CREATE_TIME = "create_time";

    /**
     * id
     */
    public static final String ID = "id";

    /**
     * 管理员id
     */
    public static final Long ADMIN_ID = 1L;

    /**
     * 查询一条
      */
    public static final String LIMIT_ONE_SQL = "LIMIT 1";

    /**
     * Banner最多数量
     */
    public static final Integer BANNER_MAX_COUNT = 5;

    /**
     * 优先显示相册，再显示照片，时间倒序
     */
    public static final String ORDER_BY_CREATE_TIME_DESC = "ORDER BY CASE WHEN type = 1 THEN 0 ELSE 1 END, create_time DESC";
}
