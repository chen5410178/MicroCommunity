export const messages = {
  en: {
    returnPayFeeManage: {
      search: {
        title: 'Search Conditions',
        hide: 'Hide',
        more: 'More',
        detailId: 'Payment Order Number',
        orderType: 'Select Fee Type',
        state: 'Select Audit Status',
        query: 'Search',
        reset: 'Reset',
        applyPersonName: 'Please enter applicant',
        auditPersonName: 'Please enter auditor',
        payerObjName: 'Please enter house number, vehicle number, etc.',
        startTime: 'Please enter application start time',
        endTime: 'Please enter application end time'
      },
      list: {
        title: 'Refund Application Form',
        export: 'Export'
      },
      table: {
        refundOrderId: 'Refund Order Number',
        orderType: 'Fee Type',
        payerObjName: 'Payment Object',
        ownerName: 'Owner',
        feeName: 'Fee Item',
        timeRange: 'Time Period',
        amount: 'Amount',
        createTime: 'Application Time',
        reason: 'Refund Reason',
        applyPersonName: 'Applicant',
        state: 'Audit Status',
        auditPersonName: 'Auditor',
        retundRemark: 'Refund Status',
        operation: 'Operation',
        audit: 'Audit',
        retryRefund: 'Retry Refund',
        detail: 'Details'
      },
      fetchError: 'Failed to fetch refund application list',
      auditSuccess: 'Audit successful',
      auditError: 'Audit failed',
      refundSubmit: 'Refund submitted',
      refundError: 'Refund failed'
    },
    audit: {
      title: 'Audit Information',
      state: 'Audit Status',
      selectState: 'Please Audit',
      approve: 'Approve',
      reject: 'Reject',
      remark: 'Reason',
      remarkPlaceholder: 'Required, please fill in the reason'
    }
  },
  zh: {
    returnPayFeeManage: {
      search: {
        title: '查询条件',
        hide: '隐藏',
        more: '更多',
        detailId: '缴费单号',
        orderType: '选择费用类型',
        state: '选择审核状态',
        query: '查询',
        reset: '重置',
        applyPersonName: '请填写申请人',
        auditPersonName: '请输入审核人',
        payerObjName: '请填写房屋编号 车辆编号等',
        startTime: '请输入申请开始时间',
        endTime: '请输入申请结束时间'
      },
      list: {
        title: '退费申请单',
        export: '导出'
      },
      table: {
        refundOrderId: '退款单号',
        orderType: '费用类型',
        payerObjName: '付费对象',
        ownerName: '业主',
        feeName: '费用项',
        timeRange: '时间段',
        amount: '金额',
        createTime: '申请时间',
        reason: '退费原因',
        applyPersonName: '申请人',
        state: '审核状态',
        auditPersonName: '审核人',
        retundRemark: '退款情况',
        operation: '操作',
        audit: '审核',
        retryRefund: '重试退款',
        detail: '详情'
      },
      fetchError: '获取退费申请列表失败',
      auditSuccess: '审核成功',
      auditError: '审核失败',
      refundSubmit: '提交退款',
      refundError: '退款失败'
    },
    audit: {
      title: '审核信息',
      state: '审核状态',
      selectState: '请审核',
      approve: '同意',
      reject: '拒绝',
      remark: '原因',
      remarkPlaceholder: '必填，请填写原因'
    }
  }
}