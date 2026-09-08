# 变更日志

## 2026-07-31 — 新增：账单手工生成功能

### DDL：新增两张表

```sql
-- 账单手工生成申请主表
CREATE TABLE `manual_bill_generation` (
  `application_id` varchar(30) NOT NULL COMMENT '申请ID',
  `community_id` varchar(30) NOT NULL COMMENT '小区ID',
  `application_name` varchar(200) DEFAULT NULL COMMENT '申请名称',
  `bill_month` varchar(7) DEFAULT NULL COMMENT '目标账单月份，格式YYYY-MM',
  `config_id` varchar(30) DEFAULT NULL COMMENT '费用项ID',
  `fee_name` varchar(100) DEFAULT NULL COMMENT '费用项名称（冗余）',
  `state` varchar(10) DEFAULT NULL COMMENT '状态：1001待审核/1002已通过/1003已驳回',
  `audit_user_id` varchar(30) DEFAULT NULL COMMENT '审核人ID',
  `audit_user_name` varchar(100) DEFAULT NULL COMMENT '审核人姓名',
  `audit_time` datetime DEFAULT NULL COMMENT '审核时间',
  `audit_remark` varchar(500) DEFAULT NULL COMMENT '审核备注/驳回原因',
  `total_count` int(11) DEFAULT NULL COMMENT '总明细数',
  `success_count` int(11) DEFAULT NULL COMMENT '成功生成数',
  `error_count` int(11) DEFAULT NULL COMMENT '生成失败数',
  `remark` varchar(500) DEFAULT NULL COMMENT '申请备注',
  `create_user_id` varchar(30) DEFAULT NULL COMMENT '创建人ID',
  `create_user_name` varchar(100) DEFAULT NULL COMMENT '创建人姓名',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `status_cd` varchar(2) DEFAULT '0' COMMENT '数据状态，0有效1删除',
  PRIMARY KEY (`application_id`),
  KEY `idx_community_id` (`community_id`),
  KEY `idx_state` (`state`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='账单手工生成申请主表';

-- 账单手工生成明细表
CREATE TABLE `manual_bill_generation_detail` (
  `detail_id` varchar(30) NOT NULL COMMENT '明细ID',
  `application_id` varchar(30) DEFAULT NULL COMMENT '关联申请ID',
  `community_id` varchar(30) DEFAULT NULL COMMENT '小区ID',
  `config_id` varchar(30) DEFAULT NULL COMMENT '费用项ID',
  `fee_name` varchar(100) DEFAULT NULL COMMENT '费用项名称（冗余）',
  `rule_id` varchar(30) DEFAULT NULL COMMENT '对应pay_fee_rule.rule_id',
  `room_id` varchar(30) DEFAULT NULL COMMENT '房屋ID',
  `room_name` varchar(200) DEFAULT NULL COMMENT '房屋名称（冗余）',
  `owner_id` varchar(30) DEFAULT NULL COMMENT '业主ID',
  `owner_name` varchar(100) DEFAULT NULL COMMENT '业主姓名（冗余）',
  `fee_type_cd` varchar(30) DEFAULT NULL COMMENT '费用类型',
  `fee_flag` varchar(10) DEFAULT NULL COMMENT '费用标识（固定1003006周期性）',
  `cur_year_month` varchar(30) DEFAULT NULL COMMENT '当前账期/账单日（冗余自pay_fee_rule.cur_year_month）',
  `prepayment_period` int(11) DEFAULT NULL COMMENT '生成期数',
  `amount` decimal(10,2) DEFAULT NULL COMMENT '预计单期金额',
  `state` varchar(10) DEFAULT NULL COMMENT '明细状态：1001待生成/1002已生成/1003生成失败',
  `error_msg` varchar(500) DEFAULT NULL COMMENT '生成失败原因',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `status_cd` varchar(2) DEFAULT '0' COMMENT '数据状态，0有效1删除',
  PRIMARY KEY (`detail_id`),
  KEY `idx_application_id` (`application_id`),
  KEY `idx_rule_id` (`rule_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='账单手工生成明细表';

ALTER TABLE `pay_fee_rule`
  ADD COLUMN `owner_id` varchar(30) DEFAULT NULL COMMENT '业主ID',
  ADD COLUMN `owner_name` varchar(50) DEFAULT NULL COMMENT '业主姓名',
  ADD COLUMN `payer_obj_name` varchar(64) NOT NULL DEFAULT '' COMMENT '缴费对象名称';
```

## 2026-08-06 — 修复：manual_bill_generation_detail 列宽不足

`fee_type_cd` 原 `varchar(10)` 无法容纳 12 位费用类型码（如 `888800010001`），`cur_year_month` 原 `varchar(7)` 无法容纳完整账期日期（如 `2026-09-22`），导致提交申请报 `Data too long`。两列同时扩宽至 `varchar(30)`，与 `pay_fee_detail.fee_type_cd`、`pay_fee_rule.cur_year_month` 保持一致。

```sql
ALTER TABLE `manual_bill_generation_detail`
  MODIFY COLUMN `fee_type_cd` varchar(30) DEFAULT NULL COMMENT '费用类型',
  MODIFY COLUMN `cur_year_month` varchar(30) DEFAULT NULL COMMENT '当前账期/账单日（冗余自 pay_fee_rule.cur_year_month）';
  
