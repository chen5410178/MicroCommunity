-- ============================================================
-- 物业端只提交开票申请: 状态同步 + 重新提交 服务路由注册
--                                  + 商城开票状态兜底同步定时任务注册
-- 创建时间: 2026-09-15 12:29:38
-- 执行环境: 物业库(TT)
-- 说明: 商城 invoice_order 为开票单唯一权威状态源;
--       物业端仅保留 "提交开票申请"(invoice.saveInvoiceApply)、
--       "只读同步商城状态"(invoice.syncInvoiceApplyState)、
--       "提交失败重新提交"(invoice.reSubmitInvoiceApply);
--       审核/上传发票/登记核销/领用/事件查询服务与前端入口已下线
--       (注销脚本见 unregister_invoice_action_service.sql)
-- 执行后需刷新服务路由缓存(AppRouteCache)或重启 service-api
-- ============================================================

-- ① 服务定义 c_service
INSERT INTO `c_service` VALUES ('2106', '982026090800000010', 'invoice.syncInvoiceApplyState', 'API', '同步商城开票状态', '1', null, 'CMD', 'http://acct-service', 'POST', '60', '3', '8000418002', '2026-09-15 12:29:38', '0');
INSERT INTO `c_service` VALUES ('2107', '982026090800000011', 'invoice.reSubmitInvoiceApply', 'API', '重新提交开票申请', '1', null, 'CMD', 'http://acct-service', 'POST', '60', '3', '8000418002', '2026-09-15 12:29:38', '0');

-- ② 路由关系 c_route(物业端 app 8000418004, D=POST)
INSERT INTO `c_route` VALUES ('3092', '8000418004', '982026090800000010', 'D', '1000', 'S', '2026-09-15 12:29:38', '0');
INSERT INTO `c_route` VALUES ('3093', '8000418004', '982026090800000011', 'D', '1000', 'S', '2026-09-15 12:29:38', '0');

-- ③ 定时兜底同步任务(service-job InvoiceStateSyncJob, 每 5 分钟)
--    任务模板 task_template.class_bean = InvoiceStateSyncJob 的 spring bean 名
INSERT INTO `task_template` VALUES ('1036', '商城开票状态同步', '兜底同步商城开票状态并回写缴费明细开票状态', '2026-09-15 12:29:38', '0', 'invoiceStateSyncJob');
INSERT INTO `task` VALUES ('522026091512293800', '-1', '商城开票状态同步', '1036', '0 */5 * * * ?', '002', '0', '2026-09-15 12:29:38');
