package xyz.kuailemao.domain.request.gpt;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author kuailemao
 * <p>
 * 创建时间：2023/11/10 14:13
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChatGptBody {
    String model;
    boolean stream;
    List<Message> messages;
}
