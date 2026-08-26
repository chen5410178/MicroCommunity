export default [
    {
        path: '/pages/property/contractCreateFee',
        meta: { title: '合同收费' },
        name: '/pages/property/contractCreateFee',
        component: () => import('@/views/fee/contractCreateFeeList.vue')
    },
    {
        path: '/pages/property/meterWaterManage',
        meta: { title: '水电抄表' },
        name: '/pages/property/meterWaterManage',
        component: () => import('@/views/fee/meterWaterManageList.vue')
    },
    {
        path: '/views/fee/meterTypeManage',
        meta: { title: '表类型管理' },
        name: '/views/fee/meterTypeManage',
        component: () => import('@/views/fee/meterTypeManageList.vue')
    },
    {
        path: '/pages/property/returnPayFeeManage',
        meta: { title: '退费审核' },
        name: '/pages/property/returnPayFeeManage',
        component: () => import('@/views/fee/returnPayFeeManageList.vue')
    },
    {
        path: '/pages/property/feeDiscountManage',
        meta: { title: '折扣设置' },
        name: '/pages/property/feeDiscountManage',
        component: () => import('@/views/fee/feeDiscountManageList.vue')
    },
    {
        path: '/pages/property/applyRoomDiscountManage',
        meta: { title: '优惠申请' },
        name: '/pages/property/applyRoomDiscountManage',
        component: () => import('@/views/fee/applyRoomDiscountManageList.vue')
    },
    {
        path: '/views/fee/discountType',
        meta: { title: '折扣类型' },
        name: '/views/fee/discountType',
        component: () => import('@/views/fee/discountTypeList.vue')
    },
    {
        path: '/views/fee/listApplyRoomDiscountRecord',
        meta: { title: '优惠记录' },
        name: '/views/fee/listApplyRoomDiscountRecord',
        component: () => import('@/views/fee/listApplyRoomDiscountRecordList.vue')
    },
    {
        path: '/pages/property/feeReceipt',
        meta: { title: '补打收据' },
        name: '/pages/property/feeReceipt',
        component: () => import('@/views/fee/feeReceiptList.vue')
    },
    {
        path: '/pages/fee/floorShare',
        meta: { title: '楼栋公摊' },
        name: '/pages/fee/floorShare',
        component: () => import('@/views/fee/floorShareList.vue')
    },
    {
        path: '/pages/fee/shareReading',
        meta: { title: '公摊抄表' },
        name: '/pages/fee/shareReading',
        component: () => import('@/views/fee/shareReadingList.vue')
    },
    {
        path: '/pages/property/roomFeeImport',
        meta: { title: '费用导入' },
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
        meta: { title: '取消费用' },
        name: '/pages/property/payFeeBatch',
        component: () => import('@/views/fee/payFeeBatchList.vue')
    },

    {
        path: '/pages/fee/oweFeeCallable',
        meta: { title: '欠费催缴' },
        name: '/pages/fee/oweFeeCallable',
        component: () => import('@/views/fee/oweFeeCallableList.vue')
    },
    {
        path: '/views/fee/addOweFeeCallable',
        meta: { title: '添加催缴' },
        name: '/views/fee/addOweFeeCallable',
        component: () => import('@/views/fee/addOweFeeCallableList.vue')
    },
    {
        path: '/pages/property/roomCreateFee',
        meta: { title: '房屋收费' },
        name: '/pages/property/roomCreateFee',
        component: () => import('@/views/fee/roomCreateFeeList.vue')
    },
    {
        path: '/views/fee/propertyFee',
        meta: { title: '物业费' },
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
        meta: { title: '业务受理' },
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
        meta: { title: '退押金' },
        name: '/views/fee/refundDepositFee',
        component: () => import('@/views/fee/refundDepositFeeList.vue')
    },
    {
        path: '/views/fee/createFeeByCombo',
        meta: { title: '按套餐收费' },
        name: '/views/fee/createFeeByCombo',
        component: () => import('@/views/fee/createFeeByComboList.vue')
    },
    {
        path: '/views/fee/owePayFeeOrder',
        meta: { title: '欠费缴费' },
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
        meta: { title: '预缴订单' },
        name: '/views/fee/prePayFeeOrder',
        component: () => import('@/views/fee/prePayFeeOrder.vue')
    },
    {
        path: '/views/fee/prePayFeeDeductionDetail',
        meta: { title: '预缴扣费详情' },
        name: '/views/fee/prePayFeeDeductionDetail',
        component: () => import('@/views/fee/prePayFeeDeductionDetail.vue')
    },
    {
        path: '/views/fee/refundPrePayFee',
        meta: { title: '退预缴费' },
        name: '/views/fee/refundPrePayFee',
        component: () => import('@/views/fee/refundPrePayFee.vue')
    },
    {
        path: '/views/fee/wirteRuleFeeRemark',
        meta: { title: '费用规则备注' },
        name: '/views/fee/wirteRuleFeeRemark',
        component: () => import('@/views/fee/wirteRuleFeeRemarkList.vue')
    },
    {
        path: '/pages/property/shopCreateFee',
        meta: { 
            title: '商铺收费', 
            keepAlive: true,
            keepAliveInclude: ['LocationShopTreeDiv', 'PrePayFeeDetailList']
        },
        name: '/pages/property/shopCreateFee',
        component: () => import('@/views/fee/shopCreateFeeList.vue')
    },
    {
        path: '/pages/fee/manualBillGeneration',
        name: '/pages/fee/manualBillGeneration',
        component: () => import('@/views/fee/manualBillGenerationMain.vue')
    },
    {
        path: '/views/fee/manualBillGenerationDetail',
        name: '/views/fee/manualBillGenerationDetail',
        component: () => import('@/views/fee/manualBillGenerationDetail.vue')
    },
]