import request from '@/utils/request'

// 获取缴费汇总列表
export function listFeeSummarys(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/callComponent/feeSummary/list',
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

// 导出Excel
export function exportFeeSummary(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/feeSummary/export',
      method: 'get',
      params,
      responseType: 'blob'
    }).then(response => {
      resolve(response)
    }).catch(error => {
      reject(error)
    })
  })
}