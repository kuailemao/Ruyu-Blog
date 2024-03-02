package xyz.kuailemao.domain.dto;

import lombok.Data;

/**
 * @author kuailemao
 * <p>
 * 创建时间：2024/1/19 21:15
 */
@Data
public class SearchTreeHoleDTO {
    // 树洞用户
    private String userName;
    //是否通过 (0否 1是)
    private Integer isCheck;
    // 开始时间
    private String startTime;
    // 结束时间
    private String endTime;
}
