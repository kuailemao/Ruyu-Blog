package xyz.kuailemao.domain.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

/**
 * @author kuailemao
 * <p>
 * 创建时间：2023/12/11 20:15
 */
@Data
public class LoginLogDeleteDTO {
    @NotNull
    List<Long> Ids;
}
