<template>
  <el-dialog
    :title="$t('menuGroup.dialog.editTitle')"
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
        :label="$t('menuGroup.form.name')"
        prop="name"
      >
        <el-input v-model="form.name" />
      </el-form-item>
      <el-form-item
        :label="$t('menuGroup.form.icon')"
        prop="icon"
      >
        <el-input v-model="form.icon" />
      </el-form-item>
      <el-form-item
        :label="$t('menuGroup.form.seq')"
        prop="seq"
      >
        <el-input v-model="form.seq" />
      </el-form-item>
      <el-form-item
        :label="$t('menuGroup.form.description')"
        prop="description"
      >
        <el-input v-model="form.description" />
      </el-form-item>

    </el-form>
    <span slot="footer" class="dialog-footer">
      <el-button @click="handleClose">{{ $t('menuGroup.dialog.cancel') }}</el-button>
      <el-button type="primary" @click="handleSubmit">{{ $t('menuGroup.dialog.confirm') }}</el-button>
    </span>
  </el-dialog>
</template>

<script>
import { updateMenuGroup } from '@/api/user/menuGroupApi'

export default {
  name: 'EditMenuGroup',
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
        mgId: '',
        name: '',
        icon: '',
        seq: '',
        description: '',
        createTime: '',
      },
      rules: {
        mgId: [
          { required: true, message: this.$t('menuGroup.validate.mgId'), trigger: 'blur' }
        ],
        name: [
          { required: true, message: this.$t('menuGroup.validate.name'), trigger: 'blur' }
        ],
        icon: [
          { required: true, message: this.$t('menuGroup.validate.icon'), trigger: 'blur' }
        ],
        seq: [
          { required: true, message: this.$t('menuGroup.validate.seq'), trigger: 'blur' }
        ],
        createTime: [
          { required: true, message: this.$t('menuGroup.validate.createTime'), trigger: 'blur' }
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
        await updateMenuGroup(this.form)
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