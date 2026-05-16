#!/usr/bin/env bash

# 某个命令执行失败立刻退出
set -Eeuo pipefail

# 服务部署脚本
#
# Usage examples:
#   bash .sh/deploy-on-server.sh
#   bash .sh/deploy-on-server.sh --all
#   bash .sh/deploy-on-server.sh --services backend admin blog
#   bash .sh/deploy-on-server.sh --tag v1.2.3
#   bash .sh/deploy-on-server.sh --compose-file /opt/blog/docker-compose.yml --env-file /opt/blog/.env

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
ROOT_DIR="$(cd "${SCRIPT_DIR}/.." && pwd)"
COMPOSE_FILE="${ROOT_DIR}/docker-compose.yml"
ENV_FILE="${ROOT_DIR}/.env"

DO_PULL=true
FORCE_RECREATE=true
WAIT_TIMEOUT=240

# Default deploy targets app services only. Use --all for full stack.
# 默认部署 app 服务，使用 --all 部署全部服务
SERVICES=("backend" "admin" "blog")
TARGET_TAG=""

log() { printf '[%s] %s\n' "$(date +'%F %T')" "$*"; }
err() { printf '[%s] ERROR: %s\n' "$(date +'%F %T')" "$*" >&2; }

print_help() {
  cat <<'EOF'
Options:
  --all                     Deploy all compose services
  --services <svc...>       Deploy specified services (space-separated)
  --tag <image_tag>         Override app image tag by exporting IMAGE_TAG before up
  --no-pull                 Skip docker compose pull
  --no-force-recreate       Do not force recreate containers
  --wait-timeout <seconds>  Wait timeout for service health (default: 240)
  --compose-file <path>     Custom docker-compose file path
  --env-file <path>         Custom .env file path
  -h, --help                Show help

Notes:
  1) If your compose uses fixed ':latest', --tag has no effect unless compose supports IMAGE_TAG.
  2) This script requires docker compose plugin and a valid .env file.
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
    --tag)
      TARGET_TAG="${2:-}"
      [[ -z "${TARGET_TAG}" ]] && { err "--tag requires a value"; exit 1; }
      shift 2
      ;;
    --no-pull)
      DO_PULL=false
      shift
      ;;
    --no-force-recreate)
      FORCE_RECREATE=false
      shift
      ;;
    --wait-timeout)
      WAIT_TIMEOUT="${2:-}"
      [[ ! "${WAIT_TIMEOUT}" =~ ^[0-9]+$ ]] && { err "--wait-timeout must be an integer"; exit 1; }
      shift 2
      ;;
    --compose-file)
      COMPOSE_FILE="${2:-}"
      [[ -z "${COMPOSE_FILE}" ]] && { err "--compose-file requires a value"; exit 1; }
      shift 2
      ;;
    --env-file)
      ENV_FILE="${2:-}"
      [[ -z "${ENV_FILE}" ]] && { err "--env-file requires a value"; exit 1; }
      shift 2
      ;;
    -h|--help)
      print_help
      exit 0
      ;;
    *)
      err "Unknown option: $1"
      print_help
      exit 1
      ;;
  esac
done

command -v docker >/dev/null 2>&1 || { err "docker not found"; exit 1; }
docker compose version >/dev/null 2>&1 || { err "docker compose plugin not available"; exit 1; }
[[ -f "${COMPOSE_FILE}" ]] || { err "compose file not found: ${COMPOSE_FILE}"; exit 1; }
[[ -f "${ENV_FILE}" ]] || { err ".env file not found: ${ENV_FILE}"; exit 1; }

COMPOSE_CMD=(docker compose --env-file "${ENV_FILE}" -f "${COMPOSE_FILE}")

if [[ -n "${TARGET_TAG}" ]]; then
  export IMAGE_TAG="${TARGET_TAG}"
fi

log "Compose file: ${COMPOSE_FILE}"
log "Env file: ${ENV_FILE}"
if [[ ${#SERVICES[@]} -eq 0 ]]; then
  log "Services: all"
else
  log "Services: ${SERVICES[*]}"
fi
log "Pull images: ${DO_PULL}"
log "Force recreate: ${FORCE_RECREATE}"
[[ -n "${TARGET_TAG}" ]] && log "Requested image tag: ${TARGET_TAG}"

if [[ "${DO_PULL}" == true ]]; then
  log "Pulling images..."
  if [[ ${#SERVICES[@]} -eq 0 ]]; then
    "${COMPOSE_CMD[@]}" pull
  else
    "${COMPOSE_CMD[@]}" pull "${SERVICES[@]}"
  fi
fi

UP_ARGS=(-d)
[[ "${DO_PULL}" == true ]] && UP_ARGS+=(--pull always)
[[ "${FORCE_RECREATE}" == true ]] && UP_ARGS+=(--force-recreate)

log "Starting services..."
if [[ ${#SERVICES[@]} -eq 0 ]]; then
  "${COMPOSE_CMD[@]}" up "${UP_ARGS[@]}"
else
  "${COMPOSE_CMD[@]}" up "${UP_ARGS[@]}" "${SERVICES[@]}"
fi

wait_service_ok() {
  local service="$1"
  local deadline=$((SECONDS + WAIT_TIMEOUT))
  local cid state health

  while (( SECONDS < deadline )); do
    cid="$("${COMPOSE_CMD[@]}" ps -q "${service}" 2>/dev/null || true)"
    if [[ -n "${cid}" ]]; then
      state="$(docker inspect -f '{{.State.Status}}' "${cid}" 2>/dev/null || true)"
      health="$(docker inspect -f '{{if .State.Health}}{{.State.Health.Status}}{{else}}none{{end}}' "${cid}" 2>/dev/null || true)"

      if [[ "${state}" == "running" && ( "${health}" == "healthy" || "${health}" == "none" ) ]]; then
        log "Service ${service} is ${state}/${health}"
        return 0
      fi
    fi
    sleep 3
  done

  err "Service ${service} did not become ready in ${WAIT_TIMEOUT}s"
  return 1
}

FAIL=0
if [[ ${#SERVICES[@]} -eq 0 ]]; then
  mapfile -t CHECK_SERVICES < <("${COMPOSE_CMD[@]}" config --services)
else
  CHECK_SERVICES=("${SERVICES[@]}")
fi

for svc in "${CHECK_SERVICES[@]}"; do
  if ! wait_service_ok "${svc}"; then
    FAIL=1
  fi
done

log "Current status:"
"${COMPOSE_CMD[@]}" ps

if [[ ${FAIL} -ne 0 ]]; then
  err "Deployment completed with unhealthy services. Showing recent logs:"
  if [[ ${#SERVICES[@]} -eq 0 ]]; then
    "${COMPOSE_CMD[@]}" logs --tail=120
  else
    "${COMPOSE_CMD[@]}" logs --tail=120 "${SERVICES[@]}"
  fi
  exit 1
fi

log "Deployment succeeded."
