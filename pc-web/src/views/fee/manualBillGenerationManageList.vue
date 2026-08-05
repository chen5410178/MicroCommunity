<template>
  <div class="app-container">
    <el-card>
      <div slot="header" class="flex justify-between">
        <span>{{ $t('manualBillGeneration.manageTitle') }}</span>
        <div>
          <el-button type="primary" size="small" @click="$router.push('/pages/fee/manualBillGeneration')">
            {{ $t('manualBillGeneration.createNew') }}
          </el-button>
        </div>
      </div>
      <el-form :model="searchForm" label-width="0">
        <el-row :gutter="20">
          <el-col :span="4">
            <el-form-item>
              <el-select v-model="searchForm.state" :placeholder="$t('manualBillGeneration.state')" clearable style="width:100%">
                <el-option :label="$t('manualBillGeneration.stateWait')" value="1001"></el-option>
                <el-option :label="$t('manualBillGeneration.statePass')" value="1002"></el-option>
                <el-option :label="$t('manualBillGeneration.stateReject')" value="1003"></el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="4">
            <el-form-item>
              <el-button type="primary" size="small" @click="handleSearch">
                {{ $t('common.search') }}
              </el-button>
              <el-button size="small" @click="handleReset">
                {{ $t('common.reset') }}
              </el-button>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
    </el-card>

    <el-card class="margin-top">
      <el-table v-loading="loading" :data="listData" border>
        <el-table-column prop="applicationName" :label="$t('manualBillGeneration.applicationName')" align="center" min-width="200"></el-table-column>
        <el-table-column prop="feeName" :label="$t('manualBillGeneration.feeName')" align="center" width="120"></el-table-column>
        <el-table-column prop="billMonth" :label="$t('manualBillGeneration.billMonth')" width="100" align="center"></el-table-column>
        <el-table-column :label="$t('manualBillGeneration.state')" width="100" align="center">
          <template slot-scope="scope">
            <el-tag v-if="scope.row.state === '1001'" type="warning" size="small">{{ $t('manualBillGeneration.stateWait') }}</el-tag>
            <el-tag v-else-if="scope.row.state === '1002'" type="success" size="small">{{ $t('manualBillGeneration.statePass') }}</el-tag>
            <el-tag v-else-if="scope.row.state === '1003'" type="danger" size="small">{{ $t('manualBillGeneration.stateReject') }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="totalCount" :label="$t('manualBillGeneration.totalCount')" width="80" align="center"></el-table-column>
        <el-table-column prop="successCount" :label="$t('manualBillGeneration.successCount')" width="80" align="center"></el-table-column>
        <el-table-column prop="createUserName" :label="$t('manualBillGeneration.createUser')" width="100" align="center"></el-table-column>
        <el-table-column prop="createTime" :label="$t('manualBillGeneration.createTime')" width="160" align="center"></el-table-column>
        <el-table-column :label="$t('common.operation')" width="200" align="center">
          <template slot-scope="scope">
            <el-button type="text" @click="handleDetail(scope.row)">
              {{ $t('common.detail') }}
            </el-button>
            <el-button v-if="scope.row.state === '1001'" type="text" style="color:#f56c6c" @click="handleDelete(scope.row)">
              {{ $t('common.delete') }}
            </el-button>
          </template>
        </el-table-column>
      </el-table>
      <el-pagination
        :current-page="pagination.page"
        :page-sizes="[10, 20, 30, 50]"
        :page-size="pagination.row"
        :total="pagination.total"
        layout="total, prev, pager, next, sizes"
        @size-change="handleSizeChange"
        @current-change="handlePageChange"
      />
    </el-card>
  </div>
</template>

<script>
import { listManualBillApplications, deleteManualBillApplication } from '@/api/fee/manualBillGenerationApi'

export default {
  name: 'ManualBillGenerationManageList',
  data() {
    return {
      loading: false,
      searchForm: {
        state: ''
      },
      listData: [],
      pagination: { page: 1, row: 10, total: 0 }
    }
  },
  mounted() {
    this.loadData()
  },
  methods: {
    loadData() {
      this.loading = true
      listManualBillApplications({
        page: this.pagination.page,
        row: this.pagination.row,
        communityId: this.getCommunityId(),
        state: this.searchForm.state
      }).then(res => {
        if (res.code === 0) {
          this.listData = res.data || []
          this.pagination.total = res.total || 0
        }
        this.loading = false
      }).catch(() => { this.loading = false })
    },
    handleSearch() {
      this.pagination.page = 1
      this.loadData()
    },
    handleReset() {
      this.searchForm = { state: '' }
      this.handleSearch()
    },
    handleDetail(row) {
      this.$router.push({
        path: '/views/fee/manualBillGenerationDetail',
        query: { applicationId: row.applicationId }
      })
    },
    handleDelete(row) {
      this.$confirm(this.$t('manualBillGeneration.deleteConfirm'), this.$t('common.tip'), {
        confirmButtonText: this.$t('common.confirm'),
        cancelButtonText: this.$t('common.cancel'),
        type: 'warning'
      }).then(() => {
        deleteManualBillApplication({
          applicationId: row.applicationId,
          communityId: this.getCommunityId()
        }).then(res => {
          if (res.code === 0) {
            this.$message.success(this.$t('common.deleteSuccess'))
            this.loadData()
          } else {
            this.$message.error(res.msg || this.$t('common.deleteFail'))
          }
        })
      }).catch(() => {})
    },
    handleSizeChange(val) {
      this.pagination.row = val
      this.loadData()
    },
    handlePageChange(val) {
      this.pagination.page = val
      this.loadData()
    }
  }
}
</script>
