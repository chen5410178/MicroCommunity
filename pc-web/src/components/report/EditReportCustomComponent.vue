<template>
  <el-dialog
    :title="$t('common.edit')"
    :visible.sync="visible"
    width="80%"
    @close="handleClose"
  >
    <el-form
      ref="form"
      :model="form"
      :rules="rules"
      label-width="150px"
      label-position="right"
    >
      <el-form-item
        :label="$t('reportCustomComponent.table.name')"
        prop="name"
      >
        <el-input
          v-model="form.name"
          :placeholder="$t('reportCustomComponent.placeholder.requiredName')"
        />
      </el-form-item>
      <el-form-item
        :label="$t('reportCustomComponent.table.componentType')"
        prop="componentType"
      >
        <el-select
          v-model="form.componentType"
          :placeholder="$t('reportCustomComponent.placeholder.requiredType')"
          style="width: 100%"
          disabled
        >
          <el-option
            value="1001"
            :label="$t('reportCustomComponent.type.table')"
          />
          <el-option
            value="2002"
            :label="$t('reportCustomComponent.type.pie')"
          />
        </el-select>
      </el-form-item>
      <el-form-item
        :label="$t('reportCustomComponent.table.queryModel')"
        prop="queryModel"
      >
        <el-select
          v-model="form.queryModel"
          :placeholder="$t('reportCustomComponent.placeholder.requiredQueryModel')"
          style="width: 100%"
        >
          <el-option value="1" label="SQL" />
          <el-option value="2" label="Java" />
        </el-select>
      </el-form-item>
      <el-form-item
        v-if="form.queryModel === '1'"
        label="SQL"
      >
        <el-input
          v-model="form.componentSql"
          type="textarea"
          :rows="15"
          :placeholder="$t('reportCustomComponent.placeholder.optionalSql')"
        />
      </el-form-item>
      <el-form-item
        v-if="form.queryModel === '2'"
        label="Java"
      >
        <el-input
          v-model="form.javaScript"
          type="textarea"
          :rows="15"
          :placeholder="$t('reportCustomComponent.placeholder.optionalJava')"
        />
      </el-form-item>
      <el-form-item
        :label="$t('reportCustomComponent.table.remark')"
      >
        <el-input
          v-model="form.remark"
          type="textarea"
          :placeholder="$t('reportCustomComponent.placeholder.optionalRemark')"
        />
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
import { updateReportCustomComponent } from '@/api/report/reportCustomComponentManageApi'

export default {
  name: 'EditReportCustomComponent',
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
        componentId: '',
        name: '',
        componentType: '',
        queryModel: '',
        componentSql: '',
        javaScript: '',
        remark: ''
      },
      rules: {
        componentId: [
          { required: true, message: this.$t('reportCustomComponent.validate.requiredId'), trigger: 'blur' }
        ],
        name: [
          { required: true, message: this.$t('reportCustomComponent.validate.requiredName'), trigger: 'blur' },
          { max: 64, message: this.$t('reportCustomComponent.validate.maxName'), trigger: 'blur' }
        ],
        componentType: [
          { required: true, message: this.$t('reportCustomComponent.validate.requiredType'), trigger: 'change' },
          { max: 12, message: this.$t('reportCustomComponent.validate.maxType'), trigger: 'blur' }
        ],
        queryModel: [
          { required: true, message: this.$t('reportCustomComponent.validate.requiredQueryModel'), trigger: 'change' },
          { max: 1, message: this.$t('reportCustomComponent.validate.maxQueryModel'), trigger: 'blur' }
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
      immediate: true
    }
  },
  methods: {
    handleClose() {
      this.$refs.form.resetFields()
    },
    async handleSubmit() {
      try {
        await this.$refs.form.validate()
        await updateReportCustomComponent(this.form)
        this.$message.success(this.$t('common.operationSuccess'))
        this.$emit('success')
        this.$emit('update:visible', false)
      } catch (error) {
        if (error.message) {
          this.$message.error(error.message)
        }
      }
    }
  }
}
</script>