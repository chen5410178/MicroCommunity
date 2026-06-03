<template>
  <div class="point-task-container">
    <el-row :gutter="20" v-if="pointTaskInfo.tasks && pointTaskInfo.tasks.length>0">
      <el-col :span="6" class="left-panel">
        <el-card class="task-card">
          <div class="task-list-wrapper">
            <ul class="task-ul">
              <li v-for="(task, index) in pointTaskInfo.tasks" :key="index" @click="_switchPointTask(task)"
                :class="{ 'selected': task.inspectionRouteId == pointTaskInfo.inspectionRouteId }"
                class="task-list-item">
                {{ task.planUserName }}({{ task.inspectionPlanName }})
              </li>
            </ul>
          </div>
        </el-card>
      </el-col>
      <el-col :span="18" class="right-panel">
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

<style lang="scss" scoped>
.point-task-container {
  padding: 20px;
}

.left-panel {
  .task-card {
    height: 700px;

    .task-list-wrapper {
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

      .task-ul {
        list-style: none;
        padding: 0 5px;
        margin: 0;

        .task-list-item {
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
}

.right-panel {
  padding-left: 5px;
}
</style>