<template>
  <el-dialog
    :title="$t('deleteApplyWithholdIntegral.confirmTitle')"
    :visible.sync="visible"
    width="30%"
    center
  >
    <div class="text-center">
      <p>{{ $t('deleteApplyWithholdIntegral.confirmText') }}</p>
    </div>
    <span slot="footer" class="dialog-footer">
      <el-button @click="close">{{ $t('deleteApplyWithholdIntegral.cancel') }}</el-button>
      <el-button type="primary" @click="confirmDelete" :loading="loading">
        {{ $t('deleteApplyWithholdIntegral.confirmDelete') }}
      </el-button>
    </span>
  </el-dialog>
</template>

<script>
import { deleteIntegralWithdrawal } from '@/api/scm/communityIntegralApi'
import { getCommunityId } from '@/api/community/communityApi'

export default {
  name: 'DeleteApplyWithholdIntegral',
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
    close() {
      this.visible = false
      this.loading = false
    },
    async confirmDelete() {
      try {
        this.loading = true
        const params = {
          ...this.currentData,
          communityId: getCommunityId()
        }
        await deleteIntegralWithdrawal(params)
        this.$emit('success')
        this.$message.success(this.$t('common.operationSuccess'))
        this.close()
      } catch (error) {
        console.error('Failed to delete integral withdrawal:', error)
        this.$message.error(this.$t('deleteApplyWithholdIntegral.deleteFailed'))
      } finally {
        this.loading = false
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