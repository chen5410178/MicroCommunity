import request from '@/utils/request'
import { getCommunityId } from '@/api/community/communityApi'

export function listMonthFee(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/fee.listMonthFee',
      method: 'get',
      params: {
        ...params,
        communityId: getCommunityId()
      }
    }).then(response => {
      const res = response.data
      if (res.code == 0) {
        resolve(res)
      } else {
        reject(new Error(res.msg || 'Failed to list month fee'))
      }
    }).catch(error => {
      reject(error)
    })
  })
}