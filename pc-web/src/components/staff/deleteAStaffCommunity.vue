<template>
  <el-dialog
    :title="$t('deleteAStaffCommunity.confirmOperation')"
    :visible.sync="visible"
    width="30%"
    :before-close="handleClose">
    <div class="text-center">
      <p>{{ $t('deleteAStaffCommunity.confirmDelete') }}</p>
    </div>
    <span slot="footer" class="dialog-footer">
      <el-button @click="handleClose">{{ $t('deleteAStaffCommunity.cancel') }}</el-button>
      <el-button type="primary" @click="deleteStaffCommunity">{{ $t('deleteAStaffCommunity.confirm') }}</el-button>
    </span>
  </el-dialog>
</template>

<script>
import { deleteStaffCommunity } from '@/api/staff/aStaffDetailApi'

export default {
  name: 'DeleteAStaffCommunity',
  data() {
    return {
      visible: false,
      community: {}
    }
  },
  methods: {
    open(community) {
      this.community = community
      this.visible = true
    },
    handleClose() {
      this.visible = false
    },
    async deleteStaffCommunity() {
      try {
        await deleteStaffCommunity(this.community)
        this.$message.success(this.$t('common.operationSuccess'))
        this.visible = false
        this.$emit('success')
      } catch (error) {
        this.$message.error(this.$t('deleteAStaffCommunity.deleteError'))
      }
    }
  }
}
</script>

