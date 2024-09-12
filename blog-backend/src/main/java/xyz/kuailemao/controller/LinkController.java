package xyz.kuailemao.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import xyz.kuailemao.annotation.AccessLimit;
import xyz.kuailemao.annotation.CheckBlacklist;
import xyz.kuailemao.annotation.LogAnnotation;
import xyz.kuailemao.constants.LogConst;
import xyz.kuailemao.domain.dto.LinkDTO;
import xyz.kuailemao.domain.dto.LinkIsCheckDTO;
import xyz.kuailemao.domain.dto.SearchLinkDTO;
import xyz.kuailemao.domain.response.ResponseResult;
import xyz.kuailemao.domain.vo.LinkListVO;
import xyz.kuailemao.domain.vo.LinkVO;
import xyz.kuailemao.service.LinkService;
import xyz.kuailemao.utils.ControllerUtils;

import java.util.List;

/**
 * (Link)表控制层
 *
 * @author kuailemao
 * @since 2023-11-14 08:48:32
 */
@Tag(name = "友链相关接口")
@RestController
@Validated
@RequestMapping("link")
public class LinkController {
    /**
     * 服务对象
     */
    @Resource
    private LinkService linkService;

    @CheckBlacklist
    @Operation(summary = "申请友链")
    @Parameter(name = "linkDTO", description = "友链申请信息")
    @AccessLimit(seconds = 60, maxCount = 10)
    @PostMapping("/auth/apply")
    public ResponseResult<Void> applyLink(@RequestBody @Valid LinkDTO linkDTO) {
        return linkService.applyLink(linkDTO);
    }

    @Operation(summary = "查询所有通过申请的友链")
    @AccessLimit(seconds = 60, maxCount = 30)
    @GetMapping("/list")
    public ResponseResult<List<LinkVO>> getLinkList() {
        return ControllerUtils.messageHandler(() -> linkService.getLinkList());
    }

    @PreAuthorize("hasAnyAuthority('blog:link:list')")
    @Operation(summary = "后台友链列表")
    @AccessLimit(seconds = 60, maxCount = 30)
    @LogAnnotation(module = "友链管理", operation = LogConst.GET)
    @GetMapping("/back/list")
    public ResponseResult<List<LinkListVO>> backList() {
        return ControllerUtils.messageHandler(() -> linkService.getBackLinkList(null));
    }

    @PreAuthorize("hasAnyAuthority('blog:link:search')")
    @Operation(summary = "搜索后台友链列表")
    @AccessLimit(seconds = 60, maxCount = 30)
    @LogAnnotation(module = "友链管理", operation = LogConst.SEARCH)
    @PostMapping("/back/search")
    public ResponseResult<List<LinkListVO>> backList(@RequestBody SearchLinkDTO searchDTO) {
        return ControllerUtils.messageHandler(() -> linkService.getBackLinkList(searchDTO));
    }

    @PreAuthorize("hasAnyAuthority('blog:link:isCheck')")
    @Operation(summary = "修改友链是否通过")
    @AccessLimit(seconds = 60, maxCount = 30)
    @LogAnnotation(module = "友链管理", operation = LogConst.UPDATE)
    @PostMapping("/back/isCheck")
    public ResponseResult<Void> isCheck(@RequestBody @Valid LinkIsCheckDTO linkIsCheckDTO) {
        return linkService.isCheckLink(linkIsCheckDTO);
    }

    @PreAuthorize("hasAnyAuthority('blog:link:delete')")
    @Operation(summary = "删除友链")
    @AccessLimit(seconds = 60, maxCount = 30)
    @LogAnnotation(module = "友链管理", operation = LogConst.DELETE)
    @DeleteMapping("/back/delete")
    public ResponseResult<Void> delete(@RequestBody List<Long> ids) {
        return linkService.deleteLink(ids);
    }

    /**
     * 邮箱审核友链接口
     * @param verifyCode 校验码
     * @param response 响应
     * @return 提示信息
     */
    @Operation(summary = "邮箱审核友链")
    @AccessLimit(seconds = 60, maxCount = 60)
    @LogAnnotation(module = "友链审核", operation = LogConst.APPROVE)
    @Parameter(name = "verifyCode", description = "校验码")
    @GetMapping("/email/apply")
    public String emailApply(@RequestParam("verifyCode") @NotBlank(message = "校验码不能为空") String verifyCode, HttpServletResponse response) {
        return linkService.emailApplyLink(verifyCode, response);
    }
}

