export default [
    {
        path: '/pages/property/communitySettingManage',
        meta: { title: '小区设置' },
        name: '/pages/property/communitySettingManage',
        component: () => import('@/views/system/communitySettingManageList.vue')
    },
    {
        path: '/pages/property/workflowManage',
        meta: { title: '工作流' },
        name: '/pages/property/workflowManage',
        component: () => import('@/views/system/workflowManageList.vue')
    },
    {
        path: '/pages/property/workflowSettingManage',
        meta: { title: '工作流设置' },
        name: '/pages/property/workflowSettingManage',
        component: () => import('@/views/system/workflowSettingManageList.vue')
    },
    {
        path: '/pages/common/storeInfoManage',
        meta: { title: '店铺信息' },
        name: '/pages/common/storeInfoManage',
        component: () => import('@/views/system/storeInfoManageList.vue')
    },
    {
        path: '/pages/property/publicWeChatManage',
        meta: { title: '公众号' },
        name: '/pages/property/publicWeChatManage',
        component: () => import('@/views/system/publicWeChatManageList.vue')
    },
    {
        path: '/pages/property/smallWeChatManage',
        meta: { title: '小程序' },
        name: '/pages/property/smallWeChatManage',
        component: () => import('@/views/system/smallWeChatManageList.vue')
    },
    {
        path: '/pages/fee/paymentPool',
        meta: { title: '支付池' },
        name: '/pages/fee/paymentPool',
        component: () => import('@/views/system/paymentPoolList.vue')
    },
    {
        path: '/pages/log/operateDataLog',
        meta: { title: '操作日志' },
        name: '/pages/log/operateDataLog',
        component: () => import('@/views/system/operateDataLogList.vue')
    },

    {
        path: '/pages/property/feePrintSpecManage',
        meta: { title: '打印规格' },
        name: '/pages/property/feePrintSpecManage',
        component: () => import('@/views/system/feePrintSpecManageList.vue')
    },
    {
        path: '/pages/property/feePrintPageManage',
        meta: { title: '打印页面' },
        name: '/pages/property/feePrintPageManage',
        component: () => import('@/views/system/feePrintPageManageList.vue')
    },
    {
        path: '/pages/property/downloadTempFile',
        meta: { title: '下载模板' },
        name: '/pages/property/downloadTempFile',
        component: () => import('@/views/system/downloadTempFileList.vue')
    },
    {
        path: '/pages/property/assetImportLog',
        meta: { title: '资产导入日志' },
        name: '/pages/property/assetImportLog',
        component: () => import('@/views/system/assetImportLogList.vue')
    },
    {
        path: '/views/system/assetImportLogDetail',
        meta: { title: '导入日志详情' },
        name: '/views/system/assetImportLogDetail',
        component: () => import('@/views/system/assetImportLogDetailList.vue')
    },
    {
        path: '/pages/fee/payFeeQrcode',
        meta: { title: '缴费二维码' },
        name: '/pages/fee/payFeeQrcode',
        component: () => import('@/views/fee/payFeeQrcodeList.vue')
    },
    {
        path: '/views/system/menuUserManage',
        meta: { title: '菜单用户' },
        name: '/views/system/menuUserManage',
        component: () => import('@/views/system/menuUserManageList.vue')
    },
]
