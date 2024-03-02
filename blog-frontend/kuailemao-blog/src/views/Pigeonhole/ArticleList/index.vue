<script setup lang="ts">
defineProps({
  articleList: {
    type: Array,
    required: true
  }
})

</script>

<template>
  <div class="category_article_container">
    <template v-for="article in articleList" :key="article.id">
      <div>
        <div class="article_left">
          <img :src="article.articleCover" alt="预览图">
        </div>
        <div class="article_right">
          <span class="article_time">{{ article.createTime }}</span>
          <span @click="$router.push(`/article/${article.id}`)"
                class="article_title"><span>{{ article.articleTitle }}</span> <span><SvgIcon
              name="heat"/>{{ article.visitCount }}</span></span>
          <div class="article_tag">
            <template v-for="tag in article.tags" :key="tag.id">
              <span @click="$router.push(`/tags/${tag.id}`)" >#{{ tag.tagName }}</span>
            </template>
          </div>
        </div>
      </div>
    </template>
  </div>
</template>

<style scoped lang="scss">
.category_article_container {
  padding: 1rem;
  display: flex;
  flex-wrap: wrap;
  width: 100%;

  div {
    margin: 0.5rem 0;
    width: 50%;
    display: flex;
    transition: all 0.3s;
    @media screen and (max-width: 1100px) {
      width: 100%;
    }

    .article_left {
      border-radius: 1em;
      width: 12.5em;
      height: 6em;
      overflow: hidden;
      border: #862e9c 2px solid;

      img {
        width: 100%;
        height: 100%;
        transition: transform 0.3s linear;
        object-fit: cover;

        &:hover {
          transform: scale(1.1);
        }
      }
    }

    .article_right {

      display: flex;
      flex-direction: column;
      justify-content: center;
      margin-left: 0.4rem;

      .article_title {
        margin-top: 0.4rem;
        font-size: 1.5rem;
        font-weight: bold;
        transition: color 0.3s;
        display: flex;
        justify-content: space-between;

        span:last-child {
          font-size: 0.8rem;
          color: grey;
          display: flex;
          align-items: center;
        }

        &:hover {
          color: #862e9c;
          cursor: pointer;
        }
      }

      .article_time {
        font-size: 1rem;
        color: grey;
      }

      .article_tag {
        display: flex;
        margin-top: 0.4rem;
        width: 100%;

        span {
          margin-right: 1rem;
          font-size: 1rem;
          color: grey;
          transition: color 0.3s;
          &:hover {
            cursor: pointer;
            color: #862e9c;
          }
        }
      }
    }
  }
}
</style>