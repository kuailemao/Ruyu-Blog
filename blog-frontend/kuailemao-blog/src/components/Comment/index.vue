<script setup lang="ts">
import {MdPreview} from 'md-editor-v3';
import 'md-editor-v3/lib/preview.css';
import EmojiPicker from './EmojiPicker.vue';
import {heo} from "@/utils/O.o/heo.ts";

import {
  addComment,
  getComment
} from "@/apis/article";
import {cancelLike, isLike, userLike} from '@/apis/like'
import ChildComment from "./ChildComment.vue";
import {ElMessage} from "element-plus";
import {useColorMode} from "@vueuse/core";

const props = defineProps({
  authorId: {
    type: Number,
    required: true
  },
  typeId: {
    type: Number,
    required: true
  },
  // 是否显示头部添加框
  isShowHeader: {
    type: Boolean,
    default: true
  },
  // 评论类型
  type: Number,
  // 点赞类型
  likeType: Number
})

// 输入框显示的内容
const textarea = ref(``);
// 预览框显示的内容
const preview = ref(``);
// 文本框
const myInput = ref();
// 是否加载
const isLoading = ref(false)

// 添加一个 ref 来跟踪当前活动的评论框
const activeCommentId = ref(null);

// 添加设置活动评论框的方法
function setActiveComment(id: number | null) {
  activeCommentId.value = id;
}

// 修改父评论的表情按钮点击处理
function handleParentEmojiButtonClick(event: Event) {
  event.stopPropagation();
  event.preventDefault();
  
  // 关闭所有回复框
  if (comments.value) {
    comments.value.forEach((comment: any) => {
      comment.showReplyBox = false;
      if (comment.childComment && comment.childComment.length) {
        closeAllReplyBoxes(comment.childComment);
      }
    });
  }
  
  // 重置活动评论框
  setActiveComment(null);
  // 聚焦到父评论输入框
  myInput.value.focus();
}

// 修改父评论的表情选择处理
function handleEmojiSelect(emoji: string) {
  // 如果没有活动的评论框，则是父评论
  if (!activeCommentId.value) {
    let start = myInput.value.selectionStart;
    let end = myInput.value.selectionEnd;
    textarea.value = textarea.value.substring(0, start) + emoji + textarea.value.substring(end, textarea.value.length);
    nextTick(() => {
      myInput.value.focus();
      myInput.value.selectionStart = start + emoji.length;
      myInput.value.selectionEnd = start + emoji.length;
    });
  }
}

// 获取选项卡div
const comments = ref()
const commentsTotal = ref(0)
// 是否预览
const isPreview = ref(false)
// 是否展示全部子评论
const showAllChildComments = ref(false)
// 查询评论数
const pageSize = ref(2)

const mode = useColorMode()

// 默认选中第一个
onMounted(() => {
  getComments(props.typeId, '1', String(pageSize.value))
})

// 用户预览
watch(() => textarea.value, (value) => {
  preview.value = parsingCommentsFunc(value);
})

// 解析评论
function parsingComments(value: string) {
  return parsingCommentsFunc(value);
}

// 解析md公共方法
function parsingCommentsFunc(value: string) {
  const codeBlockRegex = /```[\s\S]*?```/g;
  const codeBlocks = value.match(codeBlockRegex);
  let protectedValue = value;

  // 保护代码块内容
  if (codeBlocks) {
    codeBlocks.forEach((block, index) => {
      protectedValue = protectedValue.replace(block, `{{CODE_BLOCK_${index}}}`);
    });
  }

  const matches = protectedValue.match(/\[[^\]]+\]/g);

  // 判断是否有表情包
  if (matches) {
    // 遍历是否存在表情包
    for (let i = 0; i < matches.length; i++) {
      const match = matches[i];
      if (heo[match]) {
        // 有，替换 heo[match]
        protectedValue = protectedValue.replace(match, `<span><img src="${heo[match]}" width="24" height="24" alt="Ruyu-blog-[1231256151315612]" /></span>`);
      }
    }
  }

  // 恢复代码块内容
  if (codeBlocks) {
    codeBlocks.forEach((block, index) => {
      protectedValue = protectedValue.replace(`{{CODE_BLOCK_${index}}}`, block);
    });
  }
  return protectedValue;
}

function handleComments(commentContent: object[]) {
  commentContent.forEach((item: any) => {
    item.commentContent = parsingComments(item.commentContent)
    if (item.childComment && item.childComment.length) {
      handleComments(item.childComment)
    }
  })
}

// 更多评论
const moreComment = () => {
  getComments(props.typeId, '1', String(pageSize.value += 3))
}

