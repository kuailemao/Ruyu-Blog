package xyz.kuailemao.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import xyz.kuailemao.constants.SQLConst;
import xyz.kuailemao.domain.dto.SearchTreeHoleDTO;
import xyz.kuailemao.domain.dto.TreeHoleIsCheckDTO;
import xyz.kuailemao.domain.entity.TreeHole;
import xyz.kuailemao.domain.entity.User;
import xyz.kuailemao.domain.response.ResponseResult;
import xyz.kuailemao.domain.vo.TreeHoleListVO;
import xyz.kuailemao.domain.vo.TreeHoleVO;
import xyz.kuailemao.mapper.TreeHoleMapper;
import xyz.kuailemao.mapper.UserMapper;
import xyz.kuailemao.service.TreeHoleService;
import xyz.kuailemao.utils.SecurityUtils;
import xyz.kuailemao.utils.StringUtils;

import java.util.List;
import java.util.stream.Collectors;

/**
 * (TreeHole)表服务实现类
 *
 * @author kuailemao
 * @since 2023-10-30 11:14:14
 */
@Service("treeHoleService")
public class TreeHoleServiceImpl extends ServiceImpl<TreeHoleMapper, TreeHole> implements TreeHoleService {

    @Resource
    private UserMapper userMapper;

    @Resource
    private TreeHoleMapper treeHoleMapper;

    @Override
    public ResponseResult<Void> addTreeHole(String content) {
        if (this.save(TreeHole.builder().userId(SecurityUtils.getUserId()).content(content).build())) {
            return ResponseResult.success();
        }
        return ResponseResult.failure();
    }

    @Override
    public List<TreeHoleVO> getTreeHole() {
        List<TreeHole> treeHoles = treeHoleMapper.selectList(new LambdaQueryWrapper<TreeHole>().eq(TreeHole::getIsCheck, SQLConst.IS_CHECK_YES));
        if (treeHoles.isEmpty()) return null;
        return treeHoles.stream().map(treeHole -> treeHole.asViewObject(TreeHoleVO.class, treeHoleVO -> {
            User user = userMapper.selectOne(new LambdaQueryWrapper<User>().eq(User::getId, treeHole.getUserId()));
            treeHoleVO.setNickname(user.getUsername());
            treeHoleVO.setAvatar(user.getAvatar());
        })).collect(Collectors.toList());
    }

    @Override
    public List<TreeHoleListVO> getBackTreeHoleList(SearchTreeHoleDTO searchDTO) {
        LambdaQueryWrapper<TreeHole> wrapper = new LambdaQueryWrapper<>();
        if (StringUtils.isNotNull(searchDTO)) {
            // 搜索
            List<User> users = userMapper.selectList(new LambdaQueryWrapper<User>().like(User::getUsername, searchDTO.getUserName()));
            if (!users.isEmpty())
                wrapper.in(StringUtils.isNotEmpty(searchDTO.getUserName()), TreeHole::getUserId, users.stream().map(User::getId).collect(Collectors.toList()));
            else
                wrapper.eq(StringUtils.isNotNull(searchDTO.getUserName()), TreeHole::getUserId, null);

            wrapper.eq(StringUtils.isNotNull(searchDTO.getIsCheck()), TreeHole::getIsCheck, searchDTO.getIsCheck());
            if (StringUtils.isNotNull(searchDTO.getStartTime()) && StringUtils.isNotNull(searchDTO.getEndTime()))
                wrapper.between(TreeHole::getCreateTime, searchDTO.getStartTime(), searchDTO.getEndTime());
        }
        List<TreeHole> treeHoles = treeHoleMapper.selectList(wrapper);
        if (!treeHoles.isEmpty()) {
            return treeHoles.stream().map(treeHole -> treeHole.asViewObject(TreeHoleListVO.class,
                    v -> v.setUserName(userMapper.selectOne(new LambdaQueryWrapper<User>().eq(User::getId, treeHole.getUserId()))
                            .getUsername()))).toList();
        }
        return null;
    }

    @Override
    public ResponseResult<Void> isCheckTreeHole(TreeHoleIsCheckDTO isCheckDTO) {
        if (treeHoleMapper.updateById(TreeHole.builder().id(isCheckDTO.getId()).isCheck(isCheckDTO.getIsCheck()).build()) > 0)
            return ResponseResult.success();

        return ResponseResult.failure();
    }

    @Override
    public ResponseResult<Void> deleteTreeHole(List<Long> ids) {
        if (treeHoleMapper.deleteBatchIds(ids) > 0) {
            return ResponseResult.success();
        }
        return ResponseResult.failure();
    }


}
