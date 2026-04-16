<template>
  <el-dialog :title="$t('deleteMaintainancePlanMachine.title')" :visible.sync="visible" width="30%" @close="handleClose">

      <div style="text-align: center; margin-bottom: 20px;">
        <p>{{ $t('deleteMaintainancePlanMachine.confirmText') }}</p>
      </div>

    <div slot="footer" class="dialog-footer">
      <el-button @click="visible = false">{{ $t('deleteMaintainancePlanMachine.cancel') }}</el-button>
      <el-button type="primary" @click="confirmDelete">{{ $t('deleteMaintainancePlanMachine.confirm') }}</el-button>
    </div>
  </el-dialog>
</template>

<script>
import { deleteMaintainancePlanMachine } from '@/api/inspection/maintainancePlanMachineApi'
import { getCommunityId } from '@/api/community/communityApi'

export default {
  name: 'DeleteMaintainancePlanMachine',
  data() {
    return {
      visible: false,
      formData: {}
    }
  },
  methods: {
    open(params) {
      this.formData = { ...params }
      this.visible = true
    },
    async confirmDelete() {
      try {
        const params = {
          ...this.formData,
          communityId: getCommunityId()
        }
        await deleteMaintainancePlanMachine(params)
        this.$emit('success')
        this.visible = false
        this.$message.success(this.$t('common.operationSuccess'))
      } catch (error) {
        this.$message.error(this.$t('deleteMaintainancePlanMachine.deleteError'))
      }
    },
    handleClose() {
      this.formData = {}
    }
  }
}
</script>

<style scoped>
.dialog-footer {
  text-align: right;
}
</style>