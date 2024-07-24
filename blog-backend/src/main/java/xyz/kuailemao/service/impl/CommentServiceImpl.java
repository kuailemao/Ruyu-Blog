package xyz.kuailemao.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import xyz.kuailemao.constants.RedisConst;
import xyz.kuailemao.constants.SQLConst;
import xyz.kuailemao.domain.dto.CommentIsCheckDTO;
import xyz.kuailemao.domain.dto.SearchCommentDTO;
import xyz.kuailemao.domain.dto.UserCommentDTO;
import xyz.kuailemao.domain.entity.Comment;
import xyz.kuailemao.domain.entity.User;
import xyz.kuailemao.domain.response.ResponseResult;
import xyz.kuailemao.domain.vo.ArticleCommentVO;
import xyz.kuailemao.domain.vo.CommentListVO;
import xyz.kuailemao.domain.vo.PageVO;
import xyz.kuailemao.enums.LikeEnum;
import xyz.kuailemao.mapper.CommentMapper;
import xyz.kuailemao.mapper.UserMapper;
import xyz.kuailemao.service.CommentService;
import xyz.kuailemao.service.LikeService;
import xyz.kuailemao.utils.RedisCache;
import xyz.kuailemao.utils.SecurityUtils;
import xyz.kuailemao.utils.StringUtils;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * (Comment)表服务实现类
 *
 * @author kuailemao
 * @since 2023-10-19 15:44:57
 */
@Service("commentService")
public class CommentServiceImpl extends ServiceImpl<CommentMapper, Comment> implements CommentService {

    @Resource
    private CommentMapper commentMapper;

    @Resource
    private UserMapper userMapper;

    @Resource
    private LikeService likeService;

    @Resource
    private RedisCache redisCache;

    @Override
    public PageVO<List<ArticleCommentVO>> getComment(Integer type, Integer typeId, Integer pageNum, Integer pageSize) {
        // 查询父评论
        LambdaQueryWrapper<Comment> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper
                .eq(Comment::getType, type)
                .eq(Comment::getTypeId, typeId)
                .eq(Comment::getIsCheck, SQLConst.COMMENT_IS_CHECK)
                .isNull(Comment::getParentId);
        Page<Comment> page = new Page<>(pageNum, pageSize);
        IPage<Comment> commentIPage = commentMapper.selectPage(page, queryWrapper);
        List<Comment> comments = commentIPage.getRecords();
        // 查询所有子评论
        LambdaQueryWrapper<Comment> childQueryWrapper = new LambdaQueryWrapper<>();
        childQueryWrapper
                .eq(Comment::getType, type)
                .eq(Comment::getTypeId, typeId)
                .eq(Comment::getIsCheck, SQLConst.COMMENT_IS_CHECK)
                .isNotNull(Comment::getParentId);
        List<Comment> childComment = commentMapper.selectList(childQueryWrapper);
        if (!childComment.isEmpty()) comments.addAll(childComment);
        List<ArticleCommentVO> commentsVOS = comments.stream().map(comment -> comment.asViewObject(ArticleCommentVO.class)).toList();
        List<ArticleCommentVO> parentComments = commentsVOS.stream().filter(comment -> comment.getParentId() == null).toList();
        List<ArticleCommentVO> collect = parentComments.stream().peek(comment -> {
                    comment.setChildComment(getChildComment(commentsVOS, comment.getId()));
                    comment.setChildCommentCount(getChildCommentCount(commentsVOS, comment.getId()));
                    comment.setParentCommentCount(this.count(new LambdaQueryWrapper<Comment>()
                            .eq(Comment::getType, type)
                            .eq(Comment::getTypeId, typeId)
                            .eq(Comment::getIsCheck, SQLConst.COMMENT_IS_CHECK)
                            .isNull(Comment::getParentId)));
                }
        ).toList();
        // 总评论数量
        LambdaQueryWrapper<Comment> countWrapper = new LambdaQueryWrapper<>();
        countWrapper
                .eq(Comment::getType, type)
                .eq(Comment::getTypeId, typeId)
                .eq(Comment::getIsCheck, SQLConst.COMMENT_IS_CHECK);
        return new PageVO<>(collect, commentMapper.selectCount(countWrapper));
    }

    @Override
    public ResponseResult<Void> userComment(UserCommentDTO commentDTO) {
        Comment comment = commentDTO.asViewObject(Comment.class, commentDto -> commentDto.setCommentUserId(SecurityUtils.getUserId()));
        if (this.save(comment)) {
            // 缓存评论数量+1
            redisCache.incrementCacheMapValue(RedisConst.ARTICLE_COMMENT_COUNT, commentDTO.getTypeId().toString(), 1);
            return ResponseResult.success();
        }
        return ResponseResult.failure();
    }

