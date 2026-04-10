<template>
  <el-dialog
    :title="$t('common.delete')"
    :visible.sync="visible"
    width="30%"
    @close="handleClose">
    <div style="text-align: center;">
      <p>{{ $t('common.deleteConfirm') }}</p>
    </div>
    <span slot="footer" class="dialog-footer">
      <el-button @click="handleClose">{{ $t('common.cancel') }}</el-button>
      <el-button type="primary" @click="deleteSystemUser">{{ $t('common.confirm') }}</el-button>
    </span>
  </el-dialog>
</template>

<script>
import { deleteSystemUser } from '@/api/staff/systemUserApi'

export default {
  name: 'DeleteSystemUser',
  data() {
    return {
      visible: false,
      currentUser: null
    }
  },
  methods: {
    open(user) {
      this.currentUser = user
      this.visible = true
    },
    handleClose() {
      this.visible = false
    },
    async deleteSystemUser() {
      try {
        await deleteSystemUser({ staffId: this.currentUser.userId })
        this.$message.success(this.$t('common.operationSuccess'))
        this.$emit('success')
        this.handleClose()
      } catch (error) {
        this.$message.error(error)
      }
    }
  }
}
</script>