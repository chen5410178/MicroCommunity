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

// 获取设备类型字典
export function getEquipmentDict(dictType, dictName) {
  return new Promise((resolve, reject) => {
    request({
      url: '/dict.getDict',
      method: 'get',
      params: {
        dictType,
        dictName
      }
    }).then(response => {
      const res = response.data
      resolve(res)
    }).catch(error => {
      reject(error)
    })
  })
}