    @Override
    public List<CommentListVO> getBackCommentList(SearchCommentDTO searchDTO) {
        LambdaQueryWrapper<Comment> wrapper = new LambdaQueryWrapper<>();
        if (StringUtils.isNotNull(searchDTO)) {
            List<User> users = userMapper.selectList(new LambdaQueryWrapper<User>().like(User::getUsername, searchDTO.getCommentUserName()));
            if (!users.isEmpty())
                wrapper.in(StringUtils.isNotEmpty(searchDTO.getCommentUserName()), Comment::getCommentUserId, users.stream().map(User::getId).collect(Collectors.toList()));
            else
                wrapper.eq(StringUtils.isNotNull(searchDTO.getCommentUserName()), Comment::getCommentUserId, null);

            wrapper.like(StringUtils.isNotEmpty(searchDTO.getCommentContent()), Comment::getCommentContent, searchDTO.getCommentContent())
                    .eq(StringUtils.isNotNull(searchDTO.getType()), Comment::getType, searchDTO.getType())
                    .eq(StringUtils.isNotNull(searchDTO.getIsCheck()), Comment::getIsCheck, searchDTO.getIsCheck());
        }

        return commentMapper.selectList(wrapper).stream().map(comment -> comment.asViewObject(CommentListVO.class, v -> {
            v.setCommentUserName(userMapper.selectById(comment.getCommentUserId()).getUsername());
        })).collect(Collectors.toList());
    }

    @Override
    public ResponseResult<Void> isCheckComment(CommentIsCheckDTO isCheckDTO) {
        LambdaUpdateWrapper<Comment> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(Comment::getId, isCheckDTO.getId()).or().eq(Comment::getParentId, isCheckDTO.getId());
        if (commentMapper.update(Comment.builder().id(isCheckDTO.getId()).isCheck(isCheckDTO.getIsCheck()).build(),wrapper) > 0)
            return ResponseResult.success();

        return ResponseResult.failure();
    }

    @Override
    public ResponseResult<Void> deleteComment(Long id) {
        // 是否还有子评论
        if (commentMapper.selectCount(new LambdaQueryWrapper<Comment>().eq(Comment::getParentId, id)) > 0) {
            return ResponseResult.failure("该评论还有子评论");
        }
        if (commentMapper.deleteById(id) > 0) return ResponseResult.success();
        return ResponseResult.failure();
    }

    private List<ArticleCommentVO> getChildComment(List<ArticleCommentVO> comments, Long parentId) {

        return comments.stream()
                .filter(comment -> {
                    if (Objects.isNull(comment.getParentId())) {
                        User user = userMapper.selectById(comment.getCommentUserId());
                        comment.setCommentUserNickname(user.getNickname())
                                .setCommentUserAvatar(user.getAvatar())
                                .setLikeCount(likeService.getLikeCount(LikeEnum.LIKE_TYPE_COMMENT.getType(), comment.getId()));
                    }
                    return Objects.nonNull(comment.getParentId()) && Objects.equals(comment.getParentId(), parentId);
                })
                .peek(comment -> {
                    User user = userMapper.selectById(comment.getCommentUserId());
                    comment.setChildComment(getChildComment(comments, comment.getId()))
                            .setCommentUserNickname(user.getNickname())
                            .setCommentUserAvatar(user.getAvatar())
                            .setReplyUserNickname(userMapper.selectById(comment.getReplyUserId()).getNickname())
                            .setLikeCount(likeService.getLikeCount(LikeEnum.LIKE_TYPE_COMMENT.getType(), comment.getId()));
                }).toList();
    }

    // 获取父评论底下的评论数量
    private Long getChildCommentCount(List<ArticleCommentVO> comments, Long parentId) {
        // 递归获取父评论的子评论数
        return comments.stream()
                .filter(comment -> Objects.nonNull(comment.getParentId()) && Objects.equals(comment.getParentId(), parentId))
                .peek(comment -> {
                    // 回复子评论的数量
                    Long count = commentMapper.selectCount(new LambdaQueryWrapper<Comment>().eq(Comment::getReplyId, comment.getId()).eq(Comment::getIsCheck, SQLConst.COMMENT_IS_CHECK));
                    comment.setChildCommentCount(count);
                })
                .mapToLong(comment -> {
                    if (!comment.getChildComment().isEmpty()) {
                        return (1 + getChildCommentCount(comment.getChildComment(), comment.getId()));
                    } else {
                        return 1;
                    }
                })
                .sum();
    }
}
