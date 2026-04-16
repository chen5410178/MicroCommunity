export const messages = {
  en: {
    startWork: {
      state: {
        all: 'All',
        W: 'Pending',
        D: 'Processing',
        C: 'Completed'
      },
      search: {
        title: 'Search Conditions',
        workName: 'Work Order Name',
        staffName: 'Handler',
        startTime: 'Start Time',
        endTime: 'End Time'
      },
      list: {
        title: 'Work Order List',
        draft: 'Draft'
      },
      table: {
        workId: 'ID',
        workName: 'Work Order Name',
        typeName: 'Type Name',
        workCycle: 'Cycle',
        startTime: 'Start Time',
        endTime: 'End Time',
        createUser: 'Creator',
        handler: 'Handler',
        ccUser: 'CC User',
        state: 'Status',
        createTime: 'Create Time'
      },
      cycle: {
        once: 'One-time',
        periodic: 'Periodic'
      },
      operation: {
        stop: 'Stop',
        start: 'Start'
      },
      delete: {
        title: 'Confirm Operation',
        confirm: 'Are you sure to delete this work order?',
        success: 'Delete successfully',
        error: 'Delete failed'
      }
    },
  },
  zh: {
    startWork: {
      state: {
        all: '全部',
        W: '待处理',
        D: '处理中',
        C: '处理完成'
      },
      search: {
        title: '查询条件',
        workName: '工单名称',
        staffName: '处理人',
        startTime: '开始时间',
        endTime: '结束时间'
      },
      list: {
        title: '发起工作单',
        draft: '起草'
      },
      table: {
        workId: '编号',
        workName: '工单名称',
        typeName: '类型名称',
        workCycle: '标识',
        startTime: '开始时间',
        endTime: '结束时间',
        createUser: '发起人',
        handler: '处理人',
        ccUser: '抄送人',
        state: '状态',
        createTime: '创建时间'
      },
      cycle: {
        once: '一次性工单',
        periodic: '周期性工单'
      },
      operation: {
        stop: '停止',
        start: '启用'
      },
      delete: {
        title: '请确认您的操作',
        confirm: '确定删除工作单',
        success: '删除成功',
        error: '删除失败'
      }
    }
  }
}