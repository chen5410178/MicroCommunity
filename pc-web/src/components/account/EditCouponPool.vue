<template>
  <el-dialog
    :title="$t('couponPoolManage.edit.title')"
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
        :label="$t('couponPoolManage.edit.couponType')"
      >
        <el-select
          v-model="form.couponType"
          disabled
          style="width: 100%"
        >
          <el-option
            label="普通劵"
            value="1001"
          />
        </el-select>
      </el-form-item>
      <el-form-item
        :label="$t('couponPoolManage.edit.couponName')"
      >
        <el-input
          v-model="form.couponName"
          disabled
        />
      </el-form-item>
      <el-form-item
        :label="$t('couponPoolManage.edit.state')"
        prop="state"
      >
        <el-select
          v-model="form.state"
          :placeholder="$t('couponPoolManage.edit.selectState')"
          style="width: 100%"
        >
          <el-option
            :label="$t('couponPoolManage.edit.stateOff')"
            value="1001"
          />
          <el-option
            :label="$t('couponPoolManage.edit.stateOn')"
            value="2002"
          />
        </el-select>
      </el-form-item>
      <el-form-item
        :label="$t('couponPoolManage.edit.actualPrice')"
      >
        <el-input
          v-model="form.actualPrice"
          disabled
        />
      </el-form-item>
      <el-form-item
        :label="$t('couponPoolManage.edit.buyPrice')"
        prop="buyPrice"
      >
        <el-input
          v-model="form.buyPrice"
          type="number"
        />
      </el-form-item>
      <el-form-item
        :label="$t('couponPoolManage.edit.couponStock')"
        prop="couponStock"
      >
        <el-input
          v-model="form.couponStock"
          type="number"
        />
      </el-form-item>
      <el-form-item
        :label="$t('couponPoolManage.edit.validityDay')"
      >
        <el-input
          v-model="form.validityDay"
          disabled
        />
      </el-form-item>
      <el-form-item
        :label="$t('couponPoolManage.edit.seq')"
      >
        <el-input
          v-model="form.seq"
          disabled
        />
      </el-form-item>
    </el-form>
    <span slot="footer" class="dialog-footer">
      <el-button @click="visible = false">
        {{ $t('couponPoolManage.edit.cancel') }}
      </el-button>
      <el-button type="primary" @click="handleSubmit">
        {{ $t('couponPoolManage.edit.save') }}
      </el-button>
    </span>
  </el-dialog>
</template>

<script>
import { updateCouponPool } from '@/api/account/couponPoolManageApi'

export default {
  name: 'EditCouponPool',
  data() {
    return {
      visible: false,
      form: {
        poolId: '',
        couponType: '',
        couponName: '',
        state: '',
        actualPrice: '',
        buyPrice: '',
        couponStock: '',
        validityDay: '',
        seq: ''
      },
      rules: {
        buyPrice: [
          { required: true, message: this.$t('couponPoolManage.edit.buyPriceRequired'), trigger: 'blur' },
          { max: 10, message: this.$t('couponPoolManage.edit.buyPriceMaxLength'), trigger: 'blur' }
        ],
        couponStock: [
          { required: true, message: this.$t('couponPoolManage.edit.couponStockRequired'), trigger: 'blur' },
          { max: 20, message: this.$t('couponPoolManage.edit.couponStockMaxLength'), trigger: 'blur' }
        ],
        state: [
          { required: true, message: this.$t('couponPoolManage.edit.stateRequired'), trigger: 'blur' }
        ]
      }
    }
  },
  methods: {
    open(data) {
      this.form = { ...data }
      this.visible = true
    },
    handleClose() {
      this.$refs.form.resetFields()
    },
    handleSubmit() {
      this.$refs.form.validate(async valid => {
        if (valid) {
          try {
            await updateCouponPool(this.form)
            this.$message.success(this.$t('common.operationSuccess'))
            this.visible = false
            this.$emit('success')
          } catch (error) {
            this.$message.error(error.message || this.$t('couponPoolManage.edit.error'))
          }
        }
      })
    }
  }
}
</script>