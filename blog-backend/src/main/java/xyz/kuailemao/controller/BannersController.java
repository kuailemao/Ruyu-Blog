package xyz.kuailemao.controller;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.annotation.Resource;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.multipart.MultipartFile;
import xyz.kuailemao.annotation.AccessLimit;
import xyz.kuailemao.annotation.LogAnnotation;
import xyz.kuailemao.constants.LogConst;
import xyz.kuailemao.domain.entity.Banners;
import xyz.kuailemao.domain.response.ResponseResult;
import xyz.kuailemao.service.BannersService;
import org.springframework.web.bind.annotation.*;
import xyz.kuailemao.utils.ControllerUtils;

import java.util.List;

/**
 * (Banners)表控制层
 *
 * @author kuailemao
 * @since 2024-08-28 09:51:12
 */
@RestController
@RequestMapping("banners")
public class BannersController {
    /**
     * 服务对象
     */
    @Resource
    private BannersService bannersService;

    /**
     * 前台查询首页 banner列表
     */
    @Operation(summary = "前台获取所有前台首页Banner图片")
    @LogAnnotation(module = "信息管理", operation = LogConst.GET)
    @AccessLimit(seconds = 60, maxCount = 25)
    @GetMapping("/list")
    public ResponseResult<List<String>> getBanners() {
        return ControllerUtils.messageHandler(() -> bannersService.getBanners());
    }

    /**
     * 查询首页banner列表
     */
    @PreAuthorize("hasAnyAuthority('blog:banner:list')")
    @Operation(summary = "后台获取所有前台首页Banner图片")
    @LogAnnotation(module = "信息管理", operation = LogConst.GET)
    @AccessLimit(seconds = 60, maxCount = 60)
    @GetMapping("/back/list")
    public ResponseResult<List<Banners>> backGetBanners() {
        return ControllerUtils.messageHandler(() -> bannersService.backGetBanners());
    }

    /**
     * 删除首页banner
     */
    @PreAuthorize("hasAnyAuthority('blog:banner:delete')")
    @Operation(summary = "删除前台首页Banner图片")
    @LogAnnotation(module = "信息管理", operation = LogConst.DELETE)
    @Parameter(name = "id", description = "Banner ID", required = true)
    @AccessLimit(seconds = 60, maxCount = 30)
    @DeleteMapping("/{id}")
    public ResponseResult<String> delete(@PathVariable("id") Long id) {
        return bannersService.removeBannerById(id);
    }

    /**
     * 添加
     */
    @PreAuthorize("hasAnyAuthority('blog:banner:add')")
    @Operation(summary = "添加前台首页Banner图片")
    @LogAnnotation(module = "信息管理", operation = LogConst.INSERT)
    @Parameter(name = "bannerImage", description = "Banner图片", required = true)
    @AccessLimit(seconds = 60, maxCount = 30)
    @PostMapping("/upload/banner")
    public ResponseResult<Banners> uploadArticleImage(
            @RequestParam("bannerImage") MultipartFile bannerImage
    ) {
        return bannersService.uploadBannerImage(bannerImage);
    }

    /**
     * 更新顺序
     */
    @PreAuthorize("hasAnyAuthority('blog:banner:update')")
    @Operation(summary = "更新前台首页Banner图片顺序")
    @LogAnnotation(module = "信息管理", operation = LogConst.UPDATE)
    @Parameter(name = "SortOrders", description = "顺序", required = true)
    @AccessLimit(seconds = 60, maxCount = 30)
    @PutMapping("/update/sort/order")
    public ResponseResult<String> updateSortOrder(@RequestBody List<Banners> Banners) {
        return bannersService.updateSortOrder(Banners);
    }
}

