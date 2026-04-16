<template>
  <el-dialog
    :title="$t('questionTitle.delete.title')"
    :visible.sync="visible"
    width="500px"
    @close="handleClose"
  >
    <div class="delete-content">
      <el-alert
        :title="$t('questionTitle.delete.confirmMessage')"
        type="warning"
        :closable="false"
        show-icon
      />
    </div>

    <div slot="footer" class="dialog-footer">
      <el-button @click="visible = false">
        {{ $t('common.cancel') }}
      </el-button>
      <el-button type="primary" @click="handleConfirm" :loading="loading">
        {{ $t('common.confirm') }}
      </el-button>
    </div>
  </el-dialog>
</template>

<script>
import { deleteQuestionTitle } from '@/api/oa/questionTitleApi'
import { getCommunityId } from '@/api/community/communityApi'

export default {
  name: 'DeleteQuestionTitle',
  data() {
    return {
      visible: false,
      loading: false,
      formData: {
        titleId: '',
        communityId: ''
      }
    }
  },
  methods: {
    open(data) {
      this.formData = {
        titleId: data.titleId,
        communityId: getCommunityId()
      }
      this.visible = true
    },
    handleClose() {
      this.formData = {
        titleId: '',
        communityId: ''
      }
      this.loading = false
    },
    async handleConfirm() {
      try {
        this.loading = true
        await deleteQuestionTitle(this.formData)
        this.$message.success(this.$t('common.operationSuccess'))
        this.$emit('success')
        this.visible = false
      } catch (error) {
        console.error(error)
      } finally {
        this.loading = false
      }
    }
  }
}
</script>

<style scoped>
.delete-content {
  padding: 20px 0;
  text-align: center;
}
</style>