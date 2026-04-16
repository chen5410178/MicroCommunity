export const messages = {
  en: {
    workDeduction: {
      search: {
        title: 'Search Conditions',
        staffNameLike: 'Please enter handler',
        deductionPersonNameLike: 'Please enter deduction person'
      },
      list: {
        title: 'Work Order Deduction'
      },
      table: {
        staffName: 'Staff Name',
        timeRange: 'Time Range',
        content: 'Content',
        finishTime: 'Completion Time',
        state: 'Status',
        remark: 'Remark',
        score: 'Score',
        deductionMoney: 'Deduction Amount',
        deductionReason: 'Deduction Reason',
        deductionPersonName: 'Deduction Person',
        createTime: 'Time'
      },
      state: {
        W: 'Pending',
        C: 'Handler Completed',
        CC: 'Copier Completed'
      },
      fetchError: 'Failed to fetch work deduction data'
    }
  },
  zh: {
    workDeduction: {
      search: {
        title: '查询条件',
        staffNameLike: '请输入处理人',
        deductionPersonNameLike: '请输入扣款人'
      },
      list: {
        title: '工单扣款'
      },
      table: {
        staffName: '员工名称',
        timeRange: '时间段',
        content: '内容',
        finishTime: '完成时间',
        state: '状态',
        remark: '说明',
        score: '评分',
        deductionMoney: '扣款金额',
        deductionReason: '扣款说明',
        deductionPersonName: '扣款人',
        createTime: '时间'
      },
      state: {
        W: '待处理',
        C: '处理人已办理',
        CC: '抄送人已办理'
      },
      fetchError: '获取工单扣款数据失败'
    }
  }
}