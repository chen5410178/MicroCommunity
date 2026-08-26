import request from '@/utils/request'

export function listAdminComplaints(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/complaint.listAdminComplaints',
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