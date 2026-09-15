-- ============================================================
-- 物业端发票申请状态存量清洗 + 字典调整(一次性脚本)
-- 创建时间: 2026-09-15 12:29:38
-- 执行环境: 物业库(TT)
-- 背景: 物业端不再维护发票状态机(待审核W/待上传U/待领用G/已领用C),
--       仅保留"提交结果": S=已提交, F=提交失败(可重新提交);
--       审核/开票进度一律以商城 invoice_order.state 为准(只读展示)
-- 注意: t_dict.status_cd 存的是字典取值(与 invoice_apply.state 关联), 非启用标记;
--       如需下线历史取值只能删除对应字典行, 本脚本保留历史行以保证历史数据可读
-- ============================================================

-- ① 历史状态(W 待审核 / U 待上传 / G 待领用 / C 已领用)统一清洗为 S 已提交
UPDATE `invoice_apply` SET `state` = 'S' WHERE `state` IN ('W', 'U', 'G', 'C');

-- ② 字典: F 语义由"审核失败"调整为"提交失败"
UPDATE `t_dict` SET `name` = '提交失败', `description` = '提交失败'
WHERE `id` = '626307' AND `table_name` = 'invoice_apply' AND `table_columns` = 'state';

-- ③ 字典: 新增 S=已提交(不存在时插入)
INSERT INTO `t_dict` (`id`, `status_cd`, `name`, `description`, `create_time`, `table_name`, `table_columns`)
SELECT '626311', 'S', '已提交', '已提交', '2026-09-15 12:29:38', 'invoice_apply', 'state'
FROM DUAL
WHERE NOT EXISTS (
    SELECT 1 FROM `t_dict` WHERE `status_cd` = 'S' AND `table_name` = 'invoice_apply' AND `table_columns` = 'state'
);

-- ④ 校验
-- SELECT `state`, COUNT(1) FROM `invoice_apply` GROUP BY `state`;
-- SELECT * FROM `t_dict` WHERE `table_name` = 'invoice_apply' AND `table_columns` = 'state';
