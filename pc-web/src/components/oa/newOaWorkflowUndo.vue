<template>
  <div class="new-oa-workflow-undo">
    <el-card class="search-card">
      <div slot="header" class="flex justify-between">
        <span>{{ $t('newOaWorkflowUndo.searchTitle') }}</span>
      </div>
      <el-form :inline="true" :model="conditions" class="search-form text-left">
        <el-form-item>
          <el-input v-model="conditions.createUserName" :placeholder="$t('newOaWorkflowUndo.enterApplicant')"
            clearable />
        </el-form-item>
        <el-form-item>
          <el-date-picker v-model="conditions.startTime" type="datetime"
            :placeholder="$t('newOaWorkflowUndo.enterStartTime')" value-format="yyyy-MM-dd HH:mm:ss" />
        </el-form-item>
        <el-form-item>
          <el-date-picker v-model="conditions.endTime" type="datetime"
            :placeholder="$t('newOaWorkflowUndo.enterEndTime')" value-format="yyyy-MM-dd HH:mm:ss" />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="queryOaWorkflowUndo">
            <i class="el-icon-search"></i>
            {{ $t('common.search') }}
          </el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <el-card class="table-card">
      <div slot="header" class="flex justify-between">
        <span>{{ $t('newOaWorkflowUndo.todoProcess') }}</span>
      </div>
      <el-table :data="undos" border style="width: 100%" v-loading="loading">
        <el-table-column prop="create_user_name" :label="$t('newOaWorkflowUndo.applicant')" align="center" />
        <template v-for="(item, index) in formJson">
          <el-table-column v-if="item.type === 'text' || item.type === 'textarea'" :key="index" :label="item.label"
            align="center">
            <template slot-scope="scope">
              {{ scope.row[item.key] }}
            </template>
          </el-table-column>
        </template>
        <el-table-column :label="$t('newOaWorkflowUndo.status')" align="center">
          <template slot-scope="scope">
            {{ getNewOaWorkflowUndoState(scope.row) }}
          </template>
        </el-table-column>
        <el-table-column prop="create_time" :label="$t('newOaWorkflowUndo.createTime')" align="center" />
        <el-table-column :label="$t('common.operation')" align="center" width="300">
          <template slot-scope="scope">
            <el-button size="mini" type="primary" @click="openAuditNewOaWorkflowUndoDetail(scope.row)">
              {{ $t('newOaWorkflowUndo.process') }}
            </el-button>
            <el-button size="mini" @click="openNewOaWorkflowUndoDetail(scope.row)">
              {{ $t('common.detail') }}
            </el-button>
            <el-button size="mini" type="warning" v-if="isMe(scope.row)" @click="openEditNewOaWorkflow(scope.row)">
              {{ $t('common.edit') }}
            </el-button>
          </template>
        </el-table-column>
      </el-table>

      <el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange" :current-page="page.current"
        :page-sizes="[10, 20, 30, 50]" :page-size="page.size" layout="total, sizes, prev, pager, next, jumper"
        :total="total" class="pagination" />
    </el-card>
  </div>
</template>

<script>
import { queryOaWorkflowForm, queryOaWorkflowUserTaskFormData } from '@/api/oa/newOaWorkflowApi'
// import { getCommunityId } from '@/api/community/communityApi'
import { getUserId } from '@/api/user/userApi'

export default {
  name: 'NewOaWorkflowUndo',

  data() {
    return {
      loading: false,
      undos: [],
      formJson: [],
      total: 0,
      page: {
        current: 1,
        size: 10
      },
      conditions: {
        createUserName: '',
        startTime: '',
        endTime: '',
        flowId: this.flowId,
        userId: getUserId()
      }
    }
  },
  created() {
  },
  methods: {
    open(params) {
      this.conditions.flowId = params.flowId
      this.listOaWorkFlowUndoForm()
      this.listOaWorkflowUndos()
    },
    async listOaWorkFlowUndoForm() {
      try {
        const res = await queryOaWorkflowForm({
          page: 1,
          row: 1,
          flowId: this.conditions.flowId
        })
        this.formJson = JSON.parse(res.data[0].formJson).components
      } catch (error) {
        console.error('获取表单配置失败:', error)
      }
    },
    async listOaWorkflowUndos() {
      this.loading = true
      try {
        const params = {
          ...this.conditions,
          page: this.page.current,
          row: this.page.size
        }
        const { data, total } = await queryOaWorkflowUserTaskFormData(params)
        this.undos = data
        this.total = total
      } catch (error) {
        console.error('获取待办流程数据失败:', error)
      } finally {
        this.loading = false
      }
    },
    queryOaWorkflowUndo() {
      this.page.current = 1
      this.listOaWorkflowUndos()
    },
    handleSizeChange(val) {
      this.page.size = val
      this.listOaWorkflowUndos()
    },
    handleCurrentChange(val) {
      this.page.current = val
      this.listOaWorkflowUndos()
    },
    openNewOaWorkflowUndoDetail(undo) {
      this.$router.push({
        path: '/views/oa/newOaWorkflowDetail',
        query: {
          id: undo.id,
          flowId: this.conditions.flowId
        }
      })
    },
    openAuditNewOaWorkflowUndoDetail(undo) {
      this.$router.push({
        path: '/views/oa/newOaWorkflowDetail',
        query: {
          id: undo.id,
          flowId: this.conditions.flowId,
          action: 'Audit',
          taskId: undo.taskId
        }
      })
    },
    openEditNewOaWorkflow(undo) {
      this.$router.push({
        path: '/views/oa/newOaWorkflowFormEdit',
        query: {
          id: undo.id,
          flowId: this.conditions.flowId
        }
      })
    },
    getNewOaWorkflowUndoState(undo) {
      if (!Object.prototype.hasOwnProperty.call(undo, 'state')) return this.$t('common.unknown')

      const stateMap = {
        '1001': this.$t('newOaWorkflowUndo.stateApply'),
        '1002': this.$t('newOaWorkflowUndo.statePending'),
        '1003': this.$t('newOaWorkflowUndo.stateReturned'),
        '1004': this.$t('newOaWorkflowUndo.stateDelegate'),
        '1005': this.$t('newOaWorkflowUndo.stateCompleted')
      }

      return stateMap[undo.state] || this.$t('common.unknown')
    },
    isMe(undo) {
      return undo.create_user_id === getUserId()
    }
  }
}
</script>

<style lang="scss" scoped>
.new-oa-workflow-undo {
  .search-card {
    margin-bottom: 20px;

    .search-form {
      .el-form-item {
        margin-bottom: 0;
      }
    }
  }

  .pagination {
    margin-top: 20px;
    text-align: right;
  }
}
</style>