import request from '@/utils/request'

// 获取报表组件列表
export function getReportCustomComponentList(params) {
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
        reject(new Error(res.msg || 'Failed to get report components'))
      }
    }).catch(error => {
      reject(error)
    })
  })
}

// 添加报表组件
export function addReportCustomComponent(data) {
  return new Promise((resolve, reject) => {
    request({
      url: '/reportCustomComponent.saveReportCustomComponent',
      method: 'post',
      data
    }).then(response => {
      const res = response.data
      if (res.code === 0) {
        resolve(res)
      } else {
        reject(new Error(res.msg || 'Failed to add report component'))
      }
    }).catch(error => {
      reject(error)
    })
  })
}

// 更新报表组件
export function updateReportCustomComponent(data) {
  return new Promise((resolve, reject) => {
    request({
      url: '/reportCustomComponent.updateReportCustomComponent',
      method: 'post',
      data
    }).then(response => {
      const res = response.data
      if (res.code === 0) {
        resolve(res)
      } else {
        reject(new Error(res.msg || 'Failed to update report component'))
      }
    }).catch(error => {
      reject(error)
    })
  })
}

// 删除报表组件
export function deleteReportCustomComponent(data) {
  return new Promise((resolve, reject) => {
    request({
      url: '/reportCustomComponent.deleteReportCustomComponent',
      method: 'post',
      data
    }).then(response => {
      const res = response.data
      if (res.code === 0) {
        resolve(res)
      } else {
        reject(new Error(res.msg || 'Failed to delete report component'))
      }
    }).catch(error => {
      reject(error)
    })
  })
}