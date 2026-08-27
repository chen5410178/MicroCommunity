<template>
  <div class="parking-space-create-fee-add">
    <el-dialog :title="$t('parkingSpaceCreateFeeAdd.createFee')" :visible.sync="dialogVisible" width="800px"
      :before-close="handleClose">
      <el-form :model="parkingSpaceCreateFeeAddInfo" :rules="rules" ref="createForm" label-width="120px">
        <el-form-item :label="$t('parkingSpaceCreateFeeAdd.parkingSpace')">
          <el-input v-model="parkingSpaceCreateFeeAddInfo.locationTypeCdName" disabled
            :placeholder="$t('parkingSpaceCreateFeeAdd.enterParkingSpace')"></el-input>
        </el-form-item>

        <el-form-item :label="$t('parkingSpaceCreateFeeAdd.feeType')" prop="feeTypeCd">
          <el-select v-model="parkingSpaceCreateFeeAddInfo.feeTypeCd" @change="_changeParkingSpaceFeeTypeCd" style="width:100%">
            <el-option :label="$t('parkingSpaceCreateFeeAdd.selectFeeType')" value="" disabled></el-option>
            <el-option v-for="(item, index) in filteredFeeTypeCds" :key="index" :label="item.name"
              :value="item.statusCd"></el-option>
          </el-select>
        </el-form-item>

        <el-form-item :label="$t('parkingSpaceCreateFeeAdd.feeItem')" prop="configId">
          <el-select v-model="parkingSpaceCreateFeeAddInfo.configId" @change="_parkingSpaceCreateFeeAddIfOnceFee" style="width:100%">
            <el-option :label="$t('parkingSpaceCreateFeeAdd.selectFeeItem')" value="" disabled></el-option>
            <el-option v-for="(item, index) in parkingSpaceCreateFeeAddInfo.feeConfigs" :key="index" :label="item.feeName"
              :value="item.configId"></el-option>
          </el-select>
        </el-form-item>

        <el-form-item v-if="parkingSpaceCreateFeeAddInfo.computingFormula === '4004'" :label="$t('parkingSpaceCreateFeeAdd.feeAmount')"
          prop="amount">
          <el-input v-model="parkingSpaceCreateFeeAddInfo.amount" :placeholder="$t('parkingSpaceCreateFeeAdd.enterFeeAmount')"></el-input>
        </el-form-item>

        <el-form-item :label="$t('parkingSpaceCreateFeeAdd.billingStartTime')" prop="startTime">
          <el-date-picker v-model="parkingSpaceCreateFeeAddInfo.startTime" type="datetime"
            :placeholder="$t('parkingSpaceCreateFeeAdd.enterBillingStartTime')" value-format="yyyy-MM-dd HH:mm:ss"
            style="width:100%" @change="validateStartTime"></el-date-picker>
        </el-form-item>

        <el-form-item :label="$t('parkingSpaceCreateFeeAdd.billingEndTime')" prop="endTime">
          <el-date-picker v-model="parkingSpaceCreateFeeAddInfo.endTime" type="datetime"
            :placeholder="$t('parkingSpaceCreateFeeAdd.enterBillingEndTime')" value-format="yyyy-MM-dd HH:mm:ss"
            style="width:100%" @change="validateEndTime"></el-date-picker>
        </el-form-item>
      </el-form>

      <div slot="footer" class="dialog-footer">
        <el-button @click="handleClose">
          {{ $t('common.cancel') }}
        </el-button>
        <el-button type="primary" @click="submitForm" :loading="loading">
          {{ $t('common.submit') }}
        </el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { getCommunityId } from '@/api/community/communityApi'
import { getDict } from '@/api/community/communityApi'
import { saveParkingSpaceCreateFee, listFeeConfigs, listParkingAreas } from '@/api/fee/parkingSpaceCreateFeeApi'

