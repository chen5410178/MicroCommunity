<template>
  <el-dialog :title="$t('couponRuleCpps.addCoupon')" :visible.sync="visible" width="50%" @close="handleClose">
    <el-form ref="form" :model="form" :rules="rules" label-width="120px">
      <el-form-item :label="$t('couponRuleCpps.coupon')" prop="cppId">
        <el-select v-model="form.cppId" :placeholder="$t('couponRuleCpps.couponPlaceholder')" style="width: 100%">
          <el-option v-for="item in couponOptions" :key="item.cppId" :label="item.couponName" :value="item.cppId">
          </el-option>
        </el-select>
      </el-form-item>

      <el-form-item :label="$t('couponRuleCpps.quantity')" prop="quantity">
        <el-input v-model="form.quantity" :placeholder="$t('couponRuleCpps.quantityPlaceholder')" />
      </el-form-item>

      <el-form-item :label="$t('couponRuleCpps.frequency')" prop="giftFrequency">
        <el-select v-model="form.giftFrequency" :placeholder="$t('couponRuleCpps.frequencyPlaceholder')"
          style="width: 100%">
          <el-option v-for="item in frequencyOptions" :key="item.value" :label="item.label" :value="item.value">
          </el-option>
        </el-select>
      </el-form-item>
    </el-form>

    <span slot="footer" class="dialog-footer">
      <el-button @click="visible = false">{{ $t('common.cancel') }}</el-button>
      <el-button type="primary" @click="handleSubmit">{{ $t('common.confirm') }}</el-button>
    </span>
  </el-dialog>
</template>

<script>
import { saveCouponRuleCpps, listCouponPropertyPool } from '@/api/scm/couponRuleApi'
import { getCommunityId } from '@/api/community/communityApi'

export default {
  name: 'AddCouponRuleCpps',
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
        cppId: '',
        quantity: '',
        giftFrequency: '100301',
        ruleId: '',
        communityId: ''
      },
      rules: {
        cppId: [
          { required: true, message: this.$t('couponRuleCpps.couponRequired'), trigger: 'change' }
        ],
        quantity: [
          { required: true, message: this.$t('couponRuleCpps.quantityRequired'), trigger: 'blur' },
          { pattern: /^[1-9]\d*$/, message: this.$t('couponRuleCpps.quantityPattern'), trigger: 'blur' }
        ],
        giftFrequency: [
          { required: true, message: this.$t('couponRuleCpps.frequencyRequired'), trigger: 'change' }
        ]
      },
      couponOptions: [],
      frequencyOptions: [
        { value: '100301', label: this.$t('couponRuleCpps.once') },
        { value: '100302', label: this.$t('couponRuleCpps.monthly') }
      ]
    }
  },
  methods: {
    open() {
      this.form.ruleId = this.ruleId
      this.form.communityId = getCommunityId()
      this.getCouponOptions()
      this.visible = true
      this.$nextTick(() => {
        this.$refs.form.resetFields()
      })
    },
    async getCouponOptions() {
      try {
        const params = {
          page: 1,
          row: 100,
          communityId: getCommunityId()
        }
        const { data } = await listCouponPropertyPool(params)
        this.couponOptions = data
      } catch (error) {
        console.error('Failed to get coupon options:', error)
      }
    },
    handleClose() {
      this.$refs.form.resetFields()
    },
    handleSubmit() {
      this.$refs.form.validate(async valid => {
        if (valid) {
          try {
           const {code,msg} = await saveCouponRuleCpps(this.form)
           if(code != 0){
            this.$message.error(msg)
            return
           }
            this.$message.success(this.$t('common.operationSuccess'))
            this.visible = false
            this.$emit('success')
          } catch (error) {
            console.error('Failed to add coupon rule cpps:', error)
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

    .el-input,
    .el-select {
      width: 100%;
    }
  }
}
</style>