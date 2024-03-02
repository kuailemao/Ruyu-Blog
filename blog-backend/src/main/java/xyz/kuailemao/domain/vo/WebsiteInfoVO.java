package xyz.kuailemao.domain.vo;

import lombok.Data;

import java.util.Date;

/**
 * @author kuailemao
 * <p>
 * 创建时间：2023/12/27 17:16
 */
@Data
public class WebsiteInfoVO {
    //站长头像
    private String webmasterAvatar;
    //站长名称
    private String webmasterName;
    //站长文案
    private String webmasterCopy;
    //站长资料卡背景图
    private String webmasterProfileBackground;
    //gitee链接
    private String giteeLink;
    //github链接
    private String githubLink;
    //网站名称
    private String websiteName;
    //头部通知
    private String headerNotification;
    //侧面公告
    private String sidebarAnnouncement;
    //备案信息
    private String recordInfo;
    //开始运行时间
    private Date startTime;
    // 文章的最后更新
    private Date lastUpdateTime;
    // 文章数目
    private Long articleCount;
    // 分类数
    private Long categoryCount;
    // 评论数
    private Long commentCount;
    // 全站字数
    private Long wordCount;
    // 访问次数
    private Long visitCount;

}
