import request from '@/utils/request'

// 获取报表组列表
export function listReportCustomGroup(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/reportCustomGroup.listReportCustomGroup',
      method: 'get',
      params
    }).then(response => {
      const res = response.data
      if (res.code === 0) {
        resolve(res)
      } else {
        reject(new Error(res.msg || 'Failed to get report group list'))
      }
    }).catch(error => {
      reject(error)
    })
  })
}

// 添加报表组
export function saveReportCustomGroup(data) {
  return new Promise((resolve, reject) => {
    request({
      url: '/reportCustomGroup.saveReportCustomGroup',
      method: 'post',

data
    }).then(response => {
      const res = response.data
      if (res.code === 0) {
        resolve(res)
      } else {
        reject(new Error(res.msg || 'Failed to add report group'))
      }
    }).catch(error => {
      reject(error)
    })
  })
}

// 更新报表组
export function updateReportCustomGroup(data) {
  return new Promise((resolve, reject) => {
    request({
      url: '/reportCustomGroup.updateReportCustomGroup',
      method: 'post',
      data
    }).then(response => {
      const res = response.data
      if (res.code === 0) {
        resolve(res)
      } else {
        reject(new Error(res.msg || 'Failed to update report group'))
      }
    }).catch(error => {
      reject(error)
    })
  })
}

// 删除报表组
export function deleteReportCustomGroup(data) {
  return new Promise((resolve, reject) => {
    request({
      url: '/reportCustomGroup.deleteReportCustomGroup',
      method: 'post',
      data
    }).then(response => {
      const res = response.data
      if (res.code === 0) {
        resolve(res)
      } else {
        reject(new Error(res.msg || 'Failed to delete report group'))
      }
    }).catch(error => {
      reject(error)
    })
  })
}