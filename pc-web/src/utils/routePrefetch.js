// 路由 chunk 智能预取：菜单 hover 即时预取、登录/菜单加载完成后空闲预算批量预取
// 注意：不恢复 webpack 全局 prefetch，预取数量严格受预算控制

// 已预取记录，避免重复触发
const prefetched = {}

// 空闲预取累计次数（单次页面加载的全局预算，默认 8）
let idlePrefetched = 0

/**
 * 预取某个路由涉及的异步组件 chunk（仅下载 JS 并写入浏览器缓存，不渲染）
 * @param {Object} router 路由实例
 * @param {String} key 路由 path 或 name（本项目两者一致）
 */
function prefetchRoute(router, key) {
  if (!router || !key || prefetched[key]) {
    return
  }
  let resolved
  try {
    resolved = router.resolve(key)
  } catch (e) {
    return
  }
  if (!resolved || !resolved.matched || resolved.matched.length < 1) {
    try {
      resolved = router.resolve({ name: key })
    } catch (e) {
      return
    }
  }
  const matched = (resolved && resolved.matched) || []
  const loaders = []
  for (let i = 0; i < matched.length; i++) {
    const components = matched[i].components || {}
    const names = Object.keys(components)
    for (let j = 0; j < names.length; j++) {
      if (typeof components[names[j]] === 'function') {
        loaders.push(components[names[j]])
      }
    }
  }
  // 同步组件（如 Layout）不需要预取
  if (loaders.length < 1) {
    return
  }
  prefetched[key] = true
  for (let k = 0; k < loaders.length; k++) {
    try {
      const promise = loaders[k]()
      if (promise && typeof promise.catch === 'function') {
        promise.catch(function () {
          prefetched[key] = false
        })
      }
    } catch (e) {
      prefetched[key] = false
    }
  }
}

/**
 * 按路由 name 预取该页面 chunk
 */
export function prefetchRouteByName(router, name) {
  prefetchRoute(router, name)
}

/**
 * 按路由 path 预取该页面 chunk
 */
export function prefetchRouteByPath(router, path) {
  prefetchRoute(router, path)
}

/**
 * 空闲时按预算逐个预取（登录后、菜单渲染完成后调用）
 * 总预算为单次页面加载内的累计上限，避免多次菜单切换导致预取无节制
 * @param {Object} router 路由实例
 * @param {Array} names 路由 path/name 数组（已按常用度排序、并已按用户权限过滤）
 * @param {Object} options { budget: 8 }
 */
export function prefetchOnIdle(router, names, options) {
  const opts = options || {}
  const budget = opts.budget || 8
  const remain = budget - idlePrefetched
  if (remain <= 0) {
    return
  }
  const list = (names || []).slice(0, remain)
  const idle = window.requestIdleCallback || function (cb) { return setTimeout(cb, 500) }
  let index = 0
  const step = function () {
    if (index >= list.length) {
      return
    }
    idlePrefetched++
    prefetchRoute(router, list[index])
    index++
    idle(step)
  }
  idle(step)
}
