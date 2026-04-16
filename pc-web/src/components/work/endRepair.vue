<template>
  <el-dialog
    :title="$t('repairPoolManage.end')"
    :visible.sync="visible"
    width="50%"
    @close="resetForm"
  >
    <el-form ref="form" :model="formData" label-width="120px">
      <el-form-item
        :label="$t('repairPoolManage.endReason')"
        prop="context"
        :rules="[{ required: true, message: $t('repairPoolManage.endReason'), trigger: 'blur' }]"
      >
        <el-input
          v-model="formData.context"
          type="textarea"
          :rows="4"
          :placeholder="$t('repairPoolManage.endReason')"
        />
      </el-form-item>
    </el-form>
    
    <span slot="footer" class="dialog-footer">
      <el-button @click="visible = false">{{ $t('common.cancel') }}</el-button>
      <el-button type="primary" @click="submitForm">{{ $t('common.submit') }}</el-button>
    </span>
  </el-dialog>
</template>

<script>
import { endRepair } from '@/api/work/repairPoolManageApi'
import { getCommunityId } from '@/api/community/communityApi'

export default {
  name: 'EndRepair',
  data() {
    return {
      visible: false,
      formData: {
        repairId: '',
        context: ''
      }
    }
  },
  methods: {
    open(repair) {
      this.formData.repairId = repair.repairId
      this.visible = true
    },
    
    async submitForm() {
      this.$refs.form.validate(async valid => {
        if (valid) {
          try {
            const data = {
              ...this.formData,
              communityId: getCommunityId()
            }
            
            await endRepair(data)
            this.$message.success(this.$t('common.operationSuccess'))
            this.visible = false
            this.$emit('success')
          } catch (error) {
            console.error('End repair failed:', error)
          }
        }
      })
    },
    
    resetForm() {
      this.$refs.form.resetFields()
      this.formData = {
        repairId: '',
        context: ''
      }
    }
  }
}
</script>