<template>
  <el-dialog
    :title="$t('notepadManage.delete.title')"
    :visible.sync="visible"
    width="30%"
  >
    <div class="text-center">
      <p>{{ $t('notepadManage.delete.confirm') }}</p>
    </div>
    <span slot="footer" class="dialog-footer">
      <el-button @click="visible = false">{{ $t('common.cancel') }}</el-button>
      <el-button type="primary" @click="handleConfirm">{{ $t('common.confirm') }}</el-button>
    </span>
  </el-dialog>
</template>

<script>
import { deleteNotepad } from '@/api/oa/notepadManageApi'
import { getCommunityId } from '@/api/community/communityApi'

export default {
  name: 'DeleteNotepad',
  data() {
    return {
      visible: false,
      formData: {
        noteId: '',
        communityId: ''
      }
    }
  },
  methods: {
    open(data) {
      this.visible = true
      this.formData.noteId = data.noteId
      this.formData.communityId = getCommunityId()
    },
    async handleConfirm() {
      try {
        await deleteNotepad(this.formData)
        this.$message.success(this.$t('common.operationSuccess'))
        this.visible = false
        this.$emit('success')
      } catch (error) {
        console.error('删除记事本失败:', error)
      }
    }
  }
}
</script>

<style scoped>
.text-center {
  text-align: center;
}
</style>