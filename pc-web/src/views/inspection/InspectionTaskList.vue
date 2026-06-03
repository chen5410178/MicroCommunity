<template>
  <div class="inspection-task-container">
    <el-row :gutter="20">
      <el-col :span="3" class="left-panel">
        <el-card class="plan-card">
          <div class="plan-list-wrapper">
            <div v-for="item in plans" :key="item.inspectionPlanId" class="plan-list-item"
              :class="{ 'selected': conditions.inspectionPlanId === item.inspectionPlanId }" @click="switchPlan(item)">
              {{ item.inspectionPlanName }}
            </div>
          </div>
        </el-card>
      </el-col>

      <el-col :span="21" class="right-panel">
        <el-card>
          <div slot="header" class="flex justify-between">
            <span>{{ $t('inspectionTask.queryConditions') }}</span>
          </div>

          <el-row :gutter="20">
            <el-col :span="4">
              <el-input v-model="conditions.planUserName" :placeholder="$t('inspectionTask.executor')"
                clearable></el-input>
            </el-col>

            <el-col :span="4">
              <el-date-picker v-model="conditions.startTime" type="datetime"
                :placeholder="$t('inspectionTask.actualStartTime')" value-format="yyyy-MM-dd HH:mm:ss"
                style="width: 100%"></el-date-picker>
            </el-col>

            <el-col :span="4">
              <el-date-picker v-model="conditions.endTime" type="datetime"
                :placeholder="$t('inspectionTask.actualEndTime')" value-format="yyyy-MM-dd HH:mm:ss"
                style="width: 100%"></el-date-picker>
            </el-col>

            <el-col :span="4">
              <el-select v-model="conditions.state" :placeholder="$t('inspectionTask.inspectionStatus')" clearable
                style="width: 100%">
                <el-option v-for="item in stateTypes" :key="item.statusCd" :label="item.name"
                  :value="item.statusCd"></el-option>
              </el-select>
            </el-col>
            <el-col :span="4" >
              <el-button type="primary" @click="queryInspectionTasks">
                <i class="el-icon-search"></i> {{ $t('inspectionTask.query') }}
              </el-button>
              <el-button @click="resetConditions">
                <i class="el-icon-refresh"></i> {{ $t('inspectionTask.reset') }}
              </el-button>
            </el-col>
          </el-row>
        </el-card>

        <el-card style="margin-top: 20px;">
          <div slot="header" class="flex justify-between">
            <span>{{ $t('inspectionTask.inspectionTask') }}</span>
          </div>

          <el-table :data="inspectionTasks" border style="width: 100%">
            <el-table-column prop="taskId" :label="$t('inspectionTask.taskCode')" align="center" />
            <el-table-column prop="inspectionPlanName" :label="$t('inspectionTask.inspectionPlan')" align="center" />
            <el-table-column :label="$t('inspectionTask.inspectorTime')" align="center">
              <template slot-scope="{row}">
                {{ row.planInsTime }}<br>{{ row.planEndTime }}
              </template>
            </el-table-column>
            <el-table-column prop="actInsTime" :label="$t('inspectionTask.actualTime')" align="center">
              <template slot-scope="{row}">
                {{ row.actInsTime || '-' }}
              </template>
            </el-table-column>
            <el-table-column prop="originalPlanUserName" :label="$t('inspectionTask.plannedInspector')" align="center">
              <template slot-scope="{row}">
                {{ row.originalPlanUserName || '-' }}
              </template>
            </el-table-column>
            <el-table-column prop="planUserName" :label="$t('inspectionTask.currentInspector')" align="center" />
            <el-table-column prop="transferDesc" :label="$t('inspectionTask.transferDesc')" align="center">
              <template slot-scope="{row}">
                {{ row.transferDesc || '-' }}
              </template>
            </el-table-column>
            <el-table-column prop="signTypeName" :label="$t('inspectionTask.inspectionMethod')" align="center" />
            <el-table-column :label="$t('inspectionTask.inspectionStatus')" align="center">
              <template slot-scope="{row}">
                <span v-if="row.state === '20200408'" class="text-danger font-bold">
                  {{ row.stateName }}
                </span>
                <span v-else>{{ row.stateName }}</span>
              </template>
            </el-table-column>
            <el-table-column :label="$t('inspectionTask.operation')" align="center" width="220">
              <template slot-scope="{row}">
                <el-button v-if="row.state === '20200406' || row.state === '20200405'" size="mini"
                  @click="openTransfer(row)">
                  {{ $t('inspectionTask.flow') }}
                </el-button>
                <el-button size="mini" type="primary" @click="openDetail(row)">
                  {{ $t('inspectionTask.detail') }}
                </el-button>
                <el-button v-if="hasPrivilege('502022031612550001')" size="mini" type="danger" @click="openDelete(row)">
                  {{ $t('inspectionTask.delete') }}
                </el-button>
              </template>
            </el-table-column>
          </el-table>

          <el-row style="margin-top: 20px;">
            <el-col :span="18" class="text-left table-desc">
              <div >{{ $t('inspectionTask.note') }}</div>
            </el-col>
            <el-col :span="6">
              <el-pagination :current-page="currentPage" :page-size="pageSize" :total="total"
                layout="total, prev, pager, next, jumper" @current-change="handlePageChange" />
            </el-col>
          </el-row>
        </el-card>
      </el-col>
    </el-row>

    <InspectionTaskDetail ref="inspectionTaskDetail" />
    <InspectionTaskTransfer ref="inspectionTaskTransfer" @success="loadInspectionTasks" />
    <DeleteInspectionTask ref="deleteInspectionTask" @success="loadInspectionTasks" />
  </div>
