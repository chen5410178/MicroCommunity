import request from '@/utils/request'

export function listAdminComplaints(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/complaint.listAdminComplaints',
      method: 'get',
      params
    }).then(response => {
      const res = response.data
      if (res.code === 0) {
        resolve(res)
      } else {
        reject(new Error(res.msg || 'Failed to get complaint list'))
      }
    }).catch(error => {
      reject(error)
    })
  })
}