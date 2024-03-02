package xyz.kuailemao.service.impl;

import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import okio.BufferedSource;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import xyz.kuailemao.constants.SQLConst;
import xyz.kuailemao.domain.dto.ChatDTO;
import xyz.kuailemao.domain.dto.ChatGptIsCheckDTO;
import xyz.kuailemao.domain.dto.SearchChatGptDTO;
import xyz.kuailemao.domain.entity.ChatGpt;
import xyz.kuailemao.domain.entity.User;
import xyz.kuailemao.domain.request.gpt.ChatGptBody;
import xyz.kuailemao.domain.request.gpt.Message;
import xyz.kuailemao.domain.response.ResponseResult;
import xyz.kuailemao.domain.vo.ChatGptListVO;
import xyz.kuailemao.domain.vo.ChatSessionDetailVO;
import xyz.kuailemao.domain.vo.ChatSessionVO;
import xyz.kuailemao.enums.RespEnum;
import xyz.kuailemao.mapper.ChatGptMapper;
import xyz.kuailemao.mapper.UserMapper;
import xyz.kuailemao.service.ChatGptService;
import xyz.kuailemao.utils.SecurityUtils;
import xyz.kuailemao.utils.StringUtils;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

/**
 * (ChatGpt)表服务实现类
 *
 * @author kuailemao
 * @since 2023-11-07 17:13:42
 */
@Slf4j
@Service("chatGptService")
public class ChatGptServiceImpl extends ServiceImpl<ChatGptMapper, ChatGpt> implements ChatGptService {

    @Resource
    private ChatGptMapper chatGptMapper;

    @Resource
    private UserMapper userMapper;

    @Override
    public SseEmitter sendGptRequest(ChatDTO chatDTO) {
        SseEmitter emitter = new SseEmitter(3 * 60 * 1000L);
        if (SecurityUtils.isLogin()) {
            log.info("用户：{}，请求聊天", SecurityUtils.getUserId());
            new Thread(() -> {
                try {
                    // TODO 接口管理待优化
//                    String uri = "https://api.qaqgpt.com/v1/chat/completions";
                    String uri = "https://openkey.cloud/v1/chat/completions";
                    OkHttpClient client = new OkHttpClient();
                    ChatGptBody chatBody = ChatGptBody.builder().model(chatDTO.getModel())
                            .stream(true)
                            .messages(chatDTO.getContent().stream().map(content -> {
                                if (content.getType().equals("question")) {
                                    return new Message("user", content.getContent());
                                } else if (content.getType().equals("response")) {
                                    return new Message("assistant", content.getContent());
                                }
                                return null;
                            }).toList()).build();
                    RequestBody body = RequestBody.create(
                            MediaType.parse("application/json; charset=utf-8"),
                            JSONObject.toJSONString(chatBody)
                    );

                    Request request = new Request.Builder()
                            .url(uri)
                            .post(body)
                            // TODO key 管理待优化
//                            .addHeader("Authorization", "Bearer sk-QzMbpo6w6f6C8XuL1835D6C8F7De4d188347Aa4d922b1771")
                            .addHeader("Authorization", "Bearer sk-ssGcQag6c6xhM7f84ZbVQHH6JPP04Lf4aQzFPCwmlrYXwtMO")
                            .build();
                    // 异步返回
                    client.newCall(request).enqueue(new Callback() {

                        // 失败
                        @Override
                        public void onFailure(Call call, IOException e) {
                            emitter.completeWithError(e);
                        }

                        // 成功
                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
                            if (response.body() != null) {
                                try (BufferedSource source = response.body().source()) {
                                    while (!source.exhausted()) {
                                        // 保留换行符
                                        String line = source.readUtf8LineStrict();
                                        // 保留换行符与空格
                                        // 去掉前面5个字符
                                        if (line != null && !line.equals("data: [DONE]") && !line.isEmpty()) {
                                            // 以data开头
                                            if (line.startsWith("data: ")) {
                                                line = line.substring(6);
                                            }
                                            JSONObject jsonObject = JSONObject.parseObject(line);
                                            if (jsonObject != null && jsonObject.get("error") != null && !jsonObject.get("error").toString().isEmpty()) {
                                                String error = (String) jsonObject.getJSONObject("error").get("message");
                                                emitter.send(error);
                                                log.error("gpt响应错误：{}", error);
                                            } else {
                                                if (jsonObject != null && jsonObject.getJSONArray("choices") != null) {
                                                    Object obj = jsonObject.getJSONArray("choices").getJSONObject(0).getJSONObject("delta").get("content");
                                                    String jsonString = JSONObject.toJSONString(obj);
                                                    // 在这里进行正则处理
                                                    if (obj != null) {
                                                        emitter.send(jsonString);
                                                    }
                                                }
                                            }
                                        }
                                    }
                                } catch (Exception e) {
                                    ResponseResult<String> failure = ResponseResult.failure("gpt响应错误，请联系管理员！");
                                    String jsonString = JSONObject.toJSONString(failure);
                                    emitter.send(jsonString);
                                    log.error("gpt响应错误：{}", e.getMessage());
                                }
                            }
                            emitter.complete();
                        }
                    });
                } catch (Exception ex) {
                    emitter.completeWithError(ex);
                }
            }).start();
            return emitter;
        }
        try {
            ResponseResult<String> failure = ResponseResult.failure("请先登录在提问我噢，宝贝！");
            String jsonString = JSONObject.toJSONString(failure);
            emitter.send(jsonString);
            emitter.complete();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return emitter;
    }

