import request from '@/utils/request'

export function queryAdminHisFee(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/fee.queryAdminHisFee',
      method: 'get',
      params
    }).then(response => {
      const res = response.data
      if (res.code === 0) {
        resolve(res)
      } else {
        reject(new Error(res.msg || '获取费用历史数据失败'))
      }
    }).catch(error => {
      reject(error)
    })
  })
}