-- 车位申请增加停车场字段
-- 说明：申请车位时选择停车场（而非具体车位），审核通过不再强制选择车位
ALTER TABLE `parking_space_apply`
    ADD COLUMN `pa_id` varchar(30) DEFAULT NULL COMMENT '停车场ID' AFTER `ps_id`;
