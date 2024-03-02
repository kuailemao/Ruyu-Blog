package xyz.kuailemao.domain.entity;

import com.alibaba.fastjson.annotation.JSONField;
import jakarta.annotation.Resource;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import xyz.kuailemao.utils.RedisCache;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author kuailemao
 * <p>
 * 创建时间：2023/10/11 16:08
 */
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
@Data
public class LoginUser implements UserDetails {

    @Resource
    private RedisCache redisCache;

    private User user;

    //存储权限信息
    private List<String> permissions;

    //存储SpringSecurity所需要的权限信息的集合
    @JSONField(serialize = false)
    private List<SimpleGrantedAuthority> authorities;

    public LoginUser(User user) {
        this.user = user;
    }

    public LoginUser(User user, List<String> permissions) {
        this.user = user;
        this.permissions = permissions;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if (Objects.nonNull(authorities)) return authorities;
        // 没有的话，转换
        authorities = permissions.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList());
        return authorities;
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getUsername();
    }

    /**
     * 账号是否过期
     * @return
     */
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    /**
     * 用户是否锁定
     * @return
     */
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    /**
     * 用户凭证是否过期
     * @return
     */
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    /**
     * 账户是否可用
     * @return
     */
    @Override
    public boolean isEnabled() {
        return true;
    }
}
