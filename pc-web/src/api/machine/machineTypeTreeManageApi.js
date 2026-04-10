import request from '@/utils/request'

// 获取设备类型列表
export function listMachineType(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/machineType.listMachineType',
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

// 添加设备类型
export function saveMachineType(data) {
  return new Promise((resolve, reject) => {
    request({
      url: '/machineType.saveMachineType',
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

// 更新设备类型
export function updateMachineType(data) {
  return new Promise((resolve, reject) => {
    request({
      url: '/machineType.updateMachineType',
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

// 删除设备类型
export function deleteMachineType(data) {
  return new Promise((resolve, reject) => {
    request({
      url: '/machineType.deleteMachineType',
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