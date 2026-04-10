<template>
  <div class="padding">
    <el-card class="box-card">
      <div slot="header" class="flex justify-between">
        <div>{{ $t('inspectionTaskDetails.searchCondition') }}</div>
        <div class="">
          <el-button type="text" @click="toggleMoreCondition">
            {{ inspectionTaskDetailManageInfo.moreCondition ? $t('inspectionTaskDetails.hide') :
              $t('inspectionTaskDetails.more') }}
          </el-button>
        </div>
      </div>
      <div class="">
        <el-row :gutter="20">
          <el-col :span="6">
            <div class="form-group">
              <el-input :placeholder="$t('inspectionTaskDetails.inputInspector')"
                v-model="inspectionTaskDetailManageInfo.conditions.planUserName" clearable></el-input>
            </div>
          </el-col>
          <el-col :span="6">
            <div class="form-group">
              <el-date-picker v-model="inspectionTaskDetailManageInfo.conditions.inspectionStartTime" type="datetime" value-format="yyyy-MM-dd HH:mm:ss"                :placeholder="$t('inspectionTaskDetails.inputStartTime')" 
                style="width:100%"></el-date-picker>
            </div>
          </el-col>
          <el-col :span="6">
            <div class="form-group">
              <el-date-picker v-model="inspectionTaskDetailManageInfo.conditions.inspectionEndTime" type="datetime" value-format="yyyy-MM-dd HH:mm:ss"                :placeholder="$t('inspectionTaskDetails.inputEndTime')" 
                style="width:100%"></el-date-picker>
            </div>
          </el-col>
          <el-col :span="6">
            <el-button type="primary" @click="queryInspectionTaskMethod">
              <i class="el-icon-search"></i>
              <span>{{ $t('inspectionTaskDetails.search') }}</span>
            </el-button>
            <el-button type="primary" @click="resetInspectionTaskMethod">
              <i class="el-icon-refresh"></i>
              <span>{{ $t('inspectionTaskDetails.reset') }}</span>
            </el-button>
          </el-col>
        </el-row>

        <div v-show="inspectionTaskDetailManageInfo.moreCondition">
          <el-row :gutter="20" class="margin-top">
            <el-col :span="6">
              <el-select v-model="inspectionTaskDetailManageInfo.conditions.inspectionPlanId"
                :placeholder="$t('inspectionTaskDetails.selectInspectionPlan')" style="width:100%">
                <el-option v-for="(item, index) in inspectionTaskDetailManageInfo.inspectionPlanList" :key="index"
                  :label="item.inspectionPlanName" :value="item.inspectionPlanId"></el-option>
              </el-select>
            </el-col>
            <el-col :span="6">
              <el-select v-model="inspectionTaskDetailManageInfo.conditions.inspectionRouteId"
                :placeholder="$t('inspectionTaskDetails.selectInspectionRoute')" style="width:100%">
                <el-option v-for="(item, index) in inspectionTaskDetailManageInfo.inspectionRouteList" :key="index"
                  :label="item.routeName" :value="item.inspectionRouteId"></el-option>
              </el-select>
            </el-col>
            <el-col :span="6">
              <el-select v-model="inspectionTaskDetailManageInfo.conditions.inspectionId"
                :placeholder="$t('inspectionTaskDetails.selectInspectionPoint')" style="width:100%">
                <el-option v-for="(item, index) in inspectionTaskDetailManageInfo.inspectionPointList" :key="index"
                  :label="item.inspectionName" :value="item.inspectionId"></el-option>
              </el-select>
            </el-col>
          </el-row>

          <el-row :gutter="20" class="margin-top">
            <el-col :span="6">
              <div class="form-group">
                <el-input :placeholder="$t('inspectionTaskDetails.inputTaskDetailId')"
                  v-model="inspectionTaskDetailManageInfo.conditions.taskDetailId" clearable></el-input>
              </div>
            </el-col>
            <el-col :span="6">
              <el-select v-model="inspectionTaskDetailManageInfo.conditions.inspectionState"
                :placeholder="$t('inspectionTaskDetails.selectSignStatus')" style="width:100%">
                <el-option v-for="(item, index) in inspectionTaskDetailManageInfo.inspectionStateTypes" :key="index"
                  :label="item.name" :value="item.statusCd"></el-option>
              </el-select>
            </el-col>
            <el-col :span="6">
              <el-select v-model="inspectionTaskDetailManageInfo.conditions.state"
                :placeholder="$t('inspectionTaskDetails.selectInspectionPointStatus')" style="width:100%">
                <el-option v-for="(item, index) in inspectionTaskDetailManageInfo.stateTypes" :key="index"
                  :label="item.name" :value="item.statusCd"></el-option>
              </el-select>
            </el-col>
          </el-row>

          <el-row :gutter="20" class="margin-top">
            <el-col :span="6">
              <el-select v-model="inspectionTaskDetailManageInfo.conditions.taskState"
                :placeholder="$t('inspectionTaskDetails.selectTaskStatus')" style="width:100%">
                <el-option v-for="(item, index) in inspectionTaskDetailManageInfo.taskStates" :key="index"
                  :label="item.name" :value="item.statusCd"></el-option>
              </el-select>
            </el-col>
            <el-col :span="6">
              <el-select v-model="inspectionTaskDetailManageInfo.conditions.patrolType"
                :placeholder="$t('inspectionTaskDetails.selectPatrolStatus')" style="width:100%">
                <el-option v-for="(item, index) in inspectionTaskDetailManageInfo.patrolTypes" :key="index"
                  :label="item.name" :value="item.statusCd"></el-option>
              </el-select>
            </el-col>
          </el-row>
        </div>
      </div>
    </el-card>

    <el-card class="box-card margin-top">
      <div slot="header" class="flex justify-between">
        <div>{{ $t('inspectionTaskDetails.inspectionDetails') }}</div>
        <div class="ibox-tools" style="top:10px;">
          <el-button type="primary" size="small" @click="exportExcel">
            <i class="el-icon-download"></i>
            <span>{{ $t('inspectionTaskDetails.export') }}</span>
          </el-button>
        </div>
      </div>
      <div class="">
        <el-table :data="inspectionTaskDetailManageInfo.inspectionTasks" border style="width: 100%" v-loading="loading">
          <el-table-column prop="taskDetailId" :label="$t('inspectionTaskDetails.taskDetailId')"
            align="center"></el-table-column>
          <el-table-column prop="inspectionName" :label="$t('inspectionTaskDetails.inspectionPointName')"
            align="center"></el-table-column>
          <el-table-column prop="inspectionPlanName" :label="$t('inspectionTaskDetails.inspectionPlanName')"
            align="center"></el-table-column>
          <el-table-column prop="routeName" :label="$t('inspectionTaskDetails.inspectionRouteName')"
            align="center"></el-table-column>

          <el-table-column :label="$t('inspectionTaskDetails.inspector')" align="center">
            <template slot-scope="scope">
              {{ scope.row.planInsTime }}<br />{{ scope.row.planEndTime }}
            </template>
          </el-table-column>

          <el-table-column :label="$t('inspectionTaskDetails.inspectionPoint')" align="center">
            <template slot-scope="scope">
              {{ scope.row.pointStartTime }}<br />{{ scope.row.pointEndTime }}
            </template>
          </el-table-column>

          <el-table-column prop="inspectionTime" :label="$t('inspectionTaskDetails.actualInspectionTime')"
            align="center">
            <template slot-scope="scope">
              {{ scope.row.inspectionTime || '-' }}
            </template>
          </el-table-column>

          <el-table-column :label="$t('inspectionTaskDetails.actualSignStatus')" align="center">
            <template slot-scope="scope">
              <span v-if="scope.row.inspectionState == '60000'" class="text-primary">
                {{ scope.row.inspectionStateName || '-' }}
              </span>
              <span v-else class="text-danger">
                {{ scope.row.inspectionStateName || '-' }}
              </span>
            </template>
          </el-table-column>

          <el-table-column prop="planUserName" :label="$t('inspectionTaskDetails.plannedInspector')"
            align="center"></el-table-column>

          <el-table-column :label="$t('inspectionTaskDetails.actualInspector')" align="center">
            <template slot-scope="scope">
              {{ scope.row.actUserName || '-' }}
            </template>
          </el-table-column>

          <el-table-column prop="signTypeName" :label="$t('inspectionTaskDetails.inspectionMethod')"
            align="center"></el-table-column>
          <el-table-column prop="taskStateName" :label="$t('inspectionTaskDetails.taskStatus')"
            align="center"></el-table-column>

          <el-table-column :label="$t('inspectionTaskDetails.inspectionPointStatus')" align="center">
            <template slot-scope="scope">
              <span v-if="scope.row.state == '20200408'" class="text-danger">
                {{ scope.row.stateName }}
              </span>
              <span v-else>
                {{ scope.row.stateName }}
              </span>
            </template>
          </el-table-column>

          <el-table-column :label="$t('inspectionTaskDetails.patrolStatus')" align="center">
            <template slot-scope="scope">
              <span class="text-primary">
                {{ scope.row.description || '-' }}
              </span>
            </template>
          </el-table-column>

          <el-table-column :label="$t('inspectionTaskDetails.inspectionPhotos')" align="center">
            <template slot-scope="scope">
              <span v-for="_photo in scope.row.photos" :key="_photo.url">
                <el-image style="width: 60px; height: 60px; margin-right: 5px;" :src="_photo.url"
                  :preview-src-list="[_photo.url]" fit="cover"></el-image>
              </span>
            </template>
          </el-table-column>

          <el-table-column prop="createTime" :label="$t('inspectionTaskDetails.createTime')"
            align="center"></el-table-column>

          <el-table-column :label="$t('inspectionTaskDetails.locationInfo')" align="center" width="100">
            <template slot-scope="scope">
              <el-button type="primary" size="mini" @click="openMap(scope.row.latitude, scope.row.longitude)">
                {{ $t('inspectionTaskDetails.view') }}
              </el-button>
            </template>
          </el-table-column>
        </el-table>

        <el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange"
          :current-page="inspectionTaskDetailManageInfo.conditions.page" :page-sizes="[10, 20, 30, 50]"
          :page-size="inspectionTaskDetailManageInfo.conditions.row" layout="total, sizes, prev, pager, next, jumper"
          :total="inspectionTaskDetailManageInfo.total" class="pagination-container"></el-pagination>
      </div>
    </el-card>

    <view-map ref="viewMap"></view-map>
    <view-image ref="viewImage"></view-image>
  </div>
