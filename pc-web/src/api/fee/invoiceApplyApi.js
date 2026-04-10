import request from '@/utils/request'
import { getCommunityId } from '@/api/community/communityApi'

// 获取发票申请列表
export function listInvoiceApply(params) {
  return new Promise((resolve, reject) => {
    const communityId = getCommunityId()
    request({
      url: '/invoice.listInvoiceApply',
      method: 'get',
      params: {
        ...params,
        communityId
      }
    }).then(response => {
      const res = response.data
      if (res.code === 0) {
        resolve(res)
      } else {
        reject(new Error(res.msg || '获取发票申请列表失败'))
      }
    }).catch(error => {
      reject(error)
    })
  })
}

// 审核发票申请
export function auditInvoiceApply(data) {
  return new Promise((resolve, reject) => {
    const communityId = getCommunityId()
    request({
      url: '/invoice.auditInvoiceApply',
      method: 'post',
      data: {
        ...data,
        communityId
      }
    }).then(response => {
      const res = response.data
      if (res.code === 0) {
        resolve(res)
      } else {
        reject(new Error(res.msg || '审核发票申请失败'))
      }
    }).catch(error => {
      reject(error)
    })
  })
}

// 删除发票申请
export function deleteInvoiceApply(data) {
  return new Promise((resolve, reject) => {
    const communityId = getCommunityId()
    request({
      url: '/invoice.deleteInvoiceApply',
      method: 'post',
      data: {
        ...data,
        communityId
      }
    }).then(response => {
      const res = response.data
      if (res.code === 0) {
        resolve(res)
      } else {
        reject(new Error(res.msg || '删除发票申请失败'))
      }
    }).catch(error => {
      reject(error)
    })
  })
}

// 上传发票照片
export function uploadInvoicePhoto(data) {
  return new Promise((resolve, reject) => {
    const communityId = getCommunityId()
    request({
      url: '/invoice.uploadInvoicePhoto',
      method: 'post',
      data: {
        ...data,
        communityId
      }
    }).then(response => {
      const res = response.data
      if (res.code === 0) {
        resolve(res)
      } else {
        reject(new Error(res.msg || '上传发票照片失败'))
      }
    }).catch(error => {
      reject(error)
    })
  })
}

// 写入发票事件
export function writeInvoiceEvent(data) {
  return new Promise((resolve, reject) => {
    const communityId = getCommunityId()
    request({
      url: '/invoice.writeInvoiceApply',
      method: 'post',
      data: {
        ...data,
        communityId
      }
    }).then(response => {
      const res = response.data
      if (res.code === 0) {
        resolve(res)
      } else {
        reject(new Error(res.msg || '写入发票事件失败'))
      }
    }).catch(error => {
      reject(error)
    })
  })
}

// 上传图片
export function uploadImage(formData) {
  return new Promise((resolve, reject) => {
    const communityId = getCommunityId()
    formData.append('communityId', communityId)
    
    request({
      url: 'uploadFile/uploadImage',
      method: 'post',
      data: formData,
      headers: {
        'Content-Type': 'multipart/form-data'
      }
    }).then(response => {
      const res = response.data
      if (res.code === 0) {
        resolve(res.data)
      } else {
        reject(new Error(res.msg || '上传图片失败'))
      }
    }).catch(error => {
      reject(error)
    })
  })
}