INSERT INTO `c_service` (`id`, `service_id`, `service_code`, `business_type_cd`, `name`, `seq`, `messageQueueName`, `is_instance`, `url`, `method`, `timeout`, `retry_count`, `provide_app_id`, `create_time`, `status_cd`) VALUES ('2084', '982026080614340001', 'unit.queryUnitsHasOwner', 'API', '查询有业主的单元', '1', NULL, 'CMD', 'http://community-service', 'GET', '60', '3', '8000418002', '2026-08-06 09:50:28', '0');
INSERT INTO `c_service` (`id`, `service_id`, `service_code`, `business_type_cd`, `name`, `seq`, `messageQueueName`, `is_instance`, `url`, `method`, `timeout`, `retry_count`, `provide_app_id`, `create_time`, `status_cd`) VALUES ('2083', '982026073100000006', 'fee.deleteManualBillApplication', 'API', '删除手工账单申请', '1', NULL, 'CMD', 'http://fee-service', 'POST', '60', '3', '8000418002', '2026-08-06 09:32:14', '0');
INSERT INTO `c_service` (`id`, `service_id`, `service_code`, `business_type_cd`, `name`, `seq`, `messageQueueName`, `is_instance`, `url`, `method`, `timeout`, `retry_count`, `provide_app_id`, `create_time`, `status_cd`) VALUES ('2082', '982026073100000005', 'fee.auditManualBillApplication', 'API', '审核手工账单生成申请', '1', NULL, 'CMD', 'http://fee-service', 'POST', '60', '3', '8000418002', '2026-08-06 09:32:14', '0');
INSERT INTO `c_service` (`id`, `service_id`, `service_code`, `business_type_cd`, `name`, `seq`, `messageQueueName`, `is_instance`, `url`, `method`, `timeout`, `retry_count`, `provide_app_id`, `create_time`, `status_cd`) VALUES ('2078', '982026073100000001', 'fee.searchManualBillTargets', 'API', '查询手工账单生成目标', '1', NULL, 'CMD', 'http://fee-service', 'GET', '60', '3', '8000418002', '2026-08-06 09:32:13', '0');
INSERT INTO `c_service` (`id`, `service_id`, `service_code`, `business_type_cd`, `name`, `seq`, `messageQueueName`, `is_instance`, `url`, `method`, `timeout`, `retry_count`, `provide_app_id`, `create_time`, `status_cd`) VALUES ('2079', '982026073100000002', 'fee.saveManualBillApplication', 'API', '提交手工账单生成申请', '1', NULL, 'CMD', 'http://fee-service', 'POST', '60', '3', '8000418002', '2026-08-06 09:32:13', '0');
INSERT INTO `c_service` (`id`, `service_id`, `service_code`, `business_type_cd`, `name`, `seq`, `messageQueueName`, `is_instance`, `url`, `method`, `timeout`, `retry_count`, `provide_app_id`, `create_time`, `status_cd`) VALUES ('2080', '982026073100000003', 'fee.listManualBillApplications', 'API', '查询手工账单申请列表', '1', NULL, 'CMD', 'http://fee-service', 'GET', '60', '3', '8000418002', '2026-08-06 09:32:13', '0');
INSERT INTO `c_service` (`id`, `service_id`, `service_code`, `business_type_cd`, `name`, `seq`, `messageQueueName`, `is_instance`, `url`, `method`, `timeout`, `retry_count`, `provide_app_id`, `create_time`, `status_cd`) VALUES ('2081', '982026073100000004', 'fee.getManualBillApplication', 'API', '查询手工账单申请详情', '1', NULL, 'CMD', 'http://fee-service', 'GET', '60', '3', '8000418002', '2026-08-06 09:32:13', '0');
INSERT INTO `c_route` (`id`, `app_id`, `service_id`, `order_type_cd`, `invoke_limit_times`, `invoke_model`, `create_time`, `status_cd`) VALUES ('3070', '8000418004', '982026080614340001', 'Q', '1000', 'S', '2026-08-06 09:50:43', '0');
INSERT INTO `c_route` (`id`, `app_id`, `service_id`, `order_type_cd`, `invoke_limit_times`, `invoke_model`, `create_time`, `status_cd`) VALUES ('3065', '8000418004', '982026073100000002', 'D', '1000', 'S', '2026-08-06 09:32:22', '0');
INSERT INTO `c_route` (`id`, `app_id`, `service_id`, `order_type_cd`, `invoke_limit_times`, `invoke_model`, `create_time`, `status_cd`) VALUES ('3069', '8000418004', '982026073100000006', 'D', '1000', 'S', '2026-08-06 09:32:22', '0');
INSERT INTO `c_route` (`id`, `app_id`, `service_id`, `order_type_cd`, `invoke_limit_times`, `invoke_model`, `create_time`, `status_cd`) VALUES ('3067', '8000418004', '982026073100000004', 'Q', '1000', 'S', '2026-08-06 09:32:22', '0');
INSERT INTO `c_route` (`id`, `app_id`, `service_id`, `order_type_cd`, `invoke_limit_times`, `invoke_model`, `create_time`, `status_cd`) VALUES ('3068', '8000418004', '982026073100000005', 'D', '1000', 'S', '2026-08-06 09:32:22', '0');
INSERT INTO `c_route` (`id`, `app_id`, `service_id`, `order_type_cd`, `invoke_limit_times`, `invoke_model`, `create_time`, `status_cd`) VALUES ('3064', '8000418004', '982026073100000001', 'Q', '1000', 'S', '2026-08-06 09:32:22', '0');
INSERT INTO `c_route` (`id`, `app_id`, `service_id`, `order_type_cd`, `invoke_limit_times`, `invoke_model`, `create_time`, `status_cd`) VALUES ('3066', '8000418004', '982026073100000003', 'Q', '1000', 'S', '2026-08-06 09:32:22', '0');


