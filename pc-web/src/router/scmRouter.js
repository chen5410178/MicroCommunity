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
    }
]
