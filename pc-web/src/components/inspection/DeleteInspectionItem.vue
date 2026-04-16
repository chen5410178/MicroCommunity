<template>
  <el-dialog
    :title="$t('inspectionItemManage.confirmDelete')"
    :visible.sync="visible"
    width="30%"
  >
    <div class="text-center">
      <p>{{ $t('inspectionItemManage.confirmDeleteMsg') }}: {{ formData.itemName }}?</p>
    </div>
    <span slot="footer" class="dialog-footer">
      <el-button @click="visible = false">
        {{ $t('inspectionItemManage.cancelDelete') }}
      </el-button>
      <el-button type="primary" @click="handleSubmit" :loading="loading">
        {{ $t('inspectionItemManage.confirmDeleteBtn') }}
      </el-button>
    </span>
  </el-dialog>
</template>

<script>
import { deleteInspectionItem } from '@/api/inspection/inspectionItemManageApi'
import { getCommunityId } from '@/api/community/communityApi'

export default {
  name: 'DeleteInspectionItem',
  data() {
    return {
      visible: false,
      loading: false,
      formData: {
        itemId: '',
        itemName: '',
        communityId: ''
      }
    }
  },
  methods: {
    open(item) {
      this.formData = {
        itemId: item.itemId,
        itemName: item.itemName,
        communityId: getCommunityId()
      }
      this.visible = true
    },
    async handleSubmit() {
      this.loading = true
      try {
        await deleteInspectionItem(this.formData)
        this.$message.success(this.$t('common.operationSuccess'))
        this.visible = false
        this.$emit('success')
      } catch (error) {
        console.error('删除巡检项目失败:', error)
        this.$message.error(error.message || this.$t('inspectionItemManage.deleteFailed'))
      } finally {
        this.loading = false
      }
    }
  }
}
</script>