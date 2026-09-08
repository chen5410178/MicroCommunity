-- ============================================================
-- 物业端按房屋开票: 新增服务路由注册(2026-09-08, 2026-09-09 精简)
-- 执行环境: 物业库(TT)
-- 说明: 抬头保存/更新由 invoice.saveInvoiceApply 内部 syncInvoiceHeadingToMall
--       统一同步商城, 仅保留查询接口 invoice.queryInvoiceHeadings;
--       其余冗余接口(owner.queryOwnerByRoom / saveInvoiceHeading /
--       deleteInvoiceHeading / setInvoiceHeadingDefault / getMyInvoiceHeading)
--       已于 2026-09-09 撤销注册并删除对应 CMD, 非本次注册范围
-- 执行后需刷新服务路由缓存(AppRouteCache)或重启 service-api
-- ============================================================

-- ① 服务定义 c_service
INSERT INTO `c_service` VALUES ('2098', '982026090800000002', 'invoice.queryInvoiceHeadings', 'API', '查询发票抬头(商城)', '1', null, 'CMD', 'http://acct-service', 'GET', '60', '3', '8000418002', '2026-09-08 15:00:00', '0');

-- ② 路由关系 c_route(物业端 app 8000418004)
INSERT INTO `c_route` VALUES ('3084', '8000418004', '982026090800000002', 'Q', '1000', 'S', '2026-09-08 15:00:00', '0');
