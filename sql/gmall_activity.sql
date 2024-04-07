/*
SQLyog Ultimate v11.25 (64 bit)
MySQL - 5.7.35 : Database - gmall_activity
*********************************************************************
*/

/*!40101 SET NAMES utf8 */;

/*!40101 SET SQL_MODE=''*/;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
CREATE DATABASE /*!32312 IF NOT EXISTS*/`gmall_activity` /*!40100 DEFAULT CHARACTER SET utf8 */;

USE `gmall_activity`;

/*Table structure for table `activity_info` */

DROP TABLE IF EXISTS `activity_info`;

CREATE TABLE `activity_info` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '活动id',
  `activity_name` varchar(200) DEFAULT NULL COMMENT '活动名称',
  `activity_type` varchar(20) DEFAULT NULL COMMENT '活动类型（1：满减，2：折扣）',
  `activity_desc` varchar(2000) DEFAULT NULL COMMENT '活动描述',
  `start_time` datetime DEFAULT NULL COMMENT '开始时间',
  `end_time` datetime DEFAULT NULL COMMENT '结束时间',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `is_deleted` tinyint(3) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=15 DEFAULT CHARSET=utf8 COMMENT='活动表';

/*Data for the table `activity_info` */

insert  into `activity_info`(`id`,`activity_name`,`activity_type`,`activity_desc`,`start_time`,`end_time`,`create_time`,`update_time`,`is_deleted`) values (1,'双十一Apple iPhone 12 64G 手机活动','FULL_DISCOUNT','双十一活动','2020-10-19 00:00:00','2021-11-26 00:00:00','2021-08-13 15:18:39','2021-08-13 15:18:39',0),(2,'双十一小米10手机活动','FULL_REDUCTION','双十一小米10手机活动','2020-11-04 00:00:00','2021-11-30 00:00:00','2021-08-13 15:18:39','2021-08-13 15:18:39',0),(8,'年底大厂促销 满减','FULL_REDUCTION','asdfd','2020-12-08 00:00:00','2021-12-05 00:00:00','2021-08-13 15:18:39','2021-08-13 15:18:39',0),(12,'年底满减','FULL_REDUCTION','年底满减,优惠多多','2021-01-15 00:00:00','2021-12-05 00:00:00','2021-08-13 15:18:39','2021-08-13 15:18:39',0),(13,'年底满量打折','FULL_DISCOUNT','年底满量打折,折扣多多','2021-01-15 00:00:00','2021-12-05 00:00:00','2021-08-13 15:18:39','2021-08-13 15:18:39',0),(14,'满100减90元','FULL_REDUCTION','5.1大优惠','2021-04-30 00:00:00','2021-12-05 00:00:00','2021-08-13 15:18:39','2021-08-13 15:20:52',0);

/*Table structure for table `activity_rule` */

DROP TABLE IF EXISTS `activity_rule`;

CREATE TABLE `activity_rule` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '编号',
  `activity_id` int(11) DEFAULT NULL COMMENT '类型',
  `activity_type` varchar(20) DEFAULT NULL COMMENT '活动类型',
  `condition_amount` decimal(16,2) DEFAULT NULL COMMENT '满减金额',
  `condition_num` bigint(20) DEFAULT NULL COMMENT '满减件数',
  `benefit_amount` decimal(16,2) DEFAULT NULL COMMENT '优惠金额',
  `benefit_discount` decimal(10,2) DEFAULT NULL COMMENT '优惠折扣',
  `benefit_level` bigint(20) DEFAULT NULL COMMENT '优惠级别',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `is_deleted` tinyint(3) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=78 DEFAULT CHARSET=utf8 COMMENT='优惠规则';

/*Data for the table `activity_rule` */

