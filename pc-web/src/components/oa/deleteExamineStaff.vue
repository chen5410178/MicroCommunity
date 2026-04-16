<template>
  <el-dialog
    :title="$t('examineStaffManage.delete.title')"
    :visible.sync="visible"
    width="30%"
    @close="handleClose"
  >
    <div style="text-align: center;">
      <p>{{ $t('examineStaffManage.delete.confirm') }}</p>
    </div>
    <span slot="footer" class="dialog-footer">
      <el-button @click="visible = false">
        {{ $t('common.cancel') }}
      </el-button>
      <el-button type="primary" @click="handleConfirm">
        {{ $t('common.confirm') }}
      </el-button>
    </span>
  </el-dialog>
</template>

<script>
import { deleteExamineStaff } from '@/api/oa/examineStaffManageApi'
import { getCommunityId } from '@/api/community/communityApi'

export default {
  name: 'DeleteExamineStaff',
  data() {
    return {
      visible: false,
      currentRow: null
    }
  },
  methods: {
    open(row) {
      this.currentRow = row
      this.visible = true
    },
    handleClose() {
      this.currentRow = null
    },
    async handleConfirm() {
      try {
        const params = {
          ...this.currentRow,
          communityId: getCommunityId()
        }
        await deleteExamineStaff(params)
        this.$emit('success')
        this.visible = false
        this.$message.success(this.$t('common.operationSuccess'))
      } catch (error) {
        this.$message.error(this.$t('examineStaffManage.delete.error'))
      }
    }
  }
}
</script>