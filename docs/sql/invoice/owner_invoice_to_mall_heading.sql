-- ============================================================
-- 发票抬头统一由商城管理: 物业库 owner_invoice -> 商城 invoice_heading 存量迁移
-- 生成时间: 2026-09-08
-- 执行顺序: ①商城库执行(建中转表+导入数据) → ②商城库执行 INSERT 迁移 → ③物业库备份下线
-- 前提: 商城 invoice_heading 表已增加 address/bank_no/remark 列(见 invoice_ddl.sql)
-- ============================================================

-- ============ 方式一: 导入导出工具(推荐, 生产用) ============
-- 1) 物业库导出 owner_invoice 为 CSV/Excel, 映射规则:
--    heading_id     -> CONCAT('HD', REPLACE(UUID(), '-', '')) 生成
--    heading_type   -> invoice_type 1001->个人 2002->公司
--    heading_name   -> invoice_name
--    tax_no         -> invoice_num
--    phone          -> invoice_link (为空则取业主手机号)
--    user_phone     -> 业主手机号(building_owner.link, owner_id=member_id)
--    email          -> 留空(原表无此字段)
--    is_default     -> 每用户每类型仅1条置'1' 其余'0'
--    address        -> invoice_address
--    bank_no        -> invoice_account
--    remark         -> remark
--    status_cd      -> '0'
-- 2) 商城库通过导入工具(如 Navicat 数据同步)导入 invoice_heading。

-- ============ 方式二: 商城库建同构中转表(联调/测试用) ============
-- ① 商城库(hc_mall)创建中转表(结构同物业库 owner_invoice)
CREATE TABLE IF NOT EXISTS owner_invoice_tmp (
  oi_id           varchar(30) NOT NULL COMMENT '原表主键',
  owner_id        varchar(30) NOT NULL COMMENT '业主ID',
  owner_name      varchar(64) NOT NULL COMMENT '业主名称',
  invoice_type    varchar(12) NOT NULL COMMENT '发票类型 1001 个人 2002 企业',
  community_id    varchar(30) NOT NULL COMMENT '小区ID',
  invoice_name    varchar(64) NOT NULL COMMENT '发票名头',
  invoice_num     varchar(64) DEFAULT NULL COMMENT '纳税人识别号',
  invoice_address varchar(64) NOT NULL COMMENT '地址',
  invoice_link    varchar(20) DEFAULT NULL COMMENT '电话',
  invoice_account varchar(64) DEFAULT NULL COMMENT '开户行及账号',
  remark          varchar(200) DEFAULT NULL COMMENT '备注',
  create_time     timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  status_cd       varchar(2) NOT NULL DEFAULT '0',
  PRIMARY KEY (oi_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='owner_invoice迁移中转表';

-- ② 将物业库 owner_invoice 数据通过工具导入该中转表(或补充 INSERT ... SELECT 语句, 两库同机时可如下执行)
-- INSERT INTO hc_mall.owner_invoice_tmp
-- SELECT oi_id, owner_id, owner_name, invoice_type, community_id, invoice_name, invoice_num, invoice_address, invoice_link, invoice_account, remark, create_time, status_cd
-- FROM TT.owner_invoice WHERE status_cd = '0';

-- ③ 中转表 -> invoice_heading (字段映射 + user_phone取业主link + 同类型默认唯一)
INSERT INTO invoice_heading(heading_id, heading_type, heading_name, tax_no, phone, user_phone, email, is_default, address, bank_no, remark, status_cd, create_time)
SELECT
  CONCAT('HD', REPLACE(UUID(), '-', '')) AS heading_id,
  CASE t.invoice_type WHEN '1001' THEN '个人' WHEN '2002' THEN '公司' ELSE t.invoice_type END AS heading_type,
  t.invoice_name AS heading_name,
  t.invoice_num AS tax_no,
  IFNULL(NULLIF(t.invoice_link, ''), bo.link) AS phone,
  bo.link AS user_phone,
  NULL AS email,
  IF(rn.rn = 1, '1', '0') AS is_default,
  t.invoice_address AS address,
  t.invoice_account AS bank_no,
  t.remark AS remark,
  '0' AS status_cd,
  t.create_time AS create_time
FROM owner_invoice_tmp t
LEFT JOIN (SELECT member_id, link FROM building_owner WHERE link IS NOT NULL AND link != '') bo
  ON bo.member_id = t.owner_id
LEFT JOIN (
  SELECT oi_id,
         ROW_NUMBER() OVER (PARTITION BY IFNULL(bo2.link, '') , t2.invoice_type ORDER BY t2.create_time, t2.oi_id) AS rn
  FROM owner_invoice_tmp t2
  LEFT JOIN (SELECT member_id, link FROM building_owner WHERE link IS NOT NULL AND link != '') bo2
    ON bo2.member_id = t2.owner_id
) rn ON rn.oi_id = t.oi_id
WHERE t.status_cd = '0'
  AND (SELECT COUNT(1) FROM invoice_heading h WHERE h.user_phone = bo.link) = 0;

-- ④ 无业主手机号(link为空)的存量抬头无法归属, 单独排查(建议人工补录)
SELECT t.oi_id, t.owner_id, t.owner_name, t.invoice_name, t.invoice_address, t.invoice_link
FROM owner_invoice_tmp t
LEFT JOIN building_owner bo ON bo.member_id = t.owner_id
WHERE (bo.link IS NULL OR bo.link = '') AND t.status_cd = '0';

-- ============ ③物业库 owner_invoice 备份下线 ============
-- 确认迁移完整、排查记录已处理后, 在物业库执行:
-- RENAME TABLE `owner_invoice` TO `owner_invoice_bak`;

-- 清理中转表(可选)
-- DROP TABLE IF EXISTS owner_invoice_tmp;
