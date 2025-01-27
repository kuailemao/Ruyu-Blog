package xyz.kuailemao.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.annotation.Resource;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import xyz.kuailemao.domain.dto.PhotoAlbumDTO;
import xyz.kuailemao.domain.entity.Photo;
import xyz.kuailemao.domain.response.ResponseResult;
import xyz.kuailemao.domain.vo.PageVO;
import xyz.kuailemao.domain.vo.PhotoAndAlbumListVO;
import xyz.kuailemao.enums.AlbumOrPhotoEnum;
import xyz.kuailemao.enums.UploadEnum;
import xyz.kuailemao.exceptions.FileUploadException;
import xyz.kuailemao.mapper.PhotoMapper;
import xyz.kuailemao.service.PhotoService;
import xyz.kuailemao.utils.FileUploadUtils;
import xyz.kuailemao.utils.SecurityUtils;

import java.util.List;

import static xyz.kuailemao.constants.SQLConst.ORDER_BY_CREATE_TIME_DESC;


/**
 * (Photo)表服务实现类
 *
 * @author kuailemao
 * @since 2025-01-16 16:33:08
 */
@Log4j2
@Service("photosService")
public class PhotoServiceImpl extends ServiceImpl<PhotoMapper, Photo> implements PhotoService {

    @Resource
    private PhotoMapper photoMapper;

    @Resource
    private FileUploadUtils fileUploadUtils;

    @Override
    public PageVO<List<PhotoAndAlbumListVO>> getBackPhotoList(Long pageNum, Long pageSize, Long parentId) {
        // 分页
        Page<Photo> page = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<Photo> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        if (null != parentId) {
            lambdaQueryWrapper.eq(Photo::getParentId, parentId);
        } else {
            lambdaQueryWrapper.isNull(Photo::getParentId);
        }
        // 优先显示相册，再显示照片，时间倒序
        lambdaQueryWrapper.last(ORDER_BY_CREATE_TIME_DESC);
        photoMapper.selectPage(page, lambdaQueryWrapper);
        if (page.getRecords().isEmpty()) return new PageVO<>(List.of(), page.getTotal());
        List<PhotoAndAlbumListVO> photoAndAlbumListVOS = page.getRecords().stream().map(photo -> photo.asViewObject(PhotoAndAlbumListVO.class)).toList();
        return new PageVO<>(photoAndAlbumListVOS, page.getTotal());
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
            // TODO 注意：如minio地址配置的是nginx代理域名，则需要配置nginx的文件上传大小
            String bannerUrl;
            bannerUrl = fileUploadUtils.upload(UploadEnum.PHOTO_ALBUM, file, name);
            //添加数据库
            photoMapper.insert(Photo.builder()
                    .userId(SecurityUtils.getUserId())
                    .parentId(parentId)
                    .name(name)
                    .url(bannerUrl)
                    .type(AlbumOrPhotoEnum.PHOTO.getCode())
                    .size(fileUploadUtils.convertFileSizeToMB(file.getSize()))
                    .build());

            return ResponseResult.success();
        } catch (FileUploadException e) {
            log.error("{}上传失败", UploadEnum.PHOTO_ALBUM.getDescription(), e);
            return ResponseResult.failure(e.getMessage());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
