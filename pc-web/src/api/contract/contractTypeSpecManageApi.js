import request from '@/utils/request'
import { getCommunityId } from '@/api/community/communityApi'

// 查询合同类型规格列表
export function queryContractTypeSpec(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/contract/queryContractTypeSpec',
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

// 添加合同类型规格
export function saveContractTypeSpec(data) {
  return new Promise((resolve, reject) => {
request({
      url: '/contract/saveContractTypeSpec',
      method: 'post',
      data: {
        ...data,
        communityId: getCommunityId()
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

// 修改合同类型规格
export function updateContractTypeSpec(data) {
  return new Promise((resolve, reject) => {
request({
      url: '/contract/updateContractTypeSpec',
      method: 'post',
      data: {
        ...data,
        communityId: getCommunityId()
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

// 删除合同类型规格
export function deleteContractTypeSpec(data) {
  return new Promise((resolve, reject) => {
request({
      url: '/contract/deleteContractTypeSpec',
      method: 'post',
      data: {
        ...data,
        communityId: getCommunityId()
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