insert  into `activity_rule`(`id`,`activity_id`,`activity_type`,`condition_amount`,`condition_num`,`benefit_amount`,`benefit_discount`,`benefit_level`,`create_time`,`update_time`,`is_deleted`) values (27,1,'FULL_DISCOUNT',NULL,5,NULL,'9.00',NULL,'2021-08-13 15:18:39','2021-08-13 15:18:39',0),(28,1,'FULL_DISCOUNT',NULL,10,NULL,'8.00',NULL,'2021-08-13 15:18:39','2021-08-13 15:18:39',0),(29,1,'FULL_DISCOUNT',NULL,20,NULL,'6.00',NULL,'2021-08-13 15:18:39','2021-08-13 15:18:39',0),(34,2,'FULL_REDUCTION','10000.00',NULL,'500.00',NULL,NULL,'2021-08-13 15:18:39','2021-08-13 15:18:39',0),(35,2,'FULL_REDUCTION','20000.00',NULL,'1100.00',NULL,NULL,'2021-08-13 15:18:39','2021-08-13 15:18:39',0),(36,2,'FULL_REDUCTION','50000.00',NULL,'5000.00',NULL,NULL,'2021-08-13 15:18:39','2021-08-13 15:18:39',0),(66,8,'FULL_REDUCTION','1000.00',NULL,'100.00',NULL,NULL,'2021-08-13 15:18:39','2021-08-13 15:18:39',0),(67,8,'FULL_REDUCTION','2000.00',NULL,'150.00',NULL,NULL,'2021-08-13 15:18:39','2021-08-13 15:18:39',0),(68,8,'FULL_REDUCTION','2323.00',NULL,'122.00',NULL,NULL,'2021-08-13 15:18:39','2021-08-13 15:18:39',0),(73,10,'FULL_REDUCTION','120.00',NULL,'10.00',NULL,NULL,'2021-08-13 15:18:39','2021-08-13 15:18:39',0),(74,11,'FULL_DISCOUNT','120.00',130,'10.00','10.00',NULL,'2021-08-13 15:18:39','2021-08-13 15:18:39',0),(75,12,'FULL_REDUCTION','1500.00',NULL,'1000.00',NULL,NULL,'2021-08-13 15:18:39','2021-08-13 15:18:39',0),(76,13,'FULL_DISCOUNT','1500.00',5,'1000.00','7.00',NULL,'2021-08-13 15:18:39','2021-08-13 15:18:39',0),(77,14,'FULL_REDUCTION','100.00',NULL,'90.00',NULL,NULL,'2021-08-13 15:18:39','2021-08-13 15:18:39',0);

/*Table structure for table `activity_sku` */

DROP TABLE IF EXISTS `activity_sku`;

CREATE TABLE `activity_sku` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '编号',
  `activity_id` bigint(20) DEFAULT NULL COMMENT '活动id ',
  `sku_id` bigint(20) DEFAULT NULL COMMENT 'sku_id',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `is_deleted` tinyint(3) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=56 DEFAULT CHARSET=utf8 COMMENT='活动参与商品';

/*Data for the table `activity_sku` */

insert  into `activity_sku`(`id`,`activity_id`,`sku_id`,`create_time`,`update_time`,`is_deleted`) values (22,1,8,'2021-08-13 15:18:39','2021-08-13 15:18:39',0),(23,1,9,'2021-08-13 15:18:39','2021-08-13 15:18:39',0),(24,1,10,'2021-08-13 15:18:39','2021-08-13 15:18:39',0),(31,2,1,'2021-08-13 15:18:39','2021-08-13 15:18:39',0),(32,2,3,'2021-08-13 15:18:39','2021-08-13 15:18:39',0),(55,8,5,'2021-08-13 15:18:39','2021-08-13 15:18:39',0);

/*Table structure for table `coupon_info` */

DROP TABLE IF EXISTS `coupon_info`;

CREATE TABLE `coupon_info` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '购物券编号',
  `coupon_name` varchar(100) DEFAULT NULL COMMENT '购物券名称',
  `coupon_type` varchar(20) DEFAULT NULL COMMENT '购物券类型 1 现金券 2 折扣券 3 满减券 4 满件打折券',
  `condition_amount` decimal(10,2) DEFAULT NULL COMMENT '满额数（3）',
  `condition_num` bigint(20) DEFAULT NULL COMMENT '满件数（4）',
  `activity_id` bigint(20) DEFAULT NULL COMMENT '活动编号',
  `benefit_amount` decimal(16,2) DEFAULT NULL COMMENT '减金额（1 3）',
  `benefit_discount` decimal(10,2) DEFAULT NULL COMMENT '折扣（2 4）',
  `range_type` varchar(10) DEFAULT NULL COMMENT '范围类型 1、商品(spuid) 2、品类(三级分类id) 3、品牌',
  `limit_num` int(11) NOT NULL DEFAULT '0' COMMENT '最多领用次数',
  `taken_count` int(11) NOT NULL DEFAULT '0' COMMENT '已领用次数',
  `start_time` datetime DEFAULT NULL COMMENT '可以领取的开始日期',
  `end_time` datetime DEFAULT NULL COMMENT '可以领取的结束日期',
  `operate_time` datetime DEFAULT NULL COMMENT '修改时间',
  `expire_time` datetime DEFAULT NULL COMMENT '过期时间',
  `range_desc` varchar(500) DEFAULT NULL COMMENT '范围描述',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `is_deleted` tinyint(3) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=22 DEFAULT CHARSET=utf8 COMMENT='优惠券表';

