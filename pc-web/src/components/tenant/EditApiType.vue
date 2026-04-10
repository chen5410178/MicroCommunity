<template>
  <el-dialog
    :title="$t('apiType.dialog.editTitle')"
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
        :label="$t('apiType.form.typeName')"
        prop="typeName"
      >
        <el-input v-model="form.typeName" />
      </el-form-item>

    </el-form>
    <span slot="footer" class="dialog-footer">
      <el-button @click="handleClose">{{ $t('apiType.dialog.cancel') }}</el-button>
      <el-button type="primary" @click="handleSubmit">{{ $t('apiType.dialog.confirm') }}</el-button>
    </span>
  </el-dialog>
</template>

<script>
import { updateApiType } from '@/api/tenant/apiTypeApi'

export default {
  name: 'EditApiType',
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
        typeId: '',
        typeName: '',
        createTime: '',
      },
      rules: {
        typeId: [
          { required: true, message: this.$t('apiType.validate.typeId'), trigger: 'blur' }
        ],
        typeName: [
          { required: true, message: this.$t('apiType.validate.typeName'), trigger: 'blur' }
        ],
        createTime: [
          { required: true, message: this.$t('apiType.validate.createTime'), trigger: 'blur' }
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
        await updateApiType(this.form)
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