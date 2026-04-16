<template>
  <el-dialog :title="$t('maintainanceTaskDetail.title')" :visible.sync="visible" width="90%" top="5vh"
    @close="handleClose">
    <el-table :data="taskDetails" border style="width: 100%" v-loading="loading">
      <el-table-column prop="machineName" :label="$t('maintainanceTaskDetail.machineName')" align="center" />
      <el-table-column prop="planName" :label="$t('maintainanceTaskDetail.planName')" align="center" />
      <el-table-column prop="standardName" :label="$t('maintainanceTaskDetail.standardName')" align="center" />
      <el-table-column prop="planUserName" :label="$t('maintainanceTaskDetail.planUserName')" align="center" />
      <el-table-column :label="$t('maintainanceTaskDetail.timeRange')" align="center">
        <template slot-scope="scope">
          {{ scope.row.planInsTime }}<br />{{ scope.row.planEndTime }}
        </template>
      </el-table-column>
      <el-table-column :label="$t('maintainanceTaskDetail.actInsTime')" align="center">
        <template slot-scope="scope">
          {{ scope.row.inspectionTime || '-' }}
        </template>
      </el-table-column>
      <el-table-column :label="$t('maintainanceTaskDetail.actUserName')" align="center">
        <template slot-scope="scope">
          {{ scope.row.actUserName || '-' }}
        </template>
      </el-table-column>
      <el-table-column prop="stateName" :label="$t('maintainanceTaskDetail.stateName')" align="center" />
      <el-table-column :label="$t('maintainanceTaskDetail.description')" align="center">
        <template slot-scope="scope">
          <span class="text-primary">
            {{ scope.row.description || '-' }}
          </span>
        </template>
      </el-table-column>
      <el-table-column :label="$t('maintainanceTaskDetail.photos')" align="center">
        <template slot-scope="scope">
          <div v-if="scope.row.photos && scope.row.photos.length > 0" class="photo-container">
            <el-image v-for="(photo, index) in scope.row.photos" :key="index" :src="photo.url"
              :preview-src-list="scope.row.photos.map(p => p.url)" style="width: 60px; height: 60px; margin-right: 5px;"
              fit="cover" />
          </div>
          <span v-else>-</span>
        </template>
      </el-table-column>
      <el-table-column prop="createTime" :label="$t('maintainanceTaskDetail.createTime')" align="center" />
      <el-table-column :label="$t('common.operation')" align="center" width="100">
        <template slot-scope="scope">
          <el-button type="text" @click="handleViewDetail(scope.row.taskDetailId)">
            {{ $t('common.detail') }}
          </el-button>
        </template>
      </el-table-column>
    </el-table>

    <div class="pagination-wrapper">
      <el-pagination :current-page="pagination.current" :page-sizes="[10, 20, 30, 50]" :page-size="pagination.size"
        :total="pagination.total" layout="total, sizes, prev, pager, next, jumper" @size-change="handleSizeChange"
        @current-change="handleCurrentChange" />
    </div>
  </el-dialog>
</template>

<script>
import { listMaintainanceTaskDetail } from '@/api/inspection/maintainanceTaskManageApi'
import { getCommunityId } from '@/api/community/communityApi'

export default {
  name: 'MaintainanceTaskDetail',
  data() {
    return {
      visible: false,
      loading: false,
      taskDetails: [],
      taskId: '',
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
  },
  methods: {
    open(data) {
      this.taskId = data.taskId
      this.visible = true
      this.loadTaskDetails()
    },
    handleClose() {
      this.taskDetails = []
      this.pagination.current = 1
    },
    loadTaskDetails() {
      this.loading = true
      const params = {
        page: this.pagination.current,
        row: this.pagination.size,
        taskId: this.taskId,
        communityId: this.communityId
      }

      listMaintainanceTaskDetail(params)
        .then(response => {
          this.taskDetails = response.data || []
          this.pagination.total = response.total || 0
        })
        .catch(error => {
          console.error('获取任务详情失败:', error)
          this.$message.error(this.$t('maintainanceTaskDetail.fetchError'))
        })
        .finally(() => {
          this.loading = false
        })
    },
    handleSizeChange(val) {
      this.pagination.size = val
      this.loadTaskDetails()
    },
    handleCurrentChange(val) {
      this.pagination.current = val
      this.loadTaskDetails()
    },
    handleViewDetail(taskDetailId) {
      window.open(`/#/views/inspection/maintainanceTaskDetailView?taskDetailId=${taskDetailId}`, '_blank')
    }
  }
}
</script>

<style scoped>
.photo-container {
  display: flex;
  flex-wrap: wrap;
  justify-content: center;
}

.text-primary {
  color: #409EFF;
}

.pagination-wrapper {
  margin-top: 20px;
  text-align: right;
}
</style>