export default {
  name: 'ParkingSpaceCreateFeeAdd',
  data() {
    return {
      communityId: '',
      dialogVisible: false,
      loading: false,
      parkingSpaceCreateFeeAddInfo: {
        feeTypeCds: [],
        feeConfigs: [],
        parkingAreas: [],
        locationObjId: '',
        locationTypeCd: '2000',
        psId: '',
        carId: '',
        feeTypeCd: '',
        configId: '',
        carState: ['H', 'S'],
        isMore: false,
        locationTypeCdName: '',
        startTime: '',
        paId: '',
        feeFlag: '',
        computingFormula: '',
        amount: '',
        endTime: ''
      },
      rules: {
        feeTypeCd: [
          { required: true, message: this.$t('parkingSpaceCreateFeeAdd.feeTypeRequired'), trigger: 'change' }
        ],
        configId: [
          { required: true, message: this.$t('parkingSpaceCreateFeeAdd.feeItemRequired'), trigger: 'change' }
        ],
        carState: [
          { required: true, message: this.$t('parkingSpaceCreateFeeAdd.billingTypeRequired') }
        ],
        startTime: [
          { required: true, message: this.$t('parkingSpaceCreateFeeAdd.billingStartTimeRequired'), trigger: 'change' },
          { validator: this.validateDate, trigger: 'change' }
        ],
        amount: [
          { required: true, message: this.$t('parkingSpaceCreateFeeAdd.amountRequired'), trigger: 'blur' },
          { pattern: /^\d+(\.\d{1,2})?$/, message: this.$t('parkingSpaceCreateFeeAdd.amountFormatError'), trigger: 'blur' }
        ]
      }
    }
  },
  computed: {
    filteredFeeTypeCds() {
      return (this.parkingSpaceCreateFeeAddInfo.feeTypeCds || []).filter(item =>
        item.statusCd !== '888800010009' && item.statusCd !== '888800010011'
      )
    }
  },
  async created() {
    this.communityId = await getCommunityId()
    await this.loadDictData()
    await this.loadParkingAreas()
    this.parkingSpaceCreateFeeAddInfo.startTime = this.formatDateTime(new Date())
  },
  methods: {
    async loadDictData() {
      try {
        const data = await getDict('pay_fee_config', 'fee_type_cd')
        this.parkingSpaceCreateFeeAddInfo.feeTypeCds = data
      } catch (error) {
        console.error('获取字典数据失败:', error)
      }
    },

    async loadParkingAreas() {
      try {
        const params = {
          page: 1,
          row: 20,
          communityId: this.communityId
        }

        const response = await listParkingAreas(params)
        this.parkingSpaceCreateFeeAddInfo.parkingAreas = response.parkingAreas || []
      } catch (error) {
        console.error('获取停车场数据失败:', error)
      }
    },

    open(createData) {
      this.clearAddFeeConfigInfo()
      this.parkingSpaceCreateFeeAddInfo.locationTypeCd = '2000'
      const carData = createData.car || {}
      const psId = createData.psId || createData.carId || carData.carId || ''
      const psName = createData.psName || createData.carNum || carData.carNum || ''
      this.parkingSpaceCreateFeeAddInfo.locationObjId = psId
      this.parkingSpaceCreateFeeAddInfo.psId = psId
      this.parkingSpaceCreateFeeAddInfo.carId = createData.carId || carData.carId || ''
      this.parkingSpaceCreateFeeAddInfo.locationTypeCdName = psName
      this.parkingSpaceCreateFeeAddInfo.isMore = createData.isMore || false

      this.dialogVisible = true

      this.$nextTick(() => {
        if (this.$refs.createForm) {
          this.$refs.createForm.clearValidate()
        }
      })
    },

    async _changeParkingSpaceFeeTypeCd(feeTypeCd) {
      if (!feeTypeCd) return

      try {
        const params = {
          page: 1,
          row: 20,
          communityId: this.communityId,
          feeTypeCd: feeTypeCd,
          isDefault: 'F',
          valid: '1'
        }

        const response = await listFeeConfigs(params)
        this.parkingSpaceCreateFeeAddInfo.feeConfigs = response.feeConfigs || []
      } catch (error) {
        console.error('获取费用配置失败:', error)
      }
    },

    _parkingSpaceCreateFeeAddIfOnceFee(configId) {
      this.parkingSpaceCreateFeeAddInfo.endTime = ''
      const config = this.parkingSpaceCreateFeeAddInfo.feeConfigs.find(item => item.configId === configId)
      if (config) {
        this.parkingSpaceCreateFeeAddInfo.feeFlag = config.feeFlag
        this.parkingSpaceCreateFeeAddInfo.computingFormula = config.computingFormula
      }
    },

    validateStartTime() {
      if (this.parkingSpaceCreateFeeAddInfo.startTime && this.parkingSpaceCreateFeeAddInfo.endTime) {
        const start = new Date(this.parkingSpaceCreateFeeAddInfo.startTime)
        const end = new Date(this.parkingSpaceCreateFeeAddInfo.endTime)
        if (start >= end) {
          this.$message.warning(this.$t('parkingSpaceCreateFeeAdd.endTimeAfterStart'))
          this.parkingSpaceCreateFeeAddInfo.endTime = ''
        }
      }
    },

    validateEndTime() {
      if (this.parkingSpaceCreateFeeAddInfo.startTime && this.parkingSpaceCreateFeeAddInfo.endTime) {
        const start = new Date(this.parkingSpaceCreateFeeAddInfo.startTime)
        const end = new Date(this.parkingSpaceCreateFeeAddInfo.endTime)
        if (start >= end) {
          this.$message.warning(this.$t('parkingSpaceCreateFeeAdd.endTimeAfterStart'))
          this.parkingSpaceCreateFeeAddInfo.endTime = ''
        }
      }
    },

    validateDate(rule, value, callback) {
      if (!value) {
        callback(new Error(rule.message))
        return
      }

      const date = new Date(value)
      if (isNaN(date.getTime())) {
        callback(new Error(this.$t('parkingSpaceCreateFeeAdd.dateFormatError')))
        return
      }

      callback()
    },

    submitForm() {
      this.$refs.createForm.validate(async (valid) => {
        if (!valid) {
          return
        }

        try {
          this.loading = true
          const data = {
            ...this.parkingSpaceCreateFeeAddInfo,
            communityId: this.communityId
          }

          await saveParkingSpaceCreateFee(data)

          this.$message.success(this.$t('common.operationSuccess'))
          this.$emit('success')
          this.handleClose()

        } catch (error) {
          console.error('创建车辆费用失败:', error)
          this.$message.error(this.$t('parkingSpaceCreateFeeAdd.createFailed'))
        } finally {
          this.loading = false
        }
      })
    },

    formatDateTime(date) {
      if (!date) return ''
      const year = date.getFullYear()
      const month = String(date.getMonth() + 1).padStart(2, '0')
      const day = String(date.getDate()).padStart(2, '0')
      const hours = String(date.getHours()).padStart(2, '0')
      const minutes = String(date.getMinutes()).padStart(2, '0')
      const seconds = String(date.getSeconds()).padStart(2, '0')
      return `${year}-${month}-${day} ${hours}:${minutes}:${seconds}`
    },

    clearAddFeeConfigInfo() {
      const feeTypeCds = this.parkingSpaceCreateFeeAddInfo.feeTypeCds
      const parkingAreas = this.parkingSpaceCreateFeeAddInfo.parkingAreas
      this.parkingSpaceCreateFeeAddInfo = {
        feeTypeCds: feeTypeCds,
        feeConfigs: [],
        parkingAreas: parkingAreas,
        locationTypeCd: '2000',
        locationObjId: '',
        psId: '',
        carId: '',
        feeTypeCd: '',
        configId: '',
        carState: ['H', 'S'],
        isMore: false,
        locationTypeCdName: '',
        startTime: this.formatDateTime(new Date()),
        paId: '',
        feeFlag: '',
        computingFormula: '',
        amount: '',
        endTime: ''
      }
    },

    handleClose() {
      this.dialogVisible = false
      this.clearAddFeeConfigInfo()
      this.loading = false

      if (this.$refs.createForm) {
        this.$refs.createForm.clearValidate()
      }
    }
  }
}
</script>

<style lang="scss" scoped>
.parking-space-create-fee-add {
  .dialog-footer {
    text-align: right;
  }
}

::v-deep .el-dialog__body {
  padding: 20px;
}
</style>