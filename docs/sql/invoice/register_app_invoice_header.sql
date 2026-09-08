-- ============================================================
-- 业主端开票页抬头查询 App 专用接口注册(2026-09-08)
-- 执行环境: 物业库(TT)
-- 说明: invoice.queryAppInvoiceHeader 为手机端 App 专用查询命令,
--       手机号由后端从登录态 user-id 推导业主档案 link(不信任前端传参),
--       前端仅传 communityId/page/row; 通用接口 invoice.queryInvoiceHeadings
--       仍保留给 PC/物业端使用
-- 执行后需刷新服务路由缓存(AppRouteCache)或重启 service-api
-- ============================================================

-- ① 服务定义 c_service(服务归属于控制中心应用 8000418002)
INSERT INTO `c_service` VALUES ('2105', '982026090800000009', 'invoice.queryAppInvoiceHeader', 'API', '查询发票抬头(业主端)', '1', null, 'CMD', 'http://acct-service', 'GET', '60', '3', '8000418002', '2026-09-08 19:00:00', '0');

-- ② 路由关系 c_route(业主端公众号 app 992020061452450002, 与 queryAppOwnerCars 等 App 命令同 app)
INSERT INTO `c_route` VALUES ('3091', '992020061452450002', '982026090800000009', 'Q', '1000', 'S', '2026-09-08 19:00:00', '0');
