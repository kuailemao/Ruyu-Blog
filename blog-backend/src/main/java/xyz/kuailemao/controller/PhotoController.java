package xyz.kuailemao.controller;


import io.swagger.v3.oas.annotations.Operation;
import jakarta.annotation.Resource;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import xyz.kuailemao.annotation.AccessLimit;
import xyz.kuailemao.annotation.LogAnnotation;
import xyz.kuailemao.constants.LogConst;
import xyz.kuailemao.domain.entity.Photo;
import xyz.kuailemao.domain.response.ResponseResult;
import xyz.kuailemao.domain.vo.PhotoAndAlbumListVO;
import xyz.kuailemao.service.PhotoService;
import xyz.kuailemao.utils.ControllerUtils;

import java.util.List;


/**
 * (Photo)表控制层
 *
 * @author kuailemao
 * @since 2025-01-16 16:33:05
 */
@RestController
@RequestMapping("photos")
public class PhotoController {
    /**
     * 服务对象
     */
    @Resource
    private PhotoService photosService;

    @PreAuthorize("hasAnyAuthority('blog:photo:list')")
    @Operation(summary = "后台相册或照片列表")
    @AccessLimit(seconds = 60, maxCount = 30)
    @LogAnnotation(module = "相册管理", operation = LogConst.GET)
    @GetMapping("/back/list")
    public ResponseResult<List<PhotoAndAlbumListVO>> backList() {
        return ControllerUtils.messageHandler(() -> photosService.getBackPhotoList());
    }

}

