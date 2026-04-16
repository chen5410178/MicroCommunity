<template>
  <el-dialog
    :title="$t('meterTypeManage.deleteTitle')"
    :visible.sync="visible"
    width="30%"
    @close="handleClose"
  >
    <div class="delete-content">
      <p>{{ $t('meterTypeManage.deleteConfirm') }}</p>
      <p class="delete-tip">{{ $t('meterTypeManage.deleteTip') }}</p>
    </div>
    <div slot="footer" class="dialog-footer">
      <el-button @click="visible = false">{{ $t('common.cancel') }}</el-button>
      <el-button type="primary" @click="handleConfirm" :loading="loading">{{ $t('common.confirm') }}</el-button>
    </div>
  </el-dialog>
</template>

<script>
import { deleteMeterType } from '@/api/fee/meterTypeManageApi'
import { getCommunityId } from '@/api/community/communityApi'

export default {
  name: 'DeleteMeterType',
  data() {
    return {
      visible: false,
      loading: false,
      form: {
        typeId: '',
        communityId: ''
      }
    }
  },
  methods: {
    open(row) {
      this.form = {
        typeId: row.typeId,
        communityId: getCommunityId()
      }
      this.visible = true
    },
    handleClose() {
      this.visible = false
      this.loading = false
    },
    async handleConfirm() {
      try {
        this.loading = true
        await deleteMeterType(this.form)
        this.$message.success(this.$t('common.operationSuccess'))
        this.visible = false
        this.$emit('success')
      } catch (error) {
        this.$message.error(error.message || this.$t('meterTypeManage.deleteFailed'))
      } finally {
        this.loading = false
      }
    }
  }
}
</script>

<style scoped>
.delete-content {
  text-align: center;
  padding: 20px 0;
}
.delete-tip {
  color: #f56c6c;
  margin-top: 10px;
}
</style>