<template>
  <el-dialog
    :title="$t('supplierTypeManage.edit.title')"
    :visible.sync="visible"
    width="50%"
    @close="handleClose"
  >
    <el-form ref="form" :model="form" :rules="rules" label-width="120px">
      <el-form-item :label="$t('supplierTypeManage.form.typeCd')" prop="typeCd">
        <el-input v-model="form.typeCd" :placeholder="$t('supplierTypeManage.placeholder.typeCd')" />
      </el-form-item>
      <el-form-item :label="$t('supplierTypeManage.form.typeName')" prop="typeName">
        <el-input v-model="form.typeName" :placeholder="$t('supplierTypeManage.placeholder.typeName')" />
      </el-form-item>
      <el-form-item :label="$t('supplierTypeManage.form.remark')" prop="remark">
        <el-input v-model="form.remark" type="textarea" :placeholder="$t('supplierTypeManage.placeholder.remark')" />
      </el-form-item>
    </el-form>

    <div slot="footer" class="dialog-footer">
      <el-button @click="visible = false">{{ $t('common.cancel') }}</el-button>
      <el-button type="primary" @click="handleSubmit">{{ $t('common.confirm') }}</el-button>
    </div>
  </el-dialog>
</template>

<script>
import { updateSupplierType } from '@/api/scm/supplierTypeManageApi'

export default {
  name: 'EditSupplierType',
  data() {
    return {
      visible: false,
      form: {
        stId: '',
        typeCd: '',
        typeName: '',
        remark: ''
      },
      rules: {
        typeCd: [
          { required: true, message: this.$t('supplierTypeManage.validate.typeCdRequired'), trigger: 'blur' },
          { max: 32, message: this.$t('supplierTypeManage.validate.typeCdMaxLength'), trigger: 'blur' }
        ],
        typeName: [
          { required: true, message: this.$t('supplierTypeManage.validate.typeNameRequired'), trigger: 'blur' },
          { max: 255, message: this.$t('supplierTypeManage.validate.typeNameMaxLength'), trigger: 'blur' }
        ],
        remark: [
          { required: true, message: this.$t('supplierTypeManage.validate.remarkRequired'), trigger: 'blur' },
          { max: 512, message: this.$t('supplierTypeManage.validate.remarkMaxLength'), trigger: 'blur' }
        ],
        stId: [
          { required: true, message: this.$t('supplierTypeManage.validate.stIdRequired'), trigger: 'blur' }
        ]
      }
    }
  },
  methods: {
    open(row) {
      this.form = { ...row }
      this.visible = true
    },
    handleClose() {
      this.$refs.form.resetFields()
    },
    handleSubmit() {
      this.$refs.form.validate(async valid => {
        if (valid) {
          try {
            await updateSupplierType(this.form)
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