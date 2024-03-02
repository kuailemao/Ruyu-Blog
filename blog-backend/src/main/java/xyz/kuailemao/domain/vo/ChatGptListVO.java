package xyz.kuailemao.domain.vo;

import lombok.Data;

import java.util.Date;

/**
 * @author kuailemao
 * <p>
 * 创建时间：2024/1/21 10:47
 */
@Data
public class ChatGptListVO {
    //id
    private Long id;
    //用户名称
    private String userName;
    // 用户头像
    private String avatar;
    //会话记录
    private String conversation;
    // 是否有效
    private Integer isCheck;
    //创建时间
    private Date createTime;
    //修改时间
    private Date updateTime;
}
