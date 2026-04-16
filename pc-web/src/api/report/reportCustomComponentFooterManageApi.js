import request from '@/utils/request'

// 获取自定义组件页脚列表
export function listReportCustomComponentFooter(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/reportCustomComponentFooter.listReportCustomComponentFooter',
      method: 'get',
      params
    }).then(response => {
      const res = response.data
      if (res.code == 0) {
        resolve(res)
      } else {
        reject(new Error(res.msg || '获取自定义组件页脚列表失败'))
      }
    }).catch(error => {
      reject(error)
    })
  })
}

// 添加自定义组件页脚
export function saveReportCustomComponentFooter(data) {
  return new Promise((resolve, reject) => {
    request({
      url: '/reportCustomComponentFooter.saveReportCustomComponentFooter',
      method: 'post',
      data
    }).then(response => {
      const res = response.data
      if (res.code == 0) {
        resolve(res)
      } else {
        reject(new Error(res.msg || '添加自定义组件页脚失败'))
      }
    }).catch(error => {
      reject(error)
    })
  })
}

// 更新自定义组件页脚
export function updateReportCustomComponentFooter(data) {
  return new Promise((resolve, reject) => {
    request({
      url: '/reportCustomComponentFooter.updateReportCustomComponentFooter',
      method: 'post',
      data
    }).then(response => {
      const res = response.data
      if (res.code == 0) {
        resolve(res)
      } else {
        reject(new Error(res.msg || '更新自定义组件页脚失败'))
      }
    }).catch(error => {
      reject(error)
    })
  })
}

// 删除自定义组件页脚
export function deleteReportCustomComponentFooter(data) {
  return new Promise((resolve, reject) => {
    request({
      url: '/reportCustomComponentFooter.deleteReportCustomComponentFooter',
      method: 'post',
      data
    }).then(response => {
      const res = response.data
      if (res.code == 0) {
        resolve(res)
      } else {
        reject(new Error(res.msg || '删除自定义组件页脚失败'))
      }
    }).catch(error => {
      reject(error)
    })
  })
}