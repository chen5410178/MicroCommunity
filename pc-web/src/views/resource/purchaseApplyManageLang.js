export const messages = {
  en: {
    purchaseApplyManage: {
      search: {
        title: 'Search Conditions',
        applyOrderId: 'Please enter application order number',
        name: 'Please enter applicant name',
        resName: 'Please enter item name',
        startTime: 'Please select start time',
        endTime: 'Please select end time'
      },
      list: {
        title: 'Purchase Application'
      },
      table: {
        applyOrderId: 'Application Order No.',
        userName: 'Applicant',
        endUserName: 'User',
        createUserName: 'Operator',
        resourceNames: 'Item(Specification)',
        createTime: 'Application Time',
        warehousingWay: 'Purchase Method',
        stateName: 'Approval Status'
      },
      button: {
        purchase: 'Purchase',
        urgentPurchase: 'Urgent Purchase',
        flowChart: 'Flow Chart',
        cancelApply: 'Cancel Application',
        export: 'Export'
      },
      warehousingWay: {
        direct: 'Direct Storage',
        purchase: 'Purchase Storage',
        urgent: 'Urgent Purchase'
      },
      edit: {
        title: 'Edit Purchase Application',
        state: 'Order Status',
        stateRequired: 'Required, please select order status',
        stateOption1: 'Area*Unit Price+Surcharge',
        stateOption2: 'Fixed Fee',
        stateFormatError: 'Order status format error',
        applyOrderIdRequired: 'Order number cannot be empty'
      },
      delete: {
        title: 'Please confirm your operation',
        confirmText: 'Are you sure to cancel the application?',
        success: 'Cancel application successfully',
        error: 'Failed to cancel application'
      }
    }
  },
  zh: {
    purchaseApplyManage: {
      search: {
        title: '查询条件',
        applyOrderId: '请输入申请单号',
        name: '请填写申请人姓名',
        resName: '请填写物品名称',
        startTime: '请选择开始时间',
        endTime: '请选择结束时间'
      },
      list: {
        title: '采购申请'
      },
      table: {
        applyOrderId: '申请单号',
        userName: '申请人',
        endUserName: '使用人',
        createUserName: '操作人',
        resourceNames: '物品(规格)',
        createTime: '申请时间',
        warehousingWay: '采购方式',
        stateName: '审批状态'
      },
      button: {
        purchase: '采购',
        urgentPurchase: '紧急采购',
        flowChart: '流程图',
        cancelApply: '取消申请',
        export: '导出'
      },
      warehousingWay: {
        direct: '直接入库',
        purchase: '采购入库',
        urgent: '紧急采购'
      },
      edit: {
        title: '修改采购申请',
        state: '订单状态',
        stateRequired: '必填，请选择订单状态',
        stateOption1: '面积*单价+附加费',
        stateOption2: '固定费用',
        stateFormatError: '订单状态格式错误',
        applyOrderIdRequired: '订单号不能为空'
      },
      delete: {
        title: '请确认您的操作',
        confirmText: '确定取消申请?',
        success: '取消申请成功',
        error: '取消申请失败'
      }
    }
  }
}