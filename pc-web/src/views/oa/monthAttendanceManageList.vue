<template>
  <div class="month-attendance-container">
    <!-- 查询条件 -->
    <el-card class="search-wrapper">
      <div slot="header" class="flex justify-between">
        <span>{{ $t('monthAttendance.search.title') }}</span>
      </div>
      <el-row :gutter="20">
        <el-col :span="6">
            <el-select v-model="searchForm.classesId" :placeholder="$t('monthAttendance.search.selectClasses')"
              style="width:100%">
              <el-option v-for="item in attendanceClasses" :key="item.classesId" :label="item.classesName"
                :value="item.classesId" />
            </el-select>
        </el-col>
        <el-col :span="6">
            <el-input v-model="searchForm.staffName" :placeholder="$t('monthAttendance.search.inputStaffName')" />
        </el-col>
        <el-col :span="6">
            <el-date-picker v-model="searchForm.date" type="month"
              :placeholder="$t('monthAttendance.search.selectDate')" value-format="yyyy-MM" style="width:100%"
              @change="handleDateChange" />
        </el-col>
        <el-col :span="6" class="text-right">
          <el-button type="primary" @click="handleSearch">{{ $t('common.search') }}</el-button>
          <el-button @click="handleReset">{{ $t('common.reset') }}</el-button>
        </el-col>
      </el-row>
    </el-card>

    <!-- 数据表格 -->
    <el-card class="table-wrapper">
      <div slot="header" class="flex justify-between">
        <span>{{ $t('monthAttendance.table.title') }}</span>
        <el-button type="primary" size="small" class="float-right" @click="handleExport">
          {{ $t('common.export') }}
        </el-button>
      </div>

      <div class="table-content" :style="{ width: tableWidth }">
        <el-table :data="tableData" border style="width: 100%" v-loading="loading">
          <el-table-column prop="classesName" :label="$t('monthAttendance.table.classes')" align="center" />
          <el-table-column prop="staffName" :label="$t('monthAttendance.table.staffName')" align="center" />
          <el-table-column v-for="day in curDays" :key="day" :label="`${day}日`" align="center">
            <template slot-scope="scope">
              <span v-html="getDayDetail(scope.row.days, day)"></span>
            </template>
          </el-table-column>
          <el-table-column prop="clockIn" :label="$t('monthAttendance.table.clockIn')" align="center" />
          <el-table-column prop="late" :label="$t('monthAttendance.table.late')" align="center" />
          <el-table-column prop="early" :label="$t('monthAttendance.table.early')" align="center" />
          <el-table-column prop="noClockIn" :label="$t('monthAttendance.table.noClockIn')" align="center" />
          <el-table-column prop="free" :label="$t('monthAttendance.table.free')" align="center" />
        </el-table>
      </div>

      <!-- 分页 -->
      <el-pagination class="pagination-wrapper" :current-page="pagination.current" :page-sizes="[10, 20, 30, 50]"
        :page-size="pagination.size" :total="pagination.total" layout="total, sizes, prev, pager, next, jumper"
        @size-change="handleSizeChange" @current-change="handleCurrentChange" />
    </el-card>
  </div>
</template>

<script>
import { getMonthAttendance, getAttendanceClasses, exportMonthAttendance } from '@/api/oa/monthAttendanceManageApi'
import { getCommunityId } from '@/api/community/communityApi'

