import request from '@/utils/request'

// 查询核销订单列表
export function listCommunitySpaceConfirmOrder(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/communitySpace.listCommunitySpaceConfirmOrder',
      method: 'get',
      params
    }).then(response => {
      const res = response.data
      resolve({
        data: res.data,
        total: res.total
      })
    }).catch(error => {
      reject(error)
    })
  })
}

// 核销场地订单
export function saveCommunitySpaceConfirmOrder(data) {
  return new Promise((resolve, reject) => {
    request({
      url: '/communitySpace.saveCommunitySpaceConfirmOrder',
      method: 'post',
      data
    }).then(response => {
      const res = response.data
      if (res.code == 0) {
        resolve(res)
      } else {
        reject(new Error(res.msg || 'error'))
      }
    }).catch(error => {
      reject(error)
    })
  })
}