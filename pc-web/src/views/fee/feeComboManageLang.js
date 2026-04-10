export const messages = {
  en: {
    feeComboManage: {
      search: {
        title: 'Search Conditions',
        comboName: 'Please enter combo name'
      },
      list: {
        title: 'Fee Combo List'
      },
      table: {
        comboId: 'Combo ID',
        comboName: 'Combo Name',
        createTime: 'Create Time',
        remark: 'Remark'
      },
      operation: {
        feeItems: 'Fee Items'
      },
      remark: {
        line1: 'Note: Fee combos mainly solve the problem of frequent rental changes (such as park properties)',
        line2: 'Multiple fees can be packaged into a fee combo and created as a combo'
      },
      add: {
        title: 'Add Fee Combo',
        comboName: 'Combo Name',
        comboNamePlaceholder: 'Required, please enter combo name',
        feeItems: 'Fee Items',
        feeItemsPlaceholder: 'Optional, please select fee items',
        remark: 'Remark',
        remarkPlaceholder: 'Optional, please enter remark',
        success: 'Add success',
        error: 'Add failed'
      },
      edit: {
        title: 'Edit Fee Combo',
        comboName: 'Combo Name',
        comboNamePlaceholder: 'Required, please enter combo name',
        remark: 'Remark',
        remarkPlaceholder: 'Optional, please enter remark',
        success: 'Edit success',
        error: 'Edit failed'
      },
      delete: {
        title: 'Delete Confirmation',
        confirmMessage: 'Are you sure to delete this fee combo?',
        comboName: 'Combo Name',
        cancel: 'Cancel',
        confirm: 'Confirm Delete',
        success: 'Delete success',
        error: 'Delete failed'
      },
      validate: {
        comboNameRequired: 'Combo name is required',
        comboNameMaxLength: 'Combo name cannot exceed 30 characters',
        feeItemsRequired: 'Please select at least one fee item',
        comboIdRequired: 'Combo ID is required'
      },
      fetchError: 'Failed to fetch data',
      fetchConfigError: 'Failed to fetch fee configs'
    }
  },
  zh: {
    feeComboManage: {
      search: {
        title: '查询条件',
        comboName: '请输入套餐名称'
      },
      list: {
        title: '费用套餐列表'
      },
      table: {
        comboId: '套餐ID',
        comboName: '套餐名称',
        createTime: '创建时间',
        remark: '说明'
      },
      operation: {
        feeItems: '费用项'
      },
      remark: {
        line1: '说明：费用套餐主要解决房屋出租频繁（园区类物业）多个费用创建时，',
        line2: '可以选择将多个费用打包为一个费用套餐，按套餐创建费用'
      },
      add: {
        title: '添加费用套餐',
        comboName: '套餐名称',
        comboNamePlaceholder: '必填，请填写套餐名称',
        feeItems: '费用项',
        feeItemsPlaceholder: '选填，请选择费用项',
        remark: '说明',
        remarkPlaceholder: '选填，请填写说明',
        success: '添加成功',
        error: '添加失败'
      },
      edit: {
        title: '修改费用套餐',
        comboName: '套餐名称',
        comboNamePlaceholder: '必填，请填写套餐名称',
        remark: '说明',
        remarkPlaceholder: '选填，请填写说明',
        success: '修改成功',
        error: '修改失败'
      },
      delete: {
        title: '删除确认',
        confirmMessage: '确定删除此费用套餐吗？',
        comboName: '套餐名称',
        cancel: '取消',
        confirm: '确认删除',
        success: '删除成功',
        error: '删除失败'
      },
      validate: {
        comboNameRequired: '套餐名称不能为空',
        comboNameMaxLength: '套餐名称不能超过30个字符',
        feeItemsRequired: '请至少选择一个费用项',
        comboIdRequired: '套餐ID不能为空'
      },
      fetchError: '获取数据失败',
      fetchConfigError: '获取费用项失败'
    }
  }
}