export const messages = {
  en: {
    adminFeeConfig: {
      search: {
        title: 'Search Conditions',
        configId: 'Fee Item ID',
        configIdPlaceholder: 'Please enter fee item ID',
        feeName: 'Fee Item',
        feeNamePlaceholder: 'Please enter fee item',
        feeFlag: 'Fee Flag',
        feeFlagPlaceholder: 'Please select fee flag',
        paymentCd: 'Payment Type',
        paymentCdPlaceholder: 'Please select payment type',
        deductFrom: 'Account Deduction',
        deductFromPlaceholder: 'Please select account deduction'
      },
      list: {
        title: 'Fee Items'
      },
      table: {
        configId: 'ID',
        feeTypeCd: 'Fee Type',
        feeName: 'Fee Item',
        feeFlag: 'Fee Flag',
        paymentCd: 'Payment Type',
        paymentCycle: 'Payment Cycle(Month)',
        computingFormula: 'Formula',
        squarePrice: 'Unit Price(Yuan)',
        additionalAmount: 'Additional/Fixed Fee(Yuan)',
        deductFrom: 'Account Deduction',
        state: 'Status'
      },
      description: {
        feeFlag: 'Fee Flag: Divided into periodic fees and one-time fees.',
        oneTimeFee: 'One-time fees are fees that are only charged once.',
        paymentType: 'Payment Type: Divided into prepaid and postpaid.'
      },
      fetchError: 'Failed to fetch fee config data'
    }
  },
  zh: {
    adminFeeConfig: {
      search: {
        title: '查询条件',
        configId: '费用项ID',
        configIdPlaceholder: '请输入费用项ID',
        feeName: '收费项目',
        feeNamePlaceholder: '请输入收费项目',
        feeFlag: '费用标识',
        feeFlagPlaceholder: '请选择费用标识',
        paymentCd: '付费类型',
        paymentCdPlaceholder: '请选择付费类型',
        deductFrom: '账户抵扣',
        deductFromPlaceholder: '请选择账户抵扣'
      },
      list: {
        title: '费用项'
      },
      table: {
        configId: '编号',
        feeTypeCd: '费用类型',
        feeName: '收费项目',
        feeFlag: '费用标识',
        paymentCd: '付费类型',
        paymentCycle: '缴费周期(单位:月)',
        computingFormula: '公式',
        squarePrice: '计费单价(单位:元)',
        additionalAmount: '附加/固定费用(单位:元)',
        deductFrom: '账户抵扣',
        state: '状态'
      },
      description: {
        feeFlag: '费用标识：分为周期费用和一次性费用',
        oneTimeFee: '一次性费用表示费用只收取一次',
        paymentType: '付费类型：分为预付费和后付费'
      },
      fetchError: '获取费用配置数据失败'
    }
  }
}