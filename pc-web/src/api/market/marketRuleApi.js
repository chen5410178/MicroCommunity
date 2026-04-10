import request from '@/utils/request'

// 获取营销规则列表
export function listMarketRule(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/marketRule.listMarketRule',
      method: 'get',
      params
    }).then(response => {
      const res = response.data
      if (res.code == 0) {
        resolve(res)
      } else {
        reject(new Error(res.msg || '获取营销规则列表失败'))
      }
    }).catch(error => {
      reject(error)
    })
  })
}

// 添加营销规则
export function saveMarketRule(data) {
  return new Promise((resolve, reject) => {
    request({
      url: '/marketRule.saveMarketRule',
      method: 'post',
      data
    }).then(response => {
      const res = response.data
      if (res.code == 0) {
        resolve(res)
      } else {
        reject(new Error(res.msg || '添加营销规则失败'))
      }
    }).catch(error => {
      reject(error)
    })
  })
}

// 更新营销规则
export function updateMarketRule(data) {
  return new Promise((resolve, reject) => {
    request({
      url: '/marketRule.updateMarketRule',
      method: 'post',
      data
    }).then(response => {
      const res = response.data
      if (res.code == 0) {
        resolve(res)
      } else {
        reject(new Error(res.msg || '更新营销规则失败'))
      }
    }).catch(error => {
      reject(error)
    })
  })
}

// 删除营销规则
export function deleteMarketRule(data) {
  return new Promise((resolve, reject) => {
    request({
      url: '/marketRule.deleteMarketRule',
      method: 'post',
      data
    }).then(response => {
      const res = response.data
      if (res.code == 0) {
        resolve(res)
      } else {
        reject(new Error(res.msg || '删除营销规则失败'))
      }
    }).catch(error => {
      reject(error)
    })
  })
}

// 获取营销方式列表
export function listMarketRuleWay(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/marketRule.listMarketRuleWay',
      method: 'get',
      params
    }).then(response => {
      const res = response.data
      if (res.code == 0) {
        resolve(res)
      } else {
        reject(new Error(res.msg || '获取营销方式列表失败'))
      }
    }).catch(error => {
      reject(error)
    })
  })
}

// 添加营销方式
export function saveMarketRuleWay(data) {
  return new Promise((resolve, reject) => {
    request({
      url: '/marketRule.saveMarketRuleWay',
      method: 'post',
      data
    }).then(response => {
      const res = response.data
      if (res.code == 0) {
        resolve(res)
      } else {
        reject(new Error(res.msg || '添加营销方式失败'))
      }
    }).catch(error => {
      reject(error)
    })
  })
}
export function deleteMarketRuleWay(data) {
  return new Promise((resolve, reject) => {
    request({
      url: '/marketRule.deleteMarketRuleWay',
      method: 'post',
      data
    }).then(response => {
      const res = response.data
      if (res.code == 0) {
        resolve(res)
      } else {
        reject(new Error(res.msg || '删除营销方式失败'))
      }
    }).catch(error => {
      reject(error)
    })
  })
}

// 获取营销文本列表
export function listMarketText(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/marketText.listMarketText',
      method: 'get',
      params
    }).then(response => {
      const res = response.data
      if (res.code == 0) {
        resolve(res)
      } else {
        reject(new Error(res.msg || '获取营销文本列表失败'))
      }
    }).catch(error => {
      reject(error)
    })
  })
}

// 获取营销图片列表
export function listMarketPic(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/marketPic.listMarketPic',
      method: 'get',
      params
    }).then(response => {
      const res = response.data
      if (res.code == 0) {
        resolve(res)
      } else {
        reject(new Error(res.msg || '获取营销图片列表失败'))
      }
    }).catch(error => {
      reject(error)
    })
  })
}

// 获取营销商品列表
export function listMarketGoods(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/marketGoods.listMarketGoods',
      method: 'get',
      params
    }).then(response => {
      const res = response.data
      if (res.code == 0) {
        resolve(res)
      } else {
        reject(new Error(res.msg || '获取营销商品列表失败'))
      }
    }).catch(error => {
      reject(error)
    })
  })
}

// 获取小区列表
export function listCommunitys(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/community.listCommunitys',
      method: 'get',
      params
    }).then(response => {
      const res = response.data
      if (res.code == 0) {
        resolve(res)
      } else {
        reject(new Error(res.msg || '获取小区列表失败'))
      }
    }).catch(error => {
      reject(error)
    })
  })
}

// 添加营销规则小区
export function saveMarketRuleCommunity(data) {
  return new Promise((resolve, reject) => {
    request({
      url: '/marketRule.saveMarketRuleCommunity',
      method: 'post',
      data
    }).then(response => {
      const res = response.data
      if (res.code == 0) {
        resolve(res)
      } else {
        reject(new Error(res.msg || '添加营销规则小区失败'))
      }
    }).catch(error => {
      reject(error)
    })
  })
}

// 获取营销规则小区列表
export function listMarketRuleCommunity(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/marketRule.listMarketRuleCommunity',
      method: 'get',
      params
    }).then(response => {
      const res = response.data
      if (res.code == 0) {
        resolve(res)
      } else {
        reject(new Error(res.msg || '获取营销规则小区列表失败'))
      }
    }).catch(error => {
      reject(error)
    })
  })
}

export function deleteMarketRuleCommunity(data) {
  return new Promise((resolve, reject) => {
    request({
      url: '/marketRule.deleteMarketRuleCommunity',
      method: 'post',
      data
    }).then(response => {
      const res = response.data
      if (res.code == 0) {
        resolve(res)
      } else {
        reject(new Error(res.msg || '删除营销规则小区失败'))
      }
    }).catch(error => {
      reject(error)
    })
  })
}

// 添加营销规则对象
export function saveMarketRuleObj(data) {
  return new Promise((resolve, reject) => {
    request({
      url: '/marketRule.saveMarketRuleObj',
      method: 'post',
      data
    }).then(response => {
      const res = response.data
      if (res.code == 0) {
        resolve(res)
      } else {
        reject(new Error(res.msg || '添加营销规则对象失败'))
      }
    }).catch(error => {
      reject(error)
    })
  })
}

// 获取营销规则对象列表
export function listMarketRuleObj(params) {
  return new Promise((resolve, reject) => {
    request({
      url: '/marketRule.listMarketRuleObj',
      method: 'get',
      params
    }).then(response => {
      const res = response.data
      if (res.code == 0) {
        resolve(res)
      } else {
        reject(new Error(res.msg || '获取营销规则对象列表失败'))
      }
    }).catch(error => {
      reject(error)
    })
  })
}

export function deleteMarketRuleObj(data) {
  return new Promise((resolve, reject) => {
    request({
      url: '/marketRule.deleteMarketRuleObj',
      method: 'post',
      data
    }).then(response => {
      const res = response.data
      if (res.code == 0) {
        resolve(res)
      } else {
        reject(new Error(res.msg || '删除营销规则对象失败'))
      }
    }).catch(error => {
      reject(error)
    })
  })
}