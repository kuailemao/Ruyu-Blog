package xyz.kuailemao.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Objects;

/**
 * @author kuailemao
 * <p>
 * 创建时间：2023/12/6 11:30
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PermissionMenuVO {
    //权限表id
    private Integer id;
    // 菜单名称
    private String menuName;
    // 菜单id（用来去重）
    private Long menuId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PermissionMenuVO that = (PermissionMenuVO) o;
        return Objects.equals(menuId, that.menuId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(menuId);
    }
}
