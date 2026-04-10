import request from '@/utils/request'
import { getCommunityId } from '@/api/community/communityApi'

// 获取打印机规则列表
export function listPrinterRules(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/printer.listPrinterRule',
      method: 'get',
      params: {
        ...params,
        communityId: getCommunityId()
      }
    }).then(response => {
      resolve(response.data)
    }).catch(error => {
      reject(error)
    })
  })
}

// 添加打印机规则
export function savePrinterRule(data) {
  return new Promise((resolve, reject) => {
    request({
      url: '/printer.savePrinterRule',
      method: 'post',
      data: {
        ...data,
        communityId: getCommunityId()
      }
    }).then(response => {
      resolve(response.data)
    }).catch(error => {
      reject(error)
    })
  })
}

// 修改打印机规则
export function updatePrinterRule(data) {
  return new Promise((resolve, reject) => {
    request({
      url: '/printer.updatePrinterRule',
      method: 'post',
      data: {
        ...data,
        communityId: getCommunityId()
      }
    }).then(response => {
      resolve(response.data)
    }).catch(error => {
      reject(error)
    })
  })
}

// 删除打印机规则
export function deletePrinterRule(data) {
  return new Promise((resolve, reject) => {
    request({
      url: '/printer.deletePrinterRule',
      method: 'post',
      data: {
        ...data,
        communityId: getCommunityId()
      }
    }).then(response => {
      resolve(response.data)
    }).catch(error => {
      reject(error)
    })
  })
}

// 获取打印机规则关联的机器列表
export function listPrinterRuleMachines(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/printer.listPrinterRuleMachine',
      method: 'get',
      params: {
        ...params,
        communityId: getCommunityId()
      }
    }).then(response => {
      resolve(response.data)
    }).catch(error => {
      reject(error)
    })
  })
}

// 添加打印机规则机器
export function savePrinterRuleMachine(data) {
  return new Promise((resolve, reject) => {
    request({
      url: '/printer.savePrinterRuleMachine',
      method: 'post',
      data: {
        ...data,
        communityId: getCommunityId()
      }
    }).then(response => {
      resolve(response.data)
    }).catch(error => {
      reject(error)
    })
  })
}

// 修改打印机规则机器
export function updatePrinterRuleMachine(data) {
  return new Promise((resolve, reject) => {
    request({
      url: '/printer.updatePrinterRuleMachine',
      method: 'post',
      data: {
        ...data,
        communityId: getCommunityId()
      }
    }).then(response => {
      resolve(response.data)
    }).catch(error => {
      reject(error)
    })
  })
}

// 删除打印机规则机器
export function deletePrinterRuleMachine(data) {
  return new Promise((resolve, reject) => {
    request({
      url: '/printer.deletePrinterRuleMachine',
      method: 'post',
      data: {
        ...data,
        communityId: getCommunityId()
      }
    }).then(response => {
      resolve(response.data)
    }).catch(error => {
      reject(error)
    })
  })
}

// 获取打印机列表
export function listMachinePrinters(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/printer.listMachinePrinter',
      method: 'get',
      params: {
        ...params,
        communityId: getCommunityId()
      }
    }).then(response => {
      resolve(response.data)
    }).catch(error => {
      reject(error)
    })
  })
}

// 获取打印机规则关联的费用列表
export function listPrinterRuleFees(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/printer.listPrinterRuleFee',
      method: 'get',
      params: {
        ...params,
        communityId: getCommunityId()
      }
    }).then(response => {
      resolve(response.data)
    }).catch(error => {
      reject(error)
    })
  })
}

// 添加打印机规则费用
export function savePrinterRuleFee(data) {
  return new Promise((resolve, reject) => {
    request({
      url: '/printer.savePrinterRuleFee',
      method: 'post',
      data: {
        ...data,
        communityId: getCommunityId()
      }
    }).then(response => {
      resolve(response.data)
    }).catch(error => {
      reject(error)
    })
  })
}

// 删除打印机规则费用
export function deletePrinterRuleFee(data) {
  return new Promise((resolve, reject) => {
    request({
      url: '/printer.deletePrinterRuleFee',
      method: 'post',
      data: {
        ...data,
        communityId: getCommunityId()
      }
    }).then(response => {
      resolve(response.data)
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
      resolve(response.data)
    }).catch(error => {
      reject(error)
    })
  })
}

// 获取打印机规则关联的报修列表
export function listPrinterRuleRepairs(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/printer.listPrinterRuleRepair',
      method: 'get',
      params: {
        ...params,
        communityId: getCommunityId()
      }
    }).then(response => {
      resolve(response.data)
    }).catch(error => {
      reject(error)
    })
  })
}

// 添加打印机规则报修
export function savePrinterRuleRepair(data) {
  return new Promise((resolve, reject) => {
    request({
      url: '/printer.savePrinterRuleRepair',
      method: 'post',
      data: {
        ...data,
        communityId: getCommunityId()
      }
    }).then(response => {
      resolve(response.data)
    }).catch(error => {
      reject(error)
    })
  })
}

// 删除打印机规则报修
export function deletePrinterRuleRepair(data) {
  return new Promise((resolve, reject) => {
    request({
      url: '/printer.deletePrinterRuleRepair',
      method: 'post',
      data: {
        ...data,
        communityId: getCommunityId()
      }
    }).then(response => {
      resolve(response.data)
    }).catch(error => {
      reject(error)
    })
  })
}

// 获取报修设置列表
export function listRepairSettings(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/repair.listRepairSettings',
      method: 'get',
      params: {
        ...params,
        communityId: getCommunityId()
      }
    }).then(response => {
      resolve(response.data)
    }).catch(error => {
      reject(error)
    })
  })
}