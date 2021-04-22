#标准的nginx镜像,我们需要基于标准的nginx镜像制作自己的镜像
FROM  docker-public.tongtech.top/nginx:1.19.6
#设置生成镜像的Author
MAINTAINER xhcj
#拷贝当前目录的文件到指定文件夹下，改文件夹为镜像中的文件夹
RUN mkdir -p /opt/app/cnfic-usermanage/dist/
COPY dist /opt/app/cnfic-usermanage/dist/
#拷贝conf.d 文件到镜像下
COPY cnfic-usermanage-ui.conf /etc/nginx/conf.d/
#输出完成
RUN echo 'build cnfic-usermanage-ui image ok!'
