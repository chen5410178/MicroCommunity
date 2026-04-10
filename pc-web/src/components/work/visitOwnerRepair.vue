<template>
  <el-dialog :title="$t('repairReturnVisit.visit')" :visible.sync="visible" width="600px" @close="resetForm">
    <el-form ref="visitForm" :model="formData" :rules="rules" label-width="100px" label-position="right">
      <el-form-item :label="$t('repairReturnVisit.satisfaction')" prop="visitType">
        <el-select v-model="formData.visitType" :placeholder="$t('repairReturnVisit.satisfaction')"
          style="width:100%">
          <el-option :label="$t('repairReturnVisit.satisfied')" value="1001" />
          <el-option :label="$t('repairReturnVisit.unsatisfied')" value="2002" />
        </el-select>
      </el-form-item>

      <el-form-item :label="$t('repairReturnVisit.visitContent')" prop="context">
        <el-input v-model="formData.context" type="textarea" :rows="4"
          :placeholder="$t('repairReturnVisit.visitContent')" />
      </el-form-item>
    </el-form>

    <div slot="footer" class="dialog-footer">
      <el-button @click="visible = false">
        {{ $t('common.cancel') }}
      </el-button>
      <el-button type="primary" @click="submitVisit">
        {{ $t('common.submit') }}
      </el-button>
    </div>
  </el-dialog>
</template>

<script>
import { saveRepairReturnVisit } from '@/api/work/repairReturnVisitApi'

export default {
  name: 'VisitOwnerRepair',
  data() {
    return {
      visible: false,
      formData: {
        repairId: '',
        visitType: '',
        context: ''
      },
      rules: {
        visitType: [
          { required: true, message: this.$t('repairReturnVisit.satisfactionRequired'), trigger: 'change' }
        ],
        context: [
          { required: true, message: this.$t('repairReturnVisit.visitContentRequired'), trigger: 'blur' },
          { max: 1000, message: this.$t('repairReturnVisit.visitContentMaxLength'), trigger: 'blur' }
        ]
      }
    }
  },
  methods: {
    open(repairPool) {
      this.formData = {
        repairId: repairPool.repairId,
        visitType: '',
        context: '',
        communityId: this.getCommunityId()
      }
      this.visible = true
    },
    resetForm() {
      this.$refs.visitForm.resetFields()
    },
    submitVisit() {
      this.$refs.visitForm.validate(async valid => {
        if (valid) {
          try {
            await saveRepairReturnVisit(this.formData)
            this.$message.success(this.$t('common.operationSuccess'))
            this.visible = false
            this.$emit('success')
          } catch (error) {
            console.error('保存回访记录失败:', error)
          }
        }
      })
    }
  }
}
</script>