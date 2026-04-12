export default [
    {
        path: '/pages/property/contractCreateFee',
        name: '/pages/property/contractCreateFee',
        component: () => import('@/views/fee/contractCreateFeeList.vue')
    },
    {
        path: '/pages/property/meterWaterManage',
        name: '/pages/property/meterWaterManage',
        component: () => import('@/views/fee/meterWaterManageList.vue')
    },
    {
        path: '/views/fee/meterTypeManage',
        name: '/views/fee/meterTypeManage',
        component: () => import('@/views/fee/meterTypeManageList.vue')
    },
    {
        path: '/pages/property/returnPayFeeManage',
        name: '/pages/property/returnPayFeeManage',
        component: () => import('@/views/fee/returnPayFeeManageList.vue')
    },
    {
        path: '/pages/property/feeDiscountManage',
        name: '/pages/property/feeDiscountManage',
        component: () => import('@/views/fee/feeDiscountManageList.vue')
    },
    {
        path: '/pages/property/applyRoomDiscountManage',
        name: '/pages/property/applyRoomDiscountManage',
        component: () => import('@/views/fee/applyRoomDiscountManageList.vue')
    },
    {
        path: '/views/fee/discountType',
        name: '/views/fee/discountType',
        component: () => import('@/views/fee/discountTypeList.vue')
    },
    {
        path: '/views/fee/listApplyRoomDiscountRecord',
        name: '/views/fee/listApplyRoomDiscountRecord',
        component: () => import('@/views/fee/listApplyRoomDiscountRecordList.vue')
    },
    {
        path: '/pages/property/feeReceipt',
        name: '/pages/property/feeReceipt',
        component: () => import('@/views/fee/feeReceiptList.vue')
    },
    {
        path: '/pages/fee/floorShare',
        name: '/pages/fee/floorShare',
        component: () => import('@/views/fee/floorShareList.vue')
    },
    {
        path: '/pages/fee/shareReading',
        name: '/pages/fee/shareReading',
        component: () => import('@/views/fee/shareReadingList.vue')
    },
    {
        path: '/pages/property/roomFeeImport',
        name: '/pages/property/roomFeeImport',
        component: () => import('@/views/fee/roomFeeImportList.vue')
    },
    {
        path: '/views/fee/shareReadingFee',
        name: '/views/fee/shareReadingFee',
        component: () => import('@/views/fee/shareReadingFeeList.vue')
    },



    {
        path: '/pages/property/payFeeBatch',
        name: '/pages/property/payFeeBatch',
        component: () => import('@/views/fee/payFeeBatchList.vue')
    },
    {
        path: '/pages/fee/oweFeeCallable',
        name: '/pages/fee/oweFeeCallable',
        component: () => import('@/views/fee/oweFeeCallableList.vue')
    },
    {
        path: '/views/fee/addOweFeeCallable',
        name: '/views/fee/addOweFeeCallable',
        component: () => import('@/views/fee/addOweFeeCallableList.vue')
    },


    {
        path: '/pages/property/roomCreateFee',
        name: '/pages/property/roomCreateFee',
        component: () => import('@/views/fee/roomCreateFeeList.vue')
    },
    {
        path: '/views/fee/propertyFee',
        name: '/views/fee/propertyFee',
        component: () => import('@/views/fee/propertyFeeList.vue')
    },
    {
        path: '/views/fee/batchPayFeeOrder',
        name: '/views/fee/batchPayFeeOrder',
        component: () => import('@/views/fee/batchPayFeeOrderList.vue')
    },
    {
        path: '/pages/property/simplifyAcceptance',
        name: '/pages/property/simplifyAcceptance',
        component: () => import('@/views/simplify/simplifyAcceptanceList.vue')
    },
    {
        path: '/views/fee/payFeeOrder',
        name: '/views/fee/payFeeOrder',
        component: () => import('@/views/fee/payFeeOrderList.vue')
    },
    {
        path: '/views/fee/refundDepositFee',
        name: '/views/fee/refundDepositFee',
        component: () => import('@/views/fee/refundDepositFeeList.vue')
    },
    {
        path: '/views/fee/createFeeByCombo',
        name: '/views/fee/createFeeByCombo',
        component: () => import('@/views/fee/createFeeByComboList.vue')
    },
    {
        path: '/views/fee/owePayFeeOrder',
        name: '/views/fee/owePayFeeOrder',
        component: () => import('@/views/fee/owePayFeeOrderList.vue')
    },

    {
        path: '/views/fee/feeDetail',
        name: '/views/fee/feeDetail',
        component: () => import('@/views/fee/feeDetail.vue')
    },
    {
        path: '/pages/fee/roomCreatePayFee',
        name: '/pages/fee/roomCreatePayFee',
        component: () => import('@/views/fee/roomCreatePayFeeList.vue')
    },
    {
        path: '/views/fee/listApplyRoomDiscountRecordDetails',
        name: '/views/fee/listApplyRoomDiscountRecordDetails',
        component: () => import('@/views/fee/listApplyRoomDiscountRecordDetailsList.vue')
    },

    {
        path: '/views/fee/roomOweFeeCallable',
        name: '/views/fee/roomOweFeeCallable',
        component: () => import('@/views/fee/roomOweFeeCallableList.vue')
    },
    {
        path: '/views/fee/prePayFeeOrder',
        name: '/views/fee/prePayFeeOrder',
        component: () => import('@/views/fee/prePayFeeOrder.vue')
    },
    {
        path: '/views/fee/prePayFeeDeductionDetail',
        name: '/views/fee/prePayFeeDeductionDetail',
        component: () => import('@/views/fee/prePayFeeDeductionDetail.vue')
    },
    {
        path: '/views/fee/refundPrePayFee',
        name: '/views/fee/refundPrePayFee',
        component: () => import('@/views/fee/refundPrePayFee.vue')
    },
    {
        path: '/views/fee/wirteRuleFeeRemark',
        name: '/views/fee/wirteRuleFeeRemark',
        component: () => import('@/views/fee/wirteRuleFeeRemarkList.vue')
    },
    {
        path: '/pages/property/shopCreateFee',
        name: '/pages/property/shopCreateFee',
        component: () => import('@/views/fee/shopCreateFeeList.vue')
    },
    {
        path: '/pages/fee/shopCreatePayFee',
        name: '/pages/fee/shopCreatePayFee',
        component: () => import('@/views/fee/shopCreatePayFeeList.vue')
    },
]