</template>

<script>
import { listInspectionTaskDetails, exportInspectionTaskDetails,listInspectionPlans,listInspectionRoutes,listInspectionPoints } from '@/api/inspection/inspectionTaskDetailsApi'
import { getDict } from '@/api/community/communityApi'
import { getCommunityId } from '@/api/community/communityApi'
import ViewMap from '@/components/system/ViewMap'
import ViewImage from '@/components/system/viewImage'

export default {
  name: 'InspectionTaskDetailsList',
  components: {
    ViewMap,
    ViewImage
  },
  data() {
    return {
      loading: false,
      inspectionTaskDetailManageInfo: {
        inspectionTasks: [],
        total: 0,
        records: 1,
        moreCondition: false,
        stateTypes: [],
        inspectionStateTypes: [],
        taskStates: [],
        patrolTypes: [],
        inspectionPointList: [],
        inspectionPlanList: [],
        inspectionRouteList: [],
        conditions: {
          planUserName: '',
          taskDetailId: '',
          inspectionPlanName: '',
          inspectionStartTime: '',
          inspectionEndTime: '',
          state: '',
          inspectionState: '',
          inspectionId: '',
          inspectionPlanId: '',
          inspectionRouteId: '',
          taskState: '',
          patrolType: '',
          page: 1,
          row: 10,
          communityId: ''
        }
      }
    }
  },
  created() {
    this.communityId = getCommunityId()
    this.initData()
  },
  methods: {
    async initData() {
      this.loading = true
      try {
        await this.getDictData()
        await this.listInspectionTasksDetailList()
        await this.listInspectionPlanInfo()
        await this.listInspectionRouteInfo()
        await this.listInspectionPointInfo()
      } catch (error) {
        console.error('初始化数据失败:', error)
        this.$message.error(this.$t('common.error'))
      } finally {
        this.loading = false
      }
    },

    async getDictData() {
      try {
        const [stateTypes, inspectionStateTypes, taskStates, patrolTypes] = await Promise.all([
          getDict('inspection_task', 'state'),
          getDict('inspection_task_detail', 'inspection_state'),
          getDict('inspection_task_detail', 'state'),
          getDict('inspection_task_detail', 'patrol_type')
        ])

        this.inspectionTaskDetailManageInfo.stateTypes = stateTypes
        this.inspectionTaskDetailManageInfo.inspectionStateTypes = inspectionStateTypes
        this.inspectionTaskDetailManageInfo.taskStates = taskStates
        this.inspectionTaskDetailManageInfo.patrolTypes = patrolTypes
      } catch (error) {
        console.error('获取字典数据失败:', error)
      }
    },

    async listInspectionTasksDetailList() {
      try {
        this.inspectionTaskDetailManageInfo.conditions.communityId = this.communityId
        const res = await listInspectionTaskDetails(this.inspectionTaskDetailManageInfo.conditions)
        this.inspectionTaskDetailManageInfo.total = res.total
        this.inspectionTaskDetailManageInfo.records = res.records
        this.inspectionTaskDetailManageInfo.inspectionTasks = res.inspectionTaskDetails
      } catch (error) {
        console.error('获取巡检任务明细列表失败:', error)
        this.$message.error(this.$t('common.error'))
      }
    },

    async listInspectionPlanInfo() {
      try {
        const params = {
          communityId: this.communityId,
          page: 1,
          row: 200
        }
        const res = await listInspectionPlans(params)
        this.inspectionTaskDetailManageInfo.inspectionPlanList = res.data
      } catch (error) {
        console.error('获取巡检计划列表失败:', error)
      }
    },

    async listInspectionRouteInfo() {
      try {
        const params = {
          communityId: this.communityId,
          page: 1,
          row: 200
        }
        const res = await listInspectionRoutes(params)
        this.inspectionTaskDetailManageInfo.inspectionRouteList = res.inspectionRoutes
      } catch (error) {
        console.error('获取巡检路线列表失败:', error)
      }
    },

    async listInspectionPointInfo() {
      try {
        const params = {
          communityId: this.communityId,
          page: 1,
          row: 200
        }
        const res = await listInspectionPoints(params)
        this.inspectionTaskDetailManageInfo.inspectionPointList = res.inspectionPoints
      } catch (error) {
        console.error('获取巡检点列表失败:', error)
      }
    },

    queryInspectionTaskMethod() {
      this.inspectionTaskDetailManageInfo.conditions.page = 1
      this.listInspectionTasksDetailList()
    },

    resetInspectionTaskMethod() {
      this.inspectionTaskDetailManageInfo.conditions = {
        planUserName: '',
        taskDetailId: '',
        inspectionPlanName: '',
        inspectionStartTime: '',
        inspectionEndTime: '',
        state: '',
        inspectionState: '',
        inspectionId: '',
        inspectionPlanId: '',
        inspectionRouteId: '',
        taskState: '',
        patrolType: '',
        page: 1,
        row: 10,
        communityId: this.communityId
      }
      this.listInspectionTasksDetailList()
    },

    toggleMoreCondition() {
      this.inspectionTaskDetailManageInfo.moreCondition = !this.inspectionTaskDetailManageInfo.moreCondition
    },

    handleSizeChange(val) {
      this.inspectionTaskDetailManageInfo.conditions.row = val
      this.listInspectionTasksDetailList()
    },

    handleCurrentChange(val) {
      this.inspectionTaskDetailManageInfo.conditions.page = val
      this.listInspectionTasksDetailList()
    },

    openFile(photo) {
      this.$refs.viewImage.open(photo.url)
    },

    openMap(lat, lng) {
      this.$refs.viewMap.open({lat: lat, lng: lng})
    },

    async exportExcel() {
      try {
        const params = {
          ...this.inspectionTaskDetailManageInfo.conditions,
          pagePath: 'inspectionTaskDetails'
        }
        const res = await exportInspectionTaskDetails(params)
        if (res.code === 0) {
          this.$message.success(this.$t('common.operationSuccess'))
          // 这里需要根据实际项目调整跳转逻辑
          this.$router.push({ path: '/pages/property/downloadTempFile', query: { tab: this.$t('common.downloadCenter') } })
        } else {
          this.$message.error(res.msg || this.$t('common.exportFail'))
        }
      } catch (error) {
        console.error('导出失败:', error)
        this.$message.error(this.$t('common.exportFail'))
      }
    }
  }
}
</script>

<style scoped>
.text-primary {
  color: #409EFF;
}

.text-danger {
  color: #F56C6C;
}

.margin-top {
  margin-top: 20px;
}

.pagination-container {
  margin-top: 20px;
  text-align: right;
}
</style>