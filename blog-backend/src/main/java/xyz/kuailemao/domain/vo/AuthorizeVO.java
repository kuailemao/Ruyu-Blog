package xyz.kuailemao.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import xyz.kuailemao.domain.BaseData;

import java.util.Date;
import java.util.List;

/**
 * @author kuailemao
 * <p>
 * 创建时间：2023/10/11 16:29
 * 授权VO
 */
@Data
@Schema(name = "AuthorizeVO", description = "授权VO")
public class AuthorizeVO {
    // token
    @Schema(description = "token")
    private String token;
    // token 过期时间
    @Schema(description = "token 过期时间")
    private Date expire;
}
