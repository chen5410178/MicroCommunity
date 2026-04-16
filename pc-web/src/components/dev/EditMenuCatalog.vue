<template>
  <el-dialog :title="$t('menuCatalog.dialog.editTitle')" :visible.sync="dialogVisible" width="500px" @close="handleClose">
    <el-form ref="form" :model="form" :rules="rules" label-width="100px">
      <el-form-item :label="$t('menuCatalog.form.name')" prop="name">
        <el-input v-model="form.name" />
      </el-form-item>
      <el-form-item :label="$t('menuCatalog.form.icon')" prop="icon">
        <el-input v-model="form.icon" />
      </el-form-item>
      <el-form-item :label="$t('menuCatalog.form.seq')" prop="seq">
        <el-input v-model="form.seq" />
      </el-form-item>
      <el-form-item :label="$t('menuCatalog.form.storeType')" prop="storeType">
        <el-select v-model="form.storeType" style="width: 100%" placeholder="请选择">
          <el-option label="运营团队" value="800900000001" />
          <el-option label="物业" value="800900000003" />
          <el-option label="开发团队" value="800900000000" />
        </el-select>
      </el-form-item>
      <el-form-item :label="$t('menuCatalog.form.url')" prop="url">
        <el-input v-model="form.url" />
      </el-form-item>
      <el-form-item :label="$t('menuCatalog.form.isShow')" prop="isShow">
        <el-select v-model="form.isShow" style="width: 100%" placeholder="请选择">
          <el-option label="是" value="Y" />
          <el-option label="否" value="N" />
        </el-select>
      </el-form-item>
    </el-form>
    <span slot="footer" class="dialog-footer">
      <el-button @click="handleClose">{{ $t('menuCatalog.dialog.cancel') }}</el-button>
      <el-button type="primary" @click="handleSubmit">{{ $t('menuCatalog.dialog.confirm') }}</el-button>
    </span>
  </el-dialog>
</template>

<script>
import { updateMenuCatalog } from '@/api/dev/menuCatalogApi'

export default {
  name: 'EditMenuCatalog',
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
        caId: '',
        name: '',
        icon: '',
        seq: '',
        storeType: '',
        url: '',
        isShow: '',
        createTime: '',
      },
      rules: {
        caId: [
          { required: true, message: this.$t('menuCatalog.validate.caId'), trigger: 'blur' }
        ],
        name: [
          { required: true, message: this.$t('menuCatalog.validate.name'), trigger: 'blur' }
        ],
        icon: [
          { required: true, message: this.$t('menuCatalog.validate.icon'), trigger: 'blur' }
        ],
        seq: [
          { required: true, message: this.$t('menuCatalog.validate.seq'), trigger: 'blur' }
        ],
        url: [
          { required: true, message: this.$t('menuCatalog.validate.url'), trigger: 'blur' }
        ],
        isShow: [
          { required: true, message: this.$t('menuCatalog.validate.isShow'), trigger: 'blur' }
        ],
        createTime: [
          { required: true, message: this.$t('menuCatalog.validate.createTime'), trigger: 'blur' }
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
        await updateMenuCatalog(this.form)
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