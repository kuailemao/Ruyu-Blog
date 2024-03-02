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
public enum FavoriteEnum {

    FAVORITE_TYPE_ARTICLE(1, "收藏：文章"),
    FAVORITE_TYPE_LEAVE_WORD(2, "收藏：留言");
    // 类型
    private final Integer type;
    // 描述
    private final String desc;
}
