/*
SQLyog Ultimate v11.25 (64 bit)
MySQL - 5.7.35 : Database - gmall_user
*********************************************************************
*/

/*!40101 SET NAMES utf8 */;

/*!40101 SET SQL_MODE=''*/;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
CREATE DATABASE /*!32312 IF NOT EXISTS*/`gmall_user` /*!40100 DEFAULT CHARACTER SET utf8 */;

USE `gmall_user`;

/*Table structure for table `user_address` */

DROP TABLE IF EXISTS `user_address`;

CREATE TABLE `user_address` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '编号',
  `user_id` bigint(20) DEFAULT NULL COMMENT '用户id',
  `province_id` bigint(20) DEFAULT NULL COMMENT '省份id',
  `user_address` varchar(500) DEFAULT NULL COMMENT '用户地址',
  `consignee` varchar(40) DEFAULT NULL COMMENT '收件人',
  `phone_num` varchar(40) DEFAULT NULL COMMENT '联系方式',
  `is_default` varchar(1) DEFAULT NULL COMMENT '是否是默认',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `is_deleted` tinyint(3) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8 COMMENT='用户地址表';

/*Data for the table `user_address` */

insert  into `user_address`(`id`,`user_id`,`province_id`,`user_address`,`consignee`,`phone_num`,`is_default`,`create_time`,`update_time`,`is_deleted`) values (1,1,1,'北京市海淀区1','atguigu','1','0','2021-08-13 15:18:42','2021-08-13 15:18:42',0),(2,1,1,'北京市昌平区2','admin','2','1','2021-08-13 15:18:42','2021-08-13 15:18:42',0),(4,1,2,'发打发第三方第三方','晴天','15099999999','0','2021-08-13 15:18:42','2021-08-13 15:18:42',0),(5,2,8,'订个蛋糕梵蒂冈电饭锅地方','qigntiqny','15099999999','1','2021-08-13 15:18:42','2021-08-13 15:18:42',0),(6,2,3,'555555555555','晴天1','15888888877','0','2021-08-13 15:18:42','2021-08-13 15:18:42',0),(8,5,2,'西城区北三路','张三','15099999999','1','2021-08-13 15:18:42','2021-08-13 15:18:42',0),(10,6,1,'天通苑1','晴天-11','15666666666','0','2021-08-13 15:18:42','2021-08-13 15:18:42',0),(11,6,1,'天通苑1','晴天-2','15666666666','1','2021-08-13 15:18:42','2021-08-13 15:18:42',0),(12,7,1,'天通苑1号','晴天-00','15677777777','1','2021-08-13 15:18:42','2021-08-13 15:18:42',0);

/*Table structure for table `user_info` */

DROP TABLE IF EXISTS `user_info`;

CREATE TABLE `user_info` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '编号',
  `login_name` varchar(200) DEFAULT NULL COMMENT '用户名称',
  `nick_name` varchar(200) DEFAULT NULL COMMENT '用户昵称',
  `passwd` varchar(200) DEFAULT NULL COMMENT '用户密码',
  `name` varchar(200) DEFAULT NULL COMMENT '用户姓名',
  `phone_num` varchar(200) DEFAULT NULL COMMENT '手机号',
  `email` varchar(200) DEFAULT NULL COMMENT '邮箱',
  `head_img` varchar(200) DEFAULT NULL COMMENT '头像',
  `user_level` varchar(200) DEFAULT NULL COMMENT '用户级别',
  `status` tinyint(3) DEFAULT NULL,
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `is_deleted` tinyint(3) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=22 DEFAULT CHARSET=utf8 COMMENT='用户表';

/*Data for the table `user_info` */

