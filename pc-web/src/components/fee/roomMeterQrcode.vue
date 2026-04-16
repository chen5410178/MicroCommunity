<template>
  <el-dialog :title="$t('meterWater.meterQRCode')" :visible.sync="dialogVisible" width="40%" @close="handleClose">
    <el-form ref="form" :model="form" :rules="rules" label-width="120px" label-position="right" class="text-left">
      <el-form-item :label="$t('meterWater.feeType')" prop="feeTypeCd">
        <el-select v-model="form.feeTypeCd" :placeholder="$t('meterWater.selectFeeType')" style="width: 100%"
          @change="handleFeeTypeChange">
          <el-option v-for="item in feeTypeOptions" :key="item.value" :label="item.label" :value="item.value" />
        </el-select>
      </el-form-item>

      <el-form-item :label="$t('meterWater.feeItem')" prop="configId">
        <el-select v-model="form.configId" :placeholder="$t('meterWater.selectFeeItem')" style="width: 100%"
          @change="generateQRCode">
          <el-option v-for="item in feeConfigs" :key="item.configId" :label="item.feeName" :value="item.configId" />
        </el-select>
        <div class="form-tip">
          {{ $t('meterWater.feeItemTip') }}
        </div>
      </el-form-item>

      <el-form-item :label="$t('meterWater.meterType')" prop="meterType">
        <el-select v-model="form.meterType" :placeholder="$t('meterWater.selectMeterType')" style="width: 100%"
          @change="generateQRCode">
          <el-option v-for="item in meterTypes" :key="item.typeId" :label="item.typeName" :value="item.typeId" />
        </el-select>
      </el-form-item>

      <el-form-item :label="$t('meterWater.room')">
        <el-input v-model="form.ownerName" :placeholder="$t('meterWater.inputRoom')" disabled />
      </el-form-item>

      <el-form-item v-show="showQRCode" :label="$t('meterWater.qrCode')">
        <div class="qr-code-container">
          <div id="qrCodeCanvas" class="qr-code-canvas"></div>
          <div class="qr-code-title">
            {{ form.ownerName }}-{{ form.typeName }}
          </div>
          <div class="qr-code-tip">
            {{ $t('meterWater.qrCodeTip') }}
          </div>
        </div>
      </el-form-item>
    </el-form>

    <span slot="footer" class="dialog-footer">
      <el-button @click="dialogVisible = false">{{ $t('common.cancel') }}</el-button>
    </span>
  </el-dialog>
</template>

<script>
import QRCode from 'qrcodejs2'
import { listFeeConfigs, listMeterTypes } from '@/api/fee/meterWaterManageApi'
import { getDict } from '@/api/community/communityApi'
import { getCommunityId } from '@/api/community/communityApi'
import { getPropertyUrl } from '@/api/system/systemInfoManageApi'

export default {
  name: 'RoomMeterQrcode',
  data() {
    return {
      dialogVisible: false,
      form: {
        feeTypeCd: '',
        configId: '',
        meterType: '',
        roomId: '',
        ownerName: '',
        typeName: '',
        communityId: ''
      },
      rules: {
        feeTypeCd: [
          { required: true, message: this.$t('meterWater.feeTypeRequired'), trigger: 'blur' }
        ],
        configId: [
          { required: true, message: this.$t('meterWater.feeItemRequired'), trigger: 'blur' }
        ],
        meterType: [
          { required: true, message: this.$t('meterWater.meterTypeRequired'), trigger: 'blur' }
        ]
      },
      feeTypeOptions: [],
      feeConfigs: [],
      meterTypes: [],
      showQRCode: false,
      qrCode: null
    }
  },
  created() {
    this.form.communityId = getCommunityId()
    this.loadFeeTypes()
    this.loadMeterTypes()
  },
  methods: {
    open(params) {
      this.resetForm()
      if (params) {
        this.form.roomId = params.roomId
        this.form.ownerName = params.ownerName
          ? `${params.roomName}(${params.ownerName})`
          : params.roomName
      }
      this.dialogVisible = true
    },
    resetForm() {
      this.form = {
        feeTypeCd: '',
        configId: '',
        meterType: '',
        roomId: '',
        ownerName: '',
        typeName: '',
        communityId: getCommunityId()
      }
      this.showQRCode = false
      this.clearQRCode()
    },
    handleClose() {
      this.resetForm()
    },
    clearQRCode() {
      if (this.qrCode) {
        document.getElementById('qrCodeCanvas').innerHTML = ''
        this.qrCode = null
      }
    },
    async loadFeeTypes() {
      try {
        const data = await getDict('pay_fee_config', 'fee_type_cd')
        this.feeTypeOptions = data.map(item => ({
          value: item.statusCd,
          label: item.name
        }))
      } catch (error) {
        console.error('Failed to load fee types:', error)
      }
    },
    async loadMeterTypes() {
      try {
        const { data } = await listMeterTypes({
          communityId: this.form.communityId,
          page: 1,
          row: 50
        })
        this.meterTypes = data
      } catch (error) {
        console.error('Failed to load meter types:', error)
      }
    },
    async handleFeeTypeChange(feeTypeCd) {
      try {
        const { data } = await listFeeConfigs({
          communityId: this.form.communityId,
          feeTypeCd,
          isDefault: 'F',
          valid: '1',
          page: 1,
          row: 20
        })
        this.feeConfigs = data
        this.form.configId = ''
      } catch (error) {
        console.error('Failed to load fee configs:', error)
      }
    },
    generateQRCode() {
      if (!this.form.configId || !this.form.meterType || !this.form.roomId) {
        return
      }

      const meterType = this.meterTypes.find(item => item.typeId === this.form.meterType)
      this.form.typeName = meterType ? meterType.typeName : ''

      this.clearQRCode()

      const propertyUrl = getPropertyUrl()
      const qrCodeUrl = `${propertyUrl}pages/meter/qrcodeMeter?configId=${this.form.configId}&meterType=${this.form.meterType}&roomId=${this.form.roomId}&communityId=${this.form.communityId}`

      this.qrCode = new QRCode(document.getElementById('qrCodeCanvas'), {
        text: qrCodeUrl,
        width: 200,
        height: 200,
        colorDark: '#000000',
        colorLight: '#ffffff',
        correctLevel: QRCode.CorrectLevel.L
      })

      this.showQRCode = true
    }
  }
}
</script>

<style lang="scss" scoped>
.qr-code-container {
  display: flex;
  flex-direction: column;
  align-items: center;
  padding: 20px;

  .qr-code-canvas {
    margin-bottom: 10px;
  }

  .qr-code-title {
    font-size: 18px;
    font-weight: bold;
    margin-bottom: 10px;
    text-align: center;
  }

  .qr-code-tip {
    font-size: 14px;
    color: #666;
    text-align: center;
    max-width: 300px;
  }
}

.form-tip {
  font-size: 12px;
  color: #999;
  margin-top: 5px;
}
</style>