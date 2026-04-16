import request from '@/utils/request'

// 查询欠费明细
export function queryOweFeeDetail(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/reportFeeMonthStatistics/queryOweFeeDetail',
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