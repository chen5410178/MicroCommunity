export const messages = {
  en: {
    maintainancePlanManage: {
      search: {
        title: 'Search Conditions',
        planName: 'Plan Name',
        state: 'Status',
        allState: 'All Status'
      },
      list: {
        title: 'Maintenance Plan (Generate Tasks on Maintenance Day)'
      },
      table: {
        planName: 'Plan Name',
        standardName: 'Maintenance Standard',
        planPeriod: 'Plan Cycle',
        dateRange: 'Date Range',
        machineCount: 'Equipment Count',
        machine: 'Equipment',
        staffCount: 'Maintenance Staff Count',
        createUser: 'Creator',
        createTime: 'Creation Time',
        state: 'Status'
      },
      button: {
        enable: 'Enable',
        disable: 'Disable'
      },
      tip: 'Please ensure the plan start time and end time are valid and maintenance staff are assigned, otherwise maintenance tasks cannot be generated',
      fetchError: 'Failed to fetch maintenance plans'
    },
    deleteMaintainancePlan: {
      title: 'Please confirm your operation',
      confirm: 'Are you sure to delete this maintenance plan?',
      success: 'Delete successfully',
      error: 'Failed to delete'
    },
    maintainancePlanState: {
      title: 'Please confirm your operation',
      confirm: 'Are you sure to',
      plan: 'this maintenance plan?',
      success: 'Operation successful',
      error: 'Operation failed'
    }
  },
  zh: {
    maintainancePlanManage: {
      search: {
        title: '查询条件',
        planName: '计划名称',
        state: '状态',
        allState: '全部状态'
      },
      list: {
        title: '保养计划(保养日生成任务)'
      },
      table: {
        planName: '计划名称',
        standardName: '保养标准',
        planPeriod: '计划周期',
        dateRange: '日期范围',
        machineCount: '设备数',
        machine: '设备',
        staffCount: '保养人数',
        createUser: '制定人',
        createTime: '制定时间',
        state: '状态'
      },
      button: {
        enable: '启用',
        disable: '停用'
      },
      tip: '请确保计划开始时间和计划结束时间是有效时间范围，并且设置了保养人，不然无法生成保养任务',
      fetchError: '获取保养计划失败'
    },
    deleteMaintainancePlan: {
      title: '请确认您的操作',
      confirm: '确定删除保养计划?',
      success: '删除成功',
      error: '删除失败'
    },
    maintainancePlanState: {
      title: '请确认您的操作',
      confirm: '确定',
      plan: '保养计划?',
      success: '操作成功',
error: '操作失败'
    }
  },
  'zh-TW': {
    maintainancePlanManage: {
      search: {
        title: '查詢條件',
        planName: '計劃名稱',
        state: '狀態',
        allState: '全部狀態'
      },
      list: {
        title: '保養計劃(保養日生成任務)'
      },
      table: {
        planName: '計劃名稱',
        standardName: '保養標準',
        planPeriod: '計劃週期',
        dateRange: '日期範圍',
        machineCount: '設備數',
        machine: '設備',
        staffCount: '保養人數',
        createUser: '制定人',
        createTime: '制定時間',
        state: '狀態'
      },
      button: {
        enable: '啟用',
        disable: '停用'
      },
      tip: '請確保計劃開始時間和計劃結束時間是有效時間範圍，並且設置了保養人，不然無法生成保養任務',
      fetchError: '獲取保養計劃失敗'
    },
    deleteMaintainancePlan: {
      title: '請確認您的操作',
      confirm: '確定刪除保養計劃?',
      success: '刪除成功',
      error: '刪除失敗'
    },
    maintainancePlanState: {
      title: '請確認您的操作',
      confirm: '確定',
      plan: '保養計劃?',
      success: '操作成功',
      error: '操作失敗'
    }
  }
}