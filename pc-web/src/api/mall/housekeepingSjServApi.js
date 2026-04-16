import request from '@/utils/request'

// 查询家政服务列表
export function queryHousekeepingServ(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/mall.getAdminMallOpenApi',
      method: 'get',
      params
    }).then(response => {
      const res = response.data
      if (res.code === 0) {
        resolve({
          data: res.data,
          total: res.total,
          records: res.records
        })
      } else {
        reject(new Error(res.msg || '查询家政服务列表失败'))
      }
    }).catch(error => {
      reject(error)
    })
  })
}

// 查询家政服务类型
export function queryHousekeepingType(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/housekeepingType/queryHousekeepingType',
      method: 'get',
      params
    }).then(response => {
      const res = response.data
      if (res.code === 0) {
        resolve({
          data: res.data
        })
      } else {
        reject(new Error(res.msg || '查询家政服务类型失败'))
      }
    }).catch(error => {
      reject(error)
    })
  })
}