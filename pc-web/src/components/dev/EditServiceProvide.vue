<template>
  <el-dialog :title="$t('common.edit')" :visible.sync="visible" width="70%" @close="handleClose">
    <el-form ref="form" :model="form" :rules="rules" label-width="120px" label-position="right">
      <el-form-item :label="$t('serviceProvideManage.table.name')" prop="name">
        <el-input v-model="form.name" :placeholder="$t('serviceProvideManage.table.namePlaceholder')" />
      </el-form-item>
      <el-form-item :label="$t('serviceProvideManage.table.serviceCode')" prop="serviceCode">
        <el-input v-model="form.serviceCode" :placeholder="$t('serviceProvideManage.table.serviceCodePlaceholder')" />
      </el-form-item>
      <el-form-item :label="$t('serviceProvideManage.table.params')" prop="params">
        <el-input v-model="form.params" :placeholder="$t('serviceProvideManage.table.paramsPlaceholder')" />
      </el-form-item>
      <el-form-item :label="$t('serviceProvideManage.table.queryModel')" prop="queryModel">
        <el-select v-model="form.queryModel" :placeholder="$t('serviceProvideManage.table.queryModelPlaceholder')"
          style="width: 100%">
          <el-option v-for="item in queryModelOptions" :key="item.value" :label="item.label" :value="item.value" />
        </el-select>
      </el-form-item>

      <el-form-item v-if="form.queryModel === '1'" label="SQL">
        <el-input v-model="form.sql" type="textarea" :rows="10"
          :placeholder="$t('serviceProvideManage.table.sqlPlaceholder')" />
      </el-form-item>

      <el-form-item v-if="form.queryModel === '1'" label="template">
        <el-input v-model="form.template" type="textarea" :rows="10"
          :placeholder="$t('serviceProvideManage.table.templatePlaceholder')" />
      </el-form-item>

      <el-form-item v-if="form.queryModel === '2'" label="proc">
        <el-input v-model="form.proc" type="textarea" :rows="10"
          :placeholder="$t('serviceProvideManage.table.procPlaceholder')" />
      </el-form-item>

      <el-form-item v-if="form.queryModel === '3'" label="Java">
        <el-input v-model="form.javaScript" type="textarea" :rows="10"
          placeholder="Java" />
      </el-form-item>

      <el-form-item :label="$t('serviceProvideManage.table.remark')">
        <el-input v-model="form.remark" type="textarea"
          :placeholder="$t('serviceProvideManage.table.remark')" />
      </el-form-item>
    </el-form>

    <div slot="footer" class="dialog-footer">
      <el-button @click="visible = false">
        {{ $t('common.cancel') }}
      </el-button>
      <el-button type="primary" @click="handleSubmit">
        {{ $t('common.save') }}
      </el-button>
    </div>
  </el-dialog>
</template>

<script>
import { updateServiceProvide } from '@/api/dev/serviceProvideManageApi'

export default {
  name: 'EditServiceProvide',
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
      form: {
        id: '',
        name: '',
        serviceCode: '',
        params: '',
        queryModel: '',
        sql: '',
        template: '',
        proc: '',
        javaScript: '',
        remark: ''
      },
      queryModelOptions: [
        { value: '1', label: 'SQL方式' },
        { value: '2', label: this.$t('serviceProvideManage.options.proc') },
        { value: '3', label: 'Java方式' }
      ],
      rules: {
        name: [
          { required: true, message: this.$t('serviceProvideManage.table.nameRequired'), trigger: 'blur' },
          { min: 2, max: 50, message: this.$t('serviceProvideManage.table.nameLength'), trigger: 'blur' }
        ],
        serviceCode: [
          { required: true, message: this.$t('serviceProvideManage.table.serviceCodeRequired'), trigger: 'blur' },
          { min: 2, max: 50, message: this.$t('serviceProvideManage.table.serviceCodeLength'), trigger: 'blur' }
        ],
        params: [
          { required: true, message: this.$t('serviceProvideManage.table.paramsRequired'), trigger: 'blur' },
          { max: 500, message: this.$t('serviceProvideManage.table.paramsMaxLength'), trigger: 'blur' }
        ],
        queryModel: [
          { required: true, message: this.$t('serviceProvideManage.table.queryModelRequired'), trigger: 'change' }
        ]
      }
    }
  },
  watch: {
    formData: {
      handler(val) {
        if (val) {
          this.form = { ...val }
        }
      },
      immediate: true,
      deep: true
    }
  },
  methods: {
    handleClose() {
      this.$refs.form.resetFields()
      this.$emit('update:visible', false)
    },
    async handleSubmit() {
      try {
        await this.$refs.form.validate()
        await updateServiceProvide(this.form)
        this.$message.success(this.$t('common.operationSuccess'))
        this.$emit('success')
        this.handleClose()
      } catch (error) {
        if (error.message) {
          this.$message.error(error.message)
        }
      }
    }
  }
}
</script>