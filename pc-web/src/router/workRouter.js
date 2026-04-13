export default [
    {
        path: '/pages/property/itemReleaseTypeManage',
        meta: { title: '放行类型' },
        name: '/pages/property/itemReleaseTypeManage',
        component: () => import('@/views/work/itemReleaseTypeManageList.vue')
    },
    {
        path: '/pages/property/itemReleaseManage',
        meta: { title: '物品放行' },
        name: '/pages/property/itemReleaseManage',
        component: () => import('@/views/work/itemReleaseManageList.vue')
    },
    {
        path: '/views/work/addItemReleaseView',
        meta: { title: '添加放行' },
        name: '/views/work/addItemReleaseView',
        component: () => import('@/views/work/addItemReleaseViewList.vue')
    },
    {
        path: '/views/work/editItemReleaseView',
        meta: { title: '编辑放行' },
        name: '/views/work/editItemReleaseView',
        component: () => import('@/views/work/editItemReleaseViewList.vue')
    },
    {
        path: '/views/work/itemReleaseDetail',
        meta: { title: '放行详情' },
        name: '/views/work/itemReleaseDetail',
        component: () => import('@/views/work/itemReleaseDetailList.vue')
    },
    {
        path:'/views/work/finishRepair',
        meta: { title: '完成维修' },
        name:'/views/work/finishRepair',
        component: () => import('@/views/work/finishRepairList.vue')
        },
        {
            path:'/views/work/addRoomRepair',
            meta: { title: '添加报修' },
            name:'/views/work/addRoomRepair',
            component: () => import('@/views/work/addRoomRepairList.vue')
            },
]
