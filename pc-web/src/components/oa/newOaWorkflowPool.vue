<template>
  <div class="new-oa-workflow-pool">
    <el-card class="search-card">
      <div slot="header" class="flex justify-between">
        <span>{{ $t('newOaWorkflowPool.searchTitle') }}</span>
      </div>
      <el-form :inline="true" :model="conditions" class="search-form text-left">
        <el-form-item>
          <el-input v-model="conditions.createUserName" :placeholder="$t('newOaWorkflowPool.enterApplicant')"
            clearable />
        </el-form-item>
        <el-form-item>
          <el-date-picker v-model="conditions.startTime" type="datetime"
            :placeholder="$t('newOaWorkflowPool.enterStartTime')" value-format="yyyy-MM-dd HH:mm:ss" />
        </el-form-item>
        <el-form-item>
          <el-date-picker v-model="conditions.endTime" type="datetime"
            :placeholder="$t('newOaWorkflowPool.enterEndTime')" value-format="yyyy-MM-dd HH:mm:ss" />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="queryOaWorkflowPool">
            <i class="el-icon-search"></i>
            {{ $t('common.search') }}
          </el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <el-card class="table-card">
      <div slot="header" class="flex justify-between">
        <span>{{ $t('newOaWorkflowPool.processWorkOrder') }}</span>
      </div>
      <el-table :data="pools" border style="width: 100%" v-loading="loading">
        <el-table-column prop="create_user_name" :label="$t('newOaWorkflowPool.applicant')" align="center" />
        <template v-for="(item, index) in formJson">
          <el-table-column 
            v-if="item.type !== 'text' && item.type !== 'button' && item.type !== 'textarea'" :key="index"
            :label="item.label" align="center">
            <template slot-scope="scope">
              {{ scope.row[item.key] }}
            </template>
          </el-table-column>
        </template>
        <el-table-column :label="$t('newOaWorkflowPool.status')" align="center">
          <template slot-scope="scope">
            {{ getNewOaWorkflowPoolState(scope.row) }}
          </template>
        </el-table-column>
        <el-table-column prop="create_time" :label="$t('newOaWorkflowPool.createTime')" align="center" />
        <el-table-column :label="$t('common.operation')" align="center" width="200">
          <template slot-scope="scope">
            <el-button size="mini" @click="openNewOaWorkflowPoolDetail(scope.row)">
              {{ $t('common.detail') }}
            </el-button>
            <el-button size="mini" @click="openNewOaWorkflowPoolImg(scope.row)">
              {{ $t('newOaWorkflowPool.flowChart') }}
            </el-button>
          </template>
        </el-table-column>
      </el-table>

      <el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange" :current-page="page.current"
        :page-sizes="[10, 20, 30, 50]" :page-size="page.size" layout="total, sizes, prev, pager, next, jumper"
        :total="total" class="pagination" />
    </el-card>

    <view-image ref="viewImage" />
  </div>
</template>

<script>
import { queryOaWorkflowForm, queryOaWorkflowFormData } from '@/api/oa/newOaWorkflowApi'
import ViewImage from '@/components/system/viewImage'
import { getCommunityId } from '@/api/community/communityApi'

export default {
  name: 'NewOaWorkflowPool',
  components: { ViewImage },

  data() {
    return {
      loading: false,
      pools: [],
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
      this.listOaWorkFlowPoolForm()
      this.listOaWorkflowPools()
    },
    async listOaWorkFlowPoolForm() {
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
    async listOaWorkflowPools() {
      this.loading = true
      try {
        const params = {
          ...this.conditions,
          page: this.page.current,
          row: this.page.size
        }
        const { data, total } = await queryOaWorkflowFormData(params)
        this.pools = data
        this.total = total
      } catch (error) {
        console.error('获取工单池数据失败:', error)
      } finally {
        this.loading = false
      }
    },
    queryOaWorkflowPool() {
      this.page.current = 1
      this.listOaWorkflowPools()
    },
    handleSizeChange(val) {
      this.page.size = val
      this.listOaWorkflowPools()
    },
    handleCurrentChange(val) {
      this.page.current = val
      this.listOaWorkflowPools()
    },
    openNewOaWorkflowPoolDetail(pool) {
      this.$router.push({
        path: '/views/oa/newOaWorkflowDetail',
        query: {
          id: pool.id,
          flowId: this.conditions.flowId
        }
      })
    },
    async openNewOaWorkflowPoolImg(pool) {
      try {
        const res = await this.$api.workflow.listRunWorkflowImage({
          communityId: getCommunityId(),
          businessKey: pool.id
        })
        if (res.code !== '0') {
          this.$message.error(res.msg)
          return
        }
        this.$refs.viewImage.open(`data:image/png;base64,${res.data}`)
      } catch (error) {
        console.error('获取流程图失败:', error)
      }
    },
    getNewOaWorkflowPoolState(pool) {
      if (!Object.prototype.hasOwnProperty.call(pool, 'state')) return this.$t('common.unknown')

      const stateMap = {
        '1001': this.$t('newOaWorkflowPool.stateApply'),
        '1002': this.$t('newOaWorkflowPool.statePending'),
        '1003': this.$t('newOaWorkflowPool.stateReturned'),
        '1004': this.$t('newOaWorkflowPool.stateDelegate'),
        '1005': this.$t('newOaWorkflowPool.stateCompleted')
      }

      return stateMap[pool.state] || this.$t('common.unknown')
    }
  }
}
</script>

<style lang="scss" scoped>
.new-oa-workflow-pool {
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