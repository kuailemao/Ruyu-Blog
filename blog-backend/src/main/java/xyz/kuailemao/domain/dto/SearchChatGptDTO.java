package xyz.kuailemao.domain.dto;

import lombok.Data;

/**
 * @author kuailemao
 * <p>
 * 创建时间：2024/1/21 10:50
 */
@Data
public class SearchChatGptDTO {
    //聊天会话的用户姓名
    private String userName;
    // 是否有效 (0否 1是)
    private Integer isCheck;
    // 开始时间
    private String startTime;
    // 结束时间
    private String endTime;
}
