import request from '@/utils/request'

/**
 * 保存考勤班次员工信息
 * @param {Object} data 员工信息
 * @returns {Promise}
 */
export function saveAttendanceClassesStaff(data) {
  return new Promise((resolve, reject) => {
    request({
      url: '/attendanceClasses.saveAttendanceClassesStaff',
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

/**
 * 上传图片
 * @param {FormData} formData 包含图片文件的FormData
 * @returns {Promise}
 */
export function uploadFile(formData) {
  return new Promise((resolve, reject) => {
    request({
      url: '/uploadImage',
      method: 'post',
      data: formData,
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

/**
 * 查询员工信息
 * @param {Object} params 查询参数
 * @returns {Promise}
 */
export function queryStaffInfos(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/query.staff.infos',
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
 * 获取组织树
 * @param {Object} params 查询参数
 * @returns {Promise}
 */
export function listOrgTree(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/org.listOrgTree',
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
 * 下载文件
 * @param {Object} params 文件参数
 * @returns {Promise}
 */
export function downloadFile(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/callComponent/download/getFile/file',
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