/*Data for the table `coupon_info` */

insert  into `coupon_info`(`id`,`coupon_name`,`coupon_type`,`condition_amount`,`condition_num`,`activity_id`,`benefit_amount`,`benefit_discount`,`range_type`,`limit_num`,`taken_count`,`start_time`,`end_time`,`operate_time`,`expire_time`,`range_desc`,`create_time`,`update_time`,`is_deleted`) values (1,'双十一小米满10000减卷150元','FULL_REDUCTION','10000.00',NULL,NULL,'150.00',NULL,'SPU',100,93,'2020-10-25 00:00:00','2021-10-21 00:00:00','2020-11-13 15:52:18','2023-10-19 00:00:00','可购买：小米10,：Redmi 10X','2021-08-13 15:18:39','2021-08-19 11:38:24',0),(2,'双十一小米满减卷200元','FULL_REDUCTION','20000.00',NULL,NULL,'200.00',NULL,'SPU',100,101,'2020-10-25 00:00:00','2021-10-14 00:00:00','2020-11-13 15:52:18','2021-10-22 00:00:00','可购买：小米10,：Redmi 10X','2021-08-13 15:18:39','2021-08-19 11:38:25',0),(3,'双十一小米10 满6500减100元卷','FULL_REDUCTION','6500.00',NULL,2,'100.00',NULL,'SPU',100,4,'2020-11-04 00:00:00','2021-12-05 00:00:00','2020-11-13 15:52:18','2020-12-05 00:00:00','可购买：小米10','2021-08-13 15:18:39','2021-08-19 11:38:26',0),(4,'双十一华为1000元优惠券','FULL_REDUCTION','10000.00',NULL,NULL,'1000.00',NULL,'TRADEMARK',100,9,'2020-11-10 00:00:00','2021-12-04 00:00:00','2020-11-13 15:52:18','2020-12-05 00:00:00','可购买品牌：华为','2021-08-13 15:18:39','2021-08-19 11:38:44',0),(5,'小米10现金卷100','CASH',NULL,NULL,NULL,'100.00',NULL,'SPU',100,4,'2020-11-03 00:00:00','2021-12-05 00:00:00','2020-11-13 15:52:18','2020-12-05 00:00:00','可购买：小米10','2021-08-13 15:18:39','2021-08-19 11:38:26',0),(6,'小米10 五折卷','DISCOUNT',NULL,NULL,NULL,NULL,'6.00','CATAGORY',100,12,'2020-11-03 00:00:00','2021-12-05 00:00:00','2020-11-27 14:14:50','2020-12-05 00:00:00','可购买分类：手机','2021-08-13 15:18:39','2021-08-19 11:38:14',0),(7,'aaa','CASH',NULL,NULL,8,'100.00',NULL,'SPU',12,0,'2020-12-06 00:00:00','2021-12-05 00:00:00','2020-11-27 15:26:09','2020-11-28 00:00:00','可购买：Apple iPhone 12,：HUAWEI P40','2021-08-13 15:18:39','2021-08-19 11:38:27',0),(9,'777','CASH',NULL,NULL,NULL,'555.00',NULL,'SPU',1,2,'2021-01-04 00:00:00','2021-12-05 00:00:00','2021-01-04 13:28:09','2021-01-15 00:00:00','可购买：HUAWEI P40','2021-08-13 15:18:39','2021-08-19 11:38:28',0),(10,'test','CASH',NULL,NULL,NULL,'20.00',NULL,'TRADEMARK',111,27,'2021-01-22 00:00:00','2021-12-05 00:00:00','2021-01-14 16:54:40','2021-03-18 00:00:00','可购买品牌：小米','2021-08-13 15:18:39','2021-08-19 11:38:44',0),(11,'ccc','CASH',NULL,NULL,NULL,'100.00',NULL,'SPU',33,32,'2021-01-05 00:00:00','2021-12-05 00:00:00','2021-01-14 16:56:48','2021-02-27 00:00:00','可购买：Apple iPhone 12','2021-08-13 15:18:39','2021-08-19 11:38:29',0),(12,'atguigu','CASH',NULL,NULL,NULL,'50.00',NULL,'SPU',1000,0,'2021-01-15 00:00:00','2021-12-05 00:00:00','2021-01-14 17:26:47','2021-03-25 00:00:00','','2021-08-13 15:18:39','2021-08-19 11:38:36',0),(13,'atguigu_2','FULL_DISCOUNT',NULL,10,NULL,NULL,'8.00','SPU',1000,5,'2021-01-15 00:00:00','2021-02-28 00:00:00','2021-01-14 17:28:40','2021-03-25 00:00:00','可购买：苹果p20','2021-08-13 15:18:39','2021-08-19 11:38:30',0),(14,'atguigu3','FULL_REDUCTION','1000.00',NULL,NULL,'50.00',NULL,'SPU',111,5,'2021-01-12 00:00:00','2021-02-28 00:00:00','2021-01-14 17:39:11','2021-04-15 00:00:00','可购买：苹果p20','2021-08-13 15:18:39','2021-08-19 11:38:30',0),(15,'年底促销','CASH',NULL,NULL,NULL,'12000.00',NULL,'SPU',10000,96,'2021-01-14 00:00:00','2021-01-31 00:00:00','2021-01-19 00:14:49','2021-02-11 00:00:00','可购买：帅男名媛','2021-08-13 15:18:39','2021-08-19 11:38:31',0),(16,'年底打折','DISCOUNT',NULL,NULL,NULL,NULL,'5.00','SPU',10000,66,'2021-01-14 00:00:00','2021-01-31 00:00:00','2021-01-19 00:14:42','2021-02-11 00:00:00','可购买：帅男名媛','2021-08-13 15:18:39','2021-08-19 11:38:31',0),(20,'年底满减','FULL_REDUCTION','12000.00',NULL,NULL,'11980.00',NULL,'SPU',10000,92,'2021-01-15 00:00:00','2021-01-31 00:00:00','2021-01-19 00:14:37','2021-02-11 00:00:00','可购买：帅男名媛','2021-08-13 15:18:39','2021-08-19 11:38:32',0),(21,'年底满量','FULL_DISCOUNT',NULL,8,NULL,NULL,'5.00','SPU',10000,71,'2021-01-15 00:00:00','2021-01-31 00:00:00','2021-01-24 21:18:21','2021-01-21 10:51:00','可购买：帅男名媛','2021-08-13 15:18:39','2021-08-19 11:38:32',0);

