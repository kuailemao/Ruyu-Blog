package xyz.kuailemao.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @author kuailemao
 * @since 2025/1/25 7:25
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PhotoAndAlbumListVO {
    //自增id
    private Long id;
    //创建者id
    private Long userId;
    //名称
    private String name;
    //描述
    private String description;
    //类型（1：相册 2：照片）
    private Integer type;
    //父相册id
    private Long parentId;
    //图片地址
    private String url;
    //是否通过 (0否 1是)
    private Integer isCheck;
    //照片体积大小(kb)
    private Double size;
    //创建时间
    private Date createTime;
    // 相册封面
    private String albumCover;
}
