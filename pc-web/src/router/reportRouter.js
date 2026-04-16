export default [

    {
        path: '/pages/property/reportFeeSummary',
        meta: { title: '费用汇总' },
        name: '/pages/property/reportFeeSummary',
        component: () => import('@/views/report/reportFeeSummaryList.vue')
    },
    {
        path: '/pages/property/reportFeeDetail',
        meta: { title: '费用明细' },
        name: '/pages/property/reportFeeDetail',
        component: () => import('@/views/report/reportFeeDetailList.vue')
    },

    {
        path: '/pages/report/dataReport',
        meta: { title: '数据报表' },
        name: '/pages/report/dataReport',
        component: () => import('@/views/report/dataReportList.vue')
    },
    {
        path: '/pages/property/reportOweFeeDetail',
        meta: { title: '欠费明细' },
        name: '/pages/property/reportOweFeeDetail',
        component: () => import('@/views/report/reportOweFeeDetailList.vue')
    },
    {
        path: '/pages/property/reportPayFeeDetail',
        meta: { title: '缴费明细' },
        name: '/pages/property/reportPayFeeDetail',
        component: () => import('@/views/report/reportPayFeeDetailList.vue')
    },
    {
        path: '/pages/property/reportRepair',
        meta: { title: '报修报表' },
        name: '/pages/property/reportRepair',
        component: () => import('@/views/report/reportRepairList.vue')
    },
    {
        path: '/pages/property/reportNoFeeRoom',
        meta: { title: '未缴费房屋' },
        name: '/pages/property/reportNoFeeRoom',
        component: () => import('@/views/report/reportNoFeeRoomList.vue')
    },
    {
        path: '/pages/property/reportHuaning',
        meta: { title: '换拧报表' },
        name: '/pages/property/reportHuaning',
        component: () => import('@/views/report/reportHuaningList.vue')
    },
    {
        path: '/pages/property/reportPayFeeDeposit',
        meta: { title: '押金报表' },
        name: '/pages/property/reportPayFeeDeposit',
        component: () => import('@/views/report/reportPayFeeDepositList.vue')
    },
    {
        path: '/pages/property/commonReport',
        meta: { title: '通用报表' },
        name: '/pages/property/commonReport',
        component: () => import('@/views/report/commonReportList.vue')
    },

]