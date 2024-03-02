package xyz.kuailemao.domain.entity;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import xyz.kuailemao.domain.BaseData;


/**
 * (LoginLog)表实体类
 *
 * @author kuailemao
 * @since 2023-12-08 14:38:43
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName("sys_login_log")
public class LoginLog implements Serializable , BaseData {
    @Serial
    private static final long serialVersionUID = 1L;
    //日志编号
    private Long id;
    //用户名称
    private String userName;
    //登录ip
    private String ip;
    //登录地址
    private String address;
    //浏览器
    private String browser;
    //操作系统
    private String os;
    // 登录类型(0：前台，1：后台，2：非法登录)
    private Integer type;
    //登录状态(0：成功，1：失败)
    private Integer state;
    //登录信息
    private String message;
    //用户创建时间
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;
    //用户更新时间
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;
    //是否删除（0：未删除，1：已删除）
    private Integer isDeleted;
}

