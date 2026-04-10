<template>
  <div class="a-work-detail-task">
    <el-row :gutter="20" class="search-row">
      <el-col :span="6">
        <el-input v-model="searchForm.staffNameLike" :placeholder="$t('adminWorkDetail.processor')" clearable />
      </el-col>
      <el-col :span="6">
        <el-date-picker v-model="searchForm.queryStartTime" type="datetime" :placeholder="$t('adminWorkDetail.startTime')"
          value-format="yyyy-MM-dd HH:mm:ss" />
      </el-col>
      <el-col :span="6">
        <el-date-picker v-model="searchForm.queryEndTime" type="datetime" :placeholder="$t('adminWorkDetail.endTime')"
          value-format="yyyy-MM-dd HH:mm:ss" />
      </el-col>
      <el-col :span="6">
        <el-button type="primary" @click="handleSearch">{{ $t('common.search') }}</el-button>
        <el-button @click="handleReset">{{ $t('common.reset') }}</el-button>
      </el-col>
    </el-row>

    <el-table :data="tableData" border style="width: 100%">
      <el-table-column prop="taskId" :label="$t('adminWorkDetail.taskId')" align="center" />
      <el-table-column prop="staffName" :label="$t('adminWorkDetail.processor')" align="center" />
      <el-table-column prop="startTime" :label="$t('adminWorkDetail.startTime')" align="center" />
      <el-table-column prop="endTime" :label="$t('adminWorkDetail.endTime')" align="center" />
      <el-table-column prop="createTime" :label="$t('adminWorkDetail.createTime')" align="center" />
      <el-table-column prop="stateName" :label="$t('adminWorkDetail.stateName')" align="center" />
    </el-table>

    <el-pagination :current-page="pagination.current" :page-sizes="[10, 20, 30, 50]" :page-size="pagination.size"
      :total="pagination.total" layout="total, sizes, prev, pager, next, jumper" @size-change="handleSizeChange"
      @current-change="handleCurrentChange" />
  </div>
</template>

<script>
import { listAdminWorkTask } from '@/api/work/adminWorkDetailApi'

export default {
  name: 'AWorkDetailTask',
  props: {
    workId: {
      type: String,
      required: true
    }
  },
  data() {
    return {
      searchForm: {
        staffNameLike: '',
        queryStartTime: '',
        queryEndTime: ''
      },
      tableData: [],
      pagination: {
        current: 1,
        size: 10,
        total: 0
      },
      loading: false
    }
  },
  methods: {
    loadData() {
      this.loading = true
      const params = {
        workId: this.workId,
        staffNameLike: this.searchForm.staffNameLike,
        queryStartTime: this.searchForm.queryStartTime,
        queryEndTime: this.searchForm.queryEndTime,
        page: this.pagination.current,
        row: this.pagination.size
      }

      listAdminWorkTask(params)
        .then(response => {
          this.tableData = response.data
          this.pagination.total = response.total
        })
        .catch(error => {
          this.$message.error(this.$t('adminWorkDetail.fetchError'))
          console.error(error)
        })
        .finally(() => {
          this.loading = false
        })
    },
    handleSearch() {
      this.pagination.current = 1
      this.loadData()
    },
    handleReset() {
      this.searchForm = {
        staffNameLike: '',
        queryStartTime: '',
        queryEndTime: ''
      }
      this.handleSearch()
    },
    handleSizeChange(val) {
      this.pagination.size = val
      this.loadData()
    },
    handleCurrentChange(val) {
      this.pagination.current = val
      this.loadData()
    }
  },
}
</script>

<style lang="scss" scoped>
.a-work-detail-task {
  padding: 20px;

  .search-row {
    margin-bottom: 20px;

    .el-col {
      display: flex;
      align-items: center;

      .el-button {
        margin-left: 10px;
      }
    }
  }

  .el-pagination {
    margin-top: 20px;
    text-align: right;
  }
}
</style>