import request from '@/utils/request'

// 查询设备列表
export function listAdminEquipmentAccount(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/equipmentAccount.listAdminEquipmentAccount',
      method: 'get',
      params
    }).then(response => {
      const res = response.data
      if (res.code === 0) {
        resolve({
          data: res.data,
          total: res.total
        })
      } else {
        reject(new Error(res.msg || 'Failed to get equipment list'))
      }
    }).catch(error => {
      reject(error)
    })
  })
}

// 查询社区设备类型树
export function queryCommunityMachineTypeTree() {
  return new Promise((resolve, reject) => {
    request({
      url: '/community.queryCommunityMachineTypeTree',
      method: 'get',
      params: { hc: 1.8 }
    }).then(response => {
      const res = response.data
      if (res.code === 0) {
        resolve({
          data: res.data
        })
      } else {
        reject(new Error(res.msg || 'Failed to get machine type tree'))
      }
    }).catch(error => {
      reject(error)
    })
  })
}