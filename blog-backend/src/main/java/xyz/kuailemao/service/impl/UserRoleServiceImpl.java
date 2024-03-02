package xyz.kuailemao.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import xyz.kuailemao.annotation.AccessLimit;
import xyz.kuailemao.annotation.LogAnnotation;
import xyz.kuailemao.constants.LogConst;
import xyz.kuailemao.domain.dto.RolePermissionDTO;
import xyz.kuailemao.domain.dto.RoleUserDTO;
import xyz.kuailemao.domain.dto.UserRoleDTO;
import xyz.kuailemao.domain.entity.Role;
import xyz.kuailemao.domain.entity.RolePermission;
import xyz.kuailemao.domain.entity.User;
import xyz.kuailemao.domain.entity.UserRole;
import xyz.kuailemao.domain.response.ResponseResult;
import xyz.kuailemao.domain.vo.RoleAllVO;
import xyz.kuailemao.domain.vo.RoleUserVO;
import xyz.kuailemao.mapper.RoleMapper;
import xyz.kuailemao.mapper.UserMapper;
import xyz.kuailemao.mapper.UserRoleMapper;
import xyz.kuailemao.service.UserRoleService;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * (UserRole)表服务实现类
 *
 * @author kuailemao
 * @since 2023-11-17 16:34:06
 */
@Service("userRoleService")
public class UserRoleServiceImpl extends ServiceImpl<UserRoleMapper, UserRole> implements UserRoleService {

    @Resource
    private UserRoleMapper userRoleMapper;

    @Resource
    private UserMapper userMapper;

    @Resource
    private RoleMapper roleMapper;

    public List<RoleUserVO> selectRoleUser(Long roleId, String username, String email,Integer type) {
        LambdaQueryWrapper<UserRole> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(UserRole::getRoleId,roleId);
        List<Long> userIds;
        if(type == 0){
            userIds = userRoleMapper.selectList(wrapper).stream().map(UserRole::getUserId).toList();
        }else{
            // 查询存在角色的用户id
            userIds = userRoleMapper.selectList(wrapper).stream().map(UserRole::getUserId).toList();
            // 查询没有该角色的用户
            userIds = userMapper.selectList(new LambdaQueryWrapper<User>().notIn(!userIds.isEmpty(),User::getId, userIds)).stream().map(User::getId).toList();
        }
        if (!userIds.isEmpty()) {
            LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
            List<User> userList = new ArrayList<>();
            userIds.forEach(userId -> {
                if (Objects.nonNull(username)) queryWrapper.like(User::getUsername, username);
                if (Objects.nonNull(email)) queryWrapper.and(a -> a.like(User::getEmail, email));
                queryWrapper.eq(User::getId, userId);
                userList.addAll(userMapper.selectList(queryWrapper));
                queryWrapper.clear();
            });
            return userList.stream().map(user -> user.asViewObject(RoleUserVO.class)).toList();
        }
        return null;
    }

    @Transactional
    @Override
    public ResponseResult<Void> addUserRole(UserRoleDTO userRoleDTO) {
        List<Long> userIds = userRoleDTO.getUserId();
        Long roleId = userRoleDTO.getRoleId();
        // 避免重复添加
        List<Long> notUserIds = new ArrayList<>();
        LambdaQueryWrapper<UserRole> countWrapper = new LambdaQueryWrapper<>();
        userIds.forEach(userId -> {
            if (userRoleMapper.selectCount(countWrapper.eq(UserRole::getUserId, userId).eq(UserRole::getRoleId, roleId)) == 0) {
                notUserIds.add(userId);
            }
            countWrapper.clear();
        });
        List<UserRole> userRoles = notUserIds.stream().map(userId -> UserRole.builder().userId(userId).roleId(roleId).build()).toList();
        if (this.saveBatch(userRoles)) return ResponseResult.success();
        return ResponseResult.failure();
    }

    @Override
    public ResponseResult<Void> deleteUserRole(UserRoleDTO userRoleDTO) {
        int isDelete = userRoleMapper.delete(new LambdaQueryWrapper<UserRole>().eq(UserRole::getRoleId, userRoleDTO.getRoleId()).in(UserRole::getUserId, userRoleDTO.getUserId()));
        if (isDelete > 0) {
            return ResponseResult.success();
        }
        return ResponseResult.failure();
    }

    @Override
    public List<RoleAllVO> selectRoleByUserId(Long userId, String roleName, String roleKey, Integer type) {
        LambdaQueryWrapper<UserRole> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(UserRole::getUserId, userId);
        List<Long> roleIds;
        if (type == 0) {
            // 没有使用该权限的角色
            roleIds = userRoleMapper.selectList(wrapper).stream().map(UserRole::getRoleId).toList();
        } else {
            // 查询存在权限的角色id
            roleIds = userRoleMapper.selectList(wrapper).stream().map(UserRole::getRoleId).toList();
            // 查询没有该权限的角色
            roleIds = roleMapper.selectList(new LambdaQueryWrapper<Role>().notIn(!roleIds.isEmpty(), Role::getId, roleIds))
                    .stream().map(Role::getId).toList();
        }

        if (!roleIds.isEmpty()) {
            LambdaQueryWrapper<Role> queryWrapper = new LambdaQueryWrapper<>();
            List<Role> roleList = new ArrayList<>();
            // 查询角色信息
            roleIds.forEach(roleId -> {
                if (Objects.nonNull(roleName)) queryWrapper.like(Role::getRoleName, roleName);
                if (Objects.nonNull(roleKey)) queryWrapper.and(a -> a.like(Role::getRoleKey, roleKey));
                queryWrapper.eq(Role::getId, roleId);
                roleList.addAll(roleMapper.selectList(queryWrapper));
                queryWrapper.clear();
            });
            return roleList.stream().map(role -> role.asViewObject(RoleAllVO.class)).toList();
        }
        return null;
    }

    @Transactional
    @Override
    public ResponseResult<Void> addRoleUser(RoleUserDTO roleUserDTO) {
        List<Long> roleIds = roleUserDTO.getRoleId();
        List<Long> userIds = roleUserDTO.getUserId();
        LambdaQueryWrapper<UserRole> wrapper = new LambdaQueryWrapper<>();
        wrapper.in(UserRole::getUserId, userIds)
                .in(UserRole::getRoleId, roleIds);
        // 如有，先删除
        if (userRoleMapper.selectCount(wrapper) > 0) userRoleMapper.delete(wrapper);

        List<UserRole> userRoles = new ArrayList<>();
        roleIds.forEach(roleId -> {
            userIds.forEach(userId -> {
                userRoles.add(UserRole.builder().roleId(roleId).userId(userId).build());
            });
        });
        if (saveBatch(userRoles)) return ResponseResult.success();

        return ResponseResult.failure();
    }

    @Override
    public ResponseResult<Void> deleteRoleUser(RoleUserDTO roleUserDTO) {
        int isDelete = userRoleMapper.delete(new LambdaQueryWrapper<UserRole>().in(UserRole::getUserId, roleUserDTO.getUserId()).in(UserRole::getRoleId, roleUserDTO.getRoleId()));
        if (isDelete > 0) {
            return ResponseResult.success();
        }
        return ResponseResult.failure();
    }


}