    @Override
    public ResponseResult<String> saveChat(ChatDTO chatDTO) {
        // 查询用户保存会话数量
        long count = this.count(new LambdaQueryWrapper<ChatGpt>().eq(ChatGpt::getUserId, SecurityUtils.getUserId()));
        if (count >= 20)
            return ResponseResult.failure(RespEnum.SESSION_LIMIT.getCode(), RespEnum.SESSION_LIMIT.getMsg());
        ChatGpt gpt = ChatGpt.builder().userId(SecurityUtils.getUserId()).conversation(JSONObject.toJSONString(chatDTO)).build();
        if (save(gpt)) return ResponseResult.success("会话保存成功！");
        return ResponseResult.failure("会话保存失败");
    }

    @Override
    public List<ChatSessionVO> queryChatList() {
        List<ChatGpt> gptSession = this.list(new LambdaQueryWrapper<ChatGpt>().eq(ChatGpt::getUserId, SecurityUtils.getUserId()).eq(ChatGpt::getIsCheck, SQLConst.IS_CHECK_YES));
        List<ChatSessionVO> dtoList = gptSession.stream().map(session -> {
            JSONObject jsonObject = JSONObject.parseObject(session.getConversation());
            return ChatSessionVO.builder().id(session.getId()).createTime(session.getCreateTime())
                    .conversationTitle(jsonObject.getJSONArray("content")
                            .getJSONObject(0).getString("content")).build();
        }).toList();
        return dtoList;
    }

    @Override
    public ChatSessionDetailVO queryChatDetail(Long id) {
        ChatGpt chat = chatGptMapper.selectOne(new LambdaQueryWrapper<ChatGpt>().eq(ChatGpt::getUserId, SecurityUtils.getUserId()).eq(ChatGpt::getId, id));
        return ChatSessionDetailVO.builder().id(chat.getId()).conversation(chat.getConversation()).createTime(chat.getCreateTime()).build();
    }

    @Override
    public ResponseResult<Void> deleteChat(Long id) {
        if (chatGptMapper.deleteById(id) > 0) return ResponseResult.success();
        return ResponseResult.failure();
    }

    @Override
    public List<ChatGptListVO> getBackChatGptList(SearchChatGptDTO searchDTO) {
        LambdaQueryWrapper<ChatGpt> wrapper = new LambdaQueryWrapper<>();
        if (StringUtils.isNotNull(searchDTO)) {
            // 搜索
            List<User> users = userMapper.selectList(new LambdaQueryWrapper<User>().like(User::getUsername, searchDTO.getUserName()));
            if (!users.isEmpty())
                wrapper.in(StringUtils.isNotEmpty(searchDTO.getUserName()), ChatGpt::getUserId, users.stream().map(User::getId).collect(Collectors.toList()));
            else
                wrapper.eq(StringUtils.isNotNull(searchDTO.getUserName()), ChatGpt::getUserId, null);

            wrapper.eq(StringUtils.isNotNull(searchDTO.getIsCheck()), ChatGpt::getIsCheck, searchDTO.getIsCheck());
            if (StringUtils.isNotNull(searchDTO.getStartTime()) && StringUtils.isNotNull(searchDTO.getEndTime()))
                wrapper.between(ChatGpt::getCreateTime, searchDTO.getStartTime(), searchDTO.getEndTime());
        }
        List<ChatGpt> chatGpts = chatGptMapper.selectList(wrapper);
        if (!chatGpts.isEmpty()) {
            return chatGpts.stream().map(chatGpt -> {
                User user = userMapper.selectOne(new LambdaQueryWrapper<User>().eq(User::getId, chatGpt.getUserId()));
                return chatGpt.asViewObject(ChatGptListVO.class, v -> {
                    v.setUserName(user.getUsername());
                    v.setAvatar(user.getAvatar());
                });
            }).collect(Collectors.toList());
        }
        return null;
    }

    @Override
    public ResponseResult<Void> isCheckChatGpt(ChatGptIsCheckDTO isCheckDTO) {
        if (chatGptMapper.updateById(ChatGpt.builder().id(isCheckDTO.getId()).isCheck(isCheckDTO.getIsCheck()).build()) > 0)
            return ResponseResult.success();

        return ResponseResult.failure();
    }

    @Override
    public ResponseResult<Void> deleteChatGpt(List<Long> ids) {
        if (chatGptMapper.deleteBatchIds(ids) > 0) return ResponseResult.success();
        return ResponseResult.failure();
    }
}
