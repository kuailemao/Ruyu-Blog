package xyz.kuailemao.controller;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import xyz.kuailemao.annotation.AccessLimit;
import xyz.kuailemao.annotation.LogAnnotation;
import xyz.kuailemao.constants.LogConst;
import xyz.kuailemao.domain.dto.LogDTO;
import xyz.kuailemao.domain.dto.LogDeleteDTO;
import xyz.kuailemao.domain.response.ResponseResult;
import xyz.kuailemao.domain.vo.PageVO;
import xyz.kuailemao.service.LogService;
import xyz.kuailemao.utils.ControllerUtils;

/**
 * (LoginLog)表控制层
 *
 * @author kuailemao
 * @since 2023-12-08 14:38:43
 */
@Tag(name = "操作日志相关接口")
@RestController
@RequestMapping("log")
@Validated
public class LogController {
    /**
     * 服务对象
     */
    @Resource
    private LogService logService;

    @PreAuthorize("hasAnyAuthority('system:log:list')")
    @Operation(summary = "显示所有操作日志")
    @AccessLimit(seconds = 60, maxCount = 30)
    @GetMapping("/list/{current}/{pageSize}")
    public ResponseResult<PageVO> getLogList(@PathVariable("current") @NotNull Long current, @PathVariable("pageSize") @NotNull Long pageSize) {
        return ControllerUtils.messageHandler(() -> logService.searchLog(null, current,pageSize));
    }

    @PreAuthorize("hasAnyAuthority('system:log:search')")
    @Operation(summary = "搜索操作日志")
    @Parameter(name = "loginLogDTO", description = "搜索条件")
    @AccessLimit(seconds = 60, maxCount = 30)
    @PostMapping("/search")
    public ResponseResult<PageVO> getLogSearch(@RequestBody @Valid LogDTO logDTO) {
        return ControllerUtils.messageHandler(() -> logService.searchLog(logDTO, logDTO.getCurrent(),logDTO.getPageSize()));
    }

    @PreAuthorize("hasAnyAuthority('system:log:delete')")
    @Operation(summary = "删除/清空操作日志")
    @Parameter(name = "deleteLoginLogDTO", description = "删除的id数组")
    @AccessLimit(seconds = 60, maxCount = 30)
    @LogAnnotation(module = "操作日志", operation = LogConst.DELETE)
    @DeleteMapping("/delete")
    public ResponseResult<Void> deleteLog(@RequestBody @Valid LogDeleteDTO logDeleteDTO) {
        return logService.deleteLog(logDeleteDTO);
    }
}

