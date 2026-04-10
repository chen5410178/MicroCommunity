<template>
  <div class="point-task-detail-container">
    <el-row :gutter="20" class="">
      <el-col :span="6">
        <el-input :placeholder="$t('pointTaskDetail.placeholder.planUserName')" v-model="pointTaskDetailInfo.planUserName"
          clearable />
      </el-col>
      <el-col :span="6">
        <el-date-picker v-model="pointTaskDetailInfo.inspectionStartTime" type="datetime" value-format="yyyy-MM-dd HH:mm:ss"          :placeholder="$t('pointTaskDetail.placeholder.inspectionStartTime')" 
          style="width: 100%;" />
      </el-col>
      <el-col :span="6">
        <el-date-picker v-model="pointTaskDetailInfo.inspectionEndTime" type="datetime" value-format="yyyy-MM-dd HH:mm:ss"          :placeholder="$t('pointTaskDetail.placeholder.inspectionEndTime')" 
          style="width: 100%;" />
      </el-col>
      <el-col :span="6">
        <el-button type="primary" @click="_qureyPointTaskDetail()">
          <i class="el-icon-search"></i> {{ $t('common.search') }}
        </el-button>
      </el-col>
    </el-row>

    <el-table :data="pointTaskDetailInfo.details" border style="width: 100%; margin-top: 20px;" v-loading="loading">
      <el-table-column prop="taskDetailId" :label="$t('pointTaskDetail.table.taskDetailId')" align="center" />
      <el-table-column prop="inspectionName" :label="$t('pointTaskDetail.table.inspectionName')" align="center" />
      <el-table-column prop="inspectionPlanName" :label="$t('pointTaskDetail.table.inspectionPlanName')" align="center" />
      <el-table-column prop="routeName" :label="$t('pointTaskDetail.table.routeName')" align="center" />
      <el-table-column :label="$t('pointTaskDetail.table.planTime')" align="center">
        <template slot-scope="scope">
          <div>{{ scope.row.planInsTime }}</div>
          <div>{{ scope.row.planEndTime }}</div>
        </template>
      </el-table-column>
      <el-table-column :label="$t('pointTaskDetail.table.pointTime')" align="center">
        <template slot-scope="scope">
          <div>{{ scope.row.pointStartTime }}</div>
          <div>{{ scope.row.pointEndTime }}</div>
        </template>
      </el-table-column>
      <el-table-column prop="inspectionTime" :label="$t('pointTaskDetail.table.inspectionTime')" align="center">
        <template slot-scope="scope">
          {{ scope.row.inspectionTime || '-' }}
        </template>
      </el-table-column>
      <el-table-column :label="$t('pointTaskDetail.table.inspectionState')" align="center">
        <template slot-scope="scope">
          <span
            :class="{ 'text-primary': scope.row.inspectionState === '60000', 'text-danger': scope.row.inspectionState !== '60000' }">
            {{ scope.row.inspectionStateName || '-' }}
          </span>
        </template>
      </el-table-column>
      <el-table-column prop="planUserName" :label="$t('pointTaskDetail.table.planUserName')" align="center" />
      <el-table-column prop="actUserName" :label="$t('pointTaskDetail.table.actUserName')" align="center">
        <template slot-scope="scope">
          {{ scope.row.actUserName || '-' }}
        </template>
      </el-table-column>
      <el-table-column prop="signTypeName" :label="$t('pointTaskDetail.table.signTypeName')" align="center" />
      <el-table-column prop="taskStateName" :label="$t('pointTaskDetail.table.taskStateName')" align="center" />
      <el-table-column :label="$t('pointTaskDetail.table.stateName')" align="center">
        <template slot-scope="scope">
          <span :class="{ 'text-danger': scope.row.state === '20200408' }">
            {{ scope.row.stateName }}
          </span>
        </template>
      </el-table-column>
      <el-table-column :label="$t('pointTaskDetail.table.description')" align="center">
        <template slot-scope="scope">
          <span class="text-primary">
            {{ scope.row.description || '-' }}
          </span>
        </template>
      </el-table-column>
      <el-table-column :label="$t('pointTaskDetail.table.photos')" align="center">
        <template slot-scope="scope">
          <div style="white-space: nowrap;">
            <el-image v-for="(photo, index) in scope.row.photos" :key="index"
              style="width: 60px; height: 60px; margin-right: 5px;" :src="photo.url" :preview-src-list="[photo.url]"
              fit="cover" />
          </div>
        </template>
      </el-table-column>
      <el-table-column prop="createTime" :label="$t('pointTaskDetail.table.createTime')" align="center" />
    </el-table>

    <el-pagination class="margin-top" :current-page.sync="page.current" :page-sizes="[10, 20, 30, 50]"
      :page-size="page.size" :total="page.total" layout="total, sizes, prev, pager, next, jumper"
      @size-change="handleSizeChange" @current-change="handleCurrentChange" />
  </div>
