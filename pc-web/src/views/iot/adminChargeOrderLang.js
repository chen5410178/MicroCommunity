export const messages = {
  en: {
    adminChargeOrder: {
      search: {
        title: 'Search Conditions',
        orderId: 'Order ID',
        orderIdPlaceholder: 'Please enter order ID',
        personName: 'Name',
        personNamePlaceholder: 'Please enter name',
        personTel: 'Phone',
        personTelPlaceholder: 'Please enter phone number',
        machineName: 'Charger Name',
        machineNamePlaceholder: 'Please enter charger name',
        portName: 'Port',
        portNamePlaceholder: 'Please enter port name',
        state: 'Status',
        statePlaceholder: 'Please select status',
        startTime: 'Start Time',
        startTimePlaceholder: 'Please select start time',
        endTime: 'End Time',
        endTimePlaceholder: 'Please select end time'
      },
      table: {
        title: 'Charging Orders',
        orderId: 'Order ID',
        personName: 'Name',
        personTel: 'Phone',
        machineName: 'Charger',
        portName: 'Port',
        chargeHours: 'Charging Hours',
        byQuantity: 'By Quantity',
        hours: 'hours',
        energy: 'Energy',
        startTime: 'Start Time',
        endTime: 'End Time',
        acctDetailId: 'Account',
        durationPrice: 'Electricity Fee',
        servicePrice: 'Service Fee',
        amount: 'Amount',
        stateName: 'Status',
        remark: 'Remark',
        createTime: 'Create Time'
      },
      state: {
        all: 'All Status',
        charging: 'Charging',
        completed: 'Completed',
        failed: 'Failed'
      },
      community: {
        all: 'All Communities',
        fetchError: 'Failed to load communities'
      },
      fetchError: 'Failed to load charging orders'
    }
  },
  zh: {
    adminChargeOrder: {
      search: {
        title: '查询条件',
        orderId: '订单编号',
        orderIdPlaceholder: '请输入订单编号',
        personName: '名称',
        personNamePlaceholder: '请输入名称',
        personTel: '手机号',
        personTelPlaceholder: '请输入手机号',
        machineName: '充电桩名称',
        machineNamePlaceholder: '请输入充电桩名称',
        portName: '插槽',
        portNamePlaceholder: '请输入插槽',
        state: '状态',
        statePlaceholder: '请选择状态',
        startTime: '开始时间',
        startTimePlaceholder: '请选择开始时间',
        endTime: '结束时间',
        endTimePlaceholder: '请选择结束时间'
      },
      table: {
        title: '充电订单',
        orderId: '编号',
        personName: '名称',
        personTel: '手机号',
        machineName: '充电桩',
        portName: '插槽',
        chargeHours: '充电小时',
        byQuantity: '按量充电',
        hours: '小时',
        energy: '充电量',
        startTime: '开始时间',
        endTime: '结束时间',
        acctDetailId: '扣款账户',
        durationPrice: '电费',
        servicePrice: '服务费',
        amount: '扣款金额',
        stateName: '插座状态',
        remark: '说明',
        createTime: '创建时间'
      },
      state: {
        all: '全部状态',
        charging: '充电中',
        completed: '充电完成',
        failed: '充电失败'
      },
      community: {
        all: '全部小区',
        fetchError: '获取小区列表失败'
      },
      fetchError: '获取充电订单失败'
    }
  }
}