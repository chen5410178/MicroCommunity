import request from '@/utils/request'

// List report custom component relations
export function listReportCustomComponentRel(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/reportCustomComponentRel.listReportCustomComponentRel',
      method: 'get',
      params
    }).then(response => {
      const res = response.data
      if (res.code === 0) {
        resolve(res)
      } else {
        reject(new Error(res.msg || 'Failed to list report component relations'))
      }
    }).catch(error => {
      reject(error)
    })
  })
}

// Save report custom component relation
export function saveReportCustomComponentRel(data) {
  return new Promise((resolve, reject) => {
    request({
      url: '/reportCustomComponentRel.saveReportCustomComponentRel',
      method: 'post',
      data
    }).then(response => {
      const res = response.data
      if (res.code === 0) {
        resolve(res)
      } else {
        reject(new Error(res.msg || 'Failed to save report component relation'))
      }
    }).catch(error => {
      reject(error)
    })
  })
}

// Delete report custom component relation
export function deleteReportCustomComponentRel(data) {
  return new Promise((resolve, reject) => {
    request({
      url: '/reportCustomComponentRel.deleteReportCustomComponentRel',
      method: 'post',
      data
    }).then(response => {
      const res = response.data
      if (res.code === 0) {
        resolve(res)
      } else {
        reject(new Error(res.msg || 'Failed to delete report component relation'))
      }
    }).catch(error => {
      reject(error)
    })
  })
}

// List report custom components
export function listReportCustomComponent(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/reportCustomComponent.listReportCustomComponent',
      method: 'get',
      params
    }).then(response => {
      const res = response.data
      if (res.code === 0) {
        resolve(res)
      } else {
        reject(new Error(res.msg || 'Failed to list report components'))
      }
    }).catch(error => {
      reject(error)
    })
  })
}