<template>
  <el-container class="app-wrapper">
    <!-- 顶部导航 -->
    <el-header height="60px" class="app-header">
      <div class="header-left">
        <div class="logo" @click="_toIndex">{{ systemInfo.systemSimpleTitle }}</div>
        <el-menu mode="horizontal" :default-active="activeMenu" class="header-menu" background-color="#1e2132"
          text-color="#fff" active-text-color="#409EFF">
          <template v-for="(item, index) in catalogs">
            <el-menu-item :index="item.caId" v-if="!item.privId || hasPrivilege(item.privId)" :key="index"
              @click="_changeMenuCatalog(item, true)">{{ $t('menu.' + item.name) }}</el-menu-item>
          </template>
        </el-menu>
      </div>
      <div class="header-right">
        <el-dropdown @command="handleSwitchLang" class="margin-right">
          <span class="user-info">
            <i class="el-icon-language"></i>
            {{ currentLangLabel }}
          </span>
          <el-dropdown-menu slot="dropdown">
            <el-dropdown-item command="zh">中文</el-dropdown-item>
            <el-dropdown-item command="en">English</el-dropdown-item>
          </el-dropdown-menu>
        </el-dropdown>
        <el-dropdown @command="changeCommunity" v-if="storeInfo.storeTypeCd == '800900000003'" class="margin-right">
          <span class="user-info">
            {{ curCommunityName }}
            <i class="el-icon-arrow-down el-icon--right"></i>
          </span>
          <el-dropdown-menu slot="dropdown">
            <el-dropdown-item :command="c" v-for="(c, index) in communitys" :key="index">{{ c.name }}</el-dropdown-item>
            <el-dropdown-item command="moreCommunity" class="moreCommunity">{{ $t('layout.moreCommunity')
              }}</el-dropdown-item>
          </el-dropdown-menu>
        </el-dropdown>
        <el-dropdown @command="handleCommand">
          <span class="user-info">
            {{ username }}
            <i class="el-icon-arrow-down el-icon--right"></i>
          </span>
          <el-dropdown-menu slot="dropdown">
            <el-dropdown-item command="logout">{{ $t('layout.logout') }}</el-dropdown-item>
          </el-dropdown-menu>
        </el-dropdown>
        <span class="margin-left">{{ systemInfo.version }}</span>

      </div>
    </el-header>

    <el-container>
      <!-- 左侧菜单 width="64px"-->
      <el-aside class="app-aside" width="80">
        <div class="vc-menu flex justify-start" id="menu-nav">
          <div class="vc-menu-main">
            <ul>
              <template v-for="(menu, index) in menuTree">
                <li :key="index" v-if="menu.childs && menu.childs.length > 0" :class="{ active: menu.active }"
                  @click="switchMenu(menu)">
                  <i class="fa " v-bind:class="menu.icon"></i><span class="margin-left-xs">{{ $t('menu.' + menu.name)
                    }}</span>
                </li>
              </template>
            </ul>
          </div>
          <div class="vc-menu-sub" v-if="subMenus && subMenus.length > 0">
            <ul>
              <li class="title">{{ $t('menu.' + curMenuName) }}</li>
              <template v-for="(subMenu, index) in subMenus">
                <li :key="index" v-if="subMenu.isShow == 'Y'" :class="{ active: subMenu.active }"
                  v-on:click="_gotoPage(subMenu.href, subMenu.name)">
                  {{ $t('menu.' + subMenu.name) }}
                </li>
              </template>
              <li class="sub-footer" @click="_closeSubMenu()"><i class="fa fa-dedent"></i></li>
            </ul>
          </div>
        </div>
      </el-aside>

      <!-- 主要内容区 -->
      <el-main class="app-main">
        <div class="tabs-wrap" v-if="visitedTabs.length > 0">
          <div class="tabs-list">
            <template v-for="(tab, index) in visitedTabs">
              <div :key="index" v-if="tab.title" :class="['tabs-item', { active: $route.path === tab.path }]"
                @click="goTab(tab)">
                <span class="tabs-item-title">{{ $t('menu.' + tab.title) }}</span>
                <i class="el-icon-close tabs-item-close" @click.stop="removeTab(tab)" v-if="visitedTabs.length > 1"></i>
              </div>
            </template>
          </div>
          <el-button type="text" size="mini" class="tabs-close-all" @click="closeAllTabs">
            {{ $t('layout.closeAllTabs') }}
          </el-button>
        </div>
        <router-view />
      </el-main>
    </el-container>
    <more-community ref="moreCommunity" />
    <view-menu-user-list ref="viewMenuUserModel" />
    <search-community-data-list ref="searchCommunityDataModel" />
  </el-container>
</template>

