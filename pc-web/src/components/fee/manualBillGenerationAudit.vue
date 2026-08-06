<template>
  <div class="app-container">
    <el-card>
      <div slot="header" class="flex justify-between">
        <span>{{ $t('manualBillGeneration.menuAudit') }}</span>
      </div>
      <el-form :model="searchForm" label-width="0">
        <el-row :gutter="20">
          <el-col :span="5">
            <el-form-item>
              <el-date-picker v-model="searchForm.billMonth" type="month" value-format="yyyy-MM"
                :placeholder="$t('manualBillGeneration.billMonth')" clearable style="width:100%"></el-date-picker>
            </el-form-item>
          </el-col>
          <el-col :span="5">
            <el-form-item>
              <el-input v-model.trim="searchForm.applicationName" :placeholder="$t('manualBillGeneration.applicationNamePlaceholder')"
                clearable @keyup.enter.native="handleSearch"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="5">
            <el-form-item>
              <el-date-picker v-model="searchForm.createTimeRange" type="daterange" range-separator="~"
                :start-placeholder="$t('manualBillGeneration.createTimeRange')"
                :end-placeholder="$t('common.chooseDate')" value-format="yyyy-MM-dd"
                style="width:100%"></el-date-picker>
            </el-form-item>
          </el-col>
          <el-col :span="9" style="text-align:right">
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
  name: 'ManualBillGenerationAudit',
  data() {
    return {
      loading: false,
      searchForm: {
        billMonth: '',
        applicationName: '',
        createTimeRange: []
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
      const params = {
        page: this.pagination.page,
        row: this.pagination.row,
        communityId: this.getCommunityId(),
        state: '1001'
      }
      if (this.searchForm.billMonth) {
        params.billMonth = this.searchForm.billMonth
      }
      if (this.searchForm.applicationName) {
        params.applicationName = this.searchForm.applicationName
      }
      if (this.searchForm.createTimeRange && this.searchForm.createTimeRange.length === 2) {
        params.createTimeStart = this.searchForm.createTimeRange[0] + ' 00:00:00'
        params.createTimeEnd = this.searchForm.createTimeRange[1] + ' 23:59:59'
      }
      listManualBillApplications(params).then(res => {
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
      this.searchForm = { billMonth: '', applicationName: '', createTimeRange: [] }
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
