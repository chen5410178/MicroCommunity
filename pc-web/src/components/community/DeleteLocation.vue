<template>
  <el-dialog
    :title="$t('locationManage.confirmOperation')"
    :visible.sync="visible"
    width="30%"
    @close="handleClose"
  >
    <div style="text-align:center">
      <p>{{ $t('locationManage.confirmDeleteLocation') }}</p>
    </div>
    <span slot="footer" class="dialog-footer">
      <el-button @click="visible = false">{{ $t('common.cancel') }}</el-button>
      <el-button type="primary" @click="handleConfirm">{{ $t('common.confirm') }}</el-button>
    </span>
  </el-dialog>
</template>

<script>
import { getCommunityId } from '@/api/community/communityApi'
import { deleteCommunityLocation } from '@/api/community/locationManageApi'

export default {
  name: 'DeleteLocation',
  data() {
    return {
      visible: false,
      locationData: {}
    }
  },
  methods: {
    open(data) {
      this.locationData = data
      this.visible = true
    },
    handleClose() {
      this.locationData = {}
    },
    async handleConfirm() {
      try {
        const params = {
          ...this.locationData,
          communityId: getCommunityId()
        }
        await deleteCommunityLocation(params)
        this.$message.success(this.$t('common.operationSuccess'))
        this.visible = false
        this.$emit('success')
      } catch (error) {
        this.$message.error(this.$t('common.deleteFailed'))
      }
    }
  }
}
</script>