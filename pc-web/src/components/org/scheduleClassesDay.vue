<template>
  <div>
    <el-form-item :label="$t('scheduleClassesDay.scheduleCycle')">
      <el-select v-model="scheduleCycle" :placeholder="$t('scheduleClassesDay.scheduleCyclePlaceholder')"
        @change="changeInspectionPeriod" style="width:100%">
        <el-option v-for="index in 31" :key="index" :label="`${index}${$t('scheduleClassesDay.day')}`" :value="index" />
      </el-select>
    </el-form-item>

    <el-form-item :label="$t('scheduleClassesDay.scheduleInfo')">
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
  name: 'ScheduleClassesDay',
  data() {
    return {
      scheduleCycle: 1,
      days: []
    }
  },
  methods: {
    notify(params) {
      this.days = params.days;
      this.scheduleCycle = params.scheduleCycle;

      if (params.days && params.days.length > 0) {
        return;
      }

      this.changeInspectionPeriod();
    },
    initData(scheduleCycle) {
      this.scheduleCycle = scheduleCycle
      this.changeInspectionPeriod()
    },
    changeInspectionPeriod() {
      let _days = this.days;
      _days.splice(0, _days.length);
      for (let cycleIndex = 0; cycleIndex < this.scheduleCycle; cycleIndex++) {
        _days.push({
          day: cycleIndex + 1,
          workday: '2002',
          workdayName: this.$t('scheduleClassesDay.rest'),
          times: []
        })
      }
      this.$emit('cycleChange', this.scheduleCycle)
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