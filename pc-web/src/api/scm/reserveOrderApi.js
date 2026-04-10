import request from '@/utils/request'
import { getCommunityId } from '@/api/community/communityApi'

// 获取预约订单列表
export function listReserveGoodsOrder(params) {
  return new Promise((resolve, reject) => {
    const defaultParams = {
      communityId: getCommunityId(),
      ...params
    }
    request({
      url: '/reserveOrder.listReserveGoodsOrder',
      method: 'get',
      params: defaultParams
    }).then(response => {
      const res = response.data
      resolve(res)
    }).catch(error => {
      reject(error)
    })
  })
}

// 取消预约订单
export function cancelReserveOrder(data) {
  return new Promise((resolve, reject) => {
    request({
      url: '/reserveOrder.cancelReserveOrder',
      method: 'post',
      data
    }).then(response => {
      const res = response.data
      resolve(res)
    }).catch(error => {
      reject(error)
    })
  })
}