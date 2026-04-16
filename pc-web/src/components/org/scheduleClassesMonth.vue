<template>
  <div>
    <el-form-item :label="$t('scheduleClassesMonth.scheduleCycle')">
      <el-select v-model="scheduleCycle" :placeholder="$t('scheduleClassesMonth.scheduleCyclePlaceholder')"
        @change="changeInspectionPeriod" style="width:100%">
        <el-option :label="`1${$t('scheduleClassesMonth.month')}`" value="1" />
      </el-select>
    </el-form-item>

    <el-form-item :label="$t('scheduleClassesMonth.scheduleInfo')">
      <div class="schedule-days-container">
        <div v-for="(item, index) in days" :key="index" class="schedule-day-item" @click="changeWorkdayInfo(item)">
          <div class="day-number">{{ item.day }}</div>
          <div class="workday-name">{{ item.workdayName }}</div>
          <div v-for="(time, timeIndex) in item.times" :key="timeIndex" class="time-range">
            {{ time.startTime }}-{{ time.endTime }}
          </div>
        </div>
      </div>
    </el-form-item>
  </div>
</template>

<script>
export default {
  name: 'ScheduleClassesMonth',
  data() {
    return {
      scheduleCycle: 1,
      days: []
    }
  },
  methods: {
    initData(scheduleCycle) {
      this.scheduleCycle = scheduleCycle
      this.changeInspectionPeriod()
    },
    notify(_params) {
      this.days = _params.days;
      this.scheduleCycle = _params.scheduleCycle;
      if (_params.days && _params.days.length > 0) {
        return;
      }
      this.changeInspectionPeriod();
    },
    changeInspectionPeriod() {
      let _days = this.days;
      _days.splice(0, _days.length);
      for (let cycleIndex = 0; cycleIndex < 31; cycleIndex++) {
        _days.push({
          day: cycleIndex + 1,
          workday: '2002',
          workdayName: this.$t('scheduleClassesMonth.rest'),
          times: []
        })
      }
    },
    changeWorkdayInfo(item) {
      this.$emit('editDay', item)
    },
    getDaysData() {
      return this.days
    }
  }
}
</script>

<style lang="scss" scoped>
.schedule-days-container {
  display: flex;
  flex-wrap: wrap;
  gap: 10px;
}

.schedule-day-item {
  width: calc(16.66% - 10px);
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

  .day-number {
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