import request from '@/utils/request'

// 更新设备信息
export function updateEquipmentAccount(data) {
  return new Promise((resolve, reject) => {
    request({
      url: '/equipmentAccount.updateEquipmentAccount',
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

// 获取设备列表
export function listEquipmentAccount(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/equipmentAccount.listEquipmentAccount',
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