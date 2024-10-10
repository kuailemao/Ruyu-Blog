package xyz.kuailemao.service;

import com.baomidou.mybatisplus.extension.service.IService;
import xyz.kuailemao.domain.dto.AddBlackListDTO;
import xyz.kuailemao.domain.dto.SearchBlackListDTO;
import xyz.kuailemao.domain.dto.UpdateBlackListDTO;
import xyz.kuailemao.domain.entity.BlackList;
import xyz.kuailemao.domain.response.ResponseResult;
import xyz.kuailemao.domain.vo.BlackListVO;

import java.util.List;


/**
 * (BlackList)表服务接口
 *
 * @author kuailemao
 * @since 2024-09-05 16:13:20
 */
public interface BlackListService extends IService<BlackList> {

    /**
     * 新增数据
     * @param addBlackListDTO 新增对象
     * @return 新增结果
     */
    ResponseResult<Void> addBlackList(AddBlackListDTO addBlackListDTO);

    /**
     * 获取黑名单
     * @return 黑名单
     */
    List<BlackListVO> getBlackList(SearchBlackListDTO searchBlackListDTO);

    /**
     * 修改数据
     * @param updateBlackListDTO 修改对象
     * @return 修改结果
     */
    ResponseResult<Void> updateBlackList(UpdateBlackListDTO updateBlackListDTO);

    /**
     * 删除黑名单
     * @param ids 黑名单id
     * @return 是否成功
     */
    ResponseResult<Void> deleteBlackList(List<Long> ids);
}
