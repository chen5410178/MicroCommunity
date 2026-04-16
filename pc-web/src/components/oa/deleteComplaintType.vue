<template>
  <el-dialog
    :title="$t('deleteComplaintType.confirmTitle')"
    :visible.sync="visible"
    width="30%"
    @close="handleClose"
  >
    <div style="text-align: center;">
      <p>{{ $t('deleteComplaintType.confirmText') }}</p>
    </div>
    <span slot="footer" class="dialog-footer">
      <el-button @click="handleClose">{{ $t('deleteComplaintType.cancel') }}</el-button>
      <el-button type="primary" @click="deleteComplaintType">{{ $t('deleteComplaintType.confirm') }}</el-button>
    </span>
  </el-dialog>
</template>

<script>
import { deleteComplaintType } from '@/api/oa/complaintTypeApi'
import { getCommunityId } from '@/api/community/communityApi'

export default {
  name: 'DeleteComplaintType',
  data() {
    return {
      visible: false,
      currentComplaintType: null
    }
  },
  methods: {
    open(complaintType) {
      this.currentComplaintType = complaintType
      this.visible = true
    },
    async deleteComplaintType() {
      try {
        const params = {
          ...this.currentComplaintType,
          communityId: getCommunityId()
        }
        await deleteComplaintType(params)
        this.$emit('success')
        this.visible = false
        this.$message.success(this.$t('common.operationSuccess'))
      } catch (error) {
        this.$message.error(this.$t('deleteComplaintType.error'))
      }
    },
    handleClose() {
      this.visible = false
    }
  }
}
</script>