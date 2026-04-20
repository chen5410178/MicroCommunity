export default [
    {
        path: '/pages/car/adminCar',
        meta: { title: '车辆信息' },
        name: '/pages/car/adminCar',
        component: () => import('@/views/car/adminCarList.vue')
      },
      {
        path: '/pages/iot/adminCarInout',
        meta: { title: '车辆进出' },
        name: '/pages/iot/adminCarInout',
        component: () => import('@/views/iot/adminCarInoutList.vue')
      },
      {
        path: '/pages/property/parkingAreaManage',
        meta: { title: '停车场管理' },
        name: '/pages/property/parkingAreaManage',
        component: () => import('@/views/car/parkingAreaManageList.vue')
      },
      {
        path: '/pages/property/listParkingSpace',
        meta: { title: '车位信息' },
        name: '/pages/property/listParkingSpace',
        component: () => import('@/views/car/listParkingSpaceList.vue')
      },
      {
        path: '/pages/property/parkingBoxManage',
        meta: { title: '岗亭管理' },
        name: '/pages/property/parkingBoxManage',
        component: () => import('@/views/car/parkingBoxManageList.vue')
      },
      {
        path: '/pages/property/listOwnerCar',
        meta: { title: '业主车辆' },
        name: '/pages/property/listOwnerCar',
        component: () => import('@/views/car/listOwnerCarList.vue')
      },
      {
        path: '/views/car/hireParkingSpace',
        meta: { title: '租车位' },
        name: '/views/car/hireParkingSpace',
        component: () => import('@/views/car/hireParkingSpaceList.vue')
      },
      {
        path: '/views/car/carAddParkingSpace',
        meta: { title: '添加车位' },
        name: '/views/car/carAddParkingSpace',
        component: () => import('@/views/car/carAddParkingSpaceList.vue')
      },
      {
        path: '/views/fee/buyCarMonthCard',
        meta: { title: '购买月卡' },
        name: '/views/fee/buyCarMonthCard',
        component: () => import('@/views/fee/buyCarMonthCardList.vue')
      },
      {
        path: '/pages/property/carInoutManage',
        meta: { title: '进场记录' },
        name: '/pages/property/carInoutManage',
        component: () => import('@/views/car/carInoutManageList.vue')
      },
      {
        path: '/pages/property/carInManage',
        meta: { title: '在场车辆' },
        name: '/pages/property/carInManage',
        component: () => import('@/views/car/carInManageList.vue')
      },
      {
        path: '/pages/property/remainingParkingSpace',
        meta: { title: '剩余车位' },
        name: '/pages/property/remainingParkingSpace',
        component: () => import('@/views/car/remainingParkingSpaceList.vue')
      },
      {
        path: '/pages/car/tempCarPayment',
        meta: { title: '临时车缴费' },
        name: '/pages/car/tempCarPayment',
        component: () => import('@/views/car/tempCarPaymentList.vue')
      },
      {
        path: '/pages/property/parkingSpaceApplyManage',
        meta: { title: '车位申请' },
        name: '/pages/property/parkingSpaceApplyManage',
        component: () => import('@/views/car/parkingSpaceApplyManageList.vue')
      },
      {
        path: '/views/car/addParkingSpaceApply',
        meta: { title: '添加车位申请' },
        name: '/views/car/addParkingSpaceApply',
        component: () => import('@/views/car/addParkingSpaceApply.vue')
      },
      {
        path: '/views/car/auditParkingSpaceApply',
        meta: { title: '审核车位申请' },
        name: '/views/car/auditParkingSpaceApply',
        component: () => import('@/views/car/auditParkingSpaceApply.vue')
      },
      {
        path:'/views/car/carDetail',
        meta: { title: '车辆详情' },
        name:'/views/car/carDetail',
        component: () => import('@/views/car/carDetail.vue')
        },

]