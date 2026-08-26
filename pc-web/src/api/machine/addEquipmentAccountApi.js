import request from '@/utils/request'

// 保存设备账户信息
export function saveEquipmentAccount(data) {
  return new Promise((resolve, reject) => {
    request({
      url: '/equipmentAccount.saveEquipmentAccount',
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