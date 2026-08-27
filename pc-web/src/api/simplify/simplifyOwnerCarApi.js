import request from '@/utils/request'

export function queryOwnerCars(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/owner.queryOwnerCars',
      method: 'get',
      params
    }).then(response => {
      const res = response.data
      if (res.code == 0) {
        resolve(res)
      } else {
        reject(new Error(res.msg || '查询业主车辆失败'))
      }
    }).catch(error => {
      reject(error)
    })
  })
}
