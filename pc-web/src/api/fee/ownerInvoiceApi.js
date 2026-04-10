import request from '@/utils/request'
import { getCommunityId } from '@/api/community/communityApi'

// 查询业主发票列表
export function listOwnerInvoice(params) {
  return new Promise((resolve, reject) => {
    params.communityId = getCommunityId()
    request({
      url: '/invoice.listOwnerInvoice',
      method: 'get',
      params
    }).then(response => {
      const res = response.data
      if (res.code === 0) {
        resolve(res)
      } else {
        reject(new Error(res.msg || '获取发票列表失败'))
      }
    }).catch(error => {
      reject(error)
    })
  })
}

// 更新业主发票
export function updateOwnerInvoice(data) {
  return new Promise((resolve, reject) => {
    data.communityId = getCommunityId()
    request({
      url: '/invoice.updateOwnerInvoice',
      method: 'post',
      data
    }).then(response => {
      const res = response.data
      if (res.code === 0) {
        resolve(res)
      } else {
        reject(new Error(res.msg || '更新发票失败'))
      }
    }).catch(error => {
      reject(error)
    })
  })
}

// 删除业主发票
export function deleteOwnerInvoice(data) {
  return new Promise((resolve, reject) => {
    data.communityId = getCommunityId()
    request({
      url: '/invoice.deleteOwnerInvoice',
      method: 'post',
      data
    }).then(response => {
      const res = response.data
      if (res.code === 0) {
        resolve(res)
      } else {
        reject(new Error(res.msg || '删除发票失败'))
      }
    }).catch(error => {
      reject(error)
    })
  })
}