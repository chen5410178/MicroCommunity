export const messages = {
  en: {
    adminMeter: {
      search: {
        title: 'Search Conditions',
        machineName: 'Please enter name',
        address: 'Please enter meter number',
        machineModel: 'Please select model',
        roomName: 'Please enter room number (building-unit-room)'
      },
      table: {
        title: 'Smart Water and Electricity Meter',
        machineId: 'ID',
        communityName: 'Community Name',
        machineName: 'Name',
        address: 'Meter Number',
        meterType: 'Meter Type',
        machineModel: 'Model',
        roomName: 'Room',
        implBeanName: 'Manufacturer',
        degree: 'Current Degree',
        curDegrees: 'Remaining Amount',
        curReadingTime: 'Reading Time',
        stateName: 'Status',
        heartbeatTime: 'Heartbeat Time',
        readlingTime: 'Reading on day {day} at {hour}:00 every month'
      },
      model: {
        recharge: 'Recharge Mode',
        reading: 'Reading Mode'
      },
      community: {
        all: 'All Communities',
        fetchError: 'Failed to load communities'
      },
      fetchError: 'Failed to load meter data'
    }
  },
  zh: {
    adminMeter: {
      search: {
        title: '查询条件',
        machineName: '请输入名称',
        address: '请输入表号',
        machineModel: '请选择模式',
        roomName: '请输入房屋编号(楼栋-单元-房屋)'
      },
      table: {
        title: '智能水电表',
        machineId: '编号',
        communityName: '小区名称',
        machineName: '名称',
        address: '表号',
        meterType: '表类型',
        machineModel: '模式',
        roomName: '房屋',
        implBeanName: '设备厂家',
        degree: '当前度数',
        curDegrees: '剩余金额',
        curReadingTime: '读表时间',
        stateName: '状态',
        heartbeatTime: '心跳时间',
        readlingTime: '每月{day}日{hour}时抄表'
      },
      model: {
        recharge: '充值模式',
        reading: '抄表模式'
      },
      community: {
        all: '全部小区',
        fetchError: '加载小区失败'
      },
      fetchError: '加载水电表数据失败'
    }
  }
}