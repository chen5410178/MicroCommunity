export default [
    {
        path: '/pages/resource/adminEquipment',
        name: '/pages/resource/adminEquipment',
        component: () => import('@/views/resource/adminEquipmentList.vue')
    },
    {
        path: '/pages/resource/resourceAuditFlow',
        name: '/pages/resource/resourceAuditFlow',
        component: () => import('@/views/resource/resourceAuditFlowList.vue')
    },
    {
        path: '/pages/property/storehouseManage',
        name: '/pages/property/storehouseManage',
        component: () => import('@/views/resource/storehouseManageList.vue')
    },
    {
        path: '/pages/common/resourceStoreSpecificationManage',
        name: '/pages/common/resourceStoreSpecificationManage',
        component: () => import('@/views/resource/resourceStoreSpecificationManageList.vue')
    },
    {
        path: '/pages/common/resourceStoreManage',
        name: '/pages/common/resourceStoreManage',
        component: () => import('@/views/resource/resourceStoreManageList.vue')
    },

    {
        path: '/pages/property/resourceSupplierManage',
        name: '/pages/property/resourceSupplierManage',
        component: () => import('@/views/resource/resourceSupplierManageList.vue')
    },
    {
        path: '/pages/common/purchaseApplyManage',
        name: '/pages/common/purchaseApplyManage',
        component: () => import('@/views/resource/purchaseApplyManageList.vue')
    },
    {
        path: '/views/resource/addPurchaseApply',
        name: '/views/resource/addPurchaseApply',
        component: () => import('@/views/resource/addPurchaseApplyList.vue')
    },
    {
        path: '/views/resource/urgentPurchaseApplyStep',
        name: '/views/resource/urgentPurchaseApplyStep',
        component: () => import('@/views/resource/urgentPurchaseApplyStepList.vue')
    },
    {
        path: '/views/resource/purchaseApplyDetail',
        name: '/views/resource/purchaseApplyDetail',
        component: () => import('@/views/resource/purchaseApplyDetailList.vue')
    },
    {
        path: '/pages/common/itemOutManage',
        name: '/pages/common/itemOutManage',
        component: () => import('@/views/resource/itemOutManageList.vue')
    },
    {
        path: '/views/resource/addItemOut',
        name: '/views/resource/addItemOut',
        component: () => import('@/views/resource/addItemOutList.vue')
    },
    {
        path: '/pages/common/purchaseApplyDetailManage',
        name: '/pages/common/purchaseApplyDetailManage',
        component: () => import('@/views/resource/purchaseApplyDetailManageList.vue')
    },
    {
        path: '/pages/common/allocationStorehouseManage',
        name: '/pages/common/allocationStorehouseManage',
        component: () => import('@/views/resource/allocationStorehouseManageList.vue')
    },
    {
        path: '/pages/common/allocationStorehouseApply',
        name: '/pages/common/allocationStorehouseApply',
        component: () => import('@/views/resource/allocationStorehouseApplyList.vue')
    },
    {
        path: '/pages/common/allocationStorehouseDetailed',
        name: '/pages/common/allocationStorehouseDetailed',
        component: () => import('@/views/resource/allocationStorehouseDetailedList.vue')
    },
    {
        path: '/pages/property/assetInventoryManage',
        name: '/pages/property/assetInventoryManage',
        component: () => import('@/views/resource/assetInventoryManageList.vue')
    },
    {
        path: '/pages/common/myResourceStoreManage',
        name: '/pages/common/myResourceStoreManage',
        component: () => import('@/views/resource/myResourceStoreManageList.vue')
    },
    {
        path: '/pages/common/allocationUserStorehouseManage',
        name: '/pages/common/allocationUserStorehouseManage',
        component: () => import('@/views/resource/allocationUserStorehouseManageList.vue')
    },
    {
        path: '/pages/common/resourceStoreUseRecordManage',
        name: '/pages/common/resourceStoreUseRecordManage',
        component: () => import('@/views/resource/resourceStoreUseRecordManageList.vue')
    },
    {
        path: '/views/resource/resourceDetail',
        name: '/views/resource/resourceDetail',
        component: () => import('@/views/resource/resourceDetailList.vue')
    },
    {
        path: '/pages/common/allocationStorehouseDetail',
        name: '/pages/common/allocationStorehouseDetail',
        component: () => import('@/views/resource/allocationStorehouseDetailList.vue')
    },
    {
        path: '/pages/property/assetInventoryIn',
        name: '/pages/property/assetInventoryIn',
        component: () => import('@/views/resource/assetInventoryInList.vue')
    },
    {
        path: '/pages/property/assetInventoryInStock',
        name: '/pages/property/assetInventoryInStock',
        component: () => import('@/views/resource/assetInventoryInStockList.vue')
    },
    {
        path: '/pages/property/assetInventoryEdit',
        name: '/pages/property/assetInventoryEdit',
        component: () => import('@/views/resource/assetInventoryEditList.vue')
    },
    {
        path: '/pages/property/assetInventoryAudit',
        name: '/pages/property/assetInventoryAudit',
        component: () => import('@/views/resource/assetInventoryAuditList.vue')
    },
    {
        path: '/pages/common/returnStorehouseApplyManage',
        name: '/pages/common/returnStorehouseApplyManage',
        component: () => import('@/views/resource/returnStorehouseApplyManageList.vue')
    },
    {
        path: '/pages/common/transferGoodsManage',
        name: '/pages/common/transferGoodsManage',
        component: () => import('@/views/resource/transferGoodsManageList.vue')
    },
    {
        path: '/pages/common/scrapGoodsStep',
        name: '/pages/common/scrapGoodsStep',
        component: () => import('@/views/resource/scrapGoodsStepList.vue')
    },
    {
        path: '/pages/admin/myAuditOrders',
        name: '/pages/admin/myAuditOrders',
        component: () => import('@/views/resource/myAuditOrdersList.vue')
    },
    {
        path: '/views/resource/resourceEnterManage',
        name: '/views/resource/resourceEnterManage',
        component: () => import('@/views/resource/resourceEnterManageList.vue')
    },
    {
        path: '/pages/resource/editPurchaseApply',
        name: '/pages/resource/editPurchaseApply',
        component: () => import('@/views/resource/editPurchaseApplyList.vue')
    },
    {
        path: '/pages/admin/myItemOutAuditOrders',
        name: '/pages/admin/myItemOutAuditOrders',
        component: () => import('@/views/resource/myItemOutAuditOrdersList.vue')
    },
    {
        path: '/pages/admin/resourceOutManage',
        name: '/pages/admin/resourceOutManage',
        component: () => import('@/views/resource/resourceOutManageList.vue')
    },
    {
        path: '/pages/admin/allocationStorehouseAuditOrders',
        name: '/pages/admin/allocationStorehouseAuditOrders',
        component: () => import('@/views/resource/allocationStorehouseAuditOrdersList.vue')
    },
    {
        path: '/pages/resource/editAllocationStorehouseApply',
        name: '/pages/resource/editAllocationStorehouseApply',
        component: () => import('@/views/resource/editAllocationStorehouseApplyList.vue')
    },
    {
        path: '/pages/resource/allocationStorehouseEnter',
        name: '/pages/resource/allocationStorehouseEnter',
        component: () => import('@/views/resource/allocationStorehouseEnterList.vue')
    },
    {
        path: '/pages/property/itemReleaseUndo',
        name: '/pages/property/itemReleaseUndo',
        component: () => import('@/views/resource/itemReleaseUndoList.vue')
    },
    {
        path: '/pages/admin/myItemOutAuditHistoryOrders',
        name: '/pages/admin/myItemOutAuditHistoryOrders',
        component: () => import('@/views/resource/myItemOutAuditHistoryOrdersList.vue')
    },
    {
        path: '/pages/admin/allocationStorehouseHistoryAuditOrders',
        name: '/pages/admin/allocationStorehouseHistoryAuditOrders',
        component: () => import('@/views/resource/allocationStorehouseHistoryAuditOrdersList.vue')
    },
    {
        path: '/pages/property/itemReleaseFinish',
        name: '/pages/property/itemReleaseFinish',
        component: () => import('@/views/resource/itemReleaseFinishList.vue')
    },

]