package xyz.kuailemao.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

/**
 * @author kuailemao
 * <p>
 * 创建时间：2023/10/16 16:43
 * 用户注册DTO
 */
@Data
public class UserRegisterDTO {
    // 用户名
    @Schema(description = "用户名")
    @Pattern(regexp = "^[a-zA-Z0-9\\u4e00-\\u9fa5]+$")
    @Length(min = 1, max = 10)
    private String username;
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
