<template>
  <el-dialog
    :title="$t('deleteMaintainancePlan.title')"
    :visible.sync="visible"
    width="30%"
    @close="handleClose"
  >
    <div class="text-center">
      <p>{{ $t('deleteMaintainancePlan.confirm') }}</p>
    </div>
    <span slot="footer" class="dialog-footer">
      <el-button @click="handleClose">{{ $t('common.cancel') }}</el-button>
      <el-button type="primary" @click="handleConfirm">{{ $t('common.confirm') }}</el-button>
    </span>
  </el-dialog>
</template>

<script>
import { deleteMaintainancePlan } from '@/api/inspection/maintainancePlanManageApi'
import { getCommunityId } from '@/api/community/communityApi'

export default {
  name: 'DeleteMaintainancePlan',
  data() {
    return {
      visible: false,
      currentRow: {},
      communityId: ''
    }
  },
  created() {
    this.communityId = getCommunityId()
  },
  methods: {
    open(row) {
      this.currentRow = { ...row }
      this.visible = true
    },
    async handleConfirm() {
      try {
        const params = {
          planId: this.currentRow.planId,
          communityId: this.communityId
        }
        await deleteMaintainancePlan(params)
        this.$message.success(this.$t('common.operationSuccess'))
        this.$emit('success')
        this.visible = false
      } catch (error) {
        this.$message.error(this.$t('deleteMaintainancePlan.error'))
      }
    },
    handleClose() {
      this.visible = false
    }
  }
}
</script>

<style scoped>
.text-center {
  text-align: center;
}
</style>