/*
SQLyog Ultimate v11.25 (64 bit)
MySQL - 5.7.35 : Database - gmall_order
*********************************************************************
*/

/*!40101 SET NAMES utf8 */;

/*!40101 SET SQL_MODE=''*/;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
CREATE DATABASE /*!32312 IF NOT EXISTS*/`gmall_order` /*!40100 DEFAULT CHARACTER SET utf8 */;

USE `gmall_order`;

/*Table structure for table `cart_info` */

DROP TABLE IF EXISTS `cart_info`;

CREATE TABLE `cart_info` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '编号',
  `user_id` varchar(200) DEFAULT NULL COMMENT '用户id',
  `sku_id` bigint(20) DEFAULT NULL COMMENT 'skuid',
  `cart_price` decimal(10,2) DEFAULT NULL COMMENT '放入购物车时价格',
  `sku_num` int(11) DEFAULT NULL COMMENT '数量',
  `img_url` varchar(200) DEFAULT NULL COMMENT '图片文件',
  `sku_name` varchar(200) DEFAULT NULL COMMENT 'sku名称 (冗余)',
  `is_checked` int(1) DEFAULT NULL,
  `is_ordered` bigint(20) DEFAULT NULL COMMENT '是否已经下单',
  `order_time` datetime DEFAULT NULL COMMENT '下单时间',
  `source_type` varchar(20) DEFAULT NULL COMMENT '来源类型',
  `source_id` bigint(20) DEFAULT NULL COMMENT '来源编号',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `is_deleted` tinyint(3) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8 COMMENT='购物车表 用户登录系统时更新冗余';

/*Data for the table `cart_info` */

insert  into `cart_info`(`id`,`user_id`,`sku_id`,`cart_price`,`sku_num`,`img_url`,`sku_name`,`is_checked`,`is_ordered`,`order_time`,`source_type`,`source_id`,`create_time`,`update_time`,`is_deleted`) values (3,'2',1,'5999.00',1,'http://47.93.148.192:8080/group1/M00/00/01/rBHu8l-rfvmAIpgZAAIvrX6L9fo612.jpg','小米10 至尊纪念版 双模5G 骁龙865 120HZ高刷新率 120倍长焦镜头 120W快充 8GB+128GB 陶瓷黑 游戏手机',1,NULL,NULL,NULL,NULL,'2021-08-13 15:24:39','2021-08-13 15:24:39',0),(4,'2',2,'6999.00',1,'http://47.93.148.192:8080/group1/M00/00/01/rBHu8l-rfvmAcbl2AAFopp2WGBQ404.jpg','小米10 至尊纪念版 双模5G 骁龙865 120HZ高刷新率 120倍长焦镜头 120W快充 12GB+256GB 陶瓷黑 游戏手机',1,NULL,NULL,NULL,NULL,'2021-08-13 15:24:39','2021-08-13 15:24:39',0);

/*Table structure for table `order_detail` */

DROP TABLE IF EXISTS `order_detail`;

CREATE TABLE `order_detail` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '编号',
  `order_id` bigint(20) DEFAULT NULL COMMENT '订单编号',
  `sku_id` bigint(20) DEFAULT NULL COMMENT 'sku_id',
  `sku_name` varchar(200) DEFAULT NULL COMMENT 'sku名称（冗余)',
  `img_url` varchar(200) DEFAULT NULL COMMENT '图片名称（冗余)',
  `order_price` decimal(10,2) DEFAULT NULL COMMENT '购买价格(下单时sku价格）',
  `sku_num` varchar(200) DEFAULT NULL COMMENT '购买个数',
  `source_type` varchar(20) DEFAULT NULL COMMENT '来源类型',
  `source_id` bigint(20) DEFAULT NULL COMMENT '来源编号',
  `split_total_amount` decimal(10,2) DEFAULT NULL,
  `split_activity_amount` decimal(10,2) DEFAULT NULL,
  `split_coupon_amount` decimal(10,2) DEFAULT NULL,
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `is_deleted` tinyint(3) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8 COMMENT='订单明细表';

