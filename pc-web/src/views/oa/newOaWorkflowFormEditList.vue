<template>
  <div class="new-oa-workflow-form-edit-container">
    <el-card class="box-card">
      <div slot="header" class="flex justify-between">
        <span>{{ $t('newOaWorkflowFormEdit.title') }}</span>
      </div>
      <el-form ref="form" :model="formData" class="text-left" label-width="120px">
        <el-row :gutter="20">
          <el-col :span="24">
            <div v-for="(item, index) in newOaWorkflowFormEditInfo.components" :key="index">
              <!-- 文本框 -->
              <el-form-item v-if="item.type === 'textfield'" :label="item.label"
                :prop="item.key"
                :rules="getValidationRules(item)">
                <el-input v-model="formData[item.key]" :placeholder="item.description" clearable />
              </el-form-item>

              <!-- 数字框 -->
              <el-form-item v-if="item.type === 'number'" :label="item.label" 
                :prop="item.key"
                :rules="getValidationRules(item)">
                <el-input-number v-model="formData[item.key]" :placeholder="item.description" 
                  controls-position="right" style="width: 100%" />
              </el-form-item>

              <!-- 文本域 -->
              <el-form-item v-if="item.type === 'textarea'" :label="item.label"
                :prop="item.key"
                :rules="getValidationRules(item)">
                <el-input v-model="formData[item.key]" type="textarea" :placeholder="item.description" 
                  :rows="4" />
              </el-form-item>

              <!-- 日期选择 -->
              <el-form-item v-if="item.type === 'textdate'" :label="item.label"
                :prop="item.key"
                :rules="getValidationRules(item)">
                <el-date-picker v-model="formData[item.key]" type="date" :placeholder="item.description"
                  value-format="yyyy-MM-dd" style="width: 100%" />
              </el-form-item>

              <!-- 日期时间选择 -->
              <el-form-item v-if="item.type === 'textdatetime'" :label="item.label"
                :prop="item.key"
                :rules="getValidationRules(item)">
                <el-date-picker v-model="formData[item.key]" type="datetime" :placeholder="item.description"
                  value-format="yyyy-MM-dd HH:mm:ss" style="width: 100%" />
              </el-form-item>

              <!-- 单选框 -->
              <el-form-item v-if="item.type === 'radio'" :label="item.label"
                :prop="item.key"
                :rules="getValidationRules(item)">
                <el-radio-group v-model="formData[item.key]">
                  <el-radio v-for="(option, optionIndex) in item.values" :key="optionIndex"
                    :label="option.value">
                    {{ option.label }}
                  </el-radio>
                </el-radio-group>
              </el-form-item>

              <!-- 下拉选择 -->
              <el-form-item v-if="item.type === 'select'" :label="item.label"
                :prop="item.key"
                :rules="getValidationRules(item)">
                <el-select v-model="formData[item.key]" :placeholder="item.description" 
                  style="width: 100%" clearable>
                  <el-option v-for="(option, optionIndex) in item.values" :key="optionIndex"
                    :label="option.label" :value="option.value">
                  </el-option>
                </el-select>
              </el-form-item>

              <!-- 复选框 - 修复数组类型问题 -->
              <el-form-item v-if="item.type === 'checkbox'" :label="item.label"
                :prop="item.key"
                :rules="getValidationRules(item)">
                <el-checkbox-group v-model="formData[item.key]">
                  <el-checkbox v-for="(option, optionIndex) in item.values" :key="optionIndex"
                    :label="option.value">
                    {{ option.label }}
                  </el-checkbox>
                </el-checkbox-group>
              </el-form-item>
            </div>

            <!-- 文件上传 -->
            <el-form-item :label="$t('common.file')">
              <div class="file-info" v-if="newOaWorkflowFormEditInfo.fileName">
                <el-tag type="info">{{ newOaWorkflowFormEditInfo.fileName }}</el-tag>
              </div>
              <upload-file ref="uploadFile" @notify="handleFileUpload" />
            </el-form-item>

            <!-- 按钮区域 -->
            <div class="form-footer">
              <el-button type="primary" @click="_submitFormData" :loading="submitLoading">
                <i class="el-icon-check"></i>
                {{ $t('newOaWorkflowFormEdit.submit') }}
              </el-button>
              <el-button type="warning" @click="closeEditInfo">
                {{ $t('newOaWorkflowFormEdit.cancel') }}
              </el-button>
            </div>
          </el-col>
        </el-row>
      </el-form>
    </el-card>
  </div>
</template>

<script>
import { queryOaWorkflowForm, queryOaWorkflowFormData, updateOaWorkflowFormData } from '@/api/oa/newOaWorkflowFormEditApi'
import UploadFile from '@/components/upload/uploadFile'

