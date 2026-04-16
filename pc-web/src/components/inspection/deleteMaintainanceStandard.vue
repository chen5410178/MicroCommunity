<template>
  <el-dialog
    :title="$t('maintainanceStandard.delete.title')"
    :visible.sync="visible"
    width="30%"
    center
  >
    <div class="text-center">
      <p>{{ $t('maintainanceStandard.delete.confirmText') }}</p>
    </div>
    <span slot="footer" class="dialog-footer">
      <el-button @click="visible = false">
        {{ $t('common.cancel') }}
      </el-button>
      <el-button type="primary" @click="handleConfirm" :loading="loading">
        {{ $t('common.confirm') }}
      </el-button>
    </span>
  </el-dialog>
</template>

<script>
import { deleteMaintainanceStandard } from '@/api/inspection/maintainanceStandardManageApi'
import { getCommunityId } from '@/api/community/communityApi'

export default {
  name: 'DeleteMaintainanceStandard',
  data() {
    return {
      visible: false,
      loading: false,
      currentData: {
        standardId: '',
        communityId: ''
      }
    }
  },
  methods: {
    open(row) {
      this.currentData = {
        standardId: row.standardId,
        communityId: getCommunityId()
      }
      this.visible = true
    },
    async handleConfirm() {
      try {
        this.loading = true
        await deleteMaintainanceStandard(this.currentData)
        this.$message.success(this.$t('common.operationSuccess'))
        this.visible = false
        this.$emit('success')
      } catch (error) {
        this.$message.error(error.message || this.$t('maintainanceStandard.message.deleteFailed'))
      } finally {
        this.loading = false
      }
    }
  }
}
</script>