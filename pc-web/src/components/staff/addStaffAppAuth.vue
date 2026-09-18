<template>
  <el-dialog :title="$t('addStaffAppAuth.title')" :visible.sync="visible" width="50%" @close="handleClose">
    <el-form label-width="120px">
      <el-form-item :label="$t('addStaffAppAuth.authType')">
        <el-select v-model="qrCodeInfo.appType" style="width:100%" @change="handleChangeAppType">
          <el-option disabled value="" :label="$t('addStaffAppAuth.requiredSelect')" />
          <el-option value="WECHAT" :label="$t('addStaffAppAuth.wechat')" />
        </el-select>
      </el-form-item>

      <el-form-item :label="$t('addStaffAppAuth.qrcode')">
        <el-row :gutter="20">
          <el-col :span="10">
            <div class="qrcode-container">
              <img v-if="qrCodeInfo.showRefresh" class="refresh-icon" src="/assets/qrcode-refresh.png"
                @click="handleChangeAppType" />
              <div id="qrcode" class="qrcode"></div>
            </div>
          </el-col>
          <el-col :span="14">
            <p>{{ $t('addStaffAppAuth.scanTip') }}</p>
          </el-col>
        </el-row>
      </el-form-item>
    </el-form>

    <div slot="footer" class="dialog-footer">
      <el-button @click="visible = false">
        {{ $t('common.cancel') }}
      </el-button>
      <el-button type="primary" @click="handleFinish">
        {{ $t('addStaffAppAuth.finishScan') }}
      </el-button>
    </div>
  </el-dialog>
</template>

<script>
import { generatorQrCode, queryStaffAppAuth } from '@/api/staff/staffAppAuthManageApi' // 假设这是员工认证专用API
import QRCode from 'qrcodejs2'
import { getCommunityId } from '@/api/community/communityApi'

const AUTH_POLL_INTERVAL = 3000
const AUTH_POLL_TIMEOUT = 5 * 60 * 1000

export default {
  name: 'StaffAuthQrCode',
  data() {
    return {
      visible: false,
      qrCodeInfo: {
        appType: 'WECHAT',
        showRefresh: false,
        timer: null,
        qrCodeUrl: '', // 新增字段存储二维码URL
        authTimer: null,
        authStartTime: 0
      }
    }
  },
  methods: {
    open() {
      this.visible = true
      this.$nextTick(() => {
        this.generateQrCode()
        this.startAuthPolling()
      })
    },
    handleClose() {
      clearTimeout(this.qrCodeInfo.timer)
      this.stopAuthPolling()
    },
    startAuthPolling() {
      this.stopAuthPolling()
      this.qrCodeInfo.authStartTime = Date.now()
      this.qrCodeInfo.authTimer = setInterval(() => {
        this.pollAuthState()
      }, AUTH_POLL_INTERVAL)
    },
    stopAuthPolling() {
      if (this.qrCodeInfo.authTimer) {
        clearInterval(this.qrCodeInfo.authTimer)
        this.qrCodeInfo.authTimer = null
      }
    },
    async pollAuthState() {
      if (Date.now() - this.qrCodeInfo.authStartTime > AUTH_POLL_TIMEOUT) {
        this.stopAuthPolling()
        return
      }
      try {
        const { data } = await queryStaffAppAuth({ page: 1, row: 1 })
        if (data && data.state === '2002') {
          this.stopAuthPolling()
          this.$message.success(this.$t('addStaffAppAuth.authSuccess'))
          this.$emit('success')
          this.visible = false
        }
      } catch (error) {
        // 轮询失败不打断当前流程，等待下次轮询
      }
    },
    startRefreshTimer() {
      this.qrCodeInfo.timer = setTimeout(() => {
        this.qrCodeInfo.showRefresh = true
      }, 120000) // 2分钟后显示刷新按钮
    },
    async generateQrCode() {
      this.qrCodeInfo.showRefresh = false
      clearTimeout(this.qrCodeInfo.timer)

      try {
        const params = {
          communityId: getCommunityId(),
          appType: this.qrCodeInfo.appType
        }
        const { data } = await generatorQrCode(params)
        this.qrCodeInfo.qrCodeUrl = data
        this.renderQrCode(data)
        this.startRefreshTimer()
      } catch (error) {
        this.$message.error(this.$t('addStaffAppAuth.qrcodeError'))
        this.qrCodeInfo.showRefresh = true // 出错时立即显示刷新按钮
      }
    },
    renderQrCode(url) {
      const container = document.getElementById('qrcode')
      container.innerHTML = ''
      new QRCode(container, {
        text: url,
        width: 200,
        height: 200,
        colorDark: '#000000',
        colorLight: '#ffffff',
        correctLevel: QRCode.CorrectLevel.H
      })
    },
    async handleFinish() {
      this.stopAuthPolling()
      try {
        const { data } = await queryStaffAppAuth({ page: 1, row: 1 })
        if (data && data.state === '2002') {
          this.$message.success(this.$t('addStaffAppAuth.authSuccess'))
        } else {
          this.$message.warning(this.$t('addStaffAppAuth.authPending'))
        }
      } catch (error) {
        this.$message.error(this.$t('addStaffAppAuth.authFail'))
      }
      this.$emit('success')
      this.visible = false
    },
    handleChangeAppType() {
      this.generateQrCode()
    }
  }
}
</script>

<style lang="scss" scoped>
/* 保持现有样式不变 */
.qrcode-container {
  position: relative;
  width: 200px;
  height: 200px;
  border: 1px solid #eee;
  padding: 10px;
  display: flex;
  align-items: center;
  justify-content: center;

  .refresh-icon {
    position: absolute;
    top: 10px;
    left: 10px;
    z-index: 10;
    width: 24px;
    height: 24px;
    cursor: pointer;
    transition: all 0.3s;

    &:hover {
      transform: rotate(180deg);
    }
  }
}
</style>