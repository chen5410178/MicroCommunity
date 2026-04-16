<template>
  <el-dialog :title="$t('inspectionTaskTransfer.transfer')" :visible.sync="visible" width="50%" top="5vh">
    <el-form ref="form" :model="form" label-width="120px">
      <el-form-item :label="$t('inspectionTaskTransfer.transferObject')" required>
        <el-select v-model="form.staffId" :placeholder="$t('inspectionTaskTransfer.required')">
          <el-option v-for="item in staffs" :key="item.userId" :label="item.name" :value="item.userId"></el-option>
        </el-select>
      </el-form-item>

      <el-form-item :label="$t('inspectionTaskTransfer.transferDesc')" required>
        <el-input v-model="form.transferDesc" type="textarea" :rows="4"
          :placeholder="$t('inspectionTaskTransfer.required')"></el-input>
      </el-form-item>
    </el-form>

    <div slot="footer" class="dialog-footer">
      <el-button @click="visible = false">{{ $t('inspectionTaskTransfer.cancel') }}</el-button>
      <el-button type="primary" @click="handleSubmit">{{ $t('inspectionTaskTransfer.submit') }}</el-button>
    </div>
  </el-dialog>
</template>

<script>
import { updateInspectionTask } from '@/api/inspection/inspectionTaskApi'
import { queryStaffInfos } from '@/api/staff/staffApi.js'

export default {
  name: 'InspectionTaskTransfer',
  components: {},
  data() {
    return {
      visible: false,
      staffs: [],
      form: {
        taskId: '',
        staffId: '',
        staffName: '',
        transferDesc: '',
        currentUserId: '',
        inspectionPlanId:''
      }
    }
  },
  methods: {
    open(task) {
      this.form = {
        taskId: task.taskId,
        staffId: '',
        staffName: '',
        transferDesc: '',
        currentUserId: '',
        inspectionPlanId:task.inspectionPlanId
      }
      this.loadStaffs()
      this.visible = true
    },
    async loadStaffs() {
      const { staffs } = await queryStaffInfos({
        page: 1,
        row: 1000
      })
      this.staffs = staffs
    },
    handleSwitchOrg(org) {
      this.$refs.staffSelect.setOrg(org)
    },
    handleStaffChange(staff) {
      this.form.staffId = staff.staffId
      this.form.staffName = staff.staffName
    },
    async handleSubmit() {
      if (!this.form.staffId) {
        this.$message.warning(this.$t('inspectionTaskTransfer.chooseStaff'))
        return
      }

      if (!this.form.transferDesc) {
        this.$message.warning(this.$t('inspectionTaskTransfer.inputDesc'))
        return
      }

      if (this.form.staffId === this.form.currentUserId) {
        this.$message.warning(this.$t('inspectionTaskTransfer.cantTransferSelf'))
        return
      }

      try {
        await updateInspectionTask(this.form)
        this.$message.success(this.$t('common.operationSuccess'))
        this.visible = false
        this.$emit('success')
      } catch (error) {
        console.error('任务流转失败:', error)
        this.$message.error(error.message || this.$t('common.operateFail'))
      }
    }
  }
}
</script>