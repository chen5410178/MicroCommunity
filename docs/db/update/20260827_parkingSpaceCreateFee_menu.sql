-- 车位收费页面改造：菜单地址与名称同步
-- 说明：原"车辆收费"页面 carCreateFeeList.vue 已重命名为 parkingSpaceCreateFeeList.vue，
--      路由地址 /pages/property/carCreateFee 改为 /pages/property/parkingSpaceCreateFee

-- 1. m_menu：菜单名"车辆收费"改"车位收费"，url 同步修改
UPDATE `m_menu`
SET `name` = '车位收费',
    `url` = '/#/pages/property/parkingSpaceCreateFee'
WHERE `m_id` = '702020013091330005';

-- 2. m_menu_user：菜单名与 url 同步修改（两条）
UPDATE `m_menu_user`
SET `name` = '车位收费',
    `url` = '/#/pages/property/parkingSpaceCreateFee'
WHERE `mu_id` IN ('102026061600146693', '102026061699016707');

-- 3. m_menu_org：url 统一改为新地址（菜单名已是"车位收费"）
UPDATE `m_menu_org`
SET `url` = '/#/pages/property/parkingSpaceCreateFee'
WHERE `m_id` = '702020013091330005';

-- 4. sys_document：帮助文档地址与标题同步修改
UPDATE `sys_document`
SET `doc_code` = '/pages/property/parkingSpaceCreateFee',
    `doc_title` = '车位收费'
WHERE `doc_id` = '8620210101192317775';
