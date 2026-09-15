-- ============================================================
-- 物业端"同步状态"服务下线: 服务与路由注销(2026-09-15)
-- 创建时间: 2026-09-15 13:55:06
-- 执行环境: 物业库(TT)
-- 背景: 商城 invoice_order 为开票/审核状态唯一权威源, 物业端列表/详情每次查询按
--       applyId(商城 ext_apply_id) 实时取状态/失败原因/发票号/发票PDF, 不再有"同步状态"动作,
--       对应 CMD(SyncInvoiceApplyStateCmd) 与前端入口已删除, 服务注册一并注销
-- 下线服务:
--   2106 / 982026090800000010 invoice.syncInvoiceApplyState 同步商城开票状态
-- 保留服务:
--   2107 / 982026090800000011 invoice.reSubmitInvoiceApply 重新提交开票申请
-- 说明: TT.sql 基线的这两条 INSERT 已注释保留(下线仅注释不删), 本脚本用于已建库环境注销服务
-- 执行后需刷新服务路由缓存(AppRouteCache)或重启 service-api
-- ============================================================

-- ① 注销路由关系 c_route(先删路由, 再删服务)
DELETE FROM `c_route` WHERE `service_id` = '982026090800000010';

-- ② 注销服务定义 c_service
DELETE FROM `c_service` WHERE `service_id` = '982026090800000010';

-- ③ 校验: 应查询不到记录
-- SELECT * FROM `c_service` WHERE `service_id` = '982026090800000010';
-- SELECT * FROM `c_route` WHERE `service_id` = '982026090800000010';
