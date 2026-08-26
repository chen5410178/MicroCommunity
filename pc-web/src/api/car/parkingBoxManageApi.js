import request from '@/utils/request'
import { getCommunityId } from '@/api/community/communityApi'

// 查询岗亭列表
export function getParkingBoxList(params) {
  return new Promise((resolve, reject) => {
    const communityId = getCommunityId()
    request({
      url: '/iot.getOpenApi',
      method: 'get',
      params: {
        ...params,
        communityId,
        iotApiCode: 'listParkingBoxBmoImpl'
      }
    }).then(response => {
      const res = response.data
      resolve(res)
    }).catch(error => {
      reject(error)
    })
  })
}

// 添加岗亭
export function addParkingBox(data) {
  return new Promise((resolve, reject) => {
    const communityId = getCommunityId()
    request({
      url: '/parkingBox.saveParkingBox',
      method: 'post',
      data: {
        ...data,
        communityId
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

// 更新岗亭
export function updateParkingBox(data) {
  return new Promise((resolve, reject) => {
    const communityId = getCommunityId()
    request({
      url: '/parkingBox.updateParkingBox',
      method: 'post',
      data: {
        ...data,
        communityId
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

// 删除岗亭
export function deleteParkingBox(data) {
  return new Promise((resolve, reject) => {
    const communityId = getCommunityId()
    request({
      url: '/parkingBox.deleteParkingBox',
      method: 'post',
      data: {
        ...data,
        communityId
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

// 获取停车场列表
export function getParkingAreaList() {
  return new Promise((resolve, reject) => {
    const communityId = getCommunityId()
    request({
      url: '/parkingArea.listParkingAreas',
      method: 'get',
      params: {
        page: 1,
        row: 50,
        communityId
      }
    }).then(response => {
      const res = response.data
      resolve(res.data.parkingAreas || [])
    }).catch(error => {
      reject(error)
    })
  })
}