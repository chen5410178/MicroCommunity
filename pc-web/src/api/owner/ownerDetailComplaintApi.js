import request from '@/utils/request'

export function listComplaints(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/complaint.listComplaints',
      method: 'get',
      params
    })
      .then(response => {
        resolve(response.data)
      })
      .catch(error => {
        reject(error)
      })
  })
}

export function listRunWorkflowImage(params) {
  return new Promise((resolve, reject) => {
    request({
    url: '/complaint/listRunWorkflowImage',
      method: 'get',
      params
    })
    .then(response => {
      resolve(response.data)
    })
    .catch(error => {
      reject(error)
    })
  })
}