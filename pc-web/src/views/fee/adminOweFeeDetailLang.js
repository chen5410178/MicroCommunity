export const messages = {
  en: {
    adminOweFeeDetail: {
      search: {
        title: 'Search Conditions',
        feeTypeCd: 'Please select fee type',
        objNameLike: 'Please enter room number',
        startTime: 'Please select start time',
        endTime: 'Please select end time',
        ownerNameLike: 'Please enter owner name'
      },
      list: {
        title: 'Arrears Details'
      },
      table: {
        index: 'No.',
        communityName: 'Community Name',
        objName: 'Room Number',
        ownerName: 'Owner',
        ownerTel: 'Owner Tel',
        builtUpArea: 'Area',
        feeName: 'Fee Item',
        startTime: 'Start Time',
        endTime: 'End Time',
        oweDay: 'Arrears Days',
        oweAmount: 'Arrears Amount'
      },
      summary: {
        subtotal: 'Subtotal',
        total: 'Total',
        owe: 'Arrears',
        yuan: 'Yuan'
      },
      tips: {
        startTime: 'Start time: the billing start time of the created fee',
        oweDayDeposit: 'Arrears days (days): The arrears days of the deposit fee item is the number of days from the start time of the fee to the current day',
        oweDayOther: 'For fee items other than deposits, the arrears days are the number of days from the start time to the end time of the fee',
        oweAmount: 'Arrears amount: the fee payable during the arrears period'
      },
      feeType: {
        property: 'Property Fee',
        parking: 'Parking Fee'
      },
      fetchError: 'Failed to fetch arrears details'
    }
  },
  zh: {
    adminOweFeeDetail: {
      search: {
        title: '查询条件',
        feeTypeCd: '请选择费用大类',
        objNameLike: '请填写房屋编号',
        startTime: '请选择开始时间',
        endTime: '请选择结束时间',
        ownerNameLike: '请填写业主名称'
      },
      list: {
        title: '欠费明细表'
      },
      table: {
        index: '费用编号',
        communityName: '小区名称',
        objName: '房号',
        ownerName: '业主',
        ownerTel: '业主电话',
        builtUpArea: '面积',
        feeName: '费用项',
        startTime: '开始时间',
        endTime: '结束时间',
        oweDay: '欠费时长(天)',
        oweAmount: '欠费金额'
      },
      summary: {
        subtotal: '小计',
        total: '大计',
        owe: '欠费',
        yuan: '元'
      },
      tips: {
        startTime: '费用开始时间：所创建费用的计费起始时间',
        oweDayDeposit: '欠费时长（天）：押金费用项欠费时长是费用开始时间到当天的天数',
        oweDayOther: '除押金外的费用项欠费时长是费用的开始时间到费用的结束时间的天数',
        oweAmount: '欠费金额：欠费周期内应缴费用'
      },
      feeType: {
        property: '物业费',
        parking: '停车费'
      },
      fetchError: '获取欠费明细失败'
    }
  }
}