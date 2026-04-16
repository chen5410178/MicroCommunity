<template>
  <div class="today-attendance-container">
    <!-- 查询条件 -->
    <el-card class="search-wrapper">
      <div slot="header" class="flex justify-between">
        <span>{{ $t('todayAttendanceManage.search.title') }}</span>
      </div>
      <el-row :gutter="20">
        <el-col :span="4">
          <el-input
            v-model="searchForm.classesName"
            :placeholder="$t('todayAttendanceManage.search.classesName')"
            clearable
            @keyup.enter.native="handleSearch"
          />
        </el-col>
        <el-col :span="4">
          <el-input
            v-model="searchForm.departmentName"
            :placeholder="$t('todayAttendanceManage.search.departmentName')"
            clearable
            @keyup.enter.native="handleSearch"
          />
        </el-col>
        <el-col :span="4">
          <el-date-picker
            v-model="searchForm.date"
            type="date"
            :placeholder="$t('todayAttendanceManage.search.date')"
            value-format="yyyy-MM-dd"
            style="width: 100%"
          />
        </el-col>
        <el-col :span="4">
          <el-input
            v-model="searchForm.staffName"
            :placeholder="$t('todayAttendanceManage.search.staffName')"
            clearable
            @keyup.enter.native="handleSearch"
          />
        </el-col>
        <el-col :span="4">
          <el-button type="primary" @click="handleSearch">{{ $t('common.search') }}</el-button>
          <el-button @click="handleReset">{{ $t('common.reset') }}</el-button>
        </el-col>
      </el-row>
    </el-card>

    <!-- 今日考勤列表 -->
    <el-card class="list-wrapper">
      <div slot="header" class="flex justify-between">
        <span>{{ $t('todayAttendanceManage.list.title') }}</span>
      </div>
      <el-table
        v-loading="loading"
        :data="tableData"
        border
        style="width: 100%"
      >
        <el-table-column
          prop="staffName"
          :label="$t('todayAttendanceManage.table.staffName')"
          align="center"
        />
        <el-table-column
          prop="classesName"
          :label="$t('todayAttendanceManage.table.classesName')"
          align="center"
        />
        <el-table-column
          :label="$t('todayAttendanceManage.table.attendanceDate')"
          align="center"
        >
          <template slot-scope="scope">
            {{ `${scope.row.taskYear}-${scope.row.taskMonth}-${scope.row.taskDay}` }}
          </template>
        </el-table-column>
        <el-table-column
          prop="stateName"
          :label="$t('todayAttendanceManage.table.stateName')"
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
              type="primary"
              @click="handleDetail(scope.row)"
            >
              {{ $t('common.detail') }}
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
    </el-card>

    <!-- 考勤详情弹窗 -->
    <today-attendance-detail ref="detailDialog" />
  </div>
</template>

<script>
import { queryAttendanceClassesTask } from '@/api/oa/todayAttendanceManageApi'
import TodayAttendanceDetail from '@/components/oa/todayAttendanceDetail'

export default {
  name: 'TodayAttendanceManageList',
  components: {
    TodayAttendanceDetail
  },
  data() {
    return {
      loading: false,
      searchForm: {
        classesName: '',
        departmentName: '',
        date: '',
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
        const { data, total } = await queryAttendanceClassesTask(params)
        this.tableData = data
        this.page.total = total
      } catch (error) {
        this.$message.error(this.$t('todayAttendanceManage.fetchError'))
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
        date: '',
        staffName: ''
      }
      this.page.current = 1
      this.getList()
    },
    handleDetail(row) {
      this.$refs.detailDialog.open(row)
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
.today-attendance-container {
  padding: 20px;

  .search-wrapper {
    margin-bottom: 20px;

    .el-col {
      margin-bottom: 10px;
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