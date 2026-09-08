export const messages = {
  en: {
    invoiceApply: {
      searchTitle: 'Search Conditions',
      title: 'Invoicing',
      apply: 'Invoicing',
      table: {
        id: 'ID',
        invoiceKind: 'Invoice Kind',
        invoiceType: 'Invoice Type',
        ownerName: 'Owner Name',
        applicant: 'Applicant',
        invoiceName: 'Invoice Title',
        taxNumber: 'Taxpayer ID',
        addressPhone: 'Address & Phone',
        amount: 'Amount',
        invoiceCode: 'Invoice Code',
        status: 'Status',
        applyTime: 'Apply Time'
      },
      states: {
        all: 'All',
        pendingReview: 'Pending Review',
        pendingUpload: 'Pending Upload',
        reviewFailed: 'Review Failed',
        pendingReceive: 'Pending Receive',
        received: 'Received'
      },
      personal: 'Personal',
      company: 'Company',
      invoiceKindAll: 'All',
      normalInvoice: 'Normal Invoice',
      specialInvoice: 'Special VAT Invoice',
      notUploaded: 'Not Uploaded',
      openInvoice: 'Open Invoice',
      uploadInvoice: 'Upload Invoice',
      reUpload: 'Re-upload',
      verify: 'Verify',
      register: 'Register',
      auditSuccess: 'Audit successful',
      invoiceCodePlaceholder: 'Please enter invoice number',
      invoiceKindPlaceholder: 'Please select invoice kind',
      invoiceTypePlaceholder: 'Please select invoice type',
      ownerNamePlaceholder: 'Please enter owner name',
      applicantPlaceholder: 'Please enter applicant',
      phonePlaceholder: 'Please enter applicant phone'
    },
    deleteInvoiceApply: {
      title: 'Confirm Operation',
      confirmMessage: 'Confirm to delete this invoice application?'
    },
    uploadInvoicePhoto: {
      title: 'Upload Invoice',
      invoiceCode: 'Invoice Code',
      invoice: 'Invoice',
      codeRequired: 'Required, please enter invoice code',
      photoRequired: 'Please select invoice photo'
    },
    wirteInvoiceEvent: {
      title: 'Register/Verify',
      type: 'Type',
      remark: 'Remark',
      typeRequired: 'Required, please select type',
      remarkRequired: 'Required, please enter remark',
      receive: 'Receive',
      register: 'Register'
    },
    audit: {
      title: 'Audit Information',
      status: 'Audit Status',
      reason: 'Reason',
      statusRequired: 'Please select audit status',
      reasonRequired: 'Required, please enter reason',
      approve: 'Approve',
      reject: 'Reject'
    },
    uploadImage: {
      sizeLimit: 'Image size cannot exceed 2MB'
    }
  },
  zh: {
    invoiceApply: {
      searchTitle: '查询条件',
      title: '开票',
      apply: '开票',
      table: {
        id: '编号',
        invoiceKind: '发票种类',
        invoiceType: '发票类型',
        ownerName: '业主名称',
        applicant: '申请人',
        invoiceName: '发票名头',
        taxNumber: '纳税人识别号',
        addressPhone: '地址、电话',
        amount: '申请金额',
        invoiceCode: '发票号',
        status: '审核状态',
        applyTime: '申请时间'
      },
      states: {
        all: '全部',
        pendingReview: '待审核',
        pendingUpload: '待上传',
        reviewFailed: '审核失败',
        pendingReceive: '带领用',
        received: '已领用'
      },
      personal: '个人',
      company: '企业',
      invoiceKindAll: '全部',
      normalInvoice: '普票',
      specialInvoice: '专票',
      notUploaded: '未上传',
      openInvoice: '开票',
      uploadInvoice: '上传发票',
      reUpload: '重新上传',
      verify: '核销',
      register: '登记',
      auditSuccess: '审核成功',
      invoiceCodePlaceholder: '请填写发票号',
      invoiceKindPlaceholder: '请选择发票种类',
      invoiceTypePlaceholder: '请选择发票类型',
      ownerNamePlaceholder: '请填写业主名称',
      applicantPlaceholder: '请填写申请人',
      phonePlaceholder: '请填写申请人电话'
    },
    deleteInvoiceApply: {
      title: '请确认您的操作',
      confirmMessage: '确定删除申请发票'
    },
    uploadInvoicePhoto: {
      title: '上传发票',
      invoiceCode: '发票编号',
      invoice: '发票',
      codeRequired: '必填，请填写发票编号',
      photoRequired: '请选择发票'
    },
    wirteInvoiceEvent: {
      title: '登记核销',
      type: '类型',
      remark: '说明',
      typeRequired: '必填，请选择类型',
      remarkRequired: '必填，请填写说明',
      receive: '领用',
      register: '登记'
    },
    audit: {
      title: '审核信息',
      status: '审核状态',
      reason: '原因',
      statusRequired: '请审核',
      reasonRequired: '必填，请填写原因',
      approve: '同意',
      reject: '拒绝'
    },
uploadImage: {
      sizeLimit: '图片大小不能超过 2MB'
    }
  },
  'zh-TW': {
    invoiceApply: {
      searchTitle: '查詢條件',
      title: '開票',
      apply: '開票',
      table: {
        id: '編號',
        invoiceKind: '發票種類',
        invoiceType: '發票類型',
        ownerName: '業主名稱',
        applicant: '申請人',
        invoiceName: '發票名頭',
        taxNumber: '納稅人識別號',
        addressPhone: '地址、電話',
        amount: '申請金額',
        invoiceCode: '發票號',
        status: '審核狀態',
        applyTime: '申請時間'
      },
      states: {
        all: '全部',
        pendingReview: '待審核',
        pendingUpload: '待上傳',
        reviewFailed: '審核失敗',
        pendingReceive: '待領用',
        received: '已領用'
      },
      personal: '個人',
      company: '企業',
      invoiceKindAll: '全部',
      normalInvoice: '普票',
      specialInvoice: '專票',
      notUploaded: '未上傳',
      openInvoice: '開票',
      uploadInvoice: '上傳發票',
      reUpload: '重新上傳',
      verify: '核銷',
      register: '登記',
      auditSuccess: '審核成功',
      invoiceCodePlaceholder: '請填寫發票號',
      invoiceKindPlaceholder: '請選擇發票種類',
      invoiceTypePlaceholder: '請選擇發票類型',
      ownerNamePlaceholder: '請填寫業主名稱',
      applicantPlaceholder: '請填寫申請人',
      phonePlaceholder: '請填寫申請人電話'
    },
    deleteInvoiceApply: {
      title: '請確認您的操作',
      confirmMessage: '確定刪除申請發票'
    },
    uploadInvoicePhoto: {
      title: '上傳發票',
      invoiceCode: '發票編號',
      invoice: '發票',
      codeRequired: '必填，請填寫發票編號',
      photoRequired: '請選擇發票'
    },
    wirteInvoiceEvent: {
      title: '登記核銷',
      type: '類型',
      remark: '說明',
      typeRequired: '必填，請選擇類型',
      remarkRequired: '必填，請填寫說明',
      receive: '領用',
      register: '登記'
    },
    audit: {
      title: '審核資訊',
      status: '審核狀態',
      reason: '原因',
      statusRequired: '請審核',
      reasonRequired: '必填，請填寫原因',
      approve: '同意',
      reject: '拒絕'
    },
    uploadImage: {
      sizeLimit: '圖片大小不能超過 2MB'
    }
  }
}