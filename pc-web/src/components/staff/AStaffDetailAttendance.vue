<template>
    <div class="staff-attendance-container">
      <el-row :gutter="20">
        <el-col :span="4">
          <el-input
            v-model="aStaffDetailAttendanceInfo.curDate"
            :placeholder="$t('staffDetailAttendance.enterDate')"
            @change="loadStaffAttendances"
          />
        </el-col>
        <el-col :span="4">
          <el-button type="primary" size="small" @click="loadStaffAttendances">
            <i class="el-icon-search"></i> {{ $t('common.search') }}
          </el-button>
        </el-col>
      </el-row>
      <el-row :gutter="20" class="attendance-wrapper">
        <el-col
          v-for="index in aStaffDetailAttendanceInfo.maxDay"
          :key="index"
          :span="4"
          class="attendance-card"
          :style="{ 'background-color': getBgColor(index) }"
        >
          <el-card>
            <div>{{ `${aStaffDetailAttendanceInfo.curYear}-${aStaffDetailAttendanceInfo.curMonth}-${index}` }}</div>
            <div v-for="(item, detailIndex) in getAttendanceDetail(index)" :key="detailIndex">
              <div v-if="item.rest">{{ item.rest }}</div>
              <div v-else>
                {{ item.specCd === '1001' ? $t('staffDetailAttendance.checkIn') : $t('staffDetailAttendance.checkOut') }}:
                <span v-if="item.state !== '10000'">{{ formatTime(item.checkTime) }}</span>
                <span v-else>-</span>
                <span>({{ item.stateName }})</span>
              </div>
            </div>
            <div>
              <el-link @click="checkInLog(index)">{{ $t('staffDetailAttendance.attendanceLog') }}</el-link>
            </div>
          </el-card>
        </el-col>
      </el-row>
      <staff-attendance-detail ref="staffAttendanceDetail" />
    </div>
  </template>
  
  <script>
  import { queryAdminAttendanceClasses } from '@/api/staff/adminStaffDetailApi'
  import StaffAttendanceDetail from '@/components/staff/StaffAttendanceDetail.vue'
  
  export default {
    name: 'AStaffDetailAttendance',
    components: { StaffAttendanceDetail },
    data() {
      return {
        aStaffDetailAttendanceInfo: {
          staffId: '',
          attendances: [],
          curDate: '',
          maxDay: '',
          curYear: '',
          curMonth: ''
        }
      }
    },
    created() {
      const date = new Date()
      this.aStaffDetailAttendanceInfo.curMonth = date.getMonth() + 1
      this.aStaffDetailAttendanceInfo.curYear = date.getFullYear()
      this.aStaffDetailAttendanceInfo.curDate = `${date.getFullYear()}-${date.getMonth() + 1}`
      this.aStaffDetailAttendanceInfo.maxDay = new Date(date.getFullYear(), date.getMonth() + 1, 0).getDate()
    },
    methods: {
      switchTab(data) {
        this.aStaffDetailAttendanceInfo.staffId = data.staffId
        this.loadStaffAttendances()
      },
      async loadStaffAttendances() {
        if (!this.aStaffDetailAttendanceInfo.staffId || !this.aStaffDetailAttendanceInfo.curDate) return
        try {
          const { data } = await queryAdminAttendanceClasses({
            staffId: this.aStaffDetailAttendanceInfo.staffId,
            date: this.aStaffDetailAttendanceInfo.curDate,
            page: 1,
            row: 1000
          })
          this.aStaffDetailAttendanceInfo.attendances = data
        } catch (error) {
          this.$message.error(this.$t('staffDetailAttendance.fetchError'))
        }
      },
      getAttendanceDetail(day) {
        const attendance = this.getDayAttendance(day)
        if (!attendance) {
          const date = new Date()
          const taskYear = this.aStaffDetailAttendanceInfo.curYear
          const taskMonth = this.aStaffDetailAttendanceInfo.curMonth
          if (taskYear == date.getFullYear() && parseInt(taskMonth) == date.getMonth() + 1 && day > date.getDate()) {
            return [{ rest: this.$t('staffDetailAttendance.notYet') }]
          }
          return [{ rest: this.$t('staffDetailAttendance.noAttendance') }]
        }
        return attendance.attendanceClassesTaskDetails
      },
      getDayAttendance(day) {
        return this.aStaffDetailAttendanceInfo.attendances.find(item => item.taskDay == day) || null
      },
      getBgColor() {
        return '#fff'
      },
      formatTime(time) {
        return time // Implement time formatting if needed
      },
      checkInLog(day) {
        const curMonth = this.aStaffDetailAttendanceInfo.curMonth < 10 ? `0${this.aStaffDetailAttendanceInfo.curMonth}` : this.aStaffDetailAttendanceInfo.curMonth
        const formattedDay = day < 10 ? `0${day}` : day
        this.$refs.staffAttendanceDetail.open({
          staffId: this.aStaffDetailAttendanceInfo.staffId,
          date: `${this.aStaffDetailAttendanceInfo.curYear}-${curMonth}-${formattedDay}`
        })
      }
    }
  }
  </script>
  
  <style lang="scss" scoped>
  .staff-attendance-container {
    padding: 20px;
    .attendance-wrapper {
      margin-top: 20px;
      .attendance-card {
        border-radius: 5px;
        cursor: pointer;
        .el-card {
          text-align: center;
        }
      }
    }
  }
  </style>