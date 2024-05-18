package xyz.kuailemao.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

/**
 * @author kuailemao
 * <p>
 * 创建时间：2023/12/27 14:20
 * 文件上传枚举
 */
@Getter
@AllArgsConstructor
public enum UploadEnum {

    // 站长头像
    WEBSITE_INFO_AVATAR("websiteInfo/avatar/", "站长头像", List.of("jpg", "jpeg", "png")),
    // 站长背景
    WEBSITE_INFO_BACKGROUND("websiteInfo/background/", "站长背景", List.of("jpg", "jpeg", "png")),
    // 文章封面
    ARTICLE_COVER("article/articleCover/", "文章封面", List.of("jpg", "jpeg", "png")),
    // 文章图片
    ARTICLE_IMAGE("article/articleImage/", "文章图片", List.of("jpg", "jpeg", "png", "gif")),
    // 用户头像
    USER_AVATAR("user/avatar/", "用户头像", List.of("jpg", "jpeg", "png"));


    // 上传目录
    private final String dir;

    // 描述
    private final String description;

    // 支持的格式
    private final List<String> format;
}
