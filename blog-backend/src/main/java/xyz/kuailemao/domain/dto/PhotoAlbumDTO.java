package xyz.kuailemao.domain.dto;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author kuailemao
 * @since 2025/1/25 12:55
 * 创建相册DTO
 */
@Data
public class PhotoAlbumDTO {
    private Long parentId;
    private String name;
    private String description;
}
