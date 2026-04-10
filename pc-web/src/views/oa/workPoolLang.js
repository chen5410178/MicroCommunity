export const messages = {
  en: {
    workPool: {
      search: {
        title: 'Search Conditions',
        workName: 'Work Order Name',
        createUser: 'Creator',
        staffName: 'Processor',
        startTime: 'Start Time',
        endTime: 'End Time'
      },
      list: {
        title: 'Work Order Pool'
      },
      table: {
        id: 'ID',
        workName: 'Work Name',
        typeName: 'Type Name',
        workCycle: 'Cycle Type',
        once: 'One-time',
        cycle: 'Periodic',
        createUser: 'Creator',
        staffName: 'Processor',
        timeRange: 'Time Range',
        status: 'Status',
        createTime: 'Create Time',
        finishTime: 'Finish Time',
        timeout: 'Timeout'
      },
      state: {
        all: 'All',
        pending: 'Pending',
        completed: 'Completed',
        timeout: 'Timeout Work Orders'
      },
      fetchError: 'Failed to fetch work orders'
    }
  },
  zh: {
    workPool: {
      search: {
        title: '查询条件',
        workName: '请输入工单名称',
        createUser: '请输入发起人',
        staffName: '请输入处理人',
        startTime: '请输入开始时间',
        endTime: '请输入结束时间'
      },
      list: {
        title: '工作单池'
      },
      table: {
        id: '编号',
        workName: '工单名称',
        typeName: '类型名称',
        workCycle: '标识',
        once: '一次性工单',
        cycle: '周期性工单',
        createUser: '发起人',
        staffName: '处理人',
        timeRange: '时间段',
        status: '状态',
        createTime: '创建时间',
        finishTime: '完成时间',
        timeout: '超时'
      },
      state: {
        all: '全部',
        pending: '待处理',
        completed: '处理完成',
        timeout: '超时工作单'
      },
      fetchError: '获取工作单列表失败'
    }
  }
}