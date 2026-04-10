import request from '@/utils/request'
import { getCommunityId } from '@/api/community/communityApi'

// 获取报修设置列表
export function listRepairSettings() {
  return new Promise((resolve, reject) => {
    request({
      url: '/repair.listRepairSettings',
      method: 'get',
      params: {
        page: 1,
        row: 50,
        communityId: getCommunityId()
      }
    }).then(response => {
      const res = response.data
      if (res.code == 0) {
        resolve(res.data)
      } else {
        reject(new Error(res.msg || '获取报修设置失败'))
      }
    }).catch(error => {
      reject(error)
    })
  })
}

// 获取报修池列表
export function listOwnerRepairs(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/ownerRepair.listOwnerRepairs',
      method: 'get',
      params: {
        ...params,
        communityId: getCommunityId()
      }
    }).then(response => {
      const res = response.data
      if (res.code == 0) {
        resolve({
          data: res.data,
          total: res.total,
          records: res.records
        })
      } else {
        reject(new Error(res.msg || '获取报修列表失败'))
      }
    }).catch(error => {
      reject(error)
    })
  })
}

// 获取资源物品类型
export function listResourceStoreTypes(parentId = '0') {
  return new Promise((resolve, reject) => {
    request({
      url: '/resourceStoreType.listResourceStoreTypes',
      method: 'get',
      params: {
        page: 1,
        row: 100,
        communityId: getCommunityId(),
        parentId,
        giveType: 1
      }
    }).then(response => {
      const res = response.data
      if (res.code == 0) {
        resolve(res.data)
      } else {
        reject(new Error(res.msg || '获取资源物品类型失败'))
      }
    }).catch(error => {
      reject(error)
    })
  })
}

// 获取用户仓库物品
export function listUserStorehouses(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/resourceStore.listUserStorehouses',
      method: 'get',
      params: {
        ...params,
        communityId: getCommunityId(),
        chooseType: "repair",
        flag: 1,
        sign: 1,
        giveType: 1
      }
    }).then(response => {
      const res = response.data
      if (res.code == 0) {
        resolve(res.data)
      } else {
        reject(new Error(res.msg || '获取仓库物品失败'))
      }
    }).catch(error => {
      reject(error)
    })
  })
}

// 强制回单
export function repairForceFinish(data) {
  return new Promise((resolve, reject) => {
    request({
      url: '/ownerRepair.repairForceFinish',
      method: 'post',
      data: {
        ...data,
        communityId: getCommunityId()
      }
    }).then(response => {
      const res = response.data
      if (res.code == 0) {
        resolve(res)
      } else {
        reject(new Error(res.msg || '强制回单失败'))
      }
    }).catch(error => {
      reject(error)
    })
  })
}

// 获取字典数据
export function getDict(dictType, statusCd) {
  return new Promise((resolve, reject) => {
    request({
      url: '/dict.listDict',
      method: 'get',
      params: {
        dictType,
        statusCd,
        page: 1,
        row: 100
      }
    }).then(response => {
      const res = response.data
      if (res.code == 0) {
        resolve(res.data)
      } else {
        reject(new Error(res.msg || '获取字典失败'))
      }
    }).catch(error => {
      reject(error)
    })
  })
}