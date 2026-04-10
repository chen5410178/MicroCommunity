export default [
    {
        path: '/pages/car/adminCar',
        name: '/pages/car/adminCar',
        component: () => import('@/views/car/adminCarList.vue')
      },
      {
        path: '/pages/iot/adminCarInout',
        name: '/pages/iot/adminCarInout',
        component: () => import('@/views/iot/adminCarInoutList.vue')
      },
      {
        path: '/pages/property/parkingAreaManage',
        name: '/pages/property/parkingAreaManage',
        component: () => import('@/views/car/parkingAreaManageList.vue')
      },
      {
        path: '/pages/property/listParkingSpace',
        name: '/pages/property/listParkingSpace',
        component: () => import('@/views/car/listParkingSpaceList.vue')
      },
      {
        path: '/pages/property/parkingBoxManage',
        name: '/pages/property/parkingBoxManage',
        component: () => import('@/views/car/parkingBoxManageList.vue')
      },
      {
        path: '/pages/property/listOwnerCar',
        name: '/pages/property/listOwnerCar',
        component: () => import('@/views/car/listOwnerCarList.vue')
      },
      {
        path: '/views/car/hireParkingSpace',
        name: '/views/car/hireParkingSpace',
        component: () => import('@/views/car/hireParkingSpaceList.vue')
      },
      {
        path: '/views/car/carAddParkingSpace',
        name: '/views/car/carAddParkingSpace',
        component: () => import('@/views/car/carAddParkingSpaceList.vue')
      },
      {
        path: '/views/fee/buyCarMonthCard',
        name: '/views/fee/buyCarMonthCard',
        component: () => import('@/views/fee/buyCarMonthCardList.vue')
      },
      {
        path: '/pages/property/carInoutManage',
        name: '/pages/property/carInoutManage',
        component: () => import('@/views/car/carInoutManageList.vue')
      },
      {
        path: '/pages/property/carInManage',
        name: '/pages/property/carInManage',
        component: () => import('@/views/car/carInManageList.vue')
      },
      {
        path: '/pages/property/remainingParkingSpace',
        name: '/pages/property/remainingParkingSpace',
        component: () => import('@/views/car/remainingParkingSpaceList.vue')
      },
      {
        path: '/pages/car/tempCarPayment',
        name: '/pages/car/tempCarPayment',
        component: () => import('@/views/car/tempCarPaymentList.vue')
      },
      {
        path: '/pages/property/parkingSpaceApplyManage',
        name: '/pages/property/parkingSpaceApplyManage',
        component: () => import('@/views/car/parkingSpaceApplyManageList.vue')
      },
      {
        path: '/views/car/addParkingSpaceApply',
        name: '/views/car/addParkingSpaceApply',
        component: () => import('@/views/car/addParkingSpaceApply.vue')
      },
      {
        path: '/views/car/auditParkingSpaceApply',
        name: '/views/car/auditParkingSpaceApply',
        component: () => import('@/views/car/auditParkingSpaceApply.vue')
      },
      {
        path:'/views/car/carDetail',
        name:'/views/car/carDetail',
        component: () => import('@/views/car/carDetail.vue')
        },

]