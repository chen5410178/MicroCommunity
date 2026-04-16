<template>
  <el-card  width="50%" >
    <el-form ref="form" :model="formData" label-width="120px">
      <el-form-item :label="$t('auditDiv.action')" prop="auditCode">
        <el-select v-model="formData.auditCode" :placeholder="$t('auditDiv.pleaseSelect')" style="width: 100%">
          <el-option value="" disabled :label="$t('auditDiv.pleaseSelect')"></el-option>
          <el-option v-if="nextAudit.next || nextAudit.exit" value="1100" :label="$t('auditDiv.agree')"></el-option>
          <el-option v-if="nextAudit.back" value="1200" :label="$t('auditDiv.return')"></el-option>
          <el-option v-if="nextAudit.backIndex" value="1400" :label="$t('auditDiv.returnToSubmitter')"></el-option>
          <el-option value="1300" :label="$t('auditDiv.transfer')"></el-option>
        </el-select>
      </el-form-item>

      <el-form-item :label="$t('auditDiv.workOrderDescription')" prop="auditMessage">
        <el-input type="textarea" :placeholder="$t('auditDiv.requiredDescription')" v-model="formData.auditMessage"
          :rows="4"></el-input>
      </el-form-item>

      <el-form-item v-if="(formData.auditCode === '1100' && nextAudit.assignee === '-2') || formData.auditCode === '1300'"
        :label="$t('auditDiv.nextHandler')" prop="staffName">
        <el-input v-model="formData.staffName" :placeholder="$t('auditDiv.requiredNextHandler')" disabled
          style="width: calc(100% - 100px); margin-right: 10px;"></el-input>
        <el-button @click="chooseStaff">
          <i class="el-icon-search"></i>
          {{ $t('auditDiv.select') }}
        </el-button>
      </el-form-item>
    </el-form>

    <div  class="dialog-footer">
      <el-button type="primary" @click="handleSubmit">{{ $t('common.submit') }}</el-button>
    </div>
    <select-staff ref="selectStaff" @selectStaff="handleStaffChange"></select-staff>
  </el-card>
</template>

<script>
import { queryNextDealUser,auditAllocationStoreOrder } from '@/api/resource/purchaseApplyDetailApi'
import SelectStaff from '@/components/staff/SelectStaff'

export default {
  name: 'AllocationAuditDiv',
  components: {
    SelectStaff
  },
  data() {
    return {
      visible: false,
      formData: {
        auditCode: '',
        auditMessage: '',
        staffId: '',
        staffName: '',
        taskId: '',
        flowId: '',
        id: ''
      },
      nextAudit: {},
      createUserId: ''
    }
  },
  methods: {
    open(data) {
      this.formData.taskId = data.taskId
      this.formData.flowId = data.flowId
      this.formData.id = data.id
      this.createUserId = data.createUserId
      this.loadNextAuditPerson()
    },
    async loadNextAuditPerson() {
      try {
        const params = {
          taskId: this.formData.taskId,
          startUserId: this.createUserId
        }
        const res = await queryNextDealUser(params)
        if (res.code == '0') {
          this.nextAudit = res.data[0]
        }
      } catch (error) {
        console.error('获取下一处理人失败:', error)
      }
    },
    chooseStaff() {
      this.$refs.selectStaff.open({
        from: 'purchase',
        call: (staff) => {
          this.formData.staffId = staff.staffId
          this.formData.staffName = staff.staffName
        }
      })
    },
    async handleSubmit() {
      if (!this.formData.auditCode) {
        this.$message.warning(this.$t('auditDiv.pleaseSelectStatus'))
        return
      }
      if (!this.formData.auditMessage) {
        this.$message.warning(this.$t('auditDiv.pleaseFillDescription'))
        return
      }
      if (this.formData.auditCode !== '1200' &&
        this.formData.auditCode !== '1400' &&
        !this.formData.staffId) {
        this.$message.warning(this.$t('auditDiv.pleaseSelectNextHandler'))
        return
      }

      if (this.nextAudit.assignee !== '-2') {
        this.formData.staffId = this.nextAudit.assignee
      }

      try {
        const res = await auditAllocationStoreOrder(this.formData)
        if (res.code == 0) {
          this.$message.success(this.$t('common.operationSuccess'))
          this.$router.go(-1)
          this.$emit('success')
        } else {
          this.$message.error(res.msg)
        }
      } catch (error) {
        console.error('提交审核失败:', error)
        this.$message.error(this.$t('auditDiv.submitFailed'))
      }
    },
    handleClose() {
      this.$refs.form.resetFields()
      this.formData = {
        auditCode: '',
        auditMessage: '',
        staffId: '',
        staffName: '',
        taskId: '',
        flowId: '',
        id: ''
      }
      this.nextAudit = {}
    },
    handleStaffChange(staff) {
      this.formData.staffId = staff.staffId
      this.formData.staffName = staff.staffName
    }
  }
}
</script>