<script>
import { _getMenuCatalog, getMenuTree, jumpToMall, jumpToIot } from '@/api/user/menuApi'
import { getStoreInfo } from "@/api/user/indexApi"
import { listSystemInfo } from '@/api/system/systemInfoManageApi'
import { deepCopy, setCurrentCommunity } from "@/utils/vc"
import { getCommunityName, _loadCommunityInfo } from '@/api/community/communityApi'
import moreCommunity from '@/components/community/moreCommunity.vue'
import viewMenuUserList from '@/components/system/viewMenuUserList.vue'
import searchCommunityDataList from '@/components/system/searchCommunityDataList.vue'
import { initSystem } from '@/api/util/vcApi'
import config from '@/conf/config'


export default {
  name: 'Layout',
  data() {
    return {
      catalogs: [],
      menuTree: [],
      subMenus: [],
      menuWidth: 80,
      menuId: '',
      curMenuName: '',
      activeMenu: '',
      activeSubMenu: 'communityList',
      searchText: '',
      username: '',
      curCommunityName: '',
      communitys: [],
      storeInfo: {
        storeTypeCd: '',
        storeId: ''
      },
      systemInfo: {
        systemSimpleTitle: '',
        companyName: '',
        logoUrl: '',
        version: ''
      },
      visitedTabs: []
    }
  },
  created() {
    let _user = JSON.parse(localStorage.getItem('user'));
    this.username = _user.name
    this.systemInfo.version = config.version
    this.getSystemInfo()
    this._loadStoreInfo()
    this.loadCatalogs()
    this.curCommunityName = getCommunityName()
    this.loadCommunity()
    initSystem()
  },
  watch: {
    $route: {
      handler(route) {
        if (!route.path || route.path === '/views/user/login' || route.path === '/login') return
        var path = route.path
        var exists = this.visitedTabs.some(function (t) { return t.path === path })
        if (!exists) {
          var info = this.getTabTitle(path, route.meta || {})
          if (info) this.visitedTabs.push({ path: path, title: info.title, titleKey: info.titleKey })
        }
      },
      immediate: true
    }
  },
  components: {
    moreCommunity,
    viewMenuUserList,
    searchCommunityDataList
  },
  computed: {
    currentLangLabel() {
      return this.$i18n.locale === 'zh' ? this.$t('layout.chinese') : this.$t('layout.english')
    }
  },
  methods: {
    async getSystemInfo() {
      const { data } = await listSystemInfo({ page: 1, row: 1 });
      this.systemInfo.systemSimpleTitle = data[0].systemSimpleTitle
      this.systemInfo.companyName = data[0].companyName
      this.systemInfo.logoUrl = data[0].logoUrl
    },
    async loadCommunity() {
      const { communitys } = await _loadCommunityInfo()
      this.communitys = communitys
      this.curCommunityName = getCommunityName()

    },
    async _loadStoreInfo() {
      this.loading = true
      try {
        const res = await getStoreInfo()
        deepCopy(res, this.storeInfo);
      } catch (error) {
        console.error('登录失败：', error)
      } finally {
        this.loading = false
      }

    },
    async loadCatalogs() {
      this.loading = true
      try {
        const res = await _getMenuCatalog()
        if (res.code != 0) {
          return;
        }
        this.catalogs = res.data;
        if (this.catalogs && this.catalogs.length > 0) {
          this._changeMenuCatalog(this.catalogs[0])
        }
      } catch (error) {
        console.error('登录失败：', error)

      } finally {
        this.loading = false
      }
    },
    _changeMenuCatalog(_catalog, _isJump) {
      this.activeMenu = _catalog.caId;
      if (this._showModelDiv(_catalog)) {
        return;
      }
      if (_catalog.caId === '1') {
        this.$router.push('/#/pages/mall/product');
      }
      if (_catalog.url == 'IOT') {
        jumpToIot('/');
        return;
      }
      if (_catalog.url == 'MALL') {
        //获取用户名
        jumpToMall('/');
        return;
      }
      console.log(_catalog)
      if (_catalog.url != '#' && _isJump) {
        this._gotoPage(_catalog.url, _catalog.name)
        //return;
      }
      this.loadMenuTree(_catalog)
    },

    handleCommand(command) {
      if (command === 'logout') {
        // 处理退出登录

        localStorage.removeItem('token')
        localStorage.clear();
        this.$router.push('/login')
      }
    },
    changeCommunity(community) {
      if (community == 'moreCommunity') {
        this.$refs.moreCommunity.open()
        return
      }
      setCurrentCommunity(community);
      this.curCommunityName = getCommunityName()
      window.location.href = "/"
    },
    handleMenuSelect(index) {
      // 处理菜单选择
      this.$router.push({ name: index })
    },
    loadMenuTree(_catalog) {
      getMenuTree(_catalog).then(res => {
        let _menus = res.data;
        _menus.sort(function (a, b) {
          return a.seq - b.seq
        });
        this.menuTree = _menus;
      })
    },
    switchMenu(_menu) {
      //设置菜单ID
      this.menuId = _menu.id;
      this.menuTree = this.refreshMenuActive(this.menuTree, _menu.id);
      this.subMenus = _menu.childs;
      this.curMenuName = _menu.name;

      if (!_menu.childs || _menu.childs.length < 1) {
        return;
      }
      //选中第一个
      this._gotoPage(_menu.childs[0].href, _menu.childs[0].name);
      //vc._fix_height()
    },
    refreshMenuActive(jsonArray, _id) {
      for (let menuIndex = 0; menuIndex < jsonArray.length; menuIndex++) {
        if (jsonArray[menuIndex].childs) {
          let _childs = jsonArray[menuIndex].childs;
          _childs.sort(function (_child, _newChild) {
            return _child.seq - _newChild.seq
          });
          jsonArray[menuIndex].childs = _childs;
        }

        if (_id === jsonArray[menuIndex].id) {
          //如果当前本身是打开状态，说明 需要关闭
          jsonArray[menuIndex].active = true;
          continue;
        }
        jsonArray[menuIndex].active = false;
      }
      return jsonArray;
    },
    _setSelectedMenusChild(_href) {
      for (var menuIndex = 0; menuIndex < this.menuTree.length; menuIndex++) {
        if (this.menuTree[menuIndex].childs) {
          var _childs = this.menuTree[menuIndex].childs;
          for (var childIndex = 0; childIndex < _childs.length; childIndex++) {
            if (_href == _childs[childIndex].href) {
              this.menuTree[menuIndex].childs[childIndex].active = true;
            } else {
              this.menuTree[menuIndex].childs[childIndex].active = false;
            }
          }
        }
      }
      this.$forceUpdate();
    },
    _gotoPage(_href, _tabName) {
      console.log(_href, _tabName)
      // 子菜单默认选中
      this._setSelectedMenusChild(_href);
      if (_href.indexOf('.html') > -1) {
        window.open(_href, '_blank')
        return
      }
      if (_href.indexOf('/#/') > -1) {
        _href = _href.replace('/#/', '/')
      }

      this.$router.push(_href).catch(err => {
        // 忽略重复导航错误
        if (err.name !== 'NavigationDuplicated') {
          throw err
        }
      })
    },
    _showModelDiv: function (_catalog) {
      console.log(_catalog)
      if (_catalog.url.startsWith('?')) {
        let _modelName = _catalog.url.substring(1, _catalog.url.length);
        this.$refs[_modelName].open()
        return true;
      }
      return false;
    },
    _toIndex() {
      location.href = '/'
    },
    _closeSubMenu() {
      this.subMenus = []
      this.curMenuName = ''
    },
    getTabTitle(path, meta) {
      if (path === '/views/index/index' || path === '/') {
        return { titleKey: 'layout.home' }
      }
      var m = meta || {}
      if (m.title) return { title: m.title }
      if (m.titleKey) return { titleKey: m.titleKey }
      return null
    },
    goTab(tab) {
      if (this.$route.path === tab.path) return
      this.$router.push(tab.path).catch(function () { })
    },
    removeTab(tab) {
      var list = this.visitedTabs.filter(function (t) { return t.path !== tab.path })
      this.visitedTabs = list
      if (this.$route.path === tab.path) {
        var next = list.length ? list[list.length - 1] : { path: '/views/index/index' }
        this.$router.push(next.path).catch(function () { })
      }
    },
    closeAllTabs() {
      this.visitedTabs = []
      this.$router.push('/views/index/index').catch(function () { })
    },
    handleSwitchLang(command) {
      this.$i18n.locale = command
      localStorage.setItem('language', command)
    }

  }
}
</script>

