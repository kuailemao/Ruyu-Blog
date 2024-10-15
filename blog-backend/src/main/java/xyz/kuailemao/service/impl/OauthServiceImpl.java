package xyz.kuailemao.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import me.zhyd.oauth.model.AuthResponse;
import me.zhyd.oauth.model.AuthUser;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import xyz.kuailemao.domain.entity.User;
import xyz.kuailemao.mapper.UserMapper;
import xyz.kuailemao.service.IpService;
import xyz.kuailemao.service.OauthService;
import xyz.kuailemao.service.UserService;
import xyz.kuailemao.utils.AddressUtils;
import xyz.kuailemao.utils.IpUtils;

import java.util.Date;

/**
 * @author kuailemao
 * <p>
 * 创建时间：2023/12/21 17:09
 */
@Slf4j
@Service
public class OauthServiceImpl implements OauthService {

    @Resource
    private UserMapper userMapper;

    @Resource
    private UserService userService;

    @Resource
    private PasswordEncoder passwordEncoder;

    @Resource
    private IpService ipService;


    @Override
    public String handleLogin(AuthResponse authResponse, HttpServletRequest request, Integer type) {
        if (authResponse.getCode() == 2000) {
            AuthUser authUser = (AuthUser) authResponse.getData();
            // 第三方登录默认密码
            String enPassword = passwordEncoder.encode(authUser.getToken().getAccessToken());
            if (userMapper.selectCount(new LambdaQueryWrapper<User>().eq(User::getId, authUser.getUuid()).eq(User::getRegisterType, type)) == 0) {
                // 未注册
                String ipAddr = IpUtils.getIpAddr(request);

                User user = User.builder()
                        .id(Long.valueOf(authUser.getUuid()))
                        .username(authUser.getUsername())
                        .avatar(authUser.getAvatar())
                        .nickname(authUser.getNickname())
                        .password(enPassword)
                        .email(authUser.getEmail())
                        .registerType(type)
                        .registerIp(ipAddr)
                        .loginType(type)
                        .loginIp(ipAddr)
                        .loginTime(new Date())
                        .build();
                if (userService.save(user)) {
                    ipService.refreshIpDetailAsyncByUidAndRegister(user.getId());
                }
            }
            User user = User.builder().id(Long.valueOf(authUser.getUuid())).password(enPassword).build();
            userMapper.updateById(user);
            switch (type) {
                case 1:
                    return "?login_type=gitee&access_token=" + authUser.getToken().getAccessToken() + "&user_name=" + authUser.getUsername();
                case 2:
                    return "?login_type=github&access_token=" + authUser.getToken().getAccessToken() + "&user_name=" + authUser.getUsername();
            }
        } else {
            return authResponse.getMsg();
        }
        return null;
    }
}
