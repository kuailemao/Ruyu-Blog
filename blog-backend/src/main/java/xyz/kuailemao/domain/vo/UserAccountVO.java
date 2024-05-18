package xyz.kuailemao.domain.vo;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import xyz.kuailemao.domain.BaseData;

import java.util.Date;
import java.util.List;


/**
 * @author kuailemao
 * <p>
 * 创建时间：2023/10/14 16:31
 * 用户账户VO
 */
@Data
@Schema(name = "UserAccountVO", description = "前台用户账户VO")
public class UserAccountVO implements BaseData {
    //用户昵称
    @Schema(description = "用户昵称")
    private String nickname;
    //用户名
    @Schema(description = "用户名")
    private String username;
    @Schema(description = "用户类型")
    //用户注册方式(1邮箱/姓名 2Gitee 3Github)
    private Integer registerType;
    //用户头像
    @Schema(description = "用户头像")
    private String avatar;
    //个人简介
    @Schema(description = "个人简介")
    private String intro;
    //用户邮箱
    @Schema(description = "用户邮箱")
    private String email;
    //用户性别
    @Schema(description = "用户性别")
    private Integer gender;
    // 账号角色
    @Schema(description = "用户角色")
    private List<String> roles;
    // 账号权限
    @Schema(description = "用户权限")
    private List<String> permissions;
    //用户最近登录时间
    @Schema(description = "用户最近登录时间")
    private Date loginTime;
    //用户创建时间
    @Schema(description = "用户创建时间")
    private Date createTime;
}
