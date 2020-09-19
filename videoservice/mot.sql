USE mot;
DROP TABLE IF EXISTS `tb_admin_user`;

CREATE TABLE `tb_admin_user` (
                                 `admin_user_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '管理员id',
                                 `login_user_name` varchar(50) UNIQUE NOT NULL COMMENT '管理员登陆名称',
                                 `login_password` varchar(100) NOT NULL COMMENT '管理员登陆密码',
                                 `nick_name` varchar(50) UNIQUE NOT NULL COMMENT '管理员显示昵称',
                                 `locked` tinyint(4) DEFAULT '0' COMMENT '是否锁定 0未锁定 1已锁定无法登陆',
--                                  `userAvatar` mediumblob COMMENT  '用户头像',
                                 `userAvatar` varchar(100) COMMENT  '用户头像',
                                 `isManager` tinyint(4) default '0' NOT NULL comment '是否是管理员',
                                 `email` varchar(50) NOT NULL comment 'email',
                                 PRIMARY KEY (`admin_user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `tb_admin_user` */

-- insert  into `tb_admin_user`(`admin_user_id`,`login_user_name`,`login_password`,`nick_name`,`locked`,`userAvatar`,`isManager`,`email`) values (1,'admin','e10adc3949ba59abbe56e057f20f883e','高松',0,'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',1,'gaosongtju@163.com');
insert  into `tb_admin_user`(`admin_user_id`,`login_user_name`,`login_password`,`nick_name`,`locked`,`userAvatar`,`isManager`,`email`)  values (1,'admin','e10adc3949ba59abbe56e057f20f883e','高松',0,'admin/1.jpg',1,'gaosongtju@163.com');

USE mot;
DROP TABLE if exists `tb_camera`;

CREATE TABLE `tb_camera` (
                           `camera_id` int(10) NOT NULL AUTO_INCREMENT COMMENT '博客表主键id',
                           `camera_name` varchar(200) NOT NULL COMMENT '摄像头名称',
                           `camera_url` varchar(200) NOT NULL COMMENT '相机路径url',
                           `camera_cover_image` varchar(200) NOT NULL COMMENT '相机封面图',

                           `camera_category_id` int(11) NOT NULL COMMENT '相机地点id',
                           `camera_category_name` varchar(50) NOT NULL COMMENT '地点分类(冗余字段)',
                           `camera_enable` tinyint(4) NOT NULL DEFAULT '0' comment '0-未启用 1-启用',

                           `is_deleted` tinyint(4) NOT NULL DEFAULT '0' COMMENT '是否删除 0=否 1=是',
                           `camera_views` int(10) NOT NULL DEFAULT '0' COMMENT '浏览次数',
                           `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '添加时间',
                           `update_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '修改时间',
                           PRIMARY KEY (`camera_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


insert into `tb_camera`(`camera_id`,`camera_name`,`camera_url`,`camera_cover_image`,`camera_category_id`,`camera_category_name`,`camera_views`,`camera_enable`,`is_deleted`,`create_time`,`update_time`)
values (1,'相机1','http://192.168.18.74:4747/video','/admin/dist/img/rand/33.jpg',1,'7楼楼道',100,1,0,'2019-04-24 15:42:23','2019-04-24 15:42:23');

DROP table if exists `tb_person`;
create table  `tb_person`(
                            `id` int(10) AUTO_INCREMENT COMMENT '自增id',
                            `person_id` int(10) NOT NULL COMMENT '行人id',

                            `camera_id` int(10) NOT NULL COMMENT '出现的摄像头',
                            `image_url` varchar(200) NOT NULL COMMENT '图片的路径',
                            `time` bigint(20) NOT NULL COMMENT '出现的时间',
                            `position` char(20) NOT NULL COMMENT '图像中的坐标',
                            PRIMARY KEY (id)

) ENGINE=InnoDB DEFAULT CHARSET=utf8;

insert into `tb_person`(`person_id`,`camera_id`,`image_url`,`time`,`position`)
values (1,1,'/img/1.jpg',2020060916102356,'100 200 300 400');

DROP table if exists `tb_all_person`;
create table `tb_all_person`(
                                `person_id` int(10) NOT NULL COMMENT '行人id',
                                `person_name` varchar(20) NOT NULL COMMENT '行人姓名',
                                `person_image` mediumblob NOT NULL COMMENT '行人缩略图base64',
                                `is_delated` tinyint(4) NOT NULL DEFAULT '0' COMMENT '是否删除 0=否 1=是',
                                `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '添加时间',
                                primary key (person_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP table if exists tb_camera_category;
create table `tb_camera_category`(
                                    `category_id` int(10) NOT NULL AUTO_INCREMENT COMMENT '场所id',
                                    `category_name` varchar(20) NOT NULL COMMENT '场所名字',
                                    `category_icon` varchar(50) NOT NULL COMMENT '场所图标',
                                    `has_camera_num` int(10) NOT NULL default '0' COMMENT '拥有相机数量',
                                    `has_video_num` int(10) NOT NULL default '0' comment '拥有视频数量',
                                    `is_deleted` tinyint(4) NOT NULL DEFAULT '0' COMMENT '是否删除 0=否 1=是',
                                    `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '添加时间',
                                    primary key (category_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
insert  into `tb_camera_category`(`category_id`,`category_name`,`category_icon`,`has_camera_num`,`is_deleted`,`create_time`) values (20,'About','/admin/dist/img/category/10.png',8,0,'2018-11-12 00:28:49');

DROP TABLE IF EXISTS `tb_link`;

CREATE TABLE `tb_link` (
                           `link_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '友链表主键id',
                           `link_type` tinyint(4) NOT NULL DEFAULT '0' COMMENT '友链类别 0-友链 1-推荐 2-个人网站',
                           `link_name` varchar(50) NOT NULL COMMENT '网站名称',
                           `link_url` varchar(100) NOT NULL COMMENT '网站链接',
                           `link_description` varchar(100) NOT NULL COMMENT '网站描述',
                           `link_rank` int(11) NOT NULL DEFAULT '0' COMMENT '用于列表排序',
                           `is_deleted` tinyint(4) NOT NULL DEFAULT '0' COMMENT '是否删除 0-未删除 1-已删除',
                           `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '添加时间',
                           PRIMARY KEY (`link_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


DROP TABLE IF EXISTS `tb_config`;

CREATE TABLE `tb_config` (
                             `config_name` varchar(100) NOT NULL DEFAULT '' COMMENT '配置项的名称',
                             `config_value` varchar(200) NOT NULL DEFAULT '' COMMENT '配置项的值',
                             `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                             `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '修改时间',
                             PRIMARY KEY (`config_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

insert  into `tb_config`(`config_name`,`config_value`) values ('footerAbout','your camera video manager . have fun.');
insert  into `tb_config`(`config_name`,`config_value`) values ('footerCopyRight','2020 高松');
insert  into `tb_config`(`config_name`,`config_value`) values ('footerICP','京ICP备17008806号-3');
insert  into `tb_config`(`config_name`,`config_value`) values ('footerPoweredBy','https://github.com/Pokman321');
insert  into `tb_config`(`config_name`,`config_value`) values ('footerPoweredByURL','https://github.com/Pokman321');
insert  into `tb_config`(`config_name`,`config_value`) values ('websiteDescription','一个管理相机和视频，并实现行人跟踪功能的网站');
insert  into `tb_config`(`config_name`,`config_value`) values ('websiteIcon','/admin/dist/img/favicon.png');
insert  into `tb_config`(`config_name`,`config_value`) values ('websiteLogo','/admin/dist/img/logo2.png');
insert  into `tb_config`(`config_name`,`config_value`) values ('websiteName','camera video manager');
insert  into `tb_config`(`config_name`,`config_value`) values ('yourAvatar','/admin/dist/img/13.png');
insert  into `tb_config`(`config_name`,`config_value`) values ('yourEmail','gaosongtju@163.com');
insert  into `tb_config`(`config_name`,`config_value`) values ('yourName','gs');



DROP table if exists `tb_update`;
create table `tb_update`(
                            `video_id` int(10) NOT NULL AUTO_INCREMENT,
                            `user_name` varchar(20) NOT NULL,
                            `video_name` varchar(20) NOT NULL,
                            `video_cover_image` varchar(200) default null,
                            `video_path` varchar(200) default null,

                            `result_path` varchar(200) default null,
                            `has_result` tinyint(4) default '0',
                            `video_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
                            `result_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,

                            `camera_category_id` int(11) NOT NULL COMMENT '视频地点id',
                            `camera_category_name` varchar(50) NOT NULL COMMENT '地点分类(冗余字段)',
                            `video_views` int(10) NOT NULL default '0',
                            `is_show` tinyint(4) NOT NULL DEFAULT '1',
                            primary key (video_id)

) ENGINE =InnoDB DEFAULT CHARSET=utf8;