export default {
  name: 'MonthAttendanceManageList',
  data() {
    return {
      loading: false,
      searchForm: {
        classesId: '',
        staffName: '',
        date: '',
        taskYear: '',
        taskMonth: ''
      },
      tableData: [],
      attendanceClasses: [],
      curDays: 30,
      pagination: {
        current: 1,
        size: 10,
        total: 0
      },
      communityId: ''
    }
  },
  computed: {
    tableWidth() {
      const bodyWidth = document.body.clientWidth
      const menuWidth = document.getElementById('menu-nav').offsetWidth || 0
      return `${bodyWidth - menuWidth - 55}px`
    }
  },
  created() {
    this.communityId = getCommunityId()
    this.initData()
  },
  methods: {
    async initData() {
      await this.getAttendanceClasses()
      this.handleDateChange()
      this.getList()
    },

    async getList() {
      try {
        this.loading = true
        const params = {
          page: this.pagination.current,
          row: this.pagination.size,
          ...this.searchForm,
          communityId: this.communityId
        }
        const { data, total } = await getMonthAttendance(params)
        this.tableData = data
        this.pagination.total = total
      } catch (error) {
        this.$message.error(this.$t('monthAttendance.fetchError'))
      } finally {
        this.loading = false
      }
    },

    async getAttendanceClasses() {
      try {
        const params = {
          page: 1,
          row: 100
        }
        const { data } = await getAttendanceClasses(params)
        this.attendanceClasses = data
        if (data.length > 0) {
          this.searchForm.classesId = data[0].classesId
        }
      } catch (error) {
        this.$message.error(this.$t('monthAttendance.fetchClassesError'))
      }
    },

    handleDateChange() {
      if (this.searchForm.date) {
        const [year, month] = this.searchForm.date.split('-')
        this.searchForm.taskYear = year
        this.searchForm.taskMonth = month
        this.curDays = this.daysInMonth(year, month - 1)
      } else {
        const date = new Date()
        this.searchForm.taskYear = date.getFullYear()
        this.searchForm.taskMonth = date.getMonth() + 1
        this.searchForm.date = `${date.getFullYear()}-${date.getMonth() + 1}`
        this.curDays = this.daysInMonth(date.getFullYear(), date.getMonth())
      }
    },

    daysInMonth(year, month) {
      return new Date(year, month + 1, 0).getDate()
    },

    getDayDetail(detail, day) {
      const currentDate = new Date()
      if (
        this.searchForm.taskYear == currentDate.getFullYear() &&
        this.searchForm.taskMonth == (currentDate.getMonth() + 1) &&
        day > currentDate.getDate()
      ) {
        return this.$t('monthAttendance.notTime')
      }

      if (!detail || !Object.prototype.hasOwnProperty.call(detail, day)) {
        return this.$t('monthAttendance.noNeed')
      }

      const details = detail[day]
      if (!details) {
        return this.$t('monthAttendance.noNeed')
      }

      let value = ''
      details.forEach(element => {
        if (element.specCd === '1001') {
          value += `<div>${this.$t('monthAttendance.work')}：`
        } else {
          value += `<div>${this.$t('monthAttendance.offWork')}：`
        }

        if (element.state !== '10000') {
          value += `${this.timeFormat(element.checkTime)}(${element.stateName})</div>`
        } else {
          value += `-(${element.stateName})</div>`
        }
      })
      return value
    },

    timeFormat(time) {
      if (!time) return ''
      return new Date(time).toLocaleTimeString()
    },

    handleSearch() {
      this.pagination.current = 1
      this.getList()
    },

    handleReset() {
      this.searchForm = {
        classesId: this.attendanceClasses.length > 0 ? this.attendanceClasses[0].classesId : '',
        staffName: '',
        date: '',
        taskYear: '',
        taskMonth: ''
      }
      this.handleDateChange()
      this.handleSearch()
    },

    async handleExport() {
      try {
        this.loading = true
        const params = {
          ...this.searchForm,
          communityId: this.communityId,
          pagePath: 'monthAttendance'
        }
        await exportMonthAttendance(params)
        this.$router.push('/pages/property/downloadTempFile?tab=下载中心')
      } catch (error) {
        this.$message.error(this.$t('monthAttendance.exportError'))
      }
    },

    handleSizeChange(val) {
      this.pagination.size = val
      this.getList()
    },

    handleCurrentChange(val) {
      this.pagination.current = val
      this.getList()
    }
  }
}
</script>

<style lang="scss" scoped>
.month-attendance-container {
  padding: 20px;

  .search-wrapper {
    margin-bottom: 20px;

    .el-form-item {
      margin-bottom: 0;
    }
  }

  .table-wrapper {
    .table-content {
      overflow-x: auto;
    }

    .pagination-wrapper {
      margin-top: 20px;
      text-align: right;
    }
  }

  .text-right {
    text-align: right;
  }

  .float-right {
    float: right;
  }
}
</style>