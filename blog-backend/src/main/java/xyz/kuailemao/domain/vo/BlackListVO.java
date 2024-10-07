package xyz.kuailemao.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import xyz.kuailemao.domain.ip.BlackListIpInfo;

import java.util.Date;

/**
 * @author kuailemao
 * @since 2024/9/18 下午6:04
 */
@Data
@Schema(name = "BlackListVO", description = "黑名单列表VO")
public class BlackListVO {
    //表id
    @Schema(description = "表id")
    private Long id;
    // 用户名称
    @Schema(description = "用户名称")
    private String userName;
    //封禁理由
    @Schema(description = "封禁理由")
    private String reason;
    // 自动封禁、ip信息
    @Schema(description = "自动封禁、ip信息")
    private BlackListIpInfo ipInfo;
    // 封禁类型
    @Schema(description = "封禁类型")
    private Integer type;
    //封禁时间
    @Schema(description = "封禁时间")
    private Date bannedTime;
    //到期时间
    @Schema(description = "到期时间")
    private Date expiresTime;
    //更新时间
    @Schema(description = "更新时间")
    private Date updateTime;
}
