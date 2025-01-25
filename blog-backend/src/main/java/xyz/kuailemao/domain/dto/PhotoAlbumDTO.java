package xyz.kuailemao.domain.dto;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

/**
 * @author kuailemao
 * @since 2025/1/25 12:55
 * 创建相册DTO
 */
@Data
public class PhotoAlbumDTO {

    private Long parentId;

    @NotEmpty(message = "相册名称不能为空")
    @Size(max = 10, message = "相册名称不能超过10个字符")
    private String name;

    private String description;
}
