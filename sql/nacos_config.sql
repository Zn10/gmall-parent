/*
 Navicat Premium Dump SQL

 Source Server         : 192.168.200.128-主库
 Source Server Type    : MySQL
 Source Server Version : 50744 (5.7.44-log)
 Source Host           : 192.168.200.128:3306
 Source Schema         : nacos_config

 Target Server Type    : MySQL
 Target Server Version : 50744 (5.7.44-log)
 File Encoding         : 65001

 Date: 28/04/2025 10:39:34
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for config_info
-- ----------------------------
DROP TABLE IF EXISTS `config_info`;
CREATE TABLE `config_info`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `data_id` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT 'data_id',
  `group_id` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `content` longtext CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT 'content',
  `md5` varchar(32) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT 'md5',
  `gmt_create` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `gmt_modified` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '修改时间',
  `src_user` text CHARACTER SET utf8 COLLATE utf8_bin NULL COMMENT 'source user',
  `src_ip` varchar(50) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT 'source ip',
  `app_name` varchar(128) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `tenant_id` varchar(128) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT '' COMMENT '租户字段',
  `c_desc` varchar(256) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `c_use` varchar(64) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `effect` varchar(64) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `type` varchar(64) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `c_schema` text CHARACTER SET utf8 COLLATE utf8_bin NULL,
  `encrypted_data_key` text CHARACTER SET utf8 COLLATE utf8_bin NULL COMMENT '秘钥',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_configinfo_datagrouptenant`(`data_id`, `group_id`, `tenant_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 38 CHARACTER SET = utf8 COLLATE = utf8_bin COMMENT = 'config_info' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of config_info
-- ----------------------------
INSERT INTO `config_info` VALUES (1, 'common.yaml', 'DEFAULT_GROUP', 'mybatis-plus:\n  configuration:\n    log-impl: org.apache.ibatis.logging.stdout.StdOutImpl\n  mapper-locations: classpath:mapper/*Mapper.xml\nfeign:\n  sentinel:\n    enabled: true\n  client:\n    config:\n      default:\n        readTimeout: 30000\n        connectTimeout: 10000\nspring:\n  zipkin:\n    base-url: http://gmall-zipkin:9411\n    discovery-client-enabled: false\n    sender:\n      type: web\n  cloud:\n    sentinel:\n      transport:\n        dashboard: http://gmall-sentinel:8858\n  rabbitmq:\n    host: gmall-rabbitmq\n    port: 5672\n    username: admin\n    password: admin\n    publisher-confirm-type: correlated\n    publisher-returns: true\n    listener:\n      simple:\n        acknowledge-mode: manual #默认情况下消息消费者是自动确认消息的，如果要手动确认消息则需要修改确认模式为manual\n        prefetch: 1 # 消费者每次从队列获取的消息数量。此属性当不设置时为：轮询分发，设置为1为：公平分发\n  redis:\n    host: gmall-redis\n    port: 6379\n    database: 0\n    timeout: 1800000\n    password:\n    lettuce:\n      pool:\n        max-active: 20 #最大连接数\n        max-wait: -1    #最大阻塞等待时间(负数表示没限制)\n        max-idle: 5    #最大空闲\n        min-idle: 0     #最小空闲\n  jackson:\n    date-format: yyyy-MM-dd HH:mm:ss\n    time-zone: GMT+8', '60fc98ee460d0add43ffad854ad62d07', '2024-11-12 09:16:33', '2025-04-27 21:36:19', NULL, '192.168.200.1', '', 'gmall', '', '', '', 'yaml', '', '');
INSERT INTO `config_info` VALUES (2, 'server-gateway-dev.yaml', 'DEFAULT_GROUP', 'server:\n  port: 80\nspring:\n  redis:\n    host: gmall-redis\n    port: 6379\n    database: 0\n    timeout: 1800000\n    password:\n  cloud:\n    gateway:\n      discovery:      #是否与服务发现组件进行结合，通过 serviceId(必须设置成大写) 转发到具体的服务实例。默认为false，设为true便开启通过服务中心的自动根据 serviceId 创建路由的功能。\n        locator:      #路由访问方式：http://Gateway_HOST:Gateway_PORT/大写的serviceId/**，其中微服务应用名默认大写访问。\n          enabled: true\n      routes:\n        - id: service-product\n          uri: lb://service-product\n          predicates:\n            - Path=/*/product/** # 路径匹配\n        - id: service-item\n          uri: lb://service-item\n          predicates:\n            - Path=/*/item/**\n        - id: service-user\n          uri: lb://service-user\n          predicates:\n            - Path=/*/user/**\n        - id: service-cart\n          uri: lb://service-cart\n          predicates:\n            - Path=/*/cart/**\n        - id: service-order\n          uri: lb://service-order\n          predicates:\n            - Path=/*/order/**\n        - id: service-payment\n          uri: lb://service-payment\n          predicates:\n            - Path=/*/payment/**\n        - id: service-activity\n          uri: lb://service-activity\n          predicates:\n            - Path=/*/activity/** # 路径匹配\n        - id: service-comment\n          uri: lb://service-comment\n          predicates:\n            - Path=/*/comment/**\n        #==================web前端==========================\n        - id: web-item\n          uri: lb://web-all\n          predicates:\n            - Host=item.gmall.com\n        #==================首页前端==========================\n        - id: web-index\n          uri: lb://web-all\n          predicates:\n            - Host=www.gmall.com\n        - id: web-list\n          uri: lb://web-all\n          predicates:\n            - Host=list.gmall.com\n        - id: web-passport\n          uri: lb://web-all\n          predicates:\n            - Host=passport.gmall.com\n        - id: web-cart\n          uri: lb://web-all\n          predicates:\n            - Host=cart.gmall.com\n        - id: web-order\n          uri: lb://web-all\n          predicates:\n            - Host=order.gmall.com\n        - id: web-payment\n          uri: lb://web-all\n          predicates:\n            - Host=payment.gmall.com\n        - id: web-activity\n          uri: lb://web-all\n          predicates:\n            - Host=activity.gmall.com\n        - id: web-comment\n          uri: lb://web-all\n          predicates:\n            - Host=comment.gmall.com\nauthUrls:\n  url: trade.html,myOrder.html,#list.html,#addCart.html # 用户访问该控制器的时候，会被拦截跳转到登录！\n', 'da0d672804b00d1bc40978f89630afd4', '2024-11-12 09:16:33', '2025-04-27 21:36:34', NULL, '192.168.200.1', '', 'gmall', '', '', '', 'yaml', '', '');
INSERT INTO `config_info` VALUES (3, 'service-activity-dev.yaml', 'DEFAULT_GROUP', 'server:\n  port: 8200\nspring:\n  datasource:\n    type: com.zaxxer.hikari.HikariDataSource\n    driver-class-name: com.mysql.jdbc.Driver\n    url: jdbc:mysql://gmall-mysql:3306/gmall_activity?characterEncoding=utf-8&useSSL=false\n    username: root\n    password: 123456\n    hikari:\n      connection-test-query: SELECT 1\n      connection-timeout: 60000\n      idle-timeout: 500000\n      max-lifetime: 540000\n      maximum-pool-size: 12\n      minimum-idle: 10\n      pool-name: GuliHikariPool', '4c1846834214a14ad783d77afb98e41b', '2024-11-12 09:16:33', '2025-04-27 21:36:53', NULL, '192.168.200.1', '', 'gmall', '', '', '', 'yaml', '', '');
INSERT INTO `config_info` VALUES (4, 'service-cart-dev.yaml', 'DEFAULT_GROUP', 'server:\n  port: 8201\n# spring:\n#   datasource:\n#     type: com.zaxxer.hikari.HikariDataSource\n#     driver-class-name: com.mysql.jdbc.Driver\n#     url: jdbc:mysql://localhost:3306/gmall_order?characterEncoding=utf-8&useSSL=false\n#     username: root\n#     password: root\n#     hikari:\n#       connection-test-query: SELECT 1\n#       connection-timeout: 60000\n#       idle-timeout: 500000\n#       max-lifetime: 540000\n#       maximum-pool-size: 12\n#       minimum-idle: 10\n#       pool-name: GuliHikariPool\n', '9ac51021de802686cce22a3a173cc7cb', '2024-11-12 09:16:33', '2024-11-12 09:16:33', NULL, '0:0:0:0:0:0:0:1', '', 'gmall', NULL, NULL, NULL, 'yaml', NULL, '');
INSERT INTO `config_info` VALUES (5, 'service-item-dev.yaml', 'DEFAULT_GROUP', 'server:\r\n  port: 8202', 'bb8505f90d2099a1d5267b0e037cf1a3', '2024-11-12 09:16:33', '2024-11-12 09:16:33', NULL, '0:0:0:0:0:0:0:1', '', 'gmall', NULL, NULL, NULL, 'yaml', NULL, '');
INSERT INTO `config_info` VALUES (6, 'service-list-dev.yaml', 'DEFAULT_GROUP', 'server:\n  port: 8203\n\nspring:\n  elasticsearch:\n    rest:\n      uris: http://gmall-es:9200', '042336024b15cca3fc7c8fd1dbc5e98d', '2024-11-12 09:16:33', '2024-11-12 09:18:43', 'nacos', '0:0:0:0:0:0:0:1', '', 'gmall', '', '', '', 'yaml', '', '');
INSERT INTO `config_info` VALUES (7, 'service-order-dev.yaml', 'DEFAULT_GROUP', 'server:\n  port: 8204\nspring:\n  datasource:\n    type: com.zaxxer.hikari.HikariDataSource\n    driver-class-name: com.mysql.jdbc.Driver\n    url: jdbc:mysql://gmall-mysql:3306/gmall_order?characterEncoding=utf-8&useSSL=false\n    username: root\n    password: 123456\n    hikari:\n      connection-test-query: SELECT 1\n      connection-timeout: 60000\n      idle-timeout: 500000\n      max-lifetime: 540000\n      maximum-pool-size: 12\n      minimum-idle: 10\n      pool-name: GuliHikariPool\nware:\n  url: http://localhost:9001', '4def93f7eb1218ae00ec97d274bb93cf', '2024-11-12 09:16:33', '2025-04-27 21:37:17', NULL, '192.168.200.1', '', 'gmall', '', '', '', 'yaml', '', '');
INSERT INTO `config_info` VALUES (8, 'service-payment-dev.yaml', 'DEFAULT_GROUP', 'server:\n  port: 8205\nspring:\n  datasource:\n    type: com.zaxxer.hikari.HikariDataSource\n    driver-class-name: com.mysql.jdbc.Driver\n    url: jdbc:mysql://gmall-mysql:3306/gmall_order?characterEncoding=utf-8&useSSL=false\n    username: root\n    password: 123456\n    hikari:\n      connection-test-query: SELECT 1\n      connection-timeout: 60000\n      idle-timeout: 500000\n      max-lifetime: 540000\n      maximum-pool-size: 12\n      minimum-idle: 10\n      pool-name: GuliHikariPool\nalipay_public_key: MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAkWs+3gXMosiWG+EbfRyotWB0waqU3t7qMQSBxU0r3JZoND53jvWQfzrGZ8W+obMc+OgwupODDVxhG/DEKVBIptuUQYdvAjCSH98m2hclFcksspuCy9xS7PyflPE47pVzS6vA3Slvw5OFQ2qUcku4paWnBxguLUGPjEncij5NcyFyk+/k57MmrVJwCZaI+lFOS3Eq2IXc07tWXO4s/2SWr3EJiwJutOGBdA1ddvv1Urrl0pWpEFg30pJB6J7YteuxdEL90kuO5ed/vnTK5qgQRvEelROkUW44xONk1784v28OJXmGICmNL1+KyM/SFbFOSgJZSV1tEXUzvL/xvzFpLwIDAQAB\nalipay_url: https://openapi.alipay.com/gateway.do\napp_id: 2021001163617452\napp_private_key: MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQC8Z7EZmanxyFGsK4LrIUeKKrrGxWAHIgPmUV8TtZDs+jeplJSw1ckSY63QhEU444D5qd6xruJHBuB33HG+ik4n8N8nRWi3AtMgpC061oq2DcgtIKMmQHO7/poYDwbpDZrOWXIyiNshFfUOSTUpnrS8UvEks6n6xR/G72r2FG07oZzO7g3XsPMr73wpYajMYC/bhTm6CJGEWZikONNDFkQpVHa+zgitwsqlBuvBvVwGwOHA9B8aRfokwAMl6BDXKoH8BNnSEMpWSTRSwbssayXAQWNU7XKDKGozbn4U2dEbl8GCFzikI/T7ybTNm5gs46ZZBGlq/YB4+v4D3t74Vl6nAgMBAAECggEAOidzhehliYkAlLk1huhV0bMQxewEkQ8RzxTM2SORIWS2q7R+FPtYPkHgU92QFFg85lNltsi5dZ0MylKUFXFRYIi8CL4m7V6E1q12fJPeawVkBXHuig8Y6i1TWRvCUUtuvkTjt++AW/0QECHOtBMVzI95eY+vZwVToq8h/+UcNmxKyVt66Qpo4+r+cUvlvGX5mXgQVC5Ftf/MtHA1i+kjtzBITC0xAvmSXKzjN1YhtcS9rXyMHXBiFhXLdmvOXjkn0Okosr2+tmesXfSwDGhH3ZlOdHzit4D602RNl0nTA1dOUWHuCncs1TrWbriax86P/EYvmzMiHWCVTmmNJC0bMQKBgQD0HAXKNsYsdjCQOV4t3SMqOKaul67x/KA20PmMZVfQ2sQkyjyFgWpL8C16Rzf3zI7df+zF5SkvhFY4+LRZVwX5okEFYTzAZ/NYouj1/DABYOPq0E0sY18/xtq7FJ/CIk8qmCqcczqoyaoxoaC1zAt9E4CYE89iEOnO+GhcI3H3LwKBgQDFlQzvbXhWRyRFkeft/a52XLnyj6t9iP7wNGbGCSeoMDrAu3ZgoqacUPWj5MgSFZdT48H9rF4pPixXoe3jfUNsWBUHqD1F2drDz7lpL0PbpSsgy6ei+D4RwTADsuyXwrkvrWrGro+h6pNJFyly3nea/gloDtJTzfhFFwtNfmqyCQKBgBXzMx4UwMscsY82aV6MZO4V+/71CrkdszZaoiXaswPHuB1qxfhnQ6yiYyR8pO62SR5ns120Fnj8WFh1HJpv9cyVp20ZakIO1tXgiDweOh7VnIjvxBC6usTcV6y81QS62w2Ec0hwIBUvVQtzciUGvP25NDX4igxSYwPGWHP4h/XnAoGAcQN2aKTnBgKfPqPcU4ac+drECXggESgBGof+mRu3cT5U/NS9Oz0Nq6+rMVm1DpMHAdbuqRikq1aCqoVWup51qE0hikWy9ndL6GCynvWIDOSGrLWQZ2kyp5kmy5bWOWAJ6Ll6r7Y9NdIk+NOkw614IFFaNAj2STUw4uPxdRvwD3ECgYEArwOZxR3zl/FZfsvVCXfK8/fhuZXMOp6Huwqky4tNpVLvOyihpOJOcIFj6ZJhoVdmiL8p1/1S+Sm/75gx1tpFurKMNcmYZbisEC7Ukx7RQohZhZTqMPgizlVBTu5nR3xkheaJC9odvyjrWQJ569efXo30gkW04aBp7A15VNG5Z/U=\nnotify_payment_url: http://rjsh38.natappfree.cc/api/payment/alipay/callback/notify\nreturn_order_url: http://payment.gmall.com/pay/success.html\nreturn_payment_url: http://api.gmall.com/api/payment/alipay/callback/return\n', '512db27e4db7b786fd8e28999f46f4bc', '2024-11-12 09:16:33', '2025-04-27 21:37:44', NULL, '192.168.200.1', '', 'gmall', '', '', '', 'yaml', '', '');
INSERT INTO `config_info` VALUES (9, 'service-product-dev.yaml', 'DEFAULT_GROUP', 'server:\n  port: 8206\nspring:\n  datasource:\n    type: com.zaxxer.hikari.HikariDataSource\n    driver-class-name: com.mysql.jdbc.Driver\n    url: jdbc:mysql://gmall-mysql:3306/gmall_product?characterEncoding=utf-8&useSSL=false\n    username: root\n    password: 123456\n    hikari:\n      connection-test-query: SELECT 1 # 自动检测连接\n      connection-timeout: 60000 #数据库连接超时时间,默认30秒\n      idle-timeout: 500000 #空闲连接存活最大时间，默认600000（10分钟）\n      max-lifetime: 540000 #此属性控制池中连接的最长生命周期，值0表示无限生命周期，默认1800000即30分钟\n      maximum-pool-size: 12 #连接池最大连接数，默认是10\n      minimum-idle: 10 #最小空闲连接数量\n      pool-name: SPHHikariPool # 连接池名称\nminio:\n  endpointUrl: http://gmall-minio:9000\n  accessKey: admin\n  secreKey: admin123456\n  bucketName: gmall\n', '61e319343441fa1761627e439dfcc3ee', '2024-11-12 09:16:33', '2025-04-27 21:38:04', NULL, '192.168.200.1', '', 'gmall', '', '', '', 'yaml', '', '');
INSERT INTO `config_info` VALUES (10, 'service-task-dev.yaml', 'DEFAULT_GROUP', 'server:\r\n  port: 8207\r\n', 'f63f928c5905b317abec7c22b434f9c9', '2024-11-12 09:16:33', '2024-11-12 09:16:33', NULL, '0:0:0:0:0:0:0:1', '', 'gmall', NULL, NULL, NULL, 'yaml', NULL, '');
INSERT INTO `config_info` VALUES (11, 'service-user-dev.yaml', 'DEFAULT_GROUP', 'server:\n  port: 8208\n\nspring:\n  datasource:\n    type: com.zaxxer.hikari.HikariDataSource\n    driver-class-name: com.mysql.jdbc.Driver\n    url: jdbc:mysql://gmall-mysql:3306/gmall_user?characterEncoding=utf-8&useSSL=false\n    username: root\n    password: 123456\n    hikari:\n      connection-test-query: SELECT 1\n      connection-timeout: 60000\n      idle-timeout: 500000\n      max-lifetime: 540000\n      maximum-pool-size: 12\n      minimum-idle: 10\n      pool-name: GuliHikariPool\n', '0ec877a4f143e534c9f8f9a603437e34', '2024-11-12 09:16:33', '2025-04-27 21:38:30', NULL, '192.168.200.1', '', 'gmall', '', '', '', 'yaml', '', '');
INSERT INTO `config_info` VALUES (12, 'web-all-dev.yaml', 'DEFAULT_GROUP', 'server:\r\n  port: 8300\r\n\r\nspring:\r\n  thymeleaf:\r\n    mode: LEGACYHTML5\r\n    #编码 可不用配置\r\n    encoding: UTF-8\r\n    #开发配置为false,避免修改模板还要重启服务器\r\n    cache: false\r\n    #配置模板路径，默认是templates，可以不用配置\r\n    prefix: classpath:/templates/\r\n', '63683bfbfac5775737692b3850b78962', '2024-11-12 09:16:33', '2024-11-12 09:16:33', NULL, '0:0:0:0:0:0:0:1', '', 'gmall', NULL, NULL, NULL, 'yaml', NULL, '');
INSERT INTO `config_info` VALUES (13, 'service-comment-dev.yaml', 'DEFAULT_GROUP', 'server:\n  port: 8211\nspring:\n  main:\n    allow-bean-definition-overriding: true #当遇到同样名字的时候，是否允许覆盖注册\n  data:\n    mongodb:\n      host: gmall-mongdb\n      port: 27017\n      database: gmall #指定操作的数据库\n  # redis:\n  #   host: 192.168.200.129\n  #   port: 6379\n  #   database: 0\n  #   timeout: 1800000\n  #   password:\n  #   lettuce:\n  #     pool:\n  #       max-active: 20 #最大连接数\n  #       max-wait: -1    #最大阻塞等待时间(负数表示没限制)\n  #       max-idle: 5    #最大空闲\n  #       min-idle: 0     #最小空闲\n  # jackson:\n  #   date-format: yyyy-MM-dd HH:mm:ss\n  #   time-zone: GMT+8', '58c16172c83bb021113169c93fb318e9', '2024-11-12 09:16:33', '2024-11-12 09:20:47', 'nacos', '0:0:0:0:0:0:0:1', '', 'gmall', '', '', '', 'yaml', '', '');
INSERT INTO `config_info` VALUES (14, 'service-mq-dev.yaml', 'DEFAULT_GROUP', 'server:\r\n  port: 8282', '8e27892125791fb1cf2acc89acaac3a7', '2024-11-12 09:16:33', '2024-11-12 09:16:33', NULL, '0:0:0:0:0:0:0:1', '', 'gmall', NULL, NULL, NULL, 'yaml', NULL, '');
INSERT INTO `config_info` VALUES (25, 'service-ware-dev.yaml', 'DEFAULT_GROUP', 'server:\n  port: 9001\n\nmybatis-plus:\n  configuration:\n    log-impl: org.apache.ibatis.logging.stdout.StdOutImpl\n  mapper-locations: classpath:com/atguigu/gmall/*/mapper/*.xml\n\nspring:\n  thymeleaf:\n    #模板的模式，支持 HTML, XML TEXT JAVASCRIPT\n    mode: HTML5\n    #编码 可不用配置\n    encoding: UTF-8\n    #开发配置为false,避免修改模板还要重启服务器\n    cache: false\n    #配置模板路径，默认是templates，可以不用配置\n    prefix: classpath:/templates/\n  rabbitmq:\n    host: gmall-rabbitmq\n    port: 5672\n    username: admin\n    password: admin\n    publisher-confirms: true\n    publisher-returns: true\n    listener:\n      simple:\n        cknowledge-mode: manual\n        prefetch: 1 # 消费者每次从队列获取的消息数量。此属性当不设置时为：轮询分发，设置为1为：公平分发\n  redis:\n    host: gmall-redis\n    port: 6379\n    database: 0\n    timeout: 1800000\n    password:\n    lettuce:\n      pool:\n        max-active: 20 #最大连接数\n        max-wait: -1    #最大阻塞等待时间(负数表示没限制)\n        max-idle: 5    #最大空闲\n        min-idle: 0     #最小空闲\n  datasource:\n    type: com.zaxxer.hikari.HikariDataSource\n    driver-class-name: com.mysql.jdbc.Driver\n    url: jdbc:mysql://gmall-mysql:3306/gmall_ware?characterEncoding=utf-8&useSSL=false\n    username: root\n    password: 123456\n    hikari:\n      connection-test-query: SELECT 1\n      connection-timeout: 60000\n      idle-timeout: 500000\n      max-lifetime: 540000\n      maximum-pool-size: 12\n      minimum-idle: 10\n      pool-name: GuliHikariPool\n  jackson:\n    date-format: yyyy-MM-dd HH:mm:ss\n    time-zone: GMT+8\n\norder:\n  split:\n    url: http://localhost:8204/api/order/orderSplit', '099b19b234f8d01a5fc1ce9047d71f3c', '2024-11-12 09:23:17', '2025-04-27 21:39:06', NULL, '192.168.200.1', '', 'gmall', '', '', '', 'yaml', '', '');

-- ----------------------------
-- Table structure for config_info_aggr
-- ----------------------------
DROP TABLE IF EXISTS `config_info_aggr`;
CREATE TABLE `config_info_aggr`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `data_id` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT 'data_id',
  `group_id` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT 'group_id',
  `datum_id` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT 'datum_id',
  `content` longtext CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT '内容',
  `gmt_modified` datetime NOT NULL COMMENT '修改时间',
  `app_name` varchar(128) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `tenant_id` varchar(128) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT '' COMMENT '租户字段',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_configinfoaggr_datagrouptenantdatum`(`data_id`, `group_id`, `tenant_id`, `datum_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_bin COMMENT = '增加租户字段' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of config_info_aggr
-- ----------------------------

-- ----------------------------
-- Table structure for config_info_beta
-- ----------------------------
DROP TABLE IF EXISTS `config_info_beta`;
CREATE TABLE `config_info_beta`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `data_id` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT 'data_id',
  `group_id` varchar(128) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT 'group_id',
  `app_name` varchar(128) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT 'app_name',
  `content` longtext CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT 'content',
  `beta_ips` varchar(1024) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT 'betaIps',
  `md5` varchar(32) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT 'md5',
  `gmt_create` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `gmt_modified` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '修改时间',
  `src_user` text CHARACTER SET utf8 COLLATE utf8_bin NULL COMMENT 'source user',
  `src_ip` varchar(50) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT 'source ip',
  `tenant_id` varchar(128) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT '' COMMENT '租户字段',
  `encrypted_data_key` text CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT '秘钥',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_configinfobeta_datagrouptenant`(`data_id`, `group_id`, `tenant_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_bin COMMENT = 'config_info_beta' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of config_info_beta
-- ----------------------------

-- ----------------------------
-- Table structure for config_info_tag
-- ----------------------------
DROP TABLE IF EXISTS `config_info_tag`;
CREATE TABLE `config_info_tag`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `data_id` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT 'data_id',
  `group_id` varchar(128) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT 'group_id',
  `tenant_id` varchar(128) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT '' COMMENT 'tenant_id',
  `tag_id` varchar(128) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT 'tag_id',
  `app_name` varchar(128) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT 'app_name',
  `content` longtext CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT 'content',
  `md5` varchar(32) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT 'md5',
  `gmt_create` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `gmt_modified` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '修改时间',
  `src_user` text CHARACTER SET utf8 COLLATE utf8_bin NULL COMMENT 'source user',
  `src_ip` varchar(50) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT 'source ip',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_configinfotag_datagrouptenanttag`(`data_id`, `group_id`, `tenant_id`, `tag_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_bin COMMENT = 'config_info_tag' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of config_info_tag
-- ----------------------------

-- ----------------------------
-- Table structure for config_tags_relation
-- ----------------------------
DROP TABLE IF EXISTS `config_tags_relation`;
CREATE TABLE `config_tags_relation`  (
  `id` bigint(20) NOT NULL COMMENT 'id',
  `tag_name` varchar(128) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT 'tag_name',
  `tag_type` varchar(64) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT 'tag_type',
  `data_id` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT 'data_id',
  `group_id` varchar(128) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT 'group_id',
  `tenant_id` varchar(128) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT '' COMMENT 'tenant_id',
  `nid` bigint(20) NOT NULL AUTO_INCREMENT,
  PRIMARY KEY (`nid`) USING BTREE,
  UNIQUE INDEX `uk_configtagrelation_configidtag`(`id`, `tag_name`, `tag_type`) USING BTREE,
  INDEX `idx_tenant_id`(`tenant_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_bin COMMENT = 'config_tag_relation' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of config_tags_relation
-- ----------------------------

-- ----------------------------
-- Table structure for group_capacity
-- ----------------------------
DROP TABLE IF EXISTS `group_capacity`;
CREATE TABLE `group_capacity`  (
  `id` bigint(20) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `group_id` varchar(128) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL DEFAULT '' COMMENT 'Group ID，空字符表示整个集群',
  `quota` int(10) UNSIGNED NOT NULL DEFAULT 0 COMMENT '配额，0表示使用默认值',
  `usage` int(10) UNSIGNED NOT NULL DEFAULT 0 COMMENT '使用量',
  `max_size` int(10) UNSIGNED NOT NULL DEFAULT 0 COMMENT '单个配置大小上限，单位为字节，0表示使用默认值',
  `max_aggr_count` int(10) UNSIGNED NOT NULL DEFAULT 0 COMMENT '聚合子配置最大个数，，0表示使用默认值',
  `max_aggr_size` int(10) UNSIGNED NOT NULL DEFAULT 0 COMMENT '单个聚合数据的子配置大小上限，单位为字节，0表示使用默认值',
  `max_history_count` int(10) UNSIGNED NOT NULL DEFAULT 0 COMMENT '最大变更历史数量',
  `gmt_create` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `gmt_modified` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_group_id`(`group_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_bin COMMENT = '集群、各Group容量信息表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of group_capacity
-- ----------------------------

-- ----------------------------
-- Table structure for his_config_info
-- ----------------------------
DROP TABLE IF EXISTS `his_config_info`;
CREATE TABLE `his_config_info`  (
  `id` bigint(20) UNSIGNED NOT NULL,
  `nid` bigint(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `data_id` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `group_id` varchar(128) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `app_name` varchar(128) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT 'app_name',
  `content` longtext CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `md5` varchar(32) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `gmt_create` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `gmt_modified` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `src_user` text CHARACTER SET utf8 COLLATE utf8_bin NULL,
  `src_ip` varchar(50) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `op_type` char(10) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `tenant_id` varchar(128) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT '' COMMENT '租户字段',
  `encrypted_data_key` text CHARACTER SET utf8 COLLATE utf8_bin NULL COMMENT '秘钥',
  PRIMARY KEY (`nid`) USING BTREE,
  INDEX `idx_gmt_create`(`gmt_create`) USING BTREE,
  INDEX `idx_gmt_modified`(`gmt_modified`) USING BTREE,
  INDEX `idx_did`(`data_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 34 CHARACTER SET = utf8 COLLATE = utf8_bin COMMENT = '多租户改造' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of his_config_info
-- ----------------------------
INSERT INTO `his_config_info` VALUES (1, 26, 'common.yaml', 'DEFAULT_GROUP', '', 'mybatis-plus:\n  configuration:\n    log-impl: org.apache.ibatis.logging.stdout.StdOutImpl\n  mapper-locations: classpath:mapper/*Mapper.xml\nfeign:\n  sentinel:\n    enabled: true\n  client:\n    config:\n      default:\n        readTimeout: 30000\n        connectTimeout: 10000\nspring:\n  zipkin:\n    base-url: http://gmal-zipkin:9411\n    discovery-client-enabled: false\n    sender:\n      type: web\n  cloud:\n    sentinel:\n      transport:\n        dashboard: http://gmal-sentinel:8858\n  rabbitmq:\n    host: gmal-rabbitmq\n    port: 5672\n    username: guest\n    password: guest\n    publisher-confirm-type: correlated\n    publisher-returns: true\n    listener:\n      simple:\n        acknowledge-mode: manual #默认情况下消息消费者是自动确认消息的，如果要手动确认消息则需要修改确认模式为manual\n        prefetch: 1 # 消费者每次从队列获取的消息数量。此属性当不设置时为：轮询分发，设置为1为：公平分发\n  redis:\n    host: gmal-redis\n    port: 6379\n    database: 0\n    timeout: 1800000\n    password:\n    lettuce:\n      pool:\n        max-active: 20 #最大连接数\n        max-wait: -1    #最大阻塞等待时间(负数表示没限制)\n        max-idle: 5    #最大空闲\n        min-idle: 0     #最小空闲\n  jackson:\n    date-format: yyyy-MM-dd HH:mm:ss\n    time-zone: GMT+8', 'b24013121ca2a9838e1e9479f335c28f', '2025-04-28 10:36:19', '2025-04-27 21:36:19', NULL, '192.168.200.1', 'U', 'gmall', NULL);
INSERT INTO `his_config_info` VALUES (2, 27, 'server-gateway-dev.yaml', 'DEFAULT_GROUP', '', 'server:\n  port: 80\nspring:\n  redis:\n    host: gmal-redis\n    port: 6379\n    database: 0\n    timeout: 1800000\n    password:\n  cloud:\n    gateway:\n      discovery:      #是否与服务发现组件进行结合，通过 serviceId(必须设置成大写) 转发到具体的服务实例。默认为false，设为true便开启通过服务中心的自动根据 serviceId 创建路由的功能。\n        locator:      #路由访问方式：http://Gateway_HOST:Gateway_PORT/大写的serviceId/**，其中微服务应用名默认大写访问。\n          enabled: true\n      routes:\n        - id: service-product\n          uri: lb://service-product\n          predicates:\n            - Path=/*/product/** # 路径匹配\n        - id: service-item\n          uri: lb://service-item\n          predicates:\n            - Path=/*/item/**\n        - id: service-user\n          uri: lb://service-user\n          predicates:\n            - Path=/*/user/**\n        - id: service-cart\n          uri: lb://service-cart\n          predicates:\n            - Path=/*/cart/**\n        - id: service-order\n          uri: lb://service-order\n          predicates:\n            - Path=/*/order/**\n        - id: service-payment\n          uri: lb://service-payment\n          predicates:\n            - Path=/*/payment/**\n        - id: service-activity\n          uri: lb://service-activity\n          predicates:\n            - Path=/*/activity/** # 路径匹配\n        - id: service-comment\n          uri: lb://service-comment\n          predicates:\n            - Path=/*/comment/**\n        #==================web前端==========================\n        - id: web-item\n          uri: lb://web-all\n          predicates:\n            - Host=item.gmall.com\n        #==================首页前端==========================\n        - id: web-index\n          uri: lb://web-all\n          predicates:\n            - Host=www.gmall.com\n        - id: web-list\n          uri: lb://web-all\n          predicates:\n            - Host=list.gmall.com\n        - id: web-passport\n          uri: lb://web-all\n          predicates:\n            - Host=passport.gmall.com\n        - id: web-cart\n          uri: lb://web-all\n          predicates:\n            - Host=cart.gmall.com\n        - id: web-order\n          uri: lb://web-all\n          predicates:\n            - Host=order.gmall.com\n        - id: web-payment\n          uri: lb://web-all\n          predicates:\n            - Host=payment.gmall.com\n        - id: web-activity\n          uri: lb://web-all\n          predicates:\n            - Host=activity.gmall.com\n        - id: web-comment\n          uri: lb://web-all\n          predicates:\n            - Host=comment.gmall.com\nauthUrls:\n  url: trade.html,myOrder.html,#list.html,#addCart.html # 用户访问该控制器的时候，会被拦截跳转到登录！\n', '5d666884b95b85ebb8b6f9ca313b0256', '2025-04-28 10:36:34', '2025-04-27 21:36:34', NULL, '192.168.200.1', 'U', 'gmall', NULL);
INSERT INTO `his_config_info` VALUES (3, 28, 'service-activity-dev.yaml', 'DEFAULT_GROUP', '', 'server:\n  port: 8200\nspring:\n  datasource:\n    type: com.zaxxer.hikari.HikariDataSource\n    driver-class-name: com.mysql.jdbc.Driver\n    url: jdbc:mysql://gmall-mysql:3306/gmall_activity?characterEncoding=utf-8&useSSL=false\n    username: root\n    password: root\n    hikari:\n      connection-test-query: SELECT 1\n      connection-timeout: 60000\n      idle-timeout: 500000\n      max-lifetime: 540000\n      maximum-pool-size: 12\n      minimum-idle: 10\n      pool-name: GuliHikariPool', 'f37189630ceff2c11d3dfeed66a88e92', '2025-04-28 10:36:53', '2025-04-27 21:36:53', NULL, '192.168.200.1', 'U', 'gmall', NULL);
INSERT INTO `his_config_info` VALUES (7, 29, 'service-order-dev.yaml', 'DEFAULT_GROUP', '', 'server:\n  port: 8204\nspring:\n  datasource:\n    type: com.zaxxer.hikari.HikariDataSource\n    driver-class-name: com.mysql.jdbc.Driver\n    url: jdbc:mysql://gmall-mysql:3306/gmall_order?characterEncoding=utf-8&useSSL=false\n    username: root\n    password: root\n    hikari:\n      connection-test-query: SELECT 1\n      connection-timeout: 60000\n      idle-timeout: 500000\n      max-lifetime: 540000\n      maximum-pool-size: 12\n      minimum-idle: 10\n      pool-name: GuliHikariPool\nware:\n  url: http://localhost:9001', 'd2757f215c4fa2e3a78f36c7d3223d16', '2025-04-28 10:37:17', '2025-04-27 21:37:17', NULL, '192.168.200.1', 'U', 'gmall', NULL);
INSERT INTO `his_config_info` VALUES (8, 30, 'service-payment-dev.yaml', 'DEFAULT_GROUP', '', 'server:\n  port: 8205\nspring:\n  datasource:\n    type: com.zaxxer.hikari.HikariDataSource\n    driver-class-name: com.mysql.jdbc.Driver\n    url: jdbc:mysql://gmal-mysql:3306/gmall_order?characterEncoding=utf-8&useSSL=false\n    username: root\n    password: root\n    hikari:\n      connection-test-query: SELECT 1\n      connection-timeout: 60000\n      idle-timeout: 500000\n      max-lifetime: 540000\n      maximum-pool-size: 12\n      minimum-idle: 10\n      pool-name: GuliHikariPool\nalipay_public_key: MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAkWs+3gXMosiWG+EbfRyotWB0waqU3t7qMQSBxU0r3JZoND53jvWQfzrGZ8W+obMc+OgwupODDVxhG/DEKVBIptuUQYdvAjCSH98m2hclFcksspuCy9xS7PyflPE47pVzS6vA3Slvw5OFQ2qUcku4paWnBxguLUGPjEncij5NcyFyk+/k57MmrVJwCZaI+lFOS3Eq2IXc07tWXO4s/2SWr3EJiwJutOGBdA1ddvv1Urrl0pWpEFg30pJB6J7YteuxdEL90kuO5ed/vnTK5qgQRvEelROkUW44xONk1784v28OJXmGICmNL1+KyM/SFbFOSgJZSV1tEXUzvL/xvzFpLwIDAQAB\nalipay_url: https://openapi.alipay.com/gateway.do\napp_id: 2021001163617452\napp_private_key: MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQC8Z7EZmanxyFGsK4LrIUeKKrrGxWAHIgPmUV8TtZDs+jeplJSw1ckSY63QhEU444D5qd6xruJHBuB33HG+ik4n8N8nRWi3AtMgpC061oq2DcgtIKMmQHO7/poYDwbpDZrOWXIyiNshFfUOSTUpnrS8UvEks6n6xR/G72r2FG07oZzO7g3XsPMr73wpYajMYC/bhTm6CJGEWZikONNDFkQpVHa+zgitwsqlBuvBvVwGwOHA9B8aRfokwAMl6BDXKoH8BNnSEMpWSTRSwbssayXAQWNU7XKDKGozbn4U2dEbl8GCFzikI/T7ybTNm5gs46ZZBGlq/YB4+v4D3t74Vl6nAgMBAAECggEAOidzhehliYkAlLk1huhV0bMQxewEkQ8RzxTM2SORIWS2q7R+FPtYPkHgU92QFFg85lNltsi5dZ0MylKUFXFRYIi8CL4m7V6E1q12fJPeawVkBXHuig8Y6i1TWRvCUUtuvkTjt++AW/0QECHOtBMVzI95eY+vZwVToq8h/+UcNmxKyVt66Qpo4+r+cUvlvGX5mXgQVC5Ftf/MtHA1i+kjtzBITC0xAvmSXKzjN1YhtcS9rXyMHXBiFhXLdmvOXjkn0Okosr2+tmesXfSwDGhH3ZlOdHzit4D602RNl0nTA1dOUWHuCncs1TrWbriax86P/EYvmzMiHWCVTmmNJC0bMQKBgQD0HAXKNsYsdjCQOV4t3SMqOKaul67x/KA20PmMZVfQ2sQkyjyFgWpL8C16Rzf3zI7df+zF5SkvhFY4+LRZVwX5okEFYTzAZ/NYouj1/DABYOPq0E0sY18/xtq7FJ/CIk8qmCqcczqoyaoxoaC1zAt9E4CYE89iEOnO+GhcI3H3LwKBgQDFlQzvbXhWRyRFkeft/a52XLnyj6t9iP7wNGbGCSeoMDrAu3ZgoqacUPWj5MgSFZdT48H9rF4pPixXoe3jfUNsWBUHqD1F2drDz7lpL0PbpSsgy6ei+D4RwTADsuyXwrkvrWrGro+h6pNJFyly3nea/gloDtJTzfhFFwtNfmqyCQKBgBXzMx4UwMscsY82aV6MZO4V+/71CrkdszZaoiXaswPHuB1qxfhnQ6yiYyR8pO62SR5ns120Fnj8WFh1HJpv9cyVp20ZakIO1tXgiDweOh7VnIjvxBC6usTcV6y81QS62w2Ec0hwIBUvVQtzciUGvP25NDX4igxSYwPGWHP4h/XnAoGAcQN2aKTnBgKfPqPcU4ac+drECXggESgBGof+mRu3cT5U/NS9Oz0Nq6+rMVm1DpMHAdbuqRikq1aCqoVWup51qE0hikWy9ndL6GCynvWIDOSGrLWQZ2kyp5kmy5bWOWAJ6Ll6r7Y9NdIk+NOkw614IFFaNAj2STUw4uPxdRvwD3ECgYEArwOZxR3zl/FZfsvVCXfK8/fhuZXMOp6Huwqky4tNpVLvOyihpOJOcIFj6ZJhoVdmiL8p1/1S+Sm/75gx1tpFurKMNcmYZbisEC7Ukx7RQohZhZTqMPgizlVBTu5nR3xkheaJC9odvyjrWQJ569efXo30gkW04aBp7A15VNG5Z/U=\nnotify_payment_url: http://rjsh38.natappfree.cc/api/payment/alipay/callback/notify\nreturn_order_url: http://payment.gmall.com/pay/success.html\nreturn_payment_url: http://api.gmall.com/api/payment/alipay/callback/return\n', '05049e46b4c64b0e429c5cd3a9915ca6', '2025-04-28 10:37:44', '2025-04-27 21:37:44', NULL, '192.168.200.1', 'U', 'gmall', NULL);
INSERT INTO `his_config_info` VALUES (9, 31, 'service-product-dev.yaml', 'DEFAULT_GROUP', '', 'server:\n  port: 8206\nspring:\n  datasource:\n    type: com.zaxxer.hikari.HikariDataSource\n    driver-class-name: com.mysql.jdbc.Driver\n    url: jdbc:mysql://gmal-mysql:3306/gmall_product?characterEncoding=utf-8&useSSL=false\n    username: root\n    password: root\n    hikari:\n      connection-test-query: SELECT 1 # 自动检测连接\n      connection-timeout: 60000 #数据库连接超时时间,默认30秒\n      idle-timeout: 500000 #空闲连接存活最大时间，默认600000（10分钟）\n      max-lifetime: 540000 #此属性控制池中连接的最长生命周期，值0表示无限生命周期，默认1800000即30分钟\n      maximum-pool-size: 12 #连接池最大连接数，默认是10\n      minimum-idle: 10 #最小空闲连接数量\n      pool-name: SPHHikariPool # 连接池名称\nminio:\n  endpointUrl: http://gmal-minio:9000\n  accessKey: admin\n  secreKey: admin123456\n  bucketName: gmall\n', 'f3bf53383e292facb782929ebcd30ddc', '2025-04-28 10:38:04', '2025-04-27 21:38:04', NULL, '192.168.200.1', 'U', 'gmall', NULL);
INSERT INTO `his_config_info` VALUES (11, 32, 'service-user-dev.yaml', 'DEFAULT_GROUP', '', 'server:\n  port: 8208\n\nspring:\n  datasource:\n    type: com.zaxxer.hikari.HikariDataSource\n    driver-class-name: com.mysql.jdbc.Driver\n    url: jdbc:mysql://gmal-mysql:3306/gmall_user?characterEncoding=utf-8&useSSL=false\n    username: root\n    password: root\n    hikari:\n      connection-test-query: SELECT 1\n      connection-timeout: 60000\n      idle-timeout: 500000\n      max-lifetime: 540000\n      maximum-pool-size: 12\n      minimum-idle: 10\n      pool-name: GuliHikariPool\n', 'e1dbc09aaadff8bc769abc4faaccf89b', '2025-04-28 10:38:30', '2025-04-27 21:38:30', NULL, '192.168.200.1', 'U', 'gmall', NULL);
INSERT INTO `his_config_info` VALUES (25, 33, 'service-ware-dev.yaml', 'DEFAULT_GROUP', '', 'server:\r\n  port: 9001\r\n\r\nmybatis-plus:\r\n  configuration:\r\n    log-impl: org.apache.ibatis.logging.stdout.StdOutImpl\r\n  mapper-locations: classpath:com/atguigu/gmall/*/mapper/*.xml\r\n\r\nspring:\r\n  thymeleaf:\r\n    #模板的模式，支持 HTML, XML TEXT JAVASCRIPT\r\n    mode: HTML5\r\n    #编码 可不用配置\r\n    encoding: UTF-8\r\n    #开发配置为false,避免修改模板还要重启服务器\r\n    cache: false\r\n    #配置模板路径，默认是templates，可以不用配置\r\n    prefix: classpath:/templates/\r\n  rabbitmq:\r\n    host: gmall-rabbitmq\r\n    port: 5672\r\n    username: guest\r\n    password: guest\r\n    publisher-confirms: true\r\n    publisher-returns: true\r\n    listener:\r\n      simple:\r\n        cknowledge-mode: manual\r\n        prefetch: 1 # 消费者每次从队列获取的消息数量。此属性当不设置时为：轮询分发，设置为1为：公平分发\r\n  redis:\r\n    host: gmall-redis\r\n    port: 6379\r\n    database: 0\r\n    timeout: 1800000\r\n    password:\r\n    lettuce:\r\n      pool:\r\n        max-active: 20 #最大连接数\r\n        max-wait: -1    #最大阻塞等待时间(负数表示没限制)\r\n        max-idle: 5    #最大空闲\r\n        min-idle: 0     #最小空闲\r\n  datasource:\r\n    type: com.zaxxer.hikari.HikariDataSource\r\n    driver-class-name: com.mysql.jdbc.Driver\r\n    url: jdbc:mysql://gmall-mysql:3306/gmall_ware?characterEncoding=utf-8&useSSL=false\r\n    username: root\r\n    password: root\r\n    hikari:\r\n      connection-test-query: SELECT 1\r\n      connection-timeout: 60000\r\n      idle-timeout: 500000\r\n      max-lifetime: 540000\r\n      maximum-pool-size: 12\r\n      minimum-idle: 10\r\n      pool-name: GuliHikariPool\r\n  jackson:\r\n    date-format: yyyy-MM-dd HH:mm:ss\r\n    time-zone: GMT+8\r\n\r\norder:\r\n  split:\r\n    url: http://localhost:8204/api/order/orderSplit', '344aad9c523607806bd8fd7e9371237b', '2025-04-28 10:39:06', '2025-04-27 21:39:06', NULL, '192.168.200.1', 'U', 'gmall', NULL);

-- ----------------------------
-- Table structure for permissions
-- ----------------------------
DROP TABLE IF EXISTS `permissions`;
CREATE TABLE `permissions`  (
  `role` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `resource` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `action` varchar(8) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  UNIQUE INDEX `uk_role_permission`(`role`, `resource`, `action`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of permissions
-- ----------------------------

-- ----------------------------
-- Table structure for roles
-- ----------------------------
DROP TABLE IF EXISTS `roles`;
CREATE TABLE `roles`  (
  `username` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `role` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  UNIQUE INDEX `idx_user_role`(`username`, `role`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of roles
-- ----------------------------
INSERT INTO `roles` VALUES ('nacos', 'ROLE_ADMIN');

-- ----------------------------
-- Table structure for tenant_capacity
-- ----------------------------
DROP TABLE IF EXISTS `tenant_capacity`;
CREATE TABLE `tenant_capacity`  (
  `id` bigint(20) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `tenant_id` varchar(128) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL DEFAULT '' COMMENT 'Tenant ID',
  `quota` int(10) UNSIGNED NOT NULL DEFAULT 0 COMMENT '配额，0表示使用默认值',
  `usage` int(10) UNSIGNED NOT NULL DEFAULT 0 COMMENT '使用量',
  `max_size` int(10) UNSIGNED NOT NULL DEFAULT 0 COMMENT '单个配置大小上限，单位为字节，0表示使用默认值',
  `max_aggr_count` int(10) UNSIGNED NOT NULL DEFAULT 0 COMMENT '聚合子配置最大个数',
  `max_aggr_size` int(10) UNSIGNED NOT NULL DEFAULT 0 COMMENT '单个聚合数据的子配置大小上限，单位为字节，0表示使用默认值',
  `max_history_count` int(10) UNSIGNED NOT NULL DEFAULT 0 COMMENT '最大变更历史数量',
  `gmt_create` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `gmt_modified` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_tenant_id`(`tenant_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_bin COMMENT = '租户容量信息表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of tenant_capacity
-- ----------------------------

-- ----------------------------
-- Table structure for tenant_info
-- ----------------------------
DROP TABLE IF EXISTS `tenant_info`;
CREATE TABLE `tenant_info`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `kp` varchar(128) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT 'kp',
  `tenant_id` varchar(128) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT '' COMMENT 'tenant_id',
  `tenant_name` varchar(128) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT '' COMMENT 'tenant_name',
  `tenant_desc` varchar(256) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT 'tenant_desc',
  `create_source` varchar(32) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT 'create_source',
  `gmt_create` bigint(20) NOT NULL COMMENT '创建时间',
  `gmt_modified` bigint(20) NOT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_tenant_info_kptenantid`(`kp`, `tenant_id`) USING BTREE,
  INDEX `idx_tenant_id`(`tenant_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8 COLLATE = utf8_bin COMMENT = 'tenant_info' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of tenant_info
-- ----------------------------
INSERT INTO `tenant_info` VALUES (1, '1', 'gmall', 'gmall', 'gmall', 'nacos', 1731402972662, 1731402972662);

-- ----------------------------
-- Table structure for users
-- ----------------------------
DROP TABLE IF EXISTS `users`;
CREATE TABLE `users`  (
  `username` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `password` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `enabled` tinyint(1) NOT NULL,
  PRIMARY KEY (`username`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of users
-- ----------------------------
INSERT INTO `users` VALUES ('nacos', '$2a$10$EuWPZHzz32dJN7jexM34MOeYirDdFAZm2kuWj7VEOJhhZkDrxfvUu', 1);

SET FOREIGN_KEY_CHECKS = 1;
