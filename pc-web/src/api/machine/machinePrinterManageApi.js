import request from '@/utils/request'
import { getCommunityId } from '@/api/community/communityApi'

// 获取云打印机列表
export function listMachinePrinter(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/printer.listMachinePrinter',
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

// 添加云打印机
export function saveMachinePrinter(data) {
  return new Promise((resolve, reject) => {
    request({
      url: '/printer.saveMachinePrinter',
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

// 修改云打印机
export function updateMachinePrinter(data) {
  return new Promise((resolve, reject) => {
    request({
      url: '/printer.updateMachinePrinter',
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

// 删除云打印机
export function deleteMachinePrinter(data) {
  return new Promise((resolve, reject) => {
    request({
      url: '/printer.deleteMachinePrinter',
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