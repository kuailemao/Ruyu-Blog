package xyz.kuailemao.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import xyz.kuailemao.annotation.AccessLimit;
import xyz.kuailemao.annotation.LogAnnotation;
import xyz.kuailemao.constants.LogConst;
import xyz.kuailemao.domain.dto.SearchTagDTO;
import xyz.kuailemao.domain.dto.TagDTO;
import xyz.kuailemao.domain.response.ResponseResult;
import xyz.kuailemao.domain.vo.TagVO;
import xyz.kuailemao.service.TagService;
import xyz.kuailemao.utils.ControllerUtils;

import java.util.List;

/**
 * @author kuailemao
 * <p>
 * 创建时间：2023/10/30 9:50
 */
@RestController
@Tag(name = "标签相关接口")
@RequestMapping("/tag")
@Validated
public class TagController {

    @Resource
    private TagService tagService;

    @Operation(summary = "获取标签列表")
    @GetMapping("/list")
    @AccessLimit(seconds = 60, maxCount = 60)
    public ResponseResult<List<TagVO>> list() {
        return ControllerUtils.messageHandler(() -> tagService.listAllTag());
    }

    @Operation(summary = "新增标签-文章列表")
    @PreAuthorize("hasAnyAuthority('blog:tag:add')")
    @LogAnnotation(module="标签管理",operation= LogConst.INSERT)
    @AccessLimit(seconds = 60, maxCount = 30)
    @PutMapping()
    public ResponseResult<Void> addTag(@RequestBody @Valid TagDTO tagDTO) {
        return tagService.addTag(tagDTO);
    }

    @Operation(summary = "获取标签列表")
    @PreAuthorize("hasAnyAuthority('blog:tag:list')")
    @LogAnnotation(module="标签管理",operation= LogConst.GET)
    @AccessLimit(seconds = 60, maxCount = 30)
    @GetMapping("/back/list")
    public ResponseResult<List<TagVO>> listArticleTag() {
        return ControllerUtils.messageHandler(() -> tagService.listAllTag());
    }

    @Operation(summary = "搜索标签列表")
    @PreAuthorize("hasAnyAuthority('blog:tag:search')")
    @LogAnnotation(module="标签管理",operation= LogConst.SEARCH)
    @AccessLimit(seconds = 60, maxCount = 30)
    @PostMapping("/back/search")
    public ResponseResult<List<TagVO>> searchTag(@RequestBody SearchTagDTO searchTagDTO) {
        return ControllerUtils.messageHandler(() -> tagService.searchTag(searchTagDTO));
    }

    @Operation(summary = "根据id查询标签")
    @PreAuthorize("hasAnyAuthority('blog:tag:search')")
    @LogAnnotation(module="标签管理",operation= LogConst.GET)
    @AccessLimit(seconds = 60, maxCount = 30)
    @GetMapping("/back/get/{id}")
    public ResponseResult<TagVO> getTagById(@PathVariable(value = "id") Long id) {
        return ControllerUtils.messageHandler(() -> tagService.getTagById(id));
    }

    @Operation(summary = "新增标签-标签列表")
    @PreAuthorize("hasAnyAuthority('blog:tag:add')")
    @LogAnnotation(module="标签管理",operation= LogConst.INSERT)
    @AccessLimit(seconds = 60, maxCount = 30)
    @PutMapping("/back/add")
    public ResponseResult<Void> addOrUpdateTag(@RequestBody @Valid TagDTO tagDTO) {
        return tagService.addOrUpdateTag(tagDTO.setId(null));
    }

    @Operation(summary = "修改标签")
    @PreAuthorize("hasAnyAuthority('blog:tag:update')")
    @LogAnnotation(module="标签管理",operation= LogConst.UPDATE)
    @AccessLimit(seconds = 60, maxCount = 30)
    @PostMapping("/back/update")
    public ResponseResult<Void> updateTag(@RequestBody @Valid TagDTO tagDTO) {
        return tagService.addOrUpdateTag(tagDTO);
    }

    @Operation(summary = "删除标签")
    @PreAuthorize("hasAnyAuthority('blog:tag:delete')")
    @LogAnnotation(module="标签管理",operation= LogConst.DELETE)
    @AccessLimit(seconds = 60, maxCount = 30)
    @DeleteMapping("/back/delete")
    public ResponseResult<Void> deleteTag(@RequestBody List<Long> ids) {
        return tagService.deleteTagByIds(ids);
    }
}
