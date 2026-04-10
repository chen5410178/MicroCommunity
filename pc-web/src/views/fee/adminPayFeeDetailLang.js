export const messages = {
  en: {
    adminPayFeeDetail: {
      search: {
        title: 'Search Conditions',
        startTime: 'Payment Start Time',
        startTimePlaceholder: 'Please select payment start time',
        endTime: 'Payment End Time',
        endTimePlaceholder: 'Please select payment end time',
        paymentMethod: 'Payment Method',
        paymentMethodPlaceholder: 'Please select payment method',
        feeStatus: 'Fee Status',
        feeStatusPlaceholder: 'Please select fee status',
        feeType: 'Fee Type',
        feeTypePlaceholder: 'Please select fee type',
        feeStartTime: 'Fee Start Time',
        feeStartTimePlaceholder: 'Please select fee start time',
        feeEndTime: 'Fee End Time',
        feeEndTimePlaceholder: 'Please select fee end time',
        payerObjName: 'Room/Car Number',
        payerObjNamePlaceholder: 'Room number or license plate number'
      },
      list: {
        title: 'Payment Records',
        tooltip: 'All room payment record details'
      },
      table: {
        orderId: 'Order No',
        payerInfo: 'Room/Owner',
        feeInfo: 'Fee Type>Fee Item',
        feeStatus: 'Fee Status',
        paymentMethod: 'Payment Method',
        feePeriod: 'Payment Period',
        paymentTime: 'Payment Time',
        cashier: 'Cashier',
        payableAmount: 'Payable/Receivable Amount(Yuan)',
        receivedAmount: 'Received Amount(Yuan)',
        withholdAmount: 'Account Deduction(Yuan)',
        discountAmount: 'Discount/Deduction Amount(Yuan)',
        giftAmount: 'Gift Amount(Yuan)',
        lateFee: 'Late Fee(Yuan)',
        area: 'Area(sqm)',
        parkingSpace: 'Parking Space',
        remark: 'Remark'
      },
      summary: {
        subtotal: 'Subtotal',
        total: 'Total',
        receivable: 'Receivable',
        received: 'Received',
        discount: 'Discount',
        deduction: 'Deduction',
        gift: 'Gift',
        lateFee: 'Late Fee',
        yuan: 'Yuan',
        discountDesc: 'Discount amount: discount amount generated under discount rules and no arrears discount rules',
        deductionDesc: 'Deduction amount: deduction amount generated under deduction rules',
        giftDesc: 'Gift amount: payable amount of gift months under gift rules',
        lateFeeDesc: 'Late fee: default late fee generated under default late fee rules, additional late fee to be paid'
      },
      allCommunities: 'All Communities',
      fetchError: 'Failed to fetch payment details',
      fetchCommunityError: 'Failed to fetch community list',
      exportSuccess: 'Export started successfully',
      exportError: 'Failed to export data'
    }
  },
  zh: {
    adminPayFeeDetail: {
      search: {
        title: '查询条件',
        startTime: '缴费开始时间',
        startTimePlaceholder: '请选择缴费开始时间',
        endTime: '缴费结束时间',
        endTimePlaceholder: '请选择缴费结束时间',
        paymentMethod: '支付方式',
        paymentMethodPlaceholder: '请选择支付方式',
        feeStatus: '费用状态',
        feeStatusPlaceholder: '请选择费用状态',
        feeType: '费用类型',
        feeTypePlaceholder: '请选择费用类型',
        feeStartTime: '收费开始时间',
        feeStartTimePlaceholder: '请选择收费开始时间',
        feeEndTime: '收费结束时间',
        feeEndTimePlaceholder: '请选择收费结束时间',
        payerObjName: '房屋/车牌号',
        payerObjNamePlaceholder: '房屋编号或者车牌号'
      },
      list: {
        title: '缴费记录',
        tooltip: '所有房屋缴费记录明细'
      },
      table: {
        orderId: '订单号',
        payerInfo: '房号/业主',
        feeInfo: '费用类型>费用项',
        feeStatus: '费用状态',
        paymentMethod: '支付方式',
        feePeriod: '缴费时间段',
        paymentTime: '缴费时间',
        cashier: '收银员',
        payableAmount: '应缴/应收金额(元)',
        receivedAmount: '实收金额(元)',
        withholdAmount: '账户抵扣(元)',
        discountAmount: '优惠/减免金额(元)',
        giftAmount: '赠送金额(元)',
        lateFee: '滞纳金(元)',
        area: '面积(平方米)',
        parkingSpace: '车位',
        remark: '说明'
      },
      summary: {
        subtotal: '小计',
        total: '大计',
        receivable: '应收',
        received: '实收',
        discount: '优惠',
        deduction: '减免',
        gift: '赠送',
        lateFee: '滞纳金',
        yuan: '元',
        discountDesc: '优惠金额：打折规则和打折无欠费规则下产生的优惠金额',
        deductionDesc: '减免金额：减免规则下产生的减免金额',
        giftDesc: '赠送金额：赠送规则下赠送月份应缴的金额',
        lateFeeDesc: '滞纳金：违约滞纳金规则下产生的违约金额，需额外缴纳的滞纳金'
      },
      allCommunities: '全部小区',
      fetchError: '获取缴费明细失败',
      fetchCommunityError: '获取小区列表失败',
      exportSuccess: '导出成功',
      exportError: '导出失败'
    }
  }
}