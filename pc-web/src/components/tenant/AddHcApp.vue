<template>
  <el-dialog
    :title="$t('hcApp.dialog.addTitle')"
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
        :label="$t('hcApp.form.name')"
        prop="name"
      >
        <el-input v-model="form.name" />
      </el-form-item>
      <el-form-item
        :label="$t('hcApp.form.validHours')"
        prop="validHours"
      >
        <el-select v-model="form.validHours" style="width: 100%" placeholder="请选择有效期">
          <el-option
            label="12小时"
            value="12"
          />
          <el-option
            label="永久"
            value="0"
          />
        </el-select>
      </el-form-item>
      <el-form-item
        :label="$t('hcApp.form.remark')"
        prop="remark"
      >
        <el-input type="textarea" v-model="form.remark" />
      </el-form-item>
    </el-form>
    <span slot="footer" class="dialog-footer">
      <el-button @click="handleClose">{{ $t('hcApp.dialog.cancel') }}</el-button>
      <el-button type="primary" @click="handleSubmit">{{ $t('hcApp.dialog.confirm') }}</el-button>
    </span>
  </el-dialog>
</template>

<script>
import { addHcApp } from '@/api/tenant/hcAppApi'

export default {
  name: 'AddHcApp',
  props: {
    visible: {
      type: Boolean,
      default: false
    }
  },
  data() {
    return {
      dialogVisible: false,
      form: {
        appId: '',
        name: '',
        secure: '',
        appToken: '',
        tenantId: '',
        validHours: '',
        remark: '',
        createTime: '',
      },
      rules: {
        appId: [
          { required: true, message: this.$t('hcApp.validate.appId'), trigger: 'blur' }
        ],
        name: [
          { required: true, message: this.$t('hcApp.validate.name'), trigger: 'blur' }
        ],
        validHours: [
          { required: true, message: this.$t('hcApp.validate.validHours'), trigger: 'blur' }
        ],
        createTime: [
          { required: true, message: this.$t('hcApp.validate.createTime'), trigger: 'blur' }
        ],
      }
    }
  },
  watch: {
    visible: {
      handler(val) {
        this.dialogVisible = val
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
        await addHcApp(this.form)
        this.$message.success(this.$t('common.operationSuccess'))
        this.handleClose()
        this.$emit('success')
      } catch (error) {
        console.error('添加失败:', error)
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