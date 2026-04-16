<template>
  <div class="new-oa-workflow-form">
    <el-card class="form-card">
      <el-form ref="form" :model="formData" label-width="120px" label-position="right" class="text-left">
        <div v-for="(item, index) in components" :key="index">
          <!-- 标题 -->
          <el-divider v-if="item.type == 'text'" content-position="left">
            <span class="form-title">{{ item.text }}</span>
          </el-divider>

          <!-- 文本框 -->
          <el-form-item v-if="item.type == 'textfield'" :label="item.label" :prop="item.key"
            :rules="getValidationRules(item)">
            <el-input v-model="formData[item.key]" :placeholder="item.description" clearable>
            </el-input>
          </el-form-item>

          <!-- 数字框 -->
          <el-form-item v-if="item.type == 'number'" :label="item.label" :prop="item.key"
            :rules="getValidationRules(item)">
            <el-input-number v-model="formData[item.key]" :placeholder="item.description" :min="0" style="width: 100%">
            </el-input-number>
          </el-form-item>

          <!-- 文本域 -->
          <el-form-item v-if="item.type == 'textarea'" :label="item.label" :prop="item.key"
            :rules="getValidationRules(item)">
            <el-input v-model="formData[item.key]" type="textarea" :rows="4" :placeholder="item.description">
            </el-input>
          </el-form-item>

          <!-- 日期选择 -->
          <el-form-item v-if="item.type == 'textdate'" :label="item.label" :prop="item.key"
            :rules="getValidationRules(item)">
            <el-date-picker v-model="formData[item.key]" type="date" :placeholder="item.description" format="yyyy-MM-dd"
              value-format="yyyy-MM-dd" style="width: 100%">
            </el-date-picker>
          </el-form-item>

          <!-- 时间选择 -->
          <el-form-item v-if="item.type == 'textdatetime'" :label="item.label" :prop="item.key"
            :rules="getValidationRules(item)">
            <el-time-picker v-model="formData[item.key]" :placeholder="item.description" format="HH:mm"
              value-format="HH:mm" style="width: 100%">
            </el-time-picker>
          </el-form-item>

          <!-- 单选框 -->
          <el-form-item v-if="item.type == 'radio'" :label="item.label" :prop="item.key"
            :rules="getValidationRules(item)">
            <el-radio-group v-model="formData[item.key]">
              <el-radio v-for="(option, optionIndex) in item.values" :key="optionIndex" :label="option.value">
                {{ option.label }}
              </el-radio>
            </el-radio-group>
          </el-form-item>

          <!-- 下拉选择 -->
          <el-form-item v-if="item.type == 'select'" :label="item.label" :prop="item.key"
            :rules="getValidationRules(item)">
            <el-select v-model="formData[item.key]" :placeholder="item.description" style="width: 100%" clearable>
              <el-option v-for="(option, optionIndex) in item.values" :key="optionIndex" :label="option.label"
                :value="option.value">
              </el-option>
            </el-select>
          </el-form-item>

          <!-- 复选框 -->
          <el-form-item v-if="item.type == 'checkbox'" :label="item.label" :prop="item.key"
            :rules="getValidationRules(item)">
            <el-checkbox-group v-model="formData[item.key]">
              <el-checkbox v-for="(option, optionIndex) in item.values" :key="optionIndex" :label="option.value">
                {{ option.label }}
              </el-checkbox>
            </el-checkbox-group>
          </el-form-item>

          <!-- 文件上传 -->
          <el-form-item v-if="item.type == 'button' && item.action == 'submit'" :label="$t('common.file')">
            <div class="upload-section">
              <upload-file ref="uploadFile" @notify="handleFileUpload" />
            </div>
          </el-form-item>

          <!-- 提交按钮 -->
          <el-form-item v-if="item.type == 'button' && item.action == 'submit'" class="text-right">
            <el-button type="primary" @click="_doSubmit" :loading="submitLoading">
              <i class="el-icon-check"></i>
              {{ item.label }}
            </el-button>
          </el-form-item>

          <!-- 重置按钮 -->
          <el-form-item v-if="item.type == 'button' && item.action == 'reset'" class="text-right">
            <el-button @click="_doReset">
              <i class="el-icon-refresh"></i>
              {{ item.label }}
            </el-button>
          </el-form-item>
        </div>

      </el-form>
    </el-card>
  </div>
</template>

