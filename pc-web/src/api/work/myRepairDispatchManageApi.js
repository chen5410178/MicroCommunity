import request from '@/utils/request'

// 获取维修已办列表
export function listStaffFinishRepairs(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/ownerRepair.listStaffFinishRepairs',
      method: 'get',
      params
    }).then(response => {
      const res = response.data
      if (res.code === 0) {
        resolve(res)
      } else {
        reject(new Error(res.msg || '获取维修已办列表失败'))
      }
    }).catch(error => {
      reject(error)
    })
  })
}

// 获取报修类型列表
export function listRepairSettings(params) {
  return new Promise((resolve, reject) => {
    request({
      url: 'repair.listRepairSettings',
      method: 'get',
      params
    }).then(response => {
      const res = response.data
      if (res.code === 0) {
        resolve(res)
      } else {
        reject(new Error(res.msg || '获取报修类型失败'))
      }
    }).catch(error => {
      reject(error)
    })
  })
}

// 结单操作
export function closeRepairDispatch(data) {
  return new Promise((resolve, reject) => {
    request({
      url: '/ownerRepair.closeRepairDispatch',
      method: 'post',
      data
    }).then(response => {
      const res = response.data
      if (res.code === 0) {
        resolve(res)
      } else {
        reject(new Error(res.msg || '结单操作失败'))
      }
    }).catch(error => {
      reject(error)
    })
  })
}