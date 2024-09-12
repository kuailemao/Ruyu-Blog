package xyz.kuailemao.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import xyz.kuailemao.domain.dto.AddBlackListDTO;
import xyz.kuailemao.domain.dto.UpdateBlackListDTO;
import xyz.kuailemao.domain.entity.BlackList;
import xyz.kuailemao.domain.response.ResponseResult;
import xyz.kuailemao.mapper.BlackListMapper;
import xyz.kuailemao.service.BlackListService;
import xyz.kuailemao.utils.SecurityUtils;

import java.util.List;

/**
 * (BlackList)表服务实现类
 *
 * @author kuailemao
 * @since 2024-09-05 16:13:21
 */
@Service("blackListService")
public class BlackListServiceImpl extends ServiceImpl<BlackListMapper, BlackList> implements BlackListService {

    @Resource
    private BlackListMapper blackListMapper;

    @Override
    public ResponseResult<Void> addBlackList(AddBlackListDTO addBlackListDTO) {
        BlackList blackList = BlackList.builder()
                .userId(addBlackListDTO.getUserId())
                .reason(addBlackListDTO.getReason())
                .expiresTime(addBlackListDTO.getExpiresTime()).build();
        if (this.save(blackList)) return ResponseResult.success();
        return ResponseResult.failure();
    }

    @Override
    public List<BlackList> getBlackList() {
        return this.query().list();
    }

    /**
     * 用户是否存在黑名单
     * @param userId 用户id
     * @return true:存在 false:不存在
     */
    @Override
    public Boolean isUserInBlackList(Long userId) {
        LambdaQueryWrapper<BlackList> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(BlackList::getUserId, userId);
        return this.count(queryWrapper) > 0;
    }

    @Override
    public ResponseResult<Void> updateBlackList(UpdateBlackListDTO updateBlackListDTO) {
        BlackList blackList = BlackList.builder()
                .id(updateBlackListDTO.getId())
                .reason(updateBlackListDTO.getReason())
                .expiresTime(updateBlackListDTO.getExpiresTime()).build();
        if (this.updateById(blackList)) return ResponseResult.success();
        return ResponseResult.failure();
    }

    @Override
    public ResponseResult<Void> deleteBlackList(Long id) {
        if (this.removeById(id)) {
            return ResponseResult.success();
        }
        return ResponseResult.failure();
    }
}