/*Table structure for table `coupon_range` */

DROP TABLE IF EXISTS `coupon_range`;

CREATE TABLE `coupon_range` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '购物券编号',
  `coupon_id` bigint(20) NOT NULL DEFAULT '0' COMMENT '优惠券id',
  `range_type` varchar(20) NOT NULL DEFAULT '' COMMENT '范围类型 1、商品(spuid) 2、品类(三级分类id) 3、品牌',
  `range_id` bigint(20) NOT NULL DEFAULT '0',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `is_deleted` tinyint(3) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=39 DEFAULT CHARSET=utf8 COMMENT='优惠券范围表';

/*Data for the table `coupon_range` */

insert  into `coupon_range`(`id`,`coupon_id`,`range_type`,`range_id`,`create_time`,`update_time`,`is_deleted`) values (6,2,'SPU',1,'2021-08-13 15:18:39','2021-08-13 15:18:39',0),(7,2,'SPU',2,'2021-08-13 15:18:39','2021-08-13 15:18:39',0),(12,4,'TRADEMARK',3,'2021-08-13 15:18:39','2021-08-13 15:18:39',0),(13,5,'SPU',1,'2021-08-13 15:18:39','2021-08-13 15:18:39',0),(15,3,'SPU',1,'2021-08-13 15:18:39','2021-08-13 15:18:39',0),(16,1,'SPU',1,'2021-08-13 15:18:39','2021-08-13 15:18:39',0),(17,1,'SPU',2,'2021-08-13 15:18:39','2021-08-13 15:18:39',0),(20,6,'CATAGORY',61,'2021-08-13 15:18:39','2021-08-13 15:18:39',0),(22,7,'SPU',3,'2021-08-13 15:18:39','2021-08-13 15:18:39',0),(23,7,'SPU',4,'2021-08-13 15:18:39','2021-08-13 15:18:39',0),(24,11,'SPU',3,'2021-08-13 15:18:39','2021-08-13 15:18:39',0),(25,10,'TRADEMARK',5,'2021-08-13 15:18:39','2021-08-13 15:18:39',0),(26,9,'SPU',4,'2021-08-13 15:18:39','2021-08-13 15:18:39',0),(28,13,'SPU',15,'2021-08-13 15:18:39','2021-08-13 15:18:39',0),(29,14,'SPU',15,'2021-08-13 15:18:39','2021-08-13 15:18:39',0),(30,15,'SPU',16,'2021-08-13 15:18:39','2021-08-13 15:18:39',0),(31,16,'SPU',16,'2021-08-13 15:18:39','2021-08-13 15:18:39',0),(34,18,'SPU',16,'2021-08-13 15:18:39','2021-08-13 15:18:39',0),(36,19,'SPU',16,'2021-08-13 15:18:39','2021-08-13 15:18:39',0),(37,20,'SPU',16,'2021-08-13 15:18:39','2021-08-13 15:18:39',0),(38,21,'SPU',16,'2021-08-13 15:18:39','2021-08-13 15:18:39',0);

