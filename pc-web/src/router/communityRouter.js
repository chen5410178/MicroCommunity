export default [
    {
        path: '/pages/property/roomStructure',
        meta: { title: '结构图' },
        name: '/pages/property/roomStructure',
        component: () => import('@/views/room/roomStructureList.vue')
    },
    {
        path: '/pages/property/carStructure',
        meta: { title: '车位结构图' },
        name: '/pages/property/carStructure',
        component: () => import('@/views/car/carStructureList.vue')
    },
    {
        path: '/pages/property/propertyRightRegistrationManage',
        meta: { title: '产权登记' },
        name: '/pages/property/propertyRightRegistrationManage',
        component: () => import('@/views/room/propertyRightRegistrationManageList.vue')
    },
    {
        path: '/views/room/listPropertyRightRegistrationDetail',
        meta: { title: '产权详情' },
        name: '/views/room/listPropertyRightRegistrationDetail',
        component: () => import('@/views/room/listPropertyRightRegistrationDetailList.vue')
    },
    {
        path: '/pages/community/communityPublicityManage',
        meta: { title: '小区公示' },
        name: '/pages/community/communityPublicityManage',
        component: () => import('@/views/community/communityPublicityManageList.vue')
    },
    {
        path: '/views/community/addCommunityPublicity',
        meta: { title: '添加公示' },
        name: '/views/community/addCommunityPublicity',
        component: () => import('@/views/community/addCommunityPublicityList.vue')
    },
    {
        path: '/views/community/editCommunityPublicity',
        meta: { title: '编辑公示' },
        name: '/views/community/editCommunityPublicity',
        component: () => import('@/views/community/editCommunityPublicityList.vue')
    },
    {
        path: '/views/room/handover',
        meta: { title: '房屋交接' },
        name: '/views/room/handover',
        component: () => import('@/views/room/handoverList.vue')
    },
    {
        path: '/views/room/ownerExitRoom',
        meta: { title: '业主迁出' },
        name: '/views/room/ownerExitRoom',
        component: () => import('@/views/room/ownerExitRoomList.vue')
    },
    {
        path: '/pages/community/adminRoomDetail',
        meta: { title: '房屋详情' },
        name: '/pages/community/adminRoomDetail',
        component: () => import('@/views/aCommunity/adminRoomDetail.vue')
    },
    {
        path: '/views/aCommunity/adminOwnerDetail',
        meta: { title: '业主详情' },
        name: '/views/aCommunity/adminOwnerDetail',
        component: () => import('@/views/aCommunity/adminOwnerDetail.vue')
    },
    {
        path: '/pages/car/adminCarDetail',
        meta: { title: '车辆详情' },
        name: '/pages/car/adminCarDetail',
        component: () => import('@/views/aCommunity/adminCarDetailList.vue')
    },
    {
        path: '/pages/fee/adminFeeDetail',
        meta: { title: '费用详情' },
        name: '/pages/fee/adminFeeDetail',
        component: () => import('@/views/aCommunity/adminFeeDetailList.vue')
    },
    {
        path: '/views/community/listRoomDecorationRecord',
        meta: { title: '装修记录' },
        name: '/views/community/listRoomDecorationRecord',
        component: () => import('@/views/community/listRoomDecorationRecordList.vue')
    },
    {
        path: '/views/community/listRoomRenovationRecordDetails',
        meta: { title: '装修详情' },
        name: '/views/community/listRoomRenovationRecordDetails',
        component: () => import('@/views/community/listRoomRenovationRecordDetailsList.vue')
    },
    {
        path: '/pages/community/wechatSubscribe',
        meta: { title: '关注用户' },
        name: '/pages/community/wechatSubscribe',
        component: () => import('@/views/community/wechatSubscribeList.vue')
    },
]
