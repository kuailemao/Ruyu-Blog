<script setup lang="ts">

import {MdPreview} from "md-editor-v3";

defineProps({
  comment: {
    type: Object,
    required: true
  },
  authorId: {
    type: Number,
    required: true
  },
  showAllChildComments: {
    type: Boolean,
    required: false
  },
  likeBtn: {
    type: Function,
    required: true
  },
  cancelLikeBtn: {
    type: Function,
    required: true
  },
  replyBtn: {
    type: Function,
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

</script>

<template>
  <div v-for="(child,index) in comment.childComment">
    <div v-if="index < 1 || showAllChildComments">
      <div class="parent_container" :key="child.id">
        <el-avatar shape="square" :size="40"
                   :src="child.commentUserAvatar"/>
        <div class="comment_content">
          <div class="comment_content_header">
            <div>
              <div>{{ child.commentUserNickname }}</div>
              <div v-if="child.commentUserId === authorId">
                <el-tag size="small">作者</el-tag>
              </div>
              <div>{{ child.createTime }}</div>
            </div>
            <div>
              <SvgIcon @click="likeBtn(child)" v-show="!child.isLike" name="like" style="cursor: pointer"/>
              <SvgIcon @click="cancelLikeBtn(child)" v-show="child.isLike" name="like-selected"
                       style="cursor: pointer"/>
              <span style="font-size: 0.8em;color: grey">{{ child.likeCount }}</span>
              <svg-icon @click="replyBtn(comment.childComment,child.id)" name="comment"
                        style="margin:0 0.2em 0 0.5rem;cursor: pointer"/>
              <span style="font-size: 0.8em;color: grey">{{ child.childCommentCount }}</span>
            </div>
          </div>
          <!-- 父评论 -->
          <div class="comment_content_body">
            <div style="margin: 0.5rem 0">
              <span style="font-weight: bold">回复</span>
              <span class="replyUserNickname"> @{{ child.replyUserNickname }}</span>：
            </div>
            <div>
              <MdPreview :modelValue="child.commentContent"/>
            </div>
          </div>
          <!-- TODO 评论信息 -->
          <!--          <div class="comment_content_footer">-->
          <!--            <div><span><SvgIcon name="windows_icon"/></span>window 11</div>-->
          <!--            <div><span><SvgIcon name="google_icon"/></span>google chrome</div>-->
          <!--            <div><span><SvgIcon name="address_icon"/></span>北京</div>-->
          <!--          </div>-->
          <ReplyBox :type="type" :comment="child" :get-comments="getComments" :page-size="pageSize"
                    :active-comment-id="activeCommentId" :set-active-comment="setActiveComment"/>
        </div>
      </div>
      <div v-if="child.childComment && child.childComment.length">
        <ChildComment :reply-btn="replyBtn" :like-btn="likeBtn" :cancel-like-btn="cancelLikeBtn" :comment="child"
                      :author-id="authorId"
                      :get-comments="getComments" :page-size="pageSize"
                      :type="type"
                      :active-comment-id="activeCommentId"
                      :set-active-comment="setActiveComment"
        />
      </div>
    </div>
  </div>

</template>

<style scoped lang="scss">
@import "./index";

.parent_container {
  display: flex;
  margin-top: 1rem;
  border-top: 1px solid var(--el-border-color);
  padding-top: 1rem;
}
.replyUserNickname{
  color: var(--mao-bg-reply);
  font-weight: bold
}
</style>