/*Data for the table `order_detail` */

insert  into `order_detail`(`id`,`order_id`,`sku_id`,`sku_name`,`img_url`,`order_price`,`sku_num`,`source_type`,`source_id`,`split_total_amount`,`split_activity_amount`,`split_coupon_amount`,`create_time`,`update_time`,`is_deleted`) values (1,1,4,'Redmi 10X 4G Helio G85游戏芯 4800万超清四摄 5020mAh大电量 小孔全面屏 128GB大存储 4GB+128GB 冰雾白 游戏智能手机 小米 红米','http://47.93.148.192:8080/group1/M00/00/01/rBHu8l-rgJqAHPnoAAF9hoDNfsc505.jpg','999.00','1','2401',4,'999.00','0.00','0.00','2021-08-13 15:18:40','2021-08-13 15:18:40',0),(2,1,5,'Redmi 10X 4G Helio G85游戏芯 4800万超清四摄 5020mAh大电量 小孔全面屏 128GB大存储 4GB+128GB 明月灰 游戏智能手机 小米 红米','http://47.93.148.192:8080/group1/M00/00/01/rBHu8l-rgJqAHPnoAAF9hoDNfsc505.jpg','999.00','4','2401',5,'3874.00','122.00','0.00','2021-08-13 15:18:40','2021-08-13 15:18:40',0),(3,2,2,'小米10 至尊纪念版 双模5G 骁龙865 120HZ高刷新率 120倍长焦镜头 120W快充 12GB+256GB 陶瓷黑 游戏手机','http://47.93.148.192:8080/group1/M00/00/01/rBHu8l-rfvmAcbl2AAFopp2WGBQ404.jpg','6999.00','1','2401',2,'6999.00','0.00','0.00','2021-08-13 15:18:40','2021-08-13 15:18:40',0),(4,3,5,'Redmi 10X 4G Helio G85游戏芯 4800万超清四摄 5020mAh大电量 小孔全面屏 128GB大存储 4GB+128GB 明月灰 游戏智能手机 小米 红米','http://47.93.148.192:8080/group1/M00/00/01/rBHu8l-rgJqAHPnoAAF9hoDNfsc505.jpg','999.00','1','2401',5,'999.00','0.00','0.00','2021-08-13 15:18:40','2021-08-13 15:18:40',0),(5,4,2,'小米10 至尊纪念版 双模5G 骁龙865 120HZ高刷新率 120倍长焦镜头 120W快充 12GB+256GB 陶瓷黑 游戏手机','http://47.93.148.192:8080/group1/M00/00/01/rBHu8l-rfvmAcbl2AAFopp2WGBQ404.jpg','6999.00','1',NULL,NULL,'6999.00','0.00','0.00','2021-08-13 15:41:16','2021-08-13 15:41:16',0),(6,4,1,'小米10 至尊纪念版 双模5G 骁龙865 120HZ高刷新率 120倍长焦镜头 120W快充 8GB+128GB 陶瓷黑 游戏手机','http://47.93.148.192:8080/group1/M00/00/01/rBHu8l-rfvmAIpgZAAIvrX6L9fo612.jpg','5999.00','1',NULL,NULL,'5999.00','0.00','0.00','2021-08-13 15:41:16','2021-08-13 15:41:16',0);

/*Table structure for table `order_detail_activity` */

DROP TABLE IF EXISTS `order_detail_activity`;

CREATE TABLE `order_detail_activity` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '编号',
  `order_id` bigint(20) DEFAULT NULL COMMENT '订单id',
  `order_detail_id` bigint(20) DEFAULT NULL COMMENT '订单明细id',
  `activity_id` bigint(20) DEFAULT NULL COMMENT '活动ID',
  `activity_rule` bigint(20) DEFAULT NULL COMMENT '活动规则',
  `sku_id` bigint(20) DEFAULT NULL COMMENT 'skuID',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `is_deleted` tinyint(3) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8 COMMENT='订单明细购物券表';

