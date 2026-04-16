<template>
  <el-dialog :title="$t('editScheduleClassesDay.title')" :visible.sync="visible" width="60%" @close="handleClose">
    <el-form label-position="right" label-width="120px">
      <el-form-item :label="$t('editScheduleClassesDay.status')">
        <el-select v-model="workday" @change="changeScheduleClassesDayState" style="width:100%">
          <el-option :label="$t('editScheduleClassesDay.rest')" value="2002" />
          <el-option v-for="(item, index) in classess" :key="index" :label="`${item.name}${getClassTime(item)}`"
            :value="item.classesId" />
        </el-select>
      </el-form-item>

      <div v-if="workday !== '2002'">
        <el-form-item v-for="(item, index) in times" :key="index">
          <el-col :span="11">
            <el-form-item :label="$t('editScheduleClassesDay.startTime')">
              <el-input v-model="item.startTime" :placeholder="$t('editScheduleClassesDay.startTimePlaceholder')" />
            </el-form-item>
          </el-col>
          <el-col :span="11">
            <el-form-item :label="$t('editScheduleClassesDay.endTime')">
              <el-input v-model="item.endTime" :placeholder="$t('editScheduleClassesDay.endTimePlaceholder')" />
            </el-form-item>
          </el-col>
        </el-form-item>
      </div>
    </el-form>

    <span slot="footer" class="dialog-footer">
      <el-button @click="visible = false">{{ $t('common.cancel') }}</el-button>
      <el-button type="primary" @click="submitEditScheduleClassesDay">
        {{ $t('common.submit') }}
      </el-button>
    </span>
  </el-dialog>
</template>

<script>
import { listClasses } from '@/api/org/addScheduleClassesApi'

export default {
  name: 'EditScheduleClassesDay',
  data() {
    return {
      visible: false,
      workday: '',
      workdayName: '',
      times: [],
      classess: [],
      currentItem: null
    }
  },
  methods: {
    open(item) {
      this.currentItem = item 
      this.workday = item.workday
      this.workdayName = item.workdayName
      this.times = item.times ? [...item.times] : []
      this.listClassess()
      this.visible = true
    },
    handleClose() {
      this.workday = ''
      this.workdayName = ''
      this.times = []
      this.currentItem = null
    },
    async listClassess() {
      try {
        const params = {
          page: 1,
          row: 100,
          state: '1001'
        }
        const res = await listClasses(params)
        this.classess = res.data || []
      } catch (error) {
        console.error('获取班次列表失败:', error)
      }
    },
    changeScheduleClassesDayState() {
      this.times = []
      if (this.workday === '2002') {
        this.workdayName = this.$t('editScheduleClassesDay.rest')
        return
      }

      const selectedClass = this.classess.find(item => item.classesId === this.workday)
      if (selectedClass) {
        this.workdayName = selectedClass.name
        if (selectedClass.times) {
          this.times = [...selectedClass.times]
        }
      }
    },
    getClassTime(item) {
      if (!item.times || item.times.length === 0) return ''

      let timeStr = ''
      item.times.forEach(timeItem => {
        timeStr += `${timeItem.startTime}-${timeItem.endTime};`
      })

      return `(${timeStr})`
    },
    submitEditScheduleClassesDay() {
      if (this.currentItem) {
        this.currentItem.workday = this.workday
        this.currentItem.workdayName = this.workdayName
        this.currentItem.times = [...this.times]
        this.visible = false
      }
    }
  }
}
</script>

<style lang="scss" scoped>
.el-form-item {
  margin-bottom: 20px;
}
</style>