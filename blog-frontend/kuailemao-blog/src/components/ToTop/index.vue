<script setup lang="ts">

</script>

<template>
  <div>
    <el-backtop :bottom="50">
      <el-tooltip
          effect="light"
          content="回到顶部"
          placement="right"
      >
        <div>
          <svg-icon name="back_to_top" class="back-to-top"/>
        </div>
      </el-tooltip>
    </el-backtop>
  </div>
</template>

<style scoped lang="scss">

:deep(.el-backtop){
  right: 2rem !important;
}

.back-to-top{
  height: 40px !important;
  width: 40px !important;

  @media screen and (max-width: 768px) {
    height: 25px !important;
    width: 25px !important;
  }
}


</style>