import request from '@/utils/request'

// 查询费用配置列表
export function queryAdminFeeConfigs(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/feeConfig.queryAdminFeeConfigs',
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

// 查询社区费用类型树
export function queryCommunityFeeTypeTree(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/community.queryCommunityFeeTypeTree',
      method: 'get',
      params
    }).then(response => {
      const res = response.data
      if (res.code === 0) {
        resolve({
          data: res.data
        })
      } else {
        reject(new Error(res.msg || 'Failed to query community fee type tree'))
      }
    }).catch(error => {
      reject(error)
    })
  })
}