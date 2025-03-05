package xyz.kuailemao.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import xyz.kuailemao.constants.ImageConst;

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
    WEBSITE_INFO_AVATAR("websiteInfo/avatar/", "站长头像", List.of(ImageConst.JPG, ImageConst.JPEG, ImageConst.PNG, ImageConst.WEBP), 0.3),
    // 站长背景
    WEBSITE_INFO_BACKGROUND("websiteInfo/background/", "站长背景", List.of(ImageConst.JPG, ImageConst.JPEG, ImageConst.PNG, ImageConst.WEBP), 0.3),
    // 文章封面
    ARTICLE_COVER("article/articleCover/", "文章封面", List.of(ImageConst.JPG, ImageConst.JPEG, ImageConst.PNG, ImageConst.WEBP), 0.3),
    // 文章图片
    ARTICLE_IMAGE("article/articleImage/", "文章图片", List.of(ImageConst.JPG, ImageConst.JPEG, ImageConst.PNG, ImageConst.GIF, ImageConst.WEBP), 3.0),
    // 用户头像
    USER_AVATAR("user/avatar/", "用户头像", List.of(ImageConst.JPG, ImageConst.JPEG, ImageConst.PNG, ImageConst.WEBP), 0.3),
    // 前台首页Banners图片
    UI_BANNERS("banners/", "前台首页Banners图片", List.of(ImageConst.JPG, ImageConst.JPEG, ImageConst.PNG, ImageConst.WEBP), 0.3),
    // 相册模块图片
    PHOTO_ALBUM("photoAlbum/", "相册模块图片", List.of(ImageConst.JPG, ImageConst.JPEG, ImageConst.PNG, ImageConst.WEBP, ImageConst.GIF), 4.0);


    // 上传目录
    private final String dir;

    // 描述
    private final String description;

    // 支持的格式
    private final List<String> format;

    // 文件最大大小 单位：MB
    private final Double limitSize;
}
