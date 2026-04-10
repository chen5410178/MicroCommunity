<template>
  <el-dialog
    :title="$t('feePrintPageManage.edit.title')"
    :visible.sync="visible"
    width="50%"
    @close="handleClose"
  >
    <el-form
      ref="form"
      :model="formData"
      :rules="rules"
      label-width="120px"
    >
      <el-form-item
        :label="$t('feePrintPageManage.edit.pageName')"
        prop="pageName"
      >
        <el-input
          v-model="formData.pageName"
          :placeholder="$t('feePrintPageManage.edit.pageNamePlaceholder')"
        />
      </el-form-item>
      <el-form-item
        :label="$t('feePrintPageManage.edit.template')"
        prop="pageUrl"
      >
        <el-select
          v-model="formData.pageUrl"
          :placeholder="$t('feePrintPageManage.edit.templatePlaceholder')"
          style="width:100%"
        >
          <el-option
            v-for="item in templates"
            :key="item.templateId"
            :label="item.name"
            :value="item.templateId"
          />
        </el-select>
      </el-form-item>
    </el-form>
    <span slot="footer" class="dialog-footer">
      <el-button @click="visible = false">
        {{ $t('common.cancel') }}
      </el-button>
      <el-button
        type="primary"
        :loading="loading"
        @click="handleSubmit"
      >
        {{ $t('common.confirm') }}
      </el-button>
    </span>
  </el-dialog>
</template>

<script>
import { getCommunityId } from '@/api/community/communityApi'
import { updateFeePrintPage, listFeePrintPageTemplate } from '@/api/system/feePrintPageManageApi'

export default {
  name: 'EditFeePrintPage',
  data() {
    return {
      visible: false,
      loading: false,
      communityId: '',
      formData: {
        pageId: '',
        pageName: '',
        pageUrl: '',
        communityId: ''
      },
      templates: [],
      rules: {
        pageName: [
          { required: true, message: this.$t('feePrintPageManage.validate.pageNameRequired'), trigger: 'blur' },
          { max: 128, message: this.$t('feePrintPageManage.validate.pageNameMaxLength'), trigger: 'blur' }
        ],
        pageUrl: [
          { required: true, message: this.$t('feePrintPageManage.validate.templateRequired'), trigger: 'change' }
        ],
        pageId: [
          { required: true, message: this.$t('feePrintPageManage.validate.pageIdRequired'), trigger: 'blur' }
        ]
      }
    }
  },
  methods: {
    open(row) {
      this.communityId = getCommunityId()
      this.formData = {
        pageId: row.pageId,
        pageName: row.pageName,
        pageUrl: row.pageUrl,
        communityId: this.communityId
      }
      this.getTemplates()
      this.visible = true
    },
    async getTemplates() {
      try {
        const params = {
          page: 1,
          row: 50,
          communityId: this.communityId
        }
        const { data } = await listFeePrintPageTemplate(params)
        this.templates = data
      } catch (error) {
        console.error('获取模板列表失败:', error)
      }
    },
    handleSubmit() {
      this.$refs.form.validate(async valid => {
        if (valid) {
          try {
            this.loading = true
            await updateFeePrintPage(this.formData)
            this.$message.success(this.$t('common.operationSuccess'))
            this.$emit('success')
            this.visible = false
          } catch (error) {
            console.error('更新失败:', error)
          } finally {
            this.loading = false
          }
        }
      })
    },
    handleClose() {
      this.$refs.form.resetFields()
      this.formData = {
        pageId: '',
        pageName: '',
        pageUrl: '',
        communityId: this.communityId
      }
    }
  }
}
</script>