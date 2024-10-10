package xyz.kuailemao.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author kuailemao
 * @since 2024/10/8 11:10
 * 搜索DTO
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SearchBlackListDTO {
    // 用户名称
    private String userName;
    // 封禁理由
    private String reason;
    // 封禁类型
    private Integer type;
    // 开始时间
    private String startTime;
    // 结束时间
    private String endTime;
}
