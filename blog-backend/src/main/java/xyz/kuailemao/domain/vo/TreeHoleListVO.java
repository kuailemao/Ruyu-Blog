package xyz.kuailemao.domain.vo;

import lombok.Data;

import java.util.Date;

/**
 * @author kuailemao
 * <p>
 * 创建时间：2024/1/19 21:13
 */
@Data
public class TreeHoleListVO {
    //树洞表id
    private Long id;
    //用户名称
    private String userName;
    //内容
    private String content;
    // 是否通过
    private Integer isCheck;
    //创建时间
    private Date createTime;
    //修改时间
    private Date updateTime;
}
