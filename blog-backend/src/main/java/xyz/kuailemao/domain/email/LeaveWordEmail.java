package xyz.kuailemao.domain.email;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @author kuailemao
 * @since 2024/8/11 下午5:50
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LeaveWordEmail {
    // 留言头像
    private String avatar;
    // 留言用户的昵称
    private String nickname;
    // 留言内容
    private String content;
    // 留言时间
    private String time;
    // 留言的地址
    private String url;
}
