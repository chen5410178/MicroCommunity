export const messages = {
  en: {
    ownerRepairManage: {
      queryCondition: 'Query Condition',
      phoneRepair: 'Phone Repair',
      register: 'Register',
      placeholder: {
        repairId: 'Please input repair id',
        repairName: 'Please input repair name',
        tel: 'Please input tel',
        repairType: 'Please select repair type',
        state: 'Please select state'
      },
      table: {
        repairId: 'Repair Id',
        location: 'Location',
        repairType: 'Repair Type',
        repairName: 'Repair Name',
        tel: 'Tel',
        appointmentTime: 'Appointment Time',
        timeout: 'Timeout',
        createTime: 'Create Time',
        state: 'State',
        operation: 'Operation',
        scheduledTask: '(Scheduled Task)'
      },
      tip1: 'Note: Before using this function, please go to Repair Management;',
      tip2: 'For general owner housing repairs, we must add a non-public area type to report repairs normally.',
      
    },
    addOwnerRepair: {
        title: 'Repair Registration',
        repairScope: 'Repair Scope',
        belongBuilding: 'Belong Building',
        belongUnit: 'Belong Unit',
        belongRoom: 'Belong Room',
        repairType: 'Repair Type',
        repairName: 'Repair Name',
        tel: 'Contact',
        appointmentTime: 'Appointment Time',
        context: 'Repair Content',
        placeholder: {
          appointmentTime: 'Please select appointment time',
          repairScope: 'Please select repair scope',
          repairType: 'Please select repair type',
          repairName: 'Please input repair name',
          tel: 'Please input tel',
          context: 'Please input repair content',
        },
        community: 'Community',
        building: 'Building',
        unit: 'Unit',
        room: 'Room',
        buildingSuffix: ' Building',
        unitSuffix: ' Unit',
        roomSuffix: ' Room'
      },
      editOwnerRepair: {
        title: 'Edit Owner Repair',
        repairType: 'Repair Type',
        repairName: 'Repair Name',
        tel: 'Contact',
        appointmentTime: 'Appointment Time',
        context: 'Repair Content',
        placeholder: {
          appointmentTime: 'Please select appointment time'
        }
      },
      deleteOwnerRepair: {
        title: 'Confirm Operation',
        confirmText: 'Are you sure to delete this repair record?'
      },
  },
  zh: {
    ownerRepairManage: {
      queryCondition: '查询条件',
      phoneRepair: '电话报修',
      register: '登记',
      placeholder: {
        repairId: '请输入工单编号',
        repairName: '请输入报修人',
        tel: '请输入报修电话',
        repairType: '请选择报修类型',
        state: '请选择报修状态'
      },
      table: {
        repairId: '工单编号',
        location: '位置',
        repairType: '报修类型',
        repairName: '报修人',
        tel: '联系方式',
        appointmentTime: '预约时间',
        timeout: '超时时间',
        createTime: '提交时间',
        state: '状态',
        operation: '操作',
        scheduledTask: '(定时任务处理)'
      },
      tip1: '注意：此功能使用前 请先到 报修管理；',
      tip2: '一般业主房屋报修 我们必须要添加一个非公共区域的类型才能正常报修'
    },
    addOwnerRepair: {
        title: '报修登记',
        repairScope: '报修范围',
        belongBuilding: '归属楼栋',
        belongUnit: '归属单元',
        belongRoom: '归属房屋',
        repairType: '报修类型',
        repairName: '报修人',
        tel: '联系方式',
        appointmentTime: '预约时间',
        context: '报修内容',
        placeholder: {
          appointmentTime: '请选择预约时间',
          repairScope: '请选择报修范围',
          repairType: '请选择报修类型',
          repairName: '请输入报修人',
          tel: '请输入联系方式',
          context: '请输入报修内容',
        },
        community: '小区',
        building: '楼栋',
        unit: '单元',
        room: '房屋',
        buildingSuffix: '栋',
        unitSuffix: '单元',
        roomSuffix: '室'
      },
      editOwnerRepair: {
        title: '修改业主报修',
        repairType: '报修类型',
        repairName: '报修人',
        tel: '联系方式',
        appointmentTime: '预约时间',
        context: '报修内容',
        placeholder: {
          appointmentTime: '请选择预约时间'
        }
      },
      deleteOwnerRepair: {
        title: '请确认您的操作',
        confirmText: '确定删除业主报修？'
      },
  }
}