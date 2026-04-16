<template>
  <el-dialog
    :title="$t('reserveCatalogManage.delete.title')"
    :visible.sync="visible"
    width="30%"
    @close="handleClose"
  >
    <div class="delete-content">
      <p>{{ $t('reserveCatalogManage.delete.confirmText') }}</p>
      <p class="delete-name">{{ formData.name }}</p>
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
import { deleteReserveCatalog } from '@/api/scm/reserveCatalogManageApi'
import { getCommunityId } from '@/api/community/communityApi'

export default {
  name: 'DeleteReserveCatalog',
  data() {
    return {
      visible: false,
      loading: false,
      formData: {
        catalogId: '',
        name: '',
        communityId: ''
      }
    }
  },
  methods: {
    open(data) {
      this.visible = true
      this.formData = {
        catalogId: data.catalogId,
        name: data.name,
        communityId: getCommunityId()
      }
    },
    handleClose() {
      this.formData = {
        catalogId: '',
        name: '',
        communityId: ''
      }
      this.loading = false
    },
    async handleConfirm() {
      try {
        this.loading = true
        await deleteReserveCatalog({
          catalogId: this.formData.catalogId,
          communityId: this.formData.communityId
        })
        this.$message.success(this.$t('common.operationSuccess'))
        this.visible = false
        this.$emit('success')
      } catch (error) {
        this.$message.error(error.message || this.$t('reserveCatalogManage.delete.error'))
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
  font-size: 16px;
}
.delete-name {
  font-weight: bold;
  color: #f56c6c;
  margin-top: 10px;
}
</style>