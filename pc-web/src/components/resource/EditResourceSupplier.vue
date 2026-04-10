<template>
  <el-dialog
    :title="$t('editResourceSupplier.title')"
    :visible.sync="visible"
    width="50%"
    @close="handleClose"
  >
    <el-form ref="form" :model="form" :rules="rules" label-width="150px">
      <el-form-item
        :label="$t('editResourceSupplier.supplierName')"
        prop="supplierName"
      >
        <el-input
          v-model="form.supplierName"
          :placeholder="$t('editResourceSupplier.required')"
        />
      </el-form-item>
      <el-form-item
        :label="$t('editResourceSupplier.supplierAddress')"
        prop="address"
      >
        <el-input
          v-model="form.address"
          :placeholder="$t('editResourceSupplier.required')"
        />
      </el-form-item>
      <el-form-item
        :label="$t('editResourceSupplier.supplierContact')"
        prop="tel"
      >
        <el-input
          v-model="form.tel"
          :placeholder="$t('editResourceSupplier.required')"
        />
      </el-form-item>
      <el-form-item
        :label="$t('editResourceSupplier.contactName')"
        prop="contactName"
      >
        <el-input
          v-model="form.contactName"
          :placeholder="$t('editResourceSupplier.required')"
        />
      </el-form-item>
      <el-form-item :label="$t('editResourceSupplier.bank')" prop="accountBank">
        <el-input
          v-model="form.accountBank"
          :placeholder="$t('editResourceSupplier.optional')"
        />
      </el-form-item>
      <el-form-item
        :label="$t('editResourceSupplier.bankAccount')"
        prop="bankAccountNumber"
      >
        <el-input
          v-model="form.bankAccountNumber"
          :placeholder="$t('editResourceSupplier.optional')"
        />
      </el-form-item>
      <el-form-item :label="$t('editResourceSupplier.remark')" prop="remark">
        <el-input
          v-model="form.remark"
          :placeholder="$t('editResourceSupplier.optional')"
        />
      </el-form-item>
    </el-form>
    <span slot="footer" class="dialog-footer">
      <el-button @click="visible = false">
        {{ $t('editResourceSupplier.cancel') }}
      </el-button>
      <el-button type="primary" @click="handleSubmit">
        {{ $t('editResourceSupplier.save') }}
      </el-button>
    </span>
  </el-dialog>
</template>

<script>
import { updateResourceSupplier } from '@/api/resource/resourceSupplierManageApi'
import { getCommunityId } from '@/api/community/communityApi'

export default {
  name: 'EditResourceSupplier',
  data() {
    return {
      visible: false,
      form: {
        rsId: '',
        supplierName: '',
        address: '',
        tel: '',
        contactName: '',
        accountBank: '',
        bankAccountNumber: '',
        remark: ''
      },
      rules: {
        rsId: [
          { required: true, message: '供应商编号不能为空', trigger: 'blur' }
        ],
        supplierName: [
          { required: true, message: this.$t('editResourceSupplier.required'), trigger: 'blur' },
          { max: 100, message: '长度不能超过100个字符', trigger: 'blur' }
        ],
        address: [
          { required: true, message: this.$t('editResourceSupplier.required'), trigger: 'blur' },
          { max: 100, message: '长度不能超过100个字符', trigger: 'blur' }
        ],
        tel: [
          { required: true, message: this.$t('editResourceSupplier.required'), trigger: 'blur' }
        ],
        contactName: [
          { required: true, message: this.$t('editResourceSupplier.required'), trigger: 'blur' },
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
    open(row) {
      this.visible = true
      this.form = { ...row }
      this.$nextTick(() => {
        this.$refs.form.clearValidate()
      })
    },
    handleClose() {
      this.form = {
        rsId: '',
        supplierName: '',
        address: '',
        tel: '',
        contactName: '',
        accountBank: '',
        bankAccountNumber: '',
        remark: ''
      }
    },
    handleSubmit() {
      this.$refs.form.validate(async valid => {
        if (valid) {
          try {
            const params = {
              ...this.form,
              communityId: getCommunityId()
            }
            await updateResourceSupplier(params)
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