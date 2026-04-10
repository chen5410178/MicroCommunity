import request from '@/utils/request'

export function listAuditAppUserBindingOwners(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/owner.listAuditAppUserBindingOwners',
      method: 'get',
      params
    }).then(response => {
      const res = response.data
      if (res.code == 0) {
        resolve(res)
      } else {
        reject(new Error(res.msg || '获取业主APP用户列表失败'))
      }
    }).catch(error => {
      reject(error)
    })
  })
}

export function updateAppUserBindingOwner(data) {
  return new Promise((resolve, reject) => {
    request({
      url: '/owner.updateAppUserBindingOwner',
      method: 'post',
      data
    }).then(response => {
      const res = response.data
      if (res.code == 0) {
        resolve(res)
      } else {
        reject(new Error(res.msg || '审核业主APP用户失败'))
      }
    }).catch(error => {
      reject(error)
    })
  })
}