> 后台管理员账号密码：ADMIN 123456

> 版本升级说明见 [CHANGELOG.md](CHANGELOG.md)

---

## Docker 镜像部署

```bash
# 在项目根目录执行
docker-compose up -d
```

- **数据库名：** `blog`
- **Root 密码：** `root123456`（可在根目录 `docker-compose.yml` 中修改）
- **端口：** `3306`

单独构建 MySQL 镜像（可选）：

```bash
cd sql
docker build -t blog-mysql:latest .
```

### 手动导入 SQL

1. 先导入 [v1.5.0/Ruyu-Blog_v1.5.0.sql](v1.5.0/Ruyu-Blog_v1.5.0.sql)
2. 再导入 [v1.6.0/t_photo.sql](v1.6.0/t_photo.sql)（相册管理表）
