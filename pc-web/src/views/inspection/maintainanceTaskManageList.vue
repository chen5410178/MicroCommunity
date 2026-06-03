<template>
  <div class="maintainance-task-manage-container">
    <el-row :gutter="20">
      <el-col :span="4" class="left-panel">
        <el-card class="plan-card">
          <div class="plan-list-wrapper">
            <div class="plan-list-item"
              :class="{ 'selected': searchForm.planId == '' }"
              @click="_switchPlan({ planId: '', planName: '' })">
              {{ $t('common.all') }}
            </div>
            <div v-for="(item, index) in planList" :key="index"
              class="plan-list-item"
              :class="{ 'selected': searchForm.planId == item.planId }"
              @click="_switchPlan(item)">
              {{ item.planName }}
            </div>
          </div>
        </el-card>
      </el-col>

      <el-col :span="20" class="right-panel">
        <el-card class="search-card">
          <div slot="header" class="flex justify-between">
            <span>{{ $t('maintainanceTaskManage.searchTitle') }}</span>
          </div>
          <el-row :gutter="20">
            <el-col :span="4">
              <el-input v-model="searchForm.planUserName"
                :placeholder="$t('maintainanceTaskManage.planUserNamePlaceholder')" clearable />
            </el-col>
            <el-col :span="4">
              <el-date-picker v-model="searchForm.startTime" type="datetime" value-format="yyyy-MM-dd HH:mm:ss"
                :placeholder="$t('maintainanceTaskManage.startTimePlaceholder')" style="width: 100%" />
            </el-col>
            <el-col :span="4">
              <el-date-picker v-model="searchForm.endTime" type="datetime" value-format="yyyy-MM-dd HH:mm:ss"
                :placeholder="$t('maintainanceTaskManage.endTimePlaceholder')" style="width: 100%" />
            </el-col>
            <el-col :span="4">
              <el-select v-model="searchForm.state" :placeholder="$t('maintainanceTaskManage.statePlaceholder')"
                style="width: 100%">
                <el-option v-for="item in stateTypes" :key="item.statusCd" :label="item.name" :value="item.statusCd" />
              </el-select>
            </el-col>
            <el-col :span="4">
              <el-button type="primary" @click="handleSearch">
                {{ $t('maintainanceTaskManage.search') }}
              </el-button>
              <el-button @click="handleReset">
                {{ $t('maintainanceTaskManage.reset') }}
              </el-button>
            </el-col>
          </el-row>
        </el-card>

        <el-card class="list-card">
          <div slot="header" class="flex justify-between">
            <span>{{ $t('maintainanceTaskManage.listTitle') }}</span>
          </div>
          <el-table :data="tableData" border style="width: 100%" v-loading="loading">
            <el-table-column prop="taskId" :label="$t('maintainanceTaskManage.taskId')" align="center" />
            <el-table-column prop="planName" :label="$t('maintainanceTaskManage.planName')" align="center" />
            <el-table-column :label="$t('maintainanceTaskManage.timeRange')" align="center">
              <template slot-scope="scope">
                {{ scope.row.planInsTime }}<br />{{ scope.row.planEndTime }}
              </template>
            </el-table-column>
            <el-table-column prop="actInsTime" :label="$t('maintainanceTaskManage.actInsTime')" align="center">
              <template slot-scope="scope">
                {{ scope.row.actInsTime || '-' }}
              </template>
            </el-table-column>
            <el-table-column prop="originalPlanUserName" :label="$t('maintainanceTaskManage.originalPlanUser')"
              align="center">
              <template slot-scope="scope">
                {{ scope.row.originalPlanUserName || '-' }}
              </template>
            </el-table-column>
            <el-table-column prop="planUserName" :label="$t('maintainanceTaskManage.currentPlanUser')" align="center" />
            <el-table-column prop="transferDesc" :label="$t('maintainanceTaskManage.transferDesc')" align="center">
              <template slot-scope="scope">
                {{ scope.row.transferDesc || '-' }}
              </template>
            </el-table-column>
            <el-table-column prop="stateName" :label="$t('maintainanceTaskManage.state')" align="center">
              <template slot-scope="scope">
                <span v-if="scope.row.state === '20200408'" class="text-danger">
                  {{ scope.row.stateName }}
                </span>
                <span v-else>
                  {{ scope.row.stateName }}
                </span>
              </template>
            </el-table-column>
            <el-table-column :label="$t('maintainanceTaskManage.operation')" align="center" width="200">
              <template slot-scope="scope">
                <el-button size="mini" v-if="scope.row.state === '20200406' || scope.row.state === '20200405'"
                  @click="handleTransfer(scope.row)">
                  {{ $t('maintainanceTaskManage.transfer') }}
                </el-button>
                <el-button size="mini" @click="handleDetail(scope.row)">
                  {{ $t('maintainanceTaskManage.detail') }}
                </el-button>
                <el-button size="mini" type="danger" v-if="hasPrivilege('502022031612550001')"
                  @click="handleDelete(scope.row)">
                  {{ $t('maintainanceTaskManage.delete') }}
                </el-button>
              </template>
            </el-table-column>
          </el-table>

          <div class="pagination-wrapper">
            <el-pagination :current-page="pagination.current" :page-sizes="[10, 20, 30, 50]" :page-size="pagination.size"
              :total="pagination.total" layout="total, sizes, prev, pager, next, jumper" @size-change="handleSizeChange"
              @current-change="handleCurrentChange" />
          </div>

          <div class="tip-text">
            {{ $t('maintainanceTaskManage.tip') }}
          </div>
        </el-card>
      </el-col>
    </el-row>

    <maintainance-task-transfer ref="transferModal" @success="handleSuccess" />
    <maintainance-task-detail ref="detailModal" />
    <delete-maintainance-task ref="deleteModal" @success="handleSuccess" />
  </div>
