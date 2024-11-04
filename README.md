```  

gmall-parent
├── bin -- 启动脚本和打包脚本

├── module-gateway -- 网关模块[90]

└── module-management-common -- 系统公共模块
     ├── module-common-entity -- 实体模块
     ├── module-common-rabbit-util --rabbit工具模块
     ├── module-common-service-util --服务所用工具模块
     ├── module-common-util -- 全局异常和全局返回格式工具模块
     └── module-common-web-util -- web工具模块
     
├── module-management-feign -- feign远程调用模块
     ├── module-feign-api-activity -- 活动feign模块
     ├── module-feign-api-cart -- 购物车feign模块
     ├── module-feign-api-item -- 商品详情feign模块
     ├── module-feign-api-list -- 列表feign模块
     ├── module-feign-api-order -- 订单feign模块
     ├── module-feign-api-payment -- 支付feign模块
     ├── module-feign-api-product -- 商品feign模块
     └── module-feign-api-user -- 用户feign模块
     
├── module-management-service -- 服务模块
     ├── module-service-activity -- 活动模块[8200]
     ├── module-service-cart -- 购物车模块[8201]
     ├── module-service-item -- 商品详情模块[8202]
     ├── module-service-list -- 列表模块[8203]
     ├── module-service-mq -- 消息队列模块[8282]
     ├── module-service-order -- 订单模块[8204]
     ├── module-service-payment -- 支付模块[8205]
     ├── module-service-product -- 商品模块[8206]
     ├── module-service-task -- 任务模块[8207] 
     └── module-service-user -- 用户模块[8208]
     
├── module-management-web-view -- 页面模块
     ├── module-web-view-admin -- 前端后台管理项目
     ├── module-web-view-guest -- 前端项目[8300]

├── ware-manage -- 第三方库存模块[9001]
```