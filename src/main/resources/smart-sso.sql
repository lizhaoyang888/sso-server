

create database `jenkins-demo`;

CREATE TABLE `tb_user` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键',
  `name` varchar(11) NOT NULL COMMENT '用户名',
  `password` varchar(11)  NOT NULL COMMENT '密码',
  `lastLoginIp` varchar(20) DEFAULT NULL COMMENT '最后登录IP',
  `loginCount` int(11) NOT NULL COMMENT '登录总次数',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '最后登录时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='用户表'