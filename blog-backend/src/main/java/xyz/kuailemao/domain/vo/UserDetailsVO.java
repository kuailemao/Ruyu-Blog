package xyz.kuailemao.domain.vo;

import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @author kuailemao
 * <p>
 * 创建时间：2023/12/18 16:27
 */
@Data
public class UserDetailsVO {
    //用户id
    private Long id;
    //用户昵称
    private String nickname;
    //用户名
    private String username;
    // 用户角色
    private List<String> roles;
    //用户性别
    private Integer gender;
    //用户头像
    private String avatar;
    //个人简介
    private String intro;
    //用户邮箱
    private String email;
    //用户注册方式(1邮箱/姓名 2Gitee 3Github)
    private Integer registerType;
    // 用户注册ip
    private String registerIp;
    // 用户注册地址
    private String registerAddress;
    //用户登录方式(1邮箱/姓名 2QQ 3Gitee 4Github)
    private Integer loginType;
    // 用户登录ip
    private String loginIp;
    // 登录地址
    private String loginAddress;
    //是否禁用 0 否 1 是
    private Integer isDisable;
    //用户最近登录时间
    private Date loginTime;
    //用户创建时间
    private Date createTime;
    //用户更新时间
    private Date updateTime;
}
