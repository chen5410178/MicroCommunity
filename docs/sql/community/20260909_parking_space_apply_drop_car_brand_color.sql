-- 2026-09-09 车位申请保存报错 car_brand cannot be null 修复
-- 方案：彻底移除 parking_space_apply 表中汽车品牌/颜色字段（代码层已同步删除）
-- 执行方式：先执行本 SQL 再发布后端，或存量库需先备份

ALTER TABLE `parking_space_apply`
    DROP COLUMN `car_brand`,
    DROP COLUMN `car_color`;
