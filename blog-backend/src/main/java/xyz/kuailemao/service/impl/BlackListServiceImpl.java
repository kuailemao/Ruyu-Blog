package xyz.kuailemao.service.impl;

import cn.hutool.core.thread.NamedThreadFactory;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import xyz.kuailemao.constants.BlackListConst;
import xyz.kuailemao.constants.RedisConst;
import xyz.kuailemao.constants.SQLConst;
import xyz.kuailemao.domain.dto.AddBlackListDTO;
import xyz.kuailemao.domain.dto.UpdateBlackListDTO;
import xyz.kuailemao.domain.entity.BlackList;
import xyz.kuailemao.domain.ip.BlackListIpInfo;
import xyz.kuailemao.domain.response.ResponseResult;
import xyz.kuailemao.domain.vo.BlackListVO;
import xyz.kuailemao.enums.BlackListEnum;
import xyz.kuailemao.mapper.BlackListMapper;
import xyz.kuailemao.mapper.UserMapper;
import xyz.kuailemao.service.BlackListService;
import xyz.kuailemao.service.IpService;
import xyz.kuailemao.utils.IpUtils;
import xyz.kuailemao.utils.RedisCache;
import xyz.kuailemao.utils.SecurityUtils;

import java.util.Date;
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

    @Resource
    private IpService ipService;

    @Resource
    private UserMapper userMapper;

    @Resource
    private RedisCache redisCache;

    @Override
    public ResponseResult<Void> addBlackList(AddBlackListDTO addBlackListDTO) {
        BlackList blackList = BlackList.builder()
                .userId(addBlackListDTO.getUserId())
                .reason(addBlackListDTO.getReason())
                .type(
                        null != addBlackListDTO.getUserId() ?
                                BlackListConst.BLACK_LIST_TYPE_USER :
                                BlackListConst.BLACK_LIST_TYPE_BOT
                )
                .expiresTime(addBlackListDTO.getExpiresTime()).build();
        BlackListIpInfo blackListIpInfo = BlackListIpInfo.builder()
                .createIp(IpUtils.getIpAddr(SecurityUtils.getCurrentHttpRequest()))
                .build();
        blackList.setIpInfo(blackListIpInfo);
        if (this.save(blackList)) {
            if (blackList.getType() == BlackListConst.BLACK_LIST_TYPE_BOT) {
                // 更新redis缓存
                redisCache.setCacheMapValue(RedisConst.BLACK_LIST_IP_KEY, blackList.getIpInfo().getCreateIp(), blackList.getExpiresTime());
                ipService.refreshIpDetailAsyncByBid(blackList.getId());
                return ResponseResult.success();
            } else if (blackList.getType() == BlackListConst.BLACK_LIST_TYPE_USER) {
                redisCache.setCacheMapValue(RedisConst.BLACK_LIST_UID_KEY, blackList.getUserId().toString(), blackList.getExpiresTime());
            }
        }
        return ResponseResult.failure();
    }

    @Override
    public List<BlackListVO> getBlackList() {
        return this.query().list().stream()
                .map(blackList ->
                        blackList.asViewObject(
                                BlackListVO.class, (black) ->
                                {
                                    if (blackList.getUserId() != null) {
                                        black.setUserName(
                                                userMapper.selectById(blackList.getUserId()).getUsername()
                                        );
                                    }
                                }
                        )
                )
                .toList();
    }

    /**
     * 用户是否存在黑名单
     *
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

    @Override
    public ResponseResult<Void> updateIsBan(Long id) {
        BlackList blackList = blackListMapper.selectById(id);
        /*
        compareTo方法返回一个整数，如果当前时间大于过期时间，则返回正数；
        如果当前时间等于过期时间，则返回0；如果当前时间小于过期时间，则返回负数。
         */
        if (blackList != null && new Date().compareTo(blackList.getExpiresTime()) >= 0) {
            return ResponseResult.success();
        }
        return ResponseResult.failure();
    }
}
