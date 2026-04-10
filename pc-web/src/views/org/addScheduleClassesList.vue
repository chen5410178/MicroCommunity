<template>
  <div class="add-schedule-classes-container">
    <el-row>
      <el-col :span="24">
        <el-card>
          <div slot="header" class="clearfix">
            <span>{{ $t('addScheduleClasses.title') }}</span>
          </div>
          <div>
            <el-form label-position="right" label-width="120px">
              <el-form-item :label="$t('addScheduleClasses.name')">
                <el-input v-model="addScheduleClassesInfo.name"
                  :placeholder="$t('addScheduleClasses.namePlaceholder')" />
              </el-form-item>
              <el-form-item :label="$t('addScheduleClasses.scheduleType')">
                <el-select v-model="addScheduleClassesInfo.scheduleType"
                  :placeholder="$t('addScheduleClasses.scheduleTypePlaceholder')" @change="changeScheduleType"
                  style="width:100%">
                  <el-option :label="$t('addScheduleClasses.daily')" value="1001" />
                  <el-option :label="$t('addScheduleClasses.weekly')" value="2002" />
                  <el-option :label="$t('addScheduleClasses.monthly')" value="3003" />
                </el-select>
              </el-form-item>

              <div v-if="addScheduleClassesInfo.scheduleType === '1001'">
                <schedule-classes-day ref="scheduleDay" @editDay="editDay" @cycleChange="changeScheduleCycle" />
              </div>
              <div v-if="addScheduleClassesInfo.scheduleType === '2002'">
                <schedule-classes-week ref="scheduleWeek" @editDay="editDay" @cycleChange="changeScheduleCycle" />
              </div>
              <div v-if="addScheduleClassesInfo.scheduleType === '3003'">
                <schedule-classes-month ref="scheduleMonth" @editDay="editDay" />
              </div>

              <div class="action-buttons">
                <el-button type="primary" @click="saveScheduleClassesInfo">
                  {{ $t('common.save') }}
                </el-button>
                <el-button @click="goBack">
                  {{ $t('common.back') }}
                </el-button>
              </div>
            </el-form>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <edit-schedule-classes-day ref="editScheduleDay" @update="updateDay" />
  </div>
</template>

<script>
import ScheduleClassesDay from '@/components/org/scheduleClassesDay'
import ScheduleClassesWeek from '@/components/org/scheduleClassesWeek'
import ScheduleClassesMonth from '@/components/org/scheduleClassesMonth'
import EditScheduleClassesDay from '@/components/org/editScheduleClassesDay'
import { saveScheduleClasses } from '@/api/org/addScheduleClassesApi'

export default {
  name: 'AddScheduleClassesList',
  components: {
    ScheduleClassesDay,
    ScheduleClassesWeek,
    ScheduleClassesMonth,
    EditScheduleClassesDay
  },
  data() {
    return {
      addScheduleClassesInfo: {
        name: '',
        scheduleType: '',
        scheduleCycle: '1',
        days: []
      }
    }
  },
  methods: {
    changeScheduleType() {
      this.addScheduleClassesInfo.days = []
      this.addScheduleClassesInfo.scheduleCycle = 1

      if (this.addScheduleClassesInfo.scheduleType === '1001') {
        setTimeout(() => {
          this.$refs.scheduleDay.notify({
            scheduleCycle: this.addScheduleClassesInfo.scheduleCycle,
            days: this.addScheduleClassesInfo.days
          })
        }, 500)
      } else if (this.addScheduleClassesInfo.scheduleType === '2002') {
        setTimeout(() => {
          this.$refs.scheduleWeek.notify({
            scheduleCycle: this.addScheduleClassesInfo.scheduleCycle,
            days: this.addScheduleClassesInfo.days
          })
        }, 500)
      } else if (this.addScheduleClassesInfo.scheduleType === '3003') {
        setTimeout(() => {
          this.$refs.scheduleMonth.notify({
            scheduleCycle: this.addScheduleClassesInfo.scheduleCycle,
            days: this.addScheduleClassesInfo.days
          })
        }, 500)
      }
    },
    async saveScheduleClassesInfo() {
      try {
        // 根据不同类型获取数据
        if (this.addScheduleClassesInfo.scheduleType === '1001') {
          this.addScheduleClassesInfo.days = this.$refs.scheduleDay.getDaysData()
        } else if (this.addScheduleClassesInfo.scheduleType === '2002') {
          this.addScheduleClassesInfo.days = this.$refs.scheduleWeek.getDaysData()
        } else if (this.addScheduleClassesInfo.scheduleType === '3003') {
          this.addScheduleClassesInfo.days = this.$refs.scheduleMonth.getDaysData()
        }

        const res = await saveScheduleClasses(this.addScheduleClassesInfo)
        if (res.code === 0) {
          this.$message.success(this.$t('common.operationSuccess'))
          this.goBack()
        } else {
          this.$message.error(res.msg)
        }
      } catch (error) {
        this.$message.error(this.$t('common.saveFailed'))
      }
    },
    goBack() {
      this.$router.go(-1)
    },
    editDay(item) {
      this.$refs.editScheduleDay.open(item)
    },
    updateDay(updatedItem) {
      // 由于 editScheduleClassesDay 组件现在直接修改传入的对象
      // 这里不需要额外的处理，但保留方法以保持代码完整性
      console.log('Day updated:', updatedItem)
    },
    changeScheduleCycle(cycle) {
      this.addScheduleClassesInfo.scheduleCycle = cycle
    }
  }
}
</script>

<style lang="scss" scoped>
.add-schedule-classes-container {
  padding: 20px;

  .action-buttons {
    margin-top: 20px;
    text-align: right;

    .el-button+.el-button {
      margin-left: 10px;
    }
  }
}
</style>