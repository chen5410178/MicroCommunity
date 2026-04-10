import request from '@/utils/request'

// 获取设备台账列表
export function listEquipmentAccount(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/equipmentAccount.listEquipmentAccount',
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

// 获取设备类型列表
export function listMachineType(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/machineType.listMachineType',
      method: 'get',
      params
    }).then(response => {
      const res = response.data
      resolve(res.data)
    }).catch(error => {
      reject(error)
    })
  })
}

// 获取组织树
export function listOrgTree(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/org.listOrgTree',
      method: 'get',
      params
    }).then(response => {
      const res = response.data
      resolve(res.data)
    }).catch(error => {
      reject(error)
    })
  })
}

// 查询员工信息
export function queryStaffInfos(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/query.staff.infos',
      method: 'get',
      params
    }).then(response => {
      const res = response.data
      resolve({
        staffs: res.staffs
      })
    }).catch(error => {
      reject(error)
    })
  })
}

// 修改设备状态
export function changeStateEquipment(data) {
  return new Promise((resolve, reject) => {
    request({
      url: '/equipmentAccount.changeStateEquipment',
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

// 移动设备
export function moveEquipment(data) {
  return new Promise((resolve, reject) => {
    request({
      url: '/equipmentAccount.moveEquipment',
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

// 删除设备台账
export function deleteEquipmentAccount(data) {
  return new Promise((resolve, reject) => {
    request({
      url: '/equipmentAccount.deleteEquipmentAccount',
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

// 获取设备详情
export function getEquipmentAccountDetail(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/equipmentAccount.getEquipmentAccountDetail',
      method: 'get',
      params
    }).then(response => {
      const res = response.data
      resolve(res.data)
    }).catch(error => {
      reject(error)
    })
  })
}

// 导入设备数据
export function importEquipmentData(data) {
  return new Promise((resolve, reject) => {
    request({
      url: '/importExportEquipment.importData',
      method: 'post',
      data,
      headers: {
        'Content-Type': 'multipart/form-data'
      }
    }).then(response => {
      const res = response.data
      resolve(res)
    }).catch(error => {
      reject(error)
    })
  })
}

// 导出导入模板
export function exportImportTemplate(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/importExportEquipment.exportImportDataTemp',
      method: 'get',
      params,
      responseType: 'blob'
    }).then(response => {
      // 创建下载链接
      const url = window.URL.createObjectURL(new Blob([response.data]))
      const link = document.createElement('a')
      link.href = url
      link.setAttribute('download', 'equipment_import_template.xlsx')
      document.body.appendChild(link)
      link.click()
      document.body.removeChild(link)
      resolve()
    }).catch(error => {
      reject(error)
    })
  })
}

// 导出设备数据
export function exportEquipmentData(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/importExportEquipment.exportData',
      method: 'get',
      params,
      responseType: 'blob'
    }).then(response => {
      // 创建下载链接
      const url = window.URL.createObjectURL(new Blob([response.data]))
      const link = document.createElement('a')
      link.href = url
      link.setAttribute('download', 'equipment_export_data.xlsx')
      document.body.appendChild(link)
      link.click()
      document.body.removeChild(link)
      resolve()
    }).catch(error => {
      reject(error)
    })
  })
}