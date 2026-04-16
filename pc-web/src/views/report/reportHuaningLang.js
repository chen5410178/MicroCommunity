export const messages = {
  en: {
    reportHuaning: {
      search: {
        title: 'Search Conditions',
        feeType: 'Please select fee type',
        floor: 'Please select floor'
      },
      tab: {
        oweFee: 'Uncollected Situation Report',
        oweFeeDetail: 'Uncollected Details Report',
        payFee: 'Current Month Collection Report'
      }
    },
    reportHuaningOweFee: {
      table: {
        floorNum: 'Building Number',
        totalOweAmount: 'Total Uncollected Amount (as of {date})',
        oweAmount: 'Uncollected Amount',
        preYearOweAmount: 'Uncollected Amount Before'
      }
    },
    reportHuaningPayFee: {
      table: {
        floorNum: 'Building Number',
        monthlyReceivable: 'Monthly Receivable',
        monthReceived: 'Month Received',
        monthReceivedPart1: 'Month Received belongs to {year} Year 1-{month} Month Part',
        monthReceivedPart2: 'Month Received belongs to {year} Year {month} Month Part',
        monthReceivedPart3: 'Month Received belongs to {nextMonth} Pre-collection Part',
        monthReceivedPart4: 'Month Received belongs to Before {year} Year Part'
      }
    },
    reportHuaningOweFeeDetail: {
      table: {
        roomNum: 'Room Number',
        area: 'Area',
        fee: 'Fee',
        startTime: 'Fee Start Time',
        endTime: 'Fee End Time',
        totalOweAmount: 'Total Uncollected Amount',
        oweAmount: 'Uncollected Amount',
        preYearOweAmount: 'Uncollected Amount Before'
      }
    },
    viewFeeDetail: {
      title: 'Year Payment History',
      table: {
        cycle: 'Cycle (Unit: Month)',
        receivableAmount: 'Receivable Amount (Unit: Yuan)',
        receivedAmount: 'Received Amount (Unit: Yuan)',
        payTime: 'Payment Time',
        startTime: 'Payment Start Time',
        endTime: 'Payment End Time',
        status: 'Status',
        remark: 'Remark'
      }
    }
  },
  zh: {
    reportHuaning: {
      search: {
        title: '查询条件',
        feeType: '请选择收费类型',
        floor: '请选择楼栋'
      },
      tab: {
        oweFee: '未收情况表',
        oweFeeDetail: '未收明细表',
        payFee: '当月收费情况表'
      }
    },
    reportHuaningOweFee: {
      table: {
        floorNum: '楼栋号',
        totalOweAmount: '总未收金额(截止{date})',
        oweAmount: '未收金额',
        preYearOweAmount: '年前未收金额'
      }
    },
    reportHuaningPayFee: {
      table: {
        floorNum: '楼栋号',
        monthlyReceivable: '每月应收',
        monthReceived: '月实收',
        monthReceivedPart1: '月实收中属于{year}年1-{month}月部分',
        monthReceivedPart2: '月实收中属于{year}年{month}月部分',
        monthReceivedPart3: '月实收中属于{nextMonth}起预收部分',
        monthReceivedPart4: '月实收中属于{year}年前部分'
      }
    },
    reportHuaningOweFeeDetail: {
      table: {
        roomNum: '房号',
        area: '面积',
        fee: '费用',
        startTime: '费用开始时间',
        endTime: '费用截止时间',
        totalOweAmount: '总未收金额',
        oweAmount: '未收金额',
        preYearOweAmount: '年前未收金额'
      }
    },
    viewFeeDetail: {
      title: '年缴费历史',
      table: {
        cycle: '周期(单位:月)',
        receivableAmount: '应收金额(单位:元)',
        receivedAmount: '实收金额(单位:元)',
        payTime: '缴费时间',
        startTime: '缴费起始时间',
        endTime: '缴费结束时间',
        status: '状态',
        remark: '备注'
      }
    }
  }
}