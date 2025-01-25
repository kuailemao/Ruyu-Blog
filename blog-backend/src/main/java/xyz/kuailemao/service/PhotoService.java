package xyz.kuailemao.service;

import com.baomidou.mybatisplus.extension.service.IService;
import xyz.kuailemao.domain.entity.Photo;
import xyz.kuailemao.domain.vo.LinkListVO;
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
}
