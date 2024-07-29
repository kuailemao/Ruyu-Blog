<script setup lang="ts">
import * as echarts from 'echarts/core';
import {GridComponent} from 'echarts/components';
import {LineChart} from 'echarts/charts';
import {UniversalTransition} from 'echarts/features';
import {CanvasRenderer} from 'echarts/renderers';
import {
  TitleComponent,
  ToolboxComponent,
  TooltipComponent,
  LegendComponent
} from 'echarts/components';
import {PieChart} from 'echarts/charts';
import {LabelLayout} from 'echarts/features';

const views = ref(null)
const information = ref(null)

onMounted(() => {
  initViews()
  initInformation()
})

// 浏览量
function initViews() {
  echarts.use([GridComponent, LineChart, CanvasRenderer, UniversalTransition]);

  const chartDom = views.value!;
  const myChart = echarts.init(chartDom);
  let option;

  option = {
    xAxis: {
      type: 'category',
      data: ['Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat', 'Sun']
    },
    yAxis: {
      type: 'value'
    },
    series: [
      {
        data: [150, 230, 224, 218, 135, 147, 260],
        type: 'line'
      }
    ]
  };

  option && myChart.setOption(option);
}

// 网站资料
function initInformation() {
  echarts.use([
    TitleComponent,
    ToolboxComponent,
    TooltipComponent,
    LegendComponent,
    PieChart,
    CanvasRenderer,
    LabelLayout
  ]);

  const chartDom = information.value;
  const myChart = echarts.init(chartDom);
  let option;

  option = {
    tooltip: {
      trigger: 'item',
      formatter: '{a} <br/>{b} : {c} ({d}%)'
    },
    toolbox: {
      show: false,
      feature: {
        mark: {show: true},
        dataView: {show: true, readOnly: false},
        restore: {show: true},
        saveAsImage: {show: true}
      }
    },
    series: [
      {
        name: '网站资料',
        type: 'pie',
        radius: [20, 140],
        center: ['50%', '50%'],
        roseType: 'area',
        itemStyle: {
          borderRadius: 5
        },
        data: [
          {value: 30, name: '分类'},
          {value: 28, name: '标签'},
          {value: 26, name: '友链'},
          {value: 24, name: '留言'},
          {value: 22, name: '文章'},
        ]
      }
    ]
  };

  option && myChart.setOption(option);

}

</script>

<template>
  <div>
    <Main only-father-container>
      <template #banner>
        <Banner title="关于" subtitle="link"/>
      </template>
      <template #content>
        <div class="title">
          关于
        </div>
        <div class="content">
          <div class="item_content" style="margin-bottom: 10rem;margin-top: 5rem">
            <el-divider content-position="left">关于作者</el-divider>
            <div class="content_author">
              <div class="author">
                <div class="author_avatar">
                  <img src="https://image.kuailemao.xyz/blog/websiteInfo/avatar/76829cfe-f670-4e26-85c6-ab46f8b45250.jpg" alt="">
                </div>
                <div class="author_info">
                  <div class="author_name">
                    <span>ruyu</span>
                  </div>
                  <div class="author_desc">
                    <span>啥也不会</span>
                  </div>
                </div>
              </div>
              <div class="author_desc">
                <span>全栈开发小白</span>
              </div>
            </div>
          </div>
          <div class="item_content">
            <el-divider content-position="left">关于网站（后续计划）</el-divider>
            <div class="content_site">
              <div>
                <div>近一周访问量</div>
                <div ref="views" style="width: 100%;height: 100%"></div>
              </div>
              <div>
                <div>网站资料</div>
                <div ref="information" style="width: 100%;height: 100%"></div>
              </div>
            </div>
          </div>
        </div>
      </template>
    </Main>
  </div>
</template>

<style scoped lang="scss">

.content {
  width: 100%;
  height: 100%;

  .item_content {
    margin: 2rem 0;

    .content_site {
      width: 100%;
      height: 20rem;
      display: flex;
      flex-direction: row;
      justify-content: space-between;
      align-items: center;

      > div {
        width: 50%;
        height: 100%;
        display: flex;
        flex-direction: column;
        align-items: center;
        font-weight: bold;
        font-size: 1rem;

        div:first-child {
          font-size: 1rem;
          margin-bottom: 1.5rem;
        }
      }
    }

    .content_author {
      display: flex;
      flex-direction: row;
      justify-content: space-between;
      align-items: center;
      margin-bottom: 1rem;

      .author {
        display: flex;
        flex-direction: row;
        align-items: center;

        .author_avatar {
          width: 4rem;
          height: 4rem;
          border-radius: 50%;
          overflow: hidden;
          margin-right: 1rem;

          img {
            width: 100%;
            height: 100%;
          }
        }

        .author_info {
          display: flex;
          flex-direction: column;
          justify-content: center;

          .author_name {
            font-size: 1.5rem;
            font-weight: bold;
            margin-bottom: 0.5rem;
          }

          .author_desc {
            font-size: 1rem;
            color: #999;
          }
        }
      }

      .author_desc {
        font-size: 1rem;
        color: #999;
      }
    }
  }
}


.title {
  font-size: 1.5rem;
  margin-bottom: 2rem;
}
</style>