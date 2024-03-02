<script setup lang="ts">
import usePaginationStore from "@/store/modules/pagination";

const paginationStore = usePaginationStore();

</script>

<template>
  <div class="pag">
    <el-pagination background layout="prev, pager, next"
                   :total="paginationStore.articlePagination.total"
                   :hide-on-single-page="true"
                   :page-size="paginationStore.articlePagination.pageSize"
                   @current-change="paginationStore.articlePagination.current = $event"

    />
  </div>
</template>

<style scoped lang="scss">

.pag {
  width: 100%;
  height: 100%;
  display: flex;
  justify-content: center;
  align-items: center;
}
</style>