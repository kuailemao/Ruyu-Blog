package xyz.kuailemao.domain.dto;

import lombok.Data;

/**
 * @author kuailemao
 * <p>
 * 创建时间：2024/1/22 8:27
 */
@Data
public class SearchCommentDTO {
    //评论用户的名称
    private String commentUserName;
    //评论的内容
    private String commentContent;
    //评论类型 (1文章 2留言板)
    private Integer type;
    //是否通过 (0否 1是)
    private Integer isCheck;
}
