<template>
  <div class="work-detail-task">
    <el-row :gutter="20" class="margin-top-lg">
      <el-col :span="6">
        <el-input
          v-model.trim="workDetailTaskInfo.staffNameLike"
          :placeholder="$t('workDetailTask.staffNamePlaceholder')"
          clearable
        />
      </el-col>
      <el-col :span="6">
        <el-date-picker
          v-model="workDetailTaskInfo.queryStartTime"
          type="datetime"
          :placeholder="$t('workDetailTask.startTimePlaceholder')"
          value-format="yyyy-MM-dd HH:mm:ss"
          style="width: 100%"
        />
      </el-col>
      <el-col :span="6">
        <el-date-picker
          v-model="workDetailTaskInfo.queryEndTime"
          type="datetime"
          :placeholder="$t('workDetailTask.endTimePlaceholder')"
          value-format="yyyy-MM-dd HH:mm:ss"
          style="width: 100%"
        />
      </el-col>
      <el-col :span="6">
        <el-button type="primary" @click="queryWorkDetailTask">
          <i class="el-icon-search"></i>{{ $t('common.search') }}
        </el-button>
        <el-button @click="resetWorkDetailTask">
          <i class="el-icon-refresh"></i>{{ $t('common.reset') }}
        </el-button>
      </el-col>
    </el-row>

    <div class="margin-top">
      <el-table
        :data="workDetailTaskInfo.tasks"
        border
        style="width: 100%"
        v-loading="loading"
      >
        <el-table-column
          prop="taskId"
          :label="$t('workDetailTask.taskId')"
          align="center"
        />
        <el-table-column
          prop="staffName"
          :label="$t('workDetailTask.staffName')"
          align="center"
        />
        <el-table-column
          prop="startTime"
          :label="$t('workDetailTask.startTime')"
          align="center"
        />
        <el-table-column
          prop="endTime"
          :label="$t('workDetailTask.endTime')"
          align="center"
        />
        <el-table-column
          prop="createTime"
          :label="$t('workDetailTask.createTime')"
          align="center"
        />
        <el-table-column
          prop="stateName"
          :label="$t('workDetailTask.stateName')"
          align="center"
        />
        <el-table-column
          :label="$t('common.operation')"
          align="center"
          width="120"
        >
          <template slot-scope="scope">
            <el-button
              size="mini"
              @click="openWorkTaskDetail(scope.row)"
            >
              {{ $t('workDetailTask.progress') }}
            </el-button>
          </template>
        </el-table-column>
      </el-table>

      <el-pagination
        :current-page.sync="page.current"
        :page-sizes="[10, 20, 30, 50]"
        :page-size="page.size"
        :total="page.total"
        layout="total, sizes, prev, pager, next, jumper"
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
      />
    </div>
  </div>
</template>

<script>
import { listWorkTask } from '@/api/oa/workDetailApi'

export default {
  name: 'WorkDetailTask',
  data() {
    return {
      workDetailTaskInfo: {
        tasks: [],
        workId: '',
        staffNameLike: '',
        queryStartTime: '',
        queryEndTime: ''
      },
      loading: false,
      page: {
        current: 1,
        size: 10,
        total: 0
      }
    }
  },
  methods: {
    open(params) {
      this.workDetailTaskInfo.workId = params.workId
      this.loadWorkDetailTaskData(this.page.current, this.page.size)
    },
    async loadWorkDetailTaskData(page, size) {
      try {
        this.loading = true
        const params = {
          workId: this.workDetailTaskInfo.workId,
          staffNameLike: this.workDetailTaskInfo.staffNameLike,
          queryStartTime: this.workDetailTaskInfo.queryStartTime,
          queryEndTime: this.workDetailTaskInfo.queryEndTime,
          page,
          row: size
        }
        const { data, total } = await listWorkTask(params)
        this.workDetailTaskInfo.tasks = data
        this.page.total = total
      } catch (error) {
        console.error('获取任务列表失败:', error)
      } finally {
        this.loading = false
      }
    },
    queryWorkDetailTask() {
      this.page.current = 1
      this.loadWorkDetailTaskData(this.page.current, this.page.size)
    },
    resetWorkDetailTask() {
      this.workDetailTaskInfo.staffNameLike = ''
      this.workDetailTaskInfo.queryStartTime = ''
      this.workDetailTaskInfo.queryEndTime = ''
      this.queryWorkDetailTask()
    },
    openWorkTaskDetail(work) {
      this.$router.push({
        path: '/pages/oa/workTaskDetail',
        query: {
          workId: work.workId,
          taskId: work.taskId
        }
      })
    },
    handleSizeChange(val) {
      this.page.size = val
      this.loadWorkDetailTaskData(this.page.current, this.page.size)
    },
    handleCurrentChange(val) {
      this.page.current = val
      this.loadWorkDetailTaskData(this.page.current, this.page.size)
    }
  }
}
</script>

<style scoped>
.margin-top-lg {
  margin-top: 20px;
}
.margin-top {
  margin-top: 20px;
}
</style>