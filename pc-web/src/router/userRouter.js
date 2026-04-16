export default [
    {
        path: '/pages/staff/staffCommunity',
        meta: { title: '员工小区' },
        name: '/pages/staff/staffCommunity',
        component: () => import('@/views/staff/staffCommunityList.vue')
    },
    {
        path: '/pages/property/staffAppAuthManage',
        meta: { title: '员工认证' },
        name: '/pages/property/staffAppAuthManage',
        component: () => import('@/views/staff/staffAppAuthManageList.vue')
    },
    {
        path: '/pages/property/dataPrivilegeManage',
        meta: { title: '数据权限' },
        name: '/pages/property/dataPrivilegeManage',
        component: () => import('@/views/org/dataPrivilegeManageList.vue')
    },
    {
        path: '/pages/property/classesManage',
        meta: { title: '班次设置' },
        name: '/pages/property/classesManage',
        component: () => import('@/views/org/classesManageList.vue')
    },
    {
        path: '/pages/property/scheduleClasses',
        meta: { title: '排班设置' },
        name: '/pages/property/scheduleClasses',
        component: () => import('@/views/org/scheduleClassesList.vue')
    },
    {
        path: '/views/org/addScheduleClasses',
        meta: { title: '添加排班' },
        name: '/views/org/addScheduleClasses',
        component: () => import('@/views/org/addScheduleClassesList.vue')
    },
    {
        path: '/views/org/editScheduleClasses',
        meta: { title: '编辑排班' },
        name: '/views/org/editScheduleClasses',
        component: () => import('@/views/org/editScheduleClassesList.vue')
    },
    {
        path: '/pages/property/scheduleClassesPage',
        meta: { title: '排班表' },
        name: '/pages/property/scheduleClassesPage',
        component: () => import('@/views/org/scheduleClassesPageList.vue')
    },
    {
        path: '/views/owner/ownerDetail',
        meta: { title: '业主详情' },
        name: '/views/owner/ownerDetail',
        component: () => import('@/views/owner/ownerDetail.vue')
    },
    {
        path: '/views/owner/auditAuthOwnerUndo',
        meta: { title: '待审核业主' },
        name: '/views/owner/auditAuthOwnerUndo',
        component: () => import('@/views/owner/auditAuthOwnerUndoList.vue')
    },
    {
        path: '/views/org/scheduleClassesStaffManage',
        meta: { title: '排班员工' },
        name: '/views/org/scheduleClassesStaffManage',
        component: () => import('@/views/org/scheduleClassesStaffManageList.vue')
    },
]
