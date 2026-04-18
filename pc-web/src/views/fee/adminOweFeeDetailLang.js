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
  },
  'zh-TW': {
    adminOweFeeDetail: {
      search: {
        title: '查詢條件',
        feeTypeCd: '請選擇費用大類',
        objNameLike: '請填寫房屋編號',
        startTime: '請選擇開始時間',
        endTime: '請選擇結束時間',
        ownerNameLike: '請填寫業主名稱'
      },
      list: {
        title: '欠費明細表'
      },
      table: {
        index: '費用編號',
        communityName: '小區名稱',
        objName: '房號',
        ownerName: '業主',
        ownerTel: '業主電話',
        builtUpArea: '面積',
        feeName: '費用項',
        startTime: '開始時間',
        endTime: '結束時間',
        oweDay: '欠費時長(天)',
        oweAmount: '欠費金額'
      },
      summary: {
        subtotal: '小計',
        total: '大計',
        owe: '欠費',
        yuan: '元'
      },
      tips: {
        startTime: '費用開始時間：所創建費用的計費起始時間',
        oweDayDeposit: '欠費時長（天）：押金費用項欠費時長是費用開始時間到當天的天數',
        oweDayOther: '除押金外的費用項欠費時長是費用的開始時間到費用的結束時間的天數',
        oweAmount: '欠費金額：欠費週期內應繳費用'
      },
      feeType: {
        property: '物業費',
        parking: '停車費'
      },
      fetchError: '獲取欠費明細失敗'
    }
  }
}