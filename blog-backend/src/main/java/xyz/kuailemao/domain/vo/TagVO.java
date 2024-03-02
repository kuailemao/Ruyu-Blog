package xyz.kuailemao.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Date;


/**
 * @author kuailemao
 * <p>
 * 创建时间：2023/10/29 21:01
 */
@Data
@Schema(name = "TagVO", description = "标签VO")
public class TagVO {
    //标签id
    private Long id;
    //标签名称
    private String tagName;
    // 标签下的文章
    private Long articleCount;
    // 标签创建时间
    private Date createTime;
    // 标签更新时间
    private Date updateTime;
}
