<template>
    <div class="staff-classes-container">
      <el-row :gutter="20">
        <el-col :span="4">
          <el-input
            v-model="aStaffDetailClassesInfo.curDate"
            :placeholder="$t('staffDetailClasses.enterDate')"
            @change="queryScheduleClasses"
          />
        </el-col>
        <el-col :span="4">
          <el-button type="primary" size="small" @click="queryScheduleClasses">
            <i class="el-icon-search"></i> {{ $t('common.search') }}
          </el-button>
        </el-col>
      </el-row>
      <el-row :gutter="20" class="days-wrapper">
        <el-col
          v-for="(item, index) in aStaffDetailClassesInfo.days"
          :key="index"
          :span="4"
          class="day-card"
          @click.native="changeWorkdayInfo(item)"
        >
          <el-card>
            <div>{{ item.day }}</div>
            <div>{{ item.workdayName || $t('staffDetailClasses.rest') }}</div>
            <div v-for="(time, tIndex) in item.times" :key="tIndex">
              {{ time.startTime }}-{{ time.endTime }}
            </div>
          </el-card>
        </el-col>
      </el-row>
    </div>
  </template>
  
  <script>
  import { listStaffScheduleClasses } from '@/api/staff/adminStaffDetailApi'
  
  export default {
    name: 'AStaffDetailClasses',
    data() {
      return {
        aStaffDetailClassesInfo: {
          staffId: '',
          days: [],
          curDate: ''
        }
      }
    },
    created() {
      const date = new Date()
      this.aStaffDetailClassesInfo.curDate = `${date.getFullYear()}-${date.getMonth() + 1}`
    },
    methods: {
      switchTab(data) {
        this.aStaffDetailClassesInfo.staffId = data.staffId
        this.listStaffScheduleClasses(1, 10)
      },
      async listStaffScheduleClasses(page, row) {
        try {
          const { data } = await listStaffScheduleClasses({
            staffId: this.aStaffDetailClassesInfo.staffId,
            curDate: this.aStaffDetailClassesInfo.curDate,
            page,
            row
          })
          this.aStaffDetailClassesInfo.days = data.length > 0 ? data[0].days : []
        } catch (error) {
          this.$message.error(this.$t('staffDetailClasses.fetchError'))
        }
      },
      queryScheduleClasses() {
        this.listStaffScheduleClasses(1, 10)
      },
      changeWorkdayInfo(item) {
        // Implement workday info change logic if needed
        console.log('Selected workday:', item)
      }
    }
  }
  </script>
  
  <style lang="scss" scoped>
  .staff-classes-container {
    padding: 20px;
    .days-wrapper {
      margin-top: 20px;
      .day-card {
        cursor: pointer;
        .el-card {
          text-align: center;
          border-radius: 5px;
        }
      }
    }
  }
  </style>