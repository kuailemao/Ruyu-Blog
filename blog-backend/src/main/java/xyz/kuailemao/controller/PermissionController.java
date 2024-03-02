package xyz.kuailemao.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import xyz.kuailemao.annotation.LogAnnotation;
import xyz.kuailemao.constants.LogConst;
import xyz.kuailemao.domain.dto.PermissionDTO;
import xyz.kuailemao.domain.response.ResponseResult;
import xyz.kuailemao.domain.vo.PermissionMenuVO;
import xyz.kuailemao.domain.vo.PermissionVO;
import xyz.kuailemao.service.PermissionService;
import xyz.kuailemao.utils.ControllerUtils;

import java.util.List;

/**
 * @author kuailemao
 * <p>
 * 创建时间：2023/12/5 20:08
 */
@RestController
@Tag(name = "权限字符相关接口")
@RequestMapping("permission")
public class PermissionController {
    /**
     * 服务对象
     */
    @Resource
    private PermissionService permissionService;

    @PreAuthorize("hasAnyAuthority('system:permission:list')")
    @Operation(summary = "所有权限")
    @GetMapping("/list")
    public ResponseResult<List<PermissionVO>> list() {
        return ResponseResult.success(permissionService.selectPermission(null, null, null));
    }

    @PreAuthorize("hasAnyAuthority('system:permission:search')")
    @Operation(summary = "搜索权限")
    @Parameters({
            @Parameter(name = "permissionDesc", description = "权限字符描述"),
            @Parameter(name = "permissionKey", description = "权限字符键"),
            @Parameter(name = "permissionMenuId", description = "权限字符所属菜单")
    })
    @LogAnnotation(module="权限管理",operation= LogConst.SEARCH)
    @GetMapping("/search")
    public ResponseResult<List<PermissionVO>> searchPermission(
            @RequestParam(value = "permissionDesc", required = false) String permissionDesc,
            @RequestParam(value = "permissionKey", required = false) String permissionKey,
            @RequestParam(value = "permissionMenuId", required = false) Long permissionMenuId
    ) {
        return ResponseResult.success(permissionService.selectPermission(permissionDesc, permissionKey, permissionMenuId));
    }

    @PreAuthorize("hasAnyAuthority('system:permission:menu:list')")
    @Operation(summary = "查询所有权限所在菜单")
    @GetMapping("/menu")
    public ResponseResult<List<PermissionMenuVO>> menu() {
        return ControllerUtils.messageHandler(() -> permissionService.selectPermissionMenu());
    }

    @Operation(summary = "添加权限字符")
    @Parameter(name = "permissionDTO", description = "权限字符信息")
    @PreAuthorize("hasAnyAuthority('system:permission:add')")
    @LogAnnotation(module="权限管理",operation= LogConst.INSERT)
    @PostMapping("/add")
    public ResponseResult<Void> addPermission(@RequestBody @Valid PermissionDTO permissionDTO) {
        return permissionService.updateOrInsertPermission(permissionDTO.setId(null));
    }

    @Operation(summary = "修改权限字符")
    @Parameter(name = "permissionDTO", description = "权限字符信息")
    @PreAuthorize("hasAnyAuthority('system:permission:update')")
    @LogAnnotation(module="权限管理",operation= LogConst.UPDATE)
    @PostMapping("/update")
    public ResponseResult<Void> updatePermission(@RequestBody @Valid PermissionDTO permissionDTO) {
        return permissionService.updateOrInsertPermission(permissionDTO);
    }

    @Operation(summary = "获取要修改的权限信息")
    @Parameter(name = "id", description = "权限字符id")
    @PreAuthorize("hasAnyAuthority('system:permission:get')")
    @GetMapping("/get/{id}")
    public ResponseResult<PermissionDTO> getPermission(
            @PathVariable("id") @NotNull(message = "权限id不能为空") Long id
    ) {
        return ControllerUtils.messageHandler(() -> permissionService.getPermission(id));
    }

    @Operation(summary = "删除权限字符")
    @Parameter(name = "id", description = "权限字符id")
    @PreAuthorize("hasAnyAuthority('system:permission:delete')")
    @LogAnnotation(module="权限管理",operation= LogConst.DELETE)
    @DeleteMapping("/delete/{id}")
    public ResponseResult<Void> deletePermission(
            @PathVariable("id") @NotNull(message = "权限id不能为空") Long id
    ) {
        return permissionService.deletePermission(id);
    }

}
