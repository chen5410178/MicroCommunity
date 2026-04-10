<template>
  <el-dialog
    :title="$t('printOwnFee.printTitle')"
    :visible.sync="visible"
    width="50%"
    @close="handleClose">
    <div class="print-own-fee-container">
      <el-form label-width="120px">
        <el-form-item :label="$t('printOwnFee.validityDate')" required>
          <el-date-picker
            v-model="printOwnFeeInfo.targetEndTime"
            type="date"
            :placeholder="$t('printOwnFee.selectEndTime')"
            style="width:100%"
            value-format="yyyy-MM-dd">
          </el-date-picker>
        </el-form-item>
      </el-form>
    </div>
    <span slot="footer" class="dialog-footer">
      <el-button @click="handleCancel">{{ $t('common.cancel') }}</el-button>
      <el-button type="primary" @click="handleConfirm">{{ $t('common.confirm') }}</el-button>
    </span>
  </el-dialog>
</template>

<script>
export default {
  name: 'PrintOwnFee',
  data() {
    return {
      visible: false,
      printOwnFeeInfo: {
        payObjId: '',
        payObjType: '',
        payObjName: '',
        targetEndTime: ''
      }
    }
  },
  methods: {
    open(params) {
      this.clearPrintOwnFeeInfo()
      Object.assign(this.printOwnFeeInfo, params)
      this.visible = true
    },
    handleClose() {
      this.clearPrintOwnFeeInfo()
    },
    handleCancel() {
      this.visible = false
      this.clearPrintOwnFeeInfo()
    },
    handleConfirm() {
      if (!this.printOwnFeeInfo.targetEndTime) {
        this.$message.warning(this.$t('printOwnFee.selectEndTimeTip'))
        return
      }
      this._openPrintOweFeePage()
      this.visible = false
    },
    _openPrintOweFeePage() {
      window.open(`/#/views/fee/printOweFee?payObjId=${this.printOwnFeeInfo.payObjId
        }&payObjType=${this.printOwnFeeInfo.payObjType
        }&payObjName=${encodeURIComponent(this.printOwnFeeInfo.payObjName)
        }&targetEndTime=${this.printOwnFeeInfo.targetEndTime}`)
    },
    clearPrintOwnFeeInfo() {
      this.printOwnFeeInfo = {
        payObjId: '',
        payObjType: '',
        payObjName: '',
        targetEndTime: ''
      }
    }
  }
}
</script>

<style lang="scss" scoped>
.print-own-fee-container {
  padding: 20px;
}
</style>