```

## 2026-08-11 车辆加入联系人功能
```sql
ALTER TABLE `owner_car`
  ADD COLUMN `pa_id` varchar(30) NOT NULL DEFAULT '-1' COMMENT '停车场ID' AFTER `ps_id`,
  ADD COLUMN `contact` varchar(64) DEFAULT '' COMMENT '联系人' AFTER `pa_id`,
  ADD COLUMN `tel` varchar(20) DEFAULT '' COMMENT '联系电话' AFTER `contact`,
  MODIFY COLUMN `ps_id` varchar(30) DEFAULT '-1' COMMENT '车位ID（-1或无为空车位车辆）';

-- 索引（按停车场维度查重/查询）
ALTER TABLE `owner_car` ADD INDEX `idx_oc_pa_id` (`pa_id`) USING BTREE;
INSERT INTO `t_dict` ( `status_cd`, `name`, `description`, `create_time`, `table_name`, `table_columns`) 
VALUES ( '11', '预付款扣', '预付款扣', '2024-05-31 16:17:51', 'pay_fee_detail', 'prime_rate');

```

## 2026-08-27 车位修改，支持停车区
```sql
DROP TABLE IF EXISTS `parking_area_section`;
CREATE TABLE `parking_area_section` (
  `section_id`  varchar(30) NOT NULL COMMENT '区域ID',
  `pa_id`       varchar(30) NOT NULL COMMENT '停车场ID',
  `community_id` varchar(30) NOT NULL COMMENT '小区ID',
  `section_num` varchar(12) NOT NULL COMMENT '区域编号',
  `section_name` varchar(64) DEFAULT NULL COMMENT '区域名称',
  `remark`      varchar(300) DEFAULT NULL COMMENT '备注',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `status_cd`   varchar(2) NOT NULL DEFAULT '0' COMMENT '数据状态 0在用 1失效',
  PRIMARY KEY (`section_id`),
  KEY `idx_pa_id` (`pa_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- 车位表扩展字段（均非必填）
-- ----------------------------
ALTER TABLE `parking_space`
  ADD COLUMN `owner_id`   varchar(30) DEFAULT NULL COMMENT '业主ID' AFTER `pa_id`,
  ADD COLUMN `owner_name` varchar(64) DEFAULT NULL COMMENT '业主名称' AFTER `owner_id`,
  ADD COLUMN `car_id`     varchar(30) DEFAULT NULL COMMENT '车辆ID' AFTER `owner_name`,
  ADD COLUMN `car_num`    varchar(12) DEFAULT NULL COMMENT '车牌号' AFTER `car_id`,
  ADD COLUMN `start_time` datetime DEFAULT NULL COMMENT '出租开始时间' AFTER `car_num`,
  ADD COLUMN `end_time`   datetime DEFAULT NULL COMMENT '出租结束时间' AFTER `start_time`,
  ADD COLUMN `section_id` varchar(30) DEFAULT NULL COMMENT '停车区域ID' AFTER `pa_id`;

-- ----------------------------
-- c_service 服务注册（停车区域 + 停车场-区域树接口）
-- ----------------------------
INSERT INTO `c_service` VALUES ('2085', '982026082700000001', 'parkingArea.saveParkingAreaSection', 'API', '添加停车区域', '1', NULL, 'CMD', 'http://community-service', 'POST', '60', '3', '8000418002', '2026-08-27 09:34:00', '0');
INSERT INTO `c_service` VALUES ('2086', '982026082700000002', 'parkingArea.updateParkingAreaSection', 'API', '修改停车区域', '1', NULL, 'CMD', 'http://community-service', 'POST', '60', '3', '8000418002', '2026-08-27 09:34:00', '0');
INSERT INTO `c_service` VALUES ('2087', '982026082700000003', 'parkingArea.deleteParkingAreaSection', 'API', '删除停车区域', '1', NULL, 'CMD', 'http://community-service', 'POST', '60', '3', '8000418002', '2026-08-27 09:34:00', '0');
INSERT INTO `c_service` VALUES ('2088', '982026082700000004', 'parkingArea.listParkingAreaSections', 'API', '查询停车区域', '1', NULL, 'CMD', 'http://community-service', 'GET', '60', '3', '8000418002', '2026-08-27 09:34:00', '0');
INSERT INTO `c_service` VALUES ('2089', '982026082700000005', 'parkingArea.queryParkingAreaAndSections', 'API', '查询停车场及停车区域', '1', NULL, 'CMD', 'http://community-service', 'GET', '60', '3', '8000418002', '2026-08-27 09:34:00', '0');
INSERT INTO `c_service` VALUES ('2090', '982026082700000006', 'parkingSpace.releaseParkingSpace', 'API', '释放车位', '1', NULL, 'CMD', 'http://community-service', 'POST', '60', '3', '8000418002', '2026-08-28 09:00:00', '0');
INSERT INTO `c_service` VALUES ('2091', '982026082700000007', 'parkingSpace.updateParkingSpaceCar', 'API', '修改车位车辆', '1', NULL, 'CMD', 'http://community-service', 'POST', '60', '3', '8000418002', '2026-08-28 09:00:00', '0');
-- ----------------------------
-- c_route 路由配置（appId 8000418004 管理端）
-- ----------------------------
INSERT INTO `c_route` VALUES ('3071', '8000418004', '982026082700000001', 'D', '1000', 'S', '2026-08-27 09:34:00', '0');
INSERT INTO `c_route` VALUES ('3072', '8000418004', '982026082700000002', 'D', '1000', 'S', '2026-08-27 09:34:00', '0');
INSERT INTO `c_route` VALUES ('3073', '8000418004', '982026082700000003', 'D', '1000', 'S', '2026-08-27 09:34:00', '0');
INSERT INTO `c_route` VALUES ('3074', '8000418004', '982026082700000004', 'Q', '1000', 'S', '2026-08-27 09:34:00', '0');
INSERT INTO `c_route` VALUES ('3075', '8000418004', '982026082700000005', 'Q', '1000', 'S', '2026-08-27 09:34:00', '0');


-- ----------------------------
-- c_route 路由配置（appId 8000418004 管理端）
-- ----------------------------
INSERT INTO `c_route` VALUES ('3076', '8000418004', '982026082700000006', 'D', '1000', 'S', '2026-08-28 09:00:00', '0');
INSERT INTO `c_route` VALUES ('3077', '8000418004', '982026082700000007', 'D', '1000', 'S', '2026-08-28 09:00:00', '0');
ALTER TABLE `owner_car` DROP COLUMN `car_brand`, DROP COLUMN `car_color`, DROP COLUMN `ps_id`;
ALTER TABLE `business_owner_car` DROP COLUMN `car_brand`, DROP COLUMN `car_color`, DROP COLUMN `ps_id`;
INSERT INTO `c_service` VALUES ('2092', '982026082700000008', 'owner.queryCarsWithoutParkingSpace', 'API', '查询无车位车辆', '1', NULL, 'CMD', 'http://user-service', 'GET', '60', '3', '8000418002', '2026-08-28 09:00:00', '0');
INSERT INTO `c_route` VALUES ('3078', '8000418004', '982026082700000008', 'Q', '1000', 'S', '2026-08-28 09:00:00', '0');
INSERT INTO `c_service` (service_id, service_code, business_type_cd, name, seq, is_instance, url, method, timeout, retry_count, provide_app_id, create_time, status_cd)
VALUES ('9820260827000001', 'cache.queryCache', 'API', '查询缓存列表', '1', 'CMD', 'http://dev-service', 'GET', '60', '3', '8000418002', NOW(), '0');

INSERT INTO `c_service` (service_id, service_code, business_type_cd, name, seq, is_instance, url, method, timeout, retry_count, provide_app_id, create_time, status_cd)
VALUES ('9820260827000002', 'cache.flushCache', 'API', '刷新缓存', '1', 'CMD', 'http://dev-service', 'POST', '60', '3', '8000418002', NOW(), '0');

-- 2. c_route 注册（app 8000418002=PC端，S=同步）
INSERT INTO `c_route` (app_id, service_id, order_type_cd, invoke_limit_times, invoke_model, create_time, status_cd)
VALUES ('8000418004', '9820260827000001', 'Q', NULL, 'S', NOW(), '0');
INSERT INTO `c_route` (app_id, service_id, order_type_cd, invoke_limit_times, invoke_model, create_time, status_cd)
VALUES ('8000418004', '9820260827000002', 'Q', NULL, 'S', NOW(), '0');

-- 3. 前端 URL 变更后同步 p_privilege.resource
UPDATE p_privilege SET resource = '/app/cache.queryCache' WHERE p_id = '502021111540930062';  -- 缓存查询
UPDATE p_privilege SET resource = '/app/cache.flushCache' WHERE p_id = '502021111510430063';  -- 刷新缓存
```

## 2026-08-27 车辆费用修改为车位费用
```sql
UPDATE `m_menu`
SET `name` = '车位收费',
    `url` = '/#/pages/property/parkingSpaceCreateFee'
WHERE `m_id` = '702020013091330005';

-- 2. m_menu_user：菜单名与 url 同步修改（两条）
UPDATE `m_menu_user`
SET `name` = '车位收费',
    `url` = '/#/pages/property/parkingSpaceCreateFee'
WHERE `mu_id` IN ('102026061600146693', '102026061699016707');

-- 3. m_menu_org：url 统一改为新地址（菜单名已是"车位收费"）
UPDATE `m_menu_org`
SET `url` = '/#/pages/property/parkingSpaceCreateFee'
WHERE `m_id` = '702020013091330005';

-- 4. sys_document：帮助文档地址与标题同步修改
UPDATE `sys_document`
SET `doc_code` = '/pages/property/parkingSpaceCreateFee',
    `doc_title` = '车位收费'
WHERE `doc_id` = '8620210101192317775';

```

## 2026-08-28 车位收费页面体验对齐房屋收费
```sql
-- 1. c_service 注册新接口（查询有主车位的停车场及停车区域，SQL exists 过滤无 S/H 车位的停车场/区域）
INSERT INTO `c_service` VALUES ('2095', '982026082700000009', 'parkingArea.queryParkingAreaAndSectionsHasOwner', 'API', '查询有主车位的停车场及停车区域', '1', NULL, 'CMD', 'http://community-service', 'GET', '60', '3', '8000418002', '2026-08-28 09:00:00', '0');
INSERT INTO `c_route` VALUES ('3081', '8000418004', '982026082700000009', 'Q', '1000', 'S', '2026-08-28 09:00:00', '0');
INSERT INTO `c_service` (`id`, `service_id`, `service_code`, `business_type_cd`, `name`, `seq`, `messageQueueName`, `is_instance`, `url`, `method`, `timeout`, `retry_count`, `provide_app_id`, `create_time`, `status_cd`)
VALUES ('2096', '982026082800000001', 'reportFeeMonthStatistics.queryReportFeeDetailParkingSpace', 'API', '查询车位费用明细', '1', NULL, 'CMD', 'http://report-service', 'GET', '60', '3', '8000418002', '2026-08-28 10:00:00', '0');

INSERT INTO `c_route` (`id`, `app_id`, `service_id`, `order_type_cd`, `invoke_limit_times`, `invoke_model`, `create_time`, `status_cd`)
VALUES ('3082', '8000418004', '982026082800000001', 'Q', '1000', 'S', '2026-08-28 10:00:00', '0');
```

## 2026-08-31 移除系统中 FTP 相关内容
```sql
-- 1. 修正 file_rel / business_file_rel 表 save_way 字段注释（ftp → s3）
ALTER TABLE `file_rel` MODIFY COLUMN `save_way` varchar(12) NOT NULL COMMENT '存放方式，s3 table 具体查看t_dict表';
ALTER TABLE `business_file_rel` MODIFY COLUMN `save_way` varchar(12) NOT NULL COMMENT '存放方式，s3 table 具体查看t_dict表';

-- 2. 删除 FTP 遗留六表（无任何业务引用，存量库中若无数据可一并 DROP）
DROP TABLE IF EXISTS `hc_ftp_tasklog_detail`;
DROP TABLE IF EXISTS `hc_ftp_tasklog`;
DROP TABLE IF EXISTS `hc_ftp_task_attr`;
DROP TABLE IF EXISTS `hc_ftp_task`;
DROP TABLE IF EXISTS `hc_ftp_log`;
DROP TABLE IF EXISTS `hc_ftp_item_spec`;

-- 3. t_dict 中 save_way 字典项 ftp → s3
UPDATE `t_dict` SET `status_cd` = 's3', `name` = 's3对象存储', `description` = 's3对象存储' WHERE `id` = '73';

-- 4. 修正 c_mapping_domain FILE_DOMAIN 存储配置说明（去除 ftp 描述）
UPDATE `c_mapping_domain` SET `description` = '系统存储配置，IMG_PATH 配置，如果是S3 时 则配置Nginx代理S3 静态文件访问地址，如果是阿里云OSS 配置 ，配置阿里云OSS图片访问地址，可以登录OSS在后台查看，OSS_SWITCH 为文件存储方式 OSS 为S3对象存储（MinIO） COS 为腾讯云COS存储 OSS 为阿里云OSS存储 需要阿里云购买OSS' WHERE `domain` = 'FILE_DOMAIN';

-- 5. 定时任务菜单 URL 去除 FTP 遗留命名
UPDATE `m_menu_org` SET `url` = '/jobIndex/' WHERE `m_id` = '702019090293430003';

## 2026-09-03 优惠申请（电话申请）改造：支持多费用勾选 + 批量缴费自动打折

### 1. 新增申请-费用关联表 apply_room_discount_fee（TT.sql 已同步）

```sql
DROP TABLE IF EXISTS `apply_room_discount_fee`;
CREATE TABLE `apply_room_discount_fee` (
  `ard_fee_id` varchar(30) NOT NULL COMMENT '主键ID',
  `ard_id` varchar(30) NOT NULL COMMENT '优惠申请ID',
  `fee_id` varchar(30) NOT NULL COMMENT '费用ID',
  `fee_name` varchar(128) DEFAULT NULL COMMENT '费用名称（冗余存储，取自 fee_config.fee_name）',
  `community_id` varchar(30) NOT NULL COMMENT '小区ID',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `status_cd` varchar(2) NOT NULL DEFAULT '0' COMMENT '数据状态0正常1失效',
  PRIMARY KEY (`ard_fee_id`),
  KEY `idx_ard_id` (`ard_id`),
  KEY `idx_fee_id` (`fee_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
```

### 2. 主表 apply_room_discount 删除 fee_id 列（存量库升级需按顺序执行 ①②）

```sql
-- ① 迁移历史数据（存在 fee_id 的旧申请写入子表，fee_name 联查 fee_config 回填）
INSERT INTO apply_room_discount_fee(ard_fee_id, ard_id, fee_id, fee_name, community_id, create_time, status_cd)
SELECT CONCAT('F', REPLACE(UUID(), '-', ''), FLOOR(RAND()*10)), t.ard_id, t.fee_id, fc.fee_name, t.community_id, now(), '0'
FROM apply_room_discount t
LEFT JOIN pay_fee pf ON pf.fee_id = t.fee_id AND pf.status_cd = '0'
LEFT JOIN fee_config fc ON fc.config_id = pf.config_id AND fc.status_cd = '0'
WHERE t.fee_id IS NOT NULL AND t.fee_id != '';

-- ② 删除主表字段
ALTER TABLE `apply_room_discount` DROP COLUMN `fee_id`;
```

> 说明：新库（TT.sql 重建）中 apply_room_discount 已不含 fee_id 列，仅存量库需要执行上述迁移；申请时间由前端默认传值（startTime=当前时间、endTime=2050-01-01 23:59:59，长期有效），批量缴费与单费用缴费命中口径统一为 state='4' and in_use='0' and end_time >= 当前时间 and feeId 命中子表。

## 2026-09-03 — 修复：批量缴费优惠申请折扣同 discountId 重复展示与金额重复计算

### 代码变更（无 DDL）

`service-fee/src/main/java/com/java110/fee/bill/ComputeFeeDiscountImpl.java` 的 `computeApplyRoomDiscount()`：

- 同一优惠申请（discountId）命中多个缴费费用时，原先返回多条重复折扣记录，导致前端折扣面板重复展示、勾选错乱、应付金额重复累加（翻倍）。
- 现按 discountId 合并：discountPrice 使用 BigDecimal 累加（保留首条记录的其余字段），接口只返回一条折扣行。
- 影响链路：`feeDiscount.computeFeeDiscount`（批量缴费页展示）与 `BatchPayFeeImpl.computeFeesDiscount()`（缴费重算）共用 `compute()`，自动受益；合并前后各费用减免总额不变，不影响实际扣减。

## 2026-09-03 — 修复：房屋缴费历史界面缺失 pay_fee_detail 金额字段

### 根因

service-fee cmd `fee.queryFeeDetail`（`service-fee/.../cmd/fee/QueryFeeDetailCmd.java:103`）调用 `BeanConvertUtil.covertBeanList(feeDetailList, ApiFeeDetailDataVo.class)`，目标 VO 类 `ApiFeeDetailDataVo` 缺少 `acctAmount / discountAmount / deductionAmount / lateAmount / giftAmount / payableAmount / feeTime / feeStartTime / feeEndTime / importFeeName / bId / configId / payerObjType` 共 13 个字段，导致响应 JSON 不含上述字段。`BeanConvertUtil.objectFieldsPutMap()` 按目标类的字段反射迭代拷贝，目标类未声明的字段被丢弃。

数据库 / SQL / DTO / Mapper 均无需改动（`docs/db/TT.sql:30367-30398`、`java110-db/.../fee/FeeDetailServiceDaoImplMapper.xml:114-129`、`java110-bean/.../dto/fee/FeeDetailDto.java`、`java110-bean/.../po/fee/PayFeeDetailPo.java` 已含全部字段），仅 VO 类缺失。

### 代码变更

- `java110-bean/src/main/java/com/java110/vo/api/ApiFeeDetailDataVo.java`：新增 13 个字段及 getter/setter，字段顺序与 `FeeDetailDto` 对齐；`fee.queryFeeDetail` 与 `fee.queryAdminFeeDetail` 两个 Cmd 自动受益，无需改动。
- `pc-web/src/components/fee/simplifyHisFee.vue`：表格列改造
  - "应收/实收"列移除 `v-if="scope.row.acctAmount > 0"` 判断（账户抵扣挪到独立列）。
  - 在"应收/实收"列后新增 5 个金额列：账户抵扣 `acctAmount`、打折金额 `discountAmount`（保留 `payFeeDetailDiscountDtoList` 子项）、减免金额 `deductionAmount`、滞纳金 `lateAmount`、赠送金额 `giftAmount`。
  - 全部金额使用 `|| '-'` 兜底（禁止 `?.` 可选链 / `??` 空值合并）。
- `pc-web/src/views/fee/roomCreateFeeLang.js`：i18n 三语（en/zh/zh-TW）的 `simplifyHisFee` 子段新增 6 个键：`acctAmount` / `discountAmount` / `deductionAmount` / `lateAmount` / `giftAmount` / `discountDetail`。
- `CHANGE_LOG.md`：登记本次根因定位与变更内容。

### 数据库变更

无。
```

## 2026-09-08 — 发票抬头统一由商城管理（物业端下线 owner_invoice）

### 背景

物业端发票抬头原先由本地 `owner_invoice` 表独立维护，与商城（MicroCommunityMall）/物联网（MicroCommunityIot）三套数据各自独立。本次改造：抬头统一由商城 `invoice_heading` 表管理（user_phone 归属维度），物业端删除本地表与逻辑，全部转发商城接口。

### 数据库变更

```sql
-- 1) 物业库下线 owner_invoice（存量库先迁移后执行, 迁移脚本见 docs/sql/invoice/owner_invoice_to_mall_heading.sql）
-- RENAME TABLE `owner_invoice` TO `owner_invoice_bak`;
-- DROP TABLE IF EXISTS `owner_invoice`;  (新库无需执行, TT.sql 已注释下线)

-- 2) invoice_apply 增加抬头快照列(列表/详情展示用, 数据来自商城抬头同步)
ALTER TABLE `invoice_apply`
  ADD COLUMN `invoice_name` varchar(64) DEFAULT NULL COMMENT '发票名头(抬头快照)' AFTER `invoice_type`,
  ADD COLUMN `invoice_num` varchar(64) DEFAULT NULL COMMENT '纳税人识别号(抬头快照)' AFTER `invoice_name`,
  ADD COLUMN `invoice_address` varchar(64) DEFAULT NULL COMMENT '地址(抬头快照)' AFTER `invoice_num`;
```

> 商城库（hc_mall）变更见商城项目 `docs/sql/invoice/invoice_ddl.sql`：`invoice_heading` 增加 address/bank_no/remark 列，并新增 openapi BMO `listInvoiceHeadingsBmoImpl` / `deleteInvoiceHeadingBmoImpl` / `setInvoiceHeadingDefaultBmoImpl`。

### 代码变更（MicroCommunityV2）

- **删除（owner_invoice 全套下线）**：`OwnerInvoiceDto`、`OwnerInvoicePo`、`IOwnerInvoiceV1InnerServiceSMO`、`IOwnerInvoiceV1ServiceDao`、`OwnerInvoiceV1ServiceDaoImpl`、`OwnerInvoiceV1InnerServiceSMOImpl`、`SaveOwnerInvoiceCmd`、`UpdateOwnerInvoiceCmd`、`DeleteOwnerInvoiceCmd`、`ListOwnerInvoiceCmd`、`OwnerInvoiceV1ServiceDaoImplMapper.xml`。
- **`InvoiceApplyDto`**：由 `extends OwnerInvoiceDto` 改为 `extends PageDto`，补齐 invoiceName/invoiceNum/invoiceAddress/invoiceLink/invoiceAccount/ownerId 自用字段。
- **`InvoiceApplyPo` / `InvoiceApplyV1ServiceDaoImplMapper.xml`**：新增 invoiceName/invoiceNum/invoiceAddress 快照列读写；查询去掉 `left join owner_invoice`。
- **`SaveInvoiceApplyCmd`**：抬头来源改商城——去掉 oiId 必填，改为断言 invoiceType/invoiceName；`applyTel` 取业主档案 link；保存申请后同步抬头到商城（saveInvoiceHeadingBmoImpl，isDefault=0 不抢占默认），失败不阻断开票申请。
- **新增 5 个转发 CMD（service-acct/cmd/invoice/）**：
  - `QueryInvoiceHeadingsCmd`（invoice.queryInvoiceHeadings → listInvoiceHeadingsBmoImpl，按 userPhone 分页+关键字）
  - `SaveInvoiceHeadingCmd`（invoice.saveInvoiceHeading → saveInvoiceHeadingBmoImpl，upsert；类型 1001/2002→个人/公司；userPhone 优先取业主 link）
  - `DeleteInvoiceHeadingCmd`（invoice.deleteInvoiceHeading → deleteInvoiceHeadingBmoImpl）
  - `SetInvoiceHeadingDefaultCmd`（invoice.setInvoiceHeadingDefault → setInvoiceHeadingDefaultBmoImpl）
  - `GetMyInvoiceHeadingCmd`（invoice.getMyInvoiceHeading → queryInvoiceHeadingBmoImpl 单条默认，对齐 IoT）

### 前端变更（pc-web）

- `api/fee/ownerInvoiceApi.js`：URL 改 invoice.queryInvoiceHeadings / invoice.saveInvoiceHeading / invoice.deleteInvoiceHeading / invoice.setInvoiceHeadingDefault，参数改商城字段。
- `api/fee/addOwnerInvoiceApi.js`：URL 改 invoice.saveInvoiceHeading。
- `api/fee/ownerApplyInvoiceApi.js`：saveInvoiceApply 参数增加抬头字段；新增 getMyInvoiceHeading。
- `views/fee/ownerInvoiceList.vue`：按 userPhone/关键字查询商城抬头；字段列改商城字段（headingType/headingName/taxNo/address/phone/bankNo/remark/isDefault）；新增"设为默认"。
- `views/fee/addOwnerInvoiceList.vue`：表单字段改商城（headingType/headingName/taxNo/address/phone/userPhone/email/bankNo/remark），选择业主自动带出手机号。
- `components/fee/editOwnerInvoice.vue`：改调 invoice.saveInvoiceHeading（upsert）。
- `components/fee/deleteOwnerInvoice.vue`：改调 invoice.deleteInvoiceHeading。
- `components/fee/searchOwnerInvoice.vue`：按业主手机号/关键字查商城抬头，选择结果映射回物业字段。
- `views/fee/ownerApplyInvoiceList.vue`：选择抬头后携带抬头字段提交；未选抬头时按业主手机号自动带出默认抬头。
- `views/fee/ownerInvoiceLang.js` / `ownerApplyInvoiceLang.js`：新增三语键。

### 前端变更（WechatOwnerServiceV2 业主端）

- `api/url.js`：invoice.listOwnerInvoice/saveOwnerInvoice/updateOwnerInvoice 改为 invoice.queryInvoiceHeadings / invoice.saveInvoiceHeading / invoice.getMyInvoiceHeading。
- `api/invoice/invoiceApi.js`：新增 getMyInvoiceHeading；getOwnerInvoice 返回值调整。
- `pages/invoice/invoiceSetting.vue`：按业主手机号（ownerId）查询商城默认抬头回显；保存调 saveInvoiceHeading。
- `pages/invoice/addInvoiceApply.vue`：开票申请按手机号带出商城默认抬头；提交传抬头字段。

### 说明

- MALL_SWITCH 未开启时：抬头查询/保存接口返回"商城系统未部署"，开票申请按现有 IssueMallInvoiceBmoImpl 失败回写逻辑处理，不影响其他业务。
- 存量数据迁移脚本（两种方式：导入导出工具/同库中转表）见 `docs/sql/invoice/owner_invoice_to_mall_heading.sql`。
- 服务编码兼容：原 invoice.listOwnerInvoice/saveOwnerInvoice/updateOwnerInvoice/deleteOwnerInvoice 已下线，pc-web 与 WechatOwnerService 需同版本发布。
## 2026-09-08 — 物业端开票页面改造（去掉发票抬头管理页，按房屋开票）

### 背景

原“发票抬头管理”页（ownerInvoice）下线，抬头统一在开票时按业主手机号从商城 invoice_heading 取/填（有则带回、无则填写，提交时 upsert 到商城）；开票入口收敛为“开票”（原发票申请列表），开票流程重做：选择房屋 → 自动带出业主 → 带出未开票缴费记录（单选一条）→ 开票。

### 代码变更（MicroCommunityV2）

- **service-user 新增 `QueryOwnerByRoomCmd`**（serviceCode `owner.queryOwnerByRoom`，GET）：按 roomId/communityId 查询户主列表，复用 `IOwnerInnerServiceSMO.queryOwnersByRoom`（join building_room/building_owner_room_rel/building_owner，owner_type_cd=1001），返回 memberId/ownerId/name/link/roomNum/communityId。
- **service-acct `SaveInvoiceApplyCmd`**：`computeFeeDetail` 增加单选校验（detailIds 超过 1 条抛 `CmdException("当前仅支持单条开票")`），保持“仅缴费开票”FEE 分支，arIds 账户分支保留不启用。
- **服务路由注册**（c_service/c_route，app 8000418004，见 docs/sql/invoice/register_invoice_service.sql 与 TT.sql）：
  - `owner.queryOwnerByRoom`（user-service）
  - `invoice.queryInvoiceHeadings` / `invoice.saveInvoiceHeading` / `invoice.deleteInvoiceHeading` / `invoice.setInvoiceHeadingDefault` / `invoice.getMyInvoiceHeading`（acct-service，父方案新增 CMD 补注册）

### 前端变更（pc-web）

- **删除（发票抬头管理下线）**：`views/fee/ownerInvoiceList.vue`、`views/fee/addOwnerInvoiceList.vue`、`components/fee/editOwnerInvoice.vue`、`components/fee/deleteOwnerInvoice.vue`、`api/fee/ownerInvoiceApi.js`、`api/fee/addOwnerInvoiceApi.js`、`views/fee/ownerInvoiceLang.js`；`router/index.js` 删除 `/pages/fee/ownerInvoice`、`/views/fee/addOwnerInvoice` 路由；`i18n/index.js` 移除 ownerInvoiceMessages 注册。
- **`views/fee/ownerApplyInvoiceList.vue` 重做**：顶部 searchRoom 弹窗选房屋 → `owner.queryOwnerByRoom` 带出业主（下拉单选）→ 按业主 link 查商城抬头（有则默认回填+可选“选择已有抬头”，无则表单填写）→ 自动带出未开票缴费记录（`fee.queryFeeDetail` openInvoice=N，radio 单选一条）→ 提交 `invoice.saveInvoiceApply`（detailIds 单条）。
- **`components/fee/searchOwnerInvoice.vue` 改造**：去掉选业主入口，改为按业主手机号查商城抬头列表选择（`open(userPhone)` 打开，选择后 `@select` 映射物业字段回填）。
- **`api/fee/ownerApplyInvoiceApi.js`**：新增 `queryOwnerByRoom`；移除 listAccountReceipt/getMyInvoiceHeading；参数对齐（仅 detailIds 提交）。
- **`views/fee/invoiceApplyLang.js`**：title/apply 改“开票”。
- **`views/fee/ownerApplyInvoiceLang.js`**：重写开票流程三语文案。
- 业主无手机号：页面提示“业主无手机号，无法获取抬头，请联系业主完善”，禁止提交。

### 数据库脚本

- `docs/sql/invoice/delete_owner_invoice_menu.sql`：删除 m_menu `702023100962610004`（发票抬头）、p_privilege `502023100906390005`、p_privilege_rel、m_menu_user 及旧抬头 c_service 路由；“发票”菜单更名“开票”（702023100951520002 / 502023100942720003）。
- `docs/sql/invoice/register_invoice_service.sql`：注册 owner.queryOwnerByRoom 与 5 个 invoice.* 抬头接口路由（存量库执行；新库已合入 TT.sql）。
- `docs/db/TT.sql`：同步 m_menu/p_privilege/m_menu_user 更名与下线注释、c_service/c_route 新路由注册。

### 注意事项

- 预存款/账户开票本期不做：前端不传 arIds，后端 computeAcctDetail 分支保留但不启用。
- 开票成功/失败回写 open_invoice（D/Y/N）沿用现有逻辑，不改动。
