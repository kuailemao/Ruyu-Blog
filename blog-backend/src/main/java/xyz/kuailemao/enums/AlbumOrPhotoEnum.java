package xyz.kuailemao.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author kuailemao
 * @since 2025/1/25 13:19
 * @description 相册枚举
 */
@Getter
@AllArgsConstructor
public enum AlbumOrPhotoEnum {
    ALBUM(1, "相册"),
    PHOTO(2, "照片");

    private final Integer code;
    private final String desc;
}
