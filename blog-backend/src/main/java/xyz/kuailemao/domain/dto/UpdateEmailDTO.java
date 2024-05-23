package xyz.kuailemao.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Data
public class UpdateEmailDTO {
    //验证码
    @Schema(description = "验证码")
    @NotEmpty(message = "验证码不能为空")
    private String code;

    @Schema(description = "邮箱")
    @Email
    @Length(min = 4)
    private String email;

    // 密码
    @Schema(description = "密码")
    @NotEmpty(message = "密码不能为空")
    private String password;
}
