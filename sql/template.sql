/*
 Navicat Premium Data Transfer

 Source Server         : localhost
 Source Server Type    : MySQL
 Source Server Version : 50726
 Source Host           : localhost:3306
 Source Schema         : template

 Target Server Type    : MySQL
 Target Server Version : 50726
 File Encoding         : 65001

 Date: 23/04/2021 00:40:46
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for sys_admin
-- ----------------------------
DROP TABLE IF EXISTS `sys_admin`;
CREATE TABLE `sys_admin`  (
  `id` int(11) UNSIGNED NOT NULL AUTO_INCREMENT,
  `username` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '用户名/账号',
  `password` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '密码',
  `nick_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '用户昵称',
  `avatar` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '头像（使用对象存储路径）',
  `roles` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '角色ID',
  `status` tinyint(1) NULL DEFAULT NULL COMMENT '状态（0：禁用，1：启用）',
  `token` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '登录标识（token）',
  `login_time` datetime(0) NULL DEFAULT NULL COMMENT '登录时间',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 8 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '后台用户表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_admin
-- ----------------------------
INSERT INTO `sys_admin` VALUES (1, 'admin', '1f63ca875f9c931a4000d81095410515', '超级管理员', 'https://wpimg.wallstcn.com/f778738c-e4f8-4870-b634-56703b4acafe.gif?imageView2/1/w/80/h/80', '1', 1, 'eyJhbGciOiJIUzUxMiJ9.eyJjcmVhdGVkIjoxNjE5MTA5NDg3NjkzLCJleHAiOjE2MTk3MTQyODcsInVzZXJuYW1lIjoiYWRtaW4ifQ.cSRpuIsyIhtzUPjf4pQeQ4AM7e30bMoKlJVCnCRTNdrKWnkIFtbNM0_EsBWscDu2L9DzbcyQfuLmQYz_f5Sqmw', '2021-04-23 00:38:08', '2021-04-15 09:04:43', '2021-04-22 13:11:59');
INSERT INTO `sys_admin` VALUES (4, 'test01', 'e6427bb3d11b7f2c94cfa0666d5fe941', '用户管理', NULL, '2', 1, 'eyJhbGciOiJIUzUxMiJ9.eyJjcmVhdGVkIjoxNjE5MTA3MTc3OTU2LCJleHAiOjE2MTk3MTE5NzcsInVzZXJuYW1lIjoidGVzdDAxIn0.vgToT_tNEV423YsAkTUs1w_zMH1rwq-6Ws0bg459P9Toulrgg6ZIHlLBDL4DgyWVmnd_a3HyLY0fgQqaMjGztQ', '2021-04-22 23:59:38', '2021-04-22 20:40:47', '2021-04-22 20:40:47');
INSERT INTO `sys_admin` VALUES (5, 'test02', 'e6427bb3d11b7f2c94cfa0666d5fe941', '角色管理', NULL, '3', 1, 'eyJhbGciOiJIUzUxMiJ9.eyJjcmVhdGVkIjoxNjE5MTA5MjY2MTU0LCJleHAiOjE2MTk3MTQwNjYsInVzZXJuYW1lIjoidGVzdDAyIn0.yG8odP71W-cA6d8cEDlZrOMj3ryWqLCsnHUtNCZH0qafZfFLWBPhOK-tI4ZhYNJO2AxF8eQlASzCprW0J7jiUA', '2021-04-23 00:34:26', '2021-04-22 20:41:31', '2021-04-22 20:41:31');
INSERT INTO `sys_admin` VALUES (6, 'test03', 'e6427bb3d11b7f2c94cfa0666d5fe941', '资源管理', NULL, '4', 1, 'eyJhbGciOiJIUzUxMiJ9.eyJjcmVhdGVkIjoxNjE5MTA1MTI1NTk3LCJleHAiOjE2MTk3MDk5MjUsInVzZXJuYW1lIjoidGVzdDAzIn0.A6UiWzxx6r1E-buhzKrTdXDqY73666JM-JpJ9EXl6Qd5luYFhEdQ-k1rLSKaz1N-zLndZp7epFeD0eC9oMmcnA', '2021-04-22 23:25:26', '2021-04-22 23:24:34', '2021-04-22 23:24:51');
INSERT INTO `sys_admin` VALUES (7, 'test04', 'e6427bb3d11b7f2c94cfa0666d5fe941', '菜单管理', NULL, '5', 1, 'eyJhbGciOiJIUzUxMiJ9.eyJjcmVhdGVkIjoxNjE5MTA3NjQ2MTgwLCJleHAiOjE2MTk3MTI0NDYsInVzZXJuYW1lIjoidGVzdDA0In0.JxqOjPJj1fpVz_OAytK4q9QPzyTCcrP-vfSB7dkafhKbweedCV_jY0TkI_8D9IOzZzyGqGxW9DdiYwDJ-ul8CQ', '2021-04-23 00:07:26', '2021-04-22 23:30:22', '2021-04-22 23:30:22');

-- ----------------------------
-- Table structure for sys_menu
-- ----------------------------
DROP TABLE IF EXISTS `sys_menu`;
CREATE TABLE `sys_menu`  (
  `id` int(11) UNSIGNED NOT NULL AUTO_INCREMENT,
  `pid` int(11) NULL DEFAULT NULL,
  `title` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '菜单名称',
  `path` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '菜单路径',
  `hidden` tinyint(1) NULL DEFAULT NULL COMMENT '是否隐藏',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = MyISAM AUTO_INCREMENT = 6 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '菜单表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_menu
-- ----------------------------
INSERT INTO `sys_menu` VALUES (1, NULL, '权限管理', '/authorization', 0, '2021-04-22 16:34:23', '2021-04-22 16:34:23');
INSERT INTO `sys_menu` VALUES (2, 1, '管理员', '/authorization/admin', 0, '2021-04-22 16:34:43', '2021-04-22 16:34:43');
INSERT INTO `sys_menu` VALUES (3, 1, '角色', '/authorization/role', 0, '2021-04-22 16:34:59', '2021-04-22 16:34:59');
INSERT INTO `sys_menu` VALUES (4, 1, '资源', '/authorization/permission', 0, '2021-04-22 16:35:35', '2021-04-22 16:35:35');
INSERT INTO `sys_menu` VALUES (5, 1, '菜单', '/authorization/menu', 0, '2021-04-22 16:35:56', '2021-04-22 16:35:56');

-- ----------------------------
-- Table structure for sys_permission
-- ----------------------------
DROP TABLE IF EXISTS `sys_permission`;
CREATE TABLE `sys_permission`  (
  `id` int(11) UNSIGNED NOT NULL AUTO_INCREMENT,
  `pid` int(11) NULL DEFAULT NULL COMMENT '父级许可',
  `name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '许可名称',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 22 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '许可表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_permission
-- ----------------------------
INSERT INTO `sys_permission` VALUES (1, NULL, '用户管理', '2020-05-21 12:54:33', '2020-05-21 12:54:35');
INSERT INTO `sys_permission` VALUES (2, 1, '添加', '2020-05-21 12:54:38', '2020-05-21 12:54:40');
INSERT INTO `sys_permission` VALUES (3, 1, '删除', '2020-05-21 12:54:44', '2020-05-21 12:54:47');
INSERT INTO `sys_permission` VALUES (4, 1, '编辑', '2020-05-21 12:54:51', '2020-05-21 12:54:53');
INSERT INTO `sys_permission` VALUES (5, 1, '查看', '2020-06-10 16:46:14', '2020-06-10 16:46:16');
INSERT INTO `sys_permission` VALUES (6, NULL, '角色管理', '2020-05-30 11:28:08', '2020-05-30 11:28:12');
INSERT INTO `sys_permission` VALUES (7, 6, '添加', '2020-05-30 11:28:25', '2020-05-30 11:28:28');
INSERT INTO `sys_permission` VALUES (8, 6, '删除', '2020-05-30 11:28:38', '2020-05-30 11:28:41');
INSERT INTO `sys_permission` VALUES (9, 6, '编辑', '2020-05-30 11:28:51', '2020-05-30 11:28:54');
INSERT INTO `sys_permission` VALUES (10, 6, '查看', '2020-06-10 16:46:35', '2020-06-10 16:46:37');
INSERT INTO `sys_permission` VALUES (11, NULL, '资源管理', '2021-04-22 21:27:19', '2021-04-22 21:27:22');
INSERT INTO `sys_permission` VALUES (12, 11, '添加', '2021-04-22 21:27:37', '2021-04-22 21:27:39');
INSERT INTO `sys_permission` VALUES (13, 11, '删除', '2021-04-22 21:27:51', '2021-04-22 21:27:53');
INSERT INTO `sys_permission` VALUES (14, 11, '编辑', '2021-04-22 21:27:37', '2021-04-22 21:27:39');
INSERT INTO `sys_permission` VALUES (15, 11, '查看', '2021-04-22 21:27:37', '2021-04-22 21:27:39');
INSERT INTO `sys_permission` VALUES (16, NULL, '菜单管理', '2021-04-22 23:20:46', '2021-04-22 23:20:48');
INSERT INTO `sys_permission` VALUES (17, 16, '添加', '2021-04-22 23:21:24', '2021-04-22 23:21:26');
INSERT INTO `sys_permission` VALUES (18, 16, '删除', '2021-04-22 23:21:29', '2021-04-22 23:21:32');
INSERT INTO `sys_permission` VALUES (19, 16, '编辑', '2021-04-22 23:21:37', '2021-04-22 23:21:35');
INSERT INTO `sys_permission` VALUES (20, 16, '查看', '2021-04-22 23:21:39', '2021-04-22 23:21:42');

-- ----------------------------
-- Table structure for sys_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_role`;
CREATE TABLE `sys_role`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '角色名',
  `count` int(11) NULL DEFAULT NULL COMMENT '角色数量',
  `menus` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '菜单ID',
  `permissions` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '许可ID',
  `status` tinyint(1) NULL DEFAULT NULL COMMENT '状态（0：禁用，1：启用）',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 6 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '角色表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_role
-- ----------------------------
INSERT INTO `sys_role` VALUES (1, '超级管理员', 1, '*', '*', 1, '2020-05-17 15:45:42', '2020-05-21 14:02:03');
INSERT INTO `sys_role` VALUES (2, '用户管理', 1, '2', '2,3,4,5,10', 1, '2020-06-08 16:29:35', '2021-04-22 23:19:33');
INSERT INTO `sys_role` VALUES (3, '角色管理', 1, '1,3', '7,8,9,10,15,20', 1, '2020-06-10 16:08:33', '2021-04-22 23:22:47');
INSERT INTO `sys_role` VALUES (4, '资源管理', 1, '4', '12,13,14,15', 1, '2021-04-22 23:23:52', NULL);
INSERT INTO `sys_role` VALUES (5, '菜单管理', 1, '5', '17,18,19,20', 1, '2021-04-22 23:29:46', NULL);

SET FOREIGN_KEY_CHECKS = 1;
