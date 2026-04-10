<template>
  <div class="task-container">
    <el-row :gutter="20">
      <el-col :span="6">
        <el-card class="task-list">
          <el-scrollbar>
            <ul class="task-ul">
              <li v-for="(task, index) in tasks" :key="index" :class="{ 'active': task.taskId === currentTaskId }"
                @click="switchTask(task)">
                <div>{{ task.planUserName }}({{ task.inspectionPlanName }})</div>
                <div>{{ task.planInsTime }}</div>
              </li>
            </ul>
          </el-scrollbar>
        </el-card>
      </el-col>
      <el-col :span="18">
        <a-inspection-task-map ref="taskMap" />
      </el-col>
    </el-row>
  </div>
</template>

<script>
import { queryAdminRouteInspectionTask } from '@/api/inspection/aInspectionPlanDetailApi'
import AInspectionTaskMap from './AInspectionTaskMap'

export default {
  name: 'AdminRouteTask',
  components: {
    AInspectionTaskMap
  },
  data() {
    return {
      loading: false,
      tasks: [],
      currentTaskId: '',
      inspectionRouteId: ''
    }
  },
  methods: {
    async loadData(params) {
      this.inspectionRouteId = params.inspectionRouteId
      await this.getTaskList()
    },
    async getTaskList() {
      try {
        this.loading = true
        const params = {
          inspectionRouteId: this.inspectionRouteId,
          page: 1,
          row: 100
        }
        const { data } = await queryAdminRouteInspectionTask(params)
        this.tasks = data
        if (this.tasks.length > 0) {
          this.switchTask(this.tasks[0])
        }
      } catch (error) {
        this.$message.error(error)
      } finally {
        this.loading = false
      }
    },
    switchTask(task) {
      this.currentTaskId = task.taskId
      this.$refs.taskMap.initMap({
        taskId: task.taskId
      })
    }
  }
}
</script>

<style scoped>
.task-container {
  padding: 20px;
}

.task-list {
  height: 600px;
}

.task-ul {
  list-style: none;
  padding: 0;
  margin: 0;
}

.task-ul li {
  padding: 10px;
  cursor: pointer;
  border-bottom: 1px solid #eee;
}

.task-ul li:hover {
  background-color: #f5f5f5;
}

.task-ul li.active {
  background-color: #409EFF;
  color: white;
}
</style>