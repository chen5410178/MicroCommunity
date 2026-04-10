import request from '@/utils/request'

/**
 * Import historical fee detail data
 * @param {FormData} formData 
 * @returns {Promise}
 */
export function importData(formData) {
  return new Promise((resolve, reject) => {
    request({
      url: '/callComponent/upload/assetImport/importData',
      method: 'post',
      data: formData,
      headers: {
        'Content-Type': 'multipart/form-data'
      }
    }).then(response => {
      resolve(response.data)
    }).catch(error => {
      reject(error.response.data)
    })
  })
}

/**
 * Download house template
 * @returns {Promise}
 */
export function downloadHouseTemplate() {
  return new Promise((resolve, reject) => {
    request({
      url: '/import/importFeeDetail.xlsx',
      method: 'get',
      responseType: 'blob'
    }).then(response => {
      resolve(response.data)
    }).catch(error => {
      reject(error)
    })
  })
}

/**
 * Download car template
 * @returns {Promise}
 */
export function downloadCarTemplate() {
  return new Promise((resolve, reject) => {
    request({
      url: '/import/importCarFeeDetail.xlsx',
      method: 'get',
      responseType: 'blob'
    }).then(response => {
      resolve(response.data)
    }).catch(error => {
      reject(error)
    })
  })
}