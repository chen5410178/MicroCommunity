export const messages = {
  en: {
    doWork: {
      search: {
        title: 'Search Conditions',
        workNameLike: 'Work Order Name',
        staffNameLike: 'Processor',
        queryStartTime: 'Start Time',
        queryEndTime: 'End Time'
      },
      table: {
        title: 'Work Order List',
        workId: 'ID',
        workName: 'Work Order Name',
        typeName: 'Type Name',
        workCycle: 'Type',
        startTime: 'Start Time',
        endTime: 'End Time',
        createUserName: 'Creator',
        curStaffName: 'Processor',
        curCopyName: 'CC',
        stateName: 'Status',
        createTime: 'Create Time'
      },
      state: {
        all: 'All',
        pending: 'Pending',
        processed: 'Processed'
      },
      workCycle: {
        once: 'One-time Work Order',
        periodic: 'Periodic Work Order'
      },
      fetchError: 'Failed to fetch work orders'
    },
    doCopyWork: {
      detail: {
        title: 'Work Order Details',
        workId: 'Work Order ID',
        workName: 'Work Order Name',
        typeName: 'Type Name',
        workCycle: 'Type',
        startTime: 'Start Time',
        endTime: 'End Time',
        createUserName: 'Creator',
        curStaffName: 'Processor',
        curCopyName: 'CC',
        stateName: 'Status',
        createTime: 'Create Time',
        attachment: 'Attachment',
        download: 'Download'
      },
      table: {
        title: 'Work Task Items',
        select: 'Select',
        content: 'Work Order Content',
        remark: 'Processing Opinion',
        state: 'Status',
        staffName: 'Processor',
        finishTime: 'Completion Time'
      },
      audit: {
        title: 'CC Processing',
        deductionMoney: 'Deduction Amount',
        deductionMoneyPlaceholder: 'Required, please enter deduction amount',
        deductionReason: 'Explanation',
        deductionReasonPlaceholder: 'Required, please enter explanation'
      },
      state: {
        pending: 'Pending',
        processed: 'Processed'
      },
      workCycle: {
        once: 'One-time Work Order',
        periodic: 'Periodic Work Order'
      },
      message: {
        noWorkId: 'Work order ID is missing',
        loadWorkInfoError: 'Failed to load work order information',
        loadTaskItemsError: 'Failed to load task items',
        selectItem: 'Please select a processed task item',
        inputReason: 'Please enter an explanation',
        inputMoney: 'Please enter deduction amount',
        submitSuccess: 'Submitted successfully',
        submitError: 'Submission failed'
      }
    }
  },
  zh: {
    doWork: {
      search: {
        title: '查询条件',
        workNameLike: '工单名称',
        staffNameLike: '处理人',
        queryStartTime: '开始时间',
        queryEndTime: '结束时间'
      },
      table: {
        title: '工单列表',
        workId: '编号',
        workName: '工单名称',
        typeName: '类型名称',
        workCycle: '类型',
        startTime: '开始时间',
        endTime: '结束时间',
        createUserName: '发起人',
        curStaffName: '处理人',
        curCopyName: '抄送人',
        stateName: '状态',
        createTime: '创建时间'
      },
      state: {
        all: '全部',
        pending: '待处理',
        processed: '已处理'
      },
      workCycle: {
        once: '一次性工单',
        periodic: '周期性工单'
      },
      fetchError: '获取工单列表失败'
    },
    doCopyWork: {
      detail: {
        title: '任务单详情',
        workId: '工单编号',
        workName: '工单名称',
        typeName: '类型名称',
        workCycle: '标识',
        startTime: '开始时间',
        endTime: '结束时间',
        createUserName: '发起人',
        curStaffName: '处理人',
        curCopyName: '抄送人',
        stateName: '状态',
        createTime: '创建时间',
        attachment: '附件',
        download: '下载'
      },
      table: {
        title: '工作单内容',
        select: '选择',
        content: '工作单内容',
        remark: '处理意见',
        state: '状态',
        staffName: '处理人',
        finishTime: '完成时间'
      },
      audit: {
        title: '抄送办理',
        deductionMoney: '扣款金额',
        deductionMoneyPlaceholder: '必填，请填写扣款金额',
        deductionReason: '说明',
        deductionReasonPlaceholder: '必填，请填写说明'
      },
      state: {
        pending: '待处理',
        processed: '已处理'
      },
      workCycle: {
        once: '一次性工单',
        periodic: '周期性工单'
      },
      message: {
        noWorkId: '缺少工单编号',
        loadWorkInfoError: '加载工单信息失败',
        loadTaskItemsError: '加载任务明细失败',
        selectItem: '请选择一个已处理的任务明细',
        inputReason: '请输入说明',
        inputMoney: '请输入扣款金额',
        submitSuccess: '提交成功',
submitError: '提交失败'
      }
    }
  },
  'zh-TW': {
    doWork: {
      search: {
        title: '查詢條件',
        workNameLike: '工單名稱',
        staffNameLike: '處理人',
        queryStartTime: '開始時間',
        queryEndTime: '結束時間'
      },
      table: {
        title: '工單列表',
        workId: '編號',
        workName: '工單名稱',
        typeName: '類型名稱',
        workCycle: '類型',
        startTime: '開始時間',
        endTime: '結束時間',
        createUserName: '發起人',
        curStaffName: '處理人',
        curCopyName: '抄送人',
        stateName: '狀態',
        createTime: '創建時間'
      },
      state: {
        all: '全部',
        pending: '待處理',
        processed: '已處理'
      },
      workCycle: {
        once: '一次性工單',
        periodic: '周期性工單'
      },
      fetchError: '獲取工單列表失敗'
    },
    doCopyWork: {
      detail: {
        title: '任務單詳情',
        workId: '工單編號',
        workName: '工單名稱',
        typeName: '類型名稱',
        workCycle: '標識',
        startTime: '開始時間',
        endTime: '結束時間',
        createUserName: '發起人',
        curStaffName: '處理人',
        curCopyName: '抄送人',
        stateName: '狀態',
        createTime: '創建時間',
        attachment: '附件',
        download: '下載'
      },
      table: {
        title: '工作單內容',
        select: '選擇',
        content: '工作單內容',
        remark: '處理意見',
        state: '狀態',
        staffName: '處理人',
        finishTime: '完成時間'
      },
      audit: {
        title: '抄送辦理',
        deductionMoney: '扣款金額',
        deductionMoneyPlaceholder: '必填，請填寫扣款金額',
        deductionReason: '說明',
        deductionReasonPlaceholder: '必填，請填寫說明'
      },
      state: {
        pending: '待處理',
        processed: '已處理'
      },
      workCycle: {
        once: '一次性工單',
        periodic: '周期性工單'
      },
      message: {
        noWorkId: '缺少工單編號',
        loadWorkInfoError: '加載工單信息失敗',
        loadTaskItemsError: '加載任務明細失敗',
        selectItem: '請選擇一個已處理的任務明細',
        inputReason: '請輸入說明',
        inputMoney: '請輸入扣款金額',
        submitSuccess: '提交成功',
        submitError: '提交失敗'
      }
    }
  }
}