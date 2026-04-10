<template>
  <el-dialog
    :title="$t('businessDatabus.dialog.editTitle')"
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
        :label="$t('businessDatabus.form.databusName')"
        prop="databusName"
      >
        <el-input v-model="form.databusName" />
      </el-form-item>
      <el-form-item
        :label="$t('businessDatabus.form.businessTypeCd')"
        prop="businessTypeCd"
      >
        <el-input v-model="form.businessTypeCd" />
      </el-form-item>
      <el-form-item
        :label="$t('businessDatabus.form.beanName')"
        prop="beanName"
      >
        <el-input v-model="form.beanName" />
      </el-form-item>
      <el-form-item
        :label="$t('businessDatabus.form.seq')"
        prop="seq"
      >
        <el-input v-model="form.seq" />
      </el-form-item>
      <el-form-item
        :label="$t('businessDatabus.form.state')"
        prop="state"
      >
      <el-select v-model="form.state" placeholder="请选择" style="width: 100%">
          <el-option label="在用" value="1001" />
          <el-option label="停用" value="2002" />
        </el-select>
      </el-form-item>
    </el-form>
    <span slot="footer" class="dialog-footer">
      <el-button @click="handleClose">{{ $t('businessDatabus.dialog.cancel') }}</el-button>
      <el-button type="primary" @click="handleSubmit">{{ $t('businessDatabus.dialog.confirm') }}</el-button>
    </span>
  </el-dialog>
</template>

<script>
import { updateBusinessDatabus } from '@/api/dev/businessDatabusApi'

export default {
  name: 'EditBusinessDatabus',
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
        databusId: '',
        databusName: '',
        businessTypeCd: '',
        beanName: '',
        seq: '',
        state: '',
        createTime: '',
      },
      rules: {
        databusId: [
          { required: true, message: this.$t('businessDatabus.validate.databusId'), trigger: 'blur' }
        ],
        databusName: [
          { required: true, message: this.$t('businessDatabus.validate.databusName'), trigger: 'blur' }
        ],
        businessTypeCd: [
          { required: true, message: this.$t('businessDatabus.validate.businessTypeCd'), trigger: 'blur' }
        ],
        beanName: [
          { required: true, message: this.$t('businessDatabus.validate.beanName'), trigger: 'blur' }
        ],
        seq: [
          { required: true, message: this.$t('businessDatabus.validate.seq'), trigger: 'blur' }
        ],
        state: [
          { required: true, message: this.$t('businessDatabus.validate.state'), trigger: 'blur' }
        ],
        createTime: [
          { required: true, message: this.$t('businessDatabus.validate.createTime'), trigger: 'blur' }
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
        await updateBusinessDatabus(this.form)
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