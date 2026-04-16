<template>
  <el-dialog
    :title="$t('classesManage.delete.title')"
    :visible.sync="visible"
    width="30%"
    @close="handleClose"
  >
    <div class="delete-content">
      <el-alert
        type="warning"
        :title="$t('classesManage.delete.confirmMessage')"
        :closable="false"
        show-icon
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
import { deleteClasses } from '@/api/org/classesManageApi'

export default {
  name: 'DeleteClasses',
  data() {
    return {
      visible: false,
      loading: false,
      classesId: ''
    }
  },
  methods: {
    open(data) {
      this.classesId = data.classesId
      this.visible = true
    },
    handleClose() {
      this.classesId = ''
    },
    async handleConfirm() {
      try {
        this.loading = true
        await deleteClasses({ classesId: this.classesId })
        this.$message.success(this.$t('common.operationSuccess'))
        this.visible = false
        this.$emit('success')
      } catch (error) {
        this.$message.error(error.message || this.$t('common.deleteFailed'))
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