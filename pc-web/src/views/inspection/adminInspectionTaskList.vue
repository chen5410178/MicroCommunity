<template>
  <div class="admin-inspection-task-container">
    <el-row class="flex justify-start">
      <el-col :span="4" class="room-floor-unit-tree">
        <community-inspection-tree ref="communityTree" @notifyQuery="handleTreeQuery"></community-inspection-tree>
      </el-col>
      <el-col :span="20" class=" margin-left-sm">
        <el-card class="box-card">
          <div slot="header" class="clearfix flex justify-between">
            <span>{{ $t('adminInspectionTask.search.title') }}</span>
          </div>
          <el-row :gutter="20">
            <el-col :span="6">
              <el-date-picker
                v-model="searchForm.startTime"
                type="datetime"
                :placeholder="$t('adminInspectionTask.search.startTime')"
                style="width: 100%"
                value-format="yyyy-MM-dd HH:mm:ss"
              ></el-date-picker>
            </el-col>
            <el-col :span="6">
              <el-date-picker
                v-model="searchForm.endTime"
                type="datetime"
                :placeholder="$t('adminInspectionTask.search.endTime')"
                style="width: 100%"
                value-format="yyyy-MM-dd HH:mm:ss"
              ></el-date-picker>
            </el-col>
            <el-col :span="6">
              <el-select
                v-model="searchForm.state"
                :placeholder="$t('adminInspectionTask.search.state')"
                style="width: 100%"
              >
                <el-option
                  v-for="item in stateTypes"
                  :key="item.statusCd"
                  :label="item.name"
                  :value="item.statusCd"
                ></el-option>
              </el-select>
            </el-col>
            <el-col :span="6">
              <el-button type="primary" @click="handleSearch">{{ $t('adminInspectionTask.search.button') }}</el-button>
            </el-col>
          </el-row>
        </el-card>

        <el-card class="box-card margin-top">
          <div slot="header" class="clearfix flex justify-between">
            <span>{{ $t('adminInspectionTask.list.title') }}</span>
          </div>
          <el-table :data="tableData" border style="width: 100%">
            <el-table-column prop="taskId" :label="$t('adminInspectionTask.table.taskId')" align="center"></el-table-column>
            <el-table-column prop="communityName" :label="$t('adminInspectionTask.table.communityName')" align="center"></el-table-column>
            <el-table-column prop="inspectionPlanName" :label="$t('adminInspectionTask.table.inspectionPlanName')" align="center"></el-table-column>
            <el-table-column :label="$t('adminInspectionTask.table.planTime')" align="center">
              <template slot-scope="scope">
                <div>{{ scope.row.planInsTime }}</div>
                <div>{{ scope.row.planEndTime }}</div>
              </template>
            </el-table-column>
            <el-table-column prop="actInsTime" :label="$t('adminInspectionTask.table.actInsTime')" align="center">
              <template slot-scope="scope">
                {{ scope.row.actInsTime || '-' }}
              </template>
            </el-table-column>
            <el-table-column prop="originalPlanUserName" :label="$t('adminInspectionTask.table.originalPlanUserName')" align="center">
              <template slot-scope="scope">
                {{ scope.row.originalPlanUserName || '-' }}
              </template>
            </el-table-column>
            <el-table-column prop="planUserName" :label="$t('adminInspectionTask.table.planUserName')" align="center"></el-table-column>
            <el-table-column prop="transferDesc" :label="$t('adminInspectionTask.table.transferDesc')" align="center">
              <template slot-scope="scope">
                {{ scope.row.transferDesc || '-' }}
              </template>
            </el-table-column>
            <el-table-column prop="signTypeName" :label="$t('adminInspectionTask.table.signTypeName')" align="center"></el-table-column>
            <el-table-column prop="stateName" :label="$t('adminInspectionTask.table.stateName')" align="center">
              <template slot-scope="scope">
                <span v-if="scope.row.state === '20200408'" class="text-danger">{{ scope.row.stateName }}</span>
                <span v-else>{{ scope.row.stateName }}</span>
              </template>
            </el-table-column>
            <el-table-column :label="$t('adminInspectionTask.table.operation')" align="center" width="120">
              <template slot-scope="scope">
                <el-button size="mini" @click="handleDetail(scope.row)">{{ $t('adminInspectionTask.table.detail') }}</el-button>
              </template>
            </el-table-column>
          </el-table>

          <el-row class="margin-top">
            <el-col :span="18">
              <div class="tip-text">
                {{ $t('adminInspectionTask.list.tip') }}
              </div>
            </el-col>
            <el-col :span="6">
              <el-pagination
                :current-page.sync="pagination.current"
                :page-sizes="[10, 20, 30, 50]"
                :page-size="pagination.size"
                :total="pagination.total"
                layout="total, sizes, prev, pager, next, jumper"
                @size-change="handleSizeChange"
                @current-change="handleCurrentChange"
              ></el-pagination>
            </el-col>
          </el-row>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import { listAdminInspectionTasks } from '@/api/inspection/adminInspectionTaskApi'
import CommunityInspectionTree from '@/components/inspection/communityInspectionTree'
import {getDict} from '@/api/community/communityApi'

export default {
  name: 'AdminInspectionTaskList',
  components: {
    CommunityInspectionTree
  },
  data() {
    return {
      searchForm: {
        startTime: '',
        endTime: '',
        state: '',
        communityId: '',
        inspectionPlanId: '',
        staffId: '',
        orderByDesc: 'desc'
      },
      stateTypes: [],
      tableData: [],
      pagination: {
        current: 1,
        size: 10,
        total: 0
      },
      loading: false
    }
  },
  created() {
    this.getDictData()
    this.getList()
  },
  methods: {
    async getDictData() {
      try {
        const response = await getDict('inspection_task', 'state')
        this.stateTypes = response || []
      } catch (error) {
        console.error('Failed to get dict data:', error)
      }
    },
    async getList() {
      this.loading = true
      try {
        const params = {
          ...this.searchForm,
          page: this.pagination.current,
          row: this.pagination.size
        }
        const response = await listAdminInspectionTasks(params)
        this.tableData = response.inspectionTasks || []
        this.pagination.total = response.total || 0
      } catch (error) {
        console.error('Failed to get inspection tasks:', error)
      } finally {
        this.loading = false
      }
    },
    handleTreeQuery(params) {
      this.searchForm.communityId = params.communityId
      this.searchForm.inspectionPlanId = params.inspectionPlanId
      this.searchForm.staffId = params.staffId
      this.getList()
    },
    handleSearch() {
      this.pagination.current = 1
      this.getList()
    },
    handleDetail(row) {
      window.open(`/#/views/inspection/adminInspectionTaskDetail?taskId=${row.taskId}`)
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
.admin-inspection-task-container {
  padding: 20px;

  .margin-top {
    margin-top: 20px;
  }

  .margin-top-xs {
    margin-top: 10px;
  }

  .margin-left-sm {
    margin-left: 10px;
  }

  .room-floor-unit-tree {
    padding-right: 0;
  }

  .tip-text {
    padding: 10px;
    color: #666;
    font-size: 14px;
  }

  .text-danger {
    color: #f56c6c;
    font-weight: bold;
  }
}
</style>