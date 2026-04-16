<template>
  <div class="a-work-detail-task-item">
    <el-row :gutter="20">
      <el-col :span="4" class="task-list">
        <div class="task-item" v-for="(item, index) in taskList" :key="index" @click="handleTaskClick(item)"
          :class="{ 'active': activeTaskId === item.taskId }">
          <div>{{ item.staffName }}</div>
          <div>({{ formatDate(item.startTime) }} ~ {{ formatDate(item.endTime) }})</div>
        </div>
      </el-col>
      <el-col :span="20">
        <el-table :data="tableData" border style="width: 100%">
          <el-table-column prop="content" :label="$t('adminWorkDetail.processContent')" width="400" />
          <el-table-column prop="finishTime" :label="$t('adminWorkDetail.finishTime')" align="center" />
          <el-table-column :label="$t('adminWorkDetail.stateName')" align="center">
            <template slot-scope="scope">
              <span v-if="scope.row.state === 'CC'">{{ $t('adminWorkDetail.copyProcessed') }}</span>
              <span v-else-if="scope.row.state === 'C'">{{ $t('adminWorkDetail.processed') }}</span>
              <span v-else>{{ $t('adminWorkDetail.pending') }}</span>
            </template>
          </el-table-column>
          <el-table-column prop="remark" :label="$t('adminWorkDetail.remark')" align="center" />
          <el-table-column :label="$t('adminWorkDetail.attachment')" align="center">
            <template slot-scope="scope">
              <el-link v-if="scope.row.pathUrl" :href="scope.row.pathUrl" target="_blank">
                {{ $t('common.download') }}
              </el-link>
              <span v-else>-</span>
            </template>
          </el-table-column>
          <el-table-column prop="score" :label="$t('adminWorkDetail.score')" align="center" />
          <el-table-column prop="deductionMoney" :label="$t('adminWorkDetail.deductionMoney')" align="center" />
          <el-table-column prop="deductionReason" :label="$t('adminWorkDetail.deductionReason')" align="center" />
          <el-table-column prop="deductionPersonName" :label="$t('adminWorkDetail.deductionPerson')" align="center" />
          <el-table-column prop="createTime" :label="$t('adminWorkDetail.createTime')" align="center" />
        </el-table>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import { listAdminWorkTask, listAdminWorkTaskItem } from '@/api/work/adminWorkDetailApi'
import {dateFormat} from '@/utils/dateUtil'

export default {
  name: 'AWorkDetailTaskItem',
  props: {
    workId: {
      type: String,
      required: true
    },
    contents: {
      type: Array,
      default: () => []
    }
  },
  data() {
    return {
      taskList: [],
      tableData: [],
      activeTaskId: '',
      loading: false
    }
  },
  methods: {
    formatDate(date) {
      if (!date) return ''
      return dateFormat(date)
    },
    loadTaskList() {
      this.loading = true
      const params = {
        workId: this.workId,
        page: 1,
        row: 100
      }

      listAdminWorkTask(params)
        .then(response => {
          this.taskList = response.data
          if (this.taskList.length > 0) {
            this.handleTaskClick(this.taskList[0])
          }
        })
        .catch(error => {
          this.$message.error(this.$t('adminWorkDetail.fetchError'))
          console.error(error)
        })
        .finally(() => {
          this.loading = false
        })
    },
    handleTaskClick(task) {
      this.activeTaskId = task.taskId
      this.loadTaskItems(task.taskId)
    },
    loadTaskItems(taskId) {
      this.loading = true
      const params = {
        workId: this.workId,
        taskId: taskId,
        page: 1,
        row: 100
      }

      listAdminWorkTaskItem(params)
        .then(response => {
          this.tableData = response.data
        })
        .catch(error => {
          this.$message.error(this.$t('adminWorkDetail.fetchError'))
          console.error(error)
        })
        .finally(() => {
          this.loading = false
        })
    },
    loadData() {
      this.loadTaskList()
    }
  },
}
</script>

<style lang="scss" scoped>
.a-work-detail-task-item {
  padding: 20px;

  .task-list {
    border-right: 1px solid #ebeef5;
    padding-right: 20px;

    .task-item {
      padding: 10px;
      margin-bottom: 10px;
      border: 1px solid #ebeef5;
      border-radius: 4px;
      cursor: pointer;

      &:hover {
        background-color: #f5f7fa;
      }

      &.active {
        background-color: #ecf5ff;
        border-color: #d9ecff;
      }
    }
  }
}
</style>