export default [
    {
        path: '/pages/staff/staffCommunity',
        name: '/pages/staff/staffCommunity',
        component: () => import('@/views/staff/staffCommunityList.vue')
    },
    {
        path: '/pages/property/staffAppAuthManage',
        name: '/pages/property/staffAppAuthManage',
        component: () => import('@/views/staff/staffAppAuthManageList.vue')
    },
    {
        path: '/pages/property/dataPrivilegeManage',
        name: '/pages/property/dataPrivilegeManage',
        component: () => import('@/views/org/dataPrivilegeManageList.vue')
    },
    {
        path: '/pages/property/classesManage',
        name: '/pages/property/classesManage',
        component: () => import('@/views/org/classesManageList.vue')
    },
    {
        path: '/pages/property/scheduleClasses',
        name: '/pages/property/scheduleClasses',
        component: () => import('@/views/org/scheduleClassesList.vue')
    },
    {
        path: '/views/org/addScheduleClasses',
        name: '/views/org/addScheduleClasses',
        component: () => import('@/views/org/addScheduleClassesList.vue')
    },
    {
        path: '/views/org/editScheduleClasses',
        name: '/views/org/editScheduleClasses',
        component: () => import('@/views/org/editScheduleClassesList.vue')
    },
    {
        path: '/pages/property/scheduleClassesPage',
        name: '/pages/property/scheduleClassesPage',
        component: () => import('@/views/org/scheduleClassesPageList.vue')
    },
    {
        path: '/views/owner/ownerDetail',
        name: '/views/owner/ownerDetail',
        component: () => import('@/views/owner/ownerDetail.vue')
    },
    {
        path: '/views/owner/auditAuthOwnerUndo',
        name: '/views/owner/auditAuthOwnerUndo',
        component: () => import('@/views/owner/auditAuthOwnerUndoList.vue')
    },
    {
        path: '/views/org/scheduleClassesStaffManage',
        name: '/views/org/scheduleClassesStaffManage',
        component: () => import('@/views/org/scheduleClassesStaffManageList.vue')
    },
]