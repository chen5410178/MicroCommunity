<template>
  <div class="shop-create-fee-add">
    <el-dialog :title="$t('shopCreateFeeAdd.createFee')" :visible.sync="dialogVisible" width="800px"
      :before-close="handleClose">
      <el-form :model="shopCreateFeeAddInfo" :rules="rules" ref="createForm" label-width="120px">
        <el-form-item :label="$t('shopCreateFeeAdd.shop')">
          <el-input v-model="shopCreateFeeAddInfo.shopName" disabled></el-input>
        </el-form-item>

        <el-form-item :label="$t('shopCreateFeeAdd.feeType')" prop="feeTypeCd">
          <el-select v-model="shopCreateFeeAddInfo.feeTypeCd" @change="_changeShopFeeTypeCd" style="width:100%">
            <el-option :label="$t('shopCreateFeeAdd.selectFeeType')" value="" disabled></el-option>
            <el-option v-for="(item, index) in filteredFeeTypeCds" :key="index" :label="item.name"
              :value="item.statusCd"></el-option>
          </el-select>
        </el-form-item>

        <el-form-item :label="$t('shopCreateFeeAdd.feeItem')" prop="configId">
          <el-select v-model="shopCreateFeeAddInfo.configId" @change="_shopCreateFeeAddIfOnceFee" style="width:100%">
            <el-option :label="$t('shopCreateFeeAdd.selectFeeItem')" value="" disabled></el-option>
            <el-option v-for="(item, index) in shopCreateFeeAddInfo.feeConfigs" :key="index" :label="item.feeName"
              :value="item.configId"></el-option>
          </el-select>
        </el-form-item>

        <el-form-item v-if="shopCreateFeeAddInfo.computingFormula === '4004'" :label="$t('shopCreateFeeAdd.feeAmount')"
          prop="amount">
          <el-input v-model="shopCreateFeeAddInfo.amount" :placeholder="$t('shopCreateFeeAdd.enterFeeAmount')"></el-input>
        </el-form-item>

        <el-form-item :label="$t('shopCreateFeeAdd.billingStartTime')" prop="startTime">
          <el-date-picker v-model="shopCreateFeeAddInfo.startTime" type="datetime"
            :placeholder="$t('shopCreateFeeAdd.enterBillingStartTime')" value-format="yyyy-MM-dd HH:mm:ss"
            style="width:100%" @change="validateStartTime"></el-date-picker>
        </el-form-item>

        <el-form-item :label="$t('shopCreateFeeAdd.billingEndTime')" prop="endTime">
          <el-date-picker v-model="shopCreateFeeAddInfo.endTime" type="datetime"
            :placeholder="$t('shopCreateFeeAdd.enterBillingEndTime')" value-format="yyyy-MM-dd HH:mm:ss"
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
import { getCommunityId, getDict } from '@/api/community/communityApi'
import { listFeeConfigs, saveShopCreateFee } from '@/api/shop/shopCreateFeeApi'

