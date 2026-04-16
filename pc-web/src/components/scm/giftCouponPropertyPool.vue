<template>
  <el-dialog :title="$t('couponPropertyPoolManage.gift.title')" :visible.sync="visible" width="40%"
    @close="handleClose">
    <el-form ref="form" :model="formData" :rules="rules" label-width="120px">
      <el-form-item :label="$t('couponPropertyPoolManage.gift.couponName')">
        <el-input v-model="formData.couponName" disabled />
      </el-form-item>

      <el-form-item :label="$t('couponPropertyPoolManage.gift.giftCount')" prop="giftCount">
        <el-input v-model="formData.giftCount" type="number"
          :placeholder="$t('couponPropertyPoolManage.gift.giftCountPlaceholder')" />
      </el-form-item>

      <el-form-item :label="$t('couponPropertyPoolManage.gift.tel')" prop="tel">
        <el-input v-model="formData.tel" :placeholder="$t('couponPropertyPoolManage.gift.telPlaceholder')" />
      </el-form-item>
    </el-form>

    <span slot="footer" class="dialog-footer">
      <el-button @click="visible = false">{{ $t('common.cancel') }}</el-button>
      <el-button type="primary" @click="handleSubmit">{{ $t('couponPropertyPoolManage.gift.submit') }}</el-button>
    </span>
  </el-dialog>
</template>

<script>
import { giftCouponProperty } from '@/api/scm/couponPropertyPoolManageApi'
import { getCommunityId } from '@/api/community/communityApi'

export default {
  name: 'GiftCouponPropertyPool',
  data() {
    return {
      visible: false,
      formData: {
        cppId: '',
        couponName: '',
        giftCount: '',
        tel: '',
        communityId: getCommunityId()
      },
      rules: {
        cppId: [
          { required: true, message: this.$t('couponPropertyPoolManage.validate.cppIdRequired'), trigger: 'blur' }
        ],
        giftCount: [
          { required: true, message: this.$t('couponPropertyPoolManage.validate.giftCountRequired'), trigger: 'blur' }
        ],
        tel: [
          { required: true, message: this.$t('couponPropertyPoolManage.validate.telRequired'), trigger: 'blur' }
        ]
      }
    }
  },
  methods: {
    open(row) {
      this.formData = {
        ...this.formData,
        cppId: row.cppId,
        couponName: row.couponName
      }
      this.visible = true
    },
    handleClose() {
      this.$refs.form.resetFields()
    },
    handleSubmit() {
      this.$refs.form.validate(async valid => {
        if (valid) {
          try {
           const {code,msg} = await giftCouponProperty(this.formData)
           if(code != 0){
            this.$message.error(msg)
            return
           }
            this.$message.success(this.$t('common.operationSuccess'))
            this.visible = false
            this.$emit('success')
          } catch (error) {
            this.$message.error(error.message || this.$t('couponPropertyPoolManage.gift.error'))
          }
        }
      })
    }
  }
}
</script>