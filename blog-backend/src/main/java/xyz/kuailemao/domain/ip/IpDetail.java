package xyz.kuailemao.domain.ip;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author kuailemao
 * @since 2024/9/25 上午10:23
 * ip 详情
 */
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class IpDetail {
    //注册时的ip
    private String ip;
    //最新登录的ip
    private String isp;
    private String isp_id;
    private String city;
    private String city_id;
    private String country;
    private String country_id;
    private String region;
    private String region_id;
}
