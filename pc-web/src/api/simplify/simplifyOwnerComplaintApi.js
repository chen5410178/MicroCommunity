import request from '@/utils/request'

export function listComplaints(params) {
    return new Promise((resolve, reject) => {
    request({
      url: '/complaint.listComplaints',
      method: 'get',
      params
    }).then(response => {
      const res = response.data
      if (res.code == 0) {
        resolve(res)
      } else {
        reject(new Error(res.msg || '查询业主投诉失败'))
      }
    }).catch(error => {
      reject(error)
    })
  })
}

export function listRunWorkflowImage(params) {
  return request({
    url: '/workflow.listRunWorkflowImage',
    method: 'get',
    params
  })
}