/*Data for the table `order_detail_activity` */

insert  into `order_detail_activity`(`id`,`order_id`,`order_detail_id`,`activity_id`,`activity_rule`,`sku_id`,`create_time`,`update_time`,`is_deleted`) values (1,1,2,8,68,5,'2021-08-13 15:18:40','2021-08-13 15:18:40',0);

/*Table structure for table `order_detail_coupon` */

DROP TABLE IF EXISTS `order_detail_coupon`;

CREATE TABLE `order_detail_coupon` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '编号',
  `order_id` bigint(20) DEFAULT NULL COMMENT '订单id',
  `order_detail_id` bigint(20) DEFAULT NULL COMMENT '订单明细id',
  `coupon_id` bigint(20) DEFAULT NULL COMMENT '购物券ID',
  `sku_id` bigint(20) DEFAULT NULL COMMENT 'skuID',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `is_deleted` tinyint(3) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='订单明细购物券表';

/*Data for the table `order_detail_coupon` */

/*Table structure for table `order_info` */

DROP TABLE IF EXISTS `order_info`;

CREATE TABLE `order_info` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '编号',
  `consignee` varchar(100) DEFAULT NULL COMMENT '收货人',
  `consignee_tel` varchar(20) DEFAULT NULL COMMENT '收件人电话',
  `total_amount` decimal(10,2) DEFAULT NULL COMMENT '总金额',
  `order_status` varchar(20) DEFAULT NULL COMMENT '订单状态',
  `user_id` bigint(20) DEFAULT NULL COMMENT '用户id',
  `payment_way` varchar(20) DEFAULT NULL COMMENT '付款方式',
  `delivery_address` varchar(1000) DEFAULT NULL COMMENT '送货地址',
  `order_comment` varchar(200) DEFAULT NULL COMMENT '订单备注',
  `out_trade_no` varchar(50) DEFAULT NULL COMMENT '订单交易编号（第三方支付用)',
  `trade_body` varchar(200) DEFAULT NULL COMMENT '订单描述(第三方支付用)',
  `operate_time` datetime DEFAULT NULL COMMENT '操作时间',
  `expire_time` datetime DEFAULT NULL COMMENT '失效时间',
  `process_status` varchar(20) DEFAULT NULL COMMENT '进度状态',
  `tracking_no` varchar(1000) DEFAULT NULL COMMENT '物流单编号',
  `parent_order_id` bigint(20) DEFAULT NULL COMMENT '父订单编号',
  `img_url` varchar(200) DEFAULT NULL COMMENT '图片路径',
  `province_id` int(20) DEFAULT NULL COMMENT '地区',
  `activity_reduce_amount` decimal(16,2) DEFAULT NULL COMMENT '促销金额',
  `coupon_amount` decimal(10,2) DEFAULT NULL COMMENT '优惠券',
  `original_total_amount` decimal(16,2) DEFAULT NULL COMMENT '原价金额',
  `feight_fee` decimal(16,2) DEFAULT NULL COMMENT '运费',
  `feight_fee_reduce` decimal(10,2) DEFAULT NULL COMMENT '减免运费',
  `refundable_time` datetime DEFAULT NULL COMMENT '可退款日期（签收后30天）',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `is_deleted` tinyint(3) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8 COMMENT='订单表 订单表';

/*Data for the table `order_info` */

