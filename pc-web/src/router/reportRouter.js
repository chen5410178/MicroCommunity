export default [
    {
        path: '/pages/property/reportProficient',
        name: '/pages/property/reportProficient',
        component: () => import('@/views/report/reportProficientList.vue')
    },
    {
        path: '/pages/property/reportFeeSummary',
        name: '/pages/property/reportFeeSummary',
        component: () => import('@/views/report/reportFeeSummaryList.vue')
    },
    {
        path: '/pages/property/reportFeeDetail',
        name: '/pages/property/reportFeeDetail',
        component: () => import('@/views/report/reportFeeDetailList.vue')
    },
    {
        path: '/pages/report/feeRemind',
        name: '/pages/report/feeRemind',
        component: () => import('@/views/report/feeRemindList.vue')
    },
    {
        path: '/pages/report/dataReport',
        name: '/pages/report/dataReport',
        component: () => import('@/views/report/dataReportList.vue')
    },
    {
        path: '/pages/property/reportOweFeeDetail',
        name: '/pages/property/reportOweFeeDetail',
        component: () => import('@/views/report/reportOweFeeDetailList.vue')
    },
    {
        path: '/pages/property/reportPayFeeDetail',
        name: '/pages/property/reportPayFeeDetail',
        component: () => import('@/views/report/reportPayFeeDetailList.vue')
    },
    {
        path: '/pages/property/reportRepair',
        name: '/pages/property/reportRepair',
        component: () => import('@/views/report/reportRepairList.vue')
    },
    {
        path: '/pages/property/reportNoFeeRoom',
        name: '/pages/property/reportNoFeeRoom',
        component: () => import('@/views/report/reportNoFeeRoomList.vue')
    },
    {
        path: '/pages/property/reportQuestionAnswerDetail',
        name: '/pages/property/reportQuestionAnswerDetail',
        component: () => import('@/views/report/reportQuestionAnswerDetailList.vue')
    },
    {
        path: '/pages/property/reportOwnerPayFee',
        name: '/pages/property/reportOwnerPayFee',
        component: () => import('@/views/report/reportOwnerPayFeeList.vue')
    },
    {
        path: '/pages/property/reportHuaning',
        name: '/pages/property/reportHuaning',
        component: () => import('@/views/report/reportHuaningList.vue')
    },
    {
        path: '/pages/property/reportPayFeeDeposit',
        name: '/pages/property/reportPayFeeDeposit',
        component: () => import('@/views/report/reportPayFeeDepositList.vue')
    },
    {
        path: '/pages/property/commonReport',
        name: '/pages/property/commonReport',
        component: () => import('@/views/report/commonReportList.vue')
    },

]