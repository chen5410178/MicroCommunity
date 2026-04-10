<template>
  <div class="staff-fee-manage-container animated fadeInRight">
    <el-row :gutter="20" class="mb-20">
      <el-col :span="24">
        <el-card>
          <div slot="header" class="flex justify-between">
            <span>{{ $t('staffFeeManage.search.title') }}</span>
          </div>
          <el-form :inline="true" :model="searchForm" class="demo-form-inline text-left" >
            <el-form-item >
              <el-input v-model="searchForm.userCode" :placeholder="$t('staffFeeManage.search.userCodePlaceholder')"
                clearable />
            </el-form-item>
            <el-form-item >
              <el-date-picker v-model="searchForm.startTime" type="datetime"
                :placeholder="$t('staffFeeManage.search.startTimePlaceholder')" value-format="yyyy-MM-dd HH:mm:ss" />
            </el-form-item>
            <el-form-item >
              <el-date-picker v-model="searchForm.endTime" type="datetime"
                :placeholder="$t('staffFeeManage.search.endTimePlaceholder')" value-format="yyyy-MM-dd HH:mm:ss" />
            </el-form-item>
            <el-form-item>
              <el-button type="primary" @click="handleSearch">
                {{ $t('common.search') }}
              </el-button>
              <el-button @click="handleReset">
                {{ $t('common.reset') }}
              </el-button>
            </el-form-item>
          </el-form>
        </el-card>
      </el-col>
    </el-row>

    <el-row :gutter="20">
      <el-col :span="24">
        <el-card>
          <div slot="header" class="flex justify-between">
            <span>{{ $t('staffFeeManage.list.title') }}</span>
            <el-button type="primary" size="small" class="float-right" @click="handleExport">
              {{ $t('common.export') }}
            </el-button>
          </div>
          <el-table v-loading="loading" :data="tableData" border style="width: 100%">
            <el-table-column prop="userId" :label="$t('staffFeeManage.table.userCode')" align="center" />
            <el-table-column prop="userName" :label="$t('staffFeeManage.table.userName')" align="center" />
            <el-table-column prop="receivableAmount" :label="$t('staffFeeManage.table.receivableAmount')"
              align="center">
              <template slot-scope="scope">
                {{ scope.row.receivableAmount }} {{ $t('staffFeeManage.table.yuan') }}
              </template>
            </el-table-column>
            <el-table-column prop="receivedAmount" :label="$t('staffFeeManage.table.receivedAmount')" align="center">
              <template slot-scope="scope">
                {{ scope.row.receivedAmount }} {{ $t('staffFeeManage.table.yuan') }}
              </template>
            </el-table-column>
          </el-table>
          <el-pagination :current-page="pagination.current" :page-sizes="[10, 20, 30, 50]" :page-size="pagination.size"
            layout="total, sizes, prev, pager, next, jumper" :total="pagination.total" @size-change="handleSizeChange"
            @current-change="handleCurrentChange" />
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import { getStaffFeeList } from '@/api/fee/staffFeeManageApi'
import { getCommunityId } from '@/api/community/communityApi'

export default {
  name: 'StaffFeeManageList',
  data() {
    return {
      loading: false,
      searchForm: {
        communityId: '',
        userCode: '',
        startTime: '',
        endTime: ''
      },
      tableData: [],
      pagination: {
        current: 1,
        size: 10,
        total: 0
      }
    }
  },
  created() {
    this.searchForm.communityId = getCommunityId()
    this.getList()
  },
  methods: {
    async getList() {
      try {
        this.loading = true
        const params = {
          page: this.pagination.current,
          row: this.pagination.size,
          ...this.searchForm
        }
        const { data, total } = await getStaffFeeList(params)
        this.tableData = data
        this.pagination.total = total
      } catch (error) {
        this.$message.error(this.$t('staffFeeManage.fetchError'))
      } finally {
        this.loading = false
      }
    },
    handleSearch() {
      this.pagination.current = 1
      this.getList()
    },
    handleReset() {
      this.searchForm = {
        communityId: getCommunityId(),
        userCode: '',
        startTime: '',
        endTime: ''
      }
      this.pagination.current = 1
      this.getList()
    },
    handleExport() {
      // 导出逻辑
    },
    handleSizeChange(val) {
      this.pagination.size = val
      this.getList()
    },
    handleCurrentChange(val) {
      this.pagination.current = val
      this.getList()
    }
  }
}
</script>

<style lang="scss" scoped>
.staff-fee-manage-container {
  padding: 20px;

  .mb-20 {
    margin-bottom: 20px;
  }

  .float-right {
    float: right;
  }
}
</style>