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
      if (res.code == 0) {
        resolve(res)
      } else {
        reject(new Error(res.msg || 'Failed to query owner cars'))
      }
    }).catch(error => {
      reject(error)
    })
  })
}