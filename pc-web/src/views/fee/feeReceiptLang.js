export const messages = {
  en: {
    feeReceipt: {
      search: {
        title: 'Search Conditions',
        receiptId: 'Please enter receipt ID',
        roomId: 'Please enter room/carport info, format: building-unit-room, e.g. 1-1-1',
        objType: 'Please select fee type',
        all: 'All',
        houseFee: 'House Fee',
        parkingFee: 'Parking Fee',
        startTime: 'Please select start time',
        endTime: 'Please select end time'
      },
      list: {
        title: 'Receipt Information'
      },
      table: {
        receiptId: 'Receipt ID',
        receiptCode: 'Receipt Code',
        objType: 'Fee Type',
        feeName: 'Fee Item',
        roomName: 'Room',
        payObjName: 'Owner',
        carNum: 'Carport',
        amount: 'Total Amount(¥)',
        createTime: 'Payment Time'
      },
      button: {
        reprint: 'Reprint Receipt',
        reprintSmall: 'Reprint Small Receipt'
      },
      fetchError: 'Failed to fetch receipt data'
    }
  },
  zh: {
    feeReceipt: {
      search: {
        title: '查询条件',
        receiptId: '请输入收据ID',
        roomId: '请输入房屋或车位信息，格式为楼栋-单元-房屋，如1-1-1',
        objType: '请选择收费类型',
        all: '全部',
        houseFee: '房屋费',
        parkingFee: '车位费',
        startTime: '请选择开始时间',
        endTime: '请选择结束时间'
      },
      list: {
        title: '收据信息'
      },
      table: {
        receiptId: '收据ID',
        receiptCode: '收据编号',
        objType: '费用类型',
        feeName: '费用项',
        roomName: '房屋',
        payObjName: '业主',
        carNum: '车位',
        amount: '总金额(单位:元)',
        createTime: '缴费时间'
      },
      button: {
        reprint: '补打收据',
        reprintSmall: '补打小票'
      },
      fetchError: '获取收据数据失败'
    }
  }
}