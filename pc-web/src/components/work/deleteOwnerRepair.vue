<template>
  <el-dialog :title="$t('common.delete')" :visible.sync="visible" width="30%">
    <p>{{ $t('common.deleteConfirm') }}</p>

    <span slot="footer" class="dialog-footer">
      <el-button @click="visible = false">{{ $t('common.cancel') }}</el-button>
      <el-button type="primary" @click="confirmDelete">{{ $t('common.confirm') }}</el-button>
    </span>
  </el-dialog>
</template>

<script>
import { deleteOwnerRepair } from '@/api/work/repairPoolManageApi'
import { getCommunityId } from '@/api/community/communityApi'

export default {
  name: 'DeleteOwnerRepair',
  data() {
    return {
      visible: false,
      repairId: ''
    }
  },
  methods: {
    open(_repair) {
      this.repairId = _repair.repairId
      this.visible = true
    },

    async confirmDelete() {
      try {
        await deleteOwnerRepair({
          repairId: this.repairId,
          communityId: getCommunityId()
        })

        this.$message.success(this.$t('common.operationSuccess'))
        this.visible = false
        this.$emit('success')
      } catch (error) {
        console.error('Delete repair failed:', error)
      }
    }
  }
}
</script>