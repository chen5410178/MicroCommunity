-- 发票申请开票方式小区设置(INVOICE_AUTO_SWITCH)
-- 已废弃(2026-09-15): 审核开关统一由商城 invoice_setting.audit_switch 决定,
--   物业端不再读取本设置、不再自行判定是否开票;
--   保留数据行仅为兼容历史脚本与历史记录, 新增小区无需再配置。
-- 历史用途: 物业端发票申请自动开票(调商城 propertyInvoiceBmoImpl) 与审核开票双模式
-- 执行于 hc_community 库(需与 community_setting_key 表一致)
INSERT INTO `community_setting_key`
(key_id, setting_type, setting_name, setting_key, remark, create_time, status_cd)
VALUES ('112026091557300001', '2002', '发票自动','INVOICE_AUTO_SWITCH',
        '发票申请开票方式: 填1-自动(申请后直接调商城开票,无需审核); 填2-审核(审核通过后开票); 默认1',
        NOW(), '0')
ON DUPLICATE KEY UPDATE remark = VALUES(remark);
