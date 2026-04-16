<template>
  <el-dialog
    :title="$t('contractManage.delete.title')"
    :visible.sync="visible"
    width="30%"
    :before-close="handleClose"
  >
    <div style="text-align:center">
      <p>{{ $t('contractManage.delete.confirmMessage') }}</p>
    </div>
    <span slot="footer" class="dialog-footer">
      <el-button @click="handleClose">{{ $t('contractManage.delete.cancel') }}</el-button>
      <el-button type="primary" @click="deleteContract">{{ $t('contractManage.delete.confirm') }}</el-button>
    </span>
  </el-dialog>
</template>

<script>
import { deleteContract } from '@/api/contract/contractManageApi'
import { getCommunityId } from '@/api/community/communityApi'

export default {
  name: 'DeleteContract',
  data() {
    return {
      visible: false,
      contract: {}
    }
  },
  methods: {
    open(contract) {
      this.contract = { ...contract }
      this.visible = true
    },
    handleClose() {
      this.visible = false
    },
    async deleteContract() {
      try {
        const params = {
          ...this.contract,
          communityId: getCommunityId()
        }
        await deleteContract(params)
        this.$message.success(this.$t('common.operationSuccess'))
        this.$emit('success')
        this.handleClose()
      } catch (error) {
        console.error('删除合同失败:', error)
        this.$message.error(error.message || this.$t('contractManage.delete.error'))
      }
    }
  }
}
</script>