insert  into `order_info`(`id`,`consignee`,`consignee_tel`,`total_amount`,`order_status`,`user_id`,`payment_way`,`delivery_address`,`order_comment`,`out_trade_no`,`trade_body`,`operate_time`,`expire_time`,`process_status`,`tracking_no`,`parent_order_id`,`img_url`,`province_id`,`activity_reduce_amount`,`coupon_amount`,`original_total_amount`,`feight_fee`,`feight_fee_reduce`,`refundable_time`,`create_time`,`update_time`,`is_deleted`) values (1,'张噶','15034569876','4873.00','PAID',2,'1101','昌平区立水桥小区1号楼308室','','ATGUIGU1628561669341764','Redmi 10X 4G Helio G85游戏芯 4800万超清四摄 5020mAh大电量 小孔全面屏 128GB大存储 4GB+128GB 冰雾白 游戏智能手机 小米 红米 Redmi 10X 4','2021-08-10 10:14:29','2021-08-11 10:14:29','0901',NULL,NULL,NULL,1,'122.00','0.00','4995.00','10.00','10.00',NULL,'2021-08-13 15:18:40','2021-08-25 18:34:44',0),(2,'张噶','15034569876','6999.00','PAID',2,'1101','昌平区立水桥小区1号楼308室','','ATGUIGU1628668001994948','小米10 至尊纪念版 双模5G 骁龙865 120HZ高刷新率 120倍长焦镜头 120W快充 12GB+256GB 陶瓷黑 游戏手机 ','2021-08-11 15:46:42','2021-08-12 15:46:42','0901',NULL,NULL,NULL,1,'0.00','0.00','6999.00','10.00','10.00',NULL,'2021-08-13 15:18:40','2021-08-25 18:34:46',0),(3,'张噶','15034569876','999.00','PAID',2,'1101','昌平区立水桥小区1号楼308室','','ATGUIGU1628817290407364','Redmi 10X 4G Helio G85游戏芯 4800万超清四摄 5020mAh大电量 小孔全面屏 128GB大存储 4GB+128GB 明月灰 游戏智能手机 小米 红米 ','2021-08-13 09:14:50','2021-08-14 09:14:50','COMMNET',NULL,NULL,NULL,1,'0.00','0.00','999.00','10.00','10.00',NULL,'2021-08-13 15:18:40','2021-08-25 18:34:47',0),(4,'qigntiqny','15099999999','12998.00','PAID',2,'ONLINE','订个蛋糕梵蒂冈电饭锅地方','','ATGUIGU1628840474760981','小米10 至尊纪念版 双模5G 骁龙865 120HZ高刷新率 120倍长焦镜头 120W快充 12GB+256GB 陶瓷黑 游戏手机小米10 至尊纪念版 双模5G 骁龙865 120HZ高刷新率 120倍长焦镜头 120W快充 8GB+128GB 陶瓷黑 游戏手机','2021-08-13 15:41:15','2021-08-14 15:41:15','COMMNET',NULL,NULL,NULL,NULL,'0.00','0.00','12998.00','0.00',NULL,NULL,'2021-08-13 15:41:15','2021-08-25 18:34:48',0);

/*Table structure for table `order_refund_info` */

DROP TABLE IF EXISTS `order_refund_info`;

CREATE TABLE `order_refund_info` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '编号',
  `user_id` bigint(20) DEFAULT NULL COMMENT '用户id',
  `order_id` bigint(20) DEFAULT NULL COMMENT '订单id',
  `sku_id` bigint(20) DEFAULT NULL COMMENT 'skuid',
  `refund_type` varchar(10) DEFAULT NULL COMMENT '退款类型（1：退款 2：退货）',
  `refund_num` bigint(20) DEFAULT NULL COMMENT '退货件数',
  `refund_amount` decimal(16,2) DEFAULT NULL COMMENT '退款金额',
  `refund_reason_type` varchar(10) DEFAULT NULL COMMENT '原因类型',
  `refund_reason_txt` varchar(20) DEFAULT NULL COMMENT '原因内容',
  `refund_status` varchar(20) DEFAULT NULL COMMENT '退款状态（0：待审批 1：已退款）',
  `approve_operator_id` bigint(20) DEFAULT NULL COMMENT '审批人',
  `approve_time` datetime DEFAULT NULL COMMENT '审批时间',
  `approve_remark` varchar(255) DEFAULT NULL COMMENT '审批备注',
  `tracking_no` varchar(50) DEFAULT NULL COMMENT '退货物流单号',
  `tracking_time` datetime DEFAULT NULL COMMENT '退货单号录入时间',
  `recieve_operator_id` bigint(20) DEFAULT NULL COMMENT '签收人',
  `recieve_time` datetime DEFAULT NULL COMMENT '签收时间',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `is_deleted` tinyint(3) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='退单表';

