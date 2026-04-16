<template>
  <el-dialog :title="$t('inspectionTaskDetail.taskDetails')" :visible.sync="visible" width="90%" top="5vh"
    @close="close">

      <el-table :data="taskDetails" border style="width: 100%">
        <el-table-column prop="taskDetailId" :label="$t('inspectionTaskDetail.taskDetails') + 'ID'" align="center"
          width="100" />
        <el-table-column prop="inspectionName" :label="$t('inspectionTaskDetail.inspectionPoint')" align="center" />
        <el-table-column prop="stateName" :label="$t('inspectionTaskDetail.inspectionStatus')" align="center" />
        <el-table-column :label="$t('inspectionTaskDetail.signStatus')" align="center">
          <template slot-scope="{row}">
            <span v-if="row.inspectionState === '60000'" class="text-primary">
              {{ row.inspectionStateName || '-' }}
            </span>
            <span v-else class="text-danger font-bold">
              {{ row.inspectionStateName || '-' }}
            </span>
          </template>
        </el-table-column>
        <el-table-column :label="$t('inspectionTaskDetail.inspectorTime')" align="center">
          <template slot-scope="{row}">
            {{ row.planInsTime }}<br>{{ row.planEndTime }}
          </template>
        </el-table-column>
        <el-table-column :label="$t('inspectionTaskDetail.pointTime')" align="center">
          <template slot-scope="{row}">
            {{ row.pointStartTime }}<br>{{ row.pointEndTime }}
          </template>
        </el-table-column>
        <el-table-column :label="$t('inspectionTaskDetail.actualTime')" align="center">
          <template slot-scope="{row}">
            {{ row.inspectionTime || '-' }}
          </template>
        </el-table-column>
        <el-table-column :label="$t('inspectionTaskDetail.inspectionSituation')" align="center">
          <template slot-scope="{row}">
            {{ row.description || '-' }}
          </template>
        </el-table-column>
        <el-table-column :label="$t('inspectionTaskDetail.inspectionPhotos')" align="center">
          <template slot-scope="{row}">
            <span v-for="photo in row.photos" :key="photo.url" class="photo-item">
              <el-image style="width: 60px; height: 60px; margin-right: 5px;" :src="photo.url"
                :preview-src-list="[photo.url]" fit="cover" />
            </span>
          </template>
        </el-table-column>
        <el-table-column :label="$t('inspectionTaskDetail.locationInfo')" align="center" width="120">
          <template slot-scope="{row}">
            <el-button type="info" size="mini" @click="openMap(row.latitude, row.longitude)">
              {{ $t('inspectionTaskDetail.view') }}
            </el-button>
          </template>
        </el-table-column>
      </el-table>

      <el-pagination :current-page="currentPage" :page-size="pageSize" :total="total"
        layout="total, prev, pager, next, jumper" @current-change="handlePageChange" />


    <ViewMap ref="viewMap" />
    <ViewImage ref="viewImage" />
  </el-dialog>
</template>

<script>
import ViewMap from '@/components/system/ViewMap'
import ViewImage from '@/components/system/viewImage'
import { listInspectionTaskDetails } from '@/api/inspection/inspectionTaskApi'

export default {
  name: 'InspectionTaskDetail',
  components: { ViewMap, ViewImage },
  data() {
    return {
      visible: false,
      taskDetails: [],
      taskId: '',
      currentPage: 1,
      pageSize: 10,
      total: 0
    }
  },
  methods: {
    open(task) {
      this.taskId = task.taskId
      this.visible = true
      this.loadTaskDetails()
    },
    close() {
      this.taskDetails = []
      this.taskId = ''
      this.currentPage = 1
      this.total = 0
    },
    async loadTaskDetails() {
      try {
        const res = await listInspectionTaskDetails({
          page: this.currentPage,
          row: this.pageSize,
          taskId: this.taskId
        })
        this.taskDetails = res.inspectionTaskDetails || []
        this.total = res.total || 0
      } catch (error) {
        console.error('加载任务详情失败:', error)
        this.$message.error('加载任务详情失败')
      }
    },
    handlePageChange(page) {
      this.currentPage = page
      this.loadTaskDetails()
    },
    openMap(lat, lng) {
      if (!lat || !lng) {
        this.$message.warning(this.$t('inspectionTaskDetail.noLocation'))
        return
      }
      this.$refs.viewMap.open({ lat, lng })
    }
  }
}
</script>

<style scoped>
.photo-item {
  display: inline-block;
  margin-right: 5px;
}

.text-primary {
  color: #409EFF;
}

.text-danger {
  color: #F56C6C;
}

.font-bold {
  font-weight: bold;
}
</style>