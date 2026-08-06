<template>
  <div class="app-container padding">
    <el-row :gutter="20">
      <!-- 左侧分类导航 -->
      <el-col :span="3">
        <el-card class="role-card">
          <ul class="role-list">
            <li v-for="(item, index) in menus" :key="index" class="role-item"
              :class="{ 'role-item-active': activeMenu === item.value }" @click="handleMenuSelect(item.value)">
              {{ item.label }}
            </li>
          </ul>
        </el-card>
      </el-col>
      <!-- 右侧内容区 -->
      <el-col :span="21">
        <keep-alive>
          <component :is="currentComponent" :key="activeMenu" :ref="activeMenu"
            @switchTab="handleSwitchTab" />
        </keep-alive>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import ManualBillGenerationApply from '@/components/fee/manualBillGenerationApply.vue'
import ManualBillGenerationAudit from '@/components/fee/manualBillGenerationAudit.vue'
import ManualBillGenerationDone from '@/components/fee/manualBillGenerationDone.vue'

export default {
  name: 'ManualBillGenerationMain',
  components: {
    ManualBillGenerationApply,
    ManualBillGenerationAudit,
    ManualBillGenerationDone
  },
  data() {
    return {
      activeMenu: 'apply',
      menus: [
        { value: 'apply', label: this.$t('manualBillGeneration.menuApply') },
        { value: 'audit', label: this.$t('manualBillGeneration.menuAudit') },
        { value: 'done', label: this.$t('manualBillGeneration.menuDone') }
      ]
    }
  },
  computed: {
    currentComponent() {
      if (this.activeMenu === 'apply') {
        return ManualBillGenerationApply
      }
      if (this.activeMenu === 'done') {
        return ManualBillGenerationDone
      }
      return ManualBillGenerationAudit
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
.role-card {
  height: 100%;
}

.role-list {
  list-style: none;
  padding: 0;
  margin: 0;
}

.role-item {
  padding: 10px;
  text-align: center;
  cursor: pointer;
  border-radius: 4px;
  margin-bottom: 5px;
  transition: all 0.3s;
}

.role-item:hover {
  background-color: #f5f7fa;
}

.role-item-active {
  background-color: #409eff;
  color: white;
}
</style>
