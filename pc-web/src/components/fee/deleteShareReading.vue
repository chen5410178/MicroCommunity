<template>
  <el-dialog
    :title="$t('shareReading.delete.title')"
    :visible.sync="dialogVisible"
    width="30%"
    @close="handleClose"
  >
    <div class="text-center">
      <p>{{ $t('shareReading.delete.confirm') }}</p>
    </div>
    <span slot="footer" class="dialog-footer">
      <el-button @click="dialogVisible = false">{{ $t('common.cancel') }}</el-button>
      <el-button type="primary" @click="handleConfirm">{{ $t('common.confirm') }}</el-button>
    </span>
  </el-dialog>
</template>

<script>
import { getCommunityId } from '@/api/community/communityApi'
import { deleteFloorShareReading } from '@/api/fee/shareReadingApi'

export default {
  name: 'DeleteShareReading',
  data() {
    return {
      dialogVisible: false,
      readingId: '',
      communityId: ''
    }
  },
  methods: {
    open(data) {
      this.dialogVisible = true
      this.readingId = data.readingId
      this.communityId = getCommunityId()
    },
    async handleConfirm() {
      try {
        await deleteFloorShareReading({
          readingId: this.readingId,
          communityId: this.communityId
        })
        this.$message.success(this.$t('common.operationSuccess'))
        this.dialogVisible = false
        this.$emit('success')
      } catch (error) {
        console.error('删除公摊抄表失败:', error)
      }
    },
    handleClose() {
      this.readingId = ''
      this.communityId = ''
    }
  }
}
</script>