<style lang="scss" scoped>
.app-wrapper {
  height: 100vh;
}

.app-header {
  background-color: #1e2132;
  color: #fff;
  padding: 0;
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.header-left {
  display: flex;
  align-items: center;
}

.logo {
  width: 100px;
  height: 60px;
  line-height: 60px;
  text-align: center;
  font-size: 24px;
  font-weight: bold;
  color: #409EFF;
  cursor: pointer;
}

.header-menu {
  border: none;
}

.header-right {
  display: flex;
  align-items: center;
  margin-right: 20px;
}

.margin-left {
  margin-left: 15px;
  color: #fff;
}

.user-info {
  color: #fff;
  cursor: pointer;
}

.app-aside {
  width: auto;
}

.menu-wrapper {
  height: 100%;
}

.el-menu-vertical {
  border: none;
}

.el-menu-vertical:not(.el-menu--collapse) {
  width: 200px;
}

.app-main {
  background-color: #f5f7fa;
  height: calc(100vh - 60px);
  padding: 0px;
}

.tabs-wrap {
  display: flex;
  align-items: center;
  padding: 8px 16px;
  background: #fff;
  border-bottom: 1px solid #ebeef5;
  gap: 8px;
}

.tabs-list {
  display: flex;
  flex-wrap: nowrap;
  gap: 4px;
  flex: 1;
  min-width: 0;
  overflow-x: auto;
}

.tabs-item {
  display: inline-flex;
  align-items: center;
  padding: 6px 12px;
  font-size: 13px;
  border-radius: 4px;
  cursor: pointer;
  color: #606266;
  background: #f4f4f5;
  max-width: 160px;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.tabs-item:hover {
  color: #409EFF;
  background: #ecf5ff;
}

.tabs-item.active {
  color: #409EFF;
  background: #ecf5ff;
  font-weight: 500;
}

.tabs-item-title {
  flex: 1;
  min-width: 0;
  overflow: hidden;
  text-overflow: ellipsis;
}

.tabs-item-close {
  margin-left: 6px;
  font-size: 12px;
  opacity: 0.7;
}

.tabs-item-close:hover {
  opacity: 1;
  color: #f56c6c;
}

.tabs-close-all {
  flex-shrink: 0;
  padding: 4px 8px;
}

:deep(.el-menu--horizontal) {
  border-bottom: none;
}

:deep(.el-menu-item) {
  height: 50px;
  line-height: 50px;
}

:deep(.el-submenu__title) {
  height: 50px;
  line-height: 50px;
  padding: 0 20px !important;
}

:deep(.el-menu--collapse) {
  width: 64px;
}

:deep(.el-menu--collapse .el-submenu__title) {
  padding: 0 !important;
  text-align: center;
}

:deep(.el-menu--collapse .el-submenu__title i) {
  margin: 0;
  font-size: 20px;
}

:deep(.el-menu--popup) {
  min-width: 200px;
  background-color: #1e2132 !important;
}

:deep(.el-menu--popup .el-menu-item) {
  background-color: #1e2132;
  color: #fff;
}

:deep(.el-menu--popup .el-menu-item:hover) {
  background-color: #2a2f45;
}

:deep(.el-menu--popup .el-menu-item.is-active) {
  color: #409EFF;
}

.vc-menu {
  border-right: 1px solid #EFEFF0;

  .vc-menu-main {
    background-color: #1F2339;
    color: #838A9B;
    text-align: center;
    width: 80px;
    font-size: 14px;
    height: calc(100vh - 60px);
    overflow-x: hidden;
    overflow-y: scroll;
    scrollbar-width: none;

    .active {
      background-color: #3E77FC;
      color: #FFFFFF;
    }
  }

  .vc-menu-main::-webkit-scrollbar {
    display: none;
  }

  ul {
    padding-inline-start: 0px;
    margin: 0;
    padding: 0;
  }

  li {
    margin: 0;
    padding: 0;
  }
}

.moreCommunity {
  font-weight: 600;
  color: #212529;
  font-size: 12px;
}

.vc-menu-main ul li {
  height: 45px;
  line-height: 45px;
  cursor: pointer;
  list-style-type: none;
}

.vc-menu-main ul li:hover {
  background-color: #3E77FC;
  color: #FFFFFF;
}

.vc-menu-main ul li.title {
  color: #FFFFFF;
  height: 60px;
  font-size: 24px;
  line-height: 60px;
}

.vc-menu-main ul li.title a {
  text-decoration: none;
  cursor: pointer;
  color: #FFFFFF;
}

.vc-menu-sub {
  background-color: #FFFFFF;
  color: #545761;
  text-align: start;
  width: 110px;
  font-size: 14px;
  height: calc(100vh - 60px);
  overflow-x: hidden;
  overflow-y: scroll;
  scrollbar-width: none;
}

.vc-menu-sub::-webkit-scrollbar {
  display: none;
}

.vc-menu-sub .active {
  background-color: #EAF0FE;
  color: #3E77FC;
}



.vc-menu-sub ul li {
  height: 40px;
  line-height: 40px;
  padding-left: 15px;
  cursor: pointer;
  overflow: hidden;
  white-space: nowrap;
  text-overflow: ellipsis;
}

.vc-menu-sub ul li.title {
  color: #4F525C;
  height: 40px;
  font-size: 14px;
  line-height: 40px;
  text-align: center;
  padding-left: 0px;
  border-bottom: 1px solid #F2F2F3;
  margin-bottom: 10px;
}

.vc-menu-sub ul li:hover {
  background-color: #EAF0FE;
  color: #3E77FC;
}

.vc-menu-sub ul li.sub-footer {
  background-color: #F6F6F7;
  text-align: center;
  position: fixed;
  bottom: 0;
  width: 100px;
}
</style>