<template>
  <el-dialog
    :title="$t('classesManage.state.title')"
    :visible.sync="visible"
    width="30%"
    @close="handleClose"
  >
    <div class="state-content">
      <el-alert
        :type="formData.state === '1001' ? 'success' : 'warning'"
        :title="
          $t('classesManage.state.confirmMessage', {
            action: formData.stateName,
            name: $t('classesManage.state.classes')
          })
        "
        :closable="false"
        show-icon
      />
    </div>
    <div slot="footer" class="dialog-footer">
      <el-button @click="visible = false">
        {{ $t('common.cancel') }}
      </el-button>
      <el-button
        :type="formData.state === '1001' ? 'success' : 'warning'"
        @click="handleConfirm"
        :loading="loading"
      >
        {{ $t('common.confirm') }}
      </el-button>
    </div>
  </el-dialog>
</template>

<script>
import { updateClassesState } from '@/api/org/classesManageApi'

export default {
  name: 'ClassesState',
  data() {
    return {
      visible: false,
      loading: false,
      formData: {
        classesId: '',
        stateName: '',
        state: ''
      }
    }
  },
  methods: {
    open(data) {
      this.formData = {
        classesId: data.classesId,
        stateName: data.stateName,
        state: data.state
      }
      this.visible = true
    },
    handleClose() {
      this.formData = {
        classesId: '',
        stateName: '',
        state: ''
      }
    },
    async handleConfirm() {
      try {
        this.loading = true
        await updateClassesState(this.formData)
        this.$message.success(
          this.$t('classesManage.state.successMessage', {
            action: this.formData.stateName
          })
        )
        this.visible = false
        this.$emit('success')
      } catch (error) {
        this.$message.error(error.message || this.$t('common.operationFailed'))
      } finally {
        this.loading = false
      }
    }
  }
}
</script>

<style scoped>
.state-content {
  margin-bottom: 20px;
}
</style>