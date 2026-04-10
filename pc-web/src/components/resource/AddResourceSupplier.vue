<template>
  <el-dialog
    :title="$t('addResourceSupplier.title')"
    :visible.sync="visible"
    width="50%"
    @close="handleClose"
  >
    <el-form ref="form" :model="form" :rules="rules" label-width="150px">
      <el-form-item
        :label="$t('addResourceSupplier.supplierName')"
        prop="supplierName"
      >
        <el-input
          v-model="form.supplierName"
          :placeholder="$t('addResourceSupplier.required')"
        />
      </el-form-item>
      <el-form-item
        :label="$t('addResourceSupplier.supplierAddress')"
        prop="address"
      >
        <el-input
          v-model="form.address"
          :placeholder="$t('addResourceSupplier.required')"
        />
      </el-form-item>
      <el-form-item
        :label="$t('addResourceSupplier.supplierContact')"
        prop="tel"
      >
        <el-input
          v-model="form.tel"
          :placeholder="$t('addResourceSupplier.required')"
        />
      </el-form-item>
      <el-form-item
        :label="$t('addResourceSupplier.contactName')"
        prop="contactName"
      >
        <el-input
          v-model="form.contactName"
          :placeholder="$t('addResourceSupplier.required')"
        />
      </el-form-item>
      <el-form-item :label="$t('addResourceSupplier.bank')" prop="accountBank">
        <el-input
          v-model="form.accountBank"
          :placeholder="$t('addResourceSupplier.optional')"
        />
      </el-form-item>
      <el-form-item
        :label="$t('addResourceSupplier.bankAccount')"
        prop="bankAccountNumber"
      >
        <el-input
          v-model="form.bankAccountNumber"
          :placeholder="$t('addResourceSupplier.optional')"
        />
      </el-form-item>
      <el-form-item :label="$t('addResourceSupplier.remark')" prop="remark">
        <el-input
          v-model="form.remark"
          :placeholder="$t('addResourceSupplier.optional')"
        />
      </el-form-item>
    </el-form>
    <span slot="footer" class="dialog-footer">
      <el-button @click="visible = false">
        {{ $t('addResourceSupplier.cancel') }}
      </el-button>
      <el-button type="primary" @click="handleSubmit">
        {{ $t('addResourceSupplier.save') }}
      </el-button>
    </span>
  </el-dialog>
</template>

<script>
import { saveResourceSupplier } from '@/api/resource/resourceSupplierManageApi'
import { getCommunityId } from '@/api/community/communityApi'

export default {
  name: 'AddResourceSupplier',
  data() {
    return {
      visible: false,
      form: {
        supplierName: '',
        address: '',
        tel: '',
        contactName: '',
        accountBank: '',
        bankAccountNumber: '',
        remark: ''
      },
      rules: {
        supplierName: [
          { required: true, message: this.$t('addResourceSupplier.required'), trigger: 'blur' },
          { max: 100, message: '长度不能超过100个字符', trigger: 'blur' }
        ],
        address: [
          { required: true, message: this.$t('addResourceSupplier.required'), trigger: 'blur' },
          { max: 100, message: '长度不能超过100个字符', trigger: 'blur' }
        ],
        tel: [
          { required: true, message: this.$t('addResourceSupplier.required'), trigger: 'blur' }
        ],
        contactName: [
          { required: true, message: this.$t('addResourceSupplier.required'), trigger: 'blur' },
          { max: 50, message: '长度不能超过50个字符', trigger: 'blur' }
        ],
        accountBank: [
          { max: 150, message: '长度不能超过150个字符', trigger: 'blur' }
        ],
        bankAccountNumber: [
          { max: 200, message: '长度不能超过200个字符', trigger: 'blur' }
        ],
        remark: [
          { max: 512, message: '长度不能超过512个字符', trigger: 'blur' }
        ]
      }
    }
  },
  methods: {
    open() {
      this.visible = true
      this.resetForm()
    },
    resetForm() {
      this.form = {
        supplierName: '',
        address: '',
        tel: '',
        contactName: '',
        accountBank: '',
        bankAccountNumber: '',
        remark: ''
      }
      this.$nextTick(() => {
        this.$refs.form.clearValidate()
      })
    },
    handleClose() {
      this.resetForm()
    },
    handleSubmit() {
      this.$refs.form.validate(async valid => {
        if (valid) {
          try {
            const params = {
              ...this.form,
              communityId: getCommunityId()
            }
            await saveResourceSupplier(params)
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