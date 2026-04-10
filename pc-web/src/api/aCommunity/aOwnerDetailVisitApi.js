import request from '@/utils/request'

export function listAdminVisits(params) {
  return new Promise((resolve, reject) => {
    return request({
    url: '/visit.listAdminVisits',
    method: 'get',
    params
  }).then(res => {
      resolve(res.data)
    }).catch(err => {
      reject(err)
    })
  })
}