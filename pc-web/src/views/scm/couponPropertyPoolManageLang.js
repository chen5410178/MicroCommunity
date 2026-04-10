export const messages = {
  en: {
    couponPropertyPoolManage: {
      search: {
        title: 'Search Conditions',
        cppId: 'Coupon ID',
        couponName: 'Coupon Name',
        fromType: 'From Type',
        toType: 'To Type'
      },
      list: {
        title: 'Coupon List',
        add: 'Create'
      },
      table: {
        cppId: 'ID',
        couponName: 'Coupon Name',
        fromType: 'From Type',
        toType: 'To Type',
        stock: 'Quantity',
        validityDay: 'Validity',
        day: 'days',
        createTime: 'Create Time',
        gift: 'Manual Gift'
      },
      fromType: {
        market: 'Market Purchase',
        self: 'Self Made'
      },
      toType: {
        shopping: 'Shopping',
        payment: 'Payment',
        repair: 'Repair',
        parking: 'Parking',
        charging: 'Charging'
      },
      add: {
        title: 'Create Coupon',
        couponName: 'Coupon Name',
        couponNamePlaceholder: 'Required, please enter coupon name',
        toType: 'Purpose',
        toTypePlaceholder: 'Required, please select purpose',
        stock: 'Quantity',
        stockPlaceholder: 'Required, please enter quantity',
        validityDay: 'Validity',
        validityDayPlaceholder: 'Required, please enter validity (days)',
        remark: 'Instructions',
        remarkPlaceholder: 'Required, please enter instructions',
        success: 'Create successfully',
        error: 'Create failed'
      },
      edit: {
        title: 'Edit Coupon',
        couponName: 'Coupon Name',
        couponNamePlaceholder: 'Required, please enter coupon name',
        toType: 'Purpose',
        toTypePlaceholder: 'Required, please select purpose',
        stock: 'Quantity',
        stockPlaceholder: 'Required, please enter quantity',
        validityDay: 'Validity',
        validityDayPlaceholder: 'Required, please enter validity (days)',
        remark: 'Instructions',
        remarkPlaceholder: 'Required, please enter instructions',
        success: 'Edit successfully',
        error: 'Edit failed'
      },
      gift: {
        title: 'Gift Coupon',
        couponName: 'Coupon Name',
        giftCount: 'Quantity',
        giftCountPlaceholder: 'Required, please enter quantity',
        tel: 'Owner Phone',
        telPlaceholder: 'Required, please enter phone number',
        submit: 'Gift',
        success: 'Gift successfully',
        error: 'Gift failed'
      },
      delete: {
        title: 'Delete Confirmation',
        confirmText: 'Are you sure to delete this coupon?',
        success: 'Delete successfully',
        error: 'Delete failed'
      },
      validate: {
        couponNameRequired: 'Coupon name is required',
        couponNameMaxLength: 'Coupon name cannot exceed 64 characters',
        toTypeRequired: 'Purpose is required',
        stockRequired: 'Quantity is required',
        validityDayRequired: 'Validity is required',
        remarkRequired: 'Instructions is required',
        cppIdRequired: 'Coupon ID is required',
        giftCountRequired: 'Quantity is required',
        telRequired: 'Phone number is required'
      },
      fetchError: 'Failed to get coupon list'
    }
  },
  zh: {
    couponPropertyPoolManage: {
      search: {
        title: '查询条件',
        cppId: '优惠券编号',
        couponName: '优惠券名称',
        fromType: '来自方式',
        toType: '用途'
      },
      list: {
        title: '优惠券列表',
        add: '制作'
      },
      table: {
        cppId: '编号',
        couponName: '优惠券名称',
        fromType: '来自方式',
        toType: '用途',
        stock: '数量',
        validityDay: '有效期',
        day: '天',
        createTime: '创建时间',
        gift: '手工赠送'
      },
      fromType: {
        market: '优惠券市场购买',
        self: '自己制作'
      },
      toType: {
        shopping: '购物',
        payment: '缴费',
        repair: '维修劵',
        parking: '停车劵',
        charging: '充电桩充电劵'
      },
      add: {
        title: '制作优惠券',
        couponName: '优惠券名称',
        couponNamePlaceholder: '必填，请填写优惠券名称',
        toType: '用途',
        toTypePlaceholder: '必填，请选择用途',
        stock: '数量',
        stockPlaceholder: '必填，请填写数量',
        validityDay: '有效期',
        validityDayPlaceholder: '必填，请填写有效期(天)',
        remark: '使用说明',
        remarkPlaceholder: '必填，请填写使用说明',
        success: '制作成功',
        error: '制作失败'
      },
      edit: {
        title: '修改优惠券',
        couponName: '优惠券名称',
        couponNamePlaceholder: '必填，请填写优惠券名称',
        toType: '用途',
        toTypePlaceholder: '必填，请选择用途',
        stock: '数量',
        stockPlaceholder: '必填，请填写数量',
        validityDay: '有效期',
        validityDayPlaceholder: '必填，请填写有效期(天)',
        remark: '使用说明',
        remarkPlaceholder: '必填，请填写使用说明',
        success: '修改成功',
        error: '修改失败'
      },
      gift: {
        title: '赠送优惠券',
        couponName: '优惠券名称',
        giftCount: '数量',
        giftCountPlaceholder: '必填，请填写数量',
        tel: '业主手机号',
        telPlaceholder: '必填，请填写手机号',
        submit: '赠送',
        success: '赠送成功',
        error: '赠送失败'
      },
      delete: {
        title: '删除确认',
        confirmText: '确定删除优惠券?',
        success: '删除成功',
        error: '删除失败'
      },
      validate: {
        couponNameRequired: '优惠券名称不能为空',
        couponNameMaxLength: '优惠券名称不能超过64个字符',
        toTypeRequired: '用途不能为空',
        stockRequired: '数量不能为空',
        validityDayRequired: '有效期不能为空',
        remarkRequired: '使用说明不能为空',
        cppIdRequired: '优惠券编号不能为空',
        giftCountRequired: '数量不能为空',
        telRequired: '手机号不能为空'
      },
      fetchError: '获取优惠券列表失败'
    }
  }
}