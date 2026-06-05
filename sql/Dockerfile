FROM mysql:8.0

COPY init/00-create-database.sql /docker-entrypoint-initdb.d/
COPY v1.5.0/Ruyu-Blog_v1.5.0.sql /tmp/v1.5.0.sql
COPY v1.6.0/t_photo.sql /tmp/v1.6.0.sql

# 在 SQL 文件前追加 USE blog; 确保表创建在正确数据库中
RUN { echo "USE blog;"; cat /tmp/v1.5.0.sql; } > /docker-entrypoint-initdb.d/01-ruyu-blog.sql && \
    { echo "USE blog;"; cat /tmp/v1.6.0.sql; } > /docker-entrypoint-initdb.d/02-t-photo.sql && \
    rm -f /tmp/v1.5.0.sql /tmp/v1.6.0.sql

EXPOSE 3306
