<template>
  <el-dialog
    :title="$t('app.dialog.addTitle')"
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
        :label="$t('app.form.name')"
        prop="name"
      >
        <el-input v-model="form.name" />
      </el-form-item>
      <el-form-item
        :label="$t('app.form.securityCode')"
        prop="securityCode"
      >
        <el-input v-model="form.securityCode" />
      </el-form-item>
      <el-form-item
        :label="$t('app.form.whileListIp')"
        prop="whileListIp"
      >
        <el-input v-model="form.whileListIp" />
      </el-form-item>
      <el-form-item
        :label="$t('app.form.blackListIp')"
        prop="blackListIp"
      >
        <el-input v-model="form.blackListIp" />
      </el-form-item>
      <el-form-item
        :label="$t('app.form.remark')"
        prop="remark"
      >
        <el-input v-model="form.remark" />
      </el-form-item>

    </el-form>
    <span slot="footer" class="dialog-footer">
      <el-button @click="handleClose">{{ $t('app.dialog.cancel') }}</el-button>
      <el-button type="primary" @click="handleSubmit">{{ $t('app.dialog.confirm') }}</el-button>
    </span>
  </el-dialog>
</template>

<script>
import { addApp } from '@/api/dev/appApi'

export default {
  name: 'AddApp',
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
        securityCode: '',
        whileListIp: '',
        blackListIp: '',
        remark: '',
        createTime: '',
      },
      rules: {
        name: [
          { required: true, message: this.$t('app.validate.name'), trigger: 'blur' }
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
        await addApp(this.form)
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