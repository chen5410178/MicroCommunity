import i18n from './index'

// 已加载语言包记录，避免重复加载
const loaded = {}

// 语言包按语言拆分，切换语言时才下载对应语言 chunk
const loaders = {
  zh: function () {
    return import(/* webpackChunkName: "locale-zh" */ './locales/zh.js')
  },
  en: function () {
    return import(/* webpackChunkName: "locale-en" */ './locales/en.js')
  },
  'zh-TW': function () {
    return import(/* webpackChunkName: "locale-zh-TW" */ './locales/zh-TW.js')
  }
}

/**
 * 判断某语言包是否已加载
 * @param {String} locale 语言标识 zh / en / zh-TW
 */
export function isLocaleLoaded(locale) {
  return !!loaded[locale]
}

/**
 * 按需加载语言包，加载完成后写入 i18n 实例
 * @param {String} locale 语言标识 zh / en / zh-TW
 * @returns {Promise}
 */
export function loadLocaleMessages(locale) {
  const lang = locale || 'zh'
  if (loaded[lang]) {
    return Promise.resolve()
  }
  const loader = loaders[lang] || loaders.zh
  return loader().then(function (mod) {
    i18n.setLocaleMessage(lang, mod.default)
    loaded[lang] = true
  })
}

export default loadLocaleMessages
