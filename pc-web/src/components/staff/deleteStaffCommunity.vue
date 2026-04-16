<template>
  <el-dialog :title="$t('staffCommunity.deleteConfirmTitle')" :visible.sync="visible" width="30%">

      <div class="text-center">
        <p>{{ $t('staffCommunity.deleteConfirmText') }}</p>
      </div>
      <div slot="footer" class="dialog-footer">
        <el-button @click="visible = false">{{ $t('common.cancel') }}</el-button>
        <el-button type="primary" @click="confirmDelete">{{ $t('common.confirm') }}</el-button>
      </div>

  </el-dialog>
</template>

<script>
import { deleteStaffCommunity } from '@/api/staff/staffCommunityApi'

export default {
  name: 'DeleteStaffCommunity',
  data() {
    return {
      visible: false,
      currentData: {}
    }
  },
  methods: {
    open(data) {
      this.visible = true
      this.currentData = { ...data }
    },
    async confirmDelete() {
      try {
        await deleteStaffCommunity(this.currentData)
        this.$message.success(this.$t('common.operationSuccess'))
        this.$emit('success')
        this.visible = false
      } catch (error) {
        this.$message.error(this.$t('staffCommunity.deleteError'))
      }
    }
  }
}
</script>

<style scoped>
.text-center {
  text-align: center;
  margin: 20px 0;
  font-size: 16px;
}

.dialog-footer {
  text-align: right;
  margin-top: 20px;
}
</style>