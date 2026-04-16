<template>
  <el-dialog
    :title="$t('couponDetailManage.add.title')"
    :visible.sync="visible"
    width="50%"
    @close="handleClose"
  >
    <el-form
      ref="form"
      :model="formData"
      :rules="rules"
      label-width="120px"
      label-position="right"
    >
      <el-form-item
        :label="$t('couponDetailManage.add.poolId')"
        prop="poolId"
      >
        <el-input
          v-model="formData.poolId"
          :placeholder="$t('couponDetailManage.add.poolIdPlaceholder')"
        />
      </el-form-item>
      <el-form-item
        :label="$t('couponDetailManage.add.shopId')"
        prop="shopId"
      >
        <el-input
          v-model="formData.shopId"
          :placeholder="$t('couponDetailManage.add.shopIdPlaceholder')"
        />
      </el-form-item>
      <el-form-item
        :label="$t('couponDetailManage.add.couponName')"
        prop="couponName"
      >
        <el-input
          v-model="formData.couponName"
          :placeholder="$t('couponDetailManage.add.couponNamePlaceholder')"
        />
      </el-form-item>
      <el-form-item
        :label="$t('couponDetailManage.add.actualPrice')"
        prop="actualPrice"
      >
        <el-input
          v-model.number="formData.actualPrice"
          type="number"
          :placeholder="$t('couponDetailManage.add.actualPricePlaceholder')"
        />
      </el-form-item>
      <el-form-item
        :label="$t('couponDetailManage.add.buyPrice')"
        prop="buyPrice"
      >
        <el-input
          v-model.number="formData.buyPrice"
          type="number"
          :placeholder="$t('couponDetailManage.add.buyPricePlaceholder')"
        />
      </el-form-item>
      <el-form-item
        :label="$t('couponDetailManage.add.amount')"
        prop="amount"
      >
        <el-input
          v-model.number="formData.amount"
          type="number"
          :placeholder="$t('couponDetailManage.add.amountPlaceholder')"
        />
      </el-form-item>
      <el-form-item
        :label="$t('couponDetailManage.add.buyCount')"
        prop="buyCount"
      >
        <el-input
          v-model.number="formData.buyCount"
          type="number"
          :placeholder="$t('couponDetailManage.add.buyCountPlaceholder')"
        />
      </el-form-item>
      <el-form-item
        :label="$t('couponDetailManage.add.validityDay')"
        prop="validityDay"
      >
        <el-input
          v-model.number="formData.validityDay"
          type="number"
          :placeholder="$t('couponDetailManage.add.validityDayPlaceholder')"
        />
      </el-form-item>
    </el-form>
    <span slot="footer" class="dialog-footer">
      <el-button @click="visible = false">{{ $t('common.cancel') }}</el-button>
      <el-button type="primary" @click="handleSubmit">{{ $t('common.save') }}</el-button>
    </span>
  </el-dialog>
</template>

<script>
import { saveCouponDetail } from '@/api/account/couponDetailManageApi'

export default {
  name: 'AddCouponDetail',
  data() {
    return {
      visible: false,
      formData: {
        poolId: '',
        shopId: '',
        couponName: '',
        actualPrice: '',
        buyPrice: '',
        amount: '',
        buyCount: '',
        validityDay: ''
      },
      rules: {
        poolId: [
          { required: true, message: this.$t('couponDetailManage.validate.poolIdRequired'), trigger: 'blur' },
          { max: 30, message: this.$t('couponDetailManage.validate.poolIdMaxLength'), trigger: 'blur' }
        ],
        shopId: [
          { required: true, message: this.$t('couponDetailManage.validate.shopIdRequired'), trigger: 'blur' },
          { max: 30, message: this.$t('couponDetailManage.validate.shopIdMaxLength'), trigger: 'blur' }
        ],
        couponName: [
          { required: true, message: this.$t('couponDetailManage.validate.couponNameRequired'), trigger: 'blur' },
          { max: 64, message: this.$t('couponDetailManage.validate.couponNameMaxLength'), trigger: 'blur' }
        ],
        actualPrice: [
          { required: true, message: this.$t('couponDetailManage.validate.actualPriceRequired'), trigger: 'blur' },
          { type: 'number', message: this.$t('couponDetailManage.validate.actualPriceNumber'), trigger: 'blur' }
        ],
        buyPrice: [
          { required: true, message: this.$t('couponDetailManage.validate.buyPriceRequired'), trigger: 'blur' },
          { type: 'number', message: this.$t('couponDetailManage.validate.buyPriceNumber'), trigger: 'blur' }
        ],
        amount: [
          { required: true, message: this.$t('couponDetailManage.validate.amountRequired'), trigger: 'blur' },
          { type: 'number', message: this.$t('couponDetailManage.validate.amountNumber'), trigger: 'blur' }
        ],
        buyCount: [
          { required: true, message: this.$t('couponDetailManage.validate.buyCountRequired'), trigger: 'blur' },
          { type: 'number', message: this.$t('couponDetailManage.validate.buyCountNumber'), trigger: 'blur' }
        ],
        validityDay: [
          { required: true, message: this.$t('couponDetailManage.validate.validityDayRequired'), trigger: 'blur' },
          { type: 'number', message: this.$t('couponDetailManage.validate.validityDayNumber'), trigger: 'blur' }
        ]
      }
    }
  },
  methods: {
    open() {
      this.visible = true
    },
    handleClose() {
      this.$refs.form.resetFields()
    },
    handleSubmit() {
      this.$refs.form.validate(async valid => {
        if (valid) {
          try {
            await saveCouponDetail(this.formData)
            this.$message.success(this.$t('common.operationSuccess'))
            this.visible = false
            this.$emit('success')
          } catch (error) {
            this.$message.error(error.message)
          }
        }
      })
    }
  }
}
</script>