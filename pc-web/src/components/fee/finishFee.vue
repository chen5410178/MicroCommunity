<template>
    <el-dialog
      :title="$t('finishFee.confirmOperation')"
      :visible.sync="dialogVisible"
      width="30%"
      @close="closeFinishFeeModel"
    >
      <el-row>
        <el-col :span="24" align="center">
          <p>{{ $t('finishFee.confirmFinishFee') }}</p>
        </el-col>
      </el-row>
      <span slot="footer" class="dialog-footer">
        <el-button @click="closeFinishFeeModel">{{ $t('finishFee.cancel') }}</el-button>
        <el-button type="primary" @click="finishFee">{{ $t('finishFee.confirm') }}</el-button>
      </span>
    </el-dialog>
  </template>
  
  <script>
  import { finishFee } from '@/api/fee/finishFeeApi'
  import { getCommunityId } from '@/api/community/communityApi'
  
  export default {
    name: 'FinishFee',
    data() {
      return {
        dialogVisible: false,
        finishFeeInfo: {},
        communityId: ''
      }
    },
    created() {
      this.communityId = getCommunityId()
    },
    methods: {
      open(params) {
        this.finishFeeInfo = params
        this.dialogVisible = true
      },
      closeFinishFeeModel() {
        this.dialogVisible = false
      },
      async finishFee() {
        try {
          const params = {
            ...this.finishFeeInfo,
            communityId: this.communityId
          }
          await finishFee(params)
          this.$message.success(this.$t('common.operationSuccess'))
          this.closeFinishFeeModel()
          this.$emit('success')
        } catch (error) {
          this.$message.error(error.message || this.$t('finishFee.errorMessage'))
        }
      }
    }
  }
  </script>