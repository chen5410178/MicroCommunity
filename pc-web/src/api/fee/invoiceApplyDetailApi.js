import request from '@/utils/request'
import { getCommunityId } from '@/api/community/communityApi'

/**
 * 获取发票申请详情
 * @param {Object} params 
 * @returns 
 */
export function getInvoiceApplyDetail(params) {
  return new Promise((resolve, reject) => {
    params.communityId = getCommunityId()
    request({
      url: '/invoice.listInvoiceApply',
      method: 'get',
      params
    }).then(response => {
      const res = response.data
      resolve(res)
    }).catch(error => {
      reject(error)
    })
  })
}

/**
 * 获取发票申请费用明细
 * @param {Object} params 
 * @returns 
 */
export function getInvoiceApplyItemList(params) {
  return new Promise((resolve, reject) => {
    params.communityId = getCommunityId()
    request({
      url: '/invoice.listInvoiceApplyItem',
      method: 'get',
      params
    }).then(response => {
      const res = response.data
      resolve(res)
    }).catch(error => {
      reject(error)
    })
  })
}

/**
 * 获取发票申请审核记录
 * @param {Object} params 
 * @returns 
 */
export function getInvoiceEventList(params) {
  return new Promise((resolve, reject) => {
    params.communityId = getCommunityId()
    request({
      url: '/invoice.listInvoiceEvent',
      method: 'get',
      params
    }).then(response => {
      const res = response.data
      resolve(res)
    }).catch(error => {
      reject(error)
    })
  })
}