</template>

<script>
import { listInspectionTaskDetails } from '@/api/inspection/inspectionPointApi'
import { getCommunityId } from '@/api/community/communityApi'

export default {
  name: 'PointTaskDetail',
  data() {
    return {
      pointTaskDetailInfo: {
        details: [],
        inspectionId: '',
        inspectionRouteId: '',
        taskId: '',
        planUserName: '',
        inspectionStartTime: '',
        inspectionEndTime: ''
      },
      page: {
        current: 1,
        size: 10,
        total: 0
      },
      loading: false,
      communityId: ''
    }
  },
  created() {
    this.communityId = getCommunityId()
  },
  methods: {
    open(params) {
      this.pointTaskDetailInfo.inspectionId = params.inspectionId
      this.pointTaskDetailInfo.inspectionRouteId = params.inspectionRouteId
      this.pointTaskDetailInfo.taskId = params.taskId
      this._loadPointTaskDetailData(this.page.current, this.page.size)
    },
    loadData(point) {
      if (!point) return

      this.pointTaskDetailInfo.inspectionId = point.inspectionId
      this.pointTaskDetailInfo.inspectionRouteId = point.inspectionRouteId
      this.pointTaskDetailInfo.taskId = point.taskId

      this._loadPointTaskDetailData(this.page.current, this.page.size)
    },
    async _loadPointTaskDetailData(page, size) {
      this.loading = true

      try {
        const params = {
          communityId: this.communityId,
          inspectionId: this.pointTaskDetailInfo.inspectionId,
          inspectionRouteId: this.pointTaskDetailInfo.inspectionRouteId,
          taskId: this.pointTaskDetailInfo.taskId,
          planUserName: this.pointTaskDetailInfo.planUserName,
          inspectionStartTime: this.pointTaskDetailInfo.inspectionStartTime,
          inspectionEndTime: this.pointTaskDetailInfo.inspectionEndTime,
          page: page,
          row: size
        }

        const response = await listInspectionTaskDetails(params)
        this.pointTaskDetailInfo.details = response.inspectionTaskDetails
        this.page.total = response.total
      } catch (error) {
        console.error('获取巡检明细失败:', error)
        this.$message.error(this.$t('pointTaskDetail.fetchError'))
      } finally {
        this.loading = false
      }
    },
    _qureyPointTaskDetail() {
      this.page.current = 1
      this._loadPointTaskDetailData(this.page.current, this.page.size)
    },
    handleSizeChange(size) {
      this.page.size = size
      this._loadPointTaskDetailData(this.page.current, size)
    },
    handleCurrentChange(page) {
      this.page.current = page
      this._loadPointTaskDetailData(page, this.page.size)
    }
  }
}
</script>

<style scoped>
.point-task-detail-container {
  padding: 20px;
}

.margin-top-lg {
  margin-top: 20px;
}

.margin-top {
  margin-top: 20px;
}

.text-primary {
  color: #409EFF;
}

.text-danger {
  color: #F56C6C;
}
</style>