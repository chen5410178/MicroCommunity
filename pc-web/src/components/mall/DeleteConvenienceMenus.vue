<template>
  <el-dialog :title="$t('common.deleteConfirm')" :visible.sync="visible" width="30%" @close="handleClose">
    <p>{{ $t('convenienceMenus.deleteConfirm') }}</p>
    <span slot="footer" class="dialog-footer">
      <el-button @click="visible = false">{{ $t('common.cancel') }}</el-button>
      <el-button type="primary" @click="handleConfirm">{{ $t('common.confirm') }}</el-button>
    </span>
  </el-dialog>
</template>

<script>
import { deleteConvenienceMenus } from '@/api/mall/convenienceMenusManageApi'

export default {
  name: 'DeleteConvenienceMenus',
  data() {
    return {
      visible: false,
      convenienceMenusId: ''
    }
  },
  methods: {
    open(row) {
      this.convenienceMenusId = row.convenienceMenusId
      this.visible = true
    },
    handleClose() {
      this.convenienceMenusId = ''
    },
    async handleConfirm() {
      try {
        await deleteConvenienceMenus({ convenienceMenusId: this.convenienceMenusId })
        this.$message.success(this.$t('common.operationSuccess'))
        this.visible = false
        this.$emit('success')
      } catch (error) {
        this.$message.error(error.message)
      }
    }
  }
}
</script>