//  递归子评论方法
function recursionChildComment(childComment: any, res: any) {
  res.data.forEach((item: any) => {
    childComment.forEach((child: any) => {
      if (child.id === item.typeId) {
        child.isLike = true
      }
      if (child.childComment && child.childComment.length) {
        recursionChildComment(child.childComment, res)
      }
    })
  })
}

// 修改 replyBtn 函数
function replyBtn(comment: object[], id: number) {
  // 在对应评论项目显示回复框
  comment.forEach((item: any) => {
    if (item.id === id) {
      // 如果正在打开回复框，设置为活动评论
      if (!item.showReplyBox) {
        setActiveComment(id);
      } else {
        // 如果正在关闭回复框，清除活动评论
        setActiveComment(null);
      }
      item.showReplyBox = !item.showReplyBox;
    } else {
      // 关闭其他所有回复框
      item.showReplyBox = false;
    }
    if (item.childComment && item.childComment.length) {
      recursionReplyBtn(item.childComment, id);
    }
  });
}

// 修改递归函数也需要同样的逻辑
function recursionReplyBtn(childComment: object[], id: number) {
  childComment.forEach((child: any) => {
    if (child.id === id) {
      if (!child.showReplyBox) {
        setActiveComment(id);
      } else {
        setActiveComment(null);
      }
      child.showReplyBox = !child.showReplyBox;
    } else {
      child.showReplyBox = false;
    }
    if (child.childComment && child.childComment.length) {
      recursionReplyBtn(child.childComment, id);
    }
  });
}

// 添加一个辅助函数来关闭所有回复框
function closeAllReplyBoxes(comments: any[]) {
  comments.forEach(comment => {
    comment.showReplyBox = false;
    if (comment.childComment && comment.childComment.length) {
      closeAllReplyBoxes(comment.childComment);
    }
  });
}

watch(() => props.typeId, (value) => {
  getComments(value, '1', String(pageSize.value))
})

// 获取文章评论
function getComments(typeId: number, pageNum: string, pageSize: string) {
  getComment(<number>props.type, typeId, pageNum, pageSize).then(res => {
    if (res.code == 200) {
      isLoading.value = true
      comments.value = res.data.page
      commentsTotal.value = res.data.total
      handleComments(res.data.page)
      isLikeFunc()
    }
  })
}

// 点赞
function likeBtn(comment: object) {
  userLike(<number>props.likeType, comment.id).then(res => {
    if (res.code === 200) {
      comment.isLike = true
      comment.likeCount += 1
      ElMessage.success("点赞成功");
    } else {
      ElMessage.error(res.msg);
    }
  })
}

// 取消点赞
function cancelLikeBtn(comment: object) {
  cancelLike(<number>props.likeType, comment.id).then(res => {
    if (res.code === 200) {
      comment.isLike = false
      comment.likeCount -= 1
      ElMessage.info("取消点赞");
    } else {
      ElMessage.error(res.msg);
    }
  })
}

function isLikeFunc() {
  isLike(<number>props.likeType).then(res => {
    if (res.code === 200) {
      res.data.forEach((item: any) => {
        comments.value.forEach((comment: any) => {
          if (comment.id === item.typeId) {
            comment.isLike = true
          }
          // 递归子评论
          if (comment.childComment && comment.childComment.length) {
            recursionChildComment(comment.childComment, res)
          }
        })
      })
    }
  })
}

// 添加父评论
function addParentComment() {
  if (textarea.value === '') {
    ElMessage.error("评论内容不能为空");
    return
  }
  let data = {
    type: props.type,
    typeId: props.typeId,
    commentContent: textarea.value
  }
  addComment(data).then((res: any) => {
    if (res.code === 200) {
      ElMessage.success("评论成功");
      if (res.data) {
        ElNotification({
          title: '评论成功',
          duration: 4000,
          type: 'warning',
          message: h('i', { style: 'color: teal' }, res.data),
        })
      }
      textarea.value = ''
      getComments(props.typeId, '1', String(pageSize.value))
    } else if (res.code === 1002) {
      ElMessage.error(res.msg);
    }
  })
}

</script>

