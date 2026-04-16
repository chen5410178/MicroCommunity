import request from '@/utils/request'

export function getFloorAndUnits(communityId) {
  return new Promise((resolve, reject) => {
    request({
      url: '/floor.queryFloorAndUnits',
      method: 'get',
      params: { communityId }
    }).then(response => {
      const res = response.data
      resolve(res)
    }).catch(error => {
      reject(error)
    })
  })
}