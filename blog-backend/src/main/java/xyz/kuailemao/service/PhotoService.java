package xyz.kuailemao.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.web.multipart.MultipartFile;
import xyz.kuailemao.domain.dto.PhotoAlbumDTO;
import xyz.kuailemao.domain.entity.Photo;
import xyz.kuailemao.domain.response.ResponseResult;
import xyz.kuailemao.domain.vo.PhotoAndAlbumListVO;

import java.util.List;


/**
 * (Photo)表服务接口
 *
 * @author kuailemao
 * @since 2025-01-16 16:33:07
 */
public interface PhotoService extends IService<Photo> {

    /**
     * 获取后台图片列表
     * @return 图片列表
     */
    List<PhotoAndAlbumListVO> getBackPhotoList();

    /**
     * 创建相册
     * @param albumDTO 相册信息
     * @return 创建结果
     */
    ResponseResult<Void> createAlbum(PhotoAlbumDTO albumDTO);

    /**
     * 上传图片
     * @param file 图片文件
     * @param name 图片名称
     * @param parentId 相册id
     * @return 上传结果
     */
    ResponseResult<Void> uploadPhoto(MultipartFile file, String name, Long parentId);
}
