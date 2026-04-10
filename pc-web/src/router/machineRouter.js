export default [
    {
        path: '/pages/property/machineTypeTreeManage',
        name: '/pages/property/machineTypeTreeManage',
        component: () => import('@/views/machine/machineTypeTreeManageList.vue')
    },
    {
        path: '/pages/property/equipmentAccount',
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
        name: '/views/machine/equipmentAccountDetail',
        component: () => import('@/views/machine/equipmentAccountDetailList.vue')
    },
    {
        path: '/pages/iot/adminChargeMachine',
        name: '/pages/iot/adminChargeMachine',
        component: () => import('@/views/iot/adminChargeMachineList.vue')
    },
    {
        path: '/pages/property/machineTranslateManage',
        name: '/pages/property/machineTranslateManage',
        component: () => import('@/views/machine/machineTranslateManageList.vue')
    },
    {
        path: '/pages/machine/accessControlInout',
        name: '/pages/machine/accessControlInout',
        component: () => import('@/views/machine/accessControlInoutList.vue')
    },
    {
        path: '/pages/print/machinePrinterManage',
        name: '/pages/print/machinePrinterManage',
        component: () => import('@/views/machine/machinePrinterManageList.vue')
    },
    {
        path: '/pages/print/printerRule',
        name: '/pages/print/printerRule',
        component: () => import('@/views/machine/printerRuleList.vue')
    },
    {
        path: '/pages/property/videoControl',
        name: '/pages/property/videoControl',
        component: () => import('@/views/machine/videoControlList.vue')
    },
]