export const messages = {
  en: {
    communityFeeSummary: {
      search: {
        title: 'Search Conditions',
        startDate: 'Start Date',
        endDate: 'End Date',
        feeType: 'Fee Type',
        selectFeeType: 'Please select fee type',
        allCommunities: 'All Communities'
      },
      table: {
        title: 'Fee Summary Report',
        tooltip: 'Summarize fee information by month',
        updateDaily: 'Updated daily',
        totalRooms: 'Total Rooms',
        feeRooms: 'Fee Rooms',
        oweRooms: 'Owe Rooms',
        oweFeeFormula: 'History Owe + Current Owe = Total Owe',
        hisOweFee: 'History Owe',
        curOweFee: 'Current Owe',
        totalOweFee: 'Total Owe',
        receivedFeeFormula: 'Owe Recovery + Current Partial + Prepayment = Actual Payment',
        hisReceivedFee: 'Owe Recovery',
        curPartialFee: 'Current Partial',
        preReceivedFee: 'Prepayment',
        totalReceivedFee: 'Actual Payment',
        curReceivableFee: 'Current Receivable',
        curReceivedFee: 'Current Received',
        roomFeeRate: 'Paid Rooms/Fee Rooms = Room Fee Rate',
        feeRate: 'Current Received/Current Receivable = Fee Rate',
        clearRate: 'Owe Recovery/(Owe Recovery + History Owe) = Clear Rate'
      },
      fetchError: 'Failed to fetch fee summary data'
    }
  },
  zh: {
    communityFeeSummary: {
      search: {
        title: '查询条件',
        startDate: '开始时间',
        endDate: '结束时间',
        feeType: '费用大类',
        selectFeeType: '请选择费用大类',
        allCommunities: '全部小区'
      },
      table: {
        title: '费用汇总表',
        tooltip: '以月份为维度，进行费用情况汇总',
        updateDaily: '按天更新',
        totalRooms: '总户数',
        feeRooms: '收费户',
        oweRooms: '欠费户',
        oweFeeFormula: '历史欠费+当期欠费=欠费',
        hisOweFee: '历史欠费',
        curOweFee: '当期欠费',
        totalOweFee: '总欠费',
        receivedFeeFormula: '欠费追回+当期部分+预交=实缴',
        hisReceivedFee: '欠费追回',
        curPartialFee: '当期部分',
        preReceivedFee: '预交',
        totalReceivedFee: '实缴',
        curReceivableFee: '当期应收',
        curReceivedFee: '当期实收',
        roomFeeRate: '已交户/收费户=户收费率',
        feeRate: '当期实收/当期应收=收费率',
        clearRate: '欠费追回/(欠费追回+历史欠费)=清缴率'
      },
      fetchError: '获取费用汇总数据失败'
    }
  }
}