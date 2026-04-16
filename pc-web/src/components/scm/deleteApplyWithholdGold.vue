<template>
  <el-dialog
    :title="$t('deleteApplyWithholdGold.confirmTitle')"
    :visible.sync="visible"
    width="30%"
    :before-close="handleClose"
  >
    <div class="text-center">
      <p>{{ $t('deleteApplyWithholdGold.confirmText') }}</p>
    </div>
    <span slot="footer" class="dialog-footer">
      <el-button @click="handleClose">{{ $t('common.cancel') }}</el-button>
      <el-button type="primary" @click="confirmDelete" :loading="loading">
        {{ $t('common.confirmDelete') }}
      </el-button>
    </span>
  </el-dialog>
</template>

<script>
import { deleteGoldWithHold } from '@/api/scm/goldApi'

export default {
  name: 'DeleteApplyWithholdGold',
  data() {
    return {
      visible: false,
      loading: false,
      currentData: null
    }
  },
  methods: {
    open(data) {
      this.currentData = data
      this.visible = true
    },
    handleClose() {
      this.visible = false
    },
    async confirmDelete() {
      try {
        this.loading = true
        await deleteGoldWithHold({
          gwId: this.currentData.gwId,
          communityId: this.currentData.communityId
        })
        this.$emit('success')
        this.$message.success(this.$t('common.operationSuccess'))
        this.visible = false
      } catch (error) {
        console.error('Delete failed:', error)
        this.$message.error(this.$t('deleteApplyWithholdGold.deleteFailed'))
      } finally {
        this.loading = false
      }
    }
  }
}
</script>