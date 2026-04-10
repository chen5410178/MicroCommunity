export default [
    {
        path: '/pages/dev/appManage',
        name: '/pages/dev/appManage',
        component: () => import('@/views/dev/app/appList.vue')
    },
    {
        path: '/pages/dev/serviceManage',
        name: '/pages/dev/serviceManage',
        component: () => import('@/views/dev/serviceList.vue')
    },
    {
        path: '/pages/dev/serviceRegisterManage',
        name: '/pages/dev/serviceRegisterManage',
        component: () => import('@/views/dev/routeList.vue')
    },
    {
        path: '/views/dev/addRoute',
        name: '/views/dev/addRoute',
        component: () => import('@/views/dev/addRoute.vue')
    },
    {
        path: '/pages/dev/serviceImplManage',
        name: '/pages/dev/serviceImplManage',
        component: () => import('@/views/dev/serviceBusinessList.vue')
    },
    {
        path: '/pages/dev/businessTableHisManage',
        name: '/pages/dev/businessTableHisManage',
        component: () => import('@/views/dev/businessTableHisList.vue')
    },
    {
        path: '/pages/dev/menuCatalogManage',
        name: '/pages/dev/menuCatalogManage',
        component: () => import('@/views/dev/menuCatalogList.vue')
    },
    {
        path: '/views/dev/menuGroupCatalogList',
        name: '/views/dev/menuGroupCatalogList',
        component: () => import('@/views/dev/menuGroupCatalogList.vue')
    },
    {
        path: '/pages/dev/menuGroupManage',
        name: '/pages/dev/menuGroupManage',
        component: () => import('@/views/dev/menuGroupList.vue')
    },
    {
        path: '/pages/dev/mappingDomainManage',
        name: '/pages/dev/mappingDomainManage',
        component: () => import('@/views/dev/mappingDomainList.vue')
    },
    {
        path: '/pages/dev/mappingManage',
        name: '/pages/dev/mappingManage',
        component: () => import('@/views/dev/mappingList.vue')
    },
    {
        path: '/pages/dev/businessDatabusManage',
        name: '/pages/dev/businessDatabusManage',
        component: () => import('@/views/dev/businessDatabusList.vue')
    },
    {
        path: '/pages/dev/dictManage',
        name: '/pages/dev/dictManage',
        component: () => import('@/views/dev/dictList.vue')
    },
    {
        path: '/pages/dev/cacheManage',
        name: '/pages/dev/cacheManage',
        component: () => import('@/views/dev/cacheList.vue')
    },
    {
        path: '/pages/dev/menuManage',
        name: '/pages/dev/menuManage',
        component: () => import('@/views/dev/menuList.vue')
    },
    {
        path: '/views/dev/privilegeList',
        name: '/views/dev/privilegeList',
        component: () => import('@/views/dev/privilegeList.vue')
    },
    {
        path: '/pages/dev/jobManage',
        name: '/pages/dev/jobManage',
        component: () => import('@/views/dev/taskList.vue')
    },
    {
        path: '/pages/dev/attrSpecManage',
        name: '/pages/dev/attrSpecManage',
        component: () => import('@/views/dev/attrSpecList.vue')
    },
    {
        path: '/views/dev/attrValueList',
        name: '/pages/dev/attrValueList',
        component: () => import('@/views/dev/attrValueList.vue')
    },
    {
        path: '/pages/dev/reportCustomGroupManage',
        name: '/pages/dev/reportCustomGroupManage',
        component: () => import('@/views/report/reportCustomGroupManageList.vue')
    },
    {
        path: '/pages/dev/reportCustomManage',
        name: '/pages/dev/reportCustomManage',
        component: () => import('@/views/report/reportCustomManageList.vue')
    },
    {
        path: '/pages/dev/reportCustomComponentManage',
        name: '/pages/dev/reportCustomComponentManage',
        component: () => import('@/views/report/reportCustomComponentManageList.vue')
    },
    {
        path: '/pages/dev/transactionLogManage',
        name: '/pages/dev/transactionLogManage',
        component: () => import('@/views/log/transactionLogManageList.vue')
    },
    {
        path: '/pages/dev/initializeCommunityManage',
        name: '/pages/dev/initializeCommunityManage',
        component: () => import('@/views/community/initializeCommunityManageList.vue')
    },
    {
        path: '/pages/dev/serviceProvideManage',
        name: '/pages/dev/serviceProvideManage',
        component: () => import('@/views/dev/serviceProvideManageList.vue')
    },
    {
        path: '/views/dev/devServiceProvide',
        name: '/views/dev/devServiceProvide',
        component: () => import('@/views/dev/devServiceProvideList.vue')
    },
    {
        path: '/pages/dev/transactionOutLog',
        name: '/pages/dev/transactionOutLog',
        component: () => import('@/views/log/transactionOutLogList.vue')
      },
]