export const messages = {
  en: {
    adminCarInout: {
      search: {
        title: 'Search Conditions',
        carNum: 'Please enter license plate number',
        state: 'Please select vehicle status',
        startTime: 'Please select start time',
        endTime: 'Please select end time',
        carType: 'Please select license plate type'
      },
      list: {
        title: 'In/Out Records'
      },
      table: {
        photo: 'In Photo',
        communityName: 'Community Name',
        ciId: 'In/Out ID',
        state: 'Vehicle Status',
        carNum: 'License Plate',
        paNum: 'Parking Lot',
        feeName: 'Billing Rule',
        carTypeName: 'Plate Type',
        inTime: 'In Time',
        outTime: 'Out Time',
        parkingTime: 'Parking Time',
        charge: 'Charge Amount',
        remark: 'Remark'
      },
      state: {
        select: 'Please select vehicle status',
        inStatus: 'In Status',
        paid: 'Payment Completed',
        outStatus: 'Out Status',
        repay: 'Payment Timeout Repay',
        in: 'In',
        out: 'Out'
      },
      carType: {
        select: 'Please select license plate type',
        temp: 'Temporary',
        sale: 'Sale',
        monthly: 'Monthly',
        white: 'White List',
        black: 'Black List',
        internal: 'Internal',
        free: 'Free'
      },
      community: {
        all: 'All Communities'
      },
      time: {
        hour: 'hour',
        min: 'min'
      },
      action: {
        out: 'Out'
      },
      fetchError: 'Failed to fetch in/out records'
    }
  },
  zh: {
    adminCarInout: {
      search: {
        title: '查询条件',
        carNum: '请输入车牌号',
        state: '请选择车辆状态',
        startTime: '请选择开始时间',
        endTime: '请选择结束时间',
        carType: '请选择车牌类型'
      },
      list: {
        title: '进出记录'
      },
      table: {
        photo: '进场图',
        communityName: '小区名称',
        ciId: '进出场编号',
        state: '车辆状态',
        carNum: '车牌号',
        paNum: '停车场',
        feeName: '计费规则',
        carTypeName: '车牌类型',
        inTime: '进场时间',
        outTime: '出场时间',
        parkingTime: '停车时间',
        charge: '收费金额',
        remark: '说明'
      },
      state: {
        select: '请选择车辆状态',
        inStatus: '进场状态',
        paid: '支付完成',
        outStatus: '离场状态',
        repay: '支付超时重新支付',
        in: '进场',
        out: '出场'
      },
      carType: {
        select: '请选择车牌类型',
        temp: '临时车',
        sale: '出售车辆',
        monthly: '月租车',
        white: '白名单',
        black: '黑名单',
        internal: '内部车',
        free: '免费车'
      },
      community: {
        all: '全部小区'
      },
      time: {
        hour: '小时',
        min: '分'
      },
      action: {
        out: '出场'
      },
      fetchError: '获取进出记录失败'
    }
  }
}