/*Table structure for table `coupon_use` */

DROP TABLE IF EXISTS `coupon_use`;

CREATE TABLE `coupon_use` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '编号',
  `coupon_id` bigint(20) DEFAULT NULL COMMENT '购物券ID',
  `user_id` bigint(20) DEFAULT NULL COMMENT '用户ID',
  `order_id` bigint(20) DEFAULT NULL COMMENT '订单ID',
  `coupon_status` varchar(10) DEFAULT NULL COMMENT '购物券状态（1：未使用 2：已使用）',
  `get_time` datetime DEFAULT NULL COMMENT '获取时间',
  `using_time` datetime DEFAULT NULL COMMENT '使用时间',
  `used_time` datetime DEFAULT NULL COMMENT '支付时间',
  `expire_time` datetime DEFAULT NULL COMMENT '过期时间',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `is_deleted` tinyint(3) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8 COMMENT='优惠券领用表';

/*Data for the table `coupon_use` */

insert  into `coupon_use`(`id`,`coupon_id`,`user_id`,`order_id`,`coupon_status`,`get_time`,`using_time`,`used_time`,`expire_time`,`create_time`,`update_time`,`is_deleted`) values (1,1,2,NULL,'NOT_USED','2021-08-19 11:47:37',NULL,NULL,'2023-10-19 00:00:00','2021-08-19 11:47:36','2021-08-19 11:47:36',0);

/*Table structure for table `seckill_goods` */

DROP TABLE IF EXISTS `seckill_goods`;

CREATE TABLE `seckill_goods` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `spu_id` bigint(20) DEFAULT NULL COMMENT 'spu_id',
  `sku_id` bigint(20) DEFAULT NULL COMMENT 'sku_id',
  `sku_name` varchar(100) DEFAULT NULL COMMENT '标题',
  `sku_default_img` varchar(150) DEFAULT NULL COMMENT '商品图片',
  `price` decimal(10,2) DEFAULT NULL COMMENT '原价格',
  `cost_price` decimal(10,2) DEFAULT NULL COMMENT '秒杀价格',
  `check_time` datetime DEFAULT NULL COMMENT '审核日期',
  `status` varchar(1) DEFAULT NULL COMMENT '审核状态',
  `start_time` datetime DEFAULT NULL COMMENT '开始时间',
  `end_time` datetime DEFAULT NULL COMMENT '结束时间',
  `num` int(11) DEFAULT NULL COMMENT '秒杀商品数',
  `stock_count` int(11) DEFAULT NULL COMMENT '剩余库存数',
  `sku_desc` varchar(2000) DEFAULT NULL COMMENT '描述',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `is_deleted` tinyint(3) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `seckill_goods` */

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
