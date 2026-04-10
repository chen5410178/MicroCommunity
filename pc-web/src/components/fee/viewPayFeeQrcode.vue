<template>
  <el-dialog
    :title="$t('payFeeQrcode.view.title')"
    :visible.sync="visible"
    width="400px"
    @close="handleClose"
  >
    <div class="text-center">
      <div id="qrcode" style="width: 200px; height: 200px; margin: 0 auto;"></div>
      <p style="margin: 15px 0; font-size: 16px; font-weight: bold;">
        {{ form.qrcodeName }}
      </p>
      <p style="color: #999;">
        {{ $t('payFeeQrcode.view.tip') }}
      </p>
    </div>

    <span slot="footer" class="dialog-footer">
      <el-button @click="visible = false">
        {{ $t('common.close') }}
      </el-button>
    </span>
  </el-dialog>
</template>

<script>
import QRCode from 'qrcodejs2'

export default {
  name: 'ViewPayFeeQrcode',
  data() {
    return {
      visible: false,
      form: {
        qrcodeName: '',
        qrCodeUrl: ''
      },
      qrcode: null
    }
  },
  methods: {
    open(data) {
      this.visible = true
      this.form = { ...data }
      this.$nextTick(() => {
        this.generateQRCode()
      })
    },
    handleClose() {
      if (this.qrcode) {
        this.qrcode.clear()
        document.getElementById('qrcode').innerHTML = ''
      }
      this.form = {
        qrcodeName: '',
        qrCodeUrl: ''
      }
    },
    generateQRCode() {
      if (this.qrcode) {
        this.qrcode.clear()
        document.getElementById('qrcode').innerHTML = ''
      }
      
      if (this.form.qrCodeUrl) {
        this.qrcode = new QRCode(document.getElementById('qrcode'), {
          text: this.form.qrCodeUrl,
          width: 200,
          height: 200,
          colorDark: '#000000',
          colorLight: '#ffffff',
          correctLevel: QRCode.CorrectLevel.L
        })
      }
    }
  },
  beforeDestroy() {
    if (this.qrcode) {
      this.qrcode.clear()
    }
  }
}
</script>

<style scoped>
#qrcode {
  display: flex;
  justify-content: center;
  align-items: center;
  border: 1px solid #eee;
  padding: 10px;
}
</style>