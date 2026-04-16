import request from '@/utils/request'

// 查询费用统计数据
export function queryFeeDataReport(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/dataReport.queryFeeDataReport',
      method: 'get',
      params
    }).then(response => {
      const res = response.data
      resolve(res)
    }).catch(error => {
      reject(error)
    })
  })
}

// 查询工单统计数据
export function queryOrderDataReport(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/dataReport.queryOrderDataReport',
      method: 'get',
      params
    }).then(response => {
      const res = response.data
      resolve(res)
    }).catch(error => {
      reject(error)
    })
  })
}

// 查询出入统计数据
export function queryInoutDataReport(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/dataReport.queryInoutDataReport',
      method: 'get',
      params
    }).then(response => {
      const res = response.data
      resolve(res)
    }).catch(error => {
      reject(error)
    })
  })
}

// 查询其他统计数据
export function queryOthersDataReport(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/dataReport.queryOthersDataReport',
      method: 'get',
      params
    }).then(response => {
      const res = response.data
      resolve(res)
    }).catch(error => {
      reject(error)
    })
  })
}

// 查询实收统计数据
export function queryReceivedStatistics(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/dataReport.queryReceivedStatistics',
      method: 'get',
      params
    }).then(response => {
      const res = response.data
      resolve(res)
    }).catch(error => {
      reject(error)
    })
  })
}

// 查询实收明细统计数据
export function queryReceivedDetailStatistics(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/dataReport.queryReceivedDetailStatistics',
      method: 'get',
      params
    }).then(response => {
      const res = response.data
      resolve(res)
    }).catch(error => {
      reject(error)
    })
  })
}

// 查询收款方式统计数据
export function queryReceivedWayStatistics(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/dataReport.queryReceivedWayStatistics',
      method: 'get',
      params
    }).then(response => {
      const res = response.data
      resolve(res)
    }).catch(error => {
      reject(error)
    })
  })
}

// 查询欠费统计数据
export function queryOweStatistics(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/dataReport.queryOweStatistics',
      method: 'get',
      params
    }).then(response => {
      const res = response.data
      resolve(res)
    }).catch(error => {
      reject(error)
    })
  })
}

// 查询欠费明细统计数据
export function queryOweDetailStatistics(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/dataReport.queryOweDetailStatistics',
      method: 'get',
      params
    }).then(response => {
      const res = response.data
      resolve(res)
    }).catch(error => {
      reject(error)
    })
  })
}

// 查询收缴情况统计数据
export function queryDataReportFeeStatistics(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/dataReport.queryDataReportFeeStatistics',
      method: 'get',
      params
    }).then(response => {
      const res = response.data
      resolve(res)
    }).catch(error => {
      reject(error)
    })
  })
}

// 查询月实收明细数据
export function queryMonthReceivedDetail(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/dataReport.queryMonthReceivedDetail',
      method: 'get',
      params
    }).then(response => {
      const res = response.data
      resolve(res)
    }).catch(error => {
      reject(error)
    })
  })
}

// 查询月欠费明细数据
export function queryMonthOweDetail(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/dataReport.queryMonthOweDetail',
      method: 'get',
      params
    }).then(response => {
      const res = response.data
      resolve(res)
    }).catch(error => {
      reject(error)
    })
  })
}

// 查询楼栋列表
export function queryFloors(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/floor.queryFloors',
      method: 'get',
      params
    }).then(response => {
      const res = response.data
      resolve(res)
    }).catch(error => {
      reject(error)
    })
  })
}

// 查询我入驻的小区列表
export function listMyEnteredCommunitys(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/community.listMyEnteredCommunitys',
      method: 'get',
      params
    }).then(response => {
      const res = response.data
      resolve(res)
    }).catch(error => {
      reject(error)
    })
  })
}

// 导出数据
export function exportData(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/export.exportData',
      method: 'get',
      params
    }).then(response => {
      const res = response.data
      resolve(res)
    }).catch(error => {
      reject(error)
    })
  })
}