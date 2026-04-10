<template>
  <el-dialog :title="$t('dict.dialog.addTitle')" :visible.sync="dialogVisible" width="500px" @close="handleClose">
    <el-form ref="form" :model="form" :rules="rules" label-width="100px">
      <el-form-item :label="$t('dict.form.name')" prop="name">
        <el-input v-model="form.name" />
      </el-form-item>
      <el-form-item :label="$t('dict.form.statusCd')" prop="statusCd">
        <el-input v-model="form.statusCd" />
      </el-form-item>
      <el-form-item :label="$t('dict.form.description')" prop="description">
        <el-input type="textarea" v-model="form.description" />
      </el-form-item>

    </el-form>
    <span slot="footer" class="dialog-footer">
      <el-button @click="handleClose">{{ $t('dict.dialog.cancel') }}</el-button>
      <el-button type="primary" @click="handleSubmit">{{ $t('dict.dialog.confirm') }}</el-button>
    </span>
  </el-dialog>
</template>

<script>
import { addDict } from '@/api/dev/dictApi'

export default {
  name: 'AddDict',
  props: {
    visible: {
      type: Boolean,
      default: false
    },
    specId: {
      type: String,
      default: ""
    }
  },
  data() {
    return {
      dialogVisible: false,
      form: {
        name: '',
        description: '',
        createTime: '',
        specId:'',
        statusCd:''
      },
      rules: {
        name: [
          { required: true, message: this.$t('dict.validate.name'), trigger: 'blur' }
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
        this.form.specId = this.specId
        await addDict(this.form)
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