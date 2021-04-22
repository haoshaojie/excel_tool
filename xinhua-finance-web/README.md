 <p align="center">
   <img src="https://img.shields.io/badge/Release-V1.0.1-green.svg" alt="Downloads">
   <img src="https://img.shields.io/badge/JDK-1.8+-green.svg" alt="Build Status">
   <img src="https://img.shields.io/badge/license-Apache%202-blue.svg" alt="Build Status">
   <img src="https://img.shields.io/badge/Spring%20Cloud-2020-blue.svg" alt="Coverage Status">
   <img src="https://img.shields.io/badge/Spring%20Boot-2.4.2-blue.svg" alt="Downloads">
 </p>  

## cnfic-usermanage微服务开发平台
* 采用前后端分离的模式，前端开源两个框架：基于 Vue、Element-UI
* 后端采用SpringCloud全家桶
* 集成Sentinel从流量控制、熔断降级、系统负载等多个维度保护服务的稳定性。
* 注册中心、配置中心选型Nacos，为工程瘦身的同时加强各模块之间的联动。
* 使用Traefik进行反向代理，监听后台变化自动化应用新的配置文件。
* 极简封装了多租户底层，用更少的代码换来拓展性更强的SaaS多租户系统。
* 借鉴OAuth2，实现了多终端认证系统，可控制子系统的token权限互相隔离。
* 借鉴Security，封装了Secure模块，采用JWT做Token认证，可拓展集成Redis等细颗粒度控制方案。
* 项目分包明确，规范微服务的开发模式，使包与包之间的分工清晰。

## 工程结构
``` 
cnfic-usermanage
├── cnfic-auth -- 授权服务提供
├── cnfic-common -- 常用工具封装包
├── cnfic-gateway -- Spring Cloud 网关
├── cnfic-ops -- 运维中心
├    ├── cnfic-resource -- 资源管理
├── cnfic-service -- 业务模块
├    ├── cnfic-log -- 日志模块 
├    ├── cnfic-system -- 系统模块 
├    └── cnfic-user -- 用户模块 
├── cnfic-service-api -- 业务模块api封装
├    ├── cnfic-dict-api -- 字典api 
├    ├── cnfic-system-api -- 系统api 
└──  └── cnfic-user-api -- 用户api 
```

## 如何启动
```
$ git clone https://gitee.com/smallc/Saber.git
$ cd Saber
# 安装
$ yarn install
# 启动
$ yarn run serve     
```
