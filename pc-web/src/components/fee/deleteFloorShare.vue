<template>
  <el-dialog
    :title="$t('floorShare.deleteConfirm')"
    :visible.sync="dialogVisible"
    width="30%"
    center
  >
    <div class="text-center">
      <p>{{ $t('floorShare.deleteMessage') }}</p>
    </div>
    <div slot="footer" class="dialog-footer">
      <el-button @click="dialogVisible = false">{{ $t('common.cancel') }}</el-button>
      <el-button type="primary" @click="handleConfirm" :loading="loading">{{ $t('common.confirm') }}</el-button>
    </div>
  </el-dialog>
</template>

<script>
import { deleteFloorShareMeter } from '@/api/fee/floorShareApi'
import { getCommunityId } from '@/api/community/communityApi'

export default {
  name: 'DeleteFloorShare',
  data() {
    return {
      dialogVisible: false,
      loading: false,
      form: {
        fsmId: '',
        communityId: ''
      }
    }
  },
  methods: {
    open(data) {
      this.form = {
        fsmId: data.fsmId,
        communityId: getCommunityId()
      }
      this.dialogVisible = true
    },
    async handleConfirm() {
      this.loading = true
      try {
        await deleteFloorShareMeter(this.form)
        this.$message.success(this.$t('common.operationSuccess'))
        this.dialogVisible = false
        this.$emit('success')
      } catch (error) {
        this.$message.error(error.message || this.$t('floorShare.deleteFailed'))
      } finally {
        this.loading = false
      }
    }
  }
}
</script>

<style lang="scss" scoped>
.text-center {
  text-align: center;
  font-size: 16px;
  margin-bottom: 20px;
}

.dialog-footer {
  text-align: center;
}
</style>