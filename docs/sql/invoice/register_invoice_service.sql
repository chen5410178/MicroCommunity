-- ============================================================
-- 物业端按房屋开票: 新增服务路由注册(2026-09-08)
-- 执行环境: 物业库(TT)
-- 说明: ①owner.queryOwnerByRoom 按房屋查业主(本次新增)
--       ②invoice.queryInvoiceHeadings / saveInvoiceHeading / deleteInvoiceHeading /
--         setInvoiceHeadingDefault / getMyInvoiceHeading 抬头统一商城管理接口
-- 执行后需刷新服务路由缓存(AppRouteCache)或重启 service-api
-- ============================================================

-- ① 服务定义 c_service
INSERT INTO `c_service` VALUES ('2097', '982026090800000001', 'owner.queryOwnerByRoom', 'API', '按房屋查询业主', '1', null, 'CMD', 'http://user-service', 'GET', '60', '3', '8000418002', '2026-09-08 15:00:00', '0');
INSERT INTO `c_service` VALUES ('2098', '982026090800000002', 'invoice.queryInvoiceHeadings', 'API', '查询发票抬头(商城)', '1', null, 'CMD', 'http://acct-service', 'GET', '60', '3', '8000418002', '2026-09-08 15:00:00', '0');
INSERT INTO `c_service` VALUES ('2099', '982026090800000003', 'invoice.saveInvoiceHeading', 'API', '保存发票抬头(商城)', '1', null, 'CMD', 'http://acct-service', 'POST', '60', '3', '8000418002', '2026-09-08 15:00:00', '0');
INSERT INTO `c_service` VALUES ('2100', '982026090800000004', 'invoice.deleteInvoiceHeading', 'API', '删除发票抬头(商城)', '1', null, 'CMD', 'http://acct-service', 'POST', '60', '3', '8000418002', '2026-09-08 15:00:00', '0');
INSERT INTO `c_service` VALUES ('2101', '982026090800000005', 'invoice.setInvoiceHeadingDefault', 'API', '设置默认发票抬头(商城)', '1', null, 'CMD', 'http://acct-service', 'POST', '60', '3', '8000418002', '2026-09-08 15:00:00', '0');
INSERT INTO `c_service` VALUES ('2102', '982026090800000006', 'invoice.getMyInvoiceHeading', 'API', '查询默认发票抬头(商城)', '1', null, 'CMD', 'http://acct-service', 'GET', '60', '3', '8000418002', '2026-09-08 15:00:00', '0');

-- ② 路由关系 c_route(物业端 app 8000418004)
INSERT INTO `c_route` VALUES ('3083', '8000418004', '982026090800000001', 'Q', '1000', 'S', '2026-09-08 15:00:00', '0');
INSERT INTO `c_route` VALUES ('3084', '8000418004', '982026090800000002', 'Q', '1000', 'S', '2026-09-08 15:00:00', '0');
INSERT INTO `c_route` VALUES ('3085', '8000418004', '982026090800000003', 'Q', '1000', 'S', '2026-09-08 15:00:00', '0');
INSERT INTO `c_route` VALUES ('3086', '8000418004', '982026090800000004', 'Q', '1000', 'S', '2026-09-08 15:00:00', '0');
INSERT INTO `c_route` VALUES ('3087', '8000418004', '982026090800000005', 'Q', '1000', 'S', '2026-09-08 15:00:00', '0');
INSERT INTO `c_route` VALUES ('3088', '8000418004', '982026090800000006', 'Q', '1000', 'S', '2026-09-08 15:00:00', '0');
