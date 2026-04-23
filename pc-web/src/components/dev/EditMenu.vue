<template>
  <el-dialog :title="$t('menuPage.dialog.editTitle')" :visible.sync="dialogVisible" width="500px" @close="handleClose">
    <el-form ref="form" :model="form" :rules="rules" label-width="100px">
      <el-form-item :label="$t('menuPage.form.name')" prop="name">
        <el-input v-model="form.name" />
      </el-form-item>
      <el-form-item :label="$t('menuPage.form.url')" prop="url">
        <el-input v-model="form.url" />
      </el-form-item>
      <el-form-item :label="$t('menuPage.form.seq')" prop="seq">
        <el-input v-model="form.seq" />
      </el-form-item>
      <el-form-item :label="$t('menuPage.form.description')" prop="description">
        <el-input v-model="form.description" />
      </el-form-item>
      <el-form-item :label="$t('menuPage.form.isShow')" prop="isShow">
        <el-select v-model="form.isShow" placeholder="请选择" style="width: 100%">
          <el-option label="显示" value="Y" />
            <el-option label="不显示" value="N" />
        </el-select>
      </el-form-item>
    </el-form>
    <span slot="footer" class="dialog-footer">
      <el-button @click="handleClose">{{ $t('menuPage.dialog.cancel') }}</el-button>
      <el-button type="primary" @click="handleSubmit">{{ $t('menuPage.dialog.confirm') }}</el-button>
    </span>
  </el-dialog>
</template>

<script>
import { updateMenu } from '@/api/dev/menuApi'

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
        gId: '',
        url: '',
        seq: '',
        description: '',
        isShow: '',
        createTime: '',
      },
      rules: {
        mId: [
          { required: true, message: this.$t('menuPage.validate.mId'), trigger: 'blur' }
        ],
        name: [
          { required: true, message: this.$t('menuPage.validate.name'), trigger: 'blur' }
        ],
        gId: [
          { required: true, message: this.$t('menuPage.validate.gId'), trigger: 'blur' }
        ],
        url: [
          { required: true, message: this.$t('menuPage.validate.url'), trigger: 'blur' }
        ],
        seq: [
          { required: true, message: this.$t('menuPage.validate.seq'), trigger: 'blur' }
        ],
        isShow: [
          { required: true, message: this.$t('menuPage.validate.isShow'), trigger: 'blur' }
        ],
        createTime: [
          { required: true, message: this.$t('menuPage.validate.createTime'), trigger: 'blur' }
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