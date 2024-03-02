<script setup lang="ts">
import {MdPreview} from 'md-editor-v3';
import 'md-editor-v3/lib/preview.css';
import useUserStore from "@/store/modules/user.ts";
import {CircleCloseFilled, MessageBox, StarFilled} from "@element-plus/icons-vue";
import {fetchEventSource} from '@microsoft/fetch-event-source'
import {Ref} from "vue";
import {ElMessage, ElMessageBox, ElNotification, ElScrollbar} from "element-plus";
import {deleteGptSession, queryGptSessionDetail, queryGptSessionList, saveGptSession} from "@/apis/gpt";

const userStore = useUserStore()
const gptSessionList = ref([])

onMounted(() => {
  selectSessionList()
})

function selectSessionList() {
  queryGptSessionList().then(res => {
    console.log(res)
    if (res.code === 200) {
      gptSessionList.value = res.data
    }
  })
}

const form = ref({
  content: "",
  model: "gpt-3.5-turbo-0613"
})

// 对话内容
const conversation: Ref<{ content: Array<any>, model: string }> = ref({
  content: [],
  model: form.value.model
})

// 是否对话
const isConversation = ref(false)
// 是否显示停止
const isShowStop = ref(false)
// 是否禁用
const isDisabled = ref(false)

const controller = new AbortController()
const signal = controller.signal

const contentScroll = ref<InstanceType<typeof ElScrollbar>>()

// 保存会话
function saveConversation() {
  saveGptSession(conversation.value).then(res => {
    // 成功
    if (res.code === 200) {
      ElMessage.success(res.data)
      selectSessionList()
    }
    // 错误
    if (res.code === 500) {
      ElMessage.error(res.data)
    }
    // 限流，未登录
    if (res.code === 1004 || res.code === 1002) {
      ElMessage.warning(res.msg)
    }
    // 会话上限
    if (res.code === 1009) {
      ElMessage.warning(res.msg)
    }
  })
}

function initSSE() {
  ElNotification({
    title: '穷!!!',
    showClose: false,
    duration: 6000,
    message: '该模块未开放，作者没有钱',
    type: 'error',
  })
  return;
  if (form.value.content === "") {
    ElMessage.warning("请输入内容")
    return
  }

  isConversation.value = true
  conversation.value.content.push({
    type: "question",
    content: form.value.content
  })

  fetchEventSource(import.meta.env.VITE_SERVE + '/chatGpt/openai/chat', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
      'Authorization': 'Bearer ' + userStore.token
    },
    openWhenHidden: true,
    signal,
    body: JSON.stringify(conversation.value),
    onmessage: event => {
      const data = JSON.parse(event.data)
      if (data.code === 500) {
        conversation.value.content[conversation.value.content.length - 1].content += data.data
        conversation.value.content[conversation.value.content.length - 1].code = 500
        return
      }
      conversation.value.content[conversation.value.content.length - 1].content += data
    },
    // SSE连接已打开
    onopen() {
      isDisabled.value = true
      isShowStop.value = true
      conversation.value.content.push({
        type: "response",
        content: ""
      })
      return Promise.resolve()
    },
    // SSE连接断开
    onerror(error) {
      isDisabled.value = false
      console.log(error)
    },
    onclose() {
      isDisabled.value = false
      isShowStop.value = false
      contentScroll.value!.wrapRef!.scrollTop = contentScroll.value!.wrapRef!.scrollHeight
    },
  })
  form.value.content = ""
}

// 关闭连接
function closeSSE() {
  controller.abort()
  isShowStop.value = false
}

// 回到会话
function backToConversation(id: string) {
  if (conversation.value.id === id) {
    ElMessage.warning("已在当前会话")
    return
  }
  queryGptSessionDetail(id).then(res => {
    if (res.code === 200) {
      isConversation.value = true
      conversation.value = JSON.parse(res.data.conversation)
      conversation.value.id = res.data.id
      ElMessage.success("成功回到会话")
    } else {
      ElMessage.error(res.msg)
    }
  })
}

