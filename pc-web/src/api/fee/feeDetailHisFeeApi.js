import request from '@/utils/request'
import { getCommunityId } from '@/api/community/communityApi'

export function queryFeeDetail(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/fee.queryFeeDetail',
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