package xyz.kuailemao.domain.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.Date;

/**
 * @author kuailemao
 * <p>
 * 创建时间：2023/12/13 15:19
 */
@Data
public class LogDTO {
    //ip地址
    private String ip;
    //模块名称
    private String module;
    //操作人员
    private String userName;
    //操作类型
    private String operation;
    //操作状态(0：成功，1：失败)
    private Integer state;
    // 操作时间开始
    private Date logTimeStart;
    // 操作时间结束
    private Date logTimeEnd;
    // 当前页
    @NotNull
    private Long current;

    // 每页数量
    @NotNull
    private Long pageSize;
}