// 删除会话
function deleteConversation(id: string, event: any) {
  // 阻止向上冒泡事件
  event.stopPropagation()
  // 是否删除
  ElMessageBox.confirm('此操作将永久删除该会话, 是否继续?', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(() => {
    deleteGptSession(id).then(res => {
      if (res.code === 200) {
        ElMessage.success("已删除会话")
        selectSessionList()
      } else {
        ElMessage.error(res.msg)
      }
    })
  }).catch(() => {
    ElMessage.info('已取消删除')
  })
}

</script>

<template>
  <div class="parent_container">
    <div class="container">
      <div class="left_card">
        <div class="left_header">
          <div class="title">历史回话列表</div>
          <div class="desc">你可以快速从云端回复保持的某一时刻会话，云端会话最多保持20条记录</div>
        </div>
        <el-divider/>
        <div class="cards">
          <el-scrollbar height="100%">
            <template v-for="session in gptSessionList" :key="session.id">
              <div style="margin: 0.4rem" @click="backToConversation(session.id)">
                <el-col>
                  <el-card shadow="hover" class="history_card">
                    <div class="history_title">
                      {{ session.conversationTitle }}
                    </div>
                    <div>
                      <div>{{ session.createTime }}</div>
                      <div>
                        <el-button type="danger" size="small" plain @click="deleteConversation(session.id,$event)">删除
                        </el-button>
                      </div>
                    </div>
                  </el-card>
                </el-col>
              </div>
            </template>
          </el-scrollbar>
        </div>
      </div>
      <div class="right_card">
        <div v-if="!isConversation" class="right_header">
          <div>ChatGPT</div>
          <div>
            <span>当前模型为：</span>
            <el-select v-model="conversation.model" placeholder="gpt-3.5-turbo-1106">
              <el-option label="gpt-3.5-turbo" value="gpt-3.5-turbo-0613"/>
              <el-option label="gpt-3.5-turbo-16k" value="gpt-3.5-turbo-16k-0613"/>
              <el-option label="gpt-4" value="gpt-4"/>
              <el-option label="gpt-4-0613" value="gpt-4-0613"/>
            </el-select>
          </div>
        </div>
        <div v-if="!isConversation" class="right_content">
          <div>
            <div class="header">
              示例问题
            </div>
            <div>
              "成都今天的天气怎么样，适合出门运动吗?"
            </div>
            <div>
              "对于我的十岁生日你有什么好点子吗?"
            </div>
            <div>
              "我该如何使用JavaScript来发起一个HTTP请求?"
            </div>
          </div>
          <div>
            <div class="header">
              大致功能
            </div>
            <div>
              记录本次对话所有内容，并根据上下文进行回答
            </div>
            <div>
              在后续对话中用户可以根据情况提出更正
            </div>
            <div>
              对于重要的聊天记录可以保存
            </div>
          </div>
          <div>
            <div class="header">
              使用限制
            </div>
            <div>
              对于某些问题可能会回答出不太正确的结果
            </div>
            <div>
              GPT4.0使用次数限制
            </div>
            <div>
              限制每分钟10次问答
            </div>
          </div>
        </div>
        <transition name="el-fade-in-linear">
          <el-scrollbar v-if="isConversation" ref="contentScroll">
            <template v-for="item in conversation.content">
              <div class="questions_container">
                <template v-if="item.type === 'question'">
                  <div class="left_questions">
                    <el-avatar size="default"
                               :src="userStore.userInfo.avatar"></el-avatar>
                    <div class="questions_content">
                      <div class="name">
                        {{ userStore.userInfo.nickname }}
                      </div>
                      <div class="text">
                        {{ item.content }}
                      </div>
                    </div>
                  </div>
                </template>
                <template v-if="item.type === 'response'">
                  <div class="right_questions">
                    <el-avatar size="default"
                               src="http://43.136.78.47:9000/blog/gpt/chatgpt-de79290a.png"></el-avatar>
                    <div class="questions_content">
                      <div class="name">
                        人工智能助手 (模型: {{ conversation.model }})
                      </div>
                      <div class="text">
                        <MdPreview :model-value="item.content"/>
                      </div>
                    </div>
                  </div>
                </template>
              </div>
            </template>
          </el-scrollbar>
        </transition>
        <div class="conversation" v-if="!isShowStop && isConversation" @click="saveConversation">
          <el-link type="primary" :icon="StarFilled">将本次会话保存到云端</el-link>
        </div>
        <div class="conversation" v-if="isShowStop">
          <el-link :icon="CircleCloseFilled" @click="closeSSE">停止</el-link>
        </div>
        <div class="right_tail">
          <div class="btn">
            <el-input
                placeholder="进行提问吧！！"
                type="textarea"
                v-model="form.content"
                :autosize="{ minRows: 1, maxRows: 4 }"
                maxlength="5000"
                show-word-limit
                resize="none"
                :disabled="isDisabled"
            ></el-input>
            <el-button type="primary" plain style="margin-left: 1rem" @click="initSSE" :disabled="isDisabled">发送
            </el-button>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped lang="scss">
@import "index";

.conversation {
  text-align: center;
  color: #999;
  margin-top: -2rem;
}

:deep(.md-editor-preview-wrapper) {
  padding: 0.2rem;
}

// h1 - h6
:deep(.default-theme h1) {
  font-size: 1.5em;
}

:deep(.default-theme h2) {
  font-size: 1.3em;
}

:deep(.default-theme h3) {
  font-size: 1.1em;
}

:deep(.default-theme h4) {
  font-size: 1em;
}

:deep(.default-theme h5) {
  font-size: 0.9em;
}

:deep(.default-theme h6) {
  font-size: 0.8em;
}

:deep(.default-theme h1),
:deep(.default-theme h2),
:deep(.default-theme h3),
:deep(.default-theme h4),
:deep(.default-theme h5),
:deep(.default-theme h6) {
  margin: 0;
  margin-top: 1rem;
  line-height: 1.2em;
}

:deep(.default-theme p) {
  padding: 0;
  font-size: 0.85rem;
}

:deep(.default-theme pre) {
  margin: 0;
  background-color: #f6f8fa;
  border-radius: 0.3em;
  padding: 0.4em;
  overflow: auto;
}

</style>