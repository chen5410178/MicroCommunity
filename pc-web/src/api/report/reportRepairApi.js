import request from '@/utils/request'

// 查询报修汇总数据
export function queryRepair(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/reportFeeMonthStatistics/queryRepair',
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

// 导出报修数据
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

// 获取已入驻小区列表
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