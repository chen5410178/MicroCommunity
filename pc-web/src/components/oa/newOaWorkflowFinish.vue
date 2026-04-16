<template>
  <div class="new-oa-workflow-finish">
    <el-card class="search-card">
      <div slot="header" class="flex justify-between">
        <span>{{ $t('newOaWorkflowFinish.searchTitle') }}</span>
      </div>
      <el-form :inline="true" :model="conditions" class="search-form text-left">
        <el-form-item>
          <el-input v-model="conditions.createUserName" :placeholder="$t('newOaWorkflowFinish.enterApplicant')"
            clearable />
        </el-form-item>
        <el-form-item>
          <el-date-picker v-model="conditions.startTime" type="datetime"
            :placeholder="$t('newOaWorkflowFinish.enterStartTime')" value-format="yyyy-MM-dd HH:mm:ss" />
        </el-form-item>
        <el-form-item>
          <el-date-picker v-model="conditions.endTime" type="datetime"
            :placeholder="$t('newOaWorkflowFinish.enterEndTime')" value-format="yyyy-MM-dd HH:mm:ss" />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="queryOaWorkflowFinish">
            <i class="el-icon-search"></i>
            {{ $t('common.search') }}
          </el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <el-card class="table-card">
      <div slot="header" class="flex justify-between">
        <span>{{ $t('newOaWorkflowFinish.doneProcess') }}</span>
      </div>
      <el-table :data="finishs" border style="width: 100%" v-loading="loading">
        <el-table-column prop="create_user_name" :label="$t('newOaWorkflowFinish.applicant')" align="center" />
        <template v-for="(item, index) in formJson">
          <el-table-column v-if="item.type === 'text' || item.type === 'textarea'" :key="index" :label="item.label"
            align="center">
            <template slot-scope="scope">
              {{ scope.row[item.key] }}
            </template>
          </el-table-column>
        </template>
        <el-table-column :label="$t('newOaWorkflowFinish.status')" align="center">
          <template slot-scope="scope">
            {{ getNewOaWorkflowFinishState(scope.row) }}
          </template>
        </el-table-column>
        <el-table-column prop="create_time" :label="$t('newOaWorkflowFinish.createTime')" align="center" />
        <el-table-column :label="$t('common.operation')" align="center" width="150">
          <template slot-scope="scope">
            <el-button size="mini" @click="openNewOaWorkflowFinishDetail(scope.row)">
              {{ $t('common.detail') }}
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
import { queryOaWorkflowForm, queryOaWorkflowUserHisTaskFormData } from '@/api/oa/newOaWorkflowApi'

export default {
  name: 'NewOaWorkflowFinish',
  data() {
    return {
      loading: false,
      finishs: [],
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
        flowId: this.flowId
      }
    }
  },
  created() {
  },
  methods: {
    open(params) {
      this.conditions.flowId = params.flowId
      this.listOaWorkFlowFinishForm()
      this.listOaWorkflowFinishs()
    },
    async listOaWorkFlowFinishForm() {
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
    async listOaWorkflowFinishs() {
      this.loading = true
      try {
        const params = {
          ...this.conditions,
          page: this.page.current,
          row: this.page.size
        }
        const { data, total } = await queryOaWorkflowUserHisTaskFormData(params)
        this.finishs = data
        this.total = total
      } catch (error) {
        console.error('获取已办流程数据失败:', error)
      } finally {
        this.loading = false
      }
    },
    queryOaWorkflowFinish() {
      this.page.current = 1
      this.listOaWorkflowFinishs()
    },
    handleSizeChange(val) {
      this.page.size = val
      this.listOaWorkflowFinishs()
    },
    handleCurrentChange(val) {
      this.page.current = val
      this.listOaWorkflowFinishs()
    },
    openNewOaWorkflowFinishDetail(finish) {
      this.$router.push({
        path: '/views/oa/newOaWorkflowDetail',
        query: {
          id: finish.id,
          flowId: this.conditions.flowId
        }
      })
    },
    getNewOaWorkflowFinishState(finish) {
      if (!Object.prototype.hasOwnProperty.call(finish, 'state')) return this.$t('common.unknown')

      const stateMap = {
        '1001': this.$t('newOaWorkflowFinish.stateApply'),
        '1002': this.$t('newOaWorkflowFinish.statePending'),
        '1003': this.$t('newOaWorkflowFinish.stateReturned'),
        '1004': this.$t('newOaWorkflowFinish.stateDelegate'),
        '1005': this.$t('newOaWorkflowFinish.stateCompleted')
      }

      return stateMap[finish.state] || this.$t('common.unknown')
    }
  }
}
</script>

<style lang="scss" scoped>
.new-oa-workflow-finish {
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