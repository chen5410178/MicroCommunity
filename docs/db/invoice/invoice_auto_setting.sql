-- 发票申请开票方式小区设置(INVOICE_AUTO_SWITCH)
-- 用于物业端发票申请自动开票(调商城 propertyInvoiceBmoImpl) 与审核开票双模式
-- 执行于 hc_community 库(需与 community_setting_key 表一致)
INSERT INTO `community_setting_key`
(key_id, setting_type, setting_name, setting_key, remark, create_time, status_cd)
VALUES ('112026091557300001', '2002', '发票自动','INVOICE_AUTO_SWITCH',
        '发票申请开票方式: 填1-自动(申请后直接调商城开票,无需审核); 填2-审核(审核通过后开票); 默认1',
        NOW(), '0')
ON DUPLICATE KEY UPDATE remark = VALUES(remark);
