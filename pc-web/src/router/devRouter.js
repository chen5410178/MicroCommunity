export default [
    {
        path: '/pages/dev/appManage',
        meta: { title: '应用信息' },
        name: '/pages/dev/appManage',
        component: () => import('@/views/dev/app/appList.vue')
    },
    {
        path: '/pages/dev/serviceManage',
        meta: { title: '服务管理' },
        name: '/pages/dev/serviceManage',
        component: () => import('@/views/dev/serviceList.vue')
    },
    {
        path: '/pages/dev/serviceRegisterManage',
        meta: { title: '服务注册' },
        name: '/pages/dev/serviceRegisterManage',
        component: () => import('@/views/dev/routeList.vue')
    },
    {
        path: '/views/dev/addRoute',
        meta: { title: '添加路由' },
        name: '/views/dev/addRoute',
        component: () => import('@/views/dev/addRoute.vue')
    },
    {
        path: '/pages/dev/serviceImplManage',
        meta: { title: '服务实现' },
        name: '/pages/dev/serviceImplManage',
        component: () => import('@/views/dev/serviceBusinessList.vue')
    },
    {
        path: '/pages/dev/businessTableHisManage',
        meta: { title: '业务轨迹' },
        name: '/pages/dev/businessTableHisManage',
        component: () => import('@/views/dev/businessTableHisList.vue')
    },
    {
        path: '/pages/dev/menuCatalogManage',
        meta: { title: '菜单目录' },
        name: '/pages/dev/menuCatalogManage',
        component: () => import('@/views/dev/menuCatalogList.vue')
    },
    {
        path: '/views/dev/menuGroupCatalogList',
        meta: { title: '菜单组目录' },
        name: '/views/dev/menuGroupCatalogList',
        component: () => import('@/views/dev/menuGroupCatalogList.vue')
    },
    {
        path: '/pages/dev/menuGroupManage',
        meta: { title: '菜单组' },
        name: '/pages/dev/menuGroupManage',
        component: () => import('@/views/dev/menuGroupList.vue')
    },
    {
        path: '/pages/dev/mappingDomainManage',
        meta: { title: '配置项' },
        name: '/pages/dev/mappingDomainManage',
        component: () => import('@/views/dev/mappingDomainList.vue')
    },
    {
        path: '/pages/dev/mappingManage',
        meta: { title: '配置中心' },
        name: '/pages/dev/mappingManage',
        component: () => import('@/views/dev/mappingList.vue')
    },
    {
        path: '/pages/dev/businessDatabusManage',
        meta: { title: 'Databus' },
        name: '/pages/dev/businessDatabusManage',
        component: () => import('@/views/dev/businessDatabusList.vue')
    },
    {
        path: '/pages/dev/dictManage',
        meta: { title: '字典' },
        name: '/pages/dev/dictManage',
        component: () => import('@/views/dev/dictList.vue')
    },
    {
        path: '/pages/dev/cacheManage',
        meta: { title: '刷新缓存' },
        name: '/pages/dev/cacheManage',
        component: () => import('@/views/dev/cacheList.vue')
    },
    {
        path: '/pages/dev/menuManage',
        meta: { title: '菜单维护' },
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
        meta: { title: '定时任务' },
        name: '/pages/dev/jobManage',
        component: () => import('@/views/dev/taskList.vue')
    },
    {
        path: '/pages/dev/attrSpecManage',
        meta: { title: '属性' },
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
        meta: { title: '报表组' },
        name: '/pages/dev/reportCustomGroupManage',
        component: () => import('@/views/report/reportCustomGroupManageList.vue')
    },
    {
        path: '/pages/dev/reportCustomManage',
        meta: { title: '报表信息' },
        name: '/pages/dev/reportCustomManage',
        component: () => import('@/views/report/reportCustomManageList.vue')
    },
    {
        path: '/pages/dev/reportCustomComponentManage',
        meta: { title: '报表组件' },
        name: '/pages/dev/reportCustomComponentManage',
        component: () => import('@/views/report/reportCustomComponentManageList.vue')
    },
    {
        path: '/pages/dev/transactionLogManage',
        meta: { title: '交互日志' },
        name: '/pages/dev/transactionLogManage',
        component: () => import('@/views/dev/transactionLogManageList.vue')
    },
    {
        path: '/pages/dev/initializeCommunityManage',
        meta: { title: '初始化小区' },
        name: '/pages/dev/initializeCommunityManage',
        component: () => import('@/views/community/initializeCommunityManageList.vue')
    },
    {
        path: '/pages/dev/serviceProvideManage',
        meta: { title: '服务提供' },
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
        meta: { title: '调用三方' },
        name: '/pages/dev/transactionOutLog',
        component: () => import('@/views/dev/transactionOutLogList.vue')
      },
      
]