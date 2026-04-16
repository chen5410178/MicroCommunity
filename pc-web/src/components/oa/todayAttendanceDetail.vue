<template>
  <el-dialog
    :title="$t('todayAttendanceDetail.title')"
    :visible.sync="visible"
    width="80%"
    @close="handleClose"
  >
    <el-table
      :data="tableData"
      border
      style="width: 100%"
    >
      <el-table-column
        prop="specCd"
        :label="$t('todayAttendanceDetail.table.name')"
        align="center"
      >
        <template slot-scope="scope">
          {{ scope.row.specCd === '1001' ? $t('todayAttendanceDetail.table.work') : $t('todayAttendanceDetail.table.offWork') }}
        </template>
      </el-table-column>
      <el-table-column
        :label="$t('todayAttendanceDetail.table.attendanceRange')"
        align="center"
      >
        <template slot-scope="scope">
          {{ scope.row.specCd === '1001' 
            ? `${timeMinFormat(scope.row.leaveValue)}~${timeMinFormat(scope.row.value)}` 
            : `${timeMinFormat(scope.row.value)}~${timeMinFormat(scope.row.lateValue)}` }}
        </template>
      </el-table-column>
      <el-table-column
        :label="$t('todayAttendanceDetail.table.lateEarly')"
        align="center"
      >
        <template slot-scope="scope">
          {{ scope.row.specCd === '1001' 
            ? `${timeMinFormat(scope.row.value)}~${timeMinFormat(scope.row.lateValue)}` 
            : `${timeMinFormat(scope.row.leaveValue)}~${timeMinFormat(scope.row.value)}` }}
        </template>
      </el-table-column>
      <el-table-column
        prop="stateName"
        :label="$t('todayAttendanceDetail.table.status')"
        align="center"
      />
      <el-table-column
        :label="$t('todayAttendanceDetail.table.attendanceTime')"
        align="center"
      >
        <template slot-scope="scope">
          {{ scope.row.checkTime || $t('todayAttendanceDetail.table.notCheckIn') }}
        </template>
      </el-table-column>
      <el-table-column
        :label="$t('todayAttendanceDetail.table.snapshot')"
        align="center"
      >
        <template slot-scope="scope">
          <el-image
            v-if="scope.row.state !== '10000'"
            style="width: 60px; height: 60px;"
            :src="scope.row.facePath"
            :preview-src-list="[scope.row.facePath]"
            fit="cover"
          />
        </template>
      </el-table-column>
    </el-table>
  </el-dialog>
</template>

<script>
import {timeMinFormat} from '@/utils/dateUtil'
export default {
  name: 'TodayAttendanceDetail',
  data() {
    return {
      visible: false,
      tableData: []
    }
  },
  methods: {
    open(data) {
      this.tableData = data.attendanceClassesTaskDetails || []
      this.visible = true
    },
    handleClose() {
      this.tableData = []
      this.visible = false
    },
    timeMinFormat,
  }
}
</script>

<style scoped>
.el-image {
  cursor: pointer;
  transition: transform 0.3s;
}

.el-image:hover {
  transform: scale(1.1);
}
</style>