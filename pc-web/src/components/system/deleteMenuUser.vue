<template>
  <el-dialog
    :title="$t('menuUserManage.delete.title')"
    :visible.sync="visible"
    width="30%"
    @close="handleClose"
  >
    <div class="delete-content">
      <i class="el-icon-warning" style="color:#E6A23C;font-size:24px;margin-right:10px;"></i>
      <span>{{ $t('menuUserManage.delete.confirm') }}</span>
    </div>
    <div slot="footer" class="dialog-footer">
      <el-button @click="visible = false">{{ $t('common.cancel') }}</el-button>
      <el-button type="primary" @click="handleConfirm">{{ $t('common.confirm') }}</el-button>
    </div>
  </el-dialog>
</template>

<script>
import { deleteMenuUser } from '@/api/system/menuUserManageApi'

export default {
  name: 'DeleteMenuUser',
  data() {
    return {
      visible: false,
      currentData: null
    }
  },
  methods: {
    open(data) {
      this.currentData = data
      this.visible = true
    },
    async handleConfirm() {
      try {
        await deleteMenuUser(this.currentData)
        this.$message.success(this.$t('common.operationSuccess'))
        this.$emit('success')
        this.visible = false
      } catch (error) {
        this.$message.error(error.message || this.$t('menuUserManage.delete.error'))
      }
    },
    handleClose() {
      this.currentData = null
    }
  }
}
</script>

<style scoped>
.delete-content {
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 16px;
  padding: 20px 0;
}
</style>