export default {
  name: 'ShopCreateFeeAdd',
  data() {
    return {
      communityId: '',
      dialogVisible: false,
      loading: false,
      shopCreateFeeAddInfo: {
        feeTypeCds: [],
        feeConfigs: [],
        shopId: '',
        shopName: '',
        feeTypeCd: '',
        configId: '',
        startTime: '',
        feeFlag: '',
        computingFormula: '',
        amount: '',
        endTime: ''
      },
      rules: {
        feeTypeCd: [
          { required: true, message: this.$t('shopCreateFeeAdd.feeTypeRequired'), trigger: 'change' }
        ],
        configId: [
          { required: true, message: this.$t('shopCreateFeeAdd.feeItemRequired'), trigger: 'change' }
        ],
        startTime: [
          { required: true, message: this.$t('shopCreateFeeAdd.billingStartTimeRequired'), trigger: 'change' }
        ],
        amount: [
          { required: true, message: this.$t('shopCreateFeeAdd.amountRequired'), trigger: 'blur' },
          { pattern: /^\d+(\.\d{1,2})?$/, message: this.$t('shopCreateFeeAdd.amountFormatError'), trigger: 'blur' }
        ]
      }
    }
  },
  computed: {
    filteredFeeTypeCds() {
      return (this.shopCreateFeeAddInfo.feeTypeCds || []).filter(item =>
        item.statusCd !== '888800010009' && item.statusCd !== '888800010011'
      )
    }
  },
  async created() {
    this.communityId = getCommunityId()
    await this.loadDictData()
    this.shopCreateFeeAddInfo.startTime = this.formatDateTime(new Date())
  },
  methods: {
    async loadDictData() {
      try {
        const data = await getDict('pay_fee_config', 'fee_type_cd')
        this.shopCreateFeeAddInfo.feeTypeCds = data
      } catch (error) {
        console.error('Failed to load dict data:', error)
      }
    },

    open(createData) {
      this.clearAddFeeConfigInfo()
      this.shopCreateFeeAddInfo.shopId = createData.shopId
      this.shopCreateFeeAddInfo.shopName = createData.shopName || createData.shopNum

      this.dialogVisible = true

      this.$nextTick(() => {
        if (this.$refs.createForm) {
          this.$refs.createForm.clearValidate()
        }
      })
    },

    async _changeShopFeeTypeCd(feeTypeCd) {
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
        this.shopCreateFeeAddInfo.feeConfigs = response.feeConfigs || []
      } catch (error) {
        console.error('Failed to load fee configs:', error)
      }
    },

    _shopCreateFeeAddIfOnceFee(configId) {
      this.shopCreateFeeAddInfo.endTime = ''
      const config = this.shopCreateFeeAddInfo.feeConfigs.find(item => item.configId === configId)
      if (config) {
        this.shopCreateFeeAddInfo.feeFlag = config.feeFlag
        this.shopCreateFeeAddInfo.computingFormula = config.computingFormula
      }
    },

    validateStartTime() {
      if (this.shopCreateFeeAddInfo.startTime && this.shopCreateFeeAddInfo.endTime) {
        const start = new Date(this.shopCreateFeeAddInfo.startTime)
        const end = new Date(this.shopCreateFeeAddInfo.endTime)
        if (start >= end) {
          this.$message.warning(this.$t('shopCreateFeeAdd.endTimeAfterStart'))
          this.shopCreateFeeAddInfo.endTime = ''
        }
      }
    },

    validateEndTime() {
      if (this.shopCreateFeeAddInfo.startTime && this.shopCreateFeeAddInfo.endTime) {
        const start = new Date(this.shopCreateFeeAddInfo.startTime)
        const end = new Date(this.shopCreateFeeAddInfo.endTime)
        if (start >= end) {
          this.$message.warning(this.$t('shopCreateFeeAdd.endTimeAfterStart'))
          this.shopCreateFeeAddInfo.endTime = ''
        }
      }
    },

    submitForm() {
      this.$refs.createForm.validate(async (valid) => {
        if (!valid) {
          return
        }

        try {
          this.loading = true
          const data = {
            payerObjId: this.shopCreateFeeAddInfo.shopId,
            payerObjName: this.shopCreateFeeAddInfo.shopName,
            configId: this.shopCreateFeeAddInfo.configId,
            feeTypeCd: this.shopCreateFeeAddInfo.feeTypeCd,
            startTime: this.shopCreateFeeAddInfo.startTime,
            endTime: this.shopCreateFeeAddInfo.endTime,
            computingFormula: this.shopCreateFeeAddInfo.computingFormula,
            amount: this.shopCreateFeeAddInfo.amount,
            feeFlag: this.shopCreateFeeAddInfo.feeFlag
          }

          await saveShopCreateFee(data)

          this.$message.success(this.$t('common.operationSuccess'))
          this.$emit('success')
          this.handleClose()

        } catch (error) {
          console.error('Failed to create shop fee:', error)
          this.$message.error(this.$t('shopCreateFeeAdd.createFailed'))
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
      const feeTypeCds = this.shopCreateFeeAddInfo.feeTypeCds
      this.shopCreateFeeAddInfo = {
        feeTypeCds: feeTypeCds,
        feeConfigs: [],
        shopId: '',
        shopName: '',
        feeTypeCd: '',
        configId: '',
        startTime: this.formatDateTime(new Date()),
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
.shop-create-fee-add {
  .dialog-footer {
    text-align: right;
  }
}

::v-deep .el-dialog__body {
  padding: 20px;
}
</style>
