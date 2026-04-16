<template>
  <el-dialog
    :title="$t('deleteAppUserBindingOwner.title')"
    :visible.sync="visible"
    width="30%"
    @close="handleClose">
    <div class="text-center">
      {{ $t('deleteAppUserBindingOwner.confirm') }}
    </div>
    <div slot="footer" class="dialog-footer">
      <el-button @click="visible = false">{{ $t('common.cancel') }}</el-button>
      <el-button type="primary" @click="handleConfirm">{{ $t('common.confirm') }}</el-button>
    </div>
  </el-dialog>
</template>

<script>
import { deleteAppUserBindingOwner } from '@/api/staff/systemUserDetailApi'

export default {
  name: 'DeleteAppUserBindingOwner',
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
    handleClose() {
      this.currentData = null
    },
    async handleConfirm() {
      try {
        const res = await deleteAppUserBindingOwner(this.currentData)
        if (res.code === 0) {
          this.$message.success(this.$t('common.operationSuccess'))
          this.$emit('success')
          this.visible = false
        } else {
          this.$message.error(res.msg || this.$t('common.handleFailed'))
        }
      } catch (error) {
        this.$message.error(this.$t('common.handleFailed'))
      }
    }
  }
}
</script>