<template>
  <el-dialog
    :title="$t('mapping.dialog.addTitle')"
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
        :label="$t('mapping.form.name')"
        prop="name"
      >
        <el-input v-model="form.name" />
      </el-form-item>
      <el-form-item
        :label="$t('mapping.form.mappingKey')"
        prop="key"
      >
        <el-input v-model="form.key" />
      </el-form-item>
      <el-form-item
        :label="$t('mapping.form.value')"
        prop="value"
      >
        <el-input v-model="form.value" />
      </el-form-item>
      <el-form-item
        :label="$t('mapping.form.remark')"
        prop="remark"
      >
        <el-input type="textarea" v-model="form.remark" />
      </el-form-item>
    </el-form>
    <span slot="footer" class="dialog-footer">
      <el-button @click="handleClose">{{ $t('mapping.dialog.cancel') }}</el-button>
      <el-button type="primary" @click="handleSubmit">{{ $t('mapping.dialog.confirm') }}</el-button>
    </span>
  </el-dialog>
</template>

<script>
import { addMapping } from '@/api/dev/mappingApi'

export default {
  name: 'AddMapping',
  props: {
    visible: {
      type: Boolean,
      default: false
    },
    domain: {
      type: String,
      default: ""
    }
  },
  data() {
    return {
      dialogVisible: false,
      form: {
        domain: '',
        name: '',
        key: '',
        value: '',
        remark: '',
        createTime: '',
      },
      rules: {
        name: [
          { required: true, message: this.$t('mapping.validate.name'), trigger: 'blur' }
        ],
        key: [
          { required: true, message: this.$t('mapping.validate.key'), trigger: 'blur' }
        ],
        value: [
          { required: true, message: this.$t('mapping.validate.value'), trigger: 'blur' }
        ]
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
        this.form.domain = this.domain
        await addMapping(this.form)
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