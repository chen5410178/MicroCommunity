import request from '@/utils/request'

// 查询已完成访客订单
export function queryFinishVisit(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/visit.queryFinishVisit',
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