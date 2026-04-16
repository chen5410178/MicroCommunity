<template>
  <el-dialog
    :title="$t('privilege.dialog.editTitle')"
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
        :label="$t('privilege.form.name')"
        prop="name"
      >
        <el-input v-model="form.name" />
      </el-form-item>

      <el-form-item
        :label="$t('privilege.form.resource')"
        prop="resource"
      >
        <el-input v-model="form.resource" />
      </el-form-item>
    
      <el-form-item
        :label="$t('privilege.form.description')"
        prop="description"
      >
        <el-input v-model="form.description" />
      </el-form-item>
    </el-form>
    <span slot="footer" class="dialog-footer">
      <el-button @click="handleClose">{{ $t('privilege.dialog.cancel') }}</el-button>
      <el-button type="primary" @click="handleSubmit">{{ $t('privilege.dialog.confirm') }}</el-button>
    </span>
  </el-dialog>
</template>

<script>
import { updatePrivilege } from '@/api/dev/privilegeApi'

export default {
  name: 'EditPrivilege',
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
        pId: '',
        name: '',
        domain: '',
        resource: '',
        mId: '',
        description: '',
        createTime: '',
      },
      rules: {
        pId: [
          { required: true, message: this.$t('privilege.validate.pId'), trigger: 'blur' }
        ],
        name: [
          { required: true, message: this.$t('privilege.validate.name'), trigger: 'blur' }
        ],
        domain: [
          { required: true, message: this.$t('privilege.validate.domain'), trigger: 'blur' }
        ],
        mId: [
          { required: true, message: this.$t('privilege.validate.mId'), trigger: 'blur' }
        ],
        createTime: [
          { required: true, message: this.$t('privilege.validate.createTime'), trigger: 'blur' }
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
        await updatePrivilege(this.form)
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