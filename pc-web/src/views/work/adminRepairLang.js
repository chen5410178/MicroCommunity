export const messages = {
  en: {
    adminRepair: {
      search: {
        title: 'Search Conditions',
        repairId: 'Please enter work order number',
        repairName: 'Please enter repairer name',
        tel: 'Please enter repair phone'
      },
      list: {
        title: 'Community Repair'
      },
      table: {
        repairId: 'Work Order Code',
        communityName: 'Community Name',
        repairObjName: 'Location',
        repairTypeName: 'Repair Type',
        maintenanceType: 'Maintenance Type',
        repairName: 'Repairer',
        tel: 'Contact',
        appointmentTime: 'Appointment Time',
        createTime: 'Submission Time',
        submitHours: 'Submission Duration',
        finishTime: 'Completion Time',
        state: 'Status',
        timedTask: 'Timed Task Processing'
      },
      maintenanceType: {
        paid: 'Paid Service',
        free: 'Free Service',
        withMaterial: 'Need Materials',
        withoutMaterial: 'No Materials Needed'
      },
      fetchError: 'Failed to get repair list',
      treeFetchError: 'Failed to get tree data'
    }
  },
  zh: {
    adminRepair: {
      search: {
        title: '查询条件',
        repairId: '请输入工单编号',
        repairName: '请输入报修人',
        tel: '请输入报修电话'
      },
      list: {
        title: '小区报修'
      },
      table: {
        repairId: '工单编码',
        communityName: '小区名称',
        repairObjName: '位置',
        repairTypeName: '报修类型',
        maintenanceType: '维修类型',
        repairName: '报修人',
        tel: '联系方式',
        appointmentTime: '预约时间',
        createTime: '提交时间',
        submitHours: '提单时长',
        finishTime: '完成时间',
        state: '状态',
        timedTask: '定时任务处理'
      },
      maintenanceType: {
        paid: '有偿服务',
        free: '无偿服务',
        withMaterial: '需要用料',
        withoutMaterial: '无需用料'
      },
      fetchError: '获取报修列表失败',
      treeFetchError: '获取树形数据失败'
    }
  }
}