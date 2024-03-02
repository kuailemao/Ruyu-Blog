package xyz.kuailemao.domain.dto;

import jakarta.validation.constraints.Email;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import xyz.kuailemao.domain.BaseData;

/**
 * @author kuailemao
 * <p>
 * 创建时间：2023/11/14 8:52
 */
@Data
public class LinkDTO implements BaseData {
    //网站名称
    @Length(max = 15, message = "网站名称不能超过15个字符")
    private String name;
    //网站地址
    @Length(max = 50, message = "网站地址不能超过50个字符")
    private String url;
    //网站描述
    @Length(max = 30, message = "网站描述不能超过30个字符")
    private String description;
    //网站背景
    @Length(max = 100, message = "网站背景不能超过100个字符")
    private String background;
    //邮箱地址
    @Email
    @Length(max = 20, message = "邮箱地址不能超过20个字符")
    private String email;
}
