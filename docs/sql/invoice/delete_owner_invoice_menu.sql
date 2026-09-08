-- ============================================================
-- 物业端发票抬头管理下线(2026-09-08)
-- 删除发票抬头菜单/权限/员工菜单、旧抬头服务路由; "发票"菜单更名"开票"
-- 执行环境: 物业库(TT)
-- 前置: 商城 invoice_heading 抬头已迁移完成(见 owner_invoice_to_mall_heading.sql)
-- ============================================================

-- ① 旧抬头服务路由 invoice.deleteOwnerInvoice / invoice.listOwnerInvoice / invoice.saveOwnerInvoice / invoice.updateOwnerInvoice
-- (CMD 已下线, 防止安装包残留路由)
DELETE FROM `c_service` WHERE `service_code` IN (
  'invoice.deleteOwnerInvoice',
  'invoice.listOwnerInvoice',
  'invoice.saveOwnerInvoice',
  'invoice.updateOwnerInvoice'
);

-- ② 删除权限组关系 p_privilege_rel (权限ID 502023100906390005)
DELETE FROM `p_privilege_rel` WHERE `p_id` = '502023100906390005';

-- ③ 删除权限 p_privilege (权限ID 502023100906390005)
DELETE FROM `p_privilege` WHERE `p_id` = '502023100906390005';

-- ④ 删除员工自定义菜单 m_menu_user (菜单ID 702023100962610004)
DELETE FROM `m_menu_user` WHERE `m_id` = '702023100962610004';

-- ⑤ 删除菜单 m_menu (菜单ID 702023100962610004)
DELETE FROM `m_menu` WHERE `m_id` = '702023100962610004';

-- ⑥ 发票申请列表菜单更名"开票"(入口收敛为单一"开票")
UPDATE `m_menu` SET `name` = '开票' WHERE `m_id` = '702023100951520002';
UPDATE `p_privilege` SET `name` = '开票' WHERE `p_id` = '502023100942720003';
UPDATE `m_menu_user` SET `name` = '开票' WHERE `m_id` = '702023100951520002';
