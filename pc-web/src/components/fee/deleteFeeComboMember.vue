<template>
  <el-dialog
    :title="$t('common.confirm')"
    :visible.sync="visible"
    width="30%"
  >
    <div class="confirm-content">
      {{ $t('feeComboMemberManage.confirmDelete') }}
    </div>
    <span slot="footer" class="dialog-footer">
      <el-button @click="visible = false">{{ $t('common.cancel') }}</el-button>
      <el-button type="primary" @click="handleConfirm">{{ $t('common.confirm') }}</el-button>
    </span>
  </el-dialog>
</template>

<script>
import { deleteFeeComboMember } from '@/api/fee/feeComboMemberManageApi'
import { getCommunityId } from '@/api/community/communityApi'

export default {
  name: 'DeleteFeeComboMember',
  data() {
    return {
      visible: false,
      formData: {
        comboMemberId: '',
        communityId: ''
      }
    }
  },
  methods: {
    open(params) {
      this.formData = {
        ...params,
        communityId: getCommunityId()
      }
      this.visible = true
    },
    async handleConfirm() {
      try {
        await deleteFeeComboMember(this.formData)
        this.$message.success(this.$t('common.operationSuccess'))
        this.$emit('success')
        this.visible = false
      } catch (error) {
        this.$message.error(error.message || this.$t('feeComboMemberManage.deleteFailed'))
      }
    }
  }
}
</script>

<style scoped>
.confirm-content {
  text-align: center;
  font-size: 16px;
  margin: 20px 0;
}
</style>