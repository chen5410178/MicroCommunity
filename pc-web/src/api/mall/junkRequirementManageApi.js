import request from '@/utils/request'

// 获取旧货需求列表
export function listJunkRequirements(params) {
  return new Promise((resolve, reject) => {
    request
      .get('/junkRequirement.listJunkRequirements', { params })
      .then(response => {
        const res = response.data
        if (res.code === 0) {
          resolve({
            data: res.data,
            total: res.total
          })
        } else {
          reject(new Error(res.msg || 'Failed to get junk requirements list'))
        }
      })
      .catch(error => {
        reject(error)
      })
  })
}

// 更新旧货需求
export function updateJunkRequirement(data) {
  return new Promise((resolve, reject) => {
    request
      .post('/junkRequirement.updateJunkRequirement', data)
      .then(response => {
        const res = response.data
        if (res.code === 0) {
          resolve(res)
        } else {
          reject(new Error(res.msg || 'Failed to update junk requirement'))
        }
      })
      .catch(error => {
        reject(error)
      })
  })
}

// 删除旧货需求
export function deleteJunkRequirement(data) {
  return new Promise((resolve, reject) => {
    request
      .post('/junkRequirement.deleteJunkRequirement', data)
      .then(response => {
        const res = response.data
        if (res.code === 0) {
          resolve(res)
        } else {
          reject(new Error(res.msg || 'Failed to delete junk requirement'))
        }
      })
      .catch(error => {
        reject(error)
      })
  })
}