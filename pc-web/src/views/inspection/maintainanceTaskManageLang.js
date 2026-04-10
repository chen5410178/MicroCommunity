export const messages = {
  en: {
    maintainanceTaskManage: {
      searchTitle: 'Search Conditions',
      listTitle: 'Maintenance Tasks',
      planUserNamePlaceholder: 'Please enter executor',
      startTimePlaceholder: 'Please enter actual maintenance start time',
      endTimePlaceholder: 'Please enter actual maintenance end time',
      planNamePlaceholder: 'Please enter maintenance plan name',
      statePlaceholder: 'Please select maintenance status',
      search: 'Search',
      reset: 'Reset',
      taskId: 'Task Code',
      planName: 'Maintenance Plan',
      timeRange: 'Start/End Time',
      actInsTime: 'Actual Maintenance Time',
      originalPlanUser: 'Planned Maintainer',
      currentPlanUser: 'Current Maintainer',
      transferDesc: 'Transfer Description',
      state: 'Maintenance Status',
      operation: 'Operation',
      transfer: 'Transfer',
      detail: 'Detail',
      delete: 'Delete',
      tip: 'Maintenance tasks are automatically generated based on maintenance plans. If not generated, please confirm whether the scheduled task is enabled (and wait about 30 minutes) or whether the maintenance plan is set correctly',
      fetchError: 'Failed to get maintenance tasks'
    },
    maintainanceTaskTransfer: {
      title: 'Transfer',
      transferTarget: 'Transfer Target',
      transferDesc: 'Transfer Description',
      transferDescPlaceholder: 'Required, please fill in the transfer description',
      staffRequired: 'Please select staff',
      descRequired: 'Description cannot be empty',
      descMaxLength: 'Description is too long',
      sameUserError: 'Cannot transfer to current maintainer',
      cancel: 'Cancel',
      submit: 'Submit',
      success: 'Transfer successful',
      error: 'Transfer failed'
    },
    maintainanceTaskDetail: {
      title: 'Task Details',
      machineName: 'Equipment Name',
      standardName: 'Maintenance Standard',
      planUserName: 'Planned Maintainer',
      actUserName: 'Actual Maintainer',
      stateName: 'Task Status',
      description: 'Maintenance Situation',
      photos: 'Maintenance Photos',
      createTime: 'Create Time',
      viewDetail: 'View Details',
      fetchError: 'Failed to get task details',
      timeRange: 'Start/End Time',
      actInsTime: 'Actual Maintenance Time',
      planName: 'Maintenance Plan',
    },
    deleteMaintainanceTask: {
      title: 'Confirm Operation',
      confirmText: 'Are you sure to delete this maintenance task?',
      cancel: 'Cancel',
      confirm: 'Confirm',
      success: 'Delete successful',
      error: 'Delete failed'
    },
    chooseOrgTree2: {
      placeholder: 'Required, please select related organization',
      title: 'Select Organization',
      cancel: 'Cancel',
      confirm: 'Confirm'
    },
    staffSelect2: {
      placeholder: 'Required, please select staff'
    },
    viewMap: {
      noLocation: 'No location information available'
    },
    viewImage: {
      noImage: 'No image available',
      loadError: 'Image load failed'
    }
  },
  zh: {
    maintainanceTaskManage: {
      searchTitle: '查询条件',
      listTitle: '保养任务',
      planUserNamePlaceholder: '请输入执行人',
      startTimePlaceholder: '请输入实际保养开始时间',
      endTimePlaceholder: '请输入实际保养结束时间',
      planNamePlaceholder: '请输入保养计划名称',
      statePlaceholder: '请选择保养状态',
      search: '查询',
      reset: '重置',
      taskId: '任务编码',
      planName: '保养计划',
      timeRange: '开始/结束时间',
      actInsTime: '实际保养时间',
      originalPlanUser: '计划保养人',
      currentPlanUser: '当前保养人',
      transferDesc: '转移描述',
      state: '保养状态',
      operation: '操作',
      transfer: '流转',
      detail: '详情',
      delete: '删除',
      tip: '保养任务是根据保养计划，自动生成，如果没有生成 请确认是否开启了定时任务(并等待30分钟左右)，或者保养计划是否设置正确',
      fetchError: '获取保养任务失败'
    },
    maintainanceTaskTransfer: {
      title: '流转',
      transferTarget: '流转对象',
      transferDesc: '流转说明',
      transferDescPlaceholder: '必填，请填写转赠说明',
      staffRequired: '请选择员工',
      descRequired: '说明不能为空',
      descMaxLength: '说明太长',
      sameUserError: '不能流转给当前保养人',
      cancel: '取消',
      submit: '提交',
      success: '流转成功',
      error: '流转失败'
    },
    maintainanceTaskDetail: {
      title: '任务详情',
      machineName: '设备名称',
      standardName: '保养标准',
      planUserName: '计划保养人',
      actUserName: '实际保养人',
      stateName: '任务状态',
      description: '保养情况',
      photos: '保养照片',
      createTime: '创建时间',
      viewDetail: '详情',
      fetchError: '获取任务详情失败',
      timeRange: '开始/结束时间',
      actInsTime: '实际保养时间',
      planName: '保养计划',
    },
    deleteMaintainanceTask: {
      title: '请确认您的操作',
      confirmText: '确定删除保养任务？',
      cancel: '点错了',
      confirm: '确认删除',
      success: '删除成功',
      error: '删除失败'
    },
    chooseOrgTree2: {
      placeholder: '必填，请填写关联组织',
      title: '选择组织',
      cancel: '取消',
      confirm: '确定'
    },
    staffSelect2: {
      placeholder: '必填，请选择员工'
    },
    viewMap: {
      noLocation: '暂无位置信息'
    },
    viewImage: {
      noImage: '暂无图片',
      loadError: '图片加载失败'
    }
  }
}