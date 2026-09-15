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
      resolve(res)
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
        reject(new Error(res.msg || 'error'))
      }
    }).catch(error => {
      reject(error)
    })
  })
}

// 查询发票红冲列表(先按筛选条件取物业 applyId, 再查商城红冲; 商城未开启返回空列表)
export function listInvoiceReds(params) {
  return new Promise((resolve, reject) => {
    const communityId = getCommunityId()
    request({
      url: '/invoice.listInvoiceReds',
      method: 'get',
      params: {
        ...params,
        communityId
      }
    }).then(response => {
      const res = response.data
      resolve(res)
    }).catch(error => {
      reject(error)
    })
  })
}

// 发起红冲(商城为红冲单与冲红状态权威源, 物业端不落库)
export function saveInvoiceRed(data) {
  return new Promise((resolve, reject) => {
    const communityId = getCommunityId()
    request({
      url: '/invoice.saveInvoiceRed',
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
        reject(new Error(res.msg || 'error'))
      }
    }).catch(error => {
      reject(error)
    })
  })
}

// 提交失败重新提交(商城按 ext_apply_id 幂等, 抬头随开票报文 upsert)
export function reSubmitInvoiceApply(data) {
  return new Promise((resolve, reject) => {
    const communityId = getCommunityId()
    request({
      url: '/invoice.reSubmitInvoiceApply',
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
        reject(new Error(res.msg || 'error'))
      }
    }).catch(error => {
      reject(error)
    })
  })
}
