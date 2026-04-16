import request from '@/utils/request'
import { getCommunityId } from "@/api/community/communityApi"

// 获取物品列表
export function listResourceStores(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/resourceStore.listResourceStores',
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

// 获取仓库列表
export function listStorehouses(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/resourceStore.listStorehouses',
      method: 'get',
      params
    }).then(response => {
      const res = response.data
      if (res.code === 0) {
        resolve({ data: res.data })
      } else {
        reject(new Error(res.msg || '获取仓库列表失败'))
      }
    }).catch(error => {
      reject(error)
    })
  })
}

// 获取物品规格列表
export function listResourceStoreSpecifications(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/resourceStore.listResourceStoreSpecifications',
      method: 'get',
      params
    }).then(response => {
      const res = response.data
      if (res.code === 0) {
        resolve({ data: res.data })
      } else {
        reject(new Error(res.msg || '获取物品规格列表失败'))
      }
    }).catch(error => {
      reject(error)
    })
  })
}

// 获取物品类型列表
export function listResourceStoreTypes(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/resourceStoreType.listResourceStoreTypes',
      method: 'get',
      params
    }).then(response => {
      const res = response.data
      if (res.code === 0) {
        resolve({ data: res.data })
      } else {
        reject(new Error(res.msg || '获取物品类型列表失败'))
      }
    }).catch(error => {
      reject(error)
    })
  })
}

// 删除物品
export function deleteResourceStore(data) {
  return new Promise((resolve, reject) => {
    request({
      url: '/resourceStore.deleteResourceStore',
      method: 'post',
      data
    }).then(response => {
      const res = response.data
      if (res.code === 0) {
        resolve(res)
      } else {
        reject(new Error(res.msg || '删除物品失败'))
      }
    }).catch(error => {
      reject(error)
    })
  })
}

// 保存物品
export function saveResourceStore(data) {
  return new Promise((resolve, reject) => {
    request({
      url: '/resourceStore.saveResourceStore',
      method: 'post',
      data
    }).then(response => {
      const res = response.data
      if (res.code === 0) {
        resolve(res)
      } else {
        reject(new Error(res.msg || '保存物品失败'))
      }
    }).catch(error => {
      reject(error)
    })
  })
}

// 更新物品
export function updateResourceStore(data) {
  return new Promise((resolve, reject) => {
    request({
      url: '/resourceStore.updateResourceStore',
      method: 'post',
      data
    }).then(response => {
      const res = response.data
      if (res.code === 0) {
        resolve(res)
      } else {
        reject(new Error(res.msg || '更新物品失败'))
      }
    }).catch(error => {
      reject(error)
    })
  })
}

// 导出数据
export function exportData(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/export.exportData',
      method: 'get',
      params
    }).then(response => {
      const res = response.data
      if (res.code === 0) {
        resolve(res)
      } else {
        reject(new Error(res.msg || '导出数据失败'))
      }
    }).catch(error => {
      reject(error)
    })
  })
}

// 导入数据
export function importData(data) {
  return new Promise((resolve, reject) => {
    request({
      url: '/callComponent/upload/importResourceStore/importData',
      method: 'post',
      data,
      headers: {
        'Content-Type': 'multipart/form-data'
      }
    }).then(response => {
      const res = response.data
      if (res.code === 0) {
        resolve(res)
      } else {
        reject(new Error(res.msg || '导入数据失败'))
      }
    }).catch(error => {
      reject(error.response.data)
    })
  })
}

// 获取物品总价
export function listResourceStoreTimes(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/resourceStoreTimes.listResourceStoreTimes',
      method: 'get',
      params
    }).then(response => {
      const res = response.data
      if (res.code === 0) {
        resolve({ data: res.data, totalPrice: res.totalPrice })
      } else {
        reject(new Error(res.msg || '获取物品总价失败'))
      }
    }).catch(error => {
      reject(error)
    })
  })
}

// 保存物品类型
export function saveResourceStoreType(data) {
  return new Promise((resolve, reject) => {
    request({
      url: '/resourceStoreType.saveResourceStoreType',
      method: 'post',
      data
    }).then(response => {
      const res = response.data
      if (res.code === 0) {
        resolve(res)
      } else {
        reject(new Error(res.msg || '保存物品类型失败'))
      }
    }).catch(error => {
      reject(error)
    })
  })
}

// 更新物品类型
export function updateResourceStoreType(data) {
  return new Promise((resolve, reject) => {
    request({
      url: '/resourceStoreType.updateResourceStoreType',
      method: 'post',
      data
    }).then(response => {
      const res = response.data
      if (res.code === 0) {
        resolve(res)
      } else {
        reject(new Error(res.msg || '更新物品类型失败'))
      }
    }).catch(error => {
      reject(error)
    })
  })
}

// 删除物品类型
export function deleteResourceStoreType(data) {
  return new Promise((resolve, reject) => {
    request({
      url: '/resourceStoreType.deleteResourceStoreType',
      method: 'post',
      data
    }).then(response => {
      const res = response.data
      if (res.code === 0) {
        resolve(res)
      } else {
        reject(new Error(res.msg || '删除物品类型失败'))
      }
    }).catch(error => {
      reject(error)
    })
  })
}

// 上传文件
export function uploadFile(data) {
  return new Promise((resolve, reject) => {
    request({
      url: '/uploadFile.uploadImage',
      method: 'post',
      data,
      headers: {
        'Content-Type': 'multipart/form-data'
      }
    }).then(response => {
      const res = response.data
      if (res.code === 0) {
        resolve(res)
      } else {
        reject(new Error(res.msg || '上传文件失败'))
      }
    }).catch(error => {
      reject(error)
    })
  })
}

// 获取物品类型树
export function listResourceStoreTypeTree() {
  return new Promise((resolve, reject) => {
    request({
      url: '/resourceStore.listResourceStoreTypeTree',
      method: 'get',
      params: {
        communityId: getCommunityId()
      }
    }).then(response => {
      const res = response.data
      if (res.code === 0) {
        resolve(res)
      } else {
        reject(new Error(res.msg || '获取物品类型树失败'))
      }
    }).catch(error => {
      reject(error)
    })
  })
}