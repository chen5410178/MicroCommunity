<template>
  <el-dialog
    :title="$t('integralConfigManage.delete.title')"
    :visible.sync="visible"
    width="30%"
    @close="handleClose"
  >
    <div class="delete-content">
      <el-alert
        :title="$t('integralConfigManage.delete.confirmMessage')"
        type="warning"
        :closable="false"
        show-icon
      />
    </div>
    <span slot="footer" class="dialog-footer">
      <el-button @click="visible = false">
        {{ $t('common.cancel') }}
      </el-button>
      <el-button type="primary" @click="handleConfirm" :loading="loading">
        {{ $t('common.confirm') }}
      </el-button>
    </span>
  </el-dialog>
</template>

<script>
import { deleteIntegralConfig } from '@/api/scm/integralConfigManageApi'
import { getCommunityId } from '@/api/community/communityApi'

export default {
  name: 'DeleteIntegralConfig',
  data() {
    return {
      visible: false,
      loading: false,
      deleteData: {
        configId: '',
        communityId: ''
      }
    }
  },
  methods: {
    open(data) {
      this.visible = true
      this.deleteData = {
        configId: data.configId,
        communityId: getCommunityId()
      }
    },
    handleClose() {
      this.deleteData = {
        configId: '',
        communityId: ''
      }
      this.loading = false
    },
    async handleConfirm() {
      try {
        this.loading = true
        await deleteIntegralConfig(this.deleteData)
        this.$message.success(this.$t('common.operationSuccess'))
        this.visible = false
        this.$emit('success')
      } catch (error) {
        this.$message.error(error.message || this.$t('integralConfigManage.message.deleteFailed'))
      } finally {
        this.loading = false
      }
    }
  }
}
</script>

<style scoped>
.delete-content {
  margin-bottom: 20px;
}
</style>