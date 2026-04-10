<template>
  <el-dialog
    :title="$t('deleteFee.title')"
    :visible.sync="visible"
    width="30%"
    :before-close="handleClose"
  >
    <p>{{ $t('deleteFee.confirmMessage') }}</p>
    <span slot="footer" class="dialog-footer">
      <el-button @click="handleClose">{{ $t('common.cancel') }}</el-button>
      <el-button type="primary" @click="deleteFee">{{ $t('common.confirm') }}</el-button>
    </span>
  </el-dialog>
</template>

<script>
import { deleteFee } from '@/api/fee/deleteFeeApi'
import { getCommunityId } from '@/api/community/communityApi'

export default {
  name: 'DeleteFee',
  data() {
    return {
      visible: false,
      deleteFeeInfo: {}
    }
  },
  methods: {
    open(params) {
      this.deleteFeeInfo = params
      this.visible = true
    },
    handleClose() {
      this.visible = false
    },
    async deleteFee() {
      this.deleteFeeInfo.communityId = getCommunityId()
      
      try {
        const res = await deleteFee(this.deleteFeeInfo)
        if (res.code == 0) {
          this.$message.success(this.$t('common.operationSuccess'))
          this.$emit('success')
          this.handleClose()
        } else {
          this.$message.error(res.msg)
        }
      } catch (error) {
        console.error('请求失败:', error)
        this.$message.error(error || this.$t('common.operateFail'))
      }
    }
  }
}
</script>