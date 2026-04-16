import request from '@/utils/request'

// 获取设备列表
export function getMachineList(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/plugin/machine.listMachine',
      method: 'get',
      params
    }).then(response => {
      const res = response.data
      if (res.code === 0) {
        resolve(res)
      } else {
        reject(new Error(res.msg || '获取设备列表失败'))
      }
    }).catch(error => {
      reject(error)
    })
  })
}

// 添加设备
export function addMachine(data) {
  return new Promise((resolve, reject) => {
    request({
      url: '/plugin/machine.saveMachine',
      method: 'post',
      data
    }).then(response => {
      const res = response.data
      if (res.code === 0) {
        resolve(res)
      } else {
        reject(new Error(res.msg || '添加设备失败'))
      }
    }).catch(error => {
      reject(error)
    })
  })
}

// 更新设备
export function updateMachine(data) {
  return new Promise((resolve, reject) => {
    request({
      url: '/plugin/machine.updateMachine',
      method: 'post',
      data
    }).then(response => {
      const res = response.data
      if (res.code === 0) {
        resolve(res)
      } else {
        reject(new Error(res.msg || '更新设备失败'))
      }
    }).catch(error => {
      reject(error)
    })
  })
}

// 删除设备
export function deleteMachine(machineId) {
  return new Promise((resolve, reject) => {
    request({
      url: '/plugin/machine.deleteMachine',
      method: 'post',
      data: { machineId }
    }).then(response => {
      const res = response.data
      if (res.code === 0) {
        resolve(res)
      } else {
        reject(new Error(res.msg || '删除设备失败'))
      }
    }).catch(error => {
      reject(error)
    })
  })
} 