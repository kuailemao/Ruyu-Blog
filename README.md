## 博客介绍

<p align="center">
  <a href="https://www.kuailemao.xyz">
    <img src="https://foruda.gitee.com/avatar/1667975309022664009/11937114_kuailemao_1667975308.png" alt="Ruyu的个人博客" style="border-radius: 50%;">
  </a>
</p>

<p align="center">
  基于 SpringBoot3 + Vue3 开发前后端分离个人博客系统
</p>

## 项目部分截图

### 前台
前台首页
![前台首页](img/new/%E5%89%8D%E5%8F%B0%E9%A6%96%E9%A1%B5.png)

前台中心
![前台中心](img/new/%E5%89%8D%E5%8F%B0%E4%B8%AD%E5%BF%83.png)

前台文章
![前台文章](img/new/%E5%89%8D%E5%8F%B0%E6%96%87%E7%AB%A0.png)

前台评论表情包
![前台评论表情包](img/new/%E5%89%8D%E5%8F%B0%E8%AF%84%E8%AE%BA%E8%A1%A8%E6%83%85%E5%8C%85.png)

前台树洞
![前台树洞](img/new/%E5%89%8D%E5%8F%B0%E6%A0%91%E6%B4%9E.png)

前台关于
![前台关于](img/new/%E5%89%8D%E5%8F%B0%E5%85%B3%E4%BA%8E.png)

前台相册
![前台相册](img/new/%E5%89%8D%E5%8F%B0%E7%9B%B8%E5%86%8C.png)

### 后台

后台发布文章
![后台发布文章](img/new/%E5%90%8E%E5%8F%B0%E5%8F%91%E5%B8%83%E6%96%87%E7%AB%A0.png)

后台文章列表
![后台文章列表](img/new/%E5%90%8E%E5%8F%B0%E6%96%87%E7%AB%A0%E5%88%97%E8%A1%A8.png)

后台相册管理
![后台相册管理](img/new/%E5%90%8E%E5%8F%B0%E7%9B%B8%E5%86%8C%E7%AE%A1%E7%90%86.png)

后台黑名单管理
![后台黑名单管理](img/new/%E5%90%8E%E5%8F%B0%E9%BB%91%E5%90%8D%E5%8D%95%E7%AE%A1%E7%90%86.png)

后台操作日志
![后台操作日志](img/new/%E5%90%8E%E5%8F%B0%E6%93%8D%E4%BD%9C%E6%97%A5%E5%BF%97.png)

后台服务监控
![后台服务监控](img/new/%E5%90%8E%E5%8F%B0%E6%9C%8D%E5%8A%A1%E7%9B%91%E6%8E%A7.png)

以上只是该项目**部分**功能截图，更多功能待**自行探索**！！！

## 在线体验地址
**注意：在线预览地址可能与仓库代码不同步，以仓库为主**
> 如无法访问可能就是我没钱续费服务器了，服务器配置不低，费用不低，望各位能理解，且看且珍惜

**前台博客：** https://kuailemao.xyz

**后台管理：** https://blog.kuailemao.xyz

**测试账号：** Test，**密码：** 123456

**ps:** 测试账号功能不代表系统所有功能，有些权限过高模块不方便在线展示

**Gitee地址：** https://gitee.com/kuailemao/ruyu-blog

**Github地址：** https://github.com/kuailemao/Ruyu-Blog

**B站视频介绍(最初版本预览，不代表最终品质)：** https://www.bilibili.com/video/BV181hNeiEDb/?vd_source=ff1e09f5473622b91dc0efc92418b537#reply112716668797561

