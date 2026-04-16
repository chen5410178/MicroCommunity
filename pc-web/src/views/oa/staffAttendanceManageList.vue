<template>
  <div class="staff-attendance-manage-container">
    <el-row :gutter="20">
      <el-col :span="4">
        <el-card class="box-card">
          <div class="filter-wrapper">
            <el-date-picker v-model="staffAttendanceManageInfo.curDate" type="month"
              :placeholder="$t('staffAttendance.monthPlaceholder')" value-format="yyyy-MM" class="filter-item"
              @change="handleDateChange"></el-date-picker>

            <el-input v-model="staffAttendanceManageInfo.orgName" readonly
              :placeholder="$t('staffAttendance.orgPlaceholder')" class="filter-item"
              @focus="handleOrgFocus"></el-input>

            <el-input v-model="staffAttendanceManageInfo.staffNameLike"
              :placeholder="$t('staffAttendance.staffPlaceholder')" class="filter-item"
              @keyup.enter.native="loadStaffs"></el-input>

            <div class="filter-item text-right">
              <el-button type="primary" @click="loadStaffs">
                {{ $t('common.search') }}
              </el-button>
            </div>
          </div>

          <div class="staff-list">
            <ul class="staff-ul">
              <li v-for="(item, index) in staffAttendanceManageInfo.staffs" :key="index" class="staff-item"
                :class="{ 'active': staffAttendanceManageInfo.curStaffId === item.userId }" @click="switchStaff(item)">
                {{ item.name }}
              </li>
            </ul>
          </div>
        </el-card>
      </el-col>

      <el-col :span="20">
        <el-card class="box-card">
          <div class="attendance-grid">
            <el-row :gutter="20">
              <el-col v-for="index in staffAttendanceManageInfo.maxDay" :key="index" :span="4" class="attendance-day "
                :style="{ backgroundColor: getBgColor(index) }">
                <div class="labeling-strip">
                  <div class="day-header">
                    {{ staffAttendanceManageInfo.curYear }}-{{ staffAttendanceManageInfo.curMonth }}-{{ index }}
                  </div>

                  <div v-if="getDayAttendance(index) && getDayAttendance(index).state !== '30000'" class="strip"
                    @click="replenishCheckIn(index)">
                    <span> {{ $t('staffAttendance.replenish') }}</span>
                  </div>

                  <div v-for="(item, detailIndex) in getAttendanceDetail(index)" :key="detailIndex"
                    class="attendance-detail">
                    <div v-if="item.rest">{{ item.rest }}</div>
                    <div v-else>
                      {{ item.specCd === '1001' ? $t('staffAttendance.work') : $t('staffAttendance.offWork') }}：
                      <span v-if="item.state !== '10000'">{{ formatTime(item.checkTime) }}</span>
                      <span v-else> - </span>
                      <span>({{ item.stateName }})</span>
                    </div>
                  </div>

                  <div class="log-link">
                    <el-link type="primary" @click="checkInLog(index)">
                      {{ $t('staffAttendance.log') }}
                    </el-link>
                  </div>
                </div>
              </el-col>
            </el-row>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 子组件 -->

    <choose-org-tree ref="chooseOrgTree" @switchOrg="handleSwitchOrg"></choose-org-tree>
    <staff-attendance-detail ref="staffAttendanceDetail"></staff-attendance-detail>
    <staff-attendance-replenish-check-in ref="staffAttendanceReplenishCheckIn" @success="loadStaffAttendances"></staff-attendance-replenish-check-in>
  </div>
</template>

<script>
import { getCommunityId } from '@/api/community/communityApi'
import {
  queryStaffInfos,
  queryAttendanceClassesTask,

} from '@/api/oa/staffAttendanceManageApi'
import { dateFormat } from '@/utils/dateUtil'
import ChooseOrgTree from '@/components/org/ChooseOrgTree'
import StaffAttendanceDetail from '@/components/oa/staffAttendanceDetail'
import StaffAttendanceReplenishCheckIn from '@/components/oa/staffAttendanceReplenishCheckIn'

