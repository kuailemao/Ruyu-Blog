package xyz.kuailemao.domain.dto;

import lombok.Data;
import org.hibernate.validator.constraints.Length;
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
    @Length(max = 30, message = "网站名称字数不能超过30")
    private String websiteName;
    //头部通知
    @Length(max = 100, message = "头部通知字数不能超过100")
    private String headerNotification;
    //侧面公告
    @Length(max = 1000, message = "侧面公告字数不能超过1000")
    private String sidebarAnnouncement;
    //备案信息
    @Length(max = 100, message = "备案信息字数不能超过100")
    private String recordInfo;
    //开始运行时间
    private Date startTime;
}
