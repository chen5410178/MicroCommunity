import request from '@/utils/request'

// 查询物业资产指标
export function getPropertyAssetsIndex(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/propertyIndex.queryPropertyAssetsIndex',
      method: 'get',
      params
    }).then(response => {
      const res = response.data
      if (res.code === 0) {
        resolve(res)
      } else {
        reject(new Error(res.msg || '查询物业资产指标失败'))
      }
    }).catch(error => {
      reject(error)
    })
  })
}

// 查询记事本列表
export function getNotepadList(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/notepad.listNotepad',
      method: 'get',
      params
    }).then(response => {
      const res = response.data
      if (res.code === 0) {
        resolve(res)
      } else {
        reject(new Error(res.msg || '查询记事本列表失败'))
      }
    }).catch(error => {
      reject(error)
    })
  })
}

// 查询报修指标
export function getRepairIndex(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/propertyIndex.queryRepairIndex',
      method: 'get',
      params
    }).then(response => {
      const res = response.data
      if (res.code === 0) {
        resolve(res)
      } else {
        reject(new Error(res.msg || '查询报修指标失败'))
      }
    }).catch(error => {
      reject(error)
    })
  })
}

// 查询投诉指标
export function getComplaintIndex(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/propertyIndex.queryComplaintIndex',
      method: 'get',
      params
    }).then(response => {
      const res = response.data
      if (res.code === 0) {
        resolve(res)
      } else {
        reject(new Error(res.msg || '查询投诉指标失败'))
      }
    }).catch(error => {
      reject(error)
    })
  })
}

// 查询业主注册指标
export function getOwnerRegisterIndex(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/propertyIndex.queryOwnerRegisterIndex',
      method: 'get',
      params
    }).then(response => {
      const res = response.data
      if (res.code === 0) {
        resolve(res)
      } else {
        reject(new Error(res.msg || '查询业主注册指标失败'))
      }
    }).catch(error => {
      reject(error)
    })
  })
}