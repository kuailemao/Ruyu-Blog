package xyz.kuailemao.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import xyz.kuailemao.constants.RedisConst;
import xyz.kuailemao.constants.SecurityConst;
import xyz.kuailemao.domain.entity.*;
import xyz.kuailemao.mapper.PermissionMapper;
import xyz.kuailemao.mapper.RoleMapper;
import xyz.kuailemao.mapper.RolePermissionMapper;
import xyz.kuailemao.mapper.UserRoleMapper;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @author kuailemao
 * <p>
 * 创建时间：2023/10/10 17:28
 * jwt 工具类
 */
@Component
public class JwtUtils {

    @Value("${spring.security.jwt.key}")
    private String key;

    @Value("${spring.security.jwt.expire}")
    private int expire;

    @Resource
    private RedisCache redisCache;


    /**
     * 是否成功让令牌失效
     *
     * @param headerToken 请求头中的token
     * @return boolean
     */
    public boolean invalidateJwt(String headerToken) {
        String token = this.convertToken(headerToken);
        if (token == null) return false;
        // 验证令牌
        Algorithm algorithm = Algorithm.HMAC256(key);
        JWTVerifier jwtVerifier = JWT.require(algorithm).build();
        try {
            DecodedJWT jwt = jwtVerifier.verify(token);
            String id = jwt.getId();
            return deleteToken(id);
        } catch (JWTVerificationException e) {
            return false;
        }
    }

    /**
     * 删除 token
     *
     * @param uuid 令牌的id
     * @return boolean
     */
    private boolean deleteToken(String uuid) {
        // token 是否失效
        if (this.isInvalidToken(uuid))
            return false;
        // 删除
        redisCache.deleteObject(RedisConst.JWT_WHITE_LIST + uuid);
        return true;
    }


    /**
     * 是否是一个过期的令牌
     *
     * @param uuid 令牌的id
     * @return boolean
     */
    private boolean isInvalidToken(String uuid) {
        // 判断是否在redis中(白名单)
        return !Boolean.TRUE.equals(redisCache.isHasKey(RedisConst.JWT_WHITE_LIST + uuid));
    }


    /**
     * 解析jwt
     *
     * @param headerToken 请求头中的token
     * @return {@link DecodedJWT} 解析后的jwt
     */
    public DecodedJWT resolveJwt(String headerToken) {
        String token = this.convertToken(headerToken);
        if (token == null) return null;
        Algorithm algorithm = Algorithm.HMAC256(key);
        JWTVerifier jwtVerifier = JWT.require(algorithm).build();
        try {
            // 是否合法，不合法会抛出一个运行时异常（需要自己捕获）
            DecodedJWT verify = jwtVerifier.verify(token);
            // 如果不在白名单中
            if (this.isInvalidToken(verify.getId()))
                return null;
            Date expiresAt = verify.getExpiresAt();
            // 判断是否过期
            return new Date().after(expiresAt) ? null : verify;
        } catch (JWTVerificationException e) {
            return null;
        }
    }


    /**
     * 创建 jwt
     *
     * @param details  用户信息
     * @param id       用户id
     * @param username 用户名
     * @return String jwt
     */
    public String createJwt(String uuid, UserDetails details, Long id, String username) {
        Algorithm algorithm = Algorithm.HMAC256(key);
        Date expire = expireTime();
        // 当前时间
        Date now = new Date();
        String jwt = JWT.create()
                .withJWTId(uuid)
                .withClaim("id", id)
                .withClaim("name", username)
//                .withClaim("authorities", details.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList())
                .withExpiresAt(expire)
                .withIssuedAt(now)
                .sign(algorithm);
        // 存入redis
        redisCache.setCacheObject(RedisConst.JWT_WHITE_LIST + uuid, jwt, (int) (expire.getTime() - now.getTime()), TimeUnit.MILLISECONDS);
        return jwt;
    }

    /**
     * 到期时间
     *
     * @return Date
     */
    public Date expireTime() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.HOUR, expire * 24);
        return calendar.getTime();
    }

    /**
     * 获取用户id
     *
     * @param jwt jwt
     * @return Integer
     */
    public Long toId(DecodedJWT jwt) {
        Map<String, Claim> claims = jwt.getClaims();
        return claims.get("id").asLong();
    }

    /**
     * 处理Token
     *
     * @param headerToken 请求头中的token
     * @return String
     */
    private String convertToken(String headerToken) {
        if (headerToken == null || !headerToken.startsWith("Bearer "))
            return null;
        return headerToken.substring(7);
    }

    public UserDetails toUser(DecodedJWT jwt) {
        Map<String, Claim> claims = jwt.getClaims();
        List<String> listStr = getAuthorities(toId(jwt));
        List<SimpleGrantedAuthority> collect = listStr.stream().map(SimpleGrantedAuthority::new).toList();

        return new LoginUser()
                .setUser
                        (
                                new User()
                                        .setUsername(claims.get("name").asString())
                                        .setId(toId(jwt))
                        )
                .setAuthorities(collect);
    }

    @Resource
    private UserRoleMapper userRoleMapper;
    @Resource
    private RoleMapper roleMapper;
    @Resource
    private RolePermissionMapper rolePermissionMapper;
    @Resource
    private PermissionMapper permissionMapper;

    // 查询jwt角色&权限
    private List<String> getAuthorities(Long userId) {
        // 查询用户角色
        List<UserRole> userRoles = userRoleMapper.selectList(new LambdaQueryWrapper<UserRole>().eq(UserRole::getUserId, userId));
        List<Role> roles = userRoles.stream().map(role -> roleMapper.selectById(role.getRoleId())).toList();
        if (roles.isEmpty()){
            return Collections.emptyList();
        }else{
            // 查询权限关系表
            List<RolePermission> rolePermissions = rolePermissionMapper.selectBatchIds(roles.stream().map(Role::getId).toList());
            // 查询角色权限
            List<Long> pIds = rolePermissions.stream().map(RolePermission::getPermissionId).toList();
            List<Permission> permissions = permissionMapper.selectBatchIds(pIds);
            // 组合角色，权限
            List<String> list = permissions.stream().map(Permission::getPermissionKey).collect(Collectors.toList());
            roles.forEach(role -> list.add(SecurityConst.ROLE_PREFIX + role.getRoleKey()));
            list.addAll(roles.stream().map(Role::getRoleKey).toList());
            return list;
        }
    }
}
