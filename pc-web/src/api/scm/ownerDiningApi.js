import request from '@/utils/request'

// 查询业主就餐记录
export function queryOwnerReserveGoods(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/owner.queryOwnerReserveGoods',
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