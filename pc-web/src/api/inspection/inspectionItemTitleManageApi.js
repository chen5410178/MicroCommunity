import request from '@/utils/request'
import { getCommunityId } from '@/api/community/communityApi'

// 获取巡检题目列表
export function listInspectionItemTitle(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/inspectionItemTitle.listInspectionItemTitle',
      method: 'get',
      params: {
        ...params,
        communityId: getCommunityId()
      }
    }).then(response => {
      const res = response.data
      if (res.code === 0) {
        resolve({
          data: res.data,
          total: res.total
        })
      } else {
        reject(new Error(res.msg || '获取巡检题目列表失败'))
      }
    }).catch(error => {
      reject(error)
    })
  })
}

// 添加巡检题目
export function saveInspectionItemTitle(data) {
  return new Promise((resolve, reject) => {
    request({
      url: '/inspectionItemTitle.saveInspectionItemTitle',
      method: 'post',
      data
    }).then(response => {
      const res = response.data
      if (res.code === 0) {
        resolve(res)
      } else {
        reject(new Error(res.msg || '添加巡检题目失败'))
      }
    }).catch(error => {
      reject(error)
    })
  })
}

// 更新巡检题目
export function updateInspectionItemTitle(data) {
  return new Promise((resolve, reject) => {
    request({
      url: '/inspectionItemTitle.updateInspectionItemTitle',
      method: 'post',
      data
    }).then(response => {
      const res = response.data
      if (res.code === 0) {
        resolve(res)
      } else {
        reject(new Error(res.msg || '更新巡检题目失败'))
      }
    }).catch(error => {
      reject(error)
    })
  })
}

// 删除巡检题目
export function deleteInspectionItemTitle(data) {
  return new Promise((resolve, reject) => {
    request({
      url: '/inspectionItemTitle.deleteInspectionItemTitle',
      method: 'post',
      data
    }).then(response => {
      const res = response.data
      if (res.code === 0) {
        resolve(res)
      } else {
        reject(new Error(res.msg || '删除巡检题目失败'))
      }
    }).catch(error => {
      reject(error)
    })
  })
}