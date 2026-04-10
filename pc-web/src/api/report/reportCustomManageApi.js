import request from '@/utils/request'

// 获取报表自定义列表
export function listReportCustom(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/reportCustom.listReportCustom',
      method: 'get',
      params
    }).then(response => {
      const res = response.data
      if (res.code == 0) {
        resolve(res)
      } else {
        reject(new Error(res.msg || '获取报表自定义列表失败'))
      }
    }).catch(error => {
      reject(error)
    })
  })
}

// 添加报表自定义
export function saveReportCustom(data) {
  return new Promise((resolve, reject) => {
    request({
      url: '/reportCustom.saveReportCustom',
      method: 'post',
      data
    }).then(response => {
      const res = response.data
      if (res.code == 0) {
        resolve(res)
      } else {
        reject(new Error(res.msg || '添加报表自定义失败'))
      }
    }).catch(error => {
      reject(error)
    })
  })
}

// 更新报表自定义
export function updateReportCustom(data) {
  return new Promise((resolve, reject) => {
    request({
      url: '/reportCustom.updateReportCustom',
      method: 'post',
      data
    }).then(response => {
      const res = response.data
      if (res.code == 0) {
        resolve(res)
      } else {
        reject(new Error(res.msg || '更新报表自定义失败'))
      }
    }).catch(error => {
      reject(error)
    })
  })
}

// 删除报表自定义
export function deleteReportCustom(customId) {
  return new Promise((resolve, reject) => {
    request({
      url: '/reportCustom.deleteReportCustom',
      method: 'post',
      data: { customId }
    }).then(response => {
      const res = response.data
      if (res.code == 0) {
        resolve(res)
      } else {
        reject(new Error(res.msg || '删除报表自定义失败'))
      }
    }).catch(error => {
      reject(error)
    })
  })
}

// 获取报表自定义组列表
export function listReportCustomGroup(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/reportCustomGroup.listReportCustomGroup',
      method: 'get',
      params
    }).then(response => {
      const res = response.data
      if (res.code == 0) {
        resolve(res)
      } else {
        reject(new Error(res.msg || '获取报表自定义组列表失败'))
      }
    }).catch(error => {
      reject(error)
    })
  })
}