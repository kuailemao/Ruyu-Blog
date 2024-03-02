package xyz.kuailemao.service;

import com.baomidou.mybatisplus.extension.service.IService;
import xyz.kuailemao.domain.dto.FavoriteIsCheckDTO;
import xyz.kuailemao.domain.dto.SearchFavoriteDTO;
import xyz.kuailemao.domain.entity.Favorite;
import xyz.kuailemao.domain.response.ResponseResult;
import xyz.kuailemao.domain.vo.FavoriteListVO;

import java.util.List;


/**
 * (Favorite)表服务接口
 *
 * @author kuailemao
 * @since 2023-10-18 14:12:25
 */
public interface FavoriteService extends IService<Favorite> {

    /**
     * 收藏文章
     *
     * @param type   收藏类型
     * @param typeId 收藏id
     * @return 收藏结果
     */
    ResponseResult<Void> userFavorite(Integer type, Long typeId);

    /**
     * 取消收藏文章
     *
     * @param type   收藏类型
     * @param typeId 收藏id
     * @return 取消收藏结果
     */
    ResponseResult<Void> cancelFavorite(Integer type, Integer typeId);

    /**
     * 是否已经收藏
     *
     * @param type   收藏类型
     * @param typeId 收藏id
     * @return 是否已经收藏
     */
    Boolean isFavorite(Integer type, Integer typeId);

    /**
     * 后台收藏列表
     * @return 结果
     */
    List<FavoriteListVO> getBackFavoriteList(SearchFavoriteDTO searchDTO);

    /**
     * 是否通过收藏
     * @param isCheckDTO 是否通过
     * @return 是否成功
     */
    ResponseResult<Void> isCheckFavorite(FavoriteIsCheckDTO isCheckDTO);

    /**
     * 删除收藏
     * @param ids id 列表
     * @return 是否成功
     */
    ResponseResult<Void> deleteFavorite(List<Long> ids);
}
