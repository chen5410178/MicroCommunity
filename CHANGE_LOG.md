# 变更日志

## 2026-07-31 — 新增：账单手工生成功能

### DDL：新增两张表

### c_service / c_route 注册（6 个接口）

```sql
-- c_service 注册
INSERT INTO `c_service` VALUES ('2078', '982026073100000001', 'fee.searchManualBillTargets', 'API', '查询手工账单生成目标', '1', null, 'CMD', 'http://fee-service', 'GET', '60', '3', '8000418002', now(), '0');
INSERT INTO `c_service` VALUES ('2079', '982026073100000002', 'fee.saveManualBillApplication', 'API', '提交手工账单生成申请', '1', null, 'CMD', 'http://fee-service', 'POST', '60', '3', '8000418002', now(), '0');
INSERT INTO `c_service` VALUES ('2080', '982026073100000003', 'fee.listManualBillApplications', 'API', '查询手工账单申请列表', '1', null, 'CMD', 'http://fee-service', 'GET', '60', '3', '8000418002', now(), '0');
INSERT INTO `c_service` VALUES ('2081', '982026073100000004', 'fee.getManualBillApplication', 'API', '查询手工账单申请详情', '1', null, 'CMD', 'http://fee-service', 'GET', '60', '3', '8000418002', now(), '0');
INSERT INTO `c_service` VALUES ('2082', '982026073100000005', 'fee.auditManualBillApplication', 'API', '审核手工账单生成申请', '1', null, 'CMD', 'http://fee-service', 'POST', '60', '3', '8000418002', now(), '0');
INSERT INTO `c_service` VALUES ('2083', '982026073100000006', 'fee.deleteManualBillApplication', 'API', '删除手工账单申请', '1', null, 'CMD', 'http://fee-service', 'POST', '60', '3', '8000418002', now(), '0');

-- c_route 注册（PC 端 app_id = 8000418004）
INSERT INTO `c_route` VALUES ('3064', '8000418004', '982026073100000001', 'Q', '1000', 'S', now(), '0');
INSERT INTO `c_route` VALUES ('3065', '8000418004', '982026073100000002', 'D', '1000', 'S', now(), '0');
INSERT INTO `c_route` VALUES ('3066', '8000418004', '982026073100000003', 'Q', '1000', 'S', now(), '0');
INSERT INTO `c_route` VALUES ('3067', '8000418004', '982026073100000004', 'Q', '1000', 'S', now(), '0');
INSERT INTO `c_route` VALUES ('3068', '8000418004', '982026073100000005', 'D', '1000', 'S', now(), '0');
INSERT INTO `c_route` VALUES ('3069', '8000418004', '982026073100000006', 'D', '1000', 'S', now(), '0');
```

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
```
