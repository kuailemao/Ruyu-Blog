package xyz.kuailemao.domain.dto;

import lombok.Data;

/**
 * @author kuailemao
 * <p>
 * 创建时间：2024/1/21 9:24
 */
@Data
public class SearchFavoriteDTO {
    //收藏的用户姓名
    private String userName;
    //收藏类型(1,文章 2,留言板)
    private Integer type;
    // 是否有效 (0否 1是)
    private Integer isCheck;
    // 开始时间
    private String startTime;
    // 结束时间
    private String endTime;
}
