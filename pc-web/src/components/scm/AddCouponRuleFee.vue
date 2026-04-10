<template>
  <el-dialog :title="$t('couponRuleFee.addFee')" :visible.sync="visible" width="50%" @close="handleClose">
    <el-form ref="form" :model="form" :rules="rules" label-width="120px">
      <el-form-item :label="$t('couponRuleFee.fee')" prop="feeConfigId">
        <el-select v-model="form.feeConfigId" :placeholder="$t('couponRuleFee.feePlaceholder')" style="width: 100%">
          <el-option v-for="item in feeOptions" :key="item.configId" :label="item.feeName" :value="item.configId">
          </el-option>
        </el-select>
      </el-form-item>

      <el-form-item :label="$t('couponRuleFee.payStartTime')" prop="payStartTime">
        <el-date-picker v-model="form.payStartTime" type="datetime" value-format="yyyy-MM-dd"
          :placeholder="$t('couponRuleFee.payStartTimePlaceholder')" style="width: 100%">
        </el-date-picker>
      </el-form-item>

      <el-form-item :label="$t('couponRuleFee.payEndTime')" prop="payEndTime">
        <el-date-picker v-model="form.payEndTime" type="datetime" value-format="yyyy-MM-dd" :placeholder="$t('couponRuleFee.payEndTimePlaceholder')"
          style="width: 100%">
        </el-date-picker>
      </el-form-item>

      <el-form-item :label="$t('couponRuleFee.payMonth')" prop="payMonth">
        <el-input-number v-model="form.payMonth" :min="1" :max="12"
          :placeholder="$t('couponRuleFee.payMonthPlaceholder')">
        </el-input-number>
      </el-form-item>
    </el-form>

    <span slot="footer" class="dialog-footer">
      <el-button @click="visible = false">{{ $t('common.cancel') }}</el-button>
      <el-button type="primary" @click="handleSubmit">{{ $t('common.confirm') }}</el-button>
    </span>
  </el-dialog>
</template>

<script>
import { saveCouponRuleFee, listFeeConfigs } from '@/api/scm/couponRuleApi'
import { getCommunityId } from '@/api/community/communityApi'

export default {
  name: 'AddCouponRuleFee',
  props: {
    ruleId: {
      type: String,
      required: true
    }
  },
  data() {
    return {
      visible: false,
      form: {
        feeConfigId: '',
        payStartTime: '',
        payEndTime: '',
        payMonth: 1,
        ruleId: '',
        communityId: ''
      },
      rules: {
        feeConfigId: [
          { required: true, message: this.$t('couponRuleFee.feeRequired'), trigger: 'change' }
        ],
        payStartTime: [
          { required: true, message: this.$t('couponRuleFee.payStartTimeRequired'), trigger: 'change' }
        ],
        payEndTime: [
          { required: true, message: this.$t('couponRuleFee.payEndTimeRequired'), trigger: 'change' }
        ],
        payMonth: [
          { required: true, message: this.$t('couponRuleFee.payMonthRequired'), trigger: 'blur' }
        ]
      },
      feeOptions: []
    }
  },
  methods: {
    open() {
      this.form.ruleId = this.ruleId
      this.form.communityId = getCommunityId()
      this.getFeeOptions()
      this.visible = true
      this.$nextTick(() => {
        this.$refs.form.resetFields()
      })
    },
    async getFeeOptions() {
      try {
        const params = {
          page: 1,
          row: 100,
          communityId: getCommunityId()
        }
        const { feeConfigs } = await listFeeConfigs(params)
        this.feeOptions = feeConfigs
      } catch (error) {
        console.error('Failed to get fee options:', error)
      }
    },
    handleClose() {
      this.$refs.form.resetFields()
    },
    handleSubmit() {
      this.$refs.form.validate(async valid => {
        if (valid) {
          try {
            await saveCouponRuleFee(this.form)
            this.$message.success(this.$t('common.operationSuccess'))
            this.visible = false
            this.$emit('success')
          } catch (error) {
            console.error('Failed to add coupon rule fee:', error)
          }
        }
      })
    }
  }
}
</script>

<style lang="scss" scoped>
.el-dialog {
  .el-form {
    padding: 0 20px;

    .el-select,
    .el-date-picker {
      width: 100%;
    }
  }
}
</style>