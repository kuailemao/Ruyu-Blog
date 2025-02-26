<script setup lang="ts">
import {ElMessage} from "element-plus";
import {addComment} from "@/apis/article";
import EmojiPicker from './EmojiPicker.vue';

const route = useRoute()

const prop = defineProps({
  comment: {
    type: Object,
    required: true
  },
  getComments: {
    type: Function,
    required: true
  },
  pageSize: {
    type: Number,
    required: true
  },
  type: Number,
  activeCommentId: {
    type: [Number, null],
    default: null
  },
  setActiveComment: {
    type: Function,
    required: true
  }
})

// 评论框
const myInput = ref();

// 添加引用到弹出框的DOM元素
const emojiPopover = ref(null);

onMounted(() => {
  myInput.value.addEventListener('focus', function () {
    myInput.value.classList.add('active');
  });
  myInput.value.addEventListener('blur', function () {
    if (prop.comment.replyText === '' || prop.comment.replyText === undefined) {
      myInput.value.classList.remove('active');
    }
  });
  
  // 全局事件监听，确保弹出窗口内的任何点击都不会导致文本框失焦
  document.addEventListener('mousedown', (event) => {
    // 获取emoji弹出框
    const popover = document.querySelector('.emoji-popover-container');
    
    // 如果点击发生在弹出框内部，阻止默认行为并保持焦点
    if (popover && popover.contains(event.target)) {
      // 阻止鼠标点击从弹出框传播到文档
      event.preventDefault();
      event.stopPropagation();
      
      // 确保文本框保持焦点
      setTimeout(() => {
        myInput.value.focus();
      }, 10);
    }
  }, true); // 使用捕获阶段处理事件
})

function addEmoji(emoji: string, comment: object) {
  // 在对应焦点的位置插入
  if (myInput.value.selectionStart || myInput.value.selectionStart === 0) {
    const startPos = myInput.value.selectionStart
    const endPos = myInput.value.selectionEnd
    const restoreTop = myInput.value.scrollTop
    myInput.value.value = myInput.value.value.substring(0, startPos) + emoji + myInput.value.value.substring(endPos, myInput.value.value.length)
    if (restoreTop > 0) {
      myInput.value.scrollTop = restoreTop
    }
    myInput.value.focus()
    myInput.value.selectionStart = startPos + emoji.length
    myInput.value.selectionEnd = startPos + emoji.length
  } else {
    myInput.value.value += emoji
    myInput.value.focus()
  }
  comment.replyText = myInput.value.value
}

// 添加子评论
function addChildComment(comment: any) {
  if (comment.replyText === '' || comment.replyText === undefined) {
    ElMessage.warning('评论内容不能为空')
    return
  }
  const data = {
    type: prop.type,
    typeId: comment.typeId,
    commentContent: comment.replyText,
    parentId: comment.parentId == null ? comment.id : comment.parentId,
    replyId: comment.id,
    replyUserId: comment.commentUserId
  }
  addComment(data).then(res => {
    if (res.code === 200) {
      ElMessage.success("回复成功");
      if (res.data) {
        ElNotification({
          title: '回复成功',
          duration: 4000,
          type: 'warning',
          message: h('i', { style: 'color: teal' }, res.data),
        })
      }
      comment.replyText = ''
      prop.getComments(route.params.id, '1', prop.pageSize)
    } else if (res.code === 1002) {
      ElMessage.error(res.msg);
    }
  })
}

// 防止事件传播和默认行为
function preventDefaultAndKeepFocus(event) {
  // 阻止事件冒泡
  event.stopPropagation();
  
  // 添加一点延迟
  setTimeout(() => {
    // 让回复框重新获得焦点
    myInput.value.focus();
  }, 10);
}

// 修改方法，确保选择表情后回复框保持焦点
function handleEmojiSelect(emoji: string) {
  if (prop.activeCommentId === prop.comment.id) {
    addEmoji(emoji, prop.comment);
    nextTick(() => {
      myInput.value.focus();
    });
  }
}

// 点击表情按钮时自动聚焦回复框
function handleEmojiButtonClick(event: Event) {
  event.stopPropagation();
  event.preventDefault();
  
  if (prop.comment.showReplyBox) {
    prop.setActiveComment(prop.comment.id);
    myInput.value.focus();
    myInput.value.classList.add('active');
  }
}

// 在EmojiPicker操作完成时保持焦点
function handleOperationComplete(event: Event) {
  // 阻止事件冒泡
  event?.stopPropagation();
  
  setTimeout(() => {
    // 只有当这个回复框是显示状态时才聚焦
    if (prop.comment.showReplyBox) {
      myInput.value.focus();
    }
  }, 10);
}
</script>

<template>
  <transition name="el-zoom-in-top">
    <div class="reply" v-show="comment.showReplyBox">
      <textarea style="color: #7B5F69;" ref="myInput" v-model="comment.replyText" :placeholder="'@'+comment.commentUserNickname"/>
      <div class="reply-footer">
        <div class="reply-tools">
          <EmojiPicker 
            ref="emojiPopover"
            :popover-width="510" 
            @select-emoji="handleEmojiSelect"
            @operation-complete="handleOperationComplete"
            @mousedown.stop.prevent
            @click.stop.prevent
          >
            <template #trigger>
              <div 
                class="emoji-trigger-btn" 
                @click.stop.prevent="handleEmojiButtonClick"
                @mousedown.stop.prevent
              >
                <svg-icon name="emojis" class="emoji-icon"/>
                <span class="emoji-ripple"></span>
              </div>
            </template>
          </EmojiPicker>
        </div>
        <el-button type="danger" plain size="small" @click="addChildComment(comment)">发布</el-button>
      </div>
    </div>
  </transition>
</template>

<style scoped lang="scss">
.reply {
  .active {
    height: 4rem;
    background: white;
    border: 1px solid #d09aae;
  }

  textarea {
    width: 100%;
    height: 2.2rem;
    transition: height 0.5s;
    border: 1px solid #ebebeb;
    border-radius: 0.5rem;
    padding: 0.5rem;
    resize: none;
    outline: none;
    font-size: 0.8rem;
    margin-top: 1rem;
    background: #f8f8f8;

    &::-webkit-input-placeholder {
      font-size: 0.8rem;
      font-weight: bold;
      color: #7B5F69;
    }
  }

  .reply-footer {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-top: 0.5rem;
  }
}

/* 表情按钮样式 */
.reply-tools {
  display: flex;
  align-items: center;

  :deep(.el-button) {
    border: none;
    padding: 8px;
    height: auto;
    
    &:hover {
      background-color: var(--el-fill-color-light);
    }
  }

  .emoji-trigger-btn {
    display: flex;
    align-items: center;
    justify-content: center;
    cursor: pointer;
    width: 32px;
    height: 32px;
    border-radius: 4px;
    transition: all 0.3s;
    
    &:hover {
      background-color: var(--el-fill-color-light);
    }

    .emoji-icon {
      font-size: 1.25em;
      color: var(--el-text-color-regular);
    }
  }
}

/* 改进回复框的活跃状态样式 */
.reply {
  textarea:focus {
    height: 4rem;
    background: white;
    border: 1px solid #d09aae;
  }
}
</style>