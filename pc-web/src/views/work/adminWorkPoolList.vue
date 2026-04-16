<template>
  <div class="admin-work-pool-container">
    <el-row :gutter="20">
      <el-col :span="3">
        <select-admin-community @changeCommunity="handleCommunityChange" />
      </el-col>
      <el-col :span="21">
        <el-card class="box-card">
          <div slot="header" class="clearfix flex justify-between">
            <span>{{ $t('adminWorkPool.search.title') }}</span>
          </div>
          <el-row :gutter="20">
            <el-col :span="4">
              <el-input v-model="searchForm.workNameLike" :placeholder="$t('adminWorkPool.search.workName')" clearable />
            </el-col>
            <el-col :span="4">
              <el-input v-model="searchForm.createUserNameLike" :placeholder="$t('adminWorkPool.search.createUser')"
                clearable />
            </el-col>
            <el-col :span="4">
              <el-input v-model="searchForm.staffNameLike" :placeholder="$t('adminWorkPool.search.staffName')"
                clearable />
            </el-col>
            <el-col :span="4">
              <el-date-picker v-model="searchForm.queryStartTime" type="datetime"
                :placeholder="$t('adminWorkPool.search.startTime')" value-format="yyyy-MM-dd HH:mm:ss" />
            </el-col>
            <el-col :span="4">
              <el-date-picker v-model="searchForm.queryEndTime" type="datetime"
                :placeholder="$t('adminWorkPool.search.endTime')" value-format="yyyy-MM-dd HH:mm:ss" />
            </el-col>
            <el-col :span="4">
              <el-button type="primary" @click="handleSearch">
                <i class="el-icon-search"></i>
                {{ $t('common.search') }}
              </el-button>
              <el-button @click="handleReset">
                <i class="el-icon-refresh"></i>
                {{ $t('common.reset') }}
              </el-button>
            </el-col>
          </el-row>
        </el-card>

        <el-card class="box-card">
          <div slot="header" class="clearfix flex justify-between">
            <span>{{ $t('adminWorkPool.list.title') }}</span>
          </div>
          <el-table v-loading="loading" :data="tableData" border style="width: 100%">
            <el-table-column prop="workId" :label="$t('adminWorkPool.table.id')" align="center" />
            <el-table-column prop="communityName" :label="$t('adminWorkPool.table.communityName')" align="center" />
            <el-table-column prop="workName" :label="$t('adminWorkPool.table.workName')" align="center" />
            <el-table-column prop="typeName" :label="$t('adminWorkPool.table.typeName')" align="center" />
            <el-table-column :label="$t('adminWorkPool.table.workCycle')" align="center">
              <template slot-scope="scope">
                {{ scope.row.workCycle === '1001' ? $t('adminWorkPool.workCycle.once') :
                  $t('adminWorkPool.workCycle.periodic') }}
              </template>
            </el-table-column>
            <el-table-column prop="createUserName" :label="$t('adminWorkPool.table.createUser')" align="center" />
            <el-table-column prop="staffName" :label="$t('adminWorkPool.table.staffName')" align="center">
              <template slot-scope="scope">
                {{ scope.row.staffName || '-' }}
              </template>
            </el-table-column>
            <el-table-column :label="$t('adminWorkPool.table.timeRange')" align="center">
              <template slot-scope="scope">
                {{ scope.row.startTime }}<br />~{{ scope.row.endTime }}
              </template>
            </el-table-column>
            <el-table-column :label="$t('adminWorkPool.table.status')" align="center">
              <template slot-scope="scope">
                {{ scope.row.stateName }}
                <span v-if="scope.row.state === 'C' && scope.row.taskTimeout === 'Y'">
                  ({{ $t('adminWorkPool.status.timeout') }})
                </span>
              </template>
            </el-table-column>
            <el-table-column prop="createTime" :label="$t('adminWorkPool.table.createTime')" align="center" />
            <el-table-column prop="finishTime" :label="$t('adminWorkPool.table.finishTime')" align="center">
              <template slot-scope="scope">
                {{ scope.row.finishTime || '-' }}
              </template>
            </el-table-column>
            <el-table-column :label="$t('common.operation')" align="center" width="120">
              <template slot-scope="scope">
                <el-button size="mini" type="primary" @click="handleDetail(scope.row)">
                  {{ $t('common.detail') }}
                </el-button>
              </template>
            </el-table-column>
          </el-table>

          <el-pagination :current-page="pagination.current" :page-sizes="[10, 20, 30, 50]" :page-size="pagination.size"
            :total="pagination.total" layout="total, sizes, prev, pager, next, jumper" @size-change="handleSizeChange"
            @current-change="handlePageChange" />
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import { listAdminWorkTask } from '@/api/work/adminWorkPoolApi'
import SelectAdminCommunity from '@/components/community/selectAdminCommunity'

export default {
  name: 'AdminWorkPoolList',
  components: {
    SelectAdminCommunity
  },
  data() {
    return {
      loading: false,
      searchForm: {
        workNameLike: '',
        createUserNameLike: '',
        staffNameLike: '',
        queryStartTime: '',
        queryEndTime: '',
        communityId: '',
        state: '',
        page: 1,
        row: 10
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
    this.getList()
  },
  methods: {
    async getList() {
      try {
        this.loading = true
        const params = { ...this.searchForm }
        if (params.state === 'timeout') {
          params.state = 'C'
          params.taskTimeout = 'Y'
        }
        const { data, total } = await listAdminWorkTask(params)
        this.tableData = data
        this.pagination.total = total
      } catch (error) {
        this.$message.error(this.$t('adminWorkPool.fetchError'))
      } finally {
        this.loading = false
      }
    },
    handleSearch() {
      this.searchForm.page = 1
      this.pagination.current = 1
      this.getList()
    },
    handleReset() {
      this.searchForm = {
        workNameLike: '',
        createUserNameLike: '',
        staffNameLike: '',
        queryStartTime: '',
        queryEndTime: '',
        communityId: this.searchForm.communityId,
        state: '',
        page: 1,
        row: 10
      }
      this.getList()
    },
    handleCommunityChange(community) {
      this.searchForm.communityId = community.communityId
      this.handleSearch()
    },
    handleDetail(row) {
      this.$router.push(`/views/work/adminWorkDetail?workId=${row.workId}`)
    },
    handleSizeChange(val) {
      this.searchForm.row = val
      this.pagination.size = val
      this.getList()
    },
    handlePageChange(val) {
      this.searchForm.page = val
      this.pagination.current = val
      this.getList()
    }
  }
}
</script>

<style lang="scss" scoped>
.admin-work-pool-container {
  padding: 20px;

  .box-card {
    margin-bottom: 20px;
  }

  .el-pagination {
    margin-top: 20px;
    text-align: right;
  }
}
</style>