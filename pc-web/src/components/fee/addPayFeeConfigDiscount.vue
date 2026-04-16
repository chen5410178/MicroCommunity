<template>
  <el-dialog :title="$t('addPayFeeConfigDiscount.addTitle')" :visible.sync="visible" width="800px"
    :close-on-click-modal="false" @closed="resetForm">
    <el-form ref="form" :model="form" :rules="rules" label-width="160px">
      <el-form-item :label="$t('addPayFeeConfigDiscount.discountType')" prop="discountType">
        <el-select v-model="form.discountType" :placeholder="$t('addPayFeeConfigDiscount.requiredPlaceholder')"
          @change="changeDiscountType" style="width: 100%">
          <el-option disabled value="">{{ $t('addPayFeeConfigDiscount.selectDiscountType') }}</el-option>
          <el-option value="1001" :label="$t('addPayFeeConfigDiscount.discount')">{{
            $t('addPayFeeConfigDiscount.discount') }}</el-option>
          <el-option value="2002" :label="$t('addPayFeeConfigDiscount.penalty')">{{
            $t('addPayFeeConfigDiscount.penalty') }}</el-option>
        </el-select>
      </el-form-item>

      <el-form-item :label="$t('addPayFeeConfigDiscount.discountName')" prop="discountId">
        <el-select v-model="form.discountId" :placeholder="$t('addPayFeeConfigDiscount.requiredPlaceholder')"
          style="width: 100%">
          <el-option disabled value="">{{ $t('addPayFeeConfigDiscount.selectDiscountName') }}</el-option>
          <el-option v-for="item in discounts" :key="item.discountId" :label="item.discountName"
            :value="item.discountId" />
        </el-select>
      </el-form-item>

      <el-form-item :label="$t('addPayFeeConfigDiscount.paymentPeriod')" required>
        <el-col :span="11">
          <el-form-item prop="startTime">
            <el-date-picker v-model="form.startTime" type="datetime"
              :placeholder="$t('addPayFeeConfigDiscount.startTimePlaceholder')" style="width: 100%"
              value-format="yyyy-MM-dd HH:mm:ss" />
          </el-form-item>
          <div class="el-form-item__tip">{{ $t('addPayFeeConfigDiscount.paymentPeriodTip') }}</div>
        </el-col>
        <el-col :span="2" class="text-center">-</el-col>
        <el-col :span="11">
          <el-form-item prop="endTime">
            <el-date-picker v-model="form.endTime" type="datetime"
              :placeholder="$t('addPayFeeConfigDiscount.endTimePlaceholder')" style="width: 100%"
              value-format="yyyy-MM-dd HH:mm:ss" />
          </el-form-item>
        </el-col>
      </el-form-item>

      <el-form-item v-if="form.discountType !== '2002'" :label="$t('addPayFeeConfigDiscount.discountEndTime')"
        prop="payMaxEndTime">
        <el-date-picker v-model="form.payMaxEndTime" type="datetime"
          :placeholder="$t('addPayFeeConfigDiscount.discountEndTimePlaceholder')" style="width: 100%"
          value-format="yyyy-MM-dd HH:mm:ss" />
        <div class="el-form-item__tip">{{ $t('addPayFeeConfigDiscount.discountEndTimeTip') }}</div>
      </el-form-item>
    </el-form>

    <div slot="footer">
      <el-button @click="visible = false">{{ $t('common.cancel') }}</el-button>
      <el-button type="primary" @click="savePayFeeConfigDiscount" :loading="loading">
        {{ $t('common.save') }}
      </el-button>
    </div>
  </el-dialog>
</template>

<script>
import { savePayFeeConfigDiscount, getFeeDiscountList } from '@/api/fee/payFeeConfigDiscountManageApi'
import { getCommunityId } from '@/api/community/communityApi'

export default {
  name: 'AddPayFeeConfigDiscount',
  data() {
    return {
      visible: false,
      loading: false,
      form: {
        configDiscountId: '',
        discountId: '',
        configId: '',
        discountType: '',
        startTime: '',
        endTime: '',
        payMaxEndTime: '',
        communityId: ''
      },
      discounts: [],
      rules: {
        discountType: [
          { required: true, message: this.$t('addPayFeeConfigDiscount.discountTypeRequired'), trigger: 'change' }
        ],
        discountId: [
          { required: true, message: this.$t('addPayFeeConfigDiscount.discountNameRequired'), trigger: 'change' }
        ],
        startTime: [
          { required: true, message: this.$t('addPayFeeConfigDiscount.startTimeRequired'), trigger: 'change' }
        ],
        endTime: [
          { required: true, message: this.$t('addPayFeeConfigDiscount.endTimeRequired'), trigger: 'change' }
        ]
      }
    }
  },
  methods: {
    open(configId) {
      this.form.configId = configId
      this.form.communityId = getCommunityId()
      this.visible = true
    },
    resetForm() {
      this.$refs.form.resetFields()
      this.form = {
        configDiscountId: '',
        discountId: '',
        configId: this.form.configId,
        discountType: '',
        startTime: '',
        endTime: '',
        payMaxEndTime: '',
        communityId: this.form.communityId
      }
      this.discounts = []
    },
    async changeDiscountType() {
      if (!this.form.discountType) return

      try {
        const params = {
          page: 1,
          row: 100,
          communityId: this.form.communityId,
          discountType: this.form.discountType
        }
        const response = await getFeeDiscountList(params)
        this.discounts = response.data
      } catch (error) {
        this.$message.error(this.$t('addPayFeeConfigDiscount.fetchDiscountsError'))
      }
    },
    async savePayFeeConfigDiscount() {
      this.$refs.form.validate(async valid => {
        if (!valid) return

        try {
          this.loading = true
          await savePayFeeConfigDiscount(this.form)
          this.$message.success(this.$t('common.operationSuccess'))
          this.visible = false
          this.$emit('success')
        } catch (error) {
          this.$message.error(error.message || this.$t('addPayFeeConfigDiscount.saveError'))
        } finally {
          this.loading = false
        }
      })
    }
  }
}
</script>

<style lang="scss" scoped>
.text-center {
  text-align: center;
}

.el-form-item__tip {
  text-align: left;
  color: #999;
}
</style>