<script>
import UploadFile from '@/components/upload/uploadFile'
import { queryOaWorkflowForm, saveOaWorkflowFormData } from '@/api/oa/newOaWorkflowApi'

export default {
  name: 'NewOaWorkflowForm',
  components: { UploadFile },

  data() {
    return {
      formJson: {},
      components: [],
      formData: {},
      fileName: '',
      realFileName: '',
      callBackListener: 'newOaWorkflowForm',
      callBackFunction: 'fileName',
      submitLoading: false,
      showUploadComponent: false,
      flowId: null
    }
  },

  mounted() {
  },

  methods: {
    open(params) {
      this.flowId = params.flowId
      this.listOaWorkflowForm()
    },

    async listOaWorkflowForm() {
      try {
        const res = await queryOaWorkflowForm({
          page: 1,
          row: 1,
          flowId: this.flowId
        })
        console.log(res)

        this.formJson = JSON.parse(res.data[0].formJson)
        this.initForm()

      } catch (error) {
        console.error('获取表单配置失败:', error)
        this.$message.error('获取表单配置失败')
      }
    },

    initForm() {

      let _components = this.formJson.components
      this.formData = {}

      _components.forEach(item => {
        if (item.type !== 'button' && item.type !== 'text') {
          if (item.type === 'checkbox') {
            this.$set(this.formData, item.key, []);
          } else if (item.type === 'radio' || item.type === 'select') {
            this.$set(this.formData, item.key, item.values && item.values.length > 0 ? item.values[0].value : '');
          } else {
            this.$set(this.formData, item.key, '');
          }
        }
      });
      this.components = _components
    },

    getValidationRules(item) {
      const rules = []
      if (item.validate && item.validate.required) {
        rules.push({
          required: true,
          message: `${item.label}不能为空`,
          trigger: item.type === 'select' ? 'change' : 'blur'
        })
      }

      // 添加其他验证规则，如正则表达式
      if (item.validate && item.validate.pattern) {
        rules.push({
          pattern: new RegExp(item.validate.pattern),
          message: item.validate.customMessage || `${item.label}格式不正确`,
          trigger: 'blur'
        })
      }

      return rules
    },

    async _doSubmit() {
      try {
        // 表单验证
        await this.$refs.form.validate()

        this.submitLoading = true

        const _data = {
          fileName: this.fileName,
          realFileName: this.realFileName,
          flowId: this.flowId,
          ...this.formData
        }

        const res = await saveOaWorkflowFormData(_data)
        if (res.code === 0) {
          this.$message.success(this.$t('common.operationSuccess'))
          this.resetForm()
          console.log('子组件准备发出 success 事件', 'newOaWorkflowUndo')
          this.$emit('success', 'newOaWorkflowUndo')
          console.log('子组件已发出 success 事件')
        } else {
          this.$message.error(res.msg || this.$t('common.submitFailed'))
        }
      } catch (error) {
        console.log('提交表单失败，错误信息:', error)
        if (error.message) {
          // 表单验证错误
          return
        }
        console.error('提交表单失败:', error)
        this.$message.error(this.$t('common.submitFailed'))
      } finally {
        this.submitLoading = false
      }
    },

    _doReset() {
      this.$refs.form.resetFields()
      // 特殊处理复选框
      Object.keys(this.formData).forEach(key => {
        const component = this.components.find(c => c.key === key)
        if (component && component.type === 'checkbox') {
          this.formData[key] = []
        }
      })
    },

    _doUploadFile() {
      this.showUploadComponent = true
      this.$nextTick(() => {
        this.$refs.uploadFile.open()
      })
    },

    resetForm() {
      this.$refs.form.resetFields()
      if (this.$refs.uploadFile && this.$refs.uploadFile.clear) {
        this.$refs.uploadFile.clear()
      }
      this.fileName = ''
      this.realFileName = ''
      this.formJson = {}
      this.components = []
      this.formData = {}
      this.showUploadComponent = false
    },

    handleFileUpload(fileInfo) {
      this.fileName = fileInfo.fileName
      this.realFileName = fileInfo.realFileName
    }
  }
}
</script>

<style lang="scss" scoped>
.new-oa-workflow-form {
  .form-card {
    .form-title {
      font-size: 16px;
      font-weight: 600;
      color: #303133;
    }

    .upload-section {
      .file-info {
        margin-bottom: 10px;
      }
    }

    .el-form-item {
      margin-bottom: 22px;
    }

    .el-divider {
      margin: 24px 0;
    }
  }
}
</style>