**接口文档：** [API文档 (kuailemao.xyz)](http://kuailemao.xyz:8088/doc.html#/home)

**欢迎各位提交 PR ，一起改进项目**

**其他链接：**（第三方个人本项目部署链接，提供预览作用，其内容与本项目作者无关，感谢本同学提供）

前台：http://117.72.182.67:8080/ 

后台：http://117.72.182.67:8081

**测试账号：** Test，**密码：** 123456

## 运行环境

### 后端：

|   名称   | 环境  |
| :------: | :---: |
|  MySQL   |  8.0  |
|  Redis   | 7.2.3 |
| RabbitMQ | 最新  |
|  minio   | 最新  |
|   JDK    |  17   |

**前端：**

| 名称 |  环境   |
| :--: | :-----: |
| pnpm | 8.12.0  |
| node | 16.17.0 |

## 项目部署
**部署文档地址：** https://kuailemao.xyz/article/48 或项目**Wiki**

## 项目特点

* 前端参考了众多优秀博客大佬设计，页面美观，响应式布局
* 后台管理基于 Antdv Pro 后台通用框架二次开发
* 前后端分离，Docker Compose 一键部署
* 采用 RABC 权限模型，使用 SpringSecurity 进行权限管理
* 支持动态权限修改、动态菜单和路由
* 文章、分类、标签、时间轴、树洞、留言板、聊天、友链等模块
* 站长介绍、公告、电子时钟、随机文章、每日鸡汤、网站资讯
* 支持代码高亮、图片预览、黑夜模式、点赞、收藏、评论等功能
* 评论支持在线预览、Markdown、表情包
* 发送友链申请、通过等自动发送邮件提醒
* 接入第三方 gitee、github登录，减少注册成本
* 文章编辑使用 Markdown 编辑器
* 实现日志管理（操作、登录），服务监控、用户、菜单、角色、权限管理
* 使用 自己搭建 minio 进行图片存储（避免了使用第三方对象存储被刷流量问题）
* 使用 拦截器 + Redis 对接口进行了限流处理（每分钟）,后端使用 JSR 303 对参数校验，使用 Spring Aop + RabbitMQ 对后台操作日志处理
* 采用 Restful 风格的 API，注释完善，后端代码使用了大量 stream 流编程方式，代码非常美观
* ……

## 技术介绍

**前台前端（博客）：** Vue3 + Pinia +  Vue Router + TypeScript + Axios + Element Plus + Echarts……

**后台启动（管理）：** Vue3 + Pinia +  Vue Router + TypeScript + Axios + Antdv Pro + Ant Design Vue……

**后端：** JDK17 + SpringBoot3 + SpringSecurity + Mysql + Redis + Quartz  + RabbitMQ + Minio + Mybatis-Plus + Nginx + Docker……

**其他：** Gitee、Github 第三方登录

## 运行环境

### 推荐

> 最低 2 核 4 G

**我的：** 腾讯云 2 核 4 G  （带宽 6Mbps）

**系统：** **CentOS**

**前端\后端：** Docker

## 后续计划（有空）

> 白天上班，只能抽空优化项目，还望理解

- [x] 持续优化前台响应式
- [x] 新增用户设置、支持修改邮箱、头像、昵称...
- [x] 重构移动端首页
- [x] 重构移动端文章页面
- [x] 重构各种功能邮箱提醒(v1.4.1)
- [ ] 实现后台导入导出
- [x] 实现前台搜索
- [x] 内置图片上传压缩
- [x] 相册管理(v1.6.0)
- [x] 前台添加更加有趣的效果
- [x] 前台音乐播放器
- [ ] 后台图片资源管理模块
- [x] 找出并修复一些隐藏的bug(持续完善)
- [x] 前端响应速度优化到网络正常的情况下5秒以内
- [x] 实现黑名单管理机制
- [ ] 后台首页数据大屏
- [ ] 后台图片资源管理
- [ ] 博客app版本
- [ ] 博客小程序版本

## 项目总结

整个项目花费了大量的时间与精力（尤其是前台前端），作者独自手写了三个月左右，除了后台的页面使用了一个后台框架模板外，其他的全部一点一点手写（包括后台的全部模块），这也是我第一次正式的开源一个项目（莫名成就感），虽然但是这个项目我并不觉得很完美，一部分是因为我自己技术有限的原因（主后端的全栈选手），一部分是项目还没经过时间的拷打，开发过程中也参考了很多优秀的项目，在这里感谢大家的开源项目，希望我的项目也能给你带来收获。

### 鸣谢项目：

* [mrzym-blog](https://gitee.com/mrzym/stable-version-of-blog)

* [掐指yi算’逢考必过-Blog](https://gitee.com/wu_shengdong/blog)

* [hexo-theme-butterfly](https://github.com/jerryc127/hexo-theme-butterfly)

* [Antdv Pro](https://docs.antdv-pro.com/)

* [md-editor-v3](https://imzbf.github.io/md-editor-v3/zh-CN/index)

* [vue-danmaku](https://github.com/hellodigua/vue-danmaku)

* ……

#### Heo表情包开源地址
* https://github.com/zhheo/Sticker-Heo

## 最后

**该文档初次编写可能存在一些问题，如果发现，后面会进行修改提交**

### <u>如果对你有益，麻烦点个star支持项目，能让项目得到更多关注，谢谢！！！</u>

**该项目交流群：** （有什么不懂的可以提问）

**QQ：** 635887836

**二维码：**

![Ruyu开源博客交流群群聊二维码](img/Ruyu%E5%BC%80%E6%BA%90%E5%8D%9A%E5%AE%A2%E4%BA%A4%E6%B5%81%E7%BE%A4%E7%BE%A4%E8%81%8A%E4%BA%8C%E7%BB%B4%E7%A0%81.png)

