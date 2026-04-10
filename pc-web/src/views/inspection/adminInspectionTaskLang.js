export const messages = {
  en: {
    adminInspectionTask: {
      search: {
        title: 'Search Conditions',
        startTime: 'Please enter actual inspection start time',
        endTime: 'Please enter actual inspection end time',
        state: 'Please select inspection status',
        button: 'Search'
      },
      list: {
        title: 'Inspection Tasks',
        tip: 'Inspection tasks are automatically generated at 2:00 am every day according to the inspection plan. If they are not generated, please confirm whether the scheduled task is enabled or whether the inspection plan is set correctly'
      },
      table: {
        taskId: 'Task Code',
        communityName: 'Community Name',
        inspectionPlanName: 'Inspection Plan',
        planTime: 'Plan Time',
        actInsTime: 'Actual Inspection Time',
        originalPlanUserName: 'Original Inspector',
        planUserName: 'Current Inspector',
        transferDesc: 'Transfer Description',
        signTypeName: 'Inspection Method',
        stateName: 'Inspection Status',
        operation: 'Operation',
        detail: 'Detail'
      }
    }
  },
  zh: {
    adminInspectionTask: {
      search: {
        title: '查询条件',
        startTime: '请输入实际巡检开始时间',
        endTime: '请输入实际巡检结束时间',
        state: '请选择巡检状态',
        button: '查询'
      },
      list: {
        title: '巡检任务',
        tip: '巡检任务是根据巡检计划，每天晚上2点钟自动生成，如果没有生成请确认是否开启了定时任务，或者巡检计划是否设置正确'
      },
      table: {
        taskId: '任务编码',
        communityName: '小区名称',
        inspectionPlanName: '巡检计划',
        planTime: '计划时间',
        actInsTime: '实际巡检时间',
        originalPlanUserName: '计划巡检人',
        planUserName: '当前巡检人',
        transferDesc: '转移描述',
        signTypeName: '巡检方式',
        stateName: '巡检状态',
        operation: '操作',
        detail: '详情'
      }
    }
  }
}