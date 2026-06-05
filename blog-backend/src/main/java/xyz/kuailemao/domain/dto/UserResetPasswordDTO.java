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
 * 创建时间：2023/10/17 13:54
 * 修改时间：2026/4/12
 * 用户重置密码DTO
 */
@Data
public class UserResetPasswordDTO {
    // 密码
    @Schema(description = "密码")
    @NotBlank(message = "密码不能为空")
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)\\S{8,20}$", message = "密码必须包含字母和数字，长度在8到20个字符之间，且不能包含空格")
    private String password;
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
