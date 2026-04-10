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
      if (res.code === 0) {
        resolve({
          data: res.data,
          total: res.total
        })
      } else {
        reject(new Error(res.msg || '查询欠费明细失败'))
      }
    }).catch(error => {
      reject(error)
    })
  })
}

// 查询小区楼层树
export function queryCommunityFloorTree(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/community.queryCommunityFloorTree',
      method: 'get',
      params
    }).then(response => {
      const res = response.data
      if (res.code === 0) {
        resolve(res)
      } else {
        reject(new Error(res.msg || '查询小区楼层树失败'))
      }
    }).catch(error => {
      reject(error)
    })
  })
}