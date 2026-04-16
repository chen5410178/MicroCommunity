import request from '@/utils/request'
import { getCommunityId } from '@/api/community/communityApi'

// 获取停车场列表
export function getParkingAreaList(params) {
  return new Promise((resolve, reject) => {
    const query = {
      ...params,
      communityId: params.communityId || getCommunityId()
    }

    request({
      url: '/parkingArea.listParkingAreas',
      method: 'get',
      params: query
    }).then(response => {
      const res = response.data
      resolve(res)
    }).catch(error => {
      reject(error)
    })
  })
}

// 添加停车场
export function saveParkingArea(data) {
  return new Promise((resolve, reject) => {
    const params = {
      ...data,
      communityId: data.communityId || getCommunityId()
    }

    request({
      url: '/parkingArea.saveParkingArea',
      method: 'post',
      data: params
    }).then(response => {
      const res = response.data
      if (res.code === 0) {
        resolve(res)
      } else {
        reject(new Error(res.msg || '添加停车场失败'))
      }
    }).catch(error => {
      reject(error)
    })
  })
}

// 更新停车场
export function updateParkingArea(data) {
  return new Promise((resolve, reject) => {
    const params = {
      ...data,
      communityId: data.communityId || getCommunityId()
    }

    request({
      url: '/parkingArea.updateParkingArea',
      method: 'post',
      data: params
    }).then(response => {
      const res = response.data
      if (res.code === 0) {
        resolve(res)
      } else {
        reject(new Error(res.msg || '更新停车场失败'))
      }
    }).catch(error => {
      reject(error)
    })
  })
}

// 删除停车场
export function deleteParkingArea(data) {
  return new Promise((resolve, reject) => {
    const params = {
      ...data,
      communityId: data.communityId || getCommunityId()
    }

    request({
      url: '/parkingArea.deleteParkingArea',
      method: 'post',
      data: params
    }).then(response => {
      const res = response.data
      if (res.code === 0) {
        resolve(res)
      } else {
        reject(new Error(res.msg || '删除停车场失败'))
      }
    }).catch(error => {
      reject(error)
    })
  })
}