<template>
  <el-dialog
    :visible="visible"
    :title="$t('deleteRepairTypeUser.title')"
    width="30%"
    @close="handleClose"
  >
    <div class="text-center">
      <p>{{ $t('deleteRepairTypeUser.confirmDelete') }}</p>
    </div>
    <div slot="footer" class="dialog-footer">
      <el-button @click="handleClose">{{ $t('common.cancel') }}</el-button>
      <el-button type="danger" @click="confirmDelete">{{ $t('common.delete') }}</el-button>
    </div>
  </el-dialog>
</template>

<script>
import { deleteRepairTypeUser } from '@/api/work/repairTypeUserApi'
import { getCommunityId } from '@/api/community/communityApi'

export default {
  name: 'DeleteRepairTypeUser',
  data() {
    return {
      visible: false,
      repairTypeUser: {}
    }
  },
  methods: {
    open(repairTypeUser) {
      this.repairTypeUser = repairTypeUser
      this.visible = true
    },
    handleClose() {
      this.visible = false
      this.repairTypeUser = {}
    },
    async confirmDelete() {
      try {
        const communityId = await getCommunityId()
        const data = {
          communityId: communityId,
          typeUserId: this.repairTypeUser.typeUserId
        }
        
        const response = await deleteRepairTypeUser(data)
        if (response.code === 0) {
          this.$message.success(this.$t('common.operationSuccess'))
          this.$emit('success')
          this.handleClose()
        } else {
          this.$message.error(response.msg || this.$t('common.deleteFailed'))
        }
      } catch (error) {
        console.error('Error deleting repair type user:', error)
        this.$message.error(this.$t('common.deleteFailed'))
      }
    }
  }
}
</script>