export default {
  name: 'StaffAttendanceManageList',
  components: {
    ChooseOrgTree,
    StaffAttendanceDetail,
    StaffAttendanceReplenishCheckIn
  },
  data() {
    return {
      communityId: '',
      staffAttendanceManageInfo: {
        staffs: [],
        attendances: [],
        classesId: '',
        orgId: '',
        orgName: '',
        curDate: '',
        curYear: '',
        curMonth: '',
        curStaffId: '',
        maxDay: 31,
        staffNameLike: ''
      }
    }
  },
  created() {
    this.communityId = getCommunityId()
    this.initStaffDate()
    this.loadStaffs()
  },
  methods: {
    initStaffDate() {
      const date = new Date()
      this.staffAttendanceManageInfo.curMonth = date.getMonth() + 1
      this.staffAttendanceManageInfo.curYear = date.getFullYear()
      this.staffAttendanceManageInfo.curDate = `${date.getFullYear()}-${date.getMonth() + 1}`
      this.staffAttendanceManageInfo.maxDay = new Date(date.getFullYear(), date.getMonth() + 1, 0).getDate()
    },

    handleDateChange(val) {
      if (val) {
        const values = val.split('-')
        this.staffAttendanceManageInfo.curYear = values[0]
        this.staffAttendanceManageInfo.curMonth = values[1]
        this.staffAttendanceManageInfo.maxDay = new Date(values[0], values[1], 0).getDate()
        this.loadStaffAttendances()
      }
    },

    async loadStaffs() {
      try {
        const params = {
          page: 1,
          row: 100,
          orgId: this.staffAttendanceManageInfo.orgId,
          staffName: this.staffAttendanceManageInfo.staffNameLike,
          communityId: this.communityId
        }

        const data = await queryStaffInfos(params)
        this.staffAttendanceManageInfo.staffs = data.staffs || []

        if (this.staffAttendanceManageInfo.staffs.length > 0) {
          this.staffAttendanceManageInfo.curStaffId = this.staffAttendanceManageInfo.staffs[0].userId
          this.loadStaffAttendances()
        }
      } catch (error) {
        console.error('Failed to load staffs:', error)
      }
    },

    async loadStaffAttendances() {
      if (!this.staffAttendanceManageInfo.curStaffId || !this.staffAttendanceManageInfo.curDate) return

      try {
        const params = {
          page: 1,
          row: 1000,
          date: this.staffAttendanceManageInfo.curDate,
          staffId: this.staffAttendanceManageInfo.curStaffId,
          communityId: this.communityId
        }

        const { data } = await queryAttendanceClassesTask(params)
        this.staffAttendanceManageInfo.attendances = data || []
        console.log(this.staffAttendanceManageInfo.attendances)
      } catch (error) {
        console.error('Failed to load staff attendances:', error)
      }
    },

    handleOrgFocus() {
      this.$refs.chooseOrgTree.open()
    },

    handleSwitchOrg(org) {
      this.staffAttendanceManageInfo.orgId = org.orgId
      this.staffAttendanceManageInfo.orgName = org.allOrgName
      this.loadStaffs()
    },

    switchStaff(staff) {
      this.staffAttendanceManageInfo.curStaffId = staff.userId
      this.loadStaffAttendances()
    },

    getDayAttendance(day) {
      if (!this.staffAttendanceManageInfo.attendances) return null
      const attendance = this.staffAttendanceManageInfo.attendances.find(item => item.taskDay == day)

      if (!attendance) {
        return null
      }
      return attendance
    },

    getAttendanceDetail(day) {
      const attendance = this.getDayAttendance(day)

      if (!attendance) {
        const date = new Date()
        const taskYear = this.staffAttendanceManageInfo.curYear
        const taskMonth = this.staffAttendanceManageInfo.curMonth

        if (taskYear == date.getFullYear() && parseInt(taskMonth) == (date.getMonth() + 1) && day > date.getDate()) {
          return [{ rest: this.$t('staffAttendance.notTime') }]
        }

        return [{ rest: this.$t('staffAttendance.noNeed') }]
      }

      return attendance.attendanceClassesTaskDetails || []
    },

    getBgColor(day) {
      console.log(day)
      return '#fff'
    },

    formatTime(time) {
      if (!time) return ''
      return dateFormat(time)
    },

    checkInLog(day) {
      let month = this.staffAttendanceManageInfo.curMonth
      if (month < 10) month = `0${month}`
      if (day < 10) day = `0${day}`

      const date = `${this.staffAttendanceManageInfo.curYear}-${month}-${day}`
      this.$refs.staffAttendanceDetail.open({
        staffId: this.staffAttendanceManageInfo.curStaffId,
        date: date
      })
    },

    replenishCheckIn(day) {
      const details = this.getAttendanceDetail(day)
      if (!details || details.length < 1) {
        this.$message.warning(this.$t('staffAttendance.noTask'))
        return
      }

      const newDetails = details.filter(item => item.state === '10000')
      if (newDetails.length < 1) {
        this.$message.warning(this.$t('staffAttendance.noTask'))
        return
      }

      this.$refs.staffAttendanceReplenishCheckIn.open(newDetails)
    }
  }
}
</script>

<style lang="scss" scoped>
.staff-attendance-manage-container {
  padding: 20px;

  .box-card {
    margin-bottom: 20px;
  }

  .filter-wrapper {
    .filter-item {
      margin-bottom: 15px;
      width: 100%;
    }
  }

  .staff-list {
    max-height: 600px;
    overflow-y: auto;

    .staff-ul {
      list-style: none;
      padding: 0;
      margin: 0;

      .staff-item {
        padding: 10px;
        margin-bottom: 5px;
        cursor: pointer;
        border-radius: 4px;
        text-align: center;

        &:hover {
          background-color: #f5f7fa;
        }

        &.active {
          background-color: #409eff;
          color: #fff;
        }
      }
    }
  }

  .attendance-grid {


    .attendance-day {
      padding: 10px;
      margin-bottom: 20px;
      border-radius: 4px;
      box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
      height: 120px;

      .labeling-strip {
        position: relative;

        .strip {
          position: absolute;
          top: 0;
          right: 0;
          background-color: #409eff;
          color: #fff;
          padding: 4px 8px;
          font-size: 12px;
          border-radius: 0 4px 0 4px;
          cursor: pointer;
          z-index: 1;
          
          &:hover {
            background-color: #66b1ff;
          }
          
          span {
            white-space: nowrap;
          }
        }
      }

      .day-header {
        font-weight: bold;
        margin-bottom: 10px;
        text-align: center;
      }





      .replenish-btn {
        color: #409eff;
        cursor: pointer;
        text-align: center;
        margin-bottom: 10px;

        &:hover {
          text-decoration: underline;
        }
      }

      .attendance-detail {
        margin-bottom: 5px;
        font-size: 12px;
      }

      .log-link {
        text-align: center;
        margin-top: 10px;
      }
    }
  }
}
</style>