package xyz.kuailemao.domain.vo;

import lombok.Data;

/**
 * @author kuailemao
 * <p>
 * 创建时间：2023/11/14 11:27
 */
@Data
public class LinkVO {
    //友链表id
    private Long id;
    //网站名称
    private String name;
    //网站地址
    private String url;
    //网站描述
    private String description;
    //网站背景
    private String background;
    // 用户头像
    private String avatar;
}
