import request from '@/utils/request'

export function searchManualBillTargets(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/fee.searchManualBillTargets',
      method: 'get',
      params
    }).then(response => {
      resolve(response.data)
    }).catch(error => {
      reject(error)
    })
  })
}

export function saveManualBillApplication(data) {
  return new Promise((resolve, reject) => {
    request({
      url: '/fee.saveManualBillApplication',
      method: 'post',
      data
    }).then(response => {
      resolve(response.data)
    }).catch(error => {
      reject(error)
    })
  })
}

export function listManualBillApplications(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/fee.listManualBillApplications',
      method: 'get',
      params
    }).then(response => {
      resolve(response.data)
    }).catch(error => {
      reject(error)
    })
  })
}

export function getManualBillApplication(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/fee.getManualBillApplication',
      method: 'get',
      params
    }).then(response => {
      resolve(response.data)
    }).catch(error => {
      reject(error)
    })
  })
}

export function auditManualBillApplication(data) {
  return new Promise((resolve, reject) => {
    request({
      url: '/fee.auditManualBillApplication',
      method: 'post',
      data
    }).then(response => {
      resolve(response.data)
    }).catch(error => {
      reject(error)
    })
  })
}

export function deleteManualBillApplication(data) {
  return new Promise((resolve, reject) => {
    request({
      url: '/fee.deleteManualBillApplication',
      method: 'post',
      data
    }).then(response => {
      resolve(response.data)
    }).catch(error => {
      reject(error)
    })
  })
}
