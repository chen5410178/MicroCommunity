import request from '@/utils/request'

// 查询巡检项目列表
export function listInspectionItem(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/inspectionItem.listInspectionItem',
      method: 'get',
      params
    }).then(response => {
      const res = response.data
      if (res.code === 0) {
        resolve(res)
      } else {
        reject(new Error(res.msg || '获取巡检项目列表失败'))
      }
    }).catch(error => {
      reject(error)
    })
  })
}

// 添加巡检项目
export function saveInspectionItem(data) {
  return new Promise((resolve, reject) => {
    request({
      url: '/inspectionItem.saveInspectionItem',
      method: 'post',
      data
    }).then(response => {
      const res = response.data
      if (res.code === 0) {
        resolve(res)
      } else {
        reject(new Error(res.msg || '添加巡检项目失败'))
      }
    }).catch(error => {
      reject(error)
    })
  })
}

// 更新巡检项目
export function updateInspectionItem(data) {
  return new Promise((resolve, reject) => {
    request({
      url: '/inspectionItem.updateInspectionItem',
      method: 'post',
      data
    }).then(response => {
      const res = response.data
      if (res.code === 0) {
        resolve(res)
      } else {
        reject(new Error(res.msg || '更新巡检项目失败'))
      }
    }).catch(error => {
      reject(error)
    })
  })
}

// 删除巡检项目
export function deleteInspectionItem(data) {
  return new Promise((resolve, reject) => {
    request({
      url: '/inspectionItem.deleteInspectionItem',
      method: 'post',
      data
    }).then(response => {
      const res = response.data
      if (res.code === 0) {
        resolve(res)
      } else {
        reject(new Error(res.msg || '删除巡检项目失败'))
      }
    }).catch(error => {
      reject(error)
    })
  })
}