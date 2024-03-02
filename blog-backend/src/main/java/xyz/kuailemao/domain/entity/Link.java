package xyz.kuailemao.domain.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import xyz.kuailemao.domain.BaseData;

import java.util.Date;


/**
 * (Link)表实体类
 *
 * @author kuailemao
 * @since 2023-11-14 08:43:34
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@TableName("t_link")
public class Link implements BaseData {
    //友链表id
    private Long id;
    // 用户id
    private Long userId;
    //网站名称
    private String name;
    //网站地址
    private String url;
    //网站描述
    private String description;
    //网站背景
    private String background;
    //邮箱地址
    private String email;
    //文章创建时间
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;
    //文章更新时间
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;
    //审核状态（0：未通过，1：已通过）
    private Integer isCheck;
    //是否删除（0：未删除，1：已删除）
    private Integer isDeleted;
}

