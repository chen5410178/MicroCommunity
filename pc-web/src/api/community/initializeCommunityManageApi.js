import request from '@/utils/request'

// 获取小区列表
export function listCommunitys(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/community.listCommunitys',
      method: 'get',
      params
    }).then(response => {
      const res = response.data
      if (res.code == 0) {
        resolve(res)
      } else {
        reject(new Error(res.msg || '获取小区列表失败'))
      }
    }).catch(error => {
      reject(error)
    })
  })
}

// 初始化小区数据
export function initializeCommunity(data) {
  return new Promise((resolve, reject) => {
    request({
      url: '/initializeBuildingUnit/deleteBuildingUnit',
      method: 'post',
      data
    }).then(response => {
      const res = response.data
      if (res.code === 0) {
        resolve(res)
      } else {
        reject(new Error(res.msg || '初始化小区数据失败'))
      }
    }).catch(error => {
      reject(error)
    })
  })
}