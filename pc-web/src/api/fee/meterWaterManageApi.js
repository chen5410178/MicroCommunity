import request from '@/utils/request'
import { getCommunityId } from '@/api/community/communityApi'

// 查询抄表记录列表
export function listMeterWaters(params) {
  return new Promise((resolve, reject) => {
    params.communityId = getCommunityId()
    request({
      url: '/meterWater.listMeterWaters',
      method: 'get',
      params
    }).then(response => {
      const res = response.data
      resolve({
        data: res.data,
        total: res.total,
        records: res.records
      })
    }).catch(error => {
      reject(error)
    })
  })
}

// 查询抄表类型列表
export function listMeterTypes(params) {
  return new Promise((resolve, reject) => {
    params.communityId = getCommunityId()
    request({
      url: '/meterType.listMeterType',
      method: 'get',
      params
    }).then(response => {
      const res = response.data
      resolve({
        data: res.data,
        total: res.total
      })
    }).catch(error => {
      reject(error)
    })
  })
}

// 查询楼栋列表
export function queryFloors(params) {
  return new Promise((resolve, reject) => {
    params.communityId = getCommunityId()
    request({
      url: '/floor.queryFloors',
      method: 'get',
      params
    }).then(response => {
      const res = response.data
      resolve({
        data: res.apiFloorDataVoList,
        total: res.total
      })
    }).catch(error => {
      reject(error)
    })
  })
}

// 查询单元列表
export function queryUnits(params) {
  return new Promise((resolve, reject) => {
    params.communityId = getCommunityId()
    request({
      url: '/unit.queryUnits',
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

// 查询房屋列表
export function queryRooms(params) {
  return new Promise((resolve, reject) => {
    params.communityId = getCommunityId()
    request({
      url: '/room.queryRooms',
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

// 查询房屋树形列表
export function queryRoomsTree(params) {
  return new Promise((resolve, reject) => {
    params.communityId = getCommunityId()
    request({
      url: '/room.queryRoomsTree',
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

// 查询上期抄表记录
export function queryPreMeterWater(params) {
  return new Promise((resolve, reject) => {
    params.communityId = getCommunityId()
    request({
      url: '/meterWater/queryPreMeterWater',
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

// 添加抄表记录
export function saveMeterWater(data) {
  return new Promise((resolve, reject) => {
    data.communityId = getCommunityId()
    request({
      url: '/meterWater.saveMeterWater',
      method: 'post',
      data
    }).then(response => {
      const res = response.data
      resolve(res)
    }).catch(error => {
      reject(error)
    })
  })
}

// 修改抄表记录
export function updateMeterWater(data) {
  return new Promise((resolve, reject) => {
    data.communityId = getCommunityId()
    request({
      url: '/meterWater.updateMeterWater',
      method: 'post',
      data
    }).then(response => {
      const res = response.data
      resolve(res)
    }).catch(error => {
      reject(error)
    })
  })
}

// 删除抄表记录
export function deleteMeterWater(data) {
  return new Promise((resolve, reject) => {
    data.communityId = getCommunityId()
    request({
      url: '/meterWater.deleteMeterWater',
      method: 'post',
      data
    }).then(response => {
      const res = response.data
      resolve(res)
    }).catch(error => {
      reject(error)
    })
  })
}

// 查询收费项目列表
export function listFeeConfigs(params) {
  return new Promise((resolve, reject) => {
    params.communityId = getCommunityId()
    request({
      url: '/feeConfig.listFeeConfigs',
      method: 'get',
      params
    }).then(response => {
      const res = response.data
      resolve({
        data: res.feeConfigs,
        total: res.total
      })
    }).catch(error => {
      reject(error)
    })
  })
}

// 导入抄表数据
export function importMeterWaterData(data) {
  return new Promise((resolve, reject) => {
    data.append('communityId', getCommunityId())
    request({
      url: '/callComponent/upload/assetImport/importData',
      method: 'post',
      data,
      headers: {
        'Content-Type': 'multipart/form-data'
      }
    }).then(response => {
      const res = response.data
      resolve(res)
    }).catch(error => {
      reject(error.response.data)
    })
  })
}

// 导出抄表模板
export function exportMeterWaterTemplate(params) {
  return new Promise((resolve, reject) => {
    params.communityId = getCommunityId()
    request({
      url: '/export.exportData',
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