export default {
  name: 'NewOaWorkflowFormEditList',
  components: {
    UploadFile
  },
  data() {
    return {
      newOaWorkflowFormEditInfo: {
        formJson: {},
        components: [],
        conditions: {},
        flowId: '',
        id: '',
        fileName: '',
        realFileName: ''
      },
      formData: {}, // 新增：用于表单数据绑定
      submitLoading: false
    }
  },
  created() {
    this.newOaWorkflowFormEditInfo.flowId = this.$route.query.flowId
    this.newOaWorkflowFormEditInfo.id = this.$route.query.id
    this._listOaWorkflowFormEdit(1, 100)
  },
  methods: {
    async _listOaWorkflowFormEdit(page, rows) {
      try {
        const params = {
          page,
          row: rows,
          flowId: this.newOaWorkflowFormEditInfo.flowId
        }

        const { data } = await queryOaWorkflowForm(params)
        const formData = JSON.parse(data[0].formJson)
        this.newOaWorkflowFormEditInfo.formJson = formData
        this.newOaWorkflowFormEditInfo.components = formData.components
        
        // 初始化表单数据
        this.initFormData()
        
        this._listOaWorkflowDetails()
      } catch (error) {
        console.error('获取表单数据失败:', error)
        this.$message.error('获取表单数据失败')
      }
    },

    initFormData() {
      // 初始化表单数据对象
      this.formData = {}
      this.newOaWorkflowFormEditInfo.components.forEach(item => {
        if (item.type !== 'button' && item.type !== 'text') {
          if (item.type === 'checkbox') {
            // 确保复选框的值是数组类型
            this.$set(this.formData, item.key, [])
          } else if (item.type === 'radio' || item.type === 'select') {
            this.$set(this.formData, item.key, item.values && item.values.length > 0 ? item.values[0].value : '')
          } else {
            this.$set(this.formData, item.key, '')
          }
        }
      })
    },

    async _listOaWorkflowDetails() {
      try {
        const params = {
          page: 1,
          row: 1,
          id: this.newOaWorkflowFormEditInfo.id,
          flowId: this.newOaWorkflowFormEditInfo.flowId
        }

        const { data } = await queryOaWorkflowFormData(params)
        const detailData = data[0]

        // 更新表单数据
        this.newOaWorkflowFormEditInfo.components.forEach(item => {
          if (item.type !== 'button' && item.type !== 'text') {
            const value = detailData[item.key]
            if (value !== undefined && value !== null) {
              if (item.type === 'checkbox') {
                // 确保复选框数据是数组类型
                if (Array.isArray(value)) {
                  this.$set(this.formData, item.key, value)
                } else if (typeof value === 'string' && value) {
                  // 如果是字符串，转换为数组
                  this.$set(this.formData, item.key, [value])
                } else {
                  this.$set(this.formData, item.key, [])
                }
              } else {
                this.$set(this.formData, item.key, value)
              }
            }
          }
        })

        // 处理文件数据
        if (detailData.files && detailData.files.length > 0) {
          this.newOaWorkflowFormEditInfo.fileName = detailData.files[0].fileName
          this.newOaWorkflowFormEditInfo.realFileName = detailData.files[0].realFileName
          this.$nextTick(() => {
            if (this.$refs.uploadFile) {
              this.$refs.uploadFile.notifyVedio(detailData.files[0].fileName)
            }
          })
        }
      } catch (error) {
        console.error('获取表单详情失败:', error)
        this.$message.error('获取表单详情失败')
      }
    },

    getValidationRules(item) {
      const rules = []
      if (item.validate && item.validate.required) {
        rules.push({
          required: true,
          message: `${item.label}不能为空`,
          trigger: 'blur'
        })
      }
      return rules
    },

    async _submitFormData() {
      try {
        // 表单验证
        await this.$refs.form.validate()
        
        this.submitLoading = true
        
        const formData = {
          id: this.newOaWorkflowFormEditInfo.id,
          flowId: this.newOaWorkflowFormEditInfo.flowId,
          fileName: this.newOaWorkflowFormEditInfo.fileName,
          realFileName: this.newOaWorkflowFormEditInfo.realFileName,
          
        }

        // 收集表单数据
        this.newOaWorkflowFormEditInfo.components.forEach(item => {
          if (item.type !== 'button' && item.type !== 'text') {
            formData[item.key] = this.formData[item.key]
            if (item.type === 'checkbox') {
              // 复选框提交时取第一个值或空字符串
              formData[item.key] = this.formData[item.key] && this.formData[item.key].length > 0 
                ? this.formData[item.key][0] 
                : ''
            }
          }
        })

        const res = await updateOaWorkflowFormData(formData)
        if (res.code === 0) {
          this.$message.success(this.$t('common.operationSuccess'))
          this.closeEditInfo()
        } else {
          this.$message.error(res.msg || this.$t('newOaWorkflowFormEdit.submitError'))
        }
      } catch (error) {
        if (error.message) {
          // 表单验证错误
          return
        }
        console.error('提交表单失败:', error)
        this.$message.error(this.$t('newOaWorkflowFormEdit.submitError'))
      } finally {
        this.submitLoading = false
      }
    },

    closeEditInfo() {
      this.$router.go(-1)
    },

    handleFileUpload(data) {
      this.newOaWorkflowFormEditInfo.fileName = data.fileName
      this.newOaWorkflowFormEditInfo.realFileName = data.realFileName
    }
  }
}
</script>

<style lang="scss" scoped>
.new-oa-workflow-form-edit-container {
  padding: 20px;

  .box-card {
    margin-bottom: 20px;
  }

  .form-footer {
    margin-top: 20px;
    text-align: right;
  }

  .el-form-item {
    margin-bottom: 22px;
  }

  .file-info {
    margin-bottom: 10px;
  }
}
</style>