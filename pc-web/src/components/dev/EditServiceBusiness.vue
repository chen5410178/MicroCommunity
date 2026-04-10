<template>
  <el-dialog :title="$t('serviceBusiness.dialog.editTitle')" :visible.sync="dialogVisible" width="500px"
    @close="handleClose">
    <el-form ref="form" :model="form" :rules="rules" label-width="100px">
      <el-form-item :label="$t('serviceBusiness.form.name')" prop="name">
        <el-input v-model="form.name" />
      </el-form-item>
      <el-form-item :label="$t('serviceBusiness.form.businessTypeCd')" prop="businessTypeCd">
        <el-input v-model="form.businessTypeCd" />
      </el-form-item>
      <el-form-item :label="$t('serviceBusiness.form.invokeType')" prop="method">
        <el-select v-model="form.invokeType" style="width: 100%" placeholder="请选择">
          <el-option label="微服务POST方式" value="1" />
          <el-option label="webservice方式" value="2" />
          <el-option label="http post方式" value="3" />
        </el-select>
      </el-form-item>
      <el-form-item :label="$t('serviceBusiness.form.url')" prop="url">
        <el-input v-model="form.url" />
      </el-form-item>
      <el-form-item :label="$t('serviceBusiness.form.messageTopic')" prop="messageTopic">
        <el-input v-model="form.messageTopic" />
      </el-form-item>
      <el-form-item :label="$t('serviceBusiness.form.timeout')" prop="timeout">
        <el-input v-model="form.timeout" />
      </el-form-item>
      <el-form-item :label="$t('serviceBusiness.form.retryCount')" prop="retryCount">
        <el-input v-model="form.retryCount" />
      </el-form-item>
    </el-form>
    <span slot="footer" class="dialog-footer">
      <el-button @click="handleClose">{{ $t('serviceBusiness.dialog.cancel') }}</el-button>
      <el-button type="primary" @click="handleSubmit">{{ $t('serviceBusiness.dialog.confirm') }}</el-button>
    </span>
  </el-dialog>
</template>

<script>
import { updateServiceBusiness } from '@/api/dev/serviceBusinessApi'

export default {
  name: 'EditServiceBusiness',
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
        businessTypeCd: '',
        invokeType: '',
        url: '',
        name: '',
        messageTopic: '',
        timeout: '',
        retryCount: '',
        createTime: '',
      },
      rules: {
        invokeType: [
          { required: true, message: this.$t('serviceBusiness.validate.invokeType'), trigger: 'blur' }
        ],
        timeout: [
          { required: true, message: this.$t('serviceBusiness.validate.timeout'), trigger: 'blur' }
        ],
        retryCount: [
          { required: true, message: this.$t('serviceBusiness.validate.retryCount'), trigger: 'blur' }
        ],
        createTime: [
          { required: true, message: this.$t('serviceBusiness.validate.createTime'), trigger: 'blur' }
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
        await updateServiceBusiness(this.form)
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