<template>
  <div class="comments">
    <div v-if="isShowHeader">
      <div class="comments_title">
        <svg-icon name="comment" width="1.5em" height="1.5em"/>
        <span>评论({{ commentsTotal }})</span>
      </div>
      <div class="form_container">
        <!-- 评论框 -->
        <textarea ref="myInput" class="textarea" v-model="textarea" placeholder="留下你的精彩评论吧！"/>
        <div class="btn">
          <div>
            <EmojiPicker 
              :popover-width="510" 
              @select-emoji="handleEmojiSelect"
              @mousedown.stop
              @click.stop
            >
              <template #trigger>
                <div 
                  class="emoji-trigger-btn" 
                  @click.stop="handleParentEmojiButtonClick"
                  @mousedown.stop
                >
                  <svg-icon name="emojis" class="emoji-icon"/>
                </div>
              </template>
            </EmojiPicker>
          </div>
          <div>
            <el-button type="info" plain size="small" @click="isPreview=!isPreview">预览</el-button>
            <el-button type="success" plain size="small" @click="addParentComment">发布</el-button>
          </div>
        </div>
        <!-- 预览 -->
        <div class="preview" v-if="isPreview">
          <MdPreview :modelValue="preview" :theme="mode"/>
        </div>
      </div>
    </div>
    <!-- 评论内容 -->
    <div>
      <div style="display: flex;margin-top: 1rem;border-top: 1px solid var(--el-border-color);
  padding-top: 1rem;" v-for="comment in comments">
        <el-avatar shape="square" :size="40"
                   :src="comment.commentUserAvatar"/>
        <div class="comment_content">
          <div class="comment_content_header">
            <div>
              <div>{{ comment.commentUserNickname }}</div>
              <div v-if="comment.commentUserId === authorId">
                <el-tag size="small">作者</el-tag>
              </div>
              <div>{{ comment.createTime }}</div>
            </div>
            <div>
              <SvgIcon @click="likeBtn(comment)" v-show="!comment.isLike" name="like" style="cursor: pointer"/>
              <SvgIcon @click="cancelLikeBtn(comment)" v-show="comment.isLike" name="like-selected"
                       style="cursor: pointer"/>
              <span style="font-size: 0.8em;color: grey">{{ comment.likeCount }}</span>
              <svg-icon @click="replyBtn(comments,comment.id)" name="comment"
                        style="margin:0 0.2em 0 0.5rem;cursor: pointer"/>
              <span style="font-size: 0.8em;color: grey">{{ comment.childCommentCount }}</span>
            </div>
          </div>
          <!-- 父评论 -->
          <div class="comment_content_body">
            <div>
              <MdPreview :modelValue="comment.commentContent"/>
            </div>
          </div>
          <!-- TODO 评论信息 -->
          <!--          <div class="comment_content_footer">-->
          <!--            <div><span><SvgIcon name="windows_icon"/></span>window 11</div>-->
          <!--            <div><span><SvgIcon name="google_icon"/></span>google chrome</div>-->
          <!--            <div><span><SvgIcon name="address_icon"/></span>北京</div>-->
          <!--          </div>-->
          <!-- 回复框 -->
          <template v-if="isLoading">
            <ReplyBox 
              :type="type" 
              :comment="comment" 
              :get-comments="getComments" 
              :page-size="pageSize"
              :active-comment-id="activeCommentId"
              :set-active-comment="setActiveComment"
            />
          </template>
          <!-- 子评论 -->
          <template v-if="comment.childComment && comment.childComment.length">
            <ChildComment 
              :reply-btn="replyBtn" 
              :like-btn="likeBtn" 
              :cancel-like-btn="cancelLikeBtn" 
              :comment="comment"
              :author-id="authorId"
              :show-all-child-comments="showAllChildComments"
              :get-comments="getComments" 
              :page-size="pageSize"
              :type="type"
              :active-comment-id="activeCommentId"
              :set-active-comment="setActiveComment"
            />
          </template>
          <!-- 查看更多 -->
          <div style="display: flex;justify-content: center;margin-top: 1rem" v-if="comment.childCommentCount >= 2">
            <el-button type="info" plain size="small" @click="showAllChildComments = true" v-if="!showAllChildComments">
              查看全部{{ comment.childCommentCount }}条回复
            </el-button>
            <el-button type="info" plain size="small" @click="showAllChildComments = false" v-if="showAllChildComments">
              收起回复
            </el-button>
          </div>
        </div>
      </div>
    </div>
    <!-- 查看更多 -->
    <div class="more" v-if="isLoading && comments[0]?.parentCommentCount >= pageSize">
      <el-button type="info" plain size="small" @click="moreComment">查看更多</el-button>
    </div>
  </div>
</template>

<style scoped lang="scss">
@import "./index";

.more {
  display: flex;
  justify-content: center;
  margin-top: 1rem;

  .el-button {
    width: 100%;
  }
}

/* 确保表情按钮在评论组件中也有合适的间距 */
.emoji-trigger-btn {
  margin-right: 0.8rem;
}
</style>