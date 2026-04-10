<template>
  <el-dialog
    :title="$t('meterWater.confirmOperation')"
    :visible.sync="dialogVisible"
    width="30%"
    @close="handleClose"
  >
    <div class="confirm-content">
      <p>{{ $t('meterWater.confirmDeleteMeterReading') }}</p>
    </div>
    <span slot="footer" class="dialog-footer">
      <el-button @click="dialogVisible = false">{{ $t('common.cancel') }}</el-button>
      <el-button type="primary" @click="handleConfirm">{{ $t('common.confirm') }}</el-button>
    </span>
  </el-dialog>
</template>

<script>
import { deleteMeterWater } from '@/api/fee/meterWaterManageApi'
import { getCommunityId } from '@/api/community/communityApi'

export default {
  name: 'DeleteMeterWater',
  data() {
    return {
      dialogVisible: false,
      waterId: '',
      communityId: ''
    }
  },
  created() {
    this.communityId = getCommunityId()
  },
  methods: {
    open(data) {
      this.waterId = data.waterId
      this.dialogVisible = true
    },
    handleClose() {
      this.waterId = ''
    },
    async handleConfirm() {
      try {
        await deleteMeterWater({
          waterId: this.waterId,
          communityId: this.communityId
        })
        this.$message.success(this.$t('common.operationSuccess'))
        this.dialogVisible = false
        this.$emit('success')
      } catch (error) {
        console.error('Failed to delete meter water:', error)
        this.$message.error(error.message || this.$t('common.deleteFailed'))
      }
    }
  }
}
</script>

<style scoped>
.confirm-content {
  text-align: center;
  font-size: 16px;
  padding: 20px 0;
}
</style>