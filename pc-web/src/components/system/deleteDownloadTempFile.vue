<template>
  <el-dialog
    :title="$t('deleteDownloadTempFile.title')"
    :visible.sync="visible"
    width="30%"
    @close="handleClose"
  >
    <div style="text-align: center;">
      <p>{{ $t('deleteDownloadTempFile.confirmMessage') }}</p>
    </div>
    <span slot="footer" class="dialog-footer">
      <el-button @click="handleClose">{{ $t('deleteDownloadTempFile.cancel') }}</el-button>
      <el-button type="primary" @click="handleDelete">{{ $t('deleteDownloadTempFile.confirm') }}</el-button>
    </span>
  </el-dialog>
</template>

<script>
import { deleteUserDownloadFile } from '@/api/system/downloadTempFileApi'
import { getCommunityId } from '@/api/community/communityApi'

export default {
  name: 'DeleteDownloadTempFile',
  data() {
    return {
      visible: false,
      currentFile: null
    }
  },
  methods: {
    open(file) {
      this.currentFile = file
      this.visible = true
    },
    handleClose() {
      this.visible = false
      this.currentFile = null
    },
    async handleDelete() {
      try {
        const params = {
          ...this.currentFile,
          communityId: getCommunityId()
        }
        await deleteUserDownloadFile(params)
        this.$emit('success')
        this.$message.success(this.$t('common.operationSuccess'))
        this.handleClose()
      } catch (error) {
        this.$message.error(this.$t('deleteDownloadTempFile.deleteFailed'))
      }
    }
  }
}
</script>