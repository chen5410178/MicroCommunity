export default [
    {
        path: '/pages/property/communitySettingManage',
        name: '/pages/property/communitySettingManage',
        component: () => import('@/views/system/communitySettingManageList.vue')
    },
    {
        path: '/pages/property/workflowManage',
        name: '/pages/property/workflowManage',
        component: () => import('@/views/system/workflowManageList.vue')
    },
    {
        path: '/pages/property/workflowSettingManage',
        name: '/pages/property/workflowSettingManage',
        component: () => import('@/views/system/workflowSettingManageList.vue')
    },
    {
        path: '/pages/common/storeInfoManage',
        name: '/pages/common/storeInfoManage',
        component: () => import('@/views/system/storeInfoManageList.vue')
    },
    {
        path: '/pages/property/publicWeChatManage',
        name: '/pages/property/publicWeChatManage',
        component: () => import('@/views/system/publicWeChatManageList.vue')
    },
    {
        path: '/pages/property/smallWeChatManage',
        name: '/pages/property/smallWeChatManage',
        component: () => import('@/views/system/smallWeChatManageList.vue')
    },
    {
        path: '/pages/fee/paymentPool',
        name: '/pages/fee/paymentPool',
        component: () => import('@/views/system/paymentPoolList.vue')
    },
    {
        path: '/pages/log/operateDataLog',
        name: '/pages/log/operateDataLog',
        component: () => import('@/views/system/operateDataLogList.vue')
    },
    {
        path: '/pages/property/historyFeeDetailImport',
        name: '/pages/property/historyFeeDetailImport',
        component: () => import('@/views/system/historyFeeDetailImportList.vue')
    },
    {
        path: '/pages/property/feePrintSpecManage',
        name: '/pages/property/feePrintSpecManage',
        component: () => import('@/views/system/feePrintSpecManageList.vue')
    },
    {
        path: '/pages/property/feePrintPageManage',
        name: '/pages/property/feePrintPageManage',
        component: () => import('@/views/system/feePrintPageManageList.vue')
    },
    {
        path: '/pages/property/downloadTempFile',
        name: '/pages/property/downloadTempFile',
        component: () => import('@/views/system/downloadTempFileList.vue')
    },
    {
        path: '/pages/property/assetImportLog',
        name: '/pages/property/assetImportLog',
        component: () => import('@/views/system/assetImportLogList.vue')
    },
    {
        path: '/views/system/assetImportLogDetail',
        name: '/views/system/assetImportLogDetail',
        component: () => import('@/views/system/assetImportLogDetailList.vue')
    },
    {
        path: '/pages/fee/payFeeQrcode',
        name: '/pages/fee/payFeeQrcode',
        component: () => import('@/views/fee/payFeeQrcodeList.vue')
    },
    {
        path: '/views/system/menuUserManage',
        name: '/views/system/menuUserManage',
        component: () => import('@/views/system/menuUserManageList.vue')
    },
]