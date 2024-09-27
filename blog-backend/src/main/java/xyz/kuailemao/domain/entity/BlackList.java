package xyz.kuailemao.domain.entity;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import xyz.kuailemao.domain.BaseData;
import xyz.kuailemao.domain.ip.BlackListIpInfo;


/**
 * (BlackList)表实体类
 *
 * @author kuailemao
 * @since 2024-09-05 16:13:19
 */
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "t_black_list", autoResultMap = true)
public class BlackList implements BaseData{
    //表id
    @TableId(type = IdType.AUTO)
    private Long id;
    //用户id
    private Long userId;
    //封禁理由
    private String reason;
    //封禁时间
    @TableField(fill = FieldFill.INSERT)
    private Date bannedTime;
    //到期时间
    private Date expiresTime;
    // 类型（1：用户，2：路人/攻击者）
    private Integer type;
    // ip信息，如果type=2，则需要有ip信息
    @TableField(value = "ip_info",typeHandler = JacksonTypeHandler.class)
    private BlackListIpInfo ipInfo;
    //创建时间
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;
    //更新时间
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;
    //是否删除（0：未删除，1：已删除）默认：0
    private Integer isDeleted;
}

