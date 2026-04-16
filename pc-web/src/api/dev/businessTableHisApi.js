import request from '@/utils/request'

// 获取业务轨迹列表
export function getBusinessTableHisList(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/businessTableHis.listBusinessTableHis',
      method: 'get',
      params
    }).then(response => {
      const res = response.data
      if (res.code == 0) {
        resolve(res)
      } else {
        reject(new Error(res.msg || '获取业务轨迹列表失败'))
      }
    }).catch(error => {
      reject(error)
    })
  })
}

// 添加业务轨迹
export function addBusinessTableHis(data) {
  return new Promise((resolve, reject) => {
    request({
      url: '/businessTableHis.saveBusinessTableHis',
      method: 'post',
      data
    }).then(response => {
      const res = response.data
      if (res.code == 0) {
        resolve(res)
      } else {
        reject(new Error(res.msg || '添加业务轨迹失败'))
      }
    }).catch(error => {
      reject(error)
    })
  })
}

// 更新业务轨迹
export function updateBusinessTableHis(data) {
  return new Promise((resolve, reject) => {
    request({
      url: '/businessTableHis.updateBusinessTableHis',
      method: 'post',
      data
    }).then(response => {
      const res = response.data
      if (res.code == 0) {
        resolve(res)
      } else {
        reject(new Error(res.msg || '更新业务轨迹失败'))
      }
    }).catch(error => {
      reject(error)
    })
  })
}

// 删除业务轨迹
export function deleteBusinessTableHis(hisId) {
  return new Promise((resolve, reject) => {
    request({
      url: '/businessTableHis.deleteBusinessTableHis',
      method: 'post',
      data: { hisId }
    }).then(response => {
      const res = response.data
      if (res.code == 0) {
        resolve(res)
      } else {
        reject(new Error(res.msg || '删除业务轨迹失败'))
      }
    }).catch(error => {
      reject(error)
    })
  })
}
