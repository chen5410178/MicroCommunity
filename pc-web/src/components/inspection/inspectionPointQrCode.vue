<template>
  <el-dialog :title="$t('inspectionPointQrCode.title')" :visible.sync="dialogVisible" width="400px" center>
    <div class="qr-code-container text-center">
      <div id="qrcode" ref="qrcode" style="width: 200px; height: 200px; margin: 0 auto;"></div>
      <p class="inspection-name">{{ inspectionPointQrCodeInfo.inspectionName }}</p>
      <p class="tip-text">{{ $t('inspectionPointQrCode.tip') }}</p>
    </div>

    <span slot="footer" class="dialog-footer">
      <el-button @click="dialogVisible = false">{{ $t('common.close') }}</el-button>
    </span>
  </el-dialog>
</template>

<script>
import QRCode from 'qrcodejs2'
import { getCommunityId } from '@/api/community/communityApi'

export default {
  name: 'InspectionPointQrCode',
  data() {
    return {
      dialogVisible: false,
      inspectionPointQrCodeInfo: {
        url: '',
        inspectionName: ''
      },
      qrcode: null,
      communityId: ''
    }
  },
  created() {
    this.communityId = getCommunityId()
  },
  methods: {
    open(inspectionPoint) {
      this.dialogVisible = true
      this.inspectionPointQrCodeInfo = {
        url:  inspectionPoint.url,
        inspectionName: inspectionPoint.inspectionName
      }

      this.$nextTick(() => {
        this.generateQrCode()
      })
    },


    generateQrCode() {
      // 清除现有的二维码
      if (this.qrcode) {
        this.qrcode.clear()
        this.$refs.qrcode.innerHTML = ''
      }

      // 生成新的二维码
      this.qrcode = new QRCode(this.$refs.qrcode, {
        text: this.inspectionPointQrCodeInfo.url,
        width: 200,
        height: 200,
        colorDark: "#000000",
        colorLight: "#ffffff",
        correctLevel: QRCode.CorrectLevel.L
      })
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
.qr-code-container {
  padding: 20px;
}

.text-center {
  text-align: center;
}

.inspection-name {
  font-size: 18px;
  font-weight: bold;
  margin: 15px 0;
}

.tip-text {
  font-size: 14px;
  color: #606266;
  margin-top: 10px;
}
</style>