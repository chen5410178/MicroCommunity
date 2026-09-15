-- ============================================================
-- 开票中心改造: invoice_apply 新增房屋维度(room_id / room_name)
-- 创建时间: 2026-09-15 16:38:26
-- 说明: 房屋信息仅存物业库 invoice_apply, 不进商城报文与商城库(商城契约不变);
--       room_name 为提交时快照(楼栋-单元-房号), 房屋后续更名不影响历史记录
-- ============================================================

-- ① 先核对(可选): 确认列不存在
-- SHOW COLUMNS FROM `invoice_apply` LIKE 'room%';

-- ② 加列 + 索引
ALTER TABLE `invoice_apply`
  ADD COLUMN `room_id` varchar(30) DEFAULT NULL COMMENT '房屋ID(选房开票时记录)' AFTER `community_id`,
  ADD COLUMN `room_name` varchar(64) DEFAULT NULL COMMENT '房屋编号(楼栋-单元-房号)' AFTER `room_id`,
  ADD KEY `idx_room_id` (`room_id`);

-- ③ 复核: 存量记录为 NULL(列表显示 '-'), 新提交记录应写入房屋
-- SELECT `apply_id`, `room_id`, `room_name`, `state` FROM `invoice_apply` ORDER BY `create_time` DESC LIMIT 20;

-- ④ 存量回填(P2, 可选, 人工核对后执行): 仅开票项为物业缴费(item_type='2002')的单据
-- UPDATE `invoice_apply` a
-- JOIN (
--   SELECT i.`apply_id`, MAX(pf.`payer_obj_id`) AS `room_id`, MAX(pf.`payer_obj_name`) AS `room_name`
--   FROM `invoice_apply_item` i
--   JOIN `pay_fee_detail` pfd ON pfd.`detail_id` = i.`item_obj_id`
--   JOIN `pay_fee` pf ON pf.`fee_id` = pfd.`fee_id`
--   WHERE i.`item_type` = '2002' AND i.`status_cd` = '0'
--   GROUP BY i.`apply_id`
-- ) x ON x.`apply_id` = a.`apply_id`
-- SET a.`room_id` = x.`room_id`, a.`room_name` = x.`room_name`
-- WHERE (a.`room_id` IS NULL OR a.`room_id` = '');
