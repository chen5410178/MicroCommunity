-- ============================================================
-- 开票前置配置自检(上线前 / 故障定位)
-- 创建时间: 2026-09-15 12:29:38
-- 说明: 商城为开票单落库方、审核开关唯一仲裁者与开票执行者, 以下检查项须先满足,
--       否则物业端提交后商城单会落 FAIL(可在商城开票单列表按 FAIL 页签查看失败原因)
-- ============================================================

-- ================= 物业库(TT) =================

-- ① 商城开关: 须为 ON, 否则物业端提交直接失败(state=F, MALL_SWITCH 未开启)
SELECT `domain`, `key`, `value`
FROM `c_mapping`
WHERE `domain` = 'MALL' AND `key` = 'MALL_SWITCH' AND `status_cd` = '0';

-- ② 商城地址配置(URL_DOMAIN.MALL_URL)须可访问
SELECT `domain`, `key`, `value`
FROM `c_mapping`
WHERE `key` = 'MALL_URL' AND `status_cd` = '0';

-- ③ 物业申请单与商城开票单对应关系(oi_id 应等于商城 invoice_id)
--    参数: applyId
-- SELECT `apply_id`, `oi_id`, `state`, `invoice_code`, `remark`
-- FROM `invoice_apply`
-- WHERE `apply_id` = '@applyId';

-- ④ 缴费明细开票状态(Y 已开票 / N 未开票 / D 开票中)
-- SELECT `open_invoice`, COUNT(1) FROM `pay_fee_detail` GROUP BY `open_invoice`;

-- ================= 商城库 =================

-- ⑤ 审核开关: 1=商城审核(开票单置 AUDIT, 审核通过后开票); 0=不审核(直接 WAIT 并开票)
-- SELECT * FROM `invoice_setting` WHERE `setting_id` = 'global';

-- ⑥ 小区销方: 每个开票小区(=组织)须有 state='1' 的有效销方
-- 参数: communityId
-- SELECT * FROM `invoice_seller` WHERE `org_id` = '@communityId' AND `org_type` = 'C' AND `state` = '1';

-- ⑦ 开票平台(诺诺)参数是否齐备
-- SELECT * FROM `invoice_platform`;

-- ⑧ 类目映射: 建议按费用项配置(未命中时用 invoice_setting.default_tax_rate 兜底税率)
-- 参数: communityId
-- SELECT * FROM `invoice_category_map` WHERE `org_id` = '@communityId' AND `org_type` = 'C' AND `state` = '1';

-- ⑨ 商城开票单与物业申请单对应(remark 形如 applyId|ownerName|bizCategory, source='WEB')
-- 参数: applyId
-- SELECT `invoice_id`, `org_id`, `state`, `invoice_no`, `pdf_url`, `fail_reason`, `source`, `remark`
-- FROM `invoice_order`
-- WHERE `source` = 'WEB' AND `remark` LIKE CONCAT('@applyId', '%');

-- ⑩ 商城开票单状态分布(FAIL 页签对应 state='FAIL', AUDIT 对应商城审核页签)
-- SELECT `state`, `source`, COUNT(1) FROM `invoice_order` GROUP BY `state`, `source`;
