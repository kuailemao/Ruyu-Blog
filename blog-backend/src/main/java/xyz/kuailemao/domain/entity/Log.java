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
 * (Log)表实体类
 *
 * @author kuailemao
 * @since 2023-12-12 09:12:31
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@TableName(value = "sys_log")
public class Log implements Serializable, BaseData {
    @Serial
    private static final long serialVersionUID = 1L;

    //编号
    private Long id;
    //模块名称
    private String module;
    //操作
    private String operation;
    //操作人员
    private String userName;
    //ip地址
    private String ip;
    //操作地点
    private String address;
    //操作状态(0：成功，1：失败，2：异常)
    private Integer state;
    //操作方法
    private String method;
    // 请求方式
    private String reqMapping;
    //请求参数
    private String reqParameter;
    // 异常信息
    private String exception;
    //返回参数
    private String returnParameter;
    //请求地址
    private String reqAddress;
    //消耗时间(ms)
    private Long time;
    // 操作描述
    private String description;
    //创建时间
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;
    //更新时间
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;
    //是否删除（0：未删除，1：已删除）
    private Integer isDeleted;
}

