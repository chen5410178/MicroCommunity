export default [
    {
        path: '/pages/inspection/inspectionPoint',
        meta: { title: '巡检点' },
        name: '/pages/inspection/inspectionPoint',
        component: () => import('@/views/inspection/inspectionPointList.vue')
    },
    {
        path: '/pages/inspection/inspectionRoute',
        meta: { title: '巡检路线' },
        name: '/pages/inspection/inspectionRoute',
        component: () => import('@/views/inspection/inspectionRouteList.vue')
    },
    {
        path: '/pages/inspection/adminInspectionPlan',
        meta: { title: '巡检计划' },
        name: '/pages/inspection/adminInspectionPlan',
        component: () => import('@/views/inspection/adminInspectionPlanList.vue')
    },
    {
        path: '/pages/inspection/aInspectionPlanDetail',
        name: '/pages/inspection/aInspectionPlanDetail',
        component: () => import('@/views/inspection/aInspectionPlanDetailList.vue')
    },
    {
        path: '/pages/inspection/adminInspectionTask',
        meta: { title: '巡检任务' },
        name: '/pages/inspection/adminInspectionTask',
        component: () => import('@/views/inspection/adminInspectionTaskList.vue')
    },
    {
        path: '/views/inspection/adminInspectionTaskDetail',
        name: '/views/inspection/adminInspectionTaskDetail',
        component: () => import('@/views/inspection/adminInspectionTaskDetailList.vue')
    },
    {
        path: '/pages/property/inspectionItemManage',
        meta: { title: '巡检项目' },
        name: '/pages/property/inspectionItemManage',
        component: () => import('@/views/inspection/inspectionItemManageList.vue')
    },
    {
        path: '/views/inspection/inspectionItemTitleManage',
        name: '/views/inspection/inspectionItemTitleManage',
        component: () => import('@/views/inspection/inspectionItemTitleManageList.vue')
    },
    {
        path: '/pages/inspection/inspectionPlan',
        meta: { title: '巡检计划' },
        name: '/pages/inspection/inspectionPlan',
        component: () => import('@/views/inspection/inspectionPlanList.vue')
    },
    {
        path: '/views/inspection/addInspectionPlan',
        name: '/views/inspection/addInspectionPlan',
        component: () => import('@/views/inspection/addInspectionPlanList.vue')
    },
    {
        path: '/pages/inspection/inspectionTask',
        meta: { title: '巡检任务' },
        name: '/pages/inspection/inspectionTask',
        component: () => import('@/views/inspection/InspectionTaskList.vue')
    },
    {
        path: '/pages/property/inspectionTaskDetails',
        meta: { title: '巡检明细' },
        name: '/pages/property/inspectionTaskDetails',
        component: () => import('@/views/inspection/inspectionTaskDetailsList.vue')
    },
    {
        path: '/pages/property/maintainanceItem',
        meta: { title: '检查项' },
        name: '/pages/property/maintainanceItem',
        component: () => import('@/views/inspection/maintainanceItemList.vue')
    },
    {
        path: '/pages/property/maintainanceStandardManage',
        meta: { title: '保养标准' },
        name: '/pages/property/maintainanceStandardManage',
        component: () => import('@/views/inspection/maintainanceStandardManageList.vue')
    },
    {
        path: '/views/inspection/maintainanceStandardItem',
        name: '/views/inspection/maintainanceStandardItem',
        component: () => import('@/views/inspection/maintainanceStandardItemList.vue')
    },
    {
        path: '/pages/property/maintainancePlanManage',
        meta: { title: '保养计划' },
        name: '/pages/property/maintainancePlanManage',
        component: () => import('@/views/inspection/maintainancePlanManageList.vue')
    },
    {
        path: '/views/inspection/addMaintainancePlan',
        name: '/views/inspection/addMaintainancePlan',
        component: () => import('@/views/inspection/addMaintainancePlanList.vue')
    },
    {
        path: '/views/inspection/editMaintainancePlan',
        name: '/views/inspection/editMaintainancePlan',
        component: () => import('@/views/inspection/editMaintainancePlanList.vue')
    },
    {
        path: '/views/inspection/maintainancePlanMachine',
        name: '/views/inspection/maintainancePlanMachine',
        component: () => import('@/views/inspection/maintainancePlanMachineList.vue')
    },
    {
        path: '/pages/property/maintainanceTaskDetails',
        meta: { title: '保养明细' },
        name: '/pages/property/maintainanceTaskDetails',
        component: () => import('@/views/inspection/maintainanceTaskDetailsList.vue')
    },
    {
        path: '/views/inspection/maintainanceTaskDetailView',
        name: '/views/inspection/maintainanceTaskDetailView',
        component: () => import('@/views/inspection/maintainanceTaskDetailView.vue')
    },
    {
        path: '/pages/property/maintainanceTaskManage',
        meta: { title: '保养任务' },
        name: '/pages/property/maintainanceTaskManage',
        component: () => import('@/views/inspection/maintainanceTaskManageList.vue')
    },
    {
        path:'/pages/inspection/inspectionPlanDetail',
        name:'/pages/inspection/inspectionPlanDetail',
        component: () => import('@/views/inspection/inspectionPlanDetailList.vue')
        },
]