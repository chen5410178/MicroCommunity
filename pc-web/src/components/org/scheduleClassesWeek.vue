<template>
  <div>
    <el-form-item :label="$t('scheduleClassesWeek.scheduleCycle')">
      <el-select v-model="scheduleCycle" :placeholder="$t('scheduleClassesWeek.scheduleCyclePlaceholder')"
        @change="changeInspectionPeriodWeek" style="width:100%">
        <el-option v-for="index in 4" :key="index" :label="`${index}${$t('scheduleClassesWeek.week')}`"
          :value="index" />
      </el-select>
    </el-form-item>

    <div v-for="week in scheduleCycle" :key="week">
      <el-form-item :label="getWeek(week)">
        <div class="schedule-weeks-container">
          <template v-for="(item, index) in days">
            <div v-if="item.weekFlag == 1"  :key="index" class="schedule-week-item" @click="changeWorkdayWeekInfo(item)">
              <div class="day-name">{{ getWorkDay(item.day) }}</div>
              <div class="workday-name">{{ item.workdayName }}</div>
              <div v-for="(time, timeIndex) in item.times" :key="timeIndex" class="time-range">
                {{ time.startTime }}-{{ time.endTime }}
              </div>
            </div>
          </template>
        </div>
      </el-form-item>
    </div>
  </div>
</template>

<script>
export default {
  name: 'ScheduleClassesWeek',
  data() {
    return {
      scheduleCycle: 1,
      days: []
    }
  },
  methods: {
    initData(scheduleCycle) {
      this.scheduleCycle = scheduleCycle
      this.changeInspectionPeriodWeek()
    },
    notify(params) {
      this.scheduleCycle = parseInt(params.scheduleCycle);
      this.days = params.days

      // 使用深拷贝确保数据是响应式的，并确保每个 item 都有 times 数组
      if (params.days && params.days.length > 0) {
        return;
      }

      this.changeInspectionPeriodWeek();
    },
    changeInspectionPeriodWeek() {
      let _days = this.days;
      _days.splice(0, _days.length);
      for (let weekIndex = 0; weekIndex < this.scheduleCycle; weekIndex++) {
        for (let cycleIndex = 0; cycleIndex < 7; cycleIndex++) {
          _days.push({
            weekFlag: weekIndex + 1,
            day: cycleIndex + 1,
            workday: '2002',
            workdayName: this.$t('scheduleClassesWeek.rest'),
            times: []
          })
        }
      }
      this.$emit('cycleChange', this.scheduleCycle)
    },
    changeWorkdayWeekInfo(item) {
      this.$emit('editDay', item)
    },
    getWeek(week) {
      return `${week}${this.$t('scheduleClassesWeek.week')}`
    },
    getWorkDay(day) {
      const days = [
        this.$t('common.monday'),
        this.$t('common.tuesday'),
        this.$t('common.wednesday'),
        this.$t('common.thursday'),
        this.$t('common.friday'),
        this.$t('common.saturday'),
        this.$t('common.sunday')
      ]
      return days[day - 1]
    },
    getDaysData() {
      return this.days
    }
  }
}
</script>

<style lang="scss" scoped>
.schedule-weeks-container {
  display: flex;
  flex-wrap: wrap;
  gap: 10px;
}

.schedule-week-item {
  width: calc(14.28% - 10px);
  padding: 10px;
  border: 1px solid #ebeef5;
  border-radius: 4px;
  cursor: pointer;
  text-align: center;
  transition: all 0.3s;

  &:hover {
    border-color: #409eff;
    box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
  }

  .day-name {
    font-weight: bold;
    margin-bottom: 5px;
  }

  .workday-name {
    margin-bottom: 5px;
  }

  .time-range {
    font-size: 12px;
    color: #666;
  }
}
</style>