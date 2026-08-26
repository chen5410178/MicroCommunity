import request from '@/utils/request'
import { getCommunityId } from '@/api/community/communityApi'

export function queryOwnerCars(params) {
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
      resolve(res)
    }).catch(error => {
      reject(error)
    })
  })
}