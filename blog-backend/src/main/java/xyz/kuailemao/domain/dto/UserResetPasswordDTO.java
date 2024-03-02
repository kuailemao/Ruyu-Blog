package xyz.kuailemao.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

/**
 * @author kuailemao
 * <p>
 * 创建时间：2023/10/17 13:54
 * 用户重置密码DTO
 */
@Data
public class UserResetPasswordDTO {
    // 密码
    @Schema(description = "密码")
    @Length(min = 6, max = 20)
    private String password;
    //验证码
    @Schema(description = "验证码")
    @Length(max = 6, min = 6)
    private String code;
    // 邮箱
    @Schema(description = "邮箱")
    @Email
    @Length(min = 4)
    private String email;

}
