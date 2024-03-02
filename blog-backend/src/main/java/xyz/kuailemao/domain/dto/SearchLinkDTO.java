package xyz.kuailemao.domain.dto;

import lombok.Data;

/**
 * @author kuailemao
 * <p>
 * 创建时间：2024/1/22 20:44
 */
@Data
public class SearchLinkDTO {
    // 用户名称
    private String userName;
    //网站名称
    private String name;
    //是否通过 (0否 1是)
    private Integer isCheck;
    // 开始时间
    private String startTime;
    // 结束时间
    private String endTime;
}