</template>

<script>
import InspectionTaskDetail from '@/components/inspection/InspectionTaskDetail'
import InspectionTaskTransfer from '@/components/inspection/InspectionTaskTransfer'
import DeleteInspectionTask from '@/components/inspection/DeleteInspectionTask'
import {
  listInspectionPlans,
  listInspectionTasks,
} from '@/api/inspection/inspectionTaskApi'
import {getDict} from '@/api/community/communityApi'

export default {
  name: 'InspectionTaskList',
  components: { InspectionTaskDetail, InspectionTaskTransfer, DeleteInspectionTask },
  data() {
    return {
      plans: [],
      inspectionTasks: [],
      stateTypes: [],
      conditions: {
        planUserName: '',
        inspectionPlanId: '',
        startTime: '',
        endTime: '',
        state: '',
        orderByDesc: 'desc'
      },
      currentPage: 1,
      pageSize: 10,
      total: 0
    }
  },
  async created() {
    await this.loadPlans()
    await this.loadStateTypes()
    this.loadInspectionTasks()
  },
  methods: {
    async loadPlans() {
      try {
        const {data} = await listInspectionPlans()
        this.plans = [{ inspectionPlanName: this.$t('common.all'), inspectionPlanId: '' }, ...data]
      } catch (error) {
        console.error('加载巡检计划失败:', error)
      }
    },
    async loadStateTypes() {
      try {
        this.stateTypes = await getDict('inspection_task', 'state')
      } catch (error) {
        console.error('加载状态类型失败:', error)
      }
    },
    async loadInspectionTasks() {
      try {
        const params = {
          ...this.conditions,
          page: this.currentPage,
          row: this.pageSize
        }
        const res = await listInspectionTasks(params)
        this.inspectionTasks = res.inspectionTasks || []
        this.total = res.total || 0
      } catch (error) {
        console.error('加载巡检任务失败:', error)
      }
    },
    switchPlan(plan) {
      this.conditions.inspectionPlanId = plan.inspectionPlanId
      this.currentPage = 1
      this.loadInspectionTasks()
    },
    queryInspectionTasks() {
      this.currentPage = 1
      this.loadInspectionTasks()
    },
    resetConditions() {
      this.conditions = {
        planUserName: '',
        inspectionPlanId: '',
        startTime: '',
        endTime: '',
        state: '',
        orderByDesc: 'desc'
      }
      this.currentPage = 1
      this.loadInspectionTasks()
    },
    handlePageChange(page) {
      this.currentPage = page
      this.loadInspectionTasks()
    },
    openDetail(task) {
      this.$refs.inspectionTaskDetail.open(task)
    },
    openTransfer(task) {
      this.$refs.inspectionTaskTransfer.open(task)
    },
    openDelete(task) {
      this.$refs.deleteInspectionTask.open(task)
    },
  }
}
</script>

<style lang="scss" scoped>
.inspection-task-container {
  padding: 20px;
}

.left-panel {
  .plan-card {
    height: 700px;

    .plan-list-wrapper {
      height: 640px;
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

.note {
  font-size: 12px;
  color: #909399;
  padding: 10px;
  background-color: #f8f8f9;
  border-radius: 4px;
}

.text-danger {
  color: #F56C6C;
}

.font-bold {
  font-weight: bold;
}
</style>