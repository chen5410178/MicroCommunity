<template>
  <el-dialog
    :title="$t('repairDispatchManage.returnVisit')"
    :visible.sync="visible"
    width="50%"
    @close="resetForm"
  >
    <el-form :model="form" ref="form" label-width="120px">
      <el-form-item
        :label="$t('repairDispatchManage.returnVisitSuggestion')"
        prop="context"
        :rules="[{ required: true, message: $t('repairDispatchManage.requiredReturnVisitSuggestion'), trigger: 'blur' }]"
      >
        <el-input
          type="textarea"
          :rows="4"
          v-model="form.context"
          :placeholder="$t('repairDispatchManage.requiredReturnVisitSuggestion')"
        />
      </el-form-item>
    </el-form>
    
    <div slot="footer" class="dialog-footer">
      <el-button @click="visible = false">{{ $t('common.cancel') }}</el-button>
      <el-button type="primary" @click="submitForm">{{ $t('common.submit') }}</el-button>
    </div>
  </el-dialog>
</template>

<script>
import { appraiseRepair } from '@/api/work/repairDispatchManageApi'
import { getCommunityId } from '@/api/community/communityApi'

export default {
  name: 'AppraiseRepair',
  data() {
    return {
      visible: false,
      form: {
        repairId: '',
        repairType: '',
        context: '',
        publicArea: '',
        repairChannel: '',
        maintenanceType: ''
      }
    }
  },
  methods: {
    open(row) {
      this.form = {
        repairId: row.repairId,
        repairType: row.repairType,
        publicArea: row.publicArea,
        repairChannel: row.repairChannel,
        maintenanceType: row.maintenanceType,
        context: ''
      }
      this.visible = true
    },
    async submitForm() {
      this.$refs.form.validate(async valid => {
        if (!valid) return
        
        try {
          const data = {
            ...this.form,
            communityId: getCommunityId()
          }
          
          await appraiseRepair(data)
          this.$emit('success')
          this.visible = false
          this.$message.success(this.$t('common.operationSuccess'))
        } catch (error) {
          console.error('回访失败:', error)
          this.$message.error(this.$t('repairDispatchManage.returnVisitFailed'))
        }
      })
    },
    resetForm() {
      this.$refs.form.resetFields()
      this.form = {
        repairId: '',
        repairType: '',
        context: '',
        publicArea: '',
        repairChannel: '',
        maintenanceType: ''
      }
    }
  }
}
</script>