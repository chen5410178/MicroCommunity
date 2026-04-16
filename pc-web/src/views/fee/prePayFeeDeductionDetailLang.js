export const messages = {
  en: {
    prePayFeeDeductionDetail: {
      title: 'Prepaid Fee Deduction',
      table: {
        detailId: 'ID',
        feeName: 'Fee Name',
        preAmount: 'Last Month Balance',
        curAmount: 'Current Month Deduction',
        remainAmount: 'Current Month Balance',
        curOweAmount: 'Current Owe Deduction',
        hisOweAmount: 'Historical Owe Deduction',
        curYearMonth: 'Year Month',
        payerObjName: 'Payer Object',
        ownerName: 'Owner Name',
        ftName: 'Fee Type',
        createTime: 'Deduction Time',
        remark: 'Remark'
      },
      fetchError: 'Failed to get prepaid fee deduction details'
    },

  },
  zh: {
    prePayFeeDeductionDetail: {
      title: '预付扣款',
      table: {
        detailId: '编号',
        feeName: '费用名称',
        preAmount: '上月结存',
        curAmount: '本月抵扣',
        remainAmount: '本月结存',
        curOweAmount: '本欠抵扣',
        hisOweAmount: '历欠抵扣',
        curYearMonth: '年月',
        payerObjName: '费用对象',
        ownerName: '业主名称',
        ftName: '费用类型',
        createTime: '扣款时间',
        remark: '备注'
      },
      fetchError: '获取预付扣款详情失败'
    },
  }
}