<template>
  <div class="work-detail-task-item">
    <el-row :gutter="20">
      <el-col :span="4">
        <div class="tree-container">
          <el-scrollbar>
            <ul class="task-list">
              <li v-for="(item, index) in workDetailTaskItemInfo.tasks" :key="index" @click="swatchTaskItemTaskId(item)"
                :class="{ 'active-task': workDetailTaskItemInfo.taskId === item.taskId }">
                <div>{{ item.staffName }}</div>
                <div>({{ formatDate(item.startTime) }} ~ {{ formatDate(item.endTime) }})</div>
              </li>
            </ul>
          </el-scrollbar>
        </div>
      </el-col>
      <el-col :span="20">
        <el-table :data="workDetailTaskItemInfo.items" border style="width: 100%" v-loading="loading">
          <el-table-column prop="content" :label="$t('workDetailTaskItem.content')" width="400" />
          <el-table-column prop="finishTime" :label="$t('workDetailTaskItem.finishTime')" align="center" />
          <el-table-column :label="$t('workDetailTaskItem.state')" align="center">
            <template slot-scope="scope">
              <span v-if="scope.row.state === 'CC'">{{ $t('workDetailTaskItem.copyProcessed') }}</span>
              <span v-else-if="scope.row.state === 'C'">{{ $t('workDetailTaskItem.handlerProcessed') }}</span>
              <span v-else>{{ $t('workDetailTaskItem.pending') }}</span>
            </template>
          </el-table-column>
          <el-table-column prop="remark" :label="$t('workDetailTaskItem.remark')" align="center" />
          <el-table-column :label="$t('workDetailTaskItem.attachment')" align="center">
            <template slot-scope="scope">
              <div v-if="scope.row.pathUrls">
                <div v-for="(url, index) in scope.row.pathUrls" :key="index">
                  <div v-if="url.endsWith('jpg') || url.endsWith('png')">
                    <el-image style="width: 60px; height: 60px;" :src="url" :preview-src-list="[url]" fit="cover" />
                  </div>
                  <div v-else>
                    <el-link :href="url" target="_blank">{{ $t('common.download') }}</el-link>
                  </div>
                </div>
              </div>
              <div v-else>-</div>
            </template>
          </el-table-column>
          <el-table-column prop="score" :label="$t('workDetailTaskItem.score')" align="center" />
          <el-table-column prop="deductionMoney" :label="$t('workDetailTaskItem.deductionMoney')" align="center" />
          <el-table-column prop="deductionReason" :label="$t('workDetailTaskItem.deductionReason')" align="center" />
          <el-table-column prop="deductionPersonName" :label="$t('workDetailTaskItem.deductionPerson')"
            align="center" />
          <el-table-column prop="createTime" :label="$t('workDetailTaskItem.createTime')" align="center" />
        </el-table>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import { listWorkTask, listWorkTaskItem } from '@/api/oa/workDetailApi'
import { dateFormat } from '@/utils/dateUtil'

export default {
  name: 'WorkDetailTaskItem',
  data() {
    return {
      workDetailTaskItemInfo: {
        tasks: [],
        workId: '',
        taskId: '',
        items: []
      },
      loading: false
    }
  },
  methods: {
    open(params) {
      this.workDetailTaskItemInfo.workId = params.workId
      this.queryWorkDetailTaskData()
    },
    async queryWorkDetailTaskData() {
      try {
        this.loading = true
        const params = {
          workId: this.workDetailTaskItemInfo.workId,
          page: 1,
          row: 100
        }
        const { data } = await listWorkTask(params)
        this.workDetailTaskItemInfo.tasks = data
        if (data.length > 0) {
          this.swatchTaskItemTaskId(data[0])
        }
      } catch (error) {
        console.error('获取任务列表失败:', error)
      } finally {
        this.loading = false
      }
    },
    async loadWorkDetailTaskItemData() {
      try {
        this.loading = true
        const params = {
          workId: this.workDetailTaskItemInfo.workId,
          taskId: this.workDetailTaskItemInfo.taskId,
          page: 1,
          row: 100
        }
        const { data } = await listWorkTaskItem(params)
        this.workDetailTaskItemInfo.items = data
      } catch (error) {
        console.error('获取任务项列表失败:', error)
      } finally {
        this.loading = false
      }
    },
    swatchTaskItemTaskId(task) {
      this.workDetailTaskItemInfo.taskId = task.taskId
      this.loadWorkDetailTaskItemData()
    },
    formatDate(date) {
      return dateFormat(date)
    }
  }
}
</script>

<style scoped>
.tree-container {
  height: 500px;
  border: 1px solid #ebeef5;
  border-radius: 4px;
  padding: 10px;
}

.task-list {
  list-style: none;
  padding: 0;
  margin: 0;
}

.task-list li {
  padding: 10px;
  margin-bottom: 5px;
  border-radius: 4px;
  cursor: pointer;
  text-align: center;
}

.task-list li:hover {
  background-color: #f5f7fa;
}

.active-task {
  background-color: #ecf5ff;
  color: #409eff;
  border: 1px solid #d9ecff;
}
</style>