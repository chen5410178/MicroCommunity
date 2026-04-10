<template>
  <div class="task-container ">
    <!-- 查询条件 -->
    <div class="search-wrapper">
      <div class="search-title">{{ $t('task.search.title') }}</div>
      <div class="search-content">
        <el-input v-model="searchForm.taskName" :placeholder="$t('task.search.taskName')" class="search-item" clearable
          @keyup.enter.native="handleSearch" />

        <el-button type="primary" @click="handleSearch">{{ $t('common.search') }}</el-button>
      </div>
    </div>

    <div class="list-wrapper">
      <div class="list-header">
        <div class="list-title">{{ $t('task.list.title') }}</div>
        <el-button type="primary" @click="handleAdd">{{ $t('common.add') }}</el-button>
      </div>

      <el-table v-loading="loading" :data="tableData" border style="width: 100%">
        <el-table-column prop="taskId" :label="$t('task.table.taskId')" />
        <el-table-column prop="taskName" :label="$t('task.table.taskName')" />
        <el-table-column prop="templateName" :label="$t('task.table.templateId')" />
        <el-table-column prop="taskCron" :label="$t('task.table.taskCron')" />
        <el-table-column :label="$t('task.table.state')">
          <template slot-scope="scope">
            <span v-if="scope.row.state == '001'">STOP</span>
            <span v-else>RUN</span>
          </template>
        </el-table-column>
        <el-table-column prop="createTime" :label="$t('task.table.createTime')" />
        <el-table-column :label="$t('common.operation')" width="200" fixed="right">
          <template slot-scope="scope">
            <el-button size="mini" type="primary" @click="handleEdit(scope.row)">{{ $t('common.edit') }}</el-button>
            <el-button size="mini" type="danger" @click="handleDelete(scope.row)">{{ $t('common.delete') }}</el-button>
            <el-button size="mini" type="warn" @click="handleStart(scope.row)" v-if="scope.row.state == '001'">{{
              $t('task.start') }}</el-button>
            <el-button size="mini" type="warn" @click="handleStop(scope.row)" v-else>{{ $t('task.stop') }}</el-button>
            <el-button size="mini" type="warn" @click="handleTest(scope.row)" v-if="scope.row.state == '002'">Test</el-button>
          </template>
        </el-table-column>
      </el-table>

      <el-pagination :current-page.sync="page.current" :page-sizes="[10, 20, 30, 50]" :page-size="page.size"
        :total="page.total" layout="total, sizes, prev, pager, next, jumper" @size-change="handleSizeChange"
        @current-change="handleCurrentChange" />
    </div>
    <AddTask :visible.sync="addVisible" @success="handleSuccess" />

    <EditTask :visible.sync="editVisible" :form-data="currentRow" @success="handleSuccess" />

    <DelTask :visible.sync="delVisible" :task-id="currentRow.taskId" @success="handleSuccess" />
  </div>
</template>

<script>
import { getTaskList, startTask, stopTask ,runTask} from '@/api/dev/taskApi'
import AddTask from '@/components/dev/AddTask'
import EditTask from '@/components/dev/EditTask'
import DelTask from '@/components/dev/DelTask'

export default {
  name: 'taskList',
  components: {
    AddTask,
    EditTask,
    DelTask
  },
  data() {
    return {
      loading: false,
      searchForm: {
        taskName: '',
        templateId: '',
        taskCron: '',
        state: '',
      },
      tableData: [],
      page: {
        current: 1,
        size: 10,
        total: 0
      },
      addVisible: false,
      editVisible: false,
      delVisible: false,
      currentRow: {}
    }
  },
  created() {
    this.getList()
  },
  methods: {
    async getList() {
      try {
        this.loading = true
        const params = {
          page: this.page.current,
          row: this.page.size,
          taskName: this.searchForm.taskName,
          templateId: this.searchForm.templateId,
          taskCron: this.searchForm.taskCron,
          state: this.searchForm.state,
          createTime: this.searchForm.createTime,
        }
        const { data, total } = await getTaskList(params)
        this.tableData = data
        this.page.total = total
      } catch (error) {
        this.$message.error(this.$t('task.fetchError'))
      } finally {
        this.loading = false
      }
    },
    async handleStart(row) {
      try {
        this.loading = true
        await startTask(row.taskId)
        this.getList()
      } catch (error) {
        this.$message.error(this.$t('task.fetchError'))
      } finally {
        this.loading = false
      }
    },
    async handleStop(row) {
      try {
        this.loading = true
        await stopTask(row.taskId)
        this.getList()
      } catch (error) {
        this.$message.error(this.$t('task.fetchError'))
      } finally {
        this.loading = false
      }
    },
    handleSearch() {
      this.page.current = 1
      this.getList()
    },
    handleAdd() {
      this.addVisible = true
    },
    handleEdit(row) {
      this.currentRow = { ...row }
      this.editVisible = true
    },
    handleDelete(row) {
      this.currentRow = { ...row }
      this.delVisible = true
    },
    async handleTest(row) {
      try {
        this.loading = true
        await runTask(row.taskId)
        this.getList()
      } catch (error) {
        this.$message.error(this.$t('task.fetchError'))
      } finally {
        this.loading = false
      }
    },
    handleSuccess() {
      this.getList()
    },
    handleSizeChange(val) {
      this.page.size = val
      this.getList()
    },
    handleCurrentChange(val) {
      this.page.current = val
      this.getList()
    }
  }
}
</script>

<style lang="scss" scoped>
.task-container {
  padding: 20px;
  margin: 0;

  .search-wrapper {
    background-color: #fff;
    padding: 20px;
    margin-bottom: 20px;
    border-radius: 4px;

    .search-title {
      font-size: 16px;
      font-weight: bold;
      margin-bottom: 20px;
      text-align: left;
      color: #333;
    }

    .search-content {
      display: flex;
      align-items: center;
      gap: 20px;

      .search-item {
        width: 200px;
      }
    }
  }

  .list-wrapper {
    background-color: #fff;
    padding: 20px;
    border-radius: 4px;

    .list-header {
      display: flex;
      justify-content: space-between;
      align-items: center;
      margin-bottom: 20px;

      .list-title {
        font-size: 16px;
        font-weight: bold;
        color: #333;
      }
    }

    .el-pagination {
      margin-top: 20px;
      text-align: right;
    }
  }
}
</style>