package xyz.kuailemao.domain.vo;

import lombok.Data;

import java.util.Date;

/**
 * @author kuailemao
 * <p>
 * 创建时间：2024/1/11 22:43
 * 后台留言列表VO
 */
@Data
public class LeaveWordListVO {
    //id
    private Long id;
    // 留言用户名称
    private String userName;
    //留言内容
    private String content;
    //是否通过 (0否 1是)
    private Integer isCheck;
    //留言时间
    private Date createTime;
    //更新时间
    private Date updateTime;
}
