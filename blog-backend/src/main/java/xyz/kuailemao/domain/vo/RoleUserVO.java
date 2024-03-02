package xyz.kuailemao.domain.vo;

import lombok.Data;

import java.util.Date;

/**
 * @author kuailemao
 * <p>
 * 创建时间：2023/12/4 17:06
 */
@Data
public class RoleUserVO {
    //用户id
    private Long id;
    //用户昵称
    private String nickname;
    //用户名
    private String username;
    //用户邮箱
    private String email;
    //是否禁用 0 否 1 是
    private Integer isDisable;
    //用户创建时间
    private Date createTime;
}
