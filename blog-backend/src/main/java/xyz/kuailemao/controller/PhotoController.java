package xyz.kuailemao.controller;


import io.swagger.v3.oas.annotations.Operation;
import jakarta.annotation.Resource;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import xyz.kuailemao.annotation.AccessLimit;
import xyz.kuailemao.annotation.LogAnnotation;
import xyz.kuailemao.constants.LogConst;
import xyz.kuailemao.domain.dto.PhotoAlbumDTO;
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
@RequestMapping("photo")
public class PhotoController {
    /**
     * 服务对象
     */
    @Resource
    private PhotoService photoService;

    @PreAuthorize("hasAnyAuthority('blog:photo:list')")
    @Operation(summary = "后台相册或照片列表")
    @AccessLimit(seconds = 60, maxCount = 30)
    @LogAnnotation(module = "相册管理", operation = LogConst.GET)
    @GetMapping("/back/list")
    public ResponseResult<List<PhotoAndAlbumListVO>> backList() {
        return ControllerUtils.messageHandler(() -> photoService.getBackPhotoList());
    }

    @PreAuthorize("hasAnyAuthority('blog:album:create')")
    @Operation(summary = "后台创建相册")
    @AccessLimit(seconds = 60, maxCount = 30)
    @LogAnnotation(module = "相册管理", operation = LogConst.INSERT)
    @PostMapping("/album/create")
    public ResponseResult<Void> createAlbum(@RequestBody PhotoAlbumDTO albumDTO) {
        return photoService.createAlbum(albumDTO);
    }

    @PreAuthorize("hasAnyAuthority('blog:photo:upload')")
    @Operation(summary = "后台上传照片")
    @AccessLimit(seconds = 60, maxCount = 30)
    @LogAnnotation(module = "相册管理", operation = LogConst.UPLOAD_IMAGE)
    @PostMapping("/photo/upload")
    public ResponseResult<Void> uploadPhoto(@RequestParam("file") MultipartFile file,
                                    @RequestParam("name") String name,
                                    @RequestParam("parentId") Long parentId) {
        return photoService.uploadPhoto(file, name, parentId);
    }

}

