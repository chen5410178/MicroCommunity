import request from '@/utils/request'

// 获取设备账户标签列表
export function getEquipmentAccountList(params) {
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