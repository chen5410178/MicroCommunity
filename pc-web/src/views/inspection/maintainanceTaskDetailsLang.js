export const messages = {
  en: {
    maintainanceTaskDetails: {
      search: {
        title: 'Search Conditions',
        planUserName: 'Please enter maintainer',
        maintainanceStartTime: 'Please enter actual maintenance start time',
        maintainanceEndTime: 'Please enter actual maintenance end time',
        maintainancePlanId: 'Please select maintenance plan',
        selectPlan: 'Select plan',
        taskDetailId: 'Please enter task detail ID',
        taskState: 'Please select task status',
        selectState: 'Select status'
      },
      list: {
        title: 'Maintenance Details'
      },
      table: {
        taskDetailId: 'ID',
        machineName: 'Equipment Name',
        planName: 'Plan Name',
        standardName: 'Maintenance Standard',
        planUserName: 'Planned Maintainer',
        planTime: 'Maintainer\nStart/End Time',
        inspectionTime: 'Actual Maintenance Time',
        actUserName: 'Actual Maintainer',
        stateName: 'Task Status',
        description: 'Maintenance Situation',
        photos: 'Maintenance Photos',
        createTime: 'Create Time'
      },
      fetchError: 'Failed to fetch maintenance task details',
      noLocation: 'No location information available'
    }
  },
  zh: {
    maintainanceTaskDetails: {
      search: {
        title: '查询条件',
        planUserName: '请输入保养人',
        maintainanceStartTime: '请输入实际保养开始时间',
        maintainanceEndTime: '请输入实际保养结束时间',
        maintainancePlanId: '请选择保养计划',
        selectPlan: '选择计划',
        taskDetailId: '请输入任务详情ID',
        taskState: '请选择任务状态',
        selectState: '选择状态'
      },
      list: {
        title: '保养明细信息'
      },
      table: {
        taskDetailId: '编号',
        machineName: '设备名称',
        planName: '计划名称',
        standardName: '保养标准',
        planUserName: '计划保养人',
        planTime: '保养人\n开始/结束时间',
        inspectionTime: '实际保养时间',
        actUserName: '实际保养人',
        stateName: '任务状态',
        description: '保养情况',
        photos: '保养照片',
        createTime: '创建时间'
      },
      fetchError: '获取保养任务详情失败',
      noLocation: '暂无位置信息'
    }
  }
}