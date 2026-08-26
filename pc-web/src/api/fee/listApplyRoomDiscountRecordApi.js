import request from '@/utils/request'
import { getCommunityId } from '@/api/community/communityApi'

// 查询验房跟踪记录列表
export function queryApplyRoomDiscountRecord(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/applyRoomDiscountRecord/queryApplyRoomDiscountRecord',
      method: 'get',
      params: {
        ...params,
        communityId: getCommunityId()
      }
    }).then(response => {
      const res = response.data
      resolve(res)
    }).catch(error => {
      reject(error)
    })
  })
}

// 添加验房跟踪记录
export function addApplyRoomDiscountRecord(data) {
  return new Promise((resolve, reject) => {
    request({
      url: '/applyRoomDiscountRecord/addApplyRoomDiscountRecord',
      method: 'post',
      data: {
        ...data,
        communityId: getCommunityId()
      }
    }).then(response => {
      const res = response.data
      if (res.code === 0) {
        resolve(res)
      } else {
        reject(new Error(res.msg || 'error'))
      }
    }).catch(error => {
      reject(error)
    })
  })
}

// 删除验房跟踪记录
export function cutApplyRoomDiscountRecord(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/applyRoomDiscountRecord/cutApplyRoomDiscountRecord',
      method: 'post',
      data: {
        ...params,
        communityId: getCommunityId()
      }
    }).then(response => {
      const res = response.data
      if (res.code === 0) {
        resolve(res)
      } else {
        reject(new Error(res.msg || 'error'))
      }
    }).catch(error => {
      reject(error)
    })
  })
}

// 上传图片
export function uploadImage(data) {
  return new Promise((resolve, reject) => {
    request({
      url: '/uploadImage',
      method: 'post',
      headers: {
        'Content-Type': 'multipart/form-data'
      },
      data
    }).then(response => {
      const res = response.data
      resolve(res)
    }).catch(error => {
      reject(error)
    })
  })
}

// 上传视频
export function uploadVedio(data, config) {
  return new Promise((resolve, reject) => {
    request({
      url: '/callComponent/upload/uploadVedio/upload',
      method: 'post',
      headers: {
        'Content-Type': 'multipart/form-data'
      },
      data,
      ...config
    }).then(response => {
      const res = response.data
      resolve(res)
    }).catch(error => {
      reject(error)
    })
  })
}