<template>
  <el-dialog
    :title="$t('hcApi.dialog.addTitle')"
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
        :label="$t('hcApi.form.apiCode')"
        prop="apiCode"
      >
        <el-input v-model="form.apiCode" />
      </el-form-item>
      <el-form-item
        :label="$t('hcApi.form.apiName')"
        prop="apiName"
      >
        <el-input v-model="form.apiName" />
      </el-form-item>

      <el-form-item
        :label="$t('hcApi.form.seq')"
        prop="seq"
      >
        <el-input v-model="form.seq" />
      </el-form-item>
      <el-form-item
        :label="$t('hcApi.form.docUrl')"
        prop="docUrl"
      >
        <el-input v-model="form.docUrl" />
      </el-form-item>
      <el-form-item
        :label="$t('hcApi.form.remark')"
        prop="remark"
      >
        <el-input
          v-model="form.remark"
          type="textarea"
          :rows="3"
        />
      </el-form-item>
    </el-form>
    <span slot="footer" class="dialog-footer">
      <el-button @click="handleClose">{{ $t('hcApi.dialog.cancel') }}</el-button>
      <el-button type="primary" @click="handleSubmit">{{ $t('hcApi.dialog.confirm') }}</el-button>
    </span>
  </el-dialog>
</template>

<script>
import { addHcApi } from '@/api/tenant/hcApiApi'

export default {
  name: 'AddHcApi',
  props: {
    visible: {
      type: Boolean,
      default: false
    },
    typeId:{
      type:String
    }
  },
  data() {
    return {
      dialogVisible: false,
      form: {
        apiId: '',
        apiCode: '',
        apiName: '',
        typeId: '',
        seq: '',
        docUrl: '',
        remark: '',
      },
      rules: {
        apiCode: [
          { required: true, message: this.$t('hcApi.validate.apiCode'), trigger: 'blur' }
        ],
        apiName: [
          { required: true, message: this.$t('hcApi.validate.apiName'), trigger: 'blur' }
        ],
        typeId: [
          { required: true, message: this.$t('hcApi.validate.typeId'), trigger: 'blur' }
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
        this.form.typeId = this.typeId;
        await this.$refs.form.validate()
        await addHcApi(this.form)
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