package xyz.kuailemao.domain.entity;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import xyz.kuailemao.domain.BaseData;


/**
 * (User)表实体类
 *
 * @author kuailemao
 * @since 2023-10-13 15:02:46
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Accessors(chain = true)
@TableName("sys_user")
public class User implements BaseData {
    //用户id
    private Long id;
    //用户昵称
    private String nickname;
    //用户名
    private String username;
    //用户密码
    private String password;
    //用户性别(0,未定义,1,男,2女)
    private Integer gender;
    //用户头像
    private String avatar;
    //个人简介
    private String intro;
    //用户邮箱
    private String email;
    //用户注册方式(0邮箱/姓名 1Gitee 2Github)
    private Integer registerType;
    // 用户注册ip
    private String registerIp;
    // 用户注册地址
    private String registerAddress;
    //用户登录方式(0邮箱/姓名 1Gitee 2Github)
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
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;
    //用户更新时间
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;
    //是否删除（0：未删除，1：已删除）
    private Integer isDeleted;
}

