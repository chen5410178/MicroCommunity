<template>
  <el-dialog
    :title="$t('payFeeQrcode.view.title')"
    :visible.sync="visible"
    width="760px"
    @close="handleClose"
  >
    <!-- 双码场景：左小程序二维码 + 右公众号二维码 -->
    <div class="dual-code-content">
      <div class="qr-col">
        <div ref="miniQrcodeRef" class="qr-box">
          <span v-if="!miniQrcodeContent" class="qr-empty">
            {{ $t('payFeeQrcode.view.noQrcode') }}
          </span>
        </div>
        <p class="qr-name">{{ $t('payFeeQrcode.view.miniQrcode') }}</p>
        <p class="qr-tip">{{ $t('payFeeQrcode.view.miniTip') }}</p>
      </div>
      <div class="qr-name-center">{{ form.qrcodeName }}</div>
      <div class="qr-col">
        <div ref="publicQrcodeRef" class="qr-box">
          <span v-if="!publicQrcodeContent" class="qr-empty">
            {{ $t('payFeeQrcode.view.noQrcode') }}
          </span>
        </div>
        <p class="qr-name">{{ $t('payFeeQrcode.view.publicQrcode') }}</p>
        <p class="qr-tip">{{ $t('payFeeQrcode.view.publicTip') }}</p>
      </div>
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
        qrCodeUrl: '',
        pfqId: ''
      }
    }
  },
  computed: {
    origin() {
      if (!this.form.qrCodeUrl) {
        return ''
      }
      const arr = this.form.qrCodeUrl.split('/#/')
      return arr[0] || ''
    },
    miniQrcodeContent() {
      if (!this.form.pfqId || !this.origin) {
        return ''
      }
      return this.origin + '/app/qrcode/payFeeQrcodeMini/' + this.form.pfqId
    },
    publicQrcodeContent() {
      if (!this.form.pfqId || !this.origin) {
        return ''
      }
      return this.origin + '/app/qrcode/payFeeQrcodeH5/' + this.form.pfqId
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
      this.clearQRCode()
      this.form = {
        qrcodeName: '',
        qrCodeUrl: '',
        pfqId: ''
      }
    },
    generateQRCode() {
      const miniEl = this.$refs.miniQrcodeRef
      const publicEl = this.$refs.publicQrcodeRef
      if (!miniEl || !publicEl) {
        return
      }
      miniEl.innerHTML = ''
      publicEl.innerHTML = ''
      this.renderQRCode(miniEl, this.miniQrcodeContent)
      this.renderQRCode(publicEl, this.publicQrcodeContent)
    },
    renderQRCode(el, text) {
      if (!el || !text) {
        return
      }
      new QRCode(el, {
        text,
        width: 200,
        height: 200,
        colorDark: '#000000',
        colorLight: '#ffffff',
        correctLevel: QRCode.CorrectLevel.L
      })
    },
    clearQRCode() {
      const miniEl = this.$refs.miniQrcodeRef
      const publicEl = this.$refs.publicQrcodeRef
      if (miniEl) {
        miniEl.innerHTML = ''
      }
      if (publicEl) {
        publicEl.innerHTML = ''
      }
    }
  },
  beforeDestroy() {
    this.clearQRCode()
  }
}
</script>

<style scoped>
.dual-code-content {
  display: flex;
  justify-content: space-around;
  align-items: center;
  padding: 20px 0;
}
.qr-col {
  text-align: center;
}
.qr-box {
  width: 200px;
  height: 200px;
  margin: 0 auto;
  display: flex;
  align-items: center;
  justify-content: center;
  border: 1px solid #ebeef5;
  border-radius: 4px;
}
.qr-empty {
  font-size: 14px;
  color: #909399;
}
.qr-name {
  margin-top: 12px;
  font-size: 15px;
  font-weight: 500;
  color: #303133;
}
.qr-tip {
  margin-top: 6px;
  font-size: 13px;
  color: #909399;
}
.qr-name-center {
  writing-mode: vertical-rl;
  font-size: 16px;
  font-weight: bold;
  color: #303133;
  letter-spacing: 6px;
}
</style>
