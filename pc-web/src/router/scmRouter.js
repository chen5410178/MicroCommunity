export default [
    {
        path: '/pages/scm/couponMarket',
        name: '/pages/scm/couponMarket',
        component: () => import('@/views/scm/couponMarketList.vue')
    },
    {
        path: '/pages/scm/couponPropertyPoolManage',
        name: '/pages/scm/couponPropertyPoolManage',
        component: () => import('@/views/scm/couponPropertyPoolManageList.vue')
    },
    {
        path: '/pages/scm/couponRule',
        name: '/pages/scm/couponRule',
        component: () => import('@/views/scm/couponRuleList.vue')
    },
    {
        path: '/pages/scm/couponPropertyPoolDetail',
        name: '/pages/scm/couponPropertyPoolDetail',
        component: () => import('@/views/scm/couponPropertyPoolDetailList.vue')
    },
    {
        path: '/pages/scm/couponPropertyUserManage',
        name: '/pages/scm/couponPropertyUserManage',
        component: () => import('@/views/scm/couponPropertyUserManageList.vue')
    },
    {
        path: '/pages/scm/couponPropertyUserDetail',
        name: '/pages/scm/couponPropertyUserDetail',
        component: () => import('@/views/scm/couponPropertyUserDetailList.vue')
    },
    {
        path: '/pages/scm/integralConfigManage',
        name: '/pages/scm/integralConfigManage',
        component: () => import('@/views/scm/integralConfigManageList.vue')
    },
    {
        path: '/pages/scm/integralRule',
        name: '/pages/scm/integralRule',
        component: () => import('@/views/scm/integralRuleList.vue')
    },
    {
        path: '/pages/scm/integralGiftDetailManage',
        name: '/pages/scm/integralGiftDetailManage',
        component: () => import('@/views/scm/integralGiftDetailManageList.vue')
    },
    {
        path: '/pages/scm/communityIntegral',
        name: '/pages/scm/communityIntegral',
        component: () => import('@/views/scm/communityIntegralList.vue')
    },
    {
        path: '/pages/scm/gold',
        name: '/pages/scm/gold',
        component: () => import('@/views/scm/goldList.vue')
    },
 
]