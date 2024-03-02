package xyz.kuailemao.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author kuailemao
 * <p>
 * 创建时间：2023/11/6 11:11
 */
@Getter
@AllArgsConstructor
public enum LikeEnum {

    LIKE_TYPE_ARTICLE(1, "点赞：文章"),
    LIKE_TYPE_COMMENT(2, "点赞：评论"),
    LIKE_TYPE_LEAVE_WORD(3, "点赞：留言");

    // 类型
    private final Integer type;
    // 描述
    private final String desc;
}
