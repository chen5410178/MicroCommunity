<template>
  <el-dialog :title="$t('attrValue.dialog.editTitle')" :visible.sync="dialogVisible" width="500px" @close="handleClose">
    <el-form ref="form" :model="form" :rules="rules" label-width="100px">
      <el-form-item :label="$t('attrValue.form.value')" prop="value">
        <el-input v-model="form.value" />
      </el-form-item>
      <el-form-item :label="$t('attrValue.form.valueName')" prop="valueName">
        <el-input v-model="form.valueName" />
      </el-form-item>
      <el-form-item :label="$t('attrValue.form.valueShow')" prop="valueShow">
        <el-select v-model="form.valueShow" :placeholder="$t('attrValue.form.valueShow')" style="width: 100%">
          <el-option label="YES" value="Y" />
          <el-option label="NO" value="N" />
        </el-select>
      </el-form-item>
    </el-form>
    <span slot="footer" class="dialog-footer">
      <el-button @click="handleClose">{{ $t('attrValue.dialog.cancel') }}</el-button>
      <el-button type="primary" @click="handleSubmit">{{ $t('attrValue.dialog.confirm') }}</el-button>
    </span>
  </el-dialog>
</template>

<script>
import { updateAttrValue } from '@/api/dev/attrValueApi'

export default {
  name: 'EditAttrValue',
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
        valueId: '',
        specId: '',
        value: '',
        valueName: '',
        valueShow: '',
        createTime: '',
      },
      rules: {
        valueId: [
          { required: true, message: this.$t('attrValue.validate.valueId'), trigger: 'blur' }
        ],
        specId: [
          { required: true, message: this.$t('attrValue.validate.specId'), trigger: 'blur' }
        ],
        value: [
          { required: true, message: this.$t('attrValue.validate.value'), trigger: 'blur' }
        ],
        valueName: [
          { required: true, message: this.$t('attrValue.validate.valueName'), trigger: 'blur' }
        ],
        valueShow: [
          { required: true, message: this.$t('attrValue.validate.valueShow'), trigger: 'blur' }
        ],
        createTime: [
          { required: true, message: this.$t('attrValue.validate.createTime'), trigger: 'blur' }
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
        await updateAttrValue(this.form)
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