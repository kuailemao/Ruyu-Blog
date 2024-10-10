package xyz.kuailemao.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @author kuailemao
 * @since 2024/9/5 下午4:23
 */
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateBlackListDTO {
    // id
    @Schema(description = "id")
    @NotNull(message = "id不能为空")
    private Long id;

    //封禁理由
    @Schema(description = "封禁理由")
    @NotBlank(message = "封禁理由不能为空")
    private String reason;

    //到期时间
    @Schema(description = "封禁到期时间")
    @NotNull(message = "封禁到期时间不能为空")
//    @Future(message = "封禁到期时间必须大于当前时间")
    private Date expiresTime;
}
