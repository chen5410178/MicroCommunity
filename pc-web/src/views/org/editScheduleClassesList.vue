<template>
  <div class="edit-schedule-classes-container">
    <el-card class="box-card">
      <div slot="header" class="flex justify-between">
        <span>{{ $t('editScheduleClasses.title') }}</span>
      </div>
      <el-row :gutter="20">
        <el-col :span="24">
          <div class="form-content">
            <el-form ref="form" :model="editScheduleClassesInfo" label-width="120px">
              <el-form-item :label="$t('editScheduleClasses.name')">
                <el-input v-model="editScheduleClassesInfo.name"
                  :placeholder="$t('editScheduleClasses.namePlaceholder')" clearable />
              </el-form-item>
              <el-form-item :label="$t('editScheduleClasses.scheduleType')">
                <el-select v-model="editScheduleClassesInfo.scheduleType"
                  :placeholder="$t('editScheduleClasses.scheduleTypePlaceholder')" style="width:100%"
                  @change="changeScheduleType(1)">
                  <el-option disabled :value="''" :label="$t('editScheduleClasses.scheduleTypePlaceholder')" />
                  <el-option value="1001" :label="$t('editScheduleClasses.daySchedule')" />
                  <el-option value="2002" :label="$t('editScheduleClasses.weekSchedule')" />
                  <el-option value="3003" :label="$t('editScheduleClasses.monthSchedule')" />
                </el-select>
              </el-form-item>

              <div v-if="editScheduleClassesInfo.scheduleType == '1001'">
                <schedule-classes-day ref="dayComponent" 
                   @editDay="editDay" @cycleChange="changeScheduleCycle" />
              </div>
              <div v-if="editScheduleClassesInfo.scheduleType == '2002'">
                <schedule-classes-week ref="weekComponent" 
                   @editDay="editDay" @cycleChange="changeScheduleCycle" />
              </div>
              <div v-if="editScheduleClassesInfo.scheduleType == '3003'">
                <schedule-classes-month ref="monthComponent"
                   @editDay="editDay" />
              </div>

              <el-form-item>
                <el-button type="primary" @click="editScheduleClasses">
                  <i class="el-icon-check" /> {{ $t('common.save') }}
                </el-button>
                <el-button type="warning" style="margin-right:20px;" @click="goBack">
                  <i class="el-icon-close" /> {{ $t('common.back') }}
                </el-button>
              </el-form-item>
            </el-form>
          </div>
        </el-col>
      </el-row>
    </el-card>

    <edit-schedule-classes-day ref="editDayDialog" />
  </div>
</template>

<script>
import ScheduleClassesDay from '@/components/org/scheduleClassesDay'
import ScheduleClassesWeek from '@/components/org/scheduleClassesWeek'
import ScheduleClassesMonth from '@/components/org/scheduleClassesMonth'
import EditScheduleClassesDay from '@/components/org/editScheduleClassesDay'
import { updateScheduleClasses, listScheduleClasses, listScheduleClassesDay } from '@/api/org/editScheduleClassesApi'

export default {
  name: 'EditScheduleClassesList',
  components: {
    ScheduleClassesDay,
    ScheduleClassesWeek,
    ScheduleClassesMonth,
    EditScheduleClassesDay
  },
  data() {
    return {
      editScheduleClassesInfo: {
        scheduleId: '',
        name: '',
        scheduleType: '',
        scheduleCycle: '1',
        days: []
      }
    }
  },
  created() {
    this.editScheduleClassesInfo.scheduleId = this.$route.query.scheduleId
    this.listScheduleClassess()
    this.listScheduleClassesDays()
  },
  methods: {
    changeScheduleType(customChange) {
      if (customChange) {
        this.editScheduleClassesInfo.days = []
        this.editScheduleClassesInfo.scheduleCycle = 1
      }

      if (this.editScheduleClassesInfo.scheduleType === '1001') {
        setTimeout(() => {
          this.$refs.dayComponent.notify({
            scheduleCycle: this.editScheduleClassesInfo.scheduleCycle,
            days: this.editScheduleClassesInfo.days
          })
        }, 500)
      }
      if (this.editScheduleClassesInfo.scheduleType === '2002') {
        setTimeout(() => {
          this.$refs.weekComponent.notify({
          scheduleCycle: this.editScheduleClassesInfo.scheduleCycle,
            days: this.editScheduleClassesInfo.days
          })
        }, 500)
      }
      if (this.editScheduleClassesInfo.scheduleType === '3003') {
        setTimeout(() => {
          this.$refs.monthComponent.notify({
            scheduleCycle: this.editScheduleClassesInfo.scheduleCycle,
            days: this.editScheduleClassesInfo.days
          })
        }, 500)
      }
    },
    async editScheduleClasses() {
      try {
        await updateScheduleClasses(this.editScheduleClassesInfo)
        this.$message.success(this.$t('common.operationSuccess'))
        this.goBack()
      } catch (error) {
        this.$message.error(error.message || this.$t('common.updateFailed'))
      }
    },
    async listScheduleClassess() {
      try {
        const { data } = await listScheduleClasses({
          page: 1,
          row: 1,
          scheduleId: this.editScheduleClassesInfo.scheduleId
        })
        Object.assign(this.editScheduleClassesInfo, data[0])
        this.changeScheduleType()
      } catch (error) {
        console.error('请求失败:', error)
      }
    },
    async listScheduleClassesDays() {
      try {
        const { data } = await listScheduleClassesDay({
          page: 1,
          row: 100,
          scheduleId: this.editScheduleClassesInfo.scheduleId
        })
        data.forEach(item => {
          if (item.workday === '2002') {
            item.workdayName = this.$t('editScheduleClasses.rest')
          }
        })
        this.editScheduleClassesInfo.days = data
        this.changeScheduleType()
      } catch (error) {
        console.error('请求失败:', error)
      }
    },
    goBack() {
      this.$router.go(-1)
    },
    editDay(item) {
      this.$refs.editDayDialog.open(item)
    },
    changeScheduleCycle(cycle) {
      this.editScheduleClassesInfo.scheduleCycle = cycle
    }
  }
}
</script>

<style lang="scss" scoped>
.edit-schedule-classes-container {
  padding: 20px;

  .box-card {
    margin-bottom: 20px;
  }

  .form-content {
    padding: 20px;
  }

  .el-form-item {
    margin-bottom: 22px;
  }
}
</style>