<template>
  <div class="new-oa-workflow-manage-container">
    <el-card class="box-card">
      <div v-if="newOaWorkflowManageInfo.newOaWorkflows && newOaWorkflowManageInfo.newOaWorkflows.length > 0">
        <el-row :gutter="20">
          <el-col v-for="(item, index) in newOaWorkflowManageInfo.newOaWorkflows" :key="index" :span="4"
            class="text-center workflow-item" @click.native="newFlow(item)">
            <div>
              <img src="/img/flow.png" width="80px" />
            </div>
            <div class="margin-top">
              <span style="color: #333333;">{{ item.flowName }}</span>
            </div>
          </el-col>
        </el-row>
      </div>
      <div v-else class="empty-tip">
        <span>{{ $t('newOaWorkflowManage.noPublishedFlow') }}</span>
      </div>
    </el-card>
  </div>
</template>

<script>
import { queryOaWorkflow } from '@/api/oa/newOaWorkflowManageApi'

export default {
  name: 'NewOaWorkflowManageList',
  data() {
    return {
      newOaWorkflowManageInfo: {
        newOaWorkflows: [],
        conditions: {
          state: 'C',
          flowType: '1001',
          page: 1,
          row: 100
        }
      }
    }
  },
  created() {
    this._listNewOaWorkflows()
  },
  methods: {
    async _listNewOaWorkflows() {
      try {
        const { data } = await queryOaWorkflow(this.newOaWorkflowManageInfo.conditions)
        this.newOaWorkflowManageInfo.newOaWorkflows = data
      } catch (error) {
        console.error('Failed to fetch workflows:', error)
      }
    },
    async newFlow(flow) {
      try {
        const { data } = await queryOaWorkflow({
          page: 1,
          row: 1,
          flowId: flow.flowId,
          state: 'C'
        })

        if (data.length < 1) {
          this.$message.error(this.$t('newOaWorkflowManage.flowNotDeployed'))
          return
        }

        this.$router.push({
          path: '/views/oa/newOaWorkflow',
          query: { flowId: flow.flowId }
        })
      } catch (error) {
        console.error('Failed to check flow deployment:', error)
      }
    }
  }
}
</script>

<style lang="scss" scoped>
.new-oa-workflow-manage-container {
  padding: 20px;

  .box-card {
    min-height: 400px;
  }

  .workflow-item {
    margin-bottom: 20px;
    cursor: pointer;
    transition: all 0.3s;

    &:hover {
      transform: translateY(-5px);
    }
  }

  .empty-tip {
    padding: 20px;
    text-align: center;
    color: #909399;
  }

  .margin-top {
    margin-top: 10px;
  }
}
</style>