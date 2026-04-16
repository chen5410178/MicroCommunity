import request from '@/utils/request'
import { getCommunityId } from '@/api/community/communityApi'

// 保存车辆信息
export function saveOwnerCar(data) {
  return new Promise((resolve, reject) => {
    request({
      url: '/owner.saveOwnerCar',
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
        reject(new Error(res.msg || '保存车辆信息失败'))
      }
    }).catch(error => {
      reject(error)
    })
  })
}

// 查询业主列表
export function queryOwners(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/owner.queryOwners',
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
        reject(new Error(res.msg || '查询业主列表失败'))
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
        resolve(res)
    }).catch(error => {
      reject(error)
    })
  })
}

// 获取停车场区域列表
export function listParkingAreas(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/parkingArea.listParkingAreas',
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

// 获取车辆属性规格
export function getCarAttrSpec() {
  return new Promise((resolve, reject) => {
    request({
      url: '/attr.getAttrSpec',
      method: 'get',
      params: {
        specCd: 'owner_car_attr',
        communityId: getCommunityId()
      }
    }).then(response => {
      const res = response.data
      if (res.code === 0) {
        resolve(res.data)
      } else {
        reject(new Error(res.msg || '获取车辆属性规格失败'))
      }
    }).catch(error => {
      reject(error)
    })
  })
}

// 获取字典数据
export function getDict(dictType, dictCd) {
  return new Promise((resolve, reject) => {
    request({
      url: '/dict.getDict',
      method: 'get',
      params: {
        dictType,
        dictCd,
        communityId: getCommunityId()
      }
    }).then(response => {
      const res = response.data
      if (res.code === 0) {
        resolve(res.data)
      } else {
        reject(new Error(res.msg || '获取字典数据失败'))
      }
    }).catch(error => {
      reject(error)
    })
  })
}