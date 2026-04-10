import request from '@/utils/request'

// 获取调拨入库列表
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

// 提交调拨入库
export function allocationStoreEnter(data) {
  return new Promise((resolve, reject) => {
    request({
      url: '/resourceStore.allocationStoreEnter',
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