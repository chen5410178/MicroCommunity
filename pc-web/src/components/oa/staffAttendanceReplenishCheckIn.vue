<template>
  <el-dialog
    :title="$t('staffAttendance.replenishCheckIn')"
    :visible.sync="visible"
    width="50%"
  >
    <el-form label-position="left" label-width="120px">
      <el-form-item :label="$t('staffAttendance.attendanceTask')" required>
        <el-select 
          v-model="form.detailId" 
          :placeholder="$t('staffAttendance.selectTask')"
          style="width:100%"
        >
          <el-option
            v-for="(item,index) in details"
            :key="index"
            :label="`${item.specName}(${item.value})`"
            :value="item.detailId"
          ></el-option>
        </el-select>
      </el-form-item>

      <el-form-item :label="$t('staffAttendance.reason')" required>
        <el-input
          type="textarea"
          :rows="4"
          v-model="form.remark"
          :placeholder="$t('staffAttendance.inputReason')"
        ></el-input>
      </el-form-item>
    </el-form>

    <span slot="footer" class="dialog-footer">
      <el-button @click="visible = false">{{ $t('common.cancel') }}</el-button>
      <el-button type="primary" @click="handleSubmit">{{ $t('staffAttendance.submitReplenish') }}</el-button>
    </span>
  </el-dialog>
</template>

<script>
import { attendanceReplenishCheckIn } from '@/api/oa/staffAttendanceManageApi'

export default {
  name: 'StaffAttendanceReplenishCheckIn',
  data() {
    return {
      visible: false,
      details: [],
      form: {
        detailId: '',
        remark: ''
      }
    }
  },
  methods: {
    open(details) {
      details.forEach(item => {
        if(item.specCd === '1001'){
          item.specName = this.$t('staffAttendance.work')
        }else{
          item.specName = this.$t('staffAttendance.offWork')
        }
      })
      this.details = details || []
      this.form = {
        detailId: '',
        remark: ''
      }
      this.visible = true
    },

    async handleSubmit() {
      if (!this.form.detailId) {
        this.$message.warning(this.$t('staffAttendance.selectTaskFirst'))
        return
      }

      if (!this.form.remark) {
        this.$message.warning(this.$t('staffAttendance.inputReasonFirst'))
        return
      }

      try {
        await attendanceReplenishCheckIn(this.form)
        this.$message.success(this.$t('common.operationSuccess'))
        this.$emit('success')
        this.visible = false
      } catch (error) {
        console.error('Failed to submit replenish:', error)
        this.$message.error(this.$t('staffAttendance.replenishFailed'))
      }
    }
  }
}
</script>

<style lang="scss" scoped>
.dialog-footer {
  text-align: right;
}
</style>