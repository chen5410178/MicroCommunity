<template>
  <el-dialog
    :title="$t('couponPropertyPoolManage.edit.title')"
    :visible.sync="visible"
    width="50%"
    @close="handleClose"
  >
    <el-form
      ref="form"
      :model="formData"
      :rules="rules"
      label-width="120px"
    >
      <el-form-item
        :label="$t('couponPropertyPoolManage.edit.couponName')"
        prop="couponName"
      >
        <el-input
          v-model="formData.couponName"
          :placeholder="$t('couponPropertyPoolManage.edit.couponNamePlaceholder')"
        />
      </el-form-item>

      <el-form-item
        :label="$t('couponPropertyPoolManage.edit.toType')"
        prop="toType"
      >
        <el-select
          v-model="formData.toType"
          :placeholder="$t('couponPropertyPoolManage.edit.toTypePlaceholder')"
          style="width:100%"
          @change="handleToTypeChange"
        >
          <el-option
            v-for="item in toTypeOptions"
            :key="item.value"
            :label="item.label"
            :value="item.value"
          />
        </el-select>
      </el-form-item>

      <el-form-item
        v-for="(item, index) in formData.toTypes"
        :key="index"
        :label="item.name"
      >
        <el-input
          v-model="item.columnValue"
          :placeholder="item.remark"
        />
      </el-form-item>

      <el-form-item
        :label="$t('couponPropertyPoolManage.edit.stock')"
        prop="stock"
      >
        <el-input
          v-model="formData.stock"
          :placeholder="$t('couponPropertyPoolManage.edit.stockPlaceholder')"
        />
      </el-form-item>

      <el-form-item
        :label="$t('couponPropertyPoolManage.edit.validityDay')"
        prop="validityDay"
      >
        <el-input
          v-model="formData.validityDay"
          :placeholder="$t('couponPropertyPoolManage.edit.validityDayPlaceholder')"
        />
      </el-form-item>

      <el-form-item
        :label="$t('couponPropertyPoolManage.edit.remark')"
        prop="remark"
      >
        <el-input
          v-model="formData.remark"
          type="textarea"
          :placeholder="$t('couponPropertyPoolManage.edit.remarkPlaceholder')"
          :rows="3"
        />
      </el-form-item>
    </el-form>

    <span slot="footer" class="dialog-footer">
      <el-button @click="visible = false">{{ $t('common.cancel') }}</el-button>
      <el-button type="primary" @click="handleSubmit">{{ $t('common.confirm') }}</el-button>
    </span>
  </el-dialog>
</template>

<script>
import { updateCouponPropertyPool, listCouponKey } from '@/api/scm/couponPropertyPoolManageApi'
import { getCommunityId } from '@/api/community/communityApi'

export default {
  name: 'EditCouponPropertyPool',
  data() {
    return {
      visible: false,
      formData: {
        cppId: '',
        couponName: '',
        fromType: '2002',
        toType: '',
        stock: '',
        validityDay: '',
        remark: '',
        toTypes: [],
        communityId: getCommunityId()
      },
      rules: {
        couponName: [
          { required: true, message: this.$t('couponPropertyPoolManage.validate.couponNameRequired'), trigger: 'blur' },
          { max: 64, message: this.$t('couponPropertyPoolManage.validate.couponNameMaxLength'), trigger: 'blur' }
        ],
        toType: [
          { required: true, message: this.$t('couponPropertyPoolManage.validate.toTypeRequired'), trigger: 'change' }
        ],
        stock: [
          { required: true, message: this.$t('couponPropertyPoolManage.validate.stockRequired'), trigger: 'blur' }
        ],
        validityDay: [
          { required: true, message: this.$t('couponPropertyPoolManage.validate.validityDayRequired'), trigger: 'blur' }
        ],
        remark: [
          { required: true, message: this.$t('couponPropertyPoolManage.validate.remarkRequired'), trigger: 'blur' }
        ],
        cppId: [
          { required: true, message: this.$t('couponPropertyPoolManage.validate.cppIdRequired'), trigger: 'blur' }
        ]
      },
      toTypeOptions: [
        { value: '1011', label: this.$t('couponPropertyPoolManage.toType.shopping') },
        { value: '2002', label: this.$t('couponPropertyPoolManage.toType.payment') },
        { value: '3003', label: this.$t('couponPropertyPoolManage.toType.repair') },
        { value: '4004', label: this.$t('couponPropertyPoolManage.toType.parking') },
        { value: '5005', label: this.$t('couponPropertyPoolManage.toType.charging') }
      ]
    }
  },
  methods: {
    open(row) {
      this.formData = {
        ...this.formData,
        ...row,
        toType: row.toType || ''
      }
      this.formData.toTypes = row.configs || []
      this.visible = true
    },
    handleClose() {
      this.$refs.form.resetFields()
      this.formData.toTypes = []
    },
    async handleToTypeChange(val) {
      if (!val) return
      try {
        const params = {
          beanName: val,
          page: 1,
          row: 100
        }
        const { data } = await listCouponKey(params)
        this.formData.toTypes = data
      } catch (error) {
        console.error('获取优惠券属性失败:', error)
      }
    },
    handleSubmit() {
      this.$refs.form.validate(async valid => {
        if (valid) {
          try {
            await updateCouponPropertyPool(this.formData)
            this.$message.success(this.$t('common.operationSuccess'))
            this.visible = false
            this.$emit('success')
          } catch (error) {
            this.$message.error(error.message || this.$t('couponPropertyPoolManage.edit.error'))
          }
        }
      })
    }
  }
}
</script>