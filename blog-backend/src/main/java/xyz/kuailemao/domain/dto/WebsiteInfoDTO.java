package xyz.kuailemao.domain.dto;

import lombok.Data;
import xyz.kuailemao.domain.BaseData;

import java.util.Date;

/**
 * @author kuailemao
 * <p>
 * 创建时间：2024/1/3 15:05
 */
@Data
public class WebsiteInfoDTO implements BaseData {
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
}
