import request from '@/utils/request'

// 获取房屋详情
export function getRoomDetail(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/room.queryAdminRoom',
      method: 'get',
      params
    }).then(response => {
      const res = response.data
      resolve(res)
    }).catch(error => {
      reject(error)
    })
  })
}

// 获取业主信息
export function getOwnerInfo(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/owner.queryOwner',
      method: 'get',
      params
    }).then(response => {
      const res = response.data
      resolve(res)
    }).catch(error => {
      reject(error)
    })
  })
}

// 获取家庭成员列表
export function getMemberList(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/member.queryMember',
      method: 'get',
      params
    }).then(response => {
      const res = response.data
      resolve(res)
    }).catch(error => {
      reject(error)
    })
  })
}

// 获取业主车辆列表
export function getCarList(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/car.queryCar',
      method: 'get',
      params
    }).then(response => {
      const res = response.data
      resolve(res)
    }).catch(error => {
      reject(error)
    })
  })
}