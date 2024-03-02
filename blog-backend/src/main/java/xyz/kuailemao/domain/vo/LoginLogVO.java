package xyz.kuailemao.domain.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;


/**
 * (LoginLog)表实体类
 *
 * @author kuailemao
 * @since 2023-12-08 14:38:43
 */
@Data
public class LoginLogVO implements Serializable {
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
    //用户登录时间
    private Date loginTime;
}

