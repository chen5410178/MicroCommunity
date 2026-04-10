<template>
  <el-dialog :title="$t('service.dialog.editTitle')" :visible.sync="dialogVisible" width="500px" @close="handleClose">
    <el-form ref="form" :model="form" :rules="rules" label-width="100px">

      <el-form-item :label="$t('service.form.serviceCode')" prop="serviceCode">
        <el-input v-model="form.serviceCode" />
      </el-form-item>
      <el-form-item :label="$t('service.form.name')" prop="name">
        <el-input v-model="form.name" />
      </el-form-item>
      <el-form-item :label="$t('service.form.url')" prop="url">
        <el-select v-model="form.url" style="width: 100%" placeholder="请选择">
          <el-option :label="item.name" :value="item.statusCd" v-for="(item, index) in urls" :key="index" />
        </el-select>
      </el-form-item>
      <el-form-item :label="$t('service.form.method')" prop="method">
        <el-select v-model="form.method" style="width: 100%" placeholder="请选择">
          <el-option label="http get" value="GET" />
          <el-option label="http post" value="POST" />
        </el-select>
      </el-form-item>
      <el-form-item :label="$t('service.form.timeout')" prop="timeout">
        <el-input v-model="form.timeout" />
      </el-form-item>
      <el-form-item :label="$t('service.form.retryCount')" prop="retryCount">
        <el-input v-model="form.retryCount" />
      </el-form-item>
    </el-form>
    <span slot="footer" class="dialog-footer">
      <el-button @click="handleClose">{{ $t('service.dialog.cancel') }}</el-button>
      <el-button type="primary" @click="handleSubmit">{{ $t('service.dialog.confirm') }}</el-button>
    </span>
  </el-dialog>
</template>

<script>
import { updateService } from '@/api/dev/serviceApi'
import { getDict } from '@/api/community/communityApi'

export default {
  name: 'EditService',
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
      urls: [],
      form: {
        serviceId: '',
        serviceCode: '',
        name: '',
        url: '',
        method: '',
        timeout: '60',
        retryCount: '3',
        businessTypeCd: 'API',
        seq: '1',
        isInstance: 'CMD',
        provideAppId: '8000418002',
      },
      rules: {
        serviceId: [
          { required: true, message: this.$t('service.validate.serviceId'), trigger: 'blur' }
        ],
        serviceCode: [
          { required: true, message: this.$t('service.validate.serviceCode'), trigger: 'blur' }
        ],
        name: [
          { required: true, message: this.$t('service.validate.name'), trigger: 'blur' }
        ],
        timeout: [
          { required: true, message: this.$t('service.validate.timeout'), trigger: 'blur' }
        ],
        retryCount: [
          { required: true, message: this.$t('service.validate.retryCount'), trigger: 'blur' }
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
          this.form.provideAppId = '8000418002'
        }
      },
      immediate: true
    },
    dialogVisible(val) {
      this.$emit('update:visible', val)
    }
  },
  created() {
    this._loadUrl()
  },
  methods: {
    handleClose() {
      this.dialogVisible = false
      this.$refs.form && this.$refs.form.resetFields()
    },
    async _loadUrl() {
      try {
        const  data  = await getDict('c_service', 'url')
        this.urls = data;
      } catch (error) {
        console.error(error)
      }
    },
    async handleSubmit() {
      try {
        await this.$refs.form.validate()
        await updateService(this.form)
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