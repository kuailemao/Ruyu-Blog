FROM nginx:1.30.1-alpine
RUN rm -f /etc/nginx/conf.d/default.conf
COPY default-docker.conf /etc/nginx/conf.d/default.conf
COPY dist/ /usr/share/nginx/html/
