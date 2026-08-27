-- ----------------------------
-- 业主车辆去车位化改造：owner_car / business_owner_car 删除 car_brand、car_color、ps_id 三列
-- 同步修改 docs/db/TT.sql 中两张表的建表语句及 business_owner_car 记录插入语句
-- ----------------------------
ALTER TABLE `owner_car` DROP COLUMN `car_brand`, DROP COLUMN `car_color`, DROP COLUMN `ps_id`;
ALTER TABLE `business_owner_car` DROP COLUMN `car_brand`, DROP COLUMN `car_color`, DROP COLUMN `ps_id`;
