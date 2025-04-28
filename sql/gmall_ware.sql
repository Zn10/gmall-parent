/*
SQLyog Ultimate v11.25 (64 bit)
MySQL - 5.7.35 : Database - gmall_ware
*********************************************************************
*/

/*!40101 SET NAMES utf8 */;

/*!40101 SET SQL_MODE=''*/;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
CREATE DATABASE /*!32312 IF NOT EXISTS*/`gmall_ware` /*!40100 DEFAULT CHARACTER SET utf8 */;

USE `gmall_ware`;

/*Table structure for table `ware_info` */

DROP TABLE IF EXISTS `ware_info`;

CREATE TABLE `ware_info` (
  `id` bigint(20) NOT NULL,
  `name` varchar(200) DEFAULT NULL,
  `address` varchar(200) DEFAULT NULL,
  `areacode` varchar(20) DEFAULT NULL,
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `is_deleted` tinyint(3) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `ware_info` */

insert  into `ware_info`(`id`,`name`,`address`,`areacode`,`create_time`,`update_time`,`is_deleted`) values (1,'北京大兴仓库','北京大兴','110000','2021-08-13 15:18:42','2021-08-13 15:18:42',0),(2,'北京昌平区仓库','北京昌平','110100','2021-08-13 15:18:42','2021-08-13 15:18:42',0);

/*Table structure for table `ware_order_task` */

DROP TABLE IF EXISTS `ware_order_task`;

CREATE TABLE `ware_order_task` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '编号',
  `order_id` bigint(20) DEFAULT NULL COMMENT '订单编号',
  `consignee` varchar(100) DEFAULT NULL COMMENT '收货人',
  `consignee_tel` varchar(20) DEFAULT NULL COMMENT '收货人电话',
  `delivery_address` varchar(1000) DEFAULT NULL COMMENT '送货地址',
  `order_comment` varchar(200) DEFAULT NULL COMMENT '订单备注',
  `payment_way` varchar(2) DEFAULT NULL COMMENT '付款方式 1:在线付款 2:货到付款',
  `task_status` varchar(20) DEFAULT NULL COMMENT '工作单状态',
  `order_body` varchar(200) DEFAULT NULL COMMENT '订单描述',
  `tracking_no` varchar(200) DEFAULT NULL COMMENT '物流单号',
  `ware_id` bigint(20) DEFAULT NULL COMMENT '仓库编号',
  `task_comment` varchar(500) DEFAULT NULL COMMENT '工作单备注',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `is_deleted` tinyint(3) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8 COMMENT='库存工作单表 库存工作单表';

/*Data for the table `ware_order_task` */

insert  into `ware_order_task`(`id`,`order_id`,`consignee`,`consignee_tel`,`delivery_address`,`order_comment`,`payment_way`,`task_status`,`order_body`,`tracking_no`,`ware_id`,`task_comment`,`create_time`,`update_time`,`is_deleted`) values (1,1,'qigntiqny','15099999999','订个蛋糕梵蒂冈电饭锅地方','','2','SPLIT','红米3 小米77一代 小米MIX2S  ',NULL,NULL,NULL,'2021-08-13 15:18:42','2021-08-13 15:18:42',0),(2,2,'qigntiqny','15099999999','订个蛋糕梵蒂冈电饭锅地方','','2','DEDUCTED','小米77一代 小米MIX2S  ',NULL,1,NULL,'2021-08-13 15:18:42','2021-08-13 15:18:42',0),(3,3,'qigntiqny','15099999999','订个蛋糕梵蒂冈电饭锅地方','','2','DEDUCTED','红米3 ',NULL,2,NULL,'2021-08-13 15:18:42','2021-08-13 15:18:42',0),(4,4,'qigntiqny','15099999999','订个蛋糕梵蒂冈电饭锅地方','','2','DEDUCTED','小米77一代 ',NULL,NULL,NULL,'2021-08-13 15:18:42','2021-08-13 15:18:42',0),(5,5,'qigntiqny','15099999999','订个蛋糕梵蒂冈电饭锅地方','','2','DEDUCTED','小米77一代 ',NULL,1,NULL,'2021-08-13 15:18:42','2021-08-13 15:18:42',0);

/*Table structure for table `ware_order_task_detail` */

DROP TABLE IF EXISTS `ware_order_task_detail`;

CREATE TABLE `ware_order_task_detail` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '编号',
  `sku_id` bigint(20) DEFAULT NULL COMMENT 'sku_id',
  `sku_name` varchar(200) DEFAULT NULL COMMENT 'sku名称',
  `sku_num` int(11) DEFAULT NULL COMMENT '购买个数',
  `task_id` bigint(20) DEFAULT NULL COMMENT '工作单编号',
  `refund_status` varchar(20) DEFAULT NULL,
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `is_deleted` tinyint(3) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8 COMMENT='库存工作单明细表 库存工作单明细表';

/*Data for the table `ware_order_task_detail` */

insert  into `ware_order_task_detail`(`id`,`sku_id`,`sku_name`,`sku_num`,`task_id`,`refund_status`,`create_time`,`update_time`,`is_deleted`) values (1,42,'红米3',1,1,NULL,'2021-08-13 15:18:42','2021-08-13 15:18:42',0),(2,27,'小米77一代',1,1,NULL,'2021-08-13 15:18:42','2021-08-13 15:18:42',0),(3,25,'小米MIX2S ',1,1,NULL,'2021-08-13 15:18:42','2021-08-13 15:18:42',0),(4,27,'小米77一代',1,2,NULL,'2021-08-13 15:18:42','2021-08-13 15:18:42',0),(5,25,'小米MIX2S ',1,2,NULL,'2021-08-13 15:18:42','2021-08-13 15:18:42',0),(6,42,'红米3',1,3,NULL,'2021-08-13 15:18:42','2021-08-13 15:18:42',0),(7,27,'小米77一代',1,4,NULL,'2021-08-13 15:18:42','2021-08-13 15:18:42',0),(8,27,'小米77一代',1,5,NULL,'2021-08-13 15:18:42','2021-08-13 15:18:42',0);

/*Table structure for table `ware_sku` */

DROP TABLE IF EXISTS `ware_sku`;

CREATE TABLE `ware_sku` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '编号',
  `sku_id` bigint(20) DEFAULT NULL COMMENT 'skuid',
  `warehouse_id` bigint(20) DEFAULT NULL COMMENT '仓库id',
  `stock` int(11) DEFAULT NULL COMMENT '库存数',
  `stock_name` varchar(200) DEFAULT NULL COMMENT '存货名称',
  `stock_locked` int(11) DEFAULT NULL COMMENT '锁定库存数',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `is_deleted` tinyint(3) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=18 DEFAULT CHARSET=utf8 COMMENT='sku与仓库关联表';

/*Data for the table `ware_sku` */

insert  into `ware_sku`(`id`,`sku_id`,`warehouse_id`,`stock`,`stock_name`,`stock_locked`,`create_time`,`update_time`,`is_deleted`) values (1,1,1,10000,'小米红米5',60,'2021-08-13 15:18:42','2021-08-13 15:40:41',0),(2,2,2,9980,'小米红米5另一库',100,'2021-08-13 15:18:42','2021-08-13 15:40:42',0),(3,3,1,96,'笔记本',8,'2021-08-13 15:18:42','2021-08-13 15:40:43',0),(4,4,1,1000,'笔记本拯救者',16,'2021-08-13 15:18:42','2021-08-13 15:40:43',0),(5,5,1,92,'苹果手机',9,'2021-08-13 15:18:42','2021-08-13 15:40:45',0),(6,6,2,100,'笔记本电脑',16,'2021-08-13 15:18:42','2021-08-13 15:40:46',0),(7,7,1,100,'小米',42,'2021-08-13 15:18:42','2021-08-13 15:40:47',0),(8,8,1,1000,'小米一代',16,'2021-08-13 15:18:42','2021-08-13 15:40:48',0),(9,9,1,1000,'小米二代',12,'2021-08-13 15:18:42','2021-08-13 15:40:49',0),(10,10,2,100,'小米88',9,'2021-08-13 15:18:42','2021-08-13 15:40:51',0),(11,11,1,100,'小米三代',4,'2021-08-13 15:18:42','2021-08-13 15:40:52',0),(12,12,1,100,'Redmi Note8Pro 16400万全场景四摄 18W快充 红外遥控 4GB+64GB 黑色 游戏智能手机 小米 红米',15,'2021-08-13 15:18:42','2021-08-13 15:40:53',0),(13,13,2,100,'Redmi Note8Pro 16400万全场景四摄 18W快充 红外遥控 3GB+32GB 金色 游戏智能手机 小米 红米	',15,'2021-08-13 15:18:42','2021-08-13 15:40:56',0),(14,14,2,500,'Redmi Note8Pro 16400万全场景四摄 18W快充 红外遥控 8GB+128GB 金色 游戏智能手机 小米 红米',30,'2021-08-13 15:18:42','2021-08-13 15:40:57',0),(15,15,2,100,'小米CC 9 手机 PLUS',9,'2021-08-13 15:18:42','2021-08-13 15:40:59',0),(16,16,2,100,'小米CC 9 手机 一代',24,'2021-08-13 15:18:42','2021-08-13 15:41:01',0),(17,38,1,600,'小米CC 9 手机 二代',48,'2021-08-13 15:18:42','2021-08-13 15:18:42',0);

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
