package xyz.kuailemao.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.annotation.Resource;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import xyz.kuailemao.domain.dto.DeletePhotoOrAlbumDTO;
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
import xyz.kuailemao.utils.StringUtils;

import java.util.List;
import java.util.Objects;

import static xyz.kuailemao.constants.SQLConst.LIMIT_ONE_SQL;
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
        // 查询每个相册的封面
        for (Photo photo : page.getRecords()) {
            if (Objects.equals(photo.getType(), AlbumOrPhotoEnum.ALBUM.getCode())) {
                Photo photoOne = photoMapper.selectOne(
                        new LambdaQueryWrapper<Photo>()
                                .eq(Photo::getParentId, photo.getId())
                                .eq(Photo::getType, AlbumOrPhotoEnum.PHOTO.getCode())
                                .last(ORDER_BY_CREATE_TIME_DESC).last(LIMIT_ONE_SQL)
                );
                if (null != photoOne && StringUtils.isValidUrl(photoOne.getUrl())) {
                    page.getRecords().get(page.getRecords().indexOf(photo)).setUrl(photoOne.getUrl());
                }else{
                    page.getRecords().get(page.getRecords().indexOf(photo)).setUrl("");
                }
            }
        }

        List<PhotoAndAlbumListVO> photoAndAlbumListVOS = page.getRecords().stream().map(photo -> photo.asViewObject(PhotoAndAlbumListVO.class)).toList();
        return new PageVO<>(photoAndAlbumListVOS, page.getTotal());
    }

    @Override
    public ResponseResult<Void> createAlbum(PhotoAlbumDTO albumDTO) {
        // 是否存在相同名称的相册
        if (
                photoMapper.selectCount(
                        new LambdaQueryWrapper<Photo>()
                                .eq(Photo::getName, albumDTO.getName())
                                .eq(Photo::getType, AlbumOrPhotoEnum.ALBUM.getCode())
                                .eq(Photo::getParentId, albumDTO.getParentId())
                ) > 0) {
            return ResponseResult.failure("相册名称存在重复");
        }
        String albumUrl = "";

        if (albumDTO.getParentId() != null && photoMapper.selectCount(new LambdaQueryWrapper<Photo>().eq(Photo::getId, albumDTO.getParentId())) > 0) {
            // 获取相册的路径
            albumUrl = photoMapper.selectOne(new LambdaQueryWrapper<Photo>().eq(Photo::getId, albumDTO.getParentId())).getUrl();
        }
        if (
                photoMapper.insert(Photo.builder()
                        .userId(SecurityUtils.getUserId())
                        .parentId(albumDTO.getParentId())
                        .name(albumDTO.getName())
                        .description(albumDTO.getDescription())
                        .type(AlbumOrPhotoEnum.ALBUM.getCode())
                        .url(albumUrl + "/" + albumDTO.getName())
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
            // 当前相册是否存在相同名称照片
            if (
                    photoMapper.selectCount(
                            new LambdaQueryWrapper<Photo>()
                                    .eq(Photo::getName, name)
                                    .eq(Photo::getType, AlbumOrPhotoEnum.PHOTO.getCode())
                                    .eq(Photo::getParentId, parentId)
                    ) > 0) {
                return ResponseResult.failure("照片名称存在重复");
            }

            String bannerUrl;
            // 查询父相册的名称
            if (StringUtils.isNotNull(parentId)) {
                // 递归查询父相册并组合路径，并去掉最前面的 /
                bannerUrl = photoMapper.selectById(parentId).getUrl().replaceFirst("^/", "");
                bannerUrl = fileUploadUtils.upload(UploadEnum.PHOTO_ALBUM, file, name, bannerUrl);
            } else {
                bannerUrl = fileUploadUtils.upload(UploadEnum.PHOTO_ALBUM, file, name);
            }

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

    @Override
    public ResponseResult<Void> updateAlbum(PhotoAlbumDTO albumDTO) {
        if (
                photoMapper.updateById(Photo.builder()
                        .id(albumDTO.getId())
                        .name(albumDTO.getName())
                        .description(albumDTO.getDescription())
                        .build()
                ) > 0) {
            return ResponseResult.success();
        }
        return ResponseResult.failure();
    }

    @Transactional
    @Override
    public ResponseResult<Void> deletePhotoOrAlbum(DeletePhotoOrAlbumDTO deletePhotoOrAlbum) {
        if (Objects.equals(deletePhotoOrAlbum.getType(), AlbumOrPhotoEnum.ALBUM.getCode())) {
            // 是否存在子相册
            if (photoMapper.selectCount(new LambdaQueryWrapper<Photo>().eq(Photo::getParentId, deletePhotoOrAlbum.getId())) > 0) {
                return ResponseResult.failure("删除失败，该相册下存在子相册或照片");
            }
            // 删除相册
            if (photoMapper.deleteById(deletePhotoOrAlbum.getId()) > 0) {
                return ResponseResult.success();
            }
            return ResponseResult.failure();
        } else {
            // 查询照片名称
            Photo photo = photoMapper.selectById(deletePhotoOrAlbum.getId());
            // 查询父相册
            if (StringUtils.isNotNull(photo.getParentId())) {
                Photo album = photoMapper.selectById(deletePhotoOrAlbum.getParentId());
                fileUploadUtils.deleteFile(UploadEnum.PHOTO_ALBUM.getDir() + album.getName() + "/", fileUploadUtils.getFileName(photo.getUrl()));
            } else {
                fileUploadUtils.deleteFile(UploadEnum.PHOTO_ALBUM.getDir(), fileUploadUtils.getFileName(photo.getUrl()));
            }
            if (photoMapper.deleteById(deletePhotoOrAlbum.getId()) > 0) {
                return ResponseResult.success();
            }
            return ResponseResult.failure();
        }
    }
}
