#!/bin/bash

# 容器名称（请替换为你的容器名称）
CONTAINER_NAME=ruyu-blog-hd

# 镜像名称（请替换为你的镜像名称）
IMAGE_NAME=ruyu-blog-hd

# 停止并删除容器
if docker ps -a | grep -q $CONTAINER_NAME; then
  docker stop $CONTAINER_NAME
  docker rm $CONTAINER_NAME
  echo "$CONTAINER_NAME 停止并删除容器"
else
  echo "$CONTAINER_NAME 未启动"
fi

# 删除旧镜像 (如果存在)
if docker images | grep -q $IMAGE_NAME; then
  docker rmi $IMAGE_NAME
  echo "删除旧镜像 $IMAGE_NAME"
fi

# 构建新镜像
if ! docker build -t $IMAGE_NAME .; then
    echo "镜像构建失败，请检查 Dockerfile 和构建过程。"
    exit 1  # 终止脚本执行
fi

# 运行新容器
docker run -d \
  --name $CONTAINER_NAME \
  -p 8088:8088 $IMAGE_NAME

echo "$CONTAINER_NAME 已启动"