<template>
  <div class="maintainance-task-manage-container">
    <!-- 查询条件 -->
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
          <el-input v-model="searchForm.planName" :placeholder="$t('maintainanceTaskManage.planNamePlaceholder')"
            clearable />
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

    <!-- 保养任务列表 -->
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

    <!-- 子组件 -->
    <maintainance-task-transfer ref="transferModal" @success="handleSuccess" />
    <maintainance-task-detail ref="detailModal" />
    <delete-maintainance-task ref="deleteModal" @success="handleSuccess" />
  </div>
</template>

<script>
import { listMaintainanceTask } from '@/api/inspection/maintainanceTaskManageApi'
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
        planUserName: '',
        startTime: '',
        endTime: '',
        planName: '',
        state: ''
      },
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
    handleSearch() {
      this.pagination.current = 1
      this.getList()
    },
    handleReset() {
      this.searchForm = {
        planUserName: '',
        startTime: '',
        endTime: '',
        planName: '',
        state: ''
      }
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