/*Data for the table `order_refund_info` */

/*Table structure for table `order_status_log` */

DROP TABLE IF EXISTS `order_status_log`;

CREATE TABLE `order_status_log` (
  `id` bigint(11) NOT NULL AUTO_INCREMENT,
  `order_id` bigint(11) DEFAULT NULL,
  `order_status` varchar(11) DEFAULT NULL,
  `operate_time` datetime DEFAULT NULL,
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `is_deleted` tinyint(3) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8;

/*Data for the table `order_status_log` */

insert  into `order_status_log`(`id`,`order_id`,`order_status`,`operate_time`,`create_time`,`update_time`,`is_deleted`) values (1,1,'1001','2021-08-10 10:14:29','2021-08-13 15:18:40','2021-08-13 15:18:40',0),(2,2,'1001','2021-08-11 15:46:42','2021-08-13 15:18:40','2021-08-13 15:18:40',0),(3,3,'1001','2021-08-13 09:14:50','2021-08-13 15:18:40','2021-08-13 15:18:40',0),(4,4,'UNPAID','2021-08-13 15:41:16','2021-08-13 15:41:16','2021-08-13 15:41:16',0);

/*Table structure for table `payment_info` */

DROP TABLE IF EXISTS `payment_info`;

CREATE TABLE `payment_info` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '编号',
  `out_trade_no` varchar(50) DEFAULT NULL COMMENT '对外业务编号',
  `order_id` varchar(50) DEFAULT NULL COMMENT '订单编号',
  `user_id` bigint(20) DEFAULT NULL COMMENT '用户id',
  `payment_type` varchar(20) DEFAULT NULL COMMENT '支付类型（微信 支付宝）',
  `trade_no` varchar(50) DEFAULT NULL COMMENT '交易编号',
  `total_amount` decimal(10,2) DEFAULT NULL COMMENT '支付金额',
  `subject` varchar(200) DEFAULT NULL COMMENT '交易内容',
  `payment_status` varchar(20) DEFAULT NULL COMMENT '支付状态',
  `callback_time` datetime DEFAULT NULL COMMENT '回调时间',
  `callback_content` text COMMENT '回调信息',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `is_deleted` tinyint(3) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8 COMMENT='支付信息表';

/*Data for the table `payment_info` */

insert  into `payment_info`(`id`,`out_trade_no`,`order_id`,`user_id`,`payment_type`,`trade_no`,`total_amount`,`subject`,`payment_status`,`callback_time`,`callback_content`,`create_time`,`update_time`,`is_deleted`) values (1,'ATGUIGU1628561669341764','1',4,'1101',NULL,'4873.00','Redmi 10X 4G Helio G85游戏芯 4800万超清四摄 5020mAh大电量 小孔全面屏 128GB大存储 4GB+128GB 冰雾白 游戏智能手机 小米 红米 Redmi 10X 4','0801',NULL,NULL,'2021-08-13 15:18:40','2021-08-13 15:18:40',0),(2,'ATGUIGU1628668001994948','2',4,'1101',NULL,'6999.00','小米10 至尊纪念版 双模5G 骁龙865 120HZ高刷新率 120倍长焦镜头 120W快充 12GB+256GB 陶瓷黑 游戏手机 ','0801',NULL,NULL,'2021-08-13 15:18:40','2021-08-13 15:18:40',0),(3,'ATGUIGU1628817290407364','3',4,'1101',NULL,'999.00','Redmi 10X 4G Helio G85游戏芯 4800万超清四摄 5020mAh大电量 小孔全面屏 128GB大存储 4GB+128GB 明月灰 游戏智能手机 小米 红米 ','0801',NULL,NULL,'2021-08-13 15:18:40','2021-08-13 15:18:40',0);

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
