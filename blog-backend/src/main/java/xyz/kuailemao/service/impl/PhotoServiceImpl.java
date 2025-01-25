package xyz.kuailemao.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import xyz.kuailemao.constants.RespConst;
import xyz.kuailemao.constants.SQLConst;
import xyz.kuailemao.domain.dto.PhotoAlbumDTO;
import xyz.kuailemao.domain.entity.Banners;
import xyz.kuailemao.domain.entity.Photo;
import xyz.kuailemao.domain.response.ResponseResult;
import xyz.kuailemao.domain.vo.PhotoAndAlbumListVO;
import xyz.kuailemao.enums.AlbumOrPhotoEnum;
import xyz.kuailemao.enums.UploadEnum;
import xyz.kuailemao.exceptions.FileUploadException;
import xyz.kuailemao.mapper.PhotoMapper;
import xyz.kuailemao.service.PhotoService;
import xyz.kuailemao.utils.FileUploadUtils;
import xyz.kuailemao.utils.SecurityUtils;

import java.util.List;

/**
 * (Photo)表服务实现类
 *
 * @author kuailemao
 * @since 2025-01-16 16:33:08
 */
@Service("photosService")
public class PhotoServiceImpl extends ServiceImpl<PhotoMapper, Photo> implements PhotoService {

    @Resource
    private PhotoMapper photoMapper;

    @Resource
    private FileUploadUtils fileUploadUtils;

    @Override
    public List<PhotoAndAlbumListVO> getBackPhotoList() {
        return this.list().stream().map(photo -> photo.asViewObject(PhotoAndAlbumListVO.class)).toList();
    }

    @Override
    public ResponseResult<Void> createAlbum(PhotoAlbumDTO albumDTO) {
        if (
                photoMapper.insert(Photo.builder()
                        .userId(SecurityUtils.getUserId())
                        .parentId(albumDTO.getParentId())
                        .name(albumDTO.getName())
                        .description(albumDTO.getDescription())
                        .type(AlbumOrPhotoEnum.ALBUM.getCode())
                        .build()
                ) > 0) {
            return ResponseResult.success();
        }
        return ResponseResult.failure();
    }

    @Transactional
    @Override
    public ResponseResult<Void> uploadPhoto(MultipartFile file, String name, Long parentId) {
        try {
            String bannerUrl;
//            try {
//                bannerUrl = fileUploadUtils.upload(UploadEnum.PHOTO_ALBUM, file);
            // 添加数据库
//                photoMapper.insert(Photo.builder()
//                        .parentId(parentId)
//                        .name(name)
//                        .url(bannerUrl)
//                        .type(AlbumOrPhotoEnum.PHOTO.getCode())
//                        .size(fileUploadUtils.convertFileSizeToMB(file.getSize()))
//                        .build());

            return ResponseResult.success();
//            } catch (FileUploadException e) {
//                return ResponseResult.failure(e.getMessage());
//            }
        } catch (Exception e) {
            log.error(UploadEnum.PHOTO_ALBUM.getDescription() + "上传失败", e);
            return ResponseResult.failure();
        }
    }
}
