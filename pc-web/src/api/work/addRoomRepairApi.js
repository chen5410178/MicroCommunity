import request from '@/utils/request'

// 保存业主报修信息
export function saveOwnerRepair(data) {
  return new Promise((resolve, reject) => {
    request({
      url: '/ownerRepair.saveOwnerRepair',
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

// 获取报修设置列表
export function listRepairSettings(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/repair.listRepairSettings',
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