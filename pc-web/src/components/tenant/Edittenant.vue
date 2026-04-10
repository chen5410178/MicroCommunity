<template>
  <el-dialog
    :title="$t('tenant.dialog.editTitle')"
    :visible.sync="dialogVisible"
    width="500px"
    @close="handleClose"
  >
    <el-form
      ref="form"
      :model="form"
      :rules="rules"
      label-width="100px"
    >

      <el-form-item
        :label="$t('tenant.form.name')"
        prop="name"
      >
        <el-input v-model="form.name" />
      </el-form-item>
      <el-form-item
        :label="$t('tenant.form.address')"
        prop="address"
      >
        <el-input v-model="form.address" />
      </el-form-item>
      <el-form-item
        :label="$t('tenant.form.tel')"
        prop="tel"
      >
        <el-input v-model="form.tel" />
      </el-form-item>
    </el-form>
    <span slot="footer" class="dialog-footer">
      <el-button @click="handleClose">{{ $t('tenant.dialog.cancel') }}</el-button>
      <el-button type="primary" @click="handleSubmit">{{ $t('tenant.dialog.confirm') }}</el-button>
    </span>
  </el-dialog>
</template>

<script>
import { updateTenant } from '@/api/tenant/tenantApi'

export default {
  name: 'Edittenant',
  props: {
    visible: {
      type: Boolean,
      default: false
    },
    formData: {
      type: Object,
      default: () => ({})
    }
  },
  data() {
    return {
      dialogVisible: false,
      form: {
        tenantId: '',
        name: '',
        address: '',
        tel: '',
        createTime: '',
      },
      rules: {
        tenantId: [
          { required: true, message: this.$t('tenant.validate.tenantId'), trigger: 'blur' }
        ],
        name: [
          { required: true, message: this.$t('tenant.validate.name'), trigger: 'blur' }
        ],
        address: [
          { required: true, message: this.$t('tenant.validate.address'), trigger: 'blur' }
        ],
        tel: [
          { required: true, message: this.$t('tenant.validate.tel'), trigger: 'blur' }
        ],
        createTime: [
          { required: true, message: this.$t('tenant.validate.createTime'), trigger: 'blur' }
        ],
      }
    }
  },
  watch: {
    visible: {
      handler(val) {
        this.dialogVisible = val
        if (val && this.formData) {
          this.form = { ...this.formData }
        }
      },
      immediate: true
    },
    dialogVisible(val) {
      this.$emit('update:visible', val)
    }
  },
  methods: {
    handleClose() {
      this.dialogVisible = false
      this.$refs.form && this.$refs.form.resetFields()
    },
    async handleSubmit() {
      try {
        await this.$refs.form.validate()
        await updateTenant(this.form)
        this.$message.success(this.$t('common.operationSuccess'))
        this.handleClose()
        this.$emit('success')
      } catch (error) {
        console.error('编辑失败:', error)
      }
    }
  }
}
</script>

<style lang="scss" scoped>
.dialog-footer {
  text-align: right;
}
</style>