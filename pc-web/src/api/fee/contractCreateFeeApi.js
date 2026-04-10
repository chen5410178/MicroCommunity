import request from '@/utils/request'
import { getCommunityId } from '@/api/community/communityApi'

// 查询合同列表
export function queryContract(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/contract/queryContract',
      method: 'get',
      params: {
        ...params,
        communityId: getCommunityId()
      }
    }).then(response => {
      const res = response.data
      resolve(res)
    }).catch(error => {
      reject(error)
    })
  })
}

// 保存合同创建费用
export function saveContractCreateFee(data) {
  return new Promise((resolve, reject) => {
    request({
      url: '/fee.saveContractCreateFee',
      method: 'post',
      data: {
        ...data,
        communityId: getCommunityId()
      }
    }).then(response => {
      const res = response.data
      resolve(res)
    }).catch(error => {
      reject(error)
    })
  })
}

// 获取费用配置列表
export function listFeeConfigs(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/feeConfig.listFeeConfigs',
      method: 'get',
      params: {
        ...params,
        communityId: getCommunityId()
      }
    }).then(response => {
      const res = response.data
      resolve(res)
    }).catch(error => {
      reject(error)
    })
  })
}

// 获取字典数据
export function getDict(dictType, dictName) {
  return new Promise((resolve, reject) => {
    request({
      url: '/dict/getDict',
      method: 'get',
      params: {
        dictType,
        dictName,
        communityId: getCommunityId()
      }
    }).then(response => {
      const res = response.data
      resolve(res)
    }).catch(error => {
      reject(error)
    })
  })
}