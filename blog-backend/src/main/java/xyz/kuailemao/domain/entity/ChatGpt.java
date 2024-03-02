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
 * (ChatGpt)表实体类
 *
 * @author kuailemao
 * @since 2023-11-11 12:01:59
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@TableName("t_chat_gpt")
public class ChatGpt implements BaseData {
    //id
    private Long id;
    //用户id
    private Long userId;
    //会话记录
    private String conversation;
    // 是否有效
    private Integer isCheck;
    //创建时间
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;
    //修改时间
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;
    //是否删除（0：未删除，1：已删除）
    private Integer isDeleted;
}

