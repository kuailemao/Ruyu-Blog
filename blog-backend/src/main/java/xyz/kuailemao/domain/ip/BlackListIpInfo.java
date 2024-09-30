package xyz.kuailemao.domain.ip;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author kuailemao
 * @since 2024/9/25 上午10:23
 * 黑名单ip
 */
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class BlackListIpInfo {
    // 黑名单的ip
    private String createIp;
    //添加时的ip详情
    private IpDetail ipDetail;
}
