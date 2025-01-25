package xyz.kuailemao.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import xyz.kuailemao.domain.entity.Photo;
import xyz.kuailemao.domain.vo.PhotoAndAlbumListVO;
import xyz.kuailemao.mapper.PhotoMapper;
import xyz.kuailemao.service.PhotoService;

import java.util.List;

/**
 * (Photo)表服务实现类
 *
 * @author kuailemao
 * @since 2025-01-16 16:33:08
 */
@Service("photosService")
public class PhotoServiceImpl extends ServiceImpl<PhotoMapper, Photo> implements PhotoService {

    @Override
    public List<PhotoAndAlbumListVO> getBackPhotoList() {
        return this.list().stream().map(photo -> photo.asViewObject(PhotoAndAlbumListVO.class)).toList();
    }
}
