import request from '@/utils/request'

// 查询报修回访列表
export function getRepairReturnVisitList(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/repair/queryRepairReturnVisit',
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
        reject(new Error(res.msg || '查询报修回访列表失败'))
      }
    }).catch(error => {
      reject(error)
    })
  })
}

// 保存报修回访记录
export function saveRepairReturnVisit(data) {
  return new Promise((resolve, reject) => {
    request({
      url: '/repair/saveRepairReturnVisit',
      method: 'post',
      data
    }).then(response => {
      const res = response.data
      if (res.code === 0) {
        resolve(res)
      } else {
        reject(new Error(res.msg || '保存回访记录失败'))
      }
    }).catch(error => {
      reject(error)
    })
  })
}

// 查询报修类型设置
export function listRepairSettings(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/repair.listRepairSettings',
      method: 'get',
      params
    }).then(response => {
      const res = response.data
      if (res.code === 0) {
        resolve({
          data: res.data
        })
      } else {
        reject(new Error(res.msg || '查询报修类型设置失败'))
      }
    }).catch(error => {
      reject(error)
    })
  })
}