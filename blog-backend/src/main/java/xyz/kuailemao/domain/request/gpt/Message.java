package xyz.kuailemao.domain.request.gpt;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author kuailemao
 * <p>
 * 创建时间：2023/11/10 14:14
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Message {
    String role;
    String content;
}
