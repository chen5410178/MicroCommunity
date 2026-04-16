<template>
  <div class="point-task-container">
    <el-row :gutter="20" v-if="pointTaskInfo.tasks && pointTaskInfo.tasks.length>0">
      <el-col :span="6">
        <el-card class="task-list-card">
          <div class="task-list">
            <ul>
              <li v-for="(task, index) in pointTaskInfo.tasks" :key="index" @click="_switchPointTask(task)"
                :class="{ 'active': task.inspectionRouteId == pointTaskInfo.inspectionRouteId }">
                {{ task.planUserName }}({{ task.inspectionPlanName }})
              </li>
            </ul>
          </div>
        </el-card>
      </el-col>
      <el-col :span="18">
        <el-card>
          <inspection-task-map ref="inspectionTaskMap" />
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import { queryPointInspectionTask } from '@/api/inspection/inspectionPointApi'
import { getCommunityId } from '@/api/community/communityApi'
import InspectionTaskMap from '@/components/inspection/inspectionTaskMap'

export default {
  name: 'PointTask',
  components: {
    InspectionTaskMap
  },
  data() {
    return {
      pointTaskInfo: {
        tasks: [],
        inspectionId: '',
        taskId: '',
        inspectionRouteId: ''
      },
      communityId: ''
    }
  },
  created() {
    this.communityId = getCommunityId()
  },
  methods: {
    loadData(point) {
      if (!point) return

      this.pointTaskInfo.inspectionId = point.inspectionId
      this._loadPointTaskData()
    },

    async _loadPointTaskData() {
      try {
        const params = {
          communityId: this.communityId,
          inspectionId: this.pointTaskInfo.inspectionId,
          page: 1,
          row: 100
        }

        const {data} = await queryPointInspectionTask(params)
        this.pointTaskInfo.tasks = data

        if (data && data.length > 0) {
          this._switchPointTask(data[0])
        }
      } catch (error) {
        console.error('获取巡检任务失败:', error)
        this.$message.error(this.$t('pointTask.fetchError'))
      }
    },

    _switchPointTask(task) {
      this.pointTaskInfo.taskId = task.taskId
      this.pointTaskInfo.inspectionRouteId = task.inspectionRouteId

      // 通知地图组件加载任务
      this.$nextTick(() => {
        if (this.$refs.inspectionTaskMap) {
          this.$refs.inspectionTaskMap.loadData(task)
        }
      })
    }
  }
}
</script>

<style scoped>
.point-task-container {
  padding: 20px;
}

.task-list-card {
  height: 600px;
  overflow: hidden;
}

.task-list {
  height: 550px;
  overflow-y: auto;
}

.task-list ul {
  list-style: none;
  padding: 0;
  margin: 0;
}

.task-list li {
  padding: 12px 15px;
  border-bottom: 1px solid #ebeef5;
  cursor: pointer;
  transition: all 0.3s;
}

.task-list li:hover {
  background-color: #f5f7fa;
}

.task-list li.active {
  background-color: #ecf5ff;
  color: #409eff;
  font-weight: bold;
}
</style>