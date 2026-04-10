import request from '@/utils/request'

// 获取调拨申请单信息
export function listAllocationStorehouseApplys(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/resourceStore.listAllocationStorehouseApplys',
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

// 获取调拨仓库物品列表
export function listAllocationStorehouses(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/resourceStore.listAllocationStorehouses',
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