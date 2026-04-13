export default [
    {
        path: '/pages/scm/couponMarket',
        meta: { title: '优惠券市场' },
        name: '/pages/scm/couponMarket',
        component: () => import('@/views/scm/couponMarketList.vue')
    },
    {
        path: '/pages/scm/couponPropertyPoolManage',
        meta: { title: '优惠券' },
        name: '/pages/scm/couponPropertyPoolManage',
        component: () => import('@/views/scm/couponPropertyPoolManageList.vue')
    },
    {
        path: '/pages/scm/couponRule',
        meta: { title: '赠送规则' },
        name: '/pages/scm/couponRule',
        component: () => import('@/views/scm/couponRuleList.vue')
    },
    {
        path: '/pages/scm/couponPropertyPoolDetail',
        meta: { title: '赠送记录' },
        name: '/pages/scm/couponPropertyPoolDetail',
        component: () => import('@/views/scm/couponPropertyPoolDetailList.vue')
    },
    {
        path: '/pages/scm/couponPropertyUserManage',
        meta: { title: '业主优惠券' },
        name: '/pages/scm/couponPropertyUserManage',
        component: () => import('@/views/scm/couponPropertyUserManageList.vue')
    },
    {
        path: '/pages/scm/couponPropertyUserDetail',
        meta: { title: '优惠券使用' },
        name: '/pages/scm/couponPropertyUserDetail',
        component: () => import('@/views/scm/couponPropertyUserDetailList.vue')
    },
    {
        path: '/pages/scm/integralConfigManage',
        meta: { title: '积分标准' },
        name: '/pages/scm/integralConfigManage',
        component: () => import('@/views/scm/integralConfigManageList.vue')
    },
    {
        path: '/pages/scm/integralRule',
        meta: { title: '积分规则' },
        name: '/pages/scm/integralRule',
        component: () => import('@/views/scm/integralRuleList.vue')
    },
    {
        path: '/pages/scm/integralGiftDetailManage',
        meta: { title: '积分赠送' },
        name: '/pages/scm/integralGiftDetailManage',
        component: () => import('@/views/scm/integralGiftDetailManageList.vue')
    },
    {
        path: '/pages/scm/communityIntegral',
        meta: { title: '小区积分' },
        name: '/pages/scm/communityIntegral',
        component: () => import('@/views/scm/communityIntegralList.vue')
    },
    {
        path: '/pages/scm/gold',
        meta: { title: '小区金币' },
        name: '/pages/scm/gold',
        component: () => import('@/views/scm/goldList.vue')
    },
    {
        path: '/pages/reserve/reserveCatalogManage',
        meta: { title: '预约目录' },
        name: '/pages/reserve/reserveCatalogManage',
        component: () => import('@/views/scm/reserveCatalogManageList.vue')
    },
    {
        path: '/pages/reserve/reserveParamsManage',
        meta: { title: '预约参数' },
        name: '/pages/reserve/reserveParamsManage',
        component: () => import('@/views/scm/reserveParamsManageList.vue')
    },
    {
        path: '/pages/reserve/reserveDiningManage',
        meta: { title: '预约就餐' },
        name: '/pages/reserve/reserveDiningManage',
        component: () => import('@/views/scm/reserveDiningManageList.vue')
    },
    {
        path: '/pages/reserve/reserveServiceManage',
        meta: { title: '预约服务' },
        name: '/pages/reserve/reserveServiceManage',
        component: () => import('@/views/scm/reserveServiceManageList.vue')
    },
    {
        path: '/views/scm/addReserveDining',
        meta: { title: '添加预约就餐' },
        name: '/views/scm/addReserveDining',
        component: () => import('@/views/scm/addReserveDiningList.vue')
    },
    {
        path: '/views/scm/editReserveDining',
        meta: { title: '编辑预约就餐' },
        name: '/views/scm/editReserveDining',
        component: () => import('@/views/scm/editReserveDiningList.vue')
    },
    {
        path: '/views/scm/doDining',
        meta: { title: '执行就餐' },
        name: '/views/scm/doDining',
        component: () => import('@/views/scm/doDiningList.vue')
    },
    {
        path: '/views/scm/addReserveService',
        meta: { title: '添加预约服务' },
        name: '/views/scm/addReserveService',
        component: () => import('@/views/scm/addReserveServiceList.vue')
    },
    {
        path: '/views/scm/ownerDining',
        meta: { title: '业主就餐' },
        name: '/views/scm/ownerDining',
        component: () => import('@/views/scm/ownerDiningList.vue')
    },
    {
        path: '/views/scm/reserveCatalogManage',
        meta: { title: '预约目录' },
        name: '/views/scm/reserveCatalogManage',
        component: () => import('@/views/scm/reserveCatalogManageList.vue')
    },
    {
        path: '/views/scm/editReserveService',
        meta: { title: '编辑预约服务' },
        name: '/views/scm/editReserveService',
        component: () => import('@/views/scm/editReserveServiceList.vue')
    },
    {
        path: '/pages/reserve/reserveOrder',
        meta: { title: '预约订单' },
        name: '/pages/reserve/reserveOrder',
        component: () => import('@/views/scm/reserveOrderList.vue')
    },
    {
        path:'/pages/reserve/reserveConfirm',
        meta: { title: '订单核销' },
        name:'/pages/reserve/reserveConfirm',
        component: () => import('@/views/scm/reserveConfirmList.vue')
        },
]
