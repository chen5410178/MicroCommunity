export const messages = {
  en: {
    payFeeBatch: {
      search: {
        title: 'Search Conditions',
        batchId: 'Please enter batch number',
        state: 'Please select status',
        createUserName: 'Please enter creator'
      },
      list: {
        title: 'Fee Cancellation'
      },
      table: {
        batchId: 'Batch Number',
        createUserName: 'Employee',
        createTime: 'Time',
        remark: 'Cancellation Reason',
        noRemark: 'None',
        stateName: 'Audit Status',
        msg: 'Audit Opinion',
        operation: 'Operation'
      },
      state: {
        normal: 'Normal',
        applyCancel: 'Apply for Cancellation',
        auditPass: 'Audit Passed',
        auditFail: 'Audit Failed'
      },
      button: {
        applyCancel: 'Apply for Cancellation',
        audit: 'Audit'
      },
      message: {
        auditSuccess: 'Audit successful'
      }
    },
    applyDeleteFeeBatch: {
      title: 'Fee Cancellation Application',
      form: {
        batchId: 'Batch Number',
        createUserName: 'Employee',
        createTime: 'Creation Time',
        remark: 'Cancellation Reason'
      },
      rules: {
        remarkRequired: 'Cancellation reason cannot be empty'
      },
      placeholder: {
        remark: 'Please enter the cancellation reason'
      },
      message: {
        success: 'Cancellation application submitted successfully'
      }
    },
    audit: {
      title: 'Audit Information',
      form: {
        state: 'Audit Status',
        remark: 'Reason'
      },
      state: {
        agree: 'Agree',
        reject: 'Reject'
      },
      rules: {
        stateRequired: 'Audit status cannot be empty',
        remarkRequired: 'Reason cannot be empty',
        remarkMaxLength: 'Reason cannot exceed 200 characters'
      },
      placeholder: {
        state: 'Please audit',
        remark: 'Please enter the reason (required)'
      }
    }
  },
  zh: {
    payFeeBatch: {
      search: {
        title: '查询条件',
        batchId: '请输入批次号',
        state: '请选择状态',
        createUserName: '请填写创建员工'
      },
      list: {
        title: '取消费用'
      },
      table: {
        batchId: '批次号',
        createUserName: '员工',
        createTime: '时间',
        remark: '取消原因',
        noRemark: '无',
        stateName: '审核状态',
        msg: '审核意见',
        operation: '操作'
      },
      state: {
        normal: '正常',
        applyCancel: '申请取消',
        auditPass: '审核通过',
        auditFail: '审核失败'
      },
      button: {
        applyCancel: '申请取消',
        audit: '审核'
      },
      message: {
        auditSuccess: '审核成功'
      }
    },
    applyDeleteFeeBatch: {
      title: '费用取消申请',
      form: {
        batchId: '批次号',
        createUserName: '员工',
        createTime: '创建时间',
        remark: '取消原因'
      },
      rules: {
        remarkRequired: '取消原因不能为空'
      },
      placeholder: {
        remark: '请填写取消原因'
      },
      message: {
        success: '取消申请提交成功'
      }
    },
    audit: {
      title: '审核信息',
      form: {
        state: '审核状态',
        remark: '原因'
      },
      state: {
        agree: '同意',
        reject: '拒绝'
      },
      rules: {
        stateRequired: '审核状态不能为空',
        remarkRequired: '原因内容不能为空',
        remarkMaxLength: '原因内容不能超过200字'
      },
      placeholder: {
state: '请审核',
        remark: '请填写原因(必填)'
      }
    }
  },
  'zh-TW': {
    payFeeBatch: {
      search: {
        title: '查詢條件',
        batchId: '請輸入批次號',
        state: '請選擇狀態',
        createUserName: '請填寫創建員工'
      },
      list: {
        title: '取消費用'
      },
      table: {
        batchId: '批次號',
        createUserName: '員工',
        createTime: '時間',
        remark: '取消原因',
        noRemark: '無',
        stateName: '審核狀態',
        msg: '審核意見',
        operation: '操作'
      },
      state: {
        normal: '正常',
        applyCancel: '申請取消',
        auditPass: '審核通過',
        auditFail: '審核失敗'
      },
      button: {
        applyCancel: '申請取消',
        audit: '審核'
      },
      message: {
        auditSuccess: '審核成功'
      }
    },
    applyDeleteFeeBatch: {
      title: '費用取消申請',
      form: {
        batchId: '批次號',
        createUserName: '員工',
        createTime: '創建時間',
        remark: '取消原因'
      },
      rules: {
        remarkRequired: '取消原因不能為空'
      },
      placeholder: {
        remark: '請填寫取消原因'
      },
      message: {
        success: '取消申請提交成功'
      }
    },
    audit: {
      title: '審核資訊',
      form: {
        state: '審核狀態',
        remark: '原因'
      },
      state: {
        agree: '同意',
        reject: '拒絕'
      },
      rules: {
        stateRequired: '審核狀態不能為空',
        remarkRequired: '原因內容不能為空',
        remarkMaxLength: '原因內容不能超過200字'
      },
      placeholder: {
        state: '請審核',
        remark: '請填寫原因(必填)'
      }
    }
  }
}