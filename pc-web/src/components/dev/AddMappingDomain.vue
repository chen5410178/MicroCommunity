<template>
  <el-dialog
    :title="$t('mappingDomain.dialog.addTitle')"
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
        :label="$t('mappingDomain.form.domain')"
        prop="domain"
      >
        <el-input v-model="form.domain" />
      </el-form-item>
      <el-form-item
        :label="$t('mappingDomain.form.domainName')"
        prop="domainName"
      >
        <el-input v-model="form.domainName" />
      </el-form-item>
      <el-form-item
        :label="$t('mappingDomain.form.seq')"
        prop="seq"
      >
        <el-input v-model="form.seq" />
      </el-form-item>
      <el-form-item
        :label="$t('mappingDomain.form.remark')"
        prop="remark"
      >
        <el-input v-model="form.remark" />
      </el-form-item>
    </el-form>
    <span slot="footer" class="dialog-footer">
      <el-button @click="handleClose">{{ $t('mappingDomain.dialog.cancel') }}</el-button>
      <el-button type="primary" @click="handleSubmit">{{ $t('mappingDomain.dialog.confirm') }}</el-button>
    </span>
  </el-dialog>
</template>

<script>
import { addMappingDomain } from '@/api/dev/mappingDomainApi'

export default {
  name: 'AddMappingDomain',
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
        domain: '',
        domainName: '',
        seq: '',
        remark: '',
        createTime: '',
      },
      rules: {
        domain: [
          { required: true, message: this.$t('mappingDomain.validate.domain'), trigger: 'blur' }
        ],
        domainName: [
          { required: true, message: this.$t('mappingDomain.validate.domainName'), trigger: 'blur' }
        ],
        seq: [
          { required: true, message: this.$t('mappingDomain.validate.seq'), trigger: 'blur' }
        ],
        createTime: [
          { required: true, message: this.$t('mappingDomain.validate.createTime'), trigger: 'blur' }
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
        await addMappingDomain(this.form)
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