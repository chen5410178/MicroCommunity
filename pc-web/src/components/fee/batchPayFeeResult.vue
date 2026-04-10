<template>
  <el-dialog
    :title="$t('batchPayConfirm.payTip') || '缴费提示'"
    :visible.sync="visible"
    width="600px"
    :before-close="handleClose"
  >
    <div class="content">
      <p>{{$t('batchPayConfirm.paySuccess') || '缴费成功'}}</p>
    </div>
    <div slot="footer" class="dialog-footer">
      <el-button @click="_back">{{$t('batchPayConfirm.back') || '返回'}}</el-button>
      <el-button type="primary" @click="_printAndBack('ON')">{{$t('batchPayConfirm.mergePrint') || '合并打印'}}</el-button>
      <el-button type="primary" @click="_printAndBack('OFF')">{{$t('batchPayConfirm.printReceipt') || '打印收据'}}</el-button>
    </div>
  </el-dialog>
</template>

<script>
export default {
  name: 'BatchPayFeeResult',
  data() {
    return {
      visible: false,
      receiptIds: '',
      printUrl: ''
    }
  },
  methods: {
    open(params) {
      this.receiptIds = params && params.receiptIds ? params.receiptIds : ''
      this.printUrl = params && params.printUrl ? params.printUrl : ''
      this.visible = true
    },
    handleClose() {
      this.visible = false
    },
    _printAndBack(merge) {
      if (this.printUrl && this.receiptIds) {
        const url = `${this.printUrl}?receiptIds=${this.receiptIds}&merge=${merge}`
        window.open(url)
      }
      this.handleClose()
      this.$emit('closed')
    },
    _back() {
      this.handleClose()
      this.$emit('closed')
    }
  }
}
</script>

<style lang="scss" scoped>
.content {
  padding: 10px 0;
}
.dialog-footer {
  text-align: right;
}
</style>


