import request from '@/utils/request'

// 获取组件条件列表
export function listReportCustomComponentCondition(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/reportCustomComponentCondition.listReportCustomComponentCondition',
      method: 'get',
      params
    }).then(response => {
      const res = response.data
      if (res.code == 0) {
        resolve({
          data: res.data,
          total: res.total
        })
      } else {
        reject(new Error(res.msg || '获取组件条件列表失败'))
      }
    }).catch(error => {
      reject(error)
    })
  })
}

// 添加组件条件
export function saveReportCustomComponentCondition(data) {
  return new Promise((resolve, reject) => {
    request({
      url: '/reportCustomComponentCondition.saveReportCustomComponentCondition',
      method: 'post',
      data
    }).then(response => {
      const res = response.data
      if (res.code == 0) {
        resolve(res)
      } else {
        reject(new Error(res.msg || '添加组件条件失败'))
      }
    }).catch(error => {
      reject(error)
    })
  })
}

// 更新组件条件
export function updateReportCustomComponentCondition(data) {
  return new Promise((resolve, reject) => {
    request({
      url: '/reportCustomComponentCondition.updateReportCustomComponentCondition',
      method: 'post',
      data
    }).then(response => {
      const res = response.data
      if (res.code == 0) {
        resolve(res)
      } else {
        reject(new Error(res.msg || '更新组件条件失败'))
      }
    }).catch(error => {
      reject(error)
    })
  })
}

// 删除组件条件
export function deleteReportCustomComponentCondition(data) {
  return new Promise((resolve, reject) => {
    request({
      url: '/reportCustomComponentCondition.deleteReportCustomComponentCondition',
      method: 'post',
      data
    }).then(response => {
      const res = response.data
      if (res.code == 0) {
        resolve(res)
      } else {
        reject(new Error(res.msg || '删除组件条件失败'))
      }
    }).catch(error => {
      reject(error)
    })
  })
}