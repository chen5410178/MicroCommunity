import request from '@/utils/request'

// 获取场馆列表
export function listCommunityVenue(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/communityVenue.listCommunityVenue',
      method: 'get',
      params
    }).then(response => {
      const res = response.data
      if (res.code === 0) {
        resolve(res.data)
      } else {
        reject(new Error(res.msg || '获取场馆列表失败'))
      }
    }).catch(error => {
      reject(error)
    })
  })
}

// 获取场地列表
export function listCommunitySpace(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/communitySpace.listCommunitySpace',
      method: 'get',
      params
    }).then(response => {
      const res = response.data
      if (res.code === 0) {
        resolve(res.data)
      } else {
        reject(new Error(res.msg || '获取场地列表失败'))
      }
    }).catch(error => {
      reject(error)
    })
  })
}

// 获取场地预约记录
export function listCommunitySpacePerson(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/communitySpace.listCommunitySpacePerson',
      method: 'get',
      params
    }).then(response => {
      const res = response.data
      if (res.code === 0) {
        resolve(res.data)
      } else {
        reject(new Error(res.msg || '获取场地预约记录失败'))
      }
    }).catch(error => {
      reject(error)
    })
  })
}

// 保存场地预约
export function saveCommunitySpacePerson(data) {
  return new Promise((resolve, reject) => {
    request({
      url: '/communitySpace.saveCommunitySpacePerson',
      method: 'post',
      data
    }).then(response => {
      const res = response.data
      if (res.code === 0) {
        resolve(res)
      } else {
        reject(new Error(res.msg || '保存场地预约失败'))
      }
    }).catch(error => {
      reject(error)
    })
  })
}