import request from '@/utils/request'
import { getCommunityId } from '@/api/community/communityApi'

// 查询车辆信息
export function getCarInfo(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/owner.queryOwnerCars',
      method: 'get',
      params: {
        ...params,
        communityId: getCommunityId()
      }
    }).then(response => {
      const res = response.data
      if (res.code === 0) {
        resolve(res)
      } else {
        reject(new Error(res.msg || '获取车辆信息失败'))
      }
    }).catch(error => {
      reject(error)
    })
  })
}

// 添加车辆停车位
export function addCarParkingSpace(data) {
  return new Promise((resolve, reject) => {
    request({
      url: '/owner.carAddParkingSpace',
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
        reject(new Error(res.msg || '添加停车位失败'))
      }
    }).catch(error => {
      reject(error)
    })
  })
}

// 查询停车位列表
export function queryParkingSpaces(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/parkingSpace.queryParkingSpaces',
      method: 'get',
      params: {
        ...params,
        communityId: getCommunityId()
      }
    }).then(response => {
      const res = response.data
      if (res.code === 0) {
        resolve(res)
      } else {
        reject(new Error(res.msg || '查询停车位失败'))
      }
    }).catch(error => {
      reject(error)
    })
  })
}

// 查询停车场列表
export function listParkingAreas() {
  return new Promise((resolve, reject) => {
    request({
      url: '/parkingArea.listParkingAreas',
      method: 'get',
      params: {
        page: 1,
        row: 50,
        communityId: getCommunityId()
      }
    }).then(response => {
      const res = response.data
      if (res.code === 0) {
        resolve(res)
      } else {
        reject(new Error(res.msg || '查询停车场失败'))
      }
    }).catch(error => {
      reject(error)
    })
  })
}