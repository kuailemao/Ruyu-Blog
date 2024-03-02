package xyz.kuailemao.domain.dto;

import jakarta.validation.constraints.Pattern;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

/**
 * @author kuailemao
 * <p>
 * 创建时间：2023/11/10 11:16
 */
@Data
public class ContentDTO {

    // 类型限制
    @Pattern(regexp = "(gpt-3.5-turbo-0613|gpt-3.5-turbo-16k-0613|gpt-4|gpt-4-0613)")
    private String type;

    @Length(max = 1314)
    private String content;
}
