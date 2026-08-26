import request from '@/utils/request'

// 保存交接信息
export function saveHandover(data) {
  return new Promise((resolve, reject) => {
    request({
      url: '/owner.saveHandover',
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

// 查询房屋列表
export function queryRooms(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/room.queryRooms',
      method: 'get',
      params
    }).then(response => {
      const res = response.data
      resolve({
        data: res,
        total: res.total
      })
    }).catch(error => {
      reject(error)
    })
  })
}

// 查询费用配置列表
export function listFeeConfigs(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/feeConfig.listFeeConfigs',
      method: 'get',
      params
    }).then(response => {
      const res = response.data
      resolve({
        data: res,
        total: res.total
      })
    }).catch(error => {
      reject(error)
    })
  })
}