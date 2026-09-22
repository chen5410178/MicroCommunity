import Vue from 'vue'
import VueI18n from 'vue-i18n'
import ElementLocale from 'element-ui/lib/locale'
import config from '@/conf/config'

Vue.use(VueI18n)

// 语言包已按语言拆分到 locales/{zh,en,zh-TW}.js，
// 由 i18n/loader.js 的 loadLocaleMessages(locale) 在应用启动时按需加载当前语言，
// 切换语言时再动态加载对应语言 chunk，首屏只下载当前语言。
const i18n = new VueI18n({
  locale: config.lang,
  messages: {}
})

// 配置Element UI的语言
ElementLocale.i18n((key, value) => i18n.t(key, value))

export default i18n
