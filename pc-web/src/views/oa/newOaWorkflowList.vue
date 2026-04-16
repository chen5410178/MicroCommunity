<template>
  <div class="new-oa-workflow-container">
    <el-row :gutter="20">
      <el-col :span="4">
        <div class="menu-wrapper bg-white">
          <ul class="menu-list">
            <li v-if="hasPrivilege('502023041839740294')" :class="{ 'active': activeTab === 'newOaWorkflowPool' }"
              @click="switchTab('newOaWorkflowPool')">
              {{ $t('newOaWorkflow.processWorkOrder') }}
            </li>
            <li :class="{ 'active': activeTab === 'newOaWorkflowForm' }" @click="switchTab('newOaWorkflowForm')">
              {{ $t('newOaWorkflow.draftProcess') }}
            </li>
            <li :class="{ 'active': activeTab === 'newOaWorkflowUndo' }" @click="switchTab('newOaWorkflowUndo')">
              {{ $t('newOaWorkflow.processTodo') }}
            </li>
            <li :class="{ 'active': activeTab === 'newOaWorkflowFinish' }" @click="switchTab('newOaWorkflowFinish')">
              {{ $t('newOaWorkflow.processDone') }}
            </li>
          </ul>
        </div>
      </el-col>
      <el-col :span="20">
        <div class="content-wrapper">
          <new-oa-workflow-pool v-show="activeTab === 'newOaWorkflowPool'" ref="newOaWorkflowPool" />
          <new-oa-workflow-form v-if="activeTab === 'newOaWorkflowForm'" ref="newOaWorkflowForm"  @success="handleFormSuccess"/>
          <new-oa-workflow-undo v-show="activeTab === 'newOaWorkflowUndo'" ref="newOaWorkflowUndo" />
          <new-oa-workflow-finish v-show="activeTab === 'newOaWorkflowFinish'" ref="newOaWorkflowFinish" />
        </div>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import NewOaWorkflowPool from '@/components/oa/newOaWorkflowPool'
import NewOaWorkflowForm from '@/components/oa/newOaWorkflowForm'
import NewOaWorkflowUndo from '@/components/oa/newOaWorkflowUndo'
import NewOaWorkflowFinish from '@/components/oa/newOaWorkflowFinish'

export default {
  name: 'NewOaWorkflowList',
  components: {
    NewOaWorkflowPool,
    NewOaWorkflowForm,
    NewOaWorkflowUndo,
    NewOaWorkflowFinish
  },
  data() {
    return {
      activeTab: '',
      flowId: this.$route.query.flowId || ''
    }
  },
  created() {
    this.initActiveTab()
  },
  methods: {
    initActiveTab() {
      const switchValue = this.$route.query.switchValue
      if (switchValue) {
        this.switchTab(switchValue)
        return
      }
      if (this.hasPrivilege('502023041839740294')) {
        this.switchTab('newOaWorkflowPool')
      } else {
        this.switchTab('newOaWorkflowForm')
      }
    },
    switchTab(tab) {
      console.log('父组件 switchTab 被调用，参数 tab:', tab)
      if (this.activeTab === tab) return
      this.activeTab = tab
      setTimeout(() => {
        this.$refs[tab].open({ flowId: this.flowId })
      }, 500)
    },
    handleFormSuccess(targetTab) {
      console.log('父组件收到 success 事件，目标 tab:', targetTab)
      this.switchTab(targetTab)
    }
  }
}
</script>

<style lang="scss" scoped>
.new-oa-workflow-container {
  padding: 20px;
  height: 100%;

  .menu-wrapper {
    border-radius: 4px;
    padding: 10px 0;
    height: 100%;

    .menu-list {
      list-style: none;
      padding: 0;
      margin: 0;

      li {
        padding: 12px 20px;
        cursor: pointer;
        transition: all 0.3s;
        border-left: 3px solid transparent;

        &:hover {
          background-color: #f5f7fa;
        }

        &.active {
          background-color: #ecf5ff;
          border-left: 3px solid #409eff;
          color: #409eff;
        }
      }
    }
  }

  .content-wrapper {

    height: 100%;
  }
}
</style>