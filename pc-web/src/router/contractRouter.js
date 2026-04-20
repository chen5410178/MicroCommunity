export default [
    {
        path: '/pages/admin/contractTypeManage',
        meta: { title: '合同类型' },
        name: '/pages/admin/contractTypeManage',
        component: () => import('@/views/contract/contractTypeManageList.vue')
    },
    {
        path: '/views/contract/contractTypeSpecManage',
        name: '/views/contract/contractTypeSpecManage',
        component: () => import('@/views/contract/contractTypeSpecManageList.vue')
    },
    {
        path: '/pages/admin/contractPartyaManage',
        meta: { title: '合同甲方' },
        name: '/pages/admin/contractPartyaManage',
        component: () => import('@/views/contract/contractPartyaManageList.vue')
    },
    {
        path: '/pages/admin/newContractManage',
        meta: { title: '起草合同' },
        name: '/pages/admin/newContractManage',
        component: () => import('@/views/contract/newContractManageList.vue')
    },
    {
        path: '/views/contract/addContract',
        name: '/views/contract/addContract',
        component: () => import('@/views/contract/addContractList.vue')
    },
    {
        path: '/pages/admin/contractManage',
        meta: { title: '合同查询' },
        name: '/pages/admin/contractManage',
        component: () => import('@/views/contract/contractManageList.vue')
    },
    {
        path: '/pages/admin/contractChangeManage',
        meta: { title: '合同变更' },
        name: '/pages/admin/contractChangeManage',
        component: () => import('@/views/contract/contractChangeManageList.vue')
    },
    {
        path: '/views/contract/contractChangeDetail',
        name: '/views/contract/contractChangeDetail',
        component: () => import('@/views/contract/contractChangeDetailList.vue')
    },
    {
        path: '/pages/admin/expirationContractManage',
        meta: { title: '到期合同' },
        name: '/pages/admin/expirationContractManage',
        component: () => import('@/views/contract/expirationContractManageList.vue')
    },
    {
        path: '/views/contract/contractDetail',
        name: '/views/contract/contractDetail',
        component: () => import('@/views/contract/contractDetail.vue')
    },
    {
        path: '/views/contract/contractChangeDetails',
        name: '/views/contract/contractChangeDetails',
        component: () => import('@/views/contract/contractChangeDetailsList.vue')
    },
    {
        path: '/pages/admin/contractApplyAuditOrders',
        name: '/pages/admin/contractApplyAuditOrders',
        component: () => import('@/views/contract/contractApplyAuditOrdersList.vue')
    },
    {
        path: '/pages/admin/contractChangeAuditOrders',
        name: '/pages/admin/contractChangeAuditOrders',
        component: () => import('@/views/contract/contractChangeAuditOrdersList.vue')
    },
    {
        path: '/pages/admin/contractApplyAuditHistoryOrders',
        name: '/pages/admin/contractApplyAuditHistoryOrders',
        component: () => import('@/views/contract/contractApplyAuditHistoryOrdersList.vue')
    },
    {
        path:'/pages/admin/contractChangeAuditHistoryOrders',
        name:'/pages/admin/contractChangeAuditHistoryOrders',
        component: () => import('@/views/contract/contractChangeAuditHistoryOrdersList.vue')
        },
        {
            path: '/views/contract/contractApplyDetail',
            name: '/views/contract/contractApplyDetail',
            component: () => import('@/views/contract/contractApplyDetail.vue')
        },
]