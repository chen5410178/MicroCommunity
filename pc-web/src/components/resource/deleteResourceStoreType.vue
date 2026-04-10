<template>
  <el-dialog
    id="deleteResourceStoreTypeModel"
    :title="$t('common.delete')"
    :visible.sync="visible"
    width="30%"
    :before-close="handleClose"
  >
    <div style="text-align: center;">
      <span>{{ $t('common.confirmDelete') }}</span>
    </div>
    <span slot="footer" class="dialog-footer">
      <el-button @click="handleClose">{{ $t('common.cancel') }}</el-button>
      <el-button type="primary" @click="deleteResourceStoreType">{{ $t('common.confirm') }}</el-button>
    </span>
  </el-dialog>
</template>

<script>
import { deleteResourceStoreType } from '@/api/resource/resourceStoreManageApi'
import { getCommunityId } from '@/api/community/communityApi'

export default {
  name: 'DeleteResourceStoreType',
  data() {
    return {
      visible: false,
      deleteResourceStoreTypeInfo: {}
    }
  },
  methods: {
    open(resourceStoreType) {
      this.visible = true
      this.deleteResourceStoreTypeInfo = resourceStoreType
    },
    handleClose() {
      this.visible = false
      this.deleteResourceStoreTypeInfo = {}
    },
    async deleteResourceStoreType() {
      this.deleteResourceStoreTypeInfo.communityId = getCommunityId()
      try {
        const response = await deleteResourceStoreType(this.deleteResourceStoreTypeInfo)
        if (response.code === 0) {
          this.$message.success(this.$t('common.operationSuccess'))
          this.handleClose()
          this.$emit('success')
        } else {
          this.$message.error(response.msg)
        }
      } catch (error) {
        this.$message.error(this.$t('common.deleteFailed'))
      }
    }
  }
}
</script>

<style scoped>
</style>