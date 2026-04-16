<template>
  <div class="route-task-container">
    <el-row :gutter="20">
      <el-col :span="6" class="task-list-col">
        <el-card class="task-list-card">
          <ul class="task-ul">
            <li v-for="(task, index) in routeTaskInfo.tasks" :key="index"
              :class="{ 'active-task': task.taskId == routeTaskInfo.taskId }" @click="_switchRouteTask(task)">
              <div class="task-user">{{ task.planUserName }}({{ task.inspectionPlanName }})</div>
              <div class="task-time">{{ task.planInsTime }}</div>
            </li>
          </ul>
        </el-card>
      </el-col>
      <el-col :span="18" class="task-map-col">
        <inspection-task-map ref="inspectionTaskMap"/>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import { getCommunityId } from '@/api/community/communityApi'
import { queryRouteInspectionTask } from '@/api/inspection/inspectionRouteApi'
import InspectionTaskMap from './inspectionTaskMap'

export default {
  name: 'RouteTask',
  components: {
    InspectionTaskMap
  },
  data() {
    return {
      routeTaskInfo: {
        tasks: [],
        taskId: '',
        inspectionRouteId: ''
      },
      communityId: ''
    }
  },
  created() {
    this.communityId = getCommunityId()
  },
  beforeDestroy() {
  },
  methods: {
    open(params) {
      this.routeTaskInfo.inspectionRouteId = params.inspectionRouteId
      this._loadRouteTaskData()
    },
    loadData(params) {
      this.routeTaskInfo.inspectionRouteId = params.inspectionRouteId
      this._loadRouteTaskData()
    },
    handleSwitch(params) {
      this.routeTaskInfo.inspectionRouteId = params.inspectionRouteId
      this._loadRouteTaskData()
    },
    async _loadRouteTaskData() {
      try {
        const params = {
          communityId: this.communityId,
          inspectionRouteId: this.routeTaskInfo.inspectionRouteId,
          page: 1,
          row: 100
        }
        const { data } = await queryRouteInspectionTask(params)
        this.routeTaskInfo.tasks = data
        if (data && data.length > 0) {
          this._switchRouteTask(data[0])
        }
      } catch (error) {
        console.error('获取巡检任务失败:', error)
      }
    },
    _switchRouteTask(task) {
      this.routeTaskInfo.taskId = task.taskId
      this.routeTaskInfo.inspectionRouteId = task.inspectionRouteId

      // 通知地图组件加载任务
      setTimeout(() => {
        if (this.$refs.inspectionTaskMap) {
          this.$refs.inspectionTaskMap.loadData(task)
        }
      }, 500)
    }
  }
}
</script>

<style lang="scss" scoped>
.route-task-container {
  padding: 20px;
  height: 100%;

  .task-list-col {
    height: 100%;

    .task-list-card {
      height: 100%;
      overflow-y: auto;

      .task-ul {
        list-style: none;
        padding: 0;
        margin: 0;

        li {
          padding: 10px;
          cursor: pointer;
          transition: all 0.3s;

          &:hover {
            background-color: #f5f5f5;
          }

          &.active-task {
            background-color: #409eff;
            color: white;
          }

          .task-user {
          }

          .task-time {
            font-size: 12px;
            color: inherit;
          }
        }
      }
    }
  }

  .task-map-col {
    height: 100%;
  }
}
</style>