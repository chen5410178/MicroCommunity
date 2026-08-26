import request from '@/utils/request'

// 查询欠费明细
export function queryAdminOweFeeDetail(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/fee.queryAdminOweFeeDetail',
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

export function queryCommunityFloorTree(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/community.queryCommunityFloorTree',
      method: 'get',
      params
    }).then(response => {
      const res = response.data
      resolve(res)
    }).catch(error => {
      reject(error)
    })
  })
}