insert  into `user_info`(`id`,`login_name`,`nick_name`,`passwd`,`name`,`phone_num`,`email`,`head_img`,`user_level`,`status`,`create_time`,`update_time`,`is_deleted`) values (1,'atguigu','Atguigu','96e79218965eb72c92a549dd5a330112','尚硅谷','11111','atguigu.com','http://thirdwx.qlogo.cn/mmopen/vi_32/DYAIOgq83eoj0hHXhgJNOTSOFsS4uZs8x1ConecaVOB8eIl115xmJZcT4oCicvia7wMEufibKtTLqiaJeanU2Lpg3w/132','1',1,'2021-08-13 15:18:42','2021-08-17 16:01:47',0),(2,'13700000000','Administrator','96e79218965eb72c92a549dd5a330112','Admin','2222','upd@qq.com','http://thirdwx.qlogo.cn/mmopen/vi_32/DYAIOgq83eoj0hHXhgJNOTSOFsS4uZs8x1ConecaVOB8eIl115xmJZcT4oCicvia7wMEufibKtTLqiaJeanU2Lpg3w/132','2',1,'2021-08-13 15:18:42','2021-08-17 16:01:46',0),(3,'admin','admin','96e79218965eb72c92a549dd5a330112','haha','123','atguigu.com','http://thirdwx.qlogo.cn/mmopen/vi_32/DYAIOgq83eoj0hHXhgJNOTSOFsS4uZs8x1ConecaVOB8eIl115xmJZcT4oCicvia7wMEufibKtTLqiaJeanU2Lpg3w/132','3',1,'2021-08-13 15:18:42','2021-08-17 16:01:46',0),(4,'15099999999','15099999999','96e79218965eb72c92a549dd5a330112','15099999999','15099999999',NULL,'http://thirdwx.qlogo.cn/mmopen/vi_32/DYAIOgq83eoj0hHXhgJNOTSOFsS4uZs8x1ConecaVOB8eIl115xmJZcT4oCicvia7wMEufibKtTLqiaJeanU2Lpg3w/132','1',1,'2021-08-13 15:18:42','2021-08-13 15:18:42',0),(5,'15666666666','晴天-奔','96e79218965eb72c92a549dd5a330112','晴天-奔','15666666666',NULL,'http://thirdwx.qlogo.cn/mmopen/vi_32/DYAIOgq83eoj0hHXhgJNOTSOFsS4uZs8x1ConecaVOB8eIl115xmJZcT4oCicvia7wMEufibKtTLqiaJeanU2Lpg3w/132','1',1,'2021-08-13 15:18:42','2021-08-13 15:18:42',0),(6,'15000000000','11','96e79218965eb72c92a549dd5a330112','11','15000000000',NULL,'http://thirdwx.qlogo.cn/mmopen/vi_32/DYAIOgq83eoj0hHXhgJNOTSOFsS4uZs8x1ConecaVOB8eIl115xmJZcT4oCicvia7wMEufibKtTLqiaJeanU2Lpg3w/132','1',1,'2021-08-13 15:18:42','2021-08-13 15:18:42',0),(7,'18900000000','晴天-0','96e79218965eb72c92a549dd5a330112','晴天-0','18900000000',NULL,'http://thirdwx.qlogo.cn/mmopen/vi_32/DYAIOgq83eoj0hHXhgJNOTSOFsS4uZs8x1ConecaVOB8eIl115xmJZcT4oCicvia7wMEufibKtTLqiaJeanU2Lpg3w/132','1',1,'2021-08-13 15:18:42','2021-08-13 15:18:42',0),(8,'15777777777','111','bbb8aae57c104cda40c93843ad5e6db8','111','15777777777',NULL,'http://thirdwx.qlogo.cn/mmopen/vi_32/DYAIOgq83eoj0hHXhgJNOTSOFsS4uZs8x1ConecaVOB8eIl115xmJZcT4oCicvia7wMEufibKtTLqiaJeanU2Lpg3w/132','1',1,'2021-08-13 15:18:42','2021-08-13 15:18:42',0),(21,'1509999999','11','698d51a19d8a121ce581499d7b701668','11','1509999999',NULL,'http://thirdwx.qlogo.cn/mmopen/vi_32/DYAIOgq83eoj0hHXhgJNOTSOFsS4uZs8x1ConecaVOB8eIl115xmJZcT4oCicvia7wMEufibKtTLqiaJeanU2Lpg3w/132','1',1,'2021-08-13 15:18:42','2021-08-13 15:18:42',0);

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