</template>

<script>
import { listMaintainanceTask } from '@/api/inspection/maintainanceTaskManageApi'
import { listMaintainancePlan } from '@/api/inspection/maintainancePlanManageApi'
import { getDict } from '@/api/community/communityApi'
import { getCommunityId } from '@/api/community/communityApi'
import MaintainanceTaskTransfer from '@/components/inspection/maintainanceTaskTransfer'
import MaintainanceTaskDetail from '@/components/inspection/maintainanceTaskDetail'
import DeleteMaintainanceTask from '@/components/inspection/deleteMaintainanceTask'

export default {
  name: 'MaintainanceTaskManageList',
  components: {
    MaintainanceTaskTransfer,
    MaintainanceTaskDetail,
    DeleteMaintainanceTask
  },
  data() {
    return {
      loading: false,
      searchForm: {
        planId: '',
        planUserName: '',
        startTime: '',
        endTime: '',
        planName: '',
        state: ''
      },
      planList: [],
      stateTypes: [],
      tableData: [],
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
    this.getDictData()
    this._listPlans()
    this.getList()
  },
  methods: {
    async getDictData() {
      try {
        const data = await getDict('maintainance_task', 'state')
        this.stateTypes = data
      } catch (error) {
        console.error('获取字典数据失败:', error)
      }
    },
    async _listPlans() {
      try {
        const params = {
          page: 1,
          row: 1000,
          communityId: this.communityId
        }
        const { data } = await listMaintainancePlan(params)
        this.planList = data || []
      } catch (error) {
        console.error('获取保养计划列表失败:', error)
      }
    },
    async getList() {
      try {
        this.loading = true
        const params = {
          page: this.pagination.current,
          row: this.pagination.size,
          communityId: this.communityId,
          ...this.searchForm
        }
        const { data, total } = await listMaintainanceTask(params)
        this.tableData = data
        this.pagination.total = total
      } catch (error) {
        this.$message.error(this.$t('maintainanceTaskManage.fetchError'))
      } finally {
        this.loading = false
      }
    },
    _switchPlan(item) {
      this.searchForm.planId = item.planId
      this.pagination.current = 1
      this.getList()
    },
    handleSearch() {
      this.pagination.current = 1
      this.getList()
    },
    handleReset() {
      this.searchForm.planUserName = ''
      this.searchForm.startTime = ''
      this.searchForm.endTime = ''
      this.searchForm.state = ''
      this.pagination.current = 1
      this.getList()
    },
    handleSizeChange(val) {
      this.pagination.size = val
      this.getList()
    },
    handleCurrentChange(val) {
      this.pagination.current = val
      this.getList()
    },
    handleTransfer(row) {
      this.$refs.transferModal.open(row)
    },
    handleDetail(row) {
      this.$refs.detailModal.open(row)
    },
    handleDelete(row) {
      this.$refs.deleteModal.open(row)
    },
    handleSuccess() {
      this.getList()
    },
  }
}
</script>

<style lang="scss" scoped>
.maintainance-task-manage-container {
  padding: 20px;

  .left-panel {
    .plan-card {
      height: 700px;

      .plan-list-wrapper {
        height: 660px;
        overflow-y: auto;

        &::-webkit-scrollbar {
          width: 4px;
        }

        &::-webkit-scrollbar-thumb {
          background: #dcdfe6;
          border-radius: 2px;
        }

        &::-webkit-scrollbar-track {
          background: transparent;
        }

        .plan-list-item {
          padding: 10px;
          text-align: center;
          cursor: pointer;
          border-radius: 4px;
          margin-bottom: 5px;
          transition: all 0.3s;
          font-size: 14px;
          color: #606266;

          &:hover {
            background-color: #f5f7fa;
          }

          &.selected {
            background-color: #409eff;
            color: white;
          }
        }
      }
    }
  }

  .right-panel {
    padding-left: 5px;
  }

  .search-card {
    margin-bottom: 20px;
  }

  .list-card {
    margin-bottom: 20px;
  }

  .pagination-wrapper {
    margin-top: 20px;
    text-align: right;
  }

  .tip-text {
    margin-top: 20px;
    color: #999;
    font-size: 14px;
  }

  .text-danger {
    color: #f56c6c;
    font-weight: bold;
  }
}
</style>
