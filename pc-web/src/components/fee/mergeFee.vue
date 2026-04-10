<template>
  <el-dialog
    :title="$t('mergeFee.confirmOperation')"
    :visible.sync="visible"
    width="50%"
    :before-close="closeMergeFeeModel"
  >
    <el-table :data="[]" border style="width: 100%">
      <el-table-column align="center">
        <template >
          {{ $t('mergeFee.mergeDescription') }}
        </template>
      </el-table-column>
    </el-table>
    <span slot="footer" class="dialog-footer">
      <el-button @click="closeMergeFeeModel">{{ $t('mergeFee.cancel') }}</el-button>
      <el-button type="primary" @click="_doMergeFee">{{ $t('mergeFee.confirmMerge') }}</el-button>
    </span>
  </el-dialog>
</template>

<script>
import { mergePayFee } from '@/api/fee/mergeFeeApi'
import { getCommunityId } from '@/api/community/communityApi'

export default {
  name: 'MergeFee',
  data() {
    return {
      visible: false,
      mergeFeeInfo: {},
      communityId: ''
    }
  },
  methods: {
    open(params) {
      this.mergeFeeInfo = params
      this.communityId = getCommunityId()
      this.visible = true
    },
    closeMergeFeeModel() {
      this.visible = false
    },
    async _doMergeFee() {
      try {
        const params = {
          feeId: this.mergeFeeInfo.feeId,
          communityId: this.communityId
        }
        const res = await mergePayFee(params)
        if (res.code === 0) {
          this.closeMergeFeeModel()
          this.$emit('merge-success')
          this.$message.success(this.$t('common.operationSuccess'))
        } else {
          this.$message.error(res.msg)
        }
      } catch (error) {
        this.$message.error(error.message)
      }
    }
  }
}
</script>