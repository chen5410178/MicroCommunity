import request from '@/utils/request'
import { getCommunityId } from '@/api/community/communityApi'

export function queryContract(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/contract/queryContract',
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