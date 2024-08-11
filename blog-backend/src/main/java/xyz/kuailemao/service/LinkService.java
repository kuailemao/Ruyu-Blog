package xyz.kuailemao.service;

import com.baomidou.mybatisplus.extension.service.IService;
import jakarta.servlet.http.HttpServletResponse;
import xyz.kuailemao.domain.dto.LinkDTO;
import xyz.kuailemao.domain.dto.LinkIsCheckDTO;
import xyz.kuailemao.domain.dto.SearchLinkDTO;
import xyz.kuailemao.domain.entity.Link;
import xyz.kuailemao.domain.response.ResponseResult;
import xyz.kuailemao.domain.vo.LinkListVO;
import xyz.kuailemao.domain.vo.LinkVO;

import java.util.List;


/**
 * (Link)表服务接口
 *
 * @author kuailemao
 * @since 2023-11-14 08:43:35
 */
public interface LinkService extends IService<Link> {

    /**
     * 申请友链
     * @param linkDTO 友链信息
     * @return 是否成功
     */
    ResponseResult<Void> applyLink(LinkDTO linkDTO);

    /**
     * 查询通过审核的友链
     */
    List<LinkVO> getLinkList();


    /**
     * 后台友链列表
     * @return 结果
     */
    List<LinkListVO> getBackLinkList(SearchLinkDTO searchDTO);

    /**
     * 是否通过友链
     * @param isCheckDTO 是否通过
     * @return 是否成功
     */
    ResponseResult<Void> isCheckLink(LinkIsCheckDTO isCheckDTO);

    /**
     * 删除友链
     * @param ids id 列表
     * @return 是否成功
     */
    ResponseResult<Void> deleteLink(List<Long> ids);

    /**
     * 邮箱审核友链申请
     * @param verifyCode 校验码
     * @return 是否成功
     */
    String emailApplyLink(String verifyCode, HttpServletResponse response);
}
