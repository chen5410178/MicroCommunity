<template>
  <el-dialog 
    :title="$t('supplierCoupon.addTitle')" 
    :visible.sync="visible"
    width="50%"
  >
    <el-form :model="form" :rules="rules" ref="form" label-width="120px">
      <el-form-item :label="$t('supplierCoupon.name')" prop="name">
        <el-input 
          v-model="form.name" 
          :placeholder="$t('supplierCoupon.required')"
        ></el-input>
      </el-form-item>
      
      <el-form-item :label="$t('supplierCoupon.thirdPartyId')" prop="businessKey">
        <el-input 
          v-model="form.businessKey" 
          :placeholder="$t('supplierCoupon.required')"
        ></el-input>
      </el-form-item>
      
      <el-form-item :label="$t('supplierCoupon.price')" prop="valuePrice">
        <el-input 
          v-model="form.valuePrice" 
          :placeholder="$t('supplierCoupon.required')"
        ></el-input>
      </el-form-item>
      
      <el-form-item :label="$t('supplierCoupon.remark')" prop="remark">
        <el-input 
          type="textarea" 
          v-model="form.remark" 
          :placeholder="$t('supplierCoupon.required')"
          rows="4"
        ></el-input>
      </el-form-item>
    </el-form>
    
    <div slot="footer" class="dialog-footer">
      <el-button @click="visible = false">
        {{ $t('supplierCoupon.cancel') }}
      </el-button>
      <el-button type="primary" @click="submitForm">
        {{ $t('supplierCoupon.confirm') }}
      </el-button>
    </div>
  </el-dialog>
</template>

<script>
import { addSupplierCoupon } from '@/api/scm/supplierCouponApi'

export default {
  name: 'AddSupplierCoupon',
  data() {
    return {
      visible: false,
      form: {
        name: '',
        supplierId: '',
        businessKey: '',
        valuePrice: '',
        remark: ''
      },
      rules: {
        name: [
          { required: true, message: this.$t('supplierCoupon.required'), trigger: 'blur' },
          { max: 64, message: this.$t('supplierCoupon.maxLength', { length: 64 }), trigger: 'blur' }
        ],
        businessKey: [
          { required: true, message: this.$t('supplierCoupon.required'), trigger: 'blur' },
          { max: 30, message: this.$t('supplierCoupon.maxLength', { length: 30 }), trigger: 'blur' }
        ],
        valuePrice: [
          { required: true, message: this.$t('supplierCoupon.required'), trigger: 'blur' },
          { max: 10, message: this.$t('supplierCoupon.maxLength', { length: 10 }), trigger: 'blur' }
        ],
        remark: [
          { required: true, message: this.$t('supplierCoupon.required'), trigger: 'blur' },
          { max: 1024, message: this.$t('supplierCoupon.maxLength', { length: 1024 }), trigger: 'blur' }
        ]
      }
    }
  },
  methods: {
    open(data) {
      this.form = {
        name: '',
        supplierId: data.supplierId,
        businessKey: '',
        valuePrice: '',
        remark: ''
      }
      this.visible = true
      this.$nextTick(() => {
        if (this.$refs.form) {
          this.$refs.form.clearValidate()
        }
      })
    },
    
    submitForm() {
      this.$refs.form.validate(async (valid) => {
        if (valid) {
          try {
            await addSupplierCoupon(this.form)
            this.$message.success(this.$t('common.operationSuccess'))
            this.visible = false
            this.$emit('success')
          } catch (error) {
            this.$message.error(error.message || this.$t('supplierCoupon.addFailed'))
          }
        }
      })
    }
  }
}
</script>