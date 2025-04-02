```  

gmall-parent
├── bin -- 启动脚本和打包脚本
├── nacos_conf -- nacos 配置文件
├── script --  环境搭建脚本
├── sql -- sql脚本

├── module-gateway -- 网关模块[80]
└── module-management-common -- 系统公共模块
     ├── module-common-entity -- 实体模块
     ├── module-common-rabbit-util --rabbit工具模块
     ├── module-common-service-util --服务所用工具模块
     ├── module-common-util -- 全局异常和全局返回格式工具模块
     └── module-common-web-util -- web工具模块

├── module-management-feign -- feign远程调用模块
     ├── module-feign-api-activity -- 秒杀活动feign模块
     ├── module-feign-api-cart -- 购物车feign模块
     ├── module-feign-api-item -- 商品详情feign模块
     ├── module-feign-api-list -- 列表feign模块
     ├── module-feign-api-order -- 订单feign模块
     ├── module-feign-api-payment -- 支付feign模块
     ├── module-feign-api-product -- 商品feign模块
     └── module-feign-api-user -- 用户feign模块
     
├── module-management-service -- 服务模块
     ├── module-service-activity -- 秒杀活动模块[9200]
     ├── module-service-cart -- 购物车模块[9201]
     ├── module-service-item -- 商品详情模块[9202]
     ├── module-service-list -- 列表模块[9203]
     ├── module-service-mq -- 消息队列模块[9292]
     ├── module-service-order -- 订单模块[9204]
     ├── module-service-payment -- 支付模块[9205]
     ├── module-service-product -- 商品模块[9206]
     ├── module-service-task -- 任务模块[9207] 
     └── module-service-user -- 用户模块[9208]
     
├── module-management-web-view -- 页面模块
     ├── module-web-view-admin -- 前端后台管理项目
     ├── module-web-view-guest -- 前端项目[9300]

├── ware-manage -- 第三方库存模块[9001]
```