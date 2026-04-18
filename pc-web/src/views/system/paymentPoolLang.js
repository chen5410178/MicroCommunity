export const messages = {
  en: {
    paymentPool: {
      search: {
        title: 'Search Conditions',
        paymentName: 'Payment Name',
        paymentType: 'Payment Vendor',
        state: 'Status',
        allStatus: 'All Status',
        enable: 'Enable',
        disable: 'Disable'
      },
      list: {
        title: 'Payment Configuration'
      },
      table: {
        paymentName: 'Name',
        paymentType: 'Payment Vendor',
        payRange: 'Payment Range',
        communityFee: 'Community Fee',
        tempCarFee: 'Temporary Car Fee',
        specifiedFee: 'Specified Fee',
        state: 'Status',
        createTime: 'Create Time',
        remark: 'Instruction',
        enable: 'Enable',
        disable: 'Disable'
      },
      add: {
        title: 'Add Payment',
        paymentName: 'Name',
        paymentNamePlaceholder: 'Required, please enter name',
        paymentType: 'Payment Vendor',
        paymentTypePlaceholder: 'Required, please select payment vendor',
        certFile: 'Merchant Certificate(.p12)',
        payRange: 'Payment Range',
        payRangePlaceholder: 'Required, please select payment range',
        communityFee: 'Community Fee',
        tempCarFee: 'Temporary Car Fee',
        specifiedFee: 'Specified Fee',
        feeItems: 'Fee Items',
        remark: 'Instruction',
        remarkPlaceholder: 'Optional, please enter instruction',
        success: 'Add payment successfully',
        error: 'Failed to add payment'
      },
      edit: {
        title: 'Edit Payment',
        paymentName: 'Name',
        paymentNamePlaceholder: 'Required, please enter name',
        paymentType: 'Payment Vendor',
        paymentTypePlaceholder: 'Required, please select payment vendor',
        certFile: 'Merchant Certificate(.p12)',
        currentCert: 'Current Certificate',
        payRange: 'Payment Range',
        payRangePlaceholder: 'Required, please select payment range',
        communityFee: 'Community Fee',
        tempCarFee: 'Temporary Car Fee',
        specifiedFee: 'Specified Fee',
        feeItems: 'Fee Items',
        state: 'Status',
        statePlaceholder: 'Required, please select status',
        enable: 'Enable',
        disable: 'Disable',
        remark: 'Instruction',
        remarkPlaceholder: 'Optional, please enter instruction',
        success: 'Edit payment successfully',
        error: 'Failed to edit payment'
      },
      delete: {
        title: 'Delete Confirmation',
        confirmText: 'Are you sure to delete this payment configuration?',
        success: 'Delete payment successfully',
        error: 'Failed to delete payment'
      },
      validate: {
        paymentNameRequired: 'Name is required',
        paymentNameMaxLength: 'Name cannot exceed 64 characters',
        paymentTypeRequired: 'Payment vendor is required',
        payTypeRequired: 'Payment range is required',
        stateRequired: 'Status is required'
      },
      fetchError: 'Failed to fetch payment data'
    },
    uploadFile: {
      sizeLimit: 'File size cannot exceed 20MB',
      success: 'File uploaded successfully',
      error: 'Failed to upload file'
    }
  },
  zh: {
    paymentPool: {
      search: {
        title: '查询条件',
        paymentName: '支付名称',
        paymentType: '支付厂家',
        state: '状态',
        allStatus: '全部状态',
        enable: '启用',
        disable: '停用'
      },
      list: {
        title: '支付配置'
      },
      table: {
        paymentName: '名称',
        paymentType: '支付厂家',
        payRange: '支付范围',
        communityFee: '小区费用',
        tempCarFee: '临时停车费',
        specifiedFee: '指定费用项',
        state: '状态',
        createTime: '创建时间',
        remark: '使用说明',
        enable: '启用',
        disable: '停用'
      },
      add: {
        title: '添加支付',
        paymentName: '名称',
        paymentNamePlaceholder: '必填，请填写名称',
        paymentType: '支付厂家',
        paymentTypePlaceholder: '必填，请选择支付厂家',
        certFile: '商户证书(.p12)',
        payRange: '支付范围',
        payRangePlaceholder: '必填，请选择支付范围',
        communityFee: '小区费用',
        tempCarFee: '临时停车费',
        specifiedFee: '指定费用项',
        feeItems: '费用项',
        remark: '使用说明',
        remarkPlaceholder: '选填，请填写使用说明',
        success: '添加支付成功',
        error: '添加支付失败'
      },
      edit: {
        title: '修改支付',
        paymentName: '名称',
        paymentNamePlaceholder: '必填，请填写名称',
        paymentType: '支付厂家',
        paymentTypePlaceholder: '必填，请选择支付厂家',
        certFile: '商户证书(.p12)',
        currentCert: '当前证书',
        payRange: '支付范围',
        payRangePlaceholder: '必填，请选择支付范围',
        communityFee: '小区费用',
        tempCarFee: '临时停车费',
        specifiedFee: '指定费用项',
        feeItems: '费用项',
        state: '状态',
        statePlaceholder: '必填，请选择状态',
        enable: '启用',
        disable: '停用',
        remark: '使用说明',
        remarkPlaceholder: '选填，请填写使用说明',
        success: '修改支付成功',
        error: '修改支付失败'
      },
      delete: {
        title: '删除确认',
        confirmText: '确定删除该支付配置吗？',
        success: '删除支付成功',
        error: '删除支付失败'
      },
      validate: {
        paymentNameRequired: '名称不能为空',
        paymentNameMaxLength: '名称不能超过64个字符',
        paymentTypeRequired: '支付厂家不能为空',
        payTypeRequired: '支付范围不能为空',
        stateRequired: '状态不能为空'
      },
      fetchError: '获取支付数据失败'
    },
uploadFile: {
      sizeLimit: '文件大小不能超过20MB',
      success: '文件上传成功',
      error: '文件上传失败'
    }
  },
  'zh-TW': {
    paymentPool: {
      search: {
        title: '查詢條件',
        paymentName: '支付名稱',
        paymentType: '支付廠家',
        state: '狀態',
        allStatus: '全部狀態',
        enable: '啟用',
        disable: '停用'
      },
      list: {
        title: '支付配置'
      },
      table: {
        paymentName: '名稱',
        paymentType: '支付廠家',
        payRange: '支付範圍',
        communityFee: '小區費用',
        tempCarFee: '臨時停車費',
        specifiedFee: '指定費用項',
        state: '狀態',
        createTime: '創建時間',
        remark: '使用說明',
        enable: '啟用',
        disable: '停用'
      },
      add: {
        title: '添加支付',
        paymentName: '名稱',
        paymentNamePlaceholder: '必填，請填寫名稱',
        paymentType: '支付廠家',
        paymentTypePlaceholder: '必填，請選擇支付廠家',
        certFile: '商戶證書(.p12)',
        payRange: '支付範圍',
        payRangePlaceholder: '必填，請選擇支付範圍',
        communityFee: '小區費用',
        tempCarFee: '臨時停車費',
        specifiedFee: '指定費用項',
        feeItems: '費用項',
        remark: '使用說明',
        remarkPlaceholder: '選填，請填寫使用說明',
        success: '添加支付成功',
        error: '添加支付失敗'
      },
      edit: {
        title: '修改支付',
        paymentName: '名稱',
        paymentNamePlaceholder: '必填，請填寫名稱',
        paymentType: '支付廠家',
        paymentTypePlaceholder: '必填，請選擇支付廠家',
        certFile: '商戶證書(.p12)',
        currentCert: '當前證書',
        payRange: '支付範圍',
        payRangePlaceholder: '必填，請選擇支付範圍',
        communityFee: '小區費用',
        tempCarFee: '臨時停車費',
        specifiedFee: '指定費用項',
        feeItems: '費用項',
        state: '狀態',
        statePlaceholder: '必填，請選擇狀態',
        enable: '啟用',
        disable: '停用',
        remark: '使用說明',
        remarkPlaceholder: '選填，請填寫使用說明',
        success: '修改支付成功',
        error: '修改支付失敗'
      },
      delete: {
        title: '刪除確認',
        confirmText: '確定刪除該支付配置嗎？',
        success: '刪除支付成功',
        error: '刪除支付失敗'
      },
      validate: {
        paymentNameRequired: '名稱不能為空',
        paymentNameMaxLength: '名稱不能超過64個字符',
        paymentTypeRequired: '支付廠家不能為空',
        payTypeRequired: '支付範圍不能為空',
        stateRequired: '狀態不能為空'
      },
      fetchError: '獲取支付數據失敗'
    },
    uploadFile: {
      sizeLimit: '文件大小不能超過20MB',
      success: '文件上傳成功',
      error: '文件上傳失敗'
    }
  }
}