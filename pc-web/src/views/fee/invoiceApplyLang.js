export const messages = {
  en: {
    invoiceApply: {
      searchTitle: 'Search Conditions',
      title: 'Invoice Application',
      apply: 'Apply',
      table: {
        id: 'ID',
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
      notUploaded: 'Not Uploaded',
      openInvoice: 'Open Invoice',
      uploadInvoice: 'Upload Invoice',
      reUpload: 'Re-upload',
      verify: 'Verify',
      register: 'Register',
      auditSuccess: 'Audit successful',
      invoiceCodePlaceholder: 'Please enter invoice number',
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
      title: '申请发票',
      apply: '申请',
      table: {
        id: '编号',
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
      notUploaded: '未上传',
      openInvoice: '开票',
      uploadInvoice: '上传发票',
      reUpload: '重新上传',
      verify: '核销',
      register: '登记',
      auditSuccess: '审核成功',
      invoiceCodePlaceholder: '请填写发票号',
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
  }
}