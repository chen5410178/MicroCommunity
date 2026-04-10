export default [
    {
        path: '/pages/property/itemReleaseTypeManage',
        name: '/pages/property/itemReleaseTypeManage',
        component: () => import('@/views/work/itemReleaseTypeManageList.vue')
    },
    {
        path: '/pages/property/itemReleaseManage',
        name: '/pages/property/itemReleaseManage',
        component: () => import('@/views/work/itemReleaseManageList.vue')
    },
    {
        path: '/views/work/addItemReleaseView',
        name: '/views/work/addItemReleaseView',
        component: () => import('@/views/work/addItemReleaseViewList.vue')
    },
    {
        path: '/views/work/editItemReleaseView',
        name: '/views/work/editItemReleaseView',
        component: () => import('@/views/work/editItemReleaseViewList.vue')
    },
    {
        path: '/views/work/itemReleaseDetail',
        name: '/views/work/itemReleaseDetail',
        component: () => import('@/views/work/itemReleaseDetailList.vue')
    },
    {
        path:'/views/work/finishRepair',
        name:'/views/work/finishRepair',
        component: () => import('@/views/work/finishRepairList.vue')
        },
        {
            path:'/views/work/addRoomRepair',
            name:'/views/work/addRoomRepair',
            component: () => import('@/views/work/addRoomRepairList.vue')
            },
]