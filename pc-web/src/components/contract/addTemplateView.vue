<template>
  <el-dialog :title="$t('contractTypeManage.template.title')" :visible.sync="visible" width="80%" @close="handleClose">
    <div class="attr-buttons">
      <el-button v-for="(item, index) in contractTypeAttrs" :key="index" type="primary" size="small"
        @click="insertAttrs(item)">
        {{ item }}
      </el-button>
    </div>

    <el-form label-width="120px">
      <el-form-item :label="$t('contractTypeManage.template.content')">
        <div class="editor-container">
          <rich-text-editor ref="richTextEditor" v-model="context" />
        </div>
      </el-form-item>
    </el-form>
    <div slot="footer" class="dialog-footer">
      <el-button @click="visible = false">
        {{ $t('common.cancel') }}
      </el-button>
      <el-button type="primary" @click="saveTemplateInfo">
        {{ $t('common.submit') }}
      </el-button>
    </div>
  </el-dialog>
</template>

<script>
import { saveContractTypeTemplate, updateContractTypeTemplate, queryContractTypeTemplate, printContractTemplate } from '@/api/contract/contractTypeManageApi'
import RichTextEditor from '@/components/editor/RichTextEditor'

export default {
  name: 'AddTemplateView',
  components: {
    RichTextEditor
  },
  data() {
    return {
      visible: false,
      contractTypeId: '',
      context: '',
      templateId: '',
      contractTypeAttrs: [],
      contractTypeSpec: []
    }
  },
  methods: {
    open(contractType) {
      this.contractTypeId = contractType.contractTypeId
      this.visible = true
      this._loadContractAttrs(contractType.contractTypeId)

      this._loadTemplate()
    },
    async _loadContractAttrs(contractTypeId) {
      try {
        const params = {
          page: 1,
          row: 1,
          contractTypeId
        }
        const { data } = await printContractTemplate(params)
        this.contractTypeAttrs = []
        this.contractTypeSpec = data.contractTypeSpec
        this.contractTypeSpec.forEach(e => {
          const rname = e.specName
          const reg = `#${rname}#`
          this.contractTypeAttrs.push(reg)
        })
        
        // 处理基本属性
        if (data.baseRepalce) {
          data.baseRepalce.forEach(e => {
            const rname = e.name
            const rkey = e.key
            const contractarr = Object.keys(data.contract[0] || {})
            for (const a in contractarr) {
              if (rkey === contractarr[a]) {
                const reg = `#${rname}#`
                this.contractTypeAttrs.push(reg)
              }
            }
          })
        }
      } catch (error) {
        console.error('加载合同属性失败:', error)
      }
    },
    insertAttrs(attr) {
      if (this.$refs.richTextEditor && this.$refs.richTextEditor.editor) {
        this.$refs.richTextEditor.editor.txt.append(attr)
      }
    },
    async saveTemplateInfo() {
      if (!this.addTemplateValidate()) {
        return
      }

      try {
        // const url = this.templateId ? '/contract/updateContractTypeTemplate' : '/contract/saveContractTypeTemplate'
        const data = {
          contractTypeId: this.contractTypeId,
          context: this.context,
          templateId: this.templateId
        }

        const res = this.templateId
          ? await updateContractTypeTemplate(data)
          : await saveContractTypeTemplate(data)

        if (res.code === 0) {
          this.$message.success(this.$t('common.operationSuccess'))
          this.visible = false
          this.$emit('success')
        } else {
          this.$message.error(res.msg)
        }
      } catch (error) {
        this.$message.error(this.$t('common.operateFailed'))
      }
    },
    addTemplateValidate() {
      if (!this.contractTypeId) {
        this.$message.error(this.$t('contractTypeManage.validate.contractTypeRequired'))
        return false
      }
      if (!this.context) {
        this.$message.error(this.$t('contractTypeManage.validate.contentRequired'))
        return false
      }
      return true
    },
    async _loadTemplate() {
      try {
        const params = {
          page: 1,
          row: 1,
          contractTypeId: this.contractTypeId
        }
        const { data } = await queryContractTypeTemplate(params)
        if (data.length > 0) {
          this.templateId = data[0].templateId
          this.context = data[0].context
          if (this.$refs.richTextEditor) {
            this.$refs.richTextEditor.setContent(this.context)
          }
        } else {
          this.templateId = ''
          this.context = ''
          if (this.$refs.richTextEditor) {
            this.$refs.richTextEditor.clear()
          }
        }
      } catch (error) {
        console.error('加载模板失败:', error)
      }
    },
    handleClose() {
      this.contractTypeId = ''
      this.context = ''
      this.templateId = ''
      if (this.$refs.richTextEditor) {
        this.$refs.richTextEditor.clear()
      }
    }
  }
}
</script>

<style lang="scss" scoped>
.attr-buttons {
  margin-bottom: 20px;

  .el-button {
    margin-right: 10px;
    margin-bottom: 10px;
  }
}

.editor-container {
  ::v-deep .el-textarea__inner {
    min-height: 300px;
  }
}
</style>