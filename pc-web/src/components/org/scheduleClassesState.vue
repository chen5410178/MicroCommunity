<template>
  <el-dialog
    :title="$t('scheduleClasses.state.title')"
    :visible.sync="visible"
    width="30%"
    @close="handleClose"
  >
    <div style="text-align: center">
      <p>
        {{ $t('scheduleClasses.state.confirm') }}
        <strong>{{ formData.stateName }}</strong>
        {{ $t('scheduleClasses.state.schedule') }}
      </p>
    </div>
    <span slot="footer" class="dialog-footer">
      <el-button @click="handleClose">{{ $t('common.cancel') }}</el-button>
      <el-button type="primary" @click="handleConfirm">{{ $t('common.confirm') }}</el-button>
    </span>
  </el-dialog>
</template>

<script>
import { updateScheduleClassesState } from '@/api/org/scheduleClassesApi'

export default {
  name: 'ScheduleClassesState',
  data() {
    return {
      visible: false,
      formData: {
        scheduleId: '',
        stateName: '',
        state: ''
      }
    }
  },
  methods: {
    open(data) {
      this.formData = { ...data }
      this.visible = true
    },
    async handleConfirm() {
      try {
        await updateScheduleClassesState(this.formData)
        this.$message.success(this.$t('common.operationSuccess'))
        this.$emit('success')
        this.visible = false
      } catch (error) {
        this.$message.error(this.$t('scheduleClasses.state.error'))
      }
    },
    handleClose() {
      this.visible = false
    }
  }
}
</script>