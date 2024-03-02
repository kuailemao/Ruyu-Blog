<script setup lang="ts">
import type { ComputedRef } from 'vue'
import { computed } from 'vue'
import type { UserData } from '~/pages/system/user/type.ts'

const props = defineProps({
  modalOpen: {
    type: Boolean,
    default: false,
  },
  data: {
    type: Object,
    default: () => {},
  },
})

const emit = defineEmits(['update:close:modal'])

const modalData: ComputedRef<UserData> = computed(() => props.data) as ComputedRef<UserData>

const modalOpen = computed(() => props.modalOpen)

function handleClose() {
  emit('update:close:modal')
}
</script>

<template>
  <div>
    <a-modal v-model:open="modalOpen" width="900px" @cancel="handleClose">
      <template #footer>
        <a-button @click="handleClose">
          关闭
        </a-button>
      </template>
      <template #title>
        <span style="font-size: 1.2rem">用户详细</span>
      </template>
      <template v-if="modalData">
        <div class="user-detail">
          <div>
            <div>
              <label>用户头像：</label>
              <div style="transform: translateY(-20%)">
                <a-avatar shape="square" size="large" :src="modalData.avatar" alt="无法显示头像" />
              </div>
            </div>
            <div>
              <label>用户性别：</label>
              <template v-if="modalData.gender === 0">
                <div>未知</div>
              </template>
              <template v-if="modalData.gender === 1">
                <div>男</div>
              </template>
              <template v-if="modalData.gender === 2">
                <div>女</div>
              </template>
            </div>
            <div>
              <label>用户状态：</label><div>
                <template v-if="modalData.isDisable === 0">
                  <a-tag color="#87d068" style="width: 40px;height: 24px">
                    正常
                  </a-tag>
                </template>
                <template v-else>
                  <a-tag color="orange" style="width: 40px;height: 24px">
                    停用
                  </a-tag>
                </template>
              </div>
            </div>
          </div>
          <div>
            <div><label>用户名称：</label><div>{{ modalData.username }}</div></div>
            <div><label>用户昵称：</label><div>{{ modalData.nickname }}</div></div>
            <div><label>用户邮箱：</label><div>{{ modalData.email }}</div></div>
          </div>
          <div>
            <div><label>注册ip：</label><div>{{ modalData.registerIp }}</div></div>
            <div><label>注册地址：</label><div>{{ modalData.registerAddress }}</div></div>
            <div>
              <label>注册方式：</label>
              <template v-if="modalData.registerType === 0">
                <div>
                  <a-tag color="blue">
                    <div>账号/邮箱</div>
                  </a-tag>
                </div>
              </template>
              <template v-if="modalData.registerType === 1">
                <div>
                  <a-tag color="blue">
                    <div>QQ</div>
                  </a-tag>
                </div>
              </template>
              <template v-if="modalData.registerType === 2">
                <div>
                  <a-tag color="blue">
                    <div>微信</div>
                  </a-tag>
                </div>
              </template>
            </div>
          </div>
          <div>
            <div><label>登录ip：</label><div>{{ modalData.loginIp }}</div></div>
            <div><label>登录地址：</label><div>{{ modalData.loginAddress }}</div></div>
            <div>
              <label>登录方式：</label>
              <template v-if="modalData.loginType === 0">
                <div>
                  <a-tag color="blue">
                    <div>账号/邮箱</div>
                  </a-tag>
                </div>
              </template>
              <template v-if="modalData.loginType === 1">
                <div>
                  <a-tag color="blue">
                    <div>QQ</div>
                  </a-tag>
                </div>
              </template>
              <template v-if="modalData.loginType === 2">
                <div>
                  <a-tag color="blue">
                    <div>微信</div>
                  </a-tag>
                </div>
              </template>
            </div>
          </div>
          <div>
            <div><label>登录时间：</label><div>{{ modalData.loginTime }}</div></div>
            <div><label>注册时间：</label><div>{{ modalData.createTime }}</div></div>
            <div><label>更新时间：</label><div>{{ modalData.updateTime }}</div></div>
          </div>
          <div>
            <div>
              <label>用户角色：</label>
              <template v-if="modalData.roles">
                <div>{{ modalData.roles }}</div>
              </template>
            </div>
          </div>
          <div>
            <div><label>个人简介：</label><div>{{ modalData.intro }}</div></div>
          </div>
        </div>
      </template>
      <template v-else>
        <a-skeleton avatar :paragraph="{ rows: 12 }" />
      </template>
    </a-modal>
  </div>
</template>

<style scoped lang="scss">
.user-detail{
  display: flex;
  flex-direction: column;
  // 文本自动换行
  word-break: break-all;
  div{
    display: flex;
    margin-top: 1rem;
    @media(max-width: 768px) {
      display: block;
    }
    div{
      //border: 1px red solid;
      width: 100%;
      // 靠左对齐
      label{
        font-weight: bold;
        width: 7em;
        font-size: 1rem;
      }
      div{
        margin-top: 0;
      }
    }
  }
}
</style>
