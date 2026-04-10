export default [
    {
        path: '/pages/property/roomStructure',
        name: '/pages/property/roomStructure',
        component: () => import('@/views/room/roomStructureList.vue')
    },
    {
        path: '/pages/property/carStructure',
        name: '/pages/property/carStructure',
        component: () => import('@/views/car/carStructureList.vue')
    },
    {
        path: '/pages/property/propertyRightRegistrationManage',
        name: '/pages/property/propertyRightRegistrationManage',
        component: () => import('@/views/room/propertyRightRegistrationManageList.vue')
    },
    {
        path: '/views/room/listPropertyRightRegistrationDetail',
        name: '/views/room/listPropertyRightRegistrationDetail',
        component: () => import('@/views/room/listPropertyRightRegistrationDetailList.vue')
    },
    {
        path: '/pages/community/communityPublicityManage',
        name: '/pages/community/communityPublicityManage',
        component: () => import('@/views/community/communityPublicityManageList.vue')
    },
    {
        path: '/views/community/addCommunityPublicity',
        name: '/views/community/addCommunityPublicity',
        component: () => import('@/views/community/addCommunityPublicityList.vue')
    },
    {
        path: '/views/community/editCommunityPublicity',
        name: '/views/community/editCommunityPublicity',
        component: () => import('@/views/community/editCommunityPublicityList.vue')
    },
    {
        path: '/views/room/handover',
        name: '/views/room/handover',
        component: () => import('@/views/room/handoverList.vue')
    },
    {
        path: '/views/room/ownerExitRoom',
        name: '/views/room/ownerExitRoom',
        component: () => import('@/views/room/ownerExitRoomList.vue')
    },
    {
        path: '/pages/community/adminRoomDetail',
        name: '/pages/community/adminRoomDetail',
        component: () => import('@/views/aCommunity/adminRoomDetail.vue')
    },
    {
        path: '/views/aCommunity/adminOwnerDetail',
        name: '/views/aCommunity/adminOwnerDetail',
        component: () => import('@/views/aCommunity/adminOwnerDetail.vue')
    },
    {
        path: '/pages/car/adminCarDetail',
        name: '/pages/car/adminCarDetail',
        component: () => import('@/views/aCommunity/adminCarDetailList.vue')
    },
    {
        path: '/pages/fee/adminFeeDetail',
        name: '/pages/fee/adminFeeDetail',
        component: () => import('@/views/aCommunity/adminFeeDetailList.vue')
    },
    {
        path: '/views/community/listRoomDecorationRecord',
        name: '/views/community/listRoomDecorationRecord',
        component: () => import('@/views/community/listRoomDecorationRecordList.vue')
    },
    {
        path: '/views/community/listRoomRenovationRecordDetails',
        name: '/views/community/listRoomRenovationRecordDetails',
        component: () => import('@/views/community/listRoomRenovationRecordDetailsList.vue')
    },
    {
        path: '/pages/community/wechatSubscribe',
        name: '/pages/community/wechatSubscribe',
        component: () => import('@/views/community/wechatSubscribeList.vue')
    },
]