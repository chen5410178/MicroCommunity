<template>
  <el-dialog
    :title="$t('deleteApplyRoomDiscountRecord.title')"
    :visible.sync="visible"
    width="30%"
    :before-close="handleClose"
  >
    <div class="delete-content">
      <i class="el-icon-warning" style="color: #e6a23c; font-size: 24px;"></i>
      <span style="margin-left: 10px;">{{ $t('deleteApplyRoomDiscountRecord.confirmText') }}</span>
    </div>
    <span slot="footer" class="dialog-footer">
      <el-button @click="handleClose">{{ $t('common.cancel') }}</el-button>
      <el-button type="primary" @click="confirmDelete" :loading="loading">{{ $t('common.confirm') }}</el-button>
    </span>
  </el-dialog>
</template>

<script>
import { cutApplyRoomDiscountRecord } from '@/api/fee/listApplyRoomDiscountRecordApi'
import { getCommunityId } from '@/api/community/communityApi'

export default {
  name: 'DeleteApplyRoomDiscountRecord',
  data() {
    return {
      visible: false,
      loading: false,
      record: null
    }
  },
  methods: {
    open(record) {
      this.record = record
      this.visible = true
    },
    handleClose() {
      this.visible = false
    },
    async confirmDelete() {
      if (!this.record) return
      
      this.loading = true
      try {
        const params = {
          ardrId: this.record.ardrId,
          communityId: getCommunityId()
        }
        
        const res = await cutApplyRoomDiscountRecord(params)
        if (res.code === 0) {
          this.$message.success(this.$t('common.operationSuccess'))
          this.handleClose()
          this.$emit('success')
        } else {
          this.$message.error(res.msg)
        }
      } catch (error) {
        this.$message.error(this.$t('common.deleteFailed'))
      } finally {
        this.loading = false
      }
    }
  }
}
</script>

<style lang="scss" scoped>
.delete-content {
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 20px 0;
}
</style>