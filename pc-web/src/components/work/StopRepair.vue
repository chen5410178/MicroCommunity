<template>
  <el-dialog :title="$t('repairDispatchManage.pause')" :visible.sync="visible" width="50%" @close="resetForm">
    <el-form :model="form" ref="form" label-width="120px">
      <el-form-item :label="$t('repairDispatchManage.pauseReason')" prop="remark"
        :rules="[{ required: true, message: $t('repairDispatchManage.requiredPauseReason'), trigger: 'blur' }]">
        <el-input type="textarea" :rows="4" v-model="form.remark"
          :placeholder="$t('repairDispatchManage.requiredPauseReason')" />
      </el-form-item>
    </el-form>

    <div slot="footer" class="dialog-footer">
      <el-button @click="visible = false">{{ $t('common.cancel') }}</el-button>
      <el-button type="primary" @click="submitForm">{{ $t('common.submit') }}</el-button>
    </div>
  </el-dialog>
</template>

<script>
import { repairStop } from '@/api/work/repairDispatchManageApi'
import { getCommunityId } from '@/api/community/communityApi'

export default {
  name: 'StopRepair',
  data() {
    return {
      visible: false,
      form: {
        repairId: '',
        repairType: '',
        remark: ''
      }
    }
  },
  methods: {
    open(row) {
      this.form = {
        repairId: row.repairId,
        repairType: row.repairType,
        remark: ''
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

          await repairStop(data)
          this.$emit('success')
          this.visible = false
          this.$message.success(this.$t('common.operationSuccess'))
        } catch (error) {
          console.error('暂停失败:', error)
          this.$message.error(this.$t('repairDispatchManage.pauseFailed'))
        }
      })
    },
    resetForm() {
      this.$refs.form.resetFields()
      this.form = {
        repairId: '',
        repairType: '',
        remark: ''
      }
    }
  }
}
</script>