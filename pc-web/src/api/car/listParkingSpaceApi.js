import request from '@/utils/request'
import { getCommunityId } from '@/api/community/communityApi'

// 查询停车位列表
export function listParkingSpaces(params) {
  return new Promise((resolve, reject) => {
    const communityId = getCommunityId()
    request({
      url: '/parkingSpace.queryParkingSpaces',
      method: 'get',
      params: { ...params, communityId }
    }).then(response => {
      const res = response.data

      resolve(res)
    }).catch(error => {
      reject(error)
    })
  })
}

// 添加停车位
export function saveParkingSpace(data) {
  return new Promise((resolve, reject) => {
    const communityId = getCommunityId()
    request({
      url: '/parkingSpace.saveParkingSpace',
      method: 'post',
      data: { ...data, communityId }
    }).then(response => {
      const res = response.data
      if (res.code === 0) {
        resolve(res)
      } else {
        reject(new Error(res.msg || '添加停车位失败'))
      }
    }).catch(error => {
      reject(error)
    })
  })
}

// 编辑停车位
export function editParkingSpace(data) {
  return new Promise((resolve, reject) => {
    const communityId = getCommunityId()
    request({
      url: '/parkingSpace.editParkingSpace',
      method: 'post',
      data: { ...data, communityId }
    }).then(response => {
      const res = response.data
      if (res.code === 0) {
        resolve(res)
      } else {
        reject(new Error(res.msg || '编辑停车位失败'))
      }
    }).catch(error => {
      reject(error)
    })
  })
}

// 删除停车位
export function deleteParkingSpace(data) {
  return new Promise((resolve, reject) => {
    const communityId = getCommunityId()
    request({
      url: '/parkingSpace.deleteParkingSpace',
      method: 'post',
      data: { ...data, communityId }
    }).then(response => {
      const res = response.data
      if (res.code === 0) {
        resolve(res)
      } else {
        reject(new Error(res.msg || '删除停车位失败'))
      }
    }).catch(error => {
      reject(error)
    })
  })
}

// 查询停车场列表
export function listParkingAreas(params) {
  return new Promise((resolve, reject) => {
    const communityId = getCommunityId()
    request({
      url: '/parkingArea.listParkingAreas',
      method: 'get',
      params: { ...params, communityId }
    }).then(response => {
      const res = response.data
        resolve(res)
    }).catch(error => {
      reject(error)
    })
  })
}

// 批量添加停车位
export function batchSaveParkingSpace(data) {
  return new Promise((resolve, reject) => {
    const communityId = getCommunityId()
    request({
      url: '/parkingSpace.batchSaveParkingSpace',
      method: 'post',
      data: { ...data, communityId }
    }).then(response => {
      const res = response.data
      if (res.code === 0) {
        resolve(res)
      } else {
        reject(new Error(res.msg || '批量添加停车位失败'))
      }
    }).catch(error => {
      reject(error)
    })
  })
}