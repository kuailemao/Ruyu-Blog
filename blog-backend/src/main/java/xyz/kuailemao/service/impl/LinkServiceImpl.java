package xyz.kuailemao.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import xyz.kuailemao.constants.SQLConst;
import xyz.kuailemao.domain.dto.LinkDTO;
import xyz.kuailemao.domain.dto.LinkIsCheckDTO;
import xyz.kuailemao.domain.dto.SearchLinkDTO;
import xyz.kuailemao.domain.entity.Link;
import xyz.kuailemao.domain.entity.User;
import xyz.kuailemao.domain.entity.UserRole;
import xyz.kuailemao.domain.response.ResponseResult;
import xyz.kuailemao.domain.vo.LinkListVO;
import xyz.kuailemao.domain.vo.LinkVO;
import xyz.kuailemao.mapper.LinkMapper;
import xyz.kuailemao.mapper.UserMapper;
import xyz.kuailemao.mapper.UserRoleMapper;
import xyz.kuailemao.service.LinkService;
import xyz.kuailemao.service.PublicService;
import xyz.kuailemao.utils.SecurityUtils;
import xyz.kuailemao.utils.StringUtils;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * (Link)表服务实现类
 *
 * @author kuailemao
 * @since 2023-11-14 08:43:35
 */
@Service("linkService")
public class LinkServiceImpl extends ServiceImpl<LinkMapper, Link> implements LinkService {

    @Resource
    private LinkMapper linkMapper;

    @Resource
    private PublicService publicService;

    @Resource
    private UserMapper userMapper;

    @Resource
    private UserRoleMapper userRoleMapper;

    @Override
    public ResponseResult<Void> applyLink(LinkDTO linkDTO) {
        Link link = linkDTO.asViewObject(Link.class);
        link.setUserId(SecurityUtils.getUserId());
        // 1.数据库添加
        if (this.save(link)) {
            // 查询管理员
            String email = userMapper.selectById(userRoleMapper.selectOne(new LambdaQueryWrapper<UserRole>().eq(UserRole::getRoleId, SQLConst.ADMIN_ID)).getUserId()).getEmail();
            // 2.向管理员发送邮件
            publicService.sendEmail("friendLinkApplication", email);
            return ResponseResult.success();
        }
        return ResponseResult.failure();
    }

    @Override
    public List<LinkVO> getLinkList() {
        List<Link> links = linkMapper.selectList(new LambdaQueryWrapper<Link>().eq(Link::getIsCheck, SQLConst.STATUS_PUBLIC));

        return links.stream().map(link -> link.asViewObject(LinkVO.class, v -> v.setAvatar(userMapper.selectOne(new LambdaQueryWrapper<User>().eq(User::getId, link.getUserId())).getAvatar()))).toList();
    }

    @Override
    public List<LinkListVO> getBackLinkList(SearchLinkDTO searchDTO) {
        LambdaQueryWrapper<Link> wrapper = new LambdaQueryWrapper<>();
        if (StringUtils.isNotNull(searchDTO)) {
            // 搜索
            List<User> users = userMapper.selectList(new LambdaQueryWrapper<User>().like(User::getUsername, searchDTO.getUserName()));
            if (!users.isEmpty())
                wrapper.in(StringUtils.isNotEmpty(searchDTO.getUserName()), Link::getUserId, users.stream().map(User::getId).collect(Collectors.toList()));
            else
                wrapper.eq(StringUtils.isNotNull(searchDTO.getUserName()), Link::getUserId, null);

            wrapper.like(StringUtils.isNotEmpty(searchDTO.getName()), Link::getName, searchDTO.getName())
                    .eq(StringUtils.isNotNull(searchDTO.getIsCheck()), Link::getIsCheck, searchDTO.getIsCheck());
            if (StringUtils.isNotNull(searchDTO.getStartTime()) && StringUtils.isNotNull(searchDTO.getEndTime()))
                wrapper.between(Link::getCreateTime, searchDTO.getStartTime(), searchDTO.getEndTime());
        }
        List<Link> links = linkMapper.selectList(wrapper);
        if (!links.isEmpty()) {
            return links.stream().map(link -> link.asViewObject(LinkListVO.class,
                    v -> v.setUserName(userMapper.selectOne(new LambdaQueryWrapper<User>().eq(User::getId, link.getUserId()))
                            .getUsername()))).toList();
        }
        return null;
    }

    @Override
    public ResponseResult<Void> isCheckLink(LinkIsCheckDTO isCheckDTO) {
        if (linkMapper.updateById(Link.builder().id(isCheckDTO.getId()).isCheck(isCheckDTO.getIsCheck()).build()) > 0) {
            // 修改成功，发送邮件
            if (Objects.equals(isCheckDTO.getIsCheck(), SQLConst.STATUS_PUBLIC)) {
                publicService.sendEmail("friendLinkApplicationPass", linkMapper.selectById(isCheckDTO.getId()).getEmail());
                return ResponseResult.success(null, "操作成功，已发送通知邮件");
            }
            return ResponseResult.success(null, "操作成功");
        }

        return ResponseResult.failure();
    }

    @Override
    public ResponseResult<Void> deleteLink(List<Long> ids) {
        if (linkMapper.deleteBatchIds(ids) > 0) {
            return ResponseResult.success();
        }
        return ResponseResult.failure();
    }
}
