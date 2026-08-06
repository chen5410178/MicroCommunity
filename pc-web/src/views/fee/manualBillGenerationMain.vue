<template>
  <div class="app-container padding">
    <el-row :gutter="16">
      <!-- 左侧分类导航 -->
      <el-col :span="4">
        <el-card>
          <div slot="header" class="flex justify-between">
            <span>{{ $t('manualBillGeneration.mainTitle') }}</span>
          </div>
          <el-menu :default-active="activeMenu" @select="handleMenuSelect">
            <el-menu-item index="apply">
              <i class="el-icon-document-add"></i>
              <span>{{ $t('manualBillGeneration.menuApply') }}</span>
            </el-menu-item>
            <el-menu-item index="audit">
              <i class="el-icon-check"></i>
              <span>{{ $t('manualBillGeneration.menuAudit') }}</span>
            </el-menu-item>
            <el-menu-item index="done">
              <i class="el-icon-finished"></i>
              <span>{{ $t('manualBillGeneration.menuDone') }}</span>
            </el-menu-item>
          </el-menu>
        </el-card>
      </el-col>
      <!-- 右侧内容区 -->
      <el-col :span="20">
        <keep-alive>
          <component
            :is="currentComponent"
            :key="activeMenu"
            :ref="activeMenu"
            :tab-type="currentTabType"
            @switchTab="handleSwitchTab"
          />
        </keep-alive>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import ManualBillGenerationList from './manualBillGenerationList.vue'
import ManualBillGenerationManageList from './manualBillGenerationManageList.vue'

export default {
  name: 'ManualBillGenerationMain',
  components: {
    ManualBillGenerationList,
    ManualBillGenerationManageList
  },
  data() {
    return {
      activeMenu: 'apply'
    }
  },
  computed: {
    currentComponent() {
      if (this.activeMenu === 'apply') {
        return ManualBillGenerationList
      }
      return ManualBillGenerationManageList
    },
    currentTabType() {
      if (this.activeMenu === 'audit' || this.activeMenu === 'done') {
        return this.activeMenu
      }
      return ''
    }
  },
  activated() {
    this.refreshChild(this.activeMenu)
  },
  methods: {
    handleMenuSelect(tab) {
      this.activeMenu = tab
      this.refreshChild(tab)
    },
    handleSwitchTab(tab) {
      this.activeMenu = tab
      this.refreshChild(tab)
    },
    refreshChild(tab) {
      this.$nextTick(() => {
        if (this.$refs[tab] && this.$refs[tab].loadData) {
          this.$refs[tab].loadData()
        }
      })
    }
  }
}
</script>

<style lang="scss" scoped>
.el-menu {
  border-right: none;

  .el-menu-item {
    height: 44px;
    line-height: 44px;
  }
}
</style>
