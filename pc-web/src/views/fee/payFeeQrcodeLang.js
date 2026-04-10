export const messages = {
  en: {
    payFeeQrcode: {
      search: {
        title: 'Search Condition',
        qrcodeName: 'QR Code Name'
      },
      list: {
        title: 'Payment QR Code List'
      },
      table: {
        qrcodeName: 'Name',
        queryWay: 'Query Method',
        smsValidate: 'Validation',
        customFee: 'Custom Payment',
        feeType: 'Display Fee',
        state: 'Status',
        createStaffName: 'Creator',
        createTime: 'Create Time'
      },
      queryWay: {
        byPhone: 'By Owner Phone',
        byHouse: 'By House',
        byBoth: 'By House or Owner Phone'
      },
      feeType: {
        owner: 'Owner Fee',
        house: 'House Fee'
      },
      operation: {
        viewQrcode: 'QR Code'
      },
      form: {
        qrcodeName: 'Name',
        queryWay: 'Query Method',
        smsValidate: 'Validation',
        customFee: 'Custom Payment',
        preFee: 'Prepayment',
        content: 'Prompt Content'
      },
      placeholder: {
        qrcodeName: 'Please enter name',
        queryWay: 'Please select query method',
        smsValidate: 'Please select validation',
        customFee: 'Please select custom payment',
        preFee: 'Please select prepayment',
        content: 'Please enter prompt content'
      },
      rules: {
        qrcodeName: 'Name cannot be empty',
        qrcodeNameMax: 'Name cannot exceed 128 characters',
        queryWay: 'Please select query method',
        smsValidate: 'Please select validation',
        customFee: 'Please select custom payment',
        preFee: 'Please select prepayment',
        content: 'Prompt content cannot be empty',
        contentMax: 'Prompt content cannot exceed 512 characters',
        pfqId: 'ID cannot be empty'
      },
      add: {
        title: 'Add Payment QR Code'
      },
      edit: {
        title: 'Edit Payment QR Code'
      },
      delete: {
        title: 'Delete Confirmation',
        confirmText: 'Are you sure to delete this payment QR code?'
      },
      view: {
        title: 'Payment QR Code',
        tip: 'Please take a screenshot and post it at the cashier'
      },
      message: {
        addSuccess: 'Added successfully',
        addFailed: 'Failed to add',
        editSuccess: 'Modified successfully',
        editFailed: 'Failed to modify',
        deleteSuccess: 'Deleted successfully',
        deleteFailed: 'Failed to delete',
        fetchError: 'Failed to get data'
      }
    }
  },
  zh: {
    payFeeQrcode: {
      search: {
        title: '查询条件',
        qrcodeName: '二维码名称'
      },
      list: {
        title: '支付二维码列表'
      },
      table: {
        qrcodeName: '名称',
        queryWay: '查询方式',
        smsValidate: '验证',
        customFee: '自定义缴费',
        feeType: '展示费用',
        state: '状态',
        createStaffName: '创建人',
        createTime: '创建时间'
      },
      queryWay: {
        byPhone: '按业主手机号',
        byHouse: '按房屋',
        byBoth: '按房屋或者业主手机号'
      },
      feeType: {
        owner: '业主费用',
        house: '房屋费用'
      },
      operation: {
        viewQrcode: '二维码'
      },
      form: {
        qrcodeName: '名称',
        queryWay: '查询方式',
        smsValidate: '验证',
        customFee: '自定义缴费',
        preFee: '预交费',
        content: '提示内容'
      },
      placeholder: {
        qrcodeName: '请输入名称',
        queryWay: '请选择查询方式',
        smsValidate: '请选择验证',
        customFee: '请选择自定义缴费',
        preFee: '请选择预交费',
        content: '请输入提示内容'
      },
      rules: {
        qrcodeName: '名称不能为空',
        qrcodeNameMax: '名称不能超过128个字符',
        queryWay: '请选择查询方式',
        smsValidate: '请选择验证',
        customFee: '请选择自定义缴费',
        preFee: '请选择预交费',
        content: '提示内容不能为空',
        contentMax: '提示内容不能超过512个字符',
        pfqId: '编号不能为空'
      },
      add: {
        title: '添加支付二维码'
      },
      edit: {
        title: '修改支付二维码'
      },
      delete: {
        title: '删除确认',
        confirmText: '确定删除该支付二维码吗？'
      },
      view: {
        title: '支付二维码',
        tip: '请截图贴在收银台'
      },
      message: {
        addSuccess: '添加成功',
        addFailed: '添加失败',
        editSuccess: '修改成功',
        editFailed: '修改失败',
        deleteSuccess: '删除成功',
        deleteFailed: '删除失败',
        fetchError: '获取数据失败'
      }
    }
  }
}