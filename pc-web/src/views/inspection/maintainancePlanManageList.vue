<template>
  <div class="maintainance-plan-manage-container animated fadeInRight">
    <el-card class="search-card">
      <div slot="header" class="flex justify-between">
        <span>{{ $t('maintainancePlanManage.search.title') }}</span>
      </div>
      <el-row :gutter="20">
        <el-col :span="6">
          <el-input v-model="searchForm.maintainancePlanName" :placeholder="$t('maintainancePlanManage.search.planName')"
            clearable />
        </el-col>
        <el-col :span="6">
          <el-select v-model="searchForm.state" :placeholder="$t('maintainancePlanManage.search.state')"
            style="width:100%">
            <el-option :label="$t('maintainancePlanManage.search.allState')" value="" />
            <el-option v-for="item in states" :key="item.statusCd" :label="item.name" :value="item.statusCd" />
          </el-select>
        </el-col>
        <el-col :span="6">
          <el-button type="primary" @click="handleSearch">
            {{ $t('common.search') }}
          </el-button>
        </el-col>
      </el-row>
    </el-card>

    <el-card class="table-card">
      <div slot="header" class="flex justify-between">
        <span>{{ $t('maintainancePlanManage.list.title') }}</span>
        <el-button type="primary" size="small" style="float:right" @click="handleAdd">
          {{ $t('common.add') }}
        </el-button>
      </div>

      <el-table v-loading="loading" :data="tableData" border style="width:100%">
        <el-table-column prop="planName" :label="$t('maintainancePlanManage.table.planName')" align="center" />
        <el-table-column prop="standardName" :label="$t('maintainancePlanManage.table.standardName')" align="center" />
        <el-table-column prop="planPeriodName" :label="$t('maintainancePlanManage.table.planPeriod')" align="center" />
        <el-table-column :label="$t('maintainancePlanManage.table.dateRange')" align="center">
          <template slot-scope="scope">
            {{ scope.row.startDate }}~{{ scope.row.endDate }}
          </template>
        </el-table-column>
        <el-table-column :label="$t('maintainancePlanManage.table.machineCount')" align="center">
          <template slot-scope="scope">
            {{ scope.row.machineCount }}
            (<el-link type="primary" @click="handleViewMachine(scope.row)">
              {{ $t('maintainancePlanManage.table.machine') }}
            </el-link>)
          </template>
        </el-table-column>
        <el-table-column prop="staffCount" :label="$t('maintainancePlanManage.table.staffCount')" align="center" />
        <el-table-column prop="createUserName" :label="$t('maintainancePlanManage.table.createUser')" align="center" />
        <el-table-column prop="createTime" :label="$t('maintainancePlanManage.table.createTime')" align="center" />
        <el-table-column prop="stateName" :label="$t('maintainancePlanManage.table.state')" align="center" />
        <el-table-column :label="$t('common.operation')" align="center" width="300">
          <template slot-scope="scope">
            <el-button size="mini" type="primary" @click="handleEdit(scope.row)">
              {{ $t('common.edit') }}
            </el-button>
            <el-button size="mini" type="danger" @click="handleDelete(scope.row)">
              {{ $t('common.delete') }}
            </el-button>
            <el-button v-if="scope.row.state === '2020025'" size="mini" type="warning" @click="handleDisable(scope.row)">
              {{ $t('maintainancePlanManage.button.disable') }}
            </el-button>
            <el-button v-else size="mini" type="success" @click="handleEnable(scope.row)">
              {{ $t('maintainancePlanManage.button.enable') }}
            </el-button>
          </template>
        </el-table-column>
      </el-table>

      <div class="pagination-wrapper">
        <div class="tip">
          {{ $t('maintainancePlanManage.tip') }}
        </div>
        <el-pagination :current-page="pagination.current" :page-sizes="[10, 20, 30, 50]" :page-size="pagination.size"
          :total="pagination.total" layout="total, sizes, prev, pager, next, jumper" @size-change="handleSizeChange"
          @current-change="handleCurrentChange" />
      </div>
    </el-card>

    <delete-maintainance-plan ref="deleteDialog" @success="handleSuccess" />
    <maintainance-plan-state ref="stateDialog" @success="handleSuccess" />
  </div>
</template>

<script>
import { listMaintainancePlan } from '@/api/inspection/maintainancePlanManageApi'
import { getDict } from '@/api/community/communityApi'
import { getCommunityId } from '@/api/community/communityApi'
import DeleteMaintainancePlan from '@/components/inspection/deleteMaintainancePlan'
import MaintainancePlanState from '@/components/inspection/maintainancePlanState'

export default {
  name: 'MaintainancePlanManageList',
  components: {
    DeleteMaintainancePlan,
    MaintainancePlanState
  },
  data() {
    return {
      loading: false,
      searchForm: {
        maintainancePlanName: '',
        state: ''
      },
      tableData: [],
      states: [],
      pagination: {
        current: 1,
        size: 10,
        total: 0
      },
      communityId: ''
    }
  },
  created() {
    this.communityId = getCommunityId()
    this.getList()
    this.getDictData()
  },
  methods: {
    async getList() {
      try {
        this.loading = true
        const params = {
          page: this.pagination.current,
          row: this.pagination.size,
          maintainancePlanName: this.searchForm.maintainancePlanName,
          state: this.searchForm.state,
          communityId: this.communityId
        }
        const { data, total } = await listMaintainancePlan(params)
        this.tableData = data
        this.pagination.total = total
      } catch (error) {
        this.$message.error(this.$t('maintainancePlanManage.fetchError'))
      } finally {
        this.loading = false
      }
    },
    async getDictData() {
      try {
        const data = await getDict('maintainance_plan', 'state')
        this.states = data
      } catch (error) {
        console.error('获取字典数据失败:', error)
      }
    },
    handleSearch() {
      this.pagination.current = 1
      this.getList()
    },
    handleAdd() {
      this.$router.push('/views/inspection/addMaintainancePlan')
    },
    handleEdit(row) {
      this.$router.push(`/views/inspection/editMaintainancePlan?planId=${row.planId}`)
    },
    handleDelete(row) {
      this.$refs.deleteDialog.open(row)
    },
    handleViewMachine(row) {
      this.$router.push(`/views/inspection/maintainancePlanMachine?planId=${row.planId}`)
    },
    handleEnable(row) {
      this.$refs.stateDialog.open({
        planId: row.planId,
        stateName: this.$t('maintainancePlanManage.button.enable'),
        state: '2020025'
      })
    },
    handleDisable(row) {
      this.$refs.stateDialog.open({
        planId: row.planId,
        stateName: this.$t('maintainancePlanManage.button.disable'),
        state: '2020026'
      })
    },
    handleSuccess() {
      this.getList()
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
.maintainance-plan-manage-container {
  padding: 20px;

  .search-card {
    margin-bottom: 20px;
  }

  .table-card {
    margin-bottom: 20px;
  }

  .pagination-wrapper {
    margin-top: 20px;
    display: flex;
    justify-content: space-between;
    align-items: center;

    .tip {
      color: #909399;
      font-size: 14px;
    }
  }
}
</style>