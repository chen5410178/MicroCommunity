-- ----------------------------
-- 停车区域表（参照 building_unit 单元表设计）
-- ----------------------------
DROP TABLE IF EXISTS `parking_area_section`;
CREATE TABLE `parking_area_section` (
  `section_id`  varchar(30) NOT NULL COMMENT '区域ID',
  `pa_id`       varchar(30) NOT NULL COMMENT '停车场ID',
  `community_id` varchar(30) NOT NULL COMMENT '小区ID',
  `section_num` varchar(12) NOT NULL COMMENT '区域编号',
  `section_name` varchar(64) DEFAULT NULL COMMENT '区域名称',
  `remark`      varchar(300) DEFAULT NULL COMMENT '备注',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `status_cd`   varchar(2) NOT NULL DEFAULT '0' COMMENT '数据状态 0在用 1失效',
  PRIMARY KEY (`section_id`),
  KEY `idx_pa_id` (`pa_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- 车位表扩展字段（均非必填）
-- ----------------------------
ALTER TABLE `parking_space`
  ADD COLUMN `owner_id`   varchar(30) DEFAULT NULL COMMENT '业主ID' AFTER `pa_id`,
  ADD COLUMN `owner_name` varchar(64) DEFAULT NULL COMMENT '业主名称' AFTER `owner_id`,
  ADD COLUMN `car_id`     varchar(30) DEFAULT NULL COMMENT '车辆ID' AFTER `owner_name`,
  ADD COLUMN `car_num`    varchar(12) DEFAULT NULL COMMENT '车牌号' AFTER `car_id`,
  ADD COLUMN `start_time` datetime DEFAULT NULL COMMENT '出租开始时间' AFTER `car_num`,
  ADD COLUMN `end_time`   datetime DEFAULT NULL COMMENT '出租结束时间' AFTER `start_time`,
  ADD COLUMN `section_id` varchar(30) DEFAULT NULL COMMENT '停车区域ID' AFTER `pa_id`;

-- ----------------------------
-- c_service 服务注册（停车区域 + 停车场-区域树接口）
-- ----------------------------
INSERT INTO `c_service` VALUES ('2085', '982026082700000001', 'parkingArea.saveParkingAreaSection', 'API', '添加停车区域', '1', NULL, 'CMD', 'http://community-service', 'POST', '60', '3', '8000418002', '2026-08-27 09:34:00', '0');
INSERT INTO `c_service` VALUES ('2086', '982026082700000002', 'parkingArea.updateParkingAreaSection', 'API', '修改停车区域', '1', NULL, 'CMD', 'http://community-service', 'POST', '60', '3', '8000418002', '2026-08-27 09:34:00', '0');
INSERT INTO `c_service` VALUES ('2087', '982026082700000003', 'parkingArea.deleteParkingAreaSection', 'API', '删除停车区域', '1', NULL, 'CMD', 'http://community-service', 'POST', '60', '3', '8000418002', '2026-08-27 09:34:00', '0');
INSERT INTO `c_service` VALUES ('2088', '982026082700000004', 'parkingArea.listParkingAreaSections', 'API', '查询停车区域', '1', NULL, 'CMD', 'http://community-service', 'GET', '60', '3', '8000418002', '2026-08-27 09:34:00', '0');
INSERT INTO `c_service` VALUES ('2089', '982026082700000005', 'parkingArea.queryParkingAreaAndSections', 'API', '查询停车场及停车区域', '1', NULL, 'CMD', 'http://community-service', 'GET', '60', '3', '8000418002', '2026-08-27 09:34:00', '0');

-- ----------------------------
-- c_route 路由配置（appId 8000418004 管理端）
-- ----------------------------
INSERT INTO `c_route` VALUES ('3071', '8000418004', '982026082700000001', 'D', '1000', 'S', '2026-08-27 09:34:00', '0');
INSERT INTO `c_route` VALUES ('3072', '8000418004', '982026082700000002', 'D', '1000', 'S', '2026-08-27 09:34:00', '0');
INSERT INTO `c_route` VALUES ('3073', '8000418004', '982026082700000003', 'D', '1000', 'S', '2026-08-27 09:34:00', '0');
INSERT INTO `c_route` VALUES ('3074', '8000418004', '982026082700000004', 'Q', '1000', 'S', '2026-08-27 09:34:00', '0');
INSERT INTO `c_route` VALUES ('3075', '8000418004', '982026082700000005', 'Q', '1000', 'S', '2026-08-27 09:34:00', '0');
