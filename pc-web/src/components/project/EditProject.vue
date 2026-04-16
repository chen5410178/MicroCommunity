<template>
  <el-dialog
    :title="$t('project.dialog.editTitle')"
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
        :label="$t('project.form.projectName')"
        prop="projectName"
      >
        <el-input v-model="form.projectName" />
      </el-form-item>
      <el-form-item
        :label="$t('project.form.personName')"
        prop="personName"
      >
        <el-input v-model="form.personName" />
      </el-form-item>
      <el-form-item
        :label="$t('project.form.personLink')"
        prop="personLink"
      >
        <el-input v-model="form.personLink" />
      </el-form-item>
      <el-form-item
        :label="$t('project.form.remark')"
        prop="remark"
      >
        <el-input
          v-model="form.remark"
          type="textarea"
          :rows="3"
        />
      </el-form-item>
    </el-form>
    <span slot="footer" class="dialog-footer">
      <el-button @click="handleClose">{{ $t('project.dialog.cancel') }}</el-button>
      <el-button type="primary" @click="handleSubmit">{{ $t('project.dialog.confirm') }}</el-button>
    </span>
  </el-dialog>
</template>

<script>
import { updateProject } from '@/api/project/projectApi'

export default {
  name: 'EditProject',
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
        projectId: '',
        projectName: '',
        personName: '',
        personLink: '',
        remark: ''
      },
      rules: {
        projectName: [
          { required: true, message: this.$t('project.validate.projectName'), trigger: 'blur' }
        ],
        personName: [
          { required: true, message: this.$t('project.validate.personName'), trigger: 'blur' }
        ],
        personLink: [
          { required: true, message: this.$t('project.validate.personLink'), trigger: 'blur' }
        ]
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
        await updateProject(this.form)
        this.$message.success(this.$t('common.operationSuccess'))
        this.handleClose()
        this.$emit('success')
      } catch (error) {
        console.error('编辑项目失败:', error)
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