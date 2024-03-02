package xyz.kuailemao.domain.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;


/**
 * (ArticleTag)表实体类
 *
 * @author kuailemao
 * @since 2023-10-15 02:29:13
 */
@SuppressWarnings("serial")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@TableName("t_article_tag")
public class ArticleTag {
    //关系表id
    private Long id;
    //文章id
    @TableId
    private Long articleId;
    //标签id
    private Long tagId;
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;
    //是否删除（0：未删除，1：已删除）
    private Integer isDeleted;
}

