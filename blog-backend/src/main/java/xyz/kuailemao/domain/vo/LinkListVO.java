package xyz.kuailemao.domain.vo;

import lombok.Data;

import java.util.Date;

/**
 * @author kuailemao
 * <p>
 * 创建时间：2024/1/22 20:42
 */
@Data
public class LinkListVO {
    //友链表id
    private Long id;
    // 用户名称
    private String userName;
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
    private Date createTime;
    //审核状态（0：未通过，1：已通过）
    private Integer isCheck;
}
