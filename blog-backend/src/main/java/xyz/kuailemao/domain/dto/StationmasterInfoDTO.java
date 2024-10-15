package xyz.kuailemao.domain.dto;

import lombok.Data;
import org.hibernate.validator.constraints.Length;
import xyz.kuailemao.domain.BaseData;

/**
 * @author kuailemao
 * <p>
 * 创建时间：2024/1/3 14:48
 */
@Data
public class StationmasterInfoDTO implements BaseData {
    //站长名称
    @Length(max = 30, message = "站长名称字数不能超过30")
    private String webmasterName;
    //站长文案
    @Length(max = 100, message = "站长文案字数不能超过100")
    private String webmasterCopy;
    //gitee链接
    @Length(max = 100, message = "gitee链接字数不能超过100")
    private String giteeLink;
    //github链接
    @Length(max = 100, message = "github链接字数不能超过100")
    private String githubLink;
}
