package xyz.kuailemao.domain.dto;

import lombok.Data;

/**
 * @author kuailemao
 * <p>
 * 创建时间：2024/1/18 14:38
 */
@Data
public class SearchTagDTO{
    //标签名称
    private String tagName;
    // 开始时间
    private String startTime;
    // 结束时间
    private String endTime;
}
