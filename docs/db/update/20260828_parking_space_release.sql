-- ----------------------------
-- c_service 服务注册（释放车位 + 修改车辆）
-- ----------------------------
INSERT INTO `c_service` VALUES ('2090', '982026082700000006', 'parkingSpace.releaseParkingSpace', 'API', '释放车位', '1', NULL, 'CMD', 'http://community-service', 'POST', '60', '3', '8000418002', '2026-08-28 09:00:00', '0');
INSERT INTO `c_service` VALUES ('2091', '982026082700000007', 'parkingSpace.updateParkingSpaceCar', 'API', '修改车位车辆', '1', NULL, 'CMD', 'http://community-service', 'POST', '60', '3', '8000418002', '2026-08-28 09:00:00', '0');

-- ----------------------------
-- c_route 路由配置（appId 8000418004 管理端）
-- ----------------------------
INSERT INTO `c_route` VALUES ('3076', '8000418004', '982026082700000006', 'D', '1000', 'S', '2026-08-28 09:00:00', '0');
INSERT INTO `c_route` VALUES ('3077', '8000418004', '982026082700000007', 'D', '1000', 'S', '2026-08-28 09:00:00', '0');
