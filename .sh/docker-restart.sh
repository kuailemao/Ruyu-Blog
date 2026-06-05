#!/usr/bin/env bash
set -Eeuo pipefail

# 重启/更新 docker-compose.yml 中的服务。
# 默认服务: backend admin blog
#
# 用法:
#   bash .sh/docker-restart.sh
#   bash .sh/docker-restart.sh --all
#   bash .sh/docker-restart.sh --services backend admin
#   bash .sh/docker-restart.sh --services backend --no-pull
#   bash .sh/docker-restart.sh --compose-file /path/to/docker-compose.yml

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
ROOT_DIR="$(cd "${SCRIPT_DIR}/.." && pwd)"
COMPOSE_FILE="${ROOT_DIR}/docker-compose.yml"

DO_PULL=true
FORCE_RECREATE=true
SERVICES=("backend" "admin" "webdav" "blog")

print_help() {
  cat <<'EOF'
选项:
  --all                   重启所有 compose 服务。
  --services <svc...>     重启指定的服务 (以空格分隔)。
  --no-pull               跳过拉取最新镜像。
  --no-force-recreate     不强制重建容器。
  --compose-file <path>   自定义 compose 文件路径。
  -h, --help              显示此帮助信息。
EOF
}

while [[ $# -gt 0 ]]; do
  case "$1" in
    --all)
      SERVICES=()
      shift
      ;;
    --services)
      shift
      SERVICES=()
      while [[ $# -gt 0 && "${1:0:2}" != "--" ]]; do
        SERVICES+=("$1")
        shift
      done
      ;;
    --no-pull)
      DO_PULL=false
      shift
      ;;
    --no-force-recreate)
      FORCE_RECREATE=false
      shift
      ;;
    --compose-file)
      COMPOSE_FILE="${2:-}"
      if [[ -z "${COMPOSE_FILE}" ]]; then
        echo "错误: --compose-file 需要提供一个路径参数。"
        exit 1
      fi
      shift 2
      ;;
    -h|--help)
      print_help
      exit 0
      ;;
    *)
      echo "错误: 未知的选项: $1"
      print_help
      exit 1
      ;;
  esac
done

if ! command -v docker >/dev/null 2>&1; then
  echo "错误: 未安装 docker，请先安装。"
  exit 1
fi

if ! docker compose version >/dev/null 2>&1; then
  echo "错误: docker compose 插件不可用。"
  exit 1
fi

if [[ ! -f "${COMPOSE_FILE}" ]]; then
  echo "错误: 找不到指定的 compose 文件: ${COMPOSE_FILE}"
  exit 1
fi

COMPOSE_CMD=(docker compose -f "${COMPOSE_FILE}")

echo "▶ Compose 文件路径: ${COMPOSE_FILE}"
if [[ ${#SERVICES[@]} -eq 0 ]]; then
  echo "▶ 目标服务: 全部 (All)"
else
  echo "▶ 目标服务: ${SERVICES[*]}"
fi

# 为了中文阅读体验更好，将 true/false 转换为 是/否
[[ "${DO_PULL}" == true ]] && PULL_CN="是" || PULL_CN="否"
[[ "${FORCE_RECREATE}" == true ]] && RECREATE_CN="是" || RECREATE_CN="否"

echo "▶ 是否拉取镜像: ${PULL_CN}"
echo "▶ 是否强制重建: ${RECREATE_CN}"
echo "----------------------------------------"

if [[ "${DO_PULL}" == true ]]; then
  echo "正在拉取镜像..."
  if [[ ${#SERVICES[@]} -eq 0 ]]; then
    "${COMPOSE_CMD[@]}" pull
  else
    "${COMPOSE_CMD[@]}" pull "${SERVICES[@]}"
  fi
fi

UP_ARGS=(-d)
if [[ "${DO_PULL}" == true ]]; then
  UP_ARGS+=(--pull always)
fi
if [[ "${FORCE_RECREATE}" == true ]]; then
  UP_ARGS+=(--force-recreate)
fi

echo "正在启动服务..."
if [[ ${#SERVICES[@]} -eq 0 ]]; then
  "${COMPOSE_CMD[@]}" up "${UP_ARGS[@]}"
else
  "${COMPOSE_CMD[@]}" up "${UP_ARGS[@]}" "${SERVICES[@]}"
fi

echo "----------------------------------------"
echo "✅ 操作完成。当前服务状态如下:"
"${COMPOSE_CMD[@]}" ps
