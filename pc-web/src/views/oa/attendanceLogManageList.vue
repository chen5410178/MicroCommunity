<template>
  <div class="attendance-log-manage-container">
    <!-- 查询条件 -->
    <el-card class="search-wrapper">
      <div slot="header" class="flex justify-between">
        <span>{{ $t('attendanceLogManage.search.title') }}</span>
      </div>
      <el-row :gutter="20">
        <el-col :span="4">
          <el-input v-model="searchForm.classesName" :placeholder="$t('attendanceLogManage.search.classesName')"
            clearable />
        </el-col>
        <el-col :span="4">
          <el-input v-model="searchForm.departmentName" :placeholder="$t('attendanceLogManage.search.departmentName')"
            clearable />
        </el-col>
        <el-col :span="4">
          <el-date-picker v-model="searchForm.date" type="date" :placeholder="$t('attendanceLogManage.search.date')"
            value-format="yyyy-MM-dd" style="width: 100%" />
        </el-col>
        <el-col :span="4">
          <el-input v-model="searchForm.staffName" :placeholder="$t('attendanceLogManage.search.staffName')"
            clearable />
        </el-col>
        <el-col :span="4">
          <el-button type="primary" @click="handleSearch">{{ $t('common.search') }}</el-button>
          <el-button @click="handleReset">{{ $t('common.reset') }}</el-button>
        </el-col>
      </el-row>
    </el-card>

    <!-- 考勤记录列表 -->
    <el-card class="list-wrapper">
      <div slot="header" class="flex justify-between">
        <span>{{ $t('attendanceLogManage.list.title') }}</span>
      </div>
      <el-table v-loading="loading" :data="tableData" border style="width: 100%">
        <el-table-column :label="$t('attendanceLogManage.table.face')" align="center" width="100">
          <template slot-scope="scope">
            <el-image style="width: 60px; height: 60px; cursor: pointer;"
              :src="scope.row.facePath || '/img/noPhoto.jpg'" fit="cover"
              @click="handleViewImage(scope.row.facePath)" />
          </template>
        </el-table-column>
        <el-table-column prop="departmentName" :label="$t('attendanceLogManage.table.departmentName')" align="center" />
        <el-table-column prop="staffName" :label="$t('attendanceLogManage.table.staffName')" align="center" />
        <el-table-column prop="staffId" :label="$t('attendanceLogManage.table.staffId')" align="center" />
        <el-table-column prop="clockTime" :label="$t('attendanceLogManage.table.clockTime')" align="center" />
        <el-table-column prop="remark" :label="$t('attendanceLogManage.table.remark')" align="center" />
      </el-table>

      <el-pagination :current-page.sync="page.current" :page-sizes="[10, 20, 30, 50]" :page-size="page.size"
        :total="page.total" layout="total, sizes, prev, pager, next, jumper" @size-change="handleSizeChange"
        @current-change="handleCurrentChange" />
    </el-card>

    <!-- 图片查看组件 -->
    <view-image ref="viewImage" />
  </div>
</template>

<script>
import { queryAttendanceLog } from '@/api/oa/attendanceLogManageApi'
import ViewImage from '@/components/system/viewImage'

export default {
  name: 'AttendanceLogManageList',
  components: {
    ViewImage
  },
  data() {
    return {
      loading: false,
      searchForm: {
        classesName: '',
        departmentName: '',
        date: new Date().toISOString().split('T')[0],
        staffName: ''
      },
      tableData: [],
      page: {
        current: 1,
        size: 10,
        total: 0
      }
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
          ...this.searchForm
        }
        const { data, total } = await queryAttendanceLog(params)
        this.tableData = data
        this.page.total = total
      } catch (error) {
        this.$message.error(this.$t('attendanceLogManage.fetchError'))
      } finally {
        this.loading = false
      }
    },
    handleSearch() {
      this.page.current = 1
      this.getList()
    },
    handleReset() {
      this.searchForm = {
        classesName: '',
        departmentName: '',
        date: new Date().toISOString().split('T')[0],
        staffName: ''
      }
      this.handleSearch()
    },
    handleSizeChange(val) {
      this.page.size = val
      this.getList()
    },
    handleCurrentChange(val) {
      this.page.current = val
      this.getList()
    },
    handleViewImage(url) {
      if (url) {
        this.$refs.viewImage.open(url)
      }
    }
  }
}
</script>

<style lang="scss" scoped>
.attendance-log-manage-container {
  padding: 20px;

  .search-wrapper {
    margin-bottom: 20px;

    .el-row {
      margin-bottom: -20px;
    }

    .el-col {
      margin-bottom: 20px;
    }
  }

  .list-wrapper {
    .el-pagination {
      margin-top: 20px;
      text-align: right;
    }
  }
}
</style>