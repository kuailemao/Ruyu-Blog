/*
 Navicat Premium Data Transfer

 Source Server         : Ruyu-Blog
 Source Server Type    : MySQL

 Target Server Type    : MySQL
 Target Server Version : 80038 (8.0.38)
 File Encoding         : 65001

 Date: 21/10/2024 19:10:41
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for QRTZ_BLOB_TRIGGERS
-- ----------------------------
DROP TABLE IF EXISTS `QRTZ_BLOB_TRIGGERS`;
CREATE TABLE `QRTZ_BLOB_TRIGGERS`  (
  `SCHED_NAME` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `TRIGGER_NAME` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `TRIGGER_GROUP` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `BLOB_DATA` blob NULL,
  PRIMARY KEY (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`) USING BTREE,
  CONSTRAINT `QRTZ_BLOB_TRIGGERS_ibfk_1` FOREIGN KEY (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`) REFERENCES `QRTZ_TRIGGERS` (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of QRTZ_BLOB_TRIGGERS
-- ----------------------------

-- ----------------------------
-- Table structure for QRTZ_CALENDARS
-- ----------------------------
DROP TABLE IF EXISTS `QRTZ_CALENDARS`;
CREATE TABLE `QRTZ_CALENDARS`  (
  `SCHED_NAME` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `CALENDAR_NAME` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `CALENDAR` blob NOT NULL,
  PRIMARY KEY (`SCHED_NAME`, `CALENDAR_NAME`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of QRTZ_CALENDARS
-- ----------------------------

-- ----------------------------
-- Table structure for QRTZ_CRON_TRIGGERS
-- ----------------------------
DROP TABLE IF EXISTS `QRTZ_CRON_TRIGGERS`;
CREATE TABLE `QRTZ_CRON_TRIGGERS`  (
  `SCHED_NAME` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `TRIGGER_NAME` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `TRIGGER_GROUP` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `CRON_EXPRESSION` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `TIME_ZONE_ID` varchar(80) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  PRIMARY KEY (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`) USING BTREE,
  CONSTRAINT `QRTZ_CRON_TRIGGERS_ibfk_1` FOREIGN KEY (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`) REFERENCES `QRTZ_TRIGGERS` (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of QRTZ_CRON_TRIGGERS
-- ----------------------------

-- ----------------------------
-- Table structure for QRTZ_FIRED_TRIGGERS
-- ----------------------------
DROP TABLE IF EXISTS `QRTZ_FIRED_TRIGGERS`;
CREATE TABLE `QRTZ_FIRED_TRIGGERS`  (
  `SCHED_NAME` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `ENTRY_ID` varchar(95) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `TRIGGER_NAME` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `TRIGGER_GROUP` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `INSTANCE_NAME` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `FIRED_TIME` bigint NOT NULL,
  `SCHED_TIME` bigint NOT NULL,
  `PRIORITY` int NOT NULL,
  `STATE` varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `JOB_NAME` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `JOB_GROUP` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `IS_NONCONCURRENT` varchar(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `REQUESTS_RECOVERY` varchar(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  PRIMARY KEY (`SCHED_NAME`, `ENTRY_ID`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of QRTZ_FIRED_TRIGGERS
-- ----------------------------

-- ----------------------------
-- Table structure for QRTZ_JOB_DETAILS
-- ----------------------------
DROP TABLE IF EXISTS `QRTZ_JOB_DETAILS`;
CREATE TABLE `QRTZ_JOB_DETAILS`  (
  `SCHED_NAME` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `JOB_NAME` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `JOB_GROUP` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `DESCRIPTION` varchar(250) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `JOB_CLASS_NAME` varchar(250) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `IS_DURABLE` varchar(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `IS_NONCONCURRENT` varchar(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `IS_UPDATE_DATA` varchar(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `REQUESTS_RECOVERY` varchar(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `JOB_DATA` blob NULL,
  PRIMARY KEY (`SCHED_NAME`, `JOB_NAME`, `JOB_GROUP`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of QRTZ_JOB_DETAILS
-- ----------------------------
INSERT INTO `QRTZ_JOB_DETAILS` VALUES ('quartzScheduler', 'refreshTheCache', 'DEFAULT', '任务描述：用于每五分钟刷新一次常用数据缓存', 'xyz.kuailemao.quartz.RefreshTheCache', '1', '0', '0', '0', 0xACED0005737200156F72672E71756172747A2E4A6F62446174614D61709FB083E8BFA9B0CB020000787200266F72672E71756172747A2E7574696C732E537472696E674B65794469727479466C61674D61708208E8C3FBC55D280200015A0013616C6C6F77735472616E7369656E74446174617872001D6F72672E71756172747A2E7574696C732E4469727479466C61674D617013E62EAD28760ACE0200025A000564697274794C00036D617074000F4C6A6176612F7574696C2F4D61703B787000737200116A6176612E7574696C2E486173684D61700507DAC1C31660D103000246000A6C6F6164466163746F724900097468726573686F6C6478703F40000000000010770800000010000000007800);

-- ----------------------------
-- Table structure for QRTZ_LOCKS
-- ----------------------------
DROP TABLE IF EXISTS `QRTZ_LOCKS`;
CREATE TABLE `QRTZ_LOCKS`  (
  `SCHED_NAME` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `LOCK_NAME` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  PRIMARY KEY (`SCHED_NAME`, `LOCK_NAME`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of QRTZ_LOCKS
-- ----------------------------

-- ----------------------------
-- Table structure for QRTZ_PAUSED_TRIGGER_GRPS
-- ----------------------------
DROP TABLE IF EXISTS `QRTZ_PAUSED_TRIGGER_GRPS`;
CREATE TABLE `QRTZ_PAUSED_TRIGGER_GRPS`  (
  `SCHED_NAME` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `TRIGGER_GROUP` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  PRIMARY KEY (`SCHED_NAME`, `TRIGGER_GROUP`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of QRTZ_PAUSED_TRIGGER_GRPS
-- ----------------------------

-- ----------------------------
-- Table structure for QRTZ_SCHEDULER_STATE
-- ----------------------------
DROP TABLE IF EXISTS `QRTZ_SCHEDULER_STATE`;
CREATE TABLE `QRTZ_SCHEDULER_STATE`  (
  `SCHED_NAME` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `INSTANCE_NAME` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `LAST_CHECKIN_TIME` bigint NOT NULL,
  `CHECKIN_INTERVAL` bigint NOT NULL,
  PRIMARY KEY (`SCHED_NAME`, `INSTANCE_NAME`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of QRTZ_SCHEDULER_STATE
-- ----------------------------

-- ----------------------------
-- Table structure for QRTZ_SIMPLE_TRIGGERS
-- ----------------------------
DROP TABLE IF EXISTS `QRTZ_SIMPLE_TRIGGERS`;
CREATE TABLE `QRTZ_SIMPLE_TRIGGERS`  (
  `SCHED_NAME` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `TRIGGER_NAME` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `TRIGGER_GROUP` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `REPEAT_COUNT` bigint NOT NULL,
  `REPEAT_INTERVAL` bigint NOT NULL,
  `TIMES_TRIGGERED` bigint NOT NULL,
  PRIMARY KEY (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`) USING BTREE,
  CONSTRAINT `QRTZ_SIMPLE_TRIGGERS_ibfk_1` FOREIGN KEY (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`) REFERENCES `QRTZ_TRIGGERS` (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of QRTZ_SIMPLE_TRIGGERS
-- ----------------------------
INSERT INTO `QRTZ_SIMPLE_TRIGGERS` VALUES ('quartzScheduler', '6da64b5bd2ee-161d3704-6bee-45a1-8732-ed05422f5c61', 'DEFAULT', -1, 300000, 84411);

-- ----------------------------
-- Table structure for QRTZ_SIMPROP_TRIGGERS
-- ----------------------------
DROP TABLE IF EXISTS `QRTZ_SIMPROP_TRIGGERS`;
CREATE TABLE `QRTZ_SIMPROP_TRIGGERS`  (
  `SCHED_NAME` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `TRIGGER_NAME` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `TRIGGER_GROUP` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `STR_PROP_1` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `STR_PROP_2` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `STR_PROP_3` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `INT_PROP_1` int NULL DEFAULT NULL,
  `INT_PROP_2` int NULL DEFAULT NULL,
  `LONG_PROP_1` bigint NULL DEFAULT NULL,
  `LONG_PROP_2` bigint NULL DEFAULT NULL,
  `DEC_PROP_1` decimal(13, 4) NULL DEFAULT NULL,
  `DEC_PROP_2` decimal(13, 4) NULL DEFAULT NULL,
  `BOOL_PROP_1` varchar(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `BOOL_PROP_2` varchar(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  PRIMARY KEY (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`) USING BTREE,
  CONSTRAINT `QRTZ_SIMPROP_TRIGGERS_ibfk_1` FOREIGN KEY (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`) REFERENCES `QRTZ_TRIGGERS` (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of QRTZ_SIMPROP_TRIGGERS
-- ----------------------------

-- ----------------------------
-- Table structure for QRTZ_TRIGGERS
-- ----------------------------
DROP TABLE IF EXISTS `QRTZ_TRIGGERS`;
CREATE TABLE `QRTZ_TRIGGERS`  (
  `SCHED_NAME` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `TRIGGER_NAME` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `TRIGGER_GROUP` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `JOB_NAME` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `JOB_GROUP` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `DESCRIPTION` varchar(250) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `NEXT_FIRE_TIME` bigint NULL DEFAULT NULL,
  `PREV_FIRE_TIME` bigint NULL DEFAULT NULL,
  `PRIORITY` int NULL DEFAULT NULL,
  `TRIGGER_STATE` varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `TRIGGER_TYPE` varchar(8) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `START_TIME` bigint NOT NULL,
  `END_TIME` bigint NULL DEFAULT NULL,
  `CALENDAR_NAME` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `MISFIRE_INSTR` smallint NULL DEFAULT NULL,
  `JOB_DATA` blob NULL,
  PRIMARY KEY (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`) USING BTREE,
  INDEX `SCHED_NAME`(`SCHED_NAME` ASC, `JOB_NAME` ASC, `JOB_GROUP` ASC) USING BTREE,
  CONSTRAINT `QRTZ_TRIGGERS_ibfk_1` FOREIGN KEY (`SCHED_NAME`, `JOB_NAME`, `JOB_GROUP`) REFERENCES `QRTZ_JOB_DETAILS` (`SCHED_NAME`, `JOB_NAME`, `JOB_GROUP`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of QRTZ_TRIGGERS
-- ----------------------------
INSERT INTO `QRTZ_TRIGGERS` VALUES ('quartzScheduler', '6da64b5bd2ee-161d3704-6bee-45a1-8732-ed05422f5c61', 'DEFAULT', 'refreshTheCache', 'DEFAULT', NULL, 1729509274460, 1729508974460, 5, 'WAITING', 'SIMPLE', 1704198574460, 0, NULL, 0, '');

-- ----------------------------
-- Table structure for sys_log
-- ----------------------------
DROP TABLE IF EXISTS `sys_log`;
CREATE TABLE `sys_log`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '编号',
  `module` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '模块名称',
  `operation` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '操作',
  `user_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '操作人员',
  `ip` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT 'ip地址',
  `address` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '操作地点',
  `state` tinyint(1) NOT NULL COMMENT '操作状态(0：成功，1：失败，2：异常)',
  `method` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '操作方法',
  `req_parameter` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '请求参数',
  `req_mapping` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '请求方式',
  `exception` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '异常信息',
  `return_parameter` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '返回参数',
  `req_address` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '请求地址',
  `time` bigint NOT NULL COMMENT '消耗时间(ms)',
  `description` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '接口描述',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime NOT NULL COMMENT '更新时间',
  `is_deleted` tinyint(1) NOT NULL DEFAULT 0 COMMENT '是否删除（0：未删除，1：已删除）',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 11545 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = DYNAMIC;


-- ----------------------------
-- Table structure for sys_login_log
-- ----------------------------
DROP TABLE IF EXISTS `sys_login_log`;
CREATE TABLE `sys_login_log`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '日志编号',
  `user_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '用户名称',
  `ip` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '登录ip',
  `address` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '登录地址',
  `browser` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '浏览器',
  `os` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '操作系统',
  `type` tinyint(1) NOT NULL COMMENT '登录类型(0：前台，1：后台，2：非法登录)',
  `state` tinyint(1) NOT NULL COMMENT '登录状态(0：成功，1：失败)',
  `message` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '登录信息',
  `create_time` datetime NOT NULL COMMENT '用户创建时间',
  `update_time` datetime NOT NULL COMMENT '用户更新时间',
  `is_deleted` tinyint(1) NOT NULL DEFAULT 0 COMMENT '是否删除（0：未删除，1：已删除）',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2275 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = DYNAMIC;


-- ----------------------------
-- Table structure for sys_menu
-- ----------------------------
DROP TABLE IF EXISTS `sys_menu`;
CREATE TABLE `sys_menu`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '唯一id',
  `title` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '标题',
  `icon` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '图标',
  `path` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '地址',
  `component` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '绑定的哪个组件，默认自带的组件类型分别是：Iframe、RouteView和ComponentError',
  `redirect` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '父菜单重定向地址(默认第一个子菜单)',
  `affix` tinyint NOT NULL DEFAULT 0 COMMENT '是否是固定页签(0否 1是)',
  `parent_id` bigint NULL DEFAULT NULL COMMENT '父级菜单的id',
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '同路由中的name，主要是用于保活的左右',
  `hide_in_menu` tinyint NOT NULL DEFAULT 0 COMMENT '是否隐藏当前菜单(0否 1是)',
  `url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '如果当前是iframe的模式，需要有一个跳转的url支撑，其不能和path重复，path还是为路由',
  `hide_in_breadcrumb` tinyint NOT NULL DEFAULT 1 COMMENT '是否存在于面包屑(0否 1是)',
  `hide_children_in_menu` tinyint NOT NULL DEFAULT 1 COMMENT '是否不需要显示所有的子菜单(0否 1是)',
  `keep_alive` tinyint NOT NULL DEFAULT 1 COMMENT '是否保活(0否 1是)',
  `target` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '全连接跳转模式(\'_blank\' | \'_self\' | \'_parent\')',
  `is_disable` tinyint(1) NOT NULL DEFAULT 0 COMMENT '是否禁用 (0否 1是)',
  `order_num` int NOT NULL DEFAULT 1 COMMENT '排序',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime NOT NULL COMMENT '更新时间',
  `is_deleted` tinyint NOT NULL DEFAULT 0 COMMENT '是否删除（0：未删除，1：已删除）',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 72 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_menu
-- ----------------------------
INSERT INTO `sys_menu` VALUES (1, '系统管理', 'SettingTwoTone', '/system', 'RouteView', '/system/menu', 0, NULL, 'System', 0, NULL, 1, 0, 1, NULL, 0, 2, '2023-11-17 14:49:02', '2023-11-29 17:33:13', 0);
INSERT INTO `sys_menu` VALUES (2, '菜单管理', 'MenuOutlined', '/system/menu', '/system/menu', '', 0, 1, 'Menu', 0, NULL, 1, 0, 1, NULL, 0, 1, '2023-11-17 14:49:02', '2023-11-28 17:27:43', 0);
INSERT INTO `sys_menu` VALUES (3, '用户管理', 'UserOutlined', '/system/user', '/system/user', '', 0, 1, 'User', 0, NULL, 1, 0, 1, NULL, 0, 0, '2023-11-17 14:49:02', '2023-11-29 14:46:27', 0);
INSERT INTO `sys_menu` VALUES (21, '首页', 'HomeTwoTone', '/welcome', '/welcome', NULL, 0, NULL, NULL, 0, NULL, 1, 1, 1, '', 0, 0, '2023-11-28 16:36:33', '2024-07-13 21:56:58', 0);
INSERT INTO `sys_menu` VALUES (23, '角色管理', 'TeamOutlined', '/system/role', '/system/role', NULL, 0, 1, NULL, 0, NULL, 1, 1, 1, '', 0, 3, '2023-11-29 15:41:30', '2023-12-04 12:16:00', 0);
INSERT INTO `sys_menu` VALUES (24, '权限管理', 'UnlockOutlined', '/system/permission', '/system/permission', '', 0, 1, NULL, 0, NULL, 1, 1, 1, '', 0, 4, '2023-11-29 17:13:50', '2023-11-29 17:14:10', 0);
INSERT INTO `sys_menu` VALUES (25, '日志管理', 'HighlightOutlined', '/log', 'RouteView', '/log/operate', 0, 1, NULL, 0, '', 1, 1, 1, '', 0, 5, '2023-11-29 17:17:29', '2023-11-29 17:17:29', 0);
INSERT INTO `sys_menu` VALUES (26, '操作日志', 'FileProtectOutlined', '/log/operate', '/system/log/operate', '', 0, 25, NULL, 0, NULL, 1, 1, 1, '', 0, 0, '2023-11-29 17:20:28', '2023-11-29 17:29:20', 0);
INSERT INTO `sys_menu` VALUES (27, '登录日志', 'SolutionOutlined', '/log/login', '/system/log/login', '', 0, 25, NULL, 0, '', 1, 1, 1, '', 0, 1, '2023-11-29 17:29:02', '2023-11-29 17:29:02', 0);
INSERT INTO `sys_menu` VALUES (28, '网站管理', 'AppstoreTwoTone', '/blog', 'RouteView', NULL, 0, NULL, NULL, 0, NULL, 1, 1, 1, '', 0, 1, '2023-11-29 17:34:17', '2024-07-30 03:28:43', 0);
INSERT INTO `sys_menu` VALUES (29, '信息管理', 'ReadOutlined', '/blog/info', '/blog/info', '', 0, 28, NULL, 0, NULL, 1, 1, 1, '', 0, 0, '2023-11-29 20:05:20', '2023-11-29 20:09:38', 0);
INSERT INTO `sys_menu` VALUES (30, '文章管理', 'FormOutlined', '/blog/essay', '', NULL, 0, 28, NULL, 0, NULL, 1, 1, 1, '', 0, 0, '2023-11-29 20:11:25', '2023-12-26 11:16:20', 0);
INSERT INTO `sys_menu` VALUES (31, '发布文章', 'SendOutlined', '/blog/essay/publish', '/blog/essay/publish', '', 0, 30, NULL, 0, '', 1, 1, 1, '', 0, 0, '2023-11-29 20:13:00', '2023-11-29 20:13:00', 0);
INSERT INTO `sys_menu` VALUES (32, '文章列表', 'OrderedListOutlined', '/blog/essay/list', '/blog/essay/list', '', 0, 30, NULL, 0, '', 1, 1, 1, '', 0, 0, '2023-11-29 20:14:13', '2023-11-29 20:14:13', 0);
INSERT INTO `sys_menu` VALUES (33, '标签管理', 'TagsOutlined', '/blog/tag', '/blog/tag', '', 0, 28, NULL, 0, NULL, 1, 1, 1, '', 0, 2, '2023-11-29 20:15:13', '2023-11-29 20:20:28', 0);
INSERT INTO `sys_menu` VALUES (34, '分类管理', 'ContainerOutlined', '/blog/category', '/blog/category', '', 0, 28, NULL, 0, '', 1, 1, 1, '', 0, 3, '2023-11-29 20:19:09', '2023-11-29 20:19:09', 0);
INSERT INTO `sys_menu` VALUES (35, '评论管理', 'CommentOutlined', '/blog/comment', '/blog/comment', '', 0, 28, NULL, 0, NULL, 1, 1, 1, '', 0, 4, '2023-11-29 20:21:48', '2023-11-29 20:22:06', 0);
INSERT INTO `sys_menu` VALUES (36, '留言管理', 'ScheduleOutlined', '/blog/message', '/blog/message', NULL, 0, 28, NULL, 0, NULL, 1, 1, 1, '', 0, 1, '2023-11-29 20:23:19', '2023-12-26 11:16:24', 0);
INSERT INTO `sys_menu` VALUES (37, '树洞管理', 'BulbOutlined', '/blog/tree-hole', '/blog/tree-hole', '', 0, 28, NULL, 0, '', 1, 1, 1, '', 0, 5, '2023-11-29 20:27:40', '2023-11-29 20:27:40', 0);
INSERT INTO `sys_menu` VALUES (39, '友链管理', 'NodeIndexOutlined', '/blog/link', '/blog/link', NULL, 0, 28, NULL, 0, NULL, 1, 1, 1, '', 0, 6, '2023-11-29 20:31:25', '2024-01-22 20:27:26', 0);
INSERT INTO `sys_menu` VALUES (42, '数据大屏', 'PieChartTwoTone', '/data-screen', '/data-screen', NULL, 0, NULL, NULL, 1, NULL, 1, 1, 1, '', 0, 4, '2023-11-29 20:51:14', '2024-01-22 22:07:04', 0);
INSERT INTO `sys_menu` VALUES (43, '收藏管理', 'InboxOutlined', '/blog/collect', '/blog/collect', NULL, 0, 28, NULL, 0, NULL, 1, 1, 1, '', 0, 3, '2023-11-29 20:54:15', '2023-11-29 20:54:47', 0);
INSERT INTO `sys_menu` VALUES (44, '服务监控', 'AlertOutlined', '/system/server-monitoring', '/system/server-monitoring', NULL, 0, 1, NULL, 0, NULL, 1, 1, 1, '', 0, 6, '2023-11-29 21:01:24', '2023-12-14 15:26:34', 0);
INSERT INTO `sys_menu` VALUES (64, '角色授权', '', '/role/authorization', '/system/role/user-role', NULL, 0, 1, NULL, 1, NULL, 1, 1, 1, '', 0, 0, '2023-12-04 12:07:00', '2023-12-05 09:57:09', 0);
INSERT INTO `sys_menu` VALUES (65, '权限授权', '', '/permission/authorization', '/system/permission/role-permission', NULL, 0, 1, NULL, 1, NULL, 1, 1, 1, '', 0, 0, '2023-12-07 14:38:45', '2024-03-01 11:17:44', 0);
INSERT INTO `sys_menu` VALUES (68, '用户授权', '', '/user/role', '/system/user/role-user', NULL, 0, 1, NULL, 1, NULL, 1, 1, 1, '', 0, 0, '2023-12-19 10:37:05', '2024-03-01 11:17:01', 0);
INSERT INTO `sys_menu` VALUES (69, '接口文档', 'FileTextTwoTone', 'http://kuailemao.xyz:8088/doc.html#/home', NULL, NULL, 0, NULL, NULL, 0, 'http://127.0.0.1:8088/doc.html#/home', 1, 1, 1, '_blank', 0, 5, '2024-01-22 20:32:18', '2024-04-29 11:57:32', 0);
INSERT INTO `sys_menu` VALUES (70, '跳转前台', 'TabletTwoTone', 'http://kuailemao.xyz', NULL, NULL, 0, NULL, NULL, 0, '', 1, 1, 1, '_blank', 0, 6, '2024-01-22 20:38:54', '2024-04-29 11:55:51', 0);
INSERT INTO `sys_menu` VALUES (71, '黑名单管理', 'AuditOutlined', '/blog/blackList', '/blog/black-list', '', 0, 28, NULL, 0, '', 1, 1, 1, '', 0, 7, '2024-10-11 17:40:15', '2024-10-11 17:40:15', 0);

-- ----------------------------
-- Table structure for sys_permission
-- ----------------------------
DROP TABLE IF EXISTS `sys_permission`;
CREATE TABLE `sys_permission`  (
  `id` bigint UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '权限表id',
  `permission_desc` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '描述',
  `permission_key` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '权限字符',
  `menu_id` bigint NOT NULL COMMENT '菜单id',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime NOT NULL COMMENT '更新时间',
  `is_deleted` tinyint NOT NULL DEFAULT 0 COMMENT '是否删除（0：未删除，1：已删除）',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 159 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_permission
-- ----------------------------
INSERT INTO `sys_permission` VALUES (5, '获取菜单', 'system:menu:list', 2, '2023-12-06 08:41:49', '2023-12-14 17:21:57', 0);
INSERT INTO `sys_permission` VALUES (6, '查询菜单', 'system:menu:select', 2, '2023-12-05 08:41:54', '2023-12-07 12:02:31', 0);
INSERT INTO `sys_permission` VALUES (7, '修改菜单', 'system:menu:update', 2, '2023-12-04 08:41:54', '2023-12-12 20:36:49', 0);
INSERT INTO `sys_permission` VALUES (8, '删除菜单', 'system:menu:delete', 2, '2023-12-04 08:41:54', '2023-12-11 21:03:10', 0);
INSERT INTO `sys_permission` VALUES (9, '添加菜单', 'system:menu:add', 2, '2023-12-02 08:41:54', '2023-12-04 08:41:54', 0);
INSERT INTO `sys_permission` VALUES (10, '修改菜单角色列表', 'system:menu:role:list', 2, '2023-12-02 08:41:54', '2023-12-02 08:41:54', 0);
INSERT INTO `sys_permission` VALUES (11, '获取所有角色', 'system:role:list', 23, '2023-12-02 08:41:54', '2023-12-02 08:41:54', 0);
INSERT INTO `sys_permission` VALUES (12, '更新角色状态', 'system:role:status:update', 23, '2023-12-02 08:41:54', '2023-12-02 08:41:54', 0);
INSERT INTO `sys_permission` VALUES (13, '获取对应角色信息', 'system:role:get', 23, '2023-12-02 08:41:54', '2023-12-02 08:41:54', 0);
INSERT INTO `sys_permission` VALUES (14, '修改角色信息', 'system:role:update', 23, '2023-12-02 08:41:54', '2023-12-02 08:41:54', 0);
INSERT INTO `sys_permission` VALUES (15, '根据id删除角色', 'system:role:delete', 23, '2023-12-02 08:41:54', '2023-12-02 08:41:54', 0);
INSERT INTO `sys_permission` VALUES (16, '根据条件搜索角色', 'system:role:search', 23, '2023-12-02 08:41:54', '2023-12-02 08:41:54', 0);
INSERT INTO `sys_permission` VALUES (17, '查询拥有角色的用户列表', 'system:user:role:list', 23, '2023-12-02 08:41:54', '2023-12-02 08:41:54', 0);
INSERT INTO `sys_permission` VALUES (18, '查询未拥有角色的用户列表', 'system:not:role:user:list', 23, '2023-12-02 08:41:54', '2023-12-06 10:43:49', 0);
INSERT INTO `sys_permission` VALUES (19, '添加用户角色关系', 'system:user:role:add', 23, '2023-12-05 08:41:54', '2023-12-11 21:02:51', 0);
INSERT INTO `sys_permission` VALUES (20, '删除用户角色关系', 'system:user:role:delete', 23, '2023-12-02 08:41:54', '2023-12-02 08:41:54', 0);
INSERT INTO `sys_permission` VALUES (21, '所有权限', 'system:permission:list', 24, '2023-12-06 10:34:21', '2023-12-06 10:34:24', 0);
INSERT INTO `sys_permission` VALUES (22, '查询所有权限所在菜单', 'system:permission:menu:list', 24, '2023-12-06 14:26:28', '2023-12-06 14:26:31', 0);
INSERT INTO `sys_permission` VALUES (23, '搜索权限', 'system:permission:search', 24, '2023-12-06 15:18:29', '2023-12-06 15:18:33', 0);
INSERT INTO `sys_permission` VALUES (24, '添加权限', 'system:permission:add', 24, '2023-12-06 19:12:47', '2023-12-06 19:12:50', 0);
INSERT INTO `sys_permission` VALUES (27, '获取要修改的权限信息', 'system:permission:get', 24, '2023-12-06 20:48:33', '2023-12-06 20:48:33', 0);
INSERT INTO `sys_permission` VALUES (28, '修改权限字符信息', 'system:permission:update', 24, '2023-12-07 12:01:34', '2023-12-07 12:01:36', 0);
INSERT INTO `sys_permission` VALUES (30, '删除权限', 'system:permission:delete', 24, '2023-12-07 12:14:14', '2023-12-07 12:14:14', 0);
INSERT INTO `sys_permission` VALUES (31, '查询权限的角色列表', 'system:permission:role:list', 65, '2023-12-07 15:02:03', '2023-12-07 15:02:03', 0);
INSERT INTO `sys_permission` VALUES (33, '查询没有该权限的角色列表', 'system:permission:role:not:list', 65, '2023-12-07 17:41:38', '2023-12-07 17:41:38', 0);
INSERT INTO `sys_permission` VALUES (34, '单个/批量添加角色权限关系', 'system:permission:role:add', 65, '2023-12-07 20:53:14', '2023-12-08 10:51:10', 0);
INSERT INTO `sys_permission` VALUES (35, '删除角色权限关系', 'system:permission:role:delete', 65, '2023-12-07 21:00:55', '2023-12-07 21:00:55', 0);
INSERT INTO `sys_permission` VALUES (36, '显示所有登录日志', 'system:log:login:list', 27, '2023-12-11 16:20:00', '2023-12-14 17:48:07', 0);
INSERT INTO `sys_permission` VALUES (37, '登录日志搜索', 'system:log:login:search', 27, '2023-12-11 19:51:27', '2023-12-11 19:51:27', 0);
INSERT INTO `sys_permission` VALUES (38, '删除/清空登录日志', 'system:log:login:delete', 27, '2023-12-11 20:19:08', '2023-12-11 20:19:08', 0);
INSERT INTO `sys_permission` VALUES (45, '显示所有操作日志', 'system:log:list', 26, '2023-12-13 16:13:41', '2023-12-13 16:13:41', 0);
INSERT INTO `sys_permission` VALUES (87, '添加角色信息', 'system:role:add', 23, '2023-12-13 17:23:42', '2023-12-13 17:23:42', 0);
INSERT INTO `sys_permission` VALUES (91, '搜索操作日志', 'system:log:search', 26, '2023-12-13 20:43:04', '2023-12-13 20:43:04', 0);
INSERT INTO `sys_permission` VALUES (92, '删除/清空操作日志', 'system:log:delete', 26, '2023-12-14 08:45:38', '2023-12-14 08:45:38', 0);
INSERT INTO `sys_permission` VALUES (93, 'id查询操作日志', 'system:log:select:id', 26, '2023-12-14 09:00:53', '2023-12-14 09:00:53', 0);
INSERT INTO `sys_permission` VALUES (94, '获取服务监控数据', 'monitor:server:list', 44, '2023-12-14 15:21:21', '2023-12-14 15:21:21', 0);
INSERT INTO `sys_permission` VALUES (97, '获取用户列表', 'system:user:list', 3, '2023-12-18 12:07:00', '2023-12-18 12:07:00', 0);
INSERT INTO `sys_permission` VALUES (98, '搜索用户列表', 'system:user:search', 3, '2023-12-18 14:15:46', '2023-12-18 14:15:46', 0);
INSERT INTO `sys_permission` VALUES (99, '更新用户状态', 'system:user:status:update', 3, '2023-12-18 15:11:34', '2023-12-18 15:11:34', 0);
INSERT INTO `sys_permission` VALUES (100, '获取用户详细信息', 'system:user:details', 3, '2023-12-18 16:40:52', '2023-12-18 16:40:52', 0);
INSERT INTO `sys_permission` VALUES (101, '删除用户&用户角色关系', 'system:user:delete', 3, '2023-12-19 10:11:46', '2023-12-19 10:12:15', 0);
INSERT INTO `sys_permission` VALUES (102, '查询没有该用户的角色列表', 'system:user:role:not:list', 23, '2023-12-19 11:10:11', '2023-12-19 11:10:11', 0);
INSERT INTO `sys_permission` VALUES (103, '查询拥有用户的角色列表', 'system:role:user:list', 23, '2023-12-19 11:17:55', '2023-12-19 11:17:55', 0);
INSERT INTO `sys_permission` VALUES (104, '搜索管理菜单列表', 'system:search:menu:list', 2, '2023-12-25 11:48:02', '2023-12-25 11:48:02', 0);
INSERT INTO `sys_permission` VALUES (105, '添加或修改站长信息', 'blog:update:websiteInfo', 29, '2023-12-27 16:19:23', '2024-01-04 10:49:12', 0);
INSERT INTO `sys_permission` VALUES (106, '查看网站信息-后台', 'blog:get:websiteInfo', 29, '2023-12-29 08:52:51', '2024-01-22 22:18:56', 0);
INSERT INTO `sys_permission` VALUES (107, '新增标签', 'blog:tag:add', 33, '2024-01-04 10:55:39', '2024-01-04 10:55:39', 0);
INSERT INTO `sys_permission` VALUES (108, '新增分类', 'blog:category:add', 34, '2024-01-04 11:17:12', '2024-01-04 11:17:12', 0);
INSERT INTO `sys_permission` VALUES (109, '发布文章相关', 'blog:publish:article', 31, '2024-01-05 15:01:54', '2024-01-05 15:01:54', 0);
INSERT INTO `sys_permission` VALUES (110, '获取文章列表', 'blog:article:list', 32, '2024-01-07 11:28:14', '2024-01-07 11:28:14', 0);
INSERT INTO `sys_permission` VALUES (111, '搜索文章', 'blog:article:search', 32, '2024-01-07 19:30:10', '2024-01-07 19:30:10', 0);
INSERT INTO `sys_permission` VALUES (112, '修改文章', 'blog:article:update', 32, '2024-01-07 19:56:37', '2024-01-07 19:56:37', 0);
INSERT INTO `sys_permission` VALUES (113, '回显文章数据', 'blog:article:echo', 31, '2024-01-08 09:24:01', '2024-01-08 09:24:01', 0);
INSERT INTO `sys_permission` VALUES (114, '删除文章', 'blog:article:delete', 30, '2024-01-08 11:29:23', '2024-01-08 11:29:23', 0);
INSERT INTO `sys_permission` VALUES (115, '后台留言列表', 'blog:leaveword:list', 36, '2024-01-12 21:14:18', '2024-01-12 21:14:18', 0);
INSERT INTO `sys_permission` VALUES (116, '搜索后台留言列表', 'blog:leaveword:search', 36, '2024-01-16 08:50:46', '2024-01-16 08:50:46', 0);
INSERT INTO `sys_permission` VALUES (117, '修改留言是否通过', 'blog:leaveword:isCheck', 36, '2024-01-16 10:02:20', '2024-01-16 10:02:20', 0);
INSERT INTO `sys_permission` VALUES (118, '删除留言', 'blog:leaveword:delete', 36, '2024-01-16 11:11:59', '2024-01-16 11:11:59', 0);
INSERT INTO `sys_permission` VALUES (119, '获取标签列表', 'blog:tag:list', 33, '2024-01-18 14:30:10', '2024-01-18 14:30:10', 0);
INSERT INTO `sys_permission` VALUES (120, '搜索标签列表', 'blog:tag:search', 33, '2024-01-18 14:47:10', '2024-01-18 14:47:10', 0);
INSERT INTO `sys_permission` VALUES (121, '修改标签', 'blog:tag:update', 33, '2024-01-18 15:56:45', '2024-01-18 15:56:45', 0);
INSERT INTO `sys_permission` VALUES (122, '删除标签', 'blog:tag:delete', 33, '2024-01-18 16:16:41', '2024-01-18 16:16:41', 0);
INSERT INTO `sys_permission` VALUES (123, '获取分类列表', 'blog:category:list', 34, '2024-01-18 22:42:29', '2024-01-18 22:43:28', 0);
INSERT INTO `sys_permission` VALUES (124, '搜索分类列表', 'blog:category:search', 34, '2024-01-18 22:43:06', '2024-01-18 22:43:06', 0);
INSERT INTO `sys_permission` VALUES (125, '修改分类', 'blog:category:update', 34, '2024-01-18 22:44:21', '2024-01-18 22:44:21', 0);
INSERT INTO `sys_permission` VALUES (126, '删除分类', 'blog:category:delete', 34, '2024-01-18 22:44:51', '2024-01-18 22:44:51', 0);
INSERT INTO `sys_permission` VALUES (127, '后台树洞列表', 'blog:treeHole:list', 37, '2024-01-19 21:25:39', '2024-01-19 21:25:39', 0);
INSERT INTO `sys_permission` VALUES (128, '搜索后台树洞列表', 'blog:treeHole:search', 37, '2024-01-19 21:26:03', '2024-01-19 21:26:03', 0);
INSERT INTO `sys_permission` VALUES (129, '修改树洞是否通过', 'blog:treeHole:isCheck', 37, '2024-01-19 21:26:28', '2024-01-19 21:26:28', 0);
INSERT INTO `sys_permission` VALUES (130, '删除树洞', 'blog:treeHole:delete', 37, '2024-01-19 21:27:20', '2024-01-19 21:27:20', 0);
INSERT INTO `sys_permission` VALUES (131, '搜索后台收藏列表', 'blog:favorite:search', 43, '2024-01-21 09:36:25', '2024-01-21 09:36:25', 0);
INSERT INTO `sys_permission` VALUES (132, '修改收藏是否通过', 'blog:favorite:isCheck', 43, '2024-01-21 09:36:47', '2024-01-21 09:36:47', 0);
INSERT INTO `sys_permission` VALUES (133, '删除收藏', 'blog:favorite:delete', 43, '2024-01-21 09:37:11', '2024-01-21 09:37:11', 0);
INSERT INTO `sys_permission` VALUES (134, '后台收藏列表', 'blog:favorite:list', 43, '2024-01-21 09:39:35', '2024-01-21 09:39:35', 0);
INSERT INTO `sys_permission` VALUES (136, '后台聊天列表', 'blog:chatGpt:list', 38, '2024-01-21 11:06:39', '2024-01-21 11:06:39', 0);
INSERT INTO `sys_permission` VALUES (137, '搜索后台聊天列表', 'blog:chatGpt:search', 38, '2024-01-21 11:07:18', '2024-01-21 11:07:18', 0);
INSERT INTO `sys_permission` VALUES (138, '修改聊天是否通过', 'blog:chatGpt:isCheck', 38, '2024-01-21 11:07:53', '2024-01-21 11:07:53', 0);
INSERT INTO `sys_permission` VALUES (139, '删除聊天', 'blog:chatGpt:delete', 38, '2024-01-21 11:08:18', '2024-01-21 11:08:18', 0);
INSERT INTO `sys_permission` VALUES (140, '后台评论列表', 'blog:comment:list', 35, '2024-01-22 09:40:40', '2024-01-22 09:40:40', 0);
INSERT INTO `sys_permission` VALUES (141, '搜索后台评论列表', 'blog:comment:search', 35, '2024-01-22 11:02:58', '2024-01-22 11:02:58', 0);
INSERT INTO `sys_permission` VALUES (142, '修改评论是否通过', 'blog:comment:isCheck', 35, '2024-01-22 20:01:50', '2024-01-22 20:01:50', 0);
INSERT INTO `sys_permission` VALUES (143, '删除评论', 'blog:comment:delete', 35, '2024-01-22 20:02:20', '2024-01-22 20:02:20', 0);
INSERT INTO `sys_permission` VALUES (144, '后台友链列表', 'blog:link:list', 39, '2024-01-22 21:03:24', '2024-01-22 21:03:24', 0);
INSERT INTO `sys_permission` VALUES (145, '搜索后台友链列表', 'blog:link:search', 39, '2024-01-22 21:03:46', '2024-01-22 21:03:46', 0);
INSERT INTO `sys_permission` VALUES (146, '修改友链是否通过', 'blog:link:isCheck', 39, '2024-01-22 21:04:18', '2024-01-22 21:04:18', 0);
INSERT INTO `sys_permission` VALUES (147, '删除友链', 'blog:link:delete', 39, '2024-01-22 21:04:46', '2024-01-22 21:04:46', 0);
INSERT INTO `sys_permission` VALUES (150, '后台获取所有前台首页Banner图片', 'blog:banner:list', 29, '2024-09-02 11:44:19', '2024-09-02 11:44:19', 0);
INSERT INTO `sys_permission` VALUES (151, '删除前台首页Banner图片', 'blog:banner:delete', 29, '2024-09-02 11:44:34', '2024-09-02 11:44:34', 0);
INSERT INTO `sys_permission` VALUES (152, '添加前台首页Banner图片', 'blog:banner:add', 29, '2024-09-02 11:44:50', '2024-09-02 11:44:50', 0);
INSERT INTO `sys_permission` VALUES (153, '更新前台首页Banner图片顺序', 'blog:banner:update', 29, '2024-09-02 11:45:07', '2024-09-02 11:45:07', 0);
INSERT INTO `sys_permission` VALUES (154, '添加黑名单', 'blog:black:add', 71, '2024-10-11 17:41:07', '2024-10-11 17:41:07', 0);
INSERT INTO `sys_permission` VALUES (155, '修改黑名单', 'blog:black:update', 71, '2024-10-11 17:41:21', '2024-10-11 17:41:21', 0);
INSERT INTO `sys_permission` VALUES (156, '查询黑名单', 'blog:black:select', 71, '2024-10-11 17:41:35', '2024-10-11 17:41:35', 0);
INSERT INTO `sys_permission` VALUES (157, '删除黑名单', 'blog:black:delete', 71, '2024-10-11 17:41:47', '2024-10-11 17:41:47', 0);

-- ----------------------------
-- Table structure for sys_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_role`;
CREATE TABLE `sys_role`  (
  `id` bigint UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '角色id',
  `role_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '角色名称',
  `role_key` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '角色字符',
  `status` tinyint NOT NULL DEFAULT 0 COMMENT '状态（0：正常，1：停用）',
  `order_num` bigint NOT NULL COMMENT '排序',
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '备注',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime NOT NULL COMMENT '更新时间',
  `is_deleted` tinyint NOT NULL DEFAULT 0 COMMENT '是否删除（0：未删除，1：已删除）',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 45 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_role
-- ----------------------------
INSERT INTO `sys_role` VALUES (1, '超级管理员', 'ADMIN', 0, 0, '最高管理者', '2023-11-17 15:19:01', '2023-12-14 16:47:07', 0);
INSERT INTO `sys_role` VALUES (2, '测试角色', 'Test', 0, 1, '测试的用户，没有任何操作权限', '2023-11-17 15:19:06', '2024-07-31 08:41:49', 0);
INSERT INTO `sys_role` VALUES (3, '普通用户', 'USER', 0, 3, '前台普通用户（前台用户默认角色）', '2023-12-03 21:12:24', '2023-12-14 17:15:52', 0);

-- ----------------------------
-- Table structure for sys_role_menu
-- ----------------------------
DROP TABLE IF EXISTS `sys_role_menu`;
CREATE TABLE `sys_role_menu`  (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '主键',
  `role_id` bigint NOT NULL COMMENT '角色id',
  `menu_id` bigint NOT NULL COMMENT '菜单id',
  `is_deleted` tinyint NOT NULL DEFAULT 0 COMMENT '是否删除（0：未删除，1：已删除）',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1431 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_role_menu
-- ----------------------------
INSERT INTO `sys_role_menu` VALUES (1292, 1, 29, 0);
INSERT INTO `sys_role_menu` VALUES (1295, 1, 31, 0);
INSERT INTO `sys_role_menu` VALUES (1296, 1, 32, 0);
INSERT INTO `sys_role_menu` VALUES (1297, 1, 33, 0);
INSERT INTO `sys_role_menu` VALUES (1298, 1, 34, 0);
INSERT INTO `sys_role_menu` VALUES (1299, 1, 43, 0);
INSERT INTO `sys_role_menu` VALUES (1300, 1, 35, 0);
INSERT INTO `sys_role_menu` VALUES (1301, 1, 37, 0);
INSERT INTO `sys_role_menu` VALUES (1306, 1, 1, 0);
INSERT INTO `sys_role_menu` VALUES (1307, 1, 3, 0);
INSERT INTO `sys_role_menu` VALUES (1308, 1, 64, 0);
INSERT INTO `sys_role_menu` VALUES (1310, 1, 2, 0);
INSERT INTO `sys_role_menu` VALUES (1311, 1, 23, 0);
INSERT INTO `sys_role_menu` VALUES (1312, 1, 24, 0);
INSERT INTO `sys_role_menu` VALUES (1313, 1, 25, 0);
INSERT INTO `sys_role_menu` VALUES (1314, 1, 26, 0);
INSERT INTO `sys_role_menu` VALUES (1315, 1, 27, 0);
INSERT INTO `sys_role_menu` VALUES (1316, 1, 44, 0);
INSERT INTO `sys_role_menu` VALUES (1382, 1, 30, 0);
INSERT INTO `sys_role_menu` VALUES (1384, 1, 36, 0);
INSERT INTO `sys_role_menu` VALUES (1386, 1, 39, 0);
INSERT INTO `sys_role_menu` VALUES (1392, 1, 42, 0);
INSERT INTO `sys_role_menu` VALUES (1396, 1, 68, 0);
INSERT INTO `sys_role_menu` VALUES (1398, 1, 65, 0);
INSERT INTO `sys_role_menu` VALUES (1401, 2, 29, 0);
INSERT INTO `sys_role_menu` VALUES (1402, 2, 31, 0);
INSERT INTO `sys_role_menu` VALUES (1403, 2, 32, 0);
INSERT INTO `sys_role_menu` VALUES (1404, 2, 33, 0);
INSERT INTO `sys_role_menu` VALUES (1405, 2, 34, 0);
INSERT INTO `sys_role_menu` VALUES (1406, 2, 43, 0);
INSERT INTO `sys_role_menu` VALUES (1407, 2, 35, 0);
INSERT INTO `sys_role_menu` VALUES (1408, 2, 37, 0);
INSERT INTO `sys_role_menu` VALUES (1410, 2, 1, 0);
INSERT INTO `sys_role_menu` VALUES (1411, 2, 3, 0);
INSERT INTO `sys_role_menu` VALUES (1412, 2, 64, 0);
INSERT INTO `sys_role_menu` VALUES (1413, 2, 2, 0);
INSERT INTO `sys_role_menu` VALUES (1414, 2, 23, 0);
INSERT INTO `sys_role_menu` VALUES (1415, 2, 24, 0);
INSERT INTO `sys_role_menu` VALUES (1416, 2, 25, 0);
INSERT INTO `sys_role_menu` VALUES (1417, 2, 27, 0);
INSERT INTO `sys_role_menu` VALUES (1418, 2, 44, 0);
INSERT INTO `sys_role_menu` VALUES (1420, 2, 30, 0);
INSERT INTO `sys_role_menu` VALUES (1421, 2, 36, 0);
INSERT INTO `sys_role_menu` VALUES (1422, 2, 39, 0);
INSERT INTO `sys_role_menu` VALUES (1423, 2, 42, 0);
INSERT INTO `sys_role_menu` VALUES (1424, 2, 68, 0);
INSERT INTO `sys_role_menu` VALUES (1425, 2, 65, 0);
INSERT INTO `sys_role_menu` VALUES (1427, 1, 21, 0);
INSERT INTO `sys_role_menu` VALUES (1428, 2, 21, 0);
INSERT INTO `sys_role_menu` VALUES (1429, 1, 28, 0);
INSERT INTO `sys_role_menu` VALUES (1430, 2, 28, 0);

-- ----------------------------
-- Table structure for sys_role_permission
-- ----------------------------
DROP TABLE IF EXISTS `sys_role_permission`;
CREATE TABLE `sys_role_permission`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '关系表id',
  `role_id` bigint NOT NULL COMMENT '角色id',
  `permission_id` bigint NOT NULL COMMENT '权限id',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 327 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_role_permission
-- ----------------------------
INSERT INTO `sys_role_permission` VALUES (162, 1, 7);
INSERT INTO `sys_role_permission` VALUES (163, 1, 8);
INSERT INTO `sys_role_permission` VALUES (164, 1, 9);
INSERT INTO `sys_role_permission` VALUES (165, 1, 10);
INSERT INTO `sys_role_permission` VALUES (166, 1, 11);
INSERT INTO `sys_role_permission` VALUES (167, 1, 12);
INSERT INTO `sys_role_permission` VALUES (168, 1, 13);
INSERT INTO `sys_role_permission` VALUES (170, 1, 15);
INSERT INTO `sys_role_permission` VALUES (171, 1, 16);
INSERT INTO `sys_role_permission` VALUES (172, 1, 17);
INSERT INTO `sys_role_permission` VALUES (173, 1, 18);
INSERT INTO `sys_role_permission` VALUES (174, 1, 19);
INSERT INTO `sys_role_permission` VALUES (175, 1, 20);
INSERT INTO `sys_role_permission` VALUES (176, 1, 21);
INSERT INTO `sys_role_permission` VALUES (177, 1, 22);
INSERT INTO `sys_role_permission` VALUES (178, 1, 23);
INSERT INTO `sys_role_permission` VALUES (179, 1, 24);
INSERT INTO `sys_role_permission` VALUES (180, 1, 27);
INSERT INTO `sys_role_permission` VALUES (181, 1, 28);
INSERT INTO `sys_role_permission` VALUES (182, 1, 30);
INSERT INTO `sys_role_permission` VALUES (183, 1, 31);
INSERT INTO `sys_role_permission` VALUES (184, 1, 33);
INSERT INTO `sys_role_permission` VALUES (185, 1, 34);
INSERT INTO `sys_role_permission` VALUES (186, 1, 35);
INSERT INTO `sys_role_permission` VALUES (192, 1, 5);
INSERT INTO `sys_role_permission` VALUES (193, 1, 6);
INSERT INTO `sys_role_permission` VALUES (198, 1, 36);
INSERT INTO `sys_role_permission` VALUES (199, 1, 37);
INSERT INTO `sys_role_permission` VALUES (200, 1, 38);
INSERT INTO `sys_role_permission` VALUES (201, 1, 14);
INSERT INTO `sys_role_permission` VALUES (211, 1, 87);
INSERT INTO `sys_role_permission` VALUES (212, 1, 45);
INSERT INTO `sys_role_permission` VALUES (213, 1, 91);
INSERT INTO `sys_role_permission` VALUES (214, 1, 92);
INSERT INTO `sys_role_permission` VALUES (215, 1, 93);
INSERT INTO `sys_role_permission` VALUES (216, 1, 94);
INSERT INTO `sys_role_permission` VALUES (217, 1, 97);
INSERT INTO `sys_role_permission` VALUES (218, 1, 98);
INSERT INTO `sys_role_permission` VALUES (219, 1, 99);
INSERT INTO `sys_role_permission` VALUES (220, 1, 100);
INSERT INTO `sys_role_permission` VALUES (223, 1, 101);
INSERT INTO `sys_role_permission` VALUES (224, 1, 103);
INSERT INTO `sys_role_permission` VALUES (225, 1, 102);
INSERT INTO `sys_role_permission` VALUES (226, 1, 104);
INSERT INTO `sys_role_permission` VALUES (227, 1, 105);
INSERT INTO `sys_role_permission` VALUES (228, 1, 106);
INSERT INTO `sys_role_permission` VALUES (229, 1, 107);
INSERT INTO `sys_role_permission` VALUES (230, 1, 108);
INSERT INTO `sys_role_permission` VALUES (231, 1, 109);
INSERT INTO `sys_role_permission` VALUES (232, 1, 110);
INSERT INTO `sys_role_permission` VALUES (233, 1, 111);
INSERT INTO `sys_role_permission` VALUES (234, 1, 112);
INSERT INTO `sys_role_permission` VALUES (235, 1, 113);
INSERT INTO `sys_role_permission` VALUES (236, 1, 114);
INSERT INTO `sys_role_permission` VALUES (237, 1, 115);
INSERT INTO `sys_role_permission` VALUES (238, 1, 116);
INSERT INTO `sys_role_permission` VALUES (239, 1, 117);
INSERT INTO `sys_role_permission` VALUES (240, 1, 118);
INSERT INTO `sys_role_permission` VALUES (241, 1, 119);
INSERT INTO `sys_role_permission` VALUES (242, 1, 120);
INSERT INTO `sys_role_permission` VALUES (243, 1, 121);
INSERT INTO `sys_role_permission` VALUES (244, 1, 122);
INSERT INTO `sys_role_permission` VALUES (245, 1, 123);
INSERT INTO `sys_role_permission` VALUES (246, 1, 124);
INSERT INTO `sys_role_permission` VALUES (247, 1, 125);
INSERT INTO `sys_role_permission` VALUES (248, 1, 126);
INSERT INTO `sys_role_permission` VALUES (249, 1, 127);
INSERT INTO `sys_role_permission` VALUES (250, 1, 128);
INSERT INTO `sys_role_permission` VALUES (251, 1, 129);
INSERT INTO `sys_role_permission` VALUES (252, 1, 130);
INSERT INTO `sys_role_permission` VALUES (253, 1, 131);
INSERT INTO `sys_role_permission` VALUES (254, 1, 132);
INSERT INTO `sys_role_permission` VALUES (255, 1, 133);
INSERT INTO `sys_role_permission` VALUES (256, 1, 134);
INSERT INTO `sys_role_permission` VALUES (257, 1, 136);
INSERT INTO `sys_role_permission` VALUES (258, 1, 137);
INSERT INTO `sys_role_permission` VALUES (259, 1, 138);
INSERT INTO `sys_role_permission` VALUES (260, 1, 139);
INSERT INTO `sys_role_permission` VALUES (261, 1, 140);
INSERT INTO `sys_role_permission` VALUES (262, 1, 141);
INSERT INTO `sys_role_permission` VALUES (263, 1, 142);
INSERT INTO `sys_role_permission` VALUES (264, 1, 143);
INSERT INTO `sys_role_permission` VALUES (265, 1, 144);
INSERT INTO `sys_role_permission` VALUES (266, 1, 145);
INSERT INTO `sys_role_permission` VALUES (268, 1, 146);
INSERT INTO `sys_role_permission` VALUES (269, 1, 147);
INSERT INTO `sys_role_permission` VALUES (271, 2, 5);
INSERT INTO `sys_role_permission` VALUES (272, 2, 6);
INSERT INTO `sys_role_permission` VALUES (273, 2, 11);
INSERT INTO `sys_role_permission` VALUES (274, 2, 13);
INSERT INTO `sys_role_permission` VALUES (275, 2, 16);
INSERT INTO `sys_role_permission` VALUES (276, 2, 17);
INSERT INTO `sys_role_permission` VALUES (277, 2, 18);
INSERT INTO `sys_role_permission` VALUES (278, 2, 21);
INSERT INTO `sys_role_permission` VALUES (279, 2, 22);
INSERT INTO `sys_role_permission` VALUES (280, 2, 23);
INSERT INTO `sys_role_permission` VALUES (281, 2, 27);
INSERT INTO `sys_role_permission` VALUES (282, 2, 31);
INSERT INTO `sys_role_permission` VALUES (283, 2, 33);
INSERT INTO `sys_role_permission` VALUES (284, 2, 36);
INSERT INTO `sys_role_permission` VALUES (285, 2, 37);
INSERT INTO `sys_role_permission` VALUES (286, 2, 45);
INSERT INTO `sys_role_permission` VALUES (287, 2, 91);
INSERT INTO `sys_role_permission` VALUES (288, 2, 93);
INSERT INTO `sys_role_permission` VALUES (289, 2, 94);
INSERT INTO `sys_role_permission` VALUES (290, 2, 97);
INSERT INTO `sys_role_permission` VALUES (291, 2, 98);
INSERT INTO `sys_role_permission` VALUES (292, 2, 100);
INSERT INTO `sys_role_permission` VALUES (293, 2, 102);
INSERT INTO `sys_role_permission` VALUES (294, 2, 103);
INSERT INTO `sys_role_permission` VALUES (295, 2, 104);
INSERT INTO `sys_role_permission` VALUES (296, 2, 106);
INSERT INTO `sys_role_permission` VALUES (297, 2, 110);
INSERT INTO `sys_role_permission` VALUES (298, 2, 111);
INSERT INTO `sys_role_permission` VALUES (299, 2, 113);
INSERT INTO `sys_role_permission` VALUES (300, 2, 115);
INSERT INTO `sys_role_permission` VALUES (301, 2, 116);
INSERT INTO `sys_role_permission` VALUES (302, 2, 119);
INSERT INTO `sys_role_permission` VALUES (303, 2, 120);
INSERT INTO `sys_role_permission` VALUES (304, 2, 123);
INSERT INTO `sys_role_permission` VALUES (305, 2, 124);
INSERT INTO `sys_role_permission` VALUES (306, 2, 127);
INSERT INTO `sys_role_permission` VALUES (307, 2, 128);
INSERT INTO `sys_role_permission` VALUES (308, 2, 131);
INSERT INTO `sys_role_permission` VALUES (309, 2, 134);
INSERT INTO `sys_role_permission` VALUES (310, 2, 136);
INSERT INTO `sys_role_permission` VALUES (311, 2, 137);
INSERT INTO `sys_role_permission` VALUES (312, 2, 140);
INSERT INTO `sys_role_permission` VALUES (313, 2, 141);
INSERT INTO `sys_role_permission` VALUES (314, 2, 144);
INSERT INTO `sys_role_permission` VALUES (315, 2, 145);
INSERT INTO `sys_role_permission` VALUES (317, 1, 150);
INSERT INTO `sys_role_permission` VALUES (318, 1, 151);
INSERT INTO `sys_role_permission` VALUES (319, 1, 152);
INSERT INTO `sys_role_permission` VALUES (320, 1, 153);
INSERT INTO `sys_role_permission` VALUES (321, 2, 150);
INSERT INTO `sys_role_permission` VALUES (322, 1, 154);
INSERT INTO `sys_role_permission` VALUES (323, 1, 155);
INSERT INTO `sys_role_permission` VALUES (324, 1, 156);
INSERT INTO `sys_role_permission` VALUES (325, 1, 157);
INSERT INTO `sys_role_permission` VALUES (326, 2, 156);

-- ----------------------------
-- Table structure for sys_user
-- ----------------------------
DROP TABLE IF EXISTS `sys_user`;
CREATE TABLE `sys_user`  (
  `id` bigint UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '用户id',
  `nickname` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '用户昵称',
  `username` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '用户名',
  `gender` tinyint(1) NOT NULL DEFAULT 0 COMMENT '用户性别(0,未定义,1,男,2女)',
  `password` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '用户密码',
  `avatar` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '用户头像',
  `intro` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '个人简介',
  `email` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '用户邮箱',
  `register_ip` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '注册ip',
  `register_type` tinyint NOT NULL COMMENT '注册方式(0邮箱/姓名 1Gitee 2Github)',
  `register_address` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '注册地址',
  `login_ip` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '最近登录ip',
  `login_address` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '最近登录地址',
  `login_type` tinyint NULL DEFAULT NULL COMMENT '最近登录类型(0邮箱/姓名 1Gitee 2Github)',
  `login_time` datetime NOT NULL COMMENT '用户最近登录时间',
  `is_disable` tinyint(1) NOT NULL DEFAULT 0 COMMENT '是否禁用 (0否 1是)',
  `create_time` datetime NOT NULL COMMENT '用户创建时间',
  `update_time` datetime NOT NULL COMMENT '用户更新时间',
  `is_deleted` tinyint(1) NOT NULL DEFAULT 0 COMMENT '是否删除（0：未删除，1：已删除）',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 88066036 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_user
-- ----------------------------
INSERT INTO `sys_user` VALUES (1, 'Admin', 'Admin', 0, '$2a$10$VyFtQ3T943p3NY5R0IxzIONjdqABmuCSGiHe5uV8d1ujLGYuS2KZe', 'https://image.kuailemao.xyz/blog/user/avatar/ce7998d8-43b5-4457-bc5c-4fa849160b3c.jpg', '我是一个伟大的站主~~', 'ruyusan@qq.com', '127.0.0.1', 0, '内网IP', '127.0.0.1', 'XX XX', 0, '2024-10-21 08:52:51', 0, '2023-10-13 15:16:01', '2024-10-21 08:52:51', 0);
INSERT INTO `sys_user` VALUES (88065990, '测试用户', 'Test', 0, '$2a$10$VyFtQ3T943p3NY5R0IxzIONjdqABmuCSGiHe5uV8d1ujLGYuS2KZe', 'https://image.kuailemao.xyz/blog/user/avatar/ce7998d8-43b5-4457-bc5c-4fa849160b3c.jpg', '这是测试用户', NULL, '127.0.0.1', 0, '未知', '127.0.0.1', '未知', 0, '2024-10-21 16:29:53', 0, '2024-03-01 11:10:31', '2024-10-21 16:29:53', 0);

-- ----------------------------
-- Table structure for sys_user_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_user_role`;
CREATE TABLE `sys_user_role`  (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '主键',
  `user_id` int NOT NULL COMMENT '用户id',
  `role_id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '角色id',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 45 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_user_role
-- ----------------------------
INSERT INTO `sys_user_role` VALUES (1, 1, '1');
INSERT INTO `sys_user_role` VALUES (44, 88065990, '2');

-- ----------------------------
-- Table structure for sys_website_info
-- ----------------------------
DROP TABLE IF EXISTS `sys_website_info`;
CREATE TABLE `sys_website_info`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `webmaster_avatar` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '站长头像',
  `webmaster_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '站长名称',
  `webmaster_copy` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '站长文案',
  `webmaster_profile_background` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '站长资料卡背景图',
  `gitee_link` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT 'gitee链接',
  `github_link` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT 'github链接',
  `website_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '网站名称',
  `header_notification` varchar(150) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '头部通知',
  `sidebar_announcement` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '侧面公告',
  `record_info` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '备案信息',
  `start_time` datetime NULL DEFAULT NULL COMMENT '开始运行时间',
  `create_time` datetime NOT NULL COMMENT '用户创建时间',
  `update_time` datetime NOT NULL COMMENT '用户更新时间',
  `is_deleted` tinyint(1) NOT NULL DEFAULT 0 COMMENT '是否删除（0：未删除，1：已删除）',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 11 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_website_info
-- ----------------------------
INSERT INTO `sys_website_info` VALUES (1, 'https://image.kuailemao.xyz/blog/websiteInfo/avatar/95924d04-f0c5-4560-a369-645f3c9080bf.webp', 'Ruyu', '生活想要活埋了我，不料我是一粒种子', 'https://image.kuailemao.xyz/blog/websiteInfo/background/8337f2ae-c573-4719-a734-901b3d6859bc.webp', 'https://gitee.com/kuailemao', 'https://github.com/kuailemao', 'Ruyu-快乐猫', '禁止发无关评论，违者永久封禁！！！', '本项目github & gitee 开源地址：\nhttps://github.com/kuailemao/Ruyu-Blog\nhttps://gitee.com/kuailemao/ruyu-blog\n项目部署文档：https://kuailemao.xyz/article/48\n文档独立站点：http://docs.kuailemao.xyz/\nqq交流群：635887836\n欢迎指出网站的不足，给我提供意见。', '备案号', '2024-03-03 03:30:30', '2023-12-27 14:28:10', '2024-10-15 18:23:08', 0);

-- ----------------------------
-- Table structure for t_article
-- ----------------------------
DROP TABLE IF EXISTS `t_article`;
CREATE TABLE `t_article`  (
  `id` bigint UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '文章id',
  `user_id` bigint NOT NULL COMMENT '作者id',
  `category_id` bigint NOT NULL COMMENT '分类id',
  `article_cover` varchar(1024) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '文章缩略图',
  `article_title` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '文章标题',
  `article_content` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '文章内容',
  `article_type` tinyint NOT NULL COMMENT '类型 (1原创 2转载 3翻译)',
  `is_top` tinyint NOT NULL COMMENT '是否置顶 (0否 1是）',
  `status` tinyint NOT NULL COMMENT '文章状态 (1公开 2私密 3草稿)',
  `visit_count` bigint NOT NULL DEFAULT 0 COMMENT '访问量',
  `create_time` datetime NOT NULL COMMENT '文章创建时间',
  `update_time` datetime NOT NULL COMMENT '文章更新时间',
  `is_deleted` tinyint NOT NULL DEFAULT 0 COMMENT '是否删除（0：未删除，1：已删除）',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 53 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for t_article_tag
-- ----------------------------
DROP TABLE IF EXISTS `t_article_tag`;
CREATE TABLE `t_article_tag`  (
  `id` bigint UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '关系表id',
  `article_id` bigint UNSIGNED NOT NULL COMMENT '文章id',
  `tag_id` bigint NOT NULL COMMENT '标签id',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `is_deleted` tinyint NOT NULL DEFAULT 0 COMMENT '是否删除（0：未删除，1：已删除）',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 187 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for t_banners
-- ----------------------------
DROP TABLE IF EXISTS `t_banners`;
CREATE TABLE `t_banners`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `path` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '图片路径',
  `size` bigint NOT NULL COMMENT '图片大小 (字节)',
  `type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '图片类型 (MIME)',
  `user_id` bigint NOT NULL COMMENT '上传人id',
  `sort_order` int NOT NULL COMMENT '图片顺序',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 43 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for t_black_list
-- ----------------------------
DROP TABLE IF EXISTS `t_black_list`;
CREATE TABLE `t_black_list`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '表id',
  `user_id` bigint NULL DEFAULT NULL COMMENT '用户id',
  `reason` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '封禁理由',
  `banned_time` datetime NOT NULL COMMENT '封禁时间',
  `expires_time` datetime NOT NULL COMMENT '到期时间',
  `type` tinyint NOT NULL COMMENT '类型（1：用户，2：路人/攻击者）',
  `ip_info` json NULL COMMENT '如果type=2，则需要有ip信息',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime NOT NULL COMMENT '更新时间',
  `is_deleted` tinyint NOT NULL DEFAULT 0 COMMENT '是否删除（0：未删除，1：已删除）',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 257 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for t_category
-- ----------------------------
DROP TABLE IF EXISTS `t_category`;
CREATE TABLE `t_category`  (
  `id` bigint UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '分类id',
  `category_name` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '分类名',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime NOT NULL COMMENT '更新时间',
  `is_deleted` tinyint NOT NULL DEFAULT 0 COMMENT '是否删除（0：未删除，1：已删除）',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 14 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = DYNAMIC;


-- ----------------------------
-- Table structure for t_comment
-- ----------------------------
DROP TABLE IF EXISTS `t_comment`;
CREATE TABLE `t_comment`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '评论id',
  `type` tinyint(1) NOT NULL COMMENT '评论类型 (1文章 2留言板)',
  `type_id` bigint NOT NULL COMMENT '类型id',
  `parent_id` bigint NULL DEFAULT NULL COMMENT '父评论id',
  `reply_id` bigint NULL DEFAULT NULL COMMENT '回复评论id',
  `comment_content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '评论的内容',
  `comment_user_id` bigint NOT NULL COMMENT '评论用户的id',
  `reply_user_id` bigint NULL DEFAULT NULL COMMENT '回复用户的id',
  `is_check` tinyint(1) NOT NULL DEFAULT 1 COMMENT '是否通过 (0否 1是)',
  `create_time` datetime NOT NULL COMMENT '评论时间',
  `update_time` datetime NOT NULL COMMENT '更新时间',
  `is_deleted` tinyint(1) NOT NULL DEFAULT 0 COMMENT '是否删除（0：未删除，1：已删除）',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 131 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for t_favorite
-- ----------------------------
DROP TABLE IF EXISTS `t_favorite`;
CREATE TABLE `t_favorite`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '收藏id',
  `user_id` bigint NOT NULL COMMENT '收藏的用户id',
  `type` tinyint NOT NULL COMMENT '收藏类型(1,文章 2,留言板)',
  `type_id` bigint NOT NULL COMMENT '类型id',
  `is_check` tinyint(1) NOT NULL DEFAULT 1 COMMENT '是否有效 (0否 1是)',
  `create_time` datetime NOT NULL COMMENT '收藏时间',
  `is_deleted` tinyint(1) NOT NULL DEFAULT 0 COMMENT '是否删除（0：未删除，1：已删除）',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 189 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = DYNAMIC;


-- ----------------------------
-- Table structure for t_leave_word
-- ----------------------------
DROP TABLE IF EXISTS `t_leave_word`;
CREATE TABLE `t_leave_word`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'id',
  `user_id` bigint NOT NULL COMMENT '留言用户id',
  `content` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '留言内容',
  `is_check` tinyint(1) NOT NULL DEFAULT 1 COMMENT '是否通过 (0否 1是)',
  `create_time` datetime NOT NULL COMMENT '留言时间',
  `update_time` datetime NOT NULL COMMENT '更新时间',
  `is_deleted` tinyint(1) NOT NULL DEFAULT 0 COMMENT '是否删除（0：未删除，1：已删除）',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 37 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for t_like
-- ----------------------------
DROP TABLE IF EXISTS `t_like`;
CREATE TABLE `t_like`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '点赞表id',
  `user_id` bigint NOT NULL COMMENT '点赞的用户id',
  `type` tinyint NOT NULL COMMENT '点赞类型(1,文章,2,评论,3留言板)',
  `type_id` bigint NOT NULL COMMENT '点赞的文章id',
  `create_time` datetime NOT NULL COMMENT '点赞时间',
  `update_time` datetime NOT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 363 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = DYNAMIC;


-- ----------------------------
-- Table structure for t_link
-- ----------------------------
DROP TABLE IF EXISTS `t_link`;
CREATE TABLE `t_link`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '友链表id',
  `user_id` bigint NOT NULL COMMENT '用户id',
  `name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '网站名称',
  `url` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '网站地址',
  `description` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '网站描述',
  `background` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '网站背景',
  `is_check` tinyint NOT NULL DEFAULT 0 COMMENT '审核状态（0：未通过，1：已通过）',
  `email` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '邮箱地址',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime NOT NULL COMMENT '更新时间',
  `is_deleted` tinyint NOT NULL DEFAULT 0 COMMENT '是否删除（0：未删除，1：已删除）',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 41 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for t_tag
-- ----------------------------
DROP TABLE IF EXISTS `t_tag`;
CREATE TABLE `t_tag`  (
  `id` bigint UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '标签id',
  `tag_name` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '标签名称',
  `create_time` datetime NOT NULL COMMENT '标签创建时间',
  `update_time` datetime NOT NULL COMMENT '标签更新时间',
  `is_deleted` tinyint NOT NULL DEFAULT 0 COMMENT '是否删除（0：未删除，1：已删除）',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 14 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for t_tree_hole
-- ----------------------------
DROP TABLE IF EXISTS `t_tree_hole`;
CREATE TABLE `t_tree_hole`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '树洞表id',
  `user_id` bigint NOT NULL COMMENT '用户id',
  `content` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '内容',
  `is_check` tinyint(1) NOT NULL DEFAULT 1 COMMENT '是否通过 (0否 1是)',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime NOT NULL COMMENT '修改时间',
  `is_deleted` tinyint(1) NOT NULL DEFAULT 0 COMMENT '是否删除（0：未删除，1：已删除）',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 57 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = DYNAMIC;


SET FOREIGN_KEY_CHECKS = 1;
