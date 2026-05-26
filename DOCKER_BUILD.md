# Docker 构建与启动指南

## 前置准备

1. 复制环境变量配置并填写实际值：

```bash
cp .env.example .env
```

2. 编辑 `.env` 填入 MySQL、RabbitMQ、Minio、邮件等配置。

## 构建镜像

### 数据库镜像

```bash
docker build -t blog-mysql:latest ./sql
```

### 后端镜像

先执行 Maven 打包：

```bash
cd blog-backend && mvn clean package -DskipTests && cd ..
docker build -t blog-backend:latest ./blog-backend
```

### 前端镜像（后台管理）

先执行 pnpm 构建（跳过 husky）：

```bash
cd blog-frontend/kuailemao-admin && set HUSKY=0&& pnpm install && pnpm build && cd ../..
docker build -t blog-admin:latest ./blog-frontend/kuailemao-admin
```

### 前端镜像（前台博客）

先执行 pnpm 构建（跳过 husky）：

```bash
cd blog-frontend/kuailemao-blog && set HUSKY=0&& pnpm install && pnpm build && cd ../..
docker build -t blog-front:latest ./blog-frontend/kuailemao-blog
```

## 启动服务

### 启动全部

```bash
# 如果在云端注意拉取
# docker compose pull

docker compose up -d
```

### 仅启动依赖（MySQL、Redis、RabbitMQ、Minio）

```bash
docker compose up -d mysql redis rabbitmq minio
```

### 启动指定服务

```bash
# 入宫不使用webdev服务可以不创建webdev服务
docker compose up -d backend admin webdav blog
```

## 服务端口

| 服务 | 端口 |
|------|------|
| MySQL | 3306 |
| Redis | 6379 |
| RabbitMQ | 5672, 15672 |
| Minio | 9000, 9001 |
| WebDAV | Docker 内网 6065，对外通过 dav.zhuyuxi.xyz |
| 后端 | 8088 |
| 后台管理 | 81 |
| 前台博客 | 80 |

## WebDAV

WebDAV 使用 [`hacdias/webdav`](http://github.com/hacdias/webdav)。容器只暴露到 Docker 内网，由现有 `blog` 容器里的 Nginx 按域名 `dav.zhuyuxi.xyz` 反向代理。

部署前在 `.env` 增加：

```bash
WEBDAV_USERNAME=your_webdav_user
WEBDAV_PASSWORD=your_strong_webdav_password
```

启动或更新：

```bash
docker compose up -d webdav blog
```

验证：

```bash
curl -u "$WEBDAV_USERNAME:$WEBDAV_PASSWORD" -X PROPFIND "http://dav.zhuyuxi.xyz/" -H "Depth: 1" -i
```

## 常用命令

```bash
docker compose down          # 停止并删除容器
docker compose logs -f       # 查看日志
docker compose ps            # 查看运行状态
```
