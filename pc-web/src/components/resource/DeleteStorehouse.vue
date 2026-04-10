<template>
  <el-dialog
    :title="$t('common.delete')"
    :visible.sync="visible"
    width="30%"
  >
    <p>{{ $t('common.deleteConfirm') }}</p>
    <div slot="footer">
      <el-button @click="visible = false">
        {{ $t('common.cancel') }}
      </el-button>
      <el-button type="primary" @click="deleteStorehouse">
        {{ $t('common.delete') }}
      </el-button>
    </div>
  </el-dialog>
</template>

<script>
import { getCommunityId } from '@/api/community/communityApi'
import { deleteStorehouse } from '@/api/resource/storehouseManageApi'

export default {
  name: 'DeleteStorehouse',
  data() {
    return {
      visible: false,
      storehouse: {}
    }
  },
  methods: {
    open(row) {
      this.storehouse = { ...row }
      this.visible = true
    },
    close() {
      this.visible = false
    },
    async deleteStorehouse() {
      try {
        const params = {
          ...this.storehouse,
          communityId: getCommunityId()
        }
        await deleteStorehouse(params)
        this.$emit('success')
        this.close()
        this.$message.success(this.$t('common.operationSuccess'))
      } catch (error) {
        this.$message.error(error.message || this.$t('storehouseManage.messages.deleteError'))
      }
    }
  }
}
</script>