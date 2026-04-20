export default [
    {
        path: '/pages/property/machineTypeTreeManage',
        meta: { title: '设备类型' },
        name: '/pages/property/machineTypeTreeManage',
        component: () => import('@/views/machine/machineTypeTreeManageList.vue')
    },
    {
        path: '/pages/property/equipmentAccount',
        meta: { title: '设备台账' },
        name: '/pages/property/equipmentAccount',
        component: () => import('@/views/machine/equipmentAccountList.vue')
    },
    {
        path: '/views/machine/addEquipmentAccount',
        name: '/views/machine/addEquipmentAccount',
        component: () => import('@/views/machine/addEquipmentAccountList.vue')
    },
    {
        path: '/views/machine/editEquipmentAccount',
        name: '/views/machine/editEquipmentAccount',
        component: () => import('@/views/machine/editEquipmentAccountList.vue')
    },
    {
        path: '/views/machine/equipmentAccountDetail',
        meta: { title: '设备详情' },
        name: '/views/machine/equipmentAccountDetail',
        component: () => import('@/views/machine/equipmentAccountDetailList.vue')
    },
    {
        path: '/pages/iot/adminChargeMachine',
        meta: { title: '充电设备' },
        name: '/pages/iot/adminChargeMachine',
        component: () => import('@/views/iot/adminChargeMachineList.vue')
    },
    {
        path: '/pages/property/machineTranslateManage',
        meta: { title: '物联网同步' },
        name: '/pages/property/machineTranslateManage',
        component: () => import('@/views/machine/machineTranslateManageList.vue')
    },
    {
        path: '/pages/machine/accessControlInout',
        meta: { title: '开门记录' },
        name: '/pages/machine/accessControlInout',
        component: () => import('@/views/machine/accessControlInoutList.vue')
    },
    {
        path: '/pages/print/machinePrinterManage',
        meta: { title: '云打印机' },
        name: '/pages/print/machinePrinterManage',
        component: () => import('@/views/machine/machinePrinterManageList.vue')
    },
    {
        path: '/pages/print/printerRule',
        meta: { title: '打印规则' },
        name: '/pages/print/printerRule',
        component: () => import('@/views/machine/printerRuleList.vue')
    },
    {
        path: '/pages/property/videoControl',
        meta: { title: '视频监控' },
        name: '/pages/property/videoControl',
        component: () => import('@/views/machine/videoControlList.vue')
    },
]