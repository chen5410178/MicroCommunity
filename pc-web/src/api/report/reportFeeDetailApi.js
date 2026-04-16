import request from '@/utils/request'

// 查询房屋费用明细
export function queryReportFeeDetailRoom(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/reportFeeMonthStatistics.queryReportFeeDetailRoom',
      method: 'get',
      params
    }).then(response => {
      const res = response.data
      resolve({
        data: res.data,
        total: res.total
      })
    }).catch(error => {
      reject(error)
    })
  })
}

// 查询业主费用明细
export function queryReportFeeDetailOwner(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/reportFeeMonthStatistics.queryReportFeeDetailOwner',
      method: 'get',
      params
    }).then(response => {
      const res = response.data
      resolve({
        data: res.data,
        total: res.total
      })
    }).catch(error => {
      reject(error)
    })
  })
}

// 查询合同费用明细
export function queryReportFeeDetailContract(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/reportFeeMonthStatistics.queryReportFeeDetailContract',
      method: 'get',
      params
    }).then(response => {
      const res = response.data
      resolve({
        data: res.data,
        total: res.total
      })
    }).catch(error => {
      reject(error)
    })
  })
}

// 查询车辆费用明细
export function queryReportFeeDetailCar(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/reportFeeMonthStatistics.queryReportFeeDetailCar',
      method: 'get',
      params
    }).then(response => {
      const res = response.data
      resolve({
        data: res.data,
        total: res.total
      })
    }).catch(error => {
      reject(error)
    })
  })
}

// 导出房屋费用明细
export function exportReportFeeDetailRoom(params) {
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

// 导出业主费用明细
export function exportReportFeeDetailOwner(params) {
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

// 导出合同费用明细
export function exportReportFeeDetailContract(params) {
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

// 导出车辆费用明细
export function exportReportFeeDetailCar(params) {
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

export function loadStaffCommunitys(params) {
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