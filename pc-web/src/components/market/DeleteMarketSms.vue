<template>
    <el-dialog
      :title="$t('marketSms.delete.title')"
      :visible.sync="visible"
      width="30%"
      @close="handleClose"
    >
      <div class="delete-content">
        <p>{{ $t('marketSms.delete.confirm') }}</p>
      </div>
      <div slot="footer" class="dialog-footer">
        <el-button @click="visible = false">
          {{ $t('common.cancel') }}
        </el-button>
        <el-button type="primary" @click="handleConfirm">
          {{ $t('common.confirm') }}
        </el-button>
      </div>
    </el-dialog>
  </template>
  
  <script>
  import { deleteMarketSms } from '@/api/market/marketSmsManageApi'
  
  export default {
    name: 'DeleteMarketSms',
    data() {
      return {
        visible: false,
        smsId: ''
      }
    },
    methods: {
      open(data) {
        this.smsId = data.smsId
        this.visible = true
      },
      handleClose() {
        this.smsId = ''
      },
      async handleConfirm() {
        try {
          await deleteMarketSms({ smsId: this.smsId })
          this.$message.success(this.$t('common.operationSuccess'))
          this.visible = false
          this.$emit('success')
        } catch (error) {
          this.$message.error(error.message)
        }
      }
    }
  }
  </script>
  
  <style scoped>
  .delete-content {
    text-align: center;
    font-size: 16px;
    margin-bottom: 20px;
  }
  </style>