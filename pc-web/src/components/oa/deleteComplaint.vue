<template>
  <el-dialog
    :title="$t('complaint.delete.title')"
    :visible.sync="visible"
    width="30%"
    center
  >
    <div style="text-align:center">
      <p>{{ $t('complaint.delete.confirm') }}</p>
    </div>
    <span slot="footer" class="dialog-footer">
      <el-button @click="visible = false">{{ $t('common.cancel') }}</el-button>
      <el-button type="primary" @click="handleDelete" :loading="loading">{{ $t('common.confirm') }}</el-button>
    </span>
  </el-dialog>
</template>

<script>
import { deleteComplaint } from '@/api/oa/complaintApi'
import { getCommunityId } from '@/api/community/communityApi'

export default {
  name: 'DeleteComplaint',
  data() {
    return {
      visible: false,
      loading: false,
      complaintId: '',
      communityId: ''
    }
  },
  methods: {
    open(row) {
      this.communityId = getCommunityId()
      this.complaintId = row.complaintId
      this.visible = true
    },
    async handleDelete() {
      try {
        this.loading = true
        await deleteComplaint({
          complaintId: this.complaintId,
          communityId: this.communityId
        })
        this.$message.success(this.$t('common.operationSuccess'))
        this.$emit('success')
        this.visible = false
      } catch (error) {
        console.error('删除投诉失败:', error)
      } finally {
        this.loading = false
      }
    }
  }
}
</script>