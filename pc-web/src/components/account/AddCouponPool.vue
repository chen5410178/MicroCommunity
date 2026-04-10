<template>
  <el-dialog
    :title="$t('couponPoolManage.add.title')"
    :visible.sync="visible"
    width="50%"
    @close="handleClose"
  >
    <el-form
      ref="form"
      :model="form"
      :rules="rules"
      label-width="150px"
      label-position="right"
    >
      <el-form-item
        :label="$t('couponPoolManage.add.couponType')"
        prop="couponType"
      >
        <el-select
          v-model="form.couponType"
          :placeholder="$t('couponPoolManage.add.selectCouponType')"
          style="width: 100%"
        >
          <el-option
            label="普通劵"
            value="1001"
          />
        </el-select>
      </el-form-item>
      <el-form-item
        :label="$t('couponPoolManage.add.couponName')"
        prop="couponName"
      >
        <el-input
          v-model="form.couponName"
          :placeholder="$t('couponPoolManage.add.inputCouponName')"
        />
      </el-form-item>
      <el-form-item
        :label="$t('couponPoolManage.add.actualPrice')"
        prop="actualPrice"
      >
        <el-input
          v-model="form.actualPrice"
          type="number"
          :placeholder="$t('couponPoolManage.add.inputActualPrice')"
        />
      </el-form-item>
      <el-form-item
        :label="$t('couponPoolManage.add.buyPrice')"
        prop="buyPrice"
      >
        <el-input
          v-model="form.buyPrice"
          type="number"
          :placeholder="$t('couponPoolManage.add.inputBuyPrice')"
        />
      </el-form-item>
      <el-form-item
        :label="$t('couponPoolManage.add.couponStock')"
        prop="couponStock"
      >
        <el-input
          v-model="form.couponStock"
          type="number"
          :placeholder="$t('couponPoolManage.add.inputCouponStock')"
        />
      </el-form-item>
      <el-form-item
        :label="$t('couponPoolManage.add.validityDay')"
        prop="validityDay"
      >
        <el-input
          v-model="form.validityDay"
          type="number"
          :placeholder="$t('couponPoolManage.add.inputValidityDay')"
        />
      </el-form-item>
      <el-form-item
        :label="$t('couponPoolManage.add.seq')"
        prop="seq"
      >
        <el-input
          v-model="form.seq"
          type="number"
          :placeholder="$t('couponPoolManage.add.inputSeq')"
        />
      </el-form-item>
    </el-form>
    <span slot="footer" class="dialog-footer">
      <el-button @click="visible = false">
        {{ $t('couponPoolManage.add.cancel') }}
      </el-button>
      <el-button type="primary" @click="handleSubmit">
        {{ $t('couponPoolManage.add.save') }}
      </el-button>
    </span>
  </el-dialog>
</template>

<script>
import { saveCouponPool } from '@/api/account/couponPoolManageApi'

export default {
  name: 'AddCouponPool',
  data() {
    return {
      visible: false,
      form: {
        couponType: '',
        couponName: '',
        actualPrice: '',
        buyPrice: '',
        couponStock: '',
        validityDay: '',
        seq: ''
      },
      rules: {
        couponType: [
          { required: true, message: this.$t('couponPoolManage.add.couponTypeRequired'), trigger: 'blur' }
        ],
        couponName: [
          { required: true, message: this.$t('couponPoolManage.add.couponNameRequired'), trigger: 'blur' },
          { max: 64, message: this.$t('couponPoolManage.add.couponNameMaxLength'), trigger: 'blur' }
        ],
        actualPrice: [
          { required: true, message: this.$t('couponPoolManage.add.actualPriceRequired'), trigger: 'blur' },
          { max: 10, message: this.$t('couponPoolManage.add.actualPriceMaxLength'), trigger: 'blur' }
        ],
        buyPrice: [
          { required: true, message: this.$t('couponPoolManage.add.buyPriceRequired'), trigger: 'blur' },
          { max: 10, message: this.$t('couponPoolManage.add.buyPriceMaxLength'), trigger: 'blur' }
        ],
        couponStock: [
          { required: true, message: this.$t('couponPoolManage.add.couponStockRequired'), trigger: 'blur' },
          { max: 20, message: this.$t('couponPoolManage.add.couponStockMaxLength'), trigger: 'blur' }
        ],
        validityDay: [
          { required: true, message: this.$t('couponPoolManage.add.validityDayRequired'), trigger: 'blur' },
          { max: 20, message: this.$t('couponPoolManage.add.validityDayMaxLength'), trigger: 'blur' }
        ],
        seq: [
          { required: true, message: this.$t('couponPoolManage.add.seqRequired'), trigger: 'blur' },
          { max: 11, message: this.$t('couponPoolManage.add.seqMaxLength'), trigger: 'blur' }
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
            await saveCouponPool(this.form)
            this.$message.success(this.$t('common.operationSuccess'))
            this.visible = false
            this.$emit('success')
          } catch (error) {
            this.$message.error(error.message || this.$t('couponPoolManage.add.error'))
          }
        }
      })
    }
  }
}
</script>