<template>
  <el-dialog
    :title="$t('dataPrivilege.deleteTitle')"
    :visible.sync="visible"
    width="30%"
    center>
    <div class="delete-content">
      <p>{{ $t('dataPrivilege.deleteConfirm') }}</p>
    </div>
    <div slot="footer" class="dialog-footer">
      <el-button @click="visible = false">{{ $t('common.cancel') }}</el-button>
      <el-button type="danger" @click="handleConfirm">{{ $t('common.confirm') }}</el-button>
    </div>
  </el-dialog>
</template>

<script>
import { deleteDataPrivilege } from '@/api/org/dataPrivilegeManageApi'
import { getCommunityId } from '@/api/community/communityApi'

export default {
  name: 'DeleteDataPrivilege',
  data() {
    return {
      visible: false,
      dpId: '',
      communityId: ''
    }
  },
  methods: {
    open(data) {
      this.dpId = data.dpId
      this.communityId = getCommunityId()
      this.visible = true
    },
    async handleConfirm() {
      try {
        await deleteDataPrivilege({
          dpId: this.dpId,
          communityId: this.communityId
        })
        this.$message.success(this.$t('common.operationSuccess'))
        this.visible = false
        this.$emit('success')
      } catch (error) {
        console.error('Failed to delete data privilege:', error)
      }
    }
  }
}
</script>

<style scoped>
.delete-content {
  text-align: center;
  font-size: 16px;
  margin-bottom: 20px;
}
.dialog-footer {
  text-align: center;
}
</style>