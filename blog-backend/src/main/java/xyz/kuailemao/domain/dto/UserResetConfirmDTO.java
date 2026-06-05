package xyz.kuailemao.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

/**
 * @author kuailemao
 * <p>
 * 修改人：timefiy
 * 创建时间：2023/10/17 15:09
 * 修改时间：2026/4/12
 * 用户重置密码确认DTO
 */
@Data
public class UserResetConfirmDTO {
    // 验证码
    @Schema(description = "验证码")
    @NotBlank(message = "验证码不能为空")
    @Pattern(regexp = "^\\d{6}$", message = "验证码必须是6个数字")
    private String code;
    // 邮箱
    @Schema(description = "邮箱")
    @NotBlank(message = "邮箱不能为空")
    @Email(message = "邮箱格式不正确")
    @Length(min = 4, message = "邮箱长度过短")
    private String email;
}
