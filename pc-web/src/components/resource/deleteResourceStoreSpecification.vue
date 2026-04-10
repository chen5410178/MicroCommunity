<template>
  <el-dialog
    :title="$t('resourceStoreSpecificationManage.confirmDeleteTitle')"
    :visible.sync="visible"
    width="30%">
    <div style="text-align: center; margin-bottom: 20px;">
      <p>{{ $t('resourceStoreSpecificationManage.confirmDeleteMsg') }}</p>
    </div>
    <div slot="footer" class="dialog-footer">
      <el-button @click="visible = false">
        {{ $t('resourceStoreSpecificationManage.cancelDelete') }}
      </el-button>
      <el-button type="danger" @click="confirmDelete">
        {{ $t('resourceStoreSpecificationManage.confirmDelete') }}
      </el-button>
    </div>
  </el-dialog>
</template>

<script>
import { deleteResourceStoreSpecification } from '@/api/resource/resourceStoreSpecificationManageApi'

export default {
  name: 'DeleteResourceStoreSpecification',
  data() {
    return {
      visible: false,
      rssId: ''
    }
  },
  methods: {
    open(row) {
      this.rssId = row.rssId
      this.visible = true
    },
    
    async confirmDelete() {
      try {
        await deleteResourceStoreSpecification(this.rssId)
        this.$message.success(this.$t('common.operationSuccess'))
        this.visible = false
        this.$emit('success')
      } catch (error) {
        console.error('Failed to delete specification:', error)
        this.$message.error(this.$t('common.deleteFailed'))
      }
    }
  }
}
</script>