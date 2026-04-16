<template>
  <el-dialog
    :title="$t('menu.dialog.editTitle')"
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
        :label="$t('menu.form.name')"
        prop="name"
      >
        <el-input v-model="form.name" />
      </el-form-item>
      <el-form-item
        :label="$t('menu.form.mgId')"
        prop="mgId"
      >
        <el-input v-model="form.mgId" />
      </el-form-item>
      <el-form-item
        :label="$t('menu.form.url')"
        prop="url"
      >
        <el-input v-model="form.url" />
      </el-form-item>
      <el-form-item
        :label="$t('menu.form.seq')"
        prop="seq"
      >
        <el-input v-model="form.seq" />
      </el-form-item>
      <el-form-item
        :label="$t('menu.form.priv')"
        prop="priv"
      >
       <el-select v-model="form.priv" style="width: 100%" class="text-left" placeholder="请选择权限">
        <el-option label="管理员" value="1"></el-option>
        <el-option label="管理员+租户" value="2"></el-option>
       </el-select>
      </el-form-item>
    </el-form>
    <span slot="footer" class="dialog-footer">
      <el-button @click="handleClose">{{ $t('menu.dialog.cancel') }}</el-button>
      <el-button type="primary" @click="handleSubmit">{{ $t('menu.dialog.confirm') }}</el-button>
    </span>
  </el-dialog>
</template>

<script>
import { updateMenu } from '@/api/user/menuApi'

export default {
  name: 'EditMenu',
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
        mId: '',
        name: '',
        mgId: '',
        url: '',
        seq: '',
        createTime: '',
        priv: '',
      },
      rules: {
        mId: [
          { required: true, message: this.$t('menu.validate.mId'), trigger: 'blur' }
        ],
        name: [
          { required: true, message: this.$t('menu.validate.name'), trigger: 'blur' }
        ],
        mgId: [
          { required: true, message: this.$t('menu.validate.mgId'), trigger: 'blur' }
        ],
        url: [
          { required: true, message: this.$t('menu.validate.url'), trigger: 'blur' }
        ],
        seq: [
          { required: true, message: this.$t('menu.validate.seq'), trigger: 'blur' }
        ],
        createTime: [
          { required: true, message: this.$t('menu.validate.createTime'), trigger: 'blur' }
        ],
        priv: [
          { required: true, message: this.$t('menu.validate.priv'), trigger: 'blur' }
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
        await updateMenu(this.form)
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