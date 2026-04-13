export default [
    {
        path: '/pages/resource/adminEquipment',
        meta: { title: '设备管理' },
        name: '/pages/resource/adminEquipment',
        component: () => import('@/views/resource/adminEquipmentList.vue')
    },
    {
        path: '/pages/resource/resourceAuditFlow',
        meta: { title: '资源审核流程' },
        name: '/pages/resource/resourceAuditFlow',
        component: () => import('@/views/resource/resourceAuditFlowList.vue')
    },
    {
        path: '/pages/property/storehouseManage',
        meta: { title: '仓库管理' },
        name: '/pages/property/storehouseManage',
        component: () => import('@/views/resource/storehouseManageList.vue')
    },
    {
        path: '/pages/common/resourceStoreSpecificationManage',
        meta: { title: '资源规格' },
        name: '/pages/common/resourceStoreSpecificationManage',
        component: () => import('@/views/resource/resourceStoreSpecificationManageList.vue')
    },
    {
        path: '/pages/common/resourceStoreManage',
        meta: { title: '资源管理' },
        name: '/pages/common/resourceStoreManage',
        component: () => import('@/views/resource/resourceStoreManageList.vue')
    },

    {
        path: '/pages/property/resourceSupplierManage',
        meta: { title: '资源供应商' },
        name: '/pages/property/resourceSupplierManage',
        component: () => import('@/views/resource/resourceSupplierManageList.vue')
    },
    {
        path: '/pages/common/purchaseApplyManage',
        meta: { title: '采购申请' },
        name: '/pages/common/purchaseApplyManage',
        component: () => import('@/views/resource/purchaseApplyManageList.vue')
    },
    {
        path: '/views/resource/addPurchaseApply',
        meta: { title: '添加采购申请' },
        name: '/views/resource/addPurchaseApply',
        component: () => import('@/views/resource/addPurchaseApplyList.vue')
    },
    {
        path: '/views/resource/urgentPurchaseApplyStep',
        meta: { title: '紧急采购' },
        name: '/views/resource/urgentPurchaseApplyStep',
        component: () => import('@/views/resource/urgentPurchaseApplyStepList.vue')
    },
    {
        path: '/views/resource/purchaseApplyDetail',
        meta: { title: '采购申请详情' },
        name: '/views/resource/purchaseApplyDetail',
        component: () => import('@/views/resource/purchaseApplyDetailList.vue')
    },
    {
        path: '/pages/common/itemOutManage',
        meta: { title: '物品出库' },
        name: '/pages/common/itemOutManage',
        component: () => import('@/views/resource/itemOutManageList.vue')
    },
    {
        path: '/views/resource/addItemOut',
        meta: { title: '添加出库' },
        name: '/views/resource/addItemOut',
        component: () => import('@/views/resource/addItemOutList.vue')
    },
    {
        path: '/pages/common/purchaseApplyDetailManage',
        meta: { title: '采购明细' },
        name: '/pages/common/purchaseApplyDetailManage',
        component: () => import('@/views/resource/purchaseApplyDetailManageList.vue')
    },
    {
        path: '/pages/common/allocationStorehouseManage',
        meta: { title: '调拨仓库' },
        name: '/pages/common/allocationStorehouseManage',
        component: () => import('@/views/resource/allocationStorehouseManageList.vue')
    },
    {
        path: '/pages/common/allocationStorehouseApply',
        meta: { title: '调拨申请' },
        name: '/pages/common/allocationStorehouseApply',
        component: () => import('@/views/resource/allocationStorehouseApplyList.vue')
    },
    {
        path: '/pages/common/allocationStorehouseDetailed',
        meta: { title: '调拨明细' },
        name: '/pages/common/allocationStorehouseDetailed',
        component: () => import('@/views/resource/allocationStorehouseDetailedList.vue')
    },
    {
        path: '/pages/property/assetInventoryManage',
        meta: { title: '资产盘点' },
        name: '/pages/property/assetInventoryManage',
        component: () => import('@/views/resource/assetInventoryManageList.vue')
    },
    {
        path: '/pages/common/myResourceStoreManage',
        meta: { title: '我的仓库' },
        name: '/pages/common/myResourceStoreManage',
        component: () => import('@/views/resource/myResourceStoreManageList.vue')
    },
    {
        path: '/pages/common/allocationUserStorehouseManage',
        meta: { title: '用户仓库' },
        name: '/pages/common/allocationUserStorehouseManage',
        component: () => import('@/views/resource/allocationUserStorehouseManageList.vue')
    },
    {
        path: '/pages/common/resourceStoreUseRecordManage',
        meta: { title: '使用记录' },
        name: '/pages/common/resourceStoreUseRecordManage',
        component: () => import('@/views/resource/resourceStoreUseRecordManageList.vue')
    },
    {
        path: '/views/resource/resourceDetail',
        meta: { title: '资源详情' },
        name: '/views/resource/resourceDetail',
        component: () => import('@/views/resource/resourceDetailList.vue')
    },
    {
        path: '/pages/common/allocationStorehouseDetail',
        meta: { title: '调拨详情' },
        name: '/pages/common/allocationStorehouseDetail',
        component: () => import('@/views/resource/allocationStorehouseDetailList.vue')
    },
    {
        path: '/pages/property/assetInventoryIn',
        meta: { title: '资产入库' },
        name: '/pages/property/assetInventoryIn',
        component: () => import('@/views/resource/assetInventoryInList.vue')
    },
    {
        path: '/pages/property/assetInventoryInStock',
        meta: { title: '资产入库单' },
        name: '/pages/property/assetInventoryInStock',
        component: () => import('@/views/resource/assetInventoryInStockList.vue')
    },
    {
        path: '/pages/property/assetInventoryEdit',
        meta: { title: '编辑资产' },
        name: '/pages/property/assetInventoryEdit',
        component: () => import('@/views/resource/assetInventoryEditList.vue')
    },
    {
        path: '/pages/property/assetInventoryAudit',
        meta: { title: '资产审核' },
        name: '/pages/property/assetInventoryAudit',
        component: () => import('@/views/resource/assetInventoryAuditList.vue')
    },
    {
        path: '/pages/common/returnStorehouseApplyManage',
        meta: { title: '退库申请' },
        name: '/pages/common/returnStorehouseApplyManage',
        component: () => import('@/views/resource/returnStorehouseApplyManageList.vue')
    },
    {
        path: '/pages/common/transferGoodsManage',
        meta: { title: '物品调拨' },
        name: '/pages/common/transferGoodsManage',
        component: () => import('@/views/resource/transferGoodsManageList.vue')
    },
    {
        path: '/pages/common/scrapGoodsStep',
        meta: { title: '物品报废' },
        name: '/pages/common/scrapGoodsStep',
        component: () => import('@/views/resource/scrapGoodsStepList.vue')
    },
    {
        path: '/pages/admin/myAuditOrders',
        meta: { title: '我的审核' },
        name: '/pages/admin/myAuditOrders',
        component: () => import('@/views/resource/myAuditOrdersList.vue')
    },
    {
        path: '/views/resource/resourceEnterManage',
        meta: { title: '资源入库' },
        name: '/views/resource/resourceEnterManage',
        component: () => import('@/views/resource/resourceEnterManageList.vue')
    },
    {
        path: '/pages/resource/editPurchaseApply',
        meta: { title: '编辑采购申请' },
        name: '/pages/resource/editPurchaseApply',
        component: () => import('@/views/resource/editPurchaseApplyList.vue')
    },
    {
        path: '/pages/admin/myItemOutAuditOrders',
        meta: { title: '出库审核' },
        name: '/pages/admin/myItemOutAuditOrders',
        component: () => import('@/views/resource/myItemOutAuditOrdersList.vue')
    },
    {
        path: '/pages/admin/resourceOutManage',
        meta: { title: '资源出库' },
        name: '/pages/admin/resourceOutManage',
        component: () => import('@/views/resource/resourceOutManageList.vue')
    },
    {
        path: '/pages/admin/allocationStorehouseAuditOrders',
        meta: { title: '调拨审核' },
        name: '/pages/admin/allocationStorehouseAuditOrders',
        component: () => import('@/views/resource/allocationStorehouseAuditOrdersList.vue')
    },
    {
        path: '/pages/resource/editAllocationStorehouseApply',
        meta: { title: '编辑调拨' },
        name: '/pages/resource/editAllocationStorehouseApply',
        component: () => import('@/views/resource/editAllocationStorehouseApplyList.vue')
    },
    {
        path: '/pages/resource/allocationStorehouseEnter',
        meta: { title: '调拨入库' },
        name: '/pages/resource/allocationStorehouseEnter',
        component: () => import('@/views/resource/allocationStorehouseEnterList.vue')
    },
    {
        path: '/pages/property/itemReleaseUndo',
        meta: { title: '物品放行待办' },
        name: '/pages/property/itemReleaseUndo',
        component: () => import('@/views/resource/itemReleaseUndoList.vue')
    },
    {
        path: '/pages/admin/myItemOutAuditHistoryOrders',
        meta: { title: '出库历史' },
        name: '/pages/admin/myItemOutAuditHistoryOrders',
        component: () => import('@/views/resource/myItemOutAuditHistoryOrdersList.vue')
    },
    {
        path: '/pages/admin/allocationStorehouseHistoryAuditOrders',
        meta: { title: '调拨历史' },
        name: '/pages/admin/allocationStorehouseHistoryAuditOrders',
        component: () => import('@/views/resource/allocationStorehouseHistoryAuditOrdersList.vue')
    },
    {
        path: '/pages/property/itemReleaseFinish',
        meta: { title: '物品放行完成' },
        name: '/pages/property/itemReleaseFinish',
        component: () => import('@/views/resource/itemReleaseFinishList.vue')
    },

]