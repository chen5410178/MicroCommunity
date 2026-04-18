export const messages = {
  en: {
    adminInspectionPlan: {
      search: {
        title: 'Search Conditions',
        inspectionPlanId: 'Please enter plan ID',
        inspectionPlanName: 'Please enter plan name',
        staffName: 'Please enter inspector',
        state: 'Please select status'
      },
      list: {
        title: 'Inspection Plan'
      },
      table: {
        planName: 'Plan Name',
        communityName: 'Community Name',
        inspectionRoute: 'Plan Route',
        planPeriod: 'Plan Period',
        signType: 'Sign Type',
        dateRange: 'Date Range',
        timeRange: 'Time Range',
        beforeTime: 'Before Time (minutes)',
        creator: 'Creator',
        createTime: 'Create Time',
        state: 'Status',
        inspectionStaff: 'Inspection Staff'
      },
      tip: {
        ensureValid: 'Please ensure the plan start time and end time are valid time ranges and inspectors are set, otherwise inspection tasks cannot be generated'
      },
      community: {
        all: 'All Communities'
      },
      error: {
        fetchListFailed: 'Failed to fetch inspection plan list',
        fetchCommunityFailed: 'Failed to fetch community list',
        fetchStateFailed: 'Failed to fetch status options'
      }
    }
  },
  zh: {
    adminInspectionPlan: {
      search: {
        title: '查询条件',
        inspectionPlanId: '请输入计划ID',
        inspectionPlanName: '请输入计划名称',
        staffName: '请输入巡检人',
        state: '请选择状态'
      },
      list: {
        title: '巡检计划'
      },
      table: {
        planName: '计划名称',
        communityName: '小区名称',
        inspectionRoute: '计划路线',
        planPeriod: '计划周期',
        signType: '签到方式',
        dateRange: '日期范围',
        timeRange: '时间范围',
        beforeTime: '任务提前(分钟)',
        creator: '制定人',
        createTime: '制定时间',
        state: '状态',
        inspectionStaff: '巡检人员'
      },
      tip: {
        ensureValid: '请确保计划开始时间和计划结束时间是有效时间范围，并且设置了巡检人，不然无法生成巡检任务'
      },
      community: {
        all: '全部小区'
      },
      error: {
        fetchListFailed: '获取巡检计划列表失败',
        fetchCommunityFailed: '获取小区列表失败',
fetchStateFailed: '获取状态选项失败'
      }
    }
  },
  'zh-TW': {
    adminInspectionPlan: {
      search: {
        title: '查詢條件',
        inspectionPlanId: '請輸入計劃ID',
        inspectionPlanName: '請輸入計劃名稱',
        staffName: '請輸入巡檢人',
        state: '請選擇狀態'
      },
      list: {
        title: '巡檢計劃'
      },
      table: {
        planName: '計劃名稱',
        communityName: '小區名稱',
        inspectionRoute: '計劃路線',
        planPeriod: '計劃週期',
        signType: '簽到方式',
        dateRange: '日期範圍',
        timeRange: '時間範圍',
        beforeTime: '任務提前(分鐘)',
        creator: '制定人',
        createTime: '制定時間',
        state: '狀態',
        inspectionStaff: '巡檢人員'
      },
      tip: {
        ensureValid: '請確保計劃開始時間和計劃結束時間是有效時間範圍，並且設置了巡檢人，不然無法生成巡檢任務'
      },
      community: {
        all: '全部小區'
      },
      error: {
        fetchListFailed: '獲取巡檢計劃列表失敗',
        fetchCommunityFailed: '獲取小區列表失敗',
        fetchStateFailed: '獲取狀態選項失敗'
      }
    }
  }
}