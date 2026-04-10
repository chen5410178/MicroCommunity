<template>
  <el-dialog
    :title="$t('workType.delete.title')"
    :visible.sync="visible"
    width="30%"
    @close="handleClose"
  >
    <div class="delete-content">
      <i class="el-icon-warning" style="color:#E6A23C;font-size:24px;"></i>
      <span style="margin-left:10px;">{{ $t('workType.delete.confirm') }}</span>
    </div>
    
    <span slot="footer" class="dialog-footer">
      <el-button @click="visible = false">{{ $t('common.cancel') }}</el-button>
      <el-button type="primary" @click="handleConfirm">{{ $t('common.confirm') }}</el-button>
    </span>
  </el-dialog>
</template>

<script>
import { deleteWorkType } from '@/api/oa/workTypeApi'
import { getCommunityId } from '@/api/community/communityApi'

export default {
  name: 'DeleteWorkType',
  data() {
    return {
      visible: false,
      currentRow: null
    }
  },
  methods: {
    open(row) {
      this.currentRow = row
      this.visible = true
    },
    handleClose() {
      this.currentRow = null
    },
    async handleConfirm() {
      try {
        const params = {
          wtId: this.currentRow.wtId,
          communityId: getCommunityId()
        }
        await deleteWorkType(params)
        this.$message.success(this.$t('common.operationSuccess'))
        this.visible = false
        this.$emit('success')
      } catch (error) {
        this.$message.error(error.message || this.$t('workType.delete.error'))
      }
    }
  }
}
</script>

<style scoped>
.delete-content {
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 20px 0;
}
</style>