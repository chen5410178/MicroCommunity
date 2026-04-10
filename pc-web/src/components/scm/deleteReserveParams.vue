<template>
  <el-dialog
    :title="$t('reserveParamsManage.deleteTitle')"
    :visible.sync="visible"
    width="500px"
    @close="handleClose"
  >
    <div class="delete-content">
      <el-alert
        type="warning"
        :title="$t('reserveParamsManage.deleteConfirm')"
        :description="$t('reserveParamsManage.deleteTip')"
        show-icon
        :closable="false"
      />
    </div>

    <div slot="footer" class="dialog-footer">
      <el-button @click="visible = false">
        {{ $t('common.cancel') }}
      </el-button>
      <el-button type="danger" @click="handleConfirm" :loading="loading">
        {{ $t('common.confirmDelete') }}
      </el-button>
    </div>
  </el-dialog>
</template>

<script>
import { deleteReserveParams } from '@/api/scm/reserveParamsManageApi'
import { getCommunityId } from '@/api/community/communityApi'

export default {
  name: 'DeleteReserveParams',
  data() {
    return {
      visible: false,
      loading: false,
      currentRow: {
        paramsId: '',
        communityId: ''
      }
    }
  },
  methods: {
    open(row) {
      this.visible = true
      this.currentRow = {
        paramsId: row.paramsId,
        communityId: getCommunityId()
      }
    },
    async handleConfirm() {
      try {
        this.loading = true
        await deleteReserveParams(this.currentRow)
        this.$message.success(this.$t('common.operationSuccess'))
        this.visible = false
        this.$emit('success')
      } catch (error) {
        this.$message.error(error.message || this.$t('common.deleteFailed'))
      } finally {
        this.loading = false
      }
    },
    handleClose() {
      this.currentRow = {
        paramsId: '',
        communityId: ''
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