<template>
  <el-dialog
    :title="$t('businessTableHis.dialog.editTitle')"
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
    <el-form-item :label="$t('businessTableHis.form.action')" prop="action">
        <el-select v-model="form.action" style="width: 100%" placeholder="请选择">
          <el-option label="ADD" value="ADD" />
          <el-option label="UPDATE" value="MOD" />
          <el-option label="DELETE" value="DEL" />
        </el-select>
      </el-form-item>
      <el-form-item
        :label="$t('businessTableHis.form.actionObj')"
        prop="actionObj"
      >
        <el-input v-model="form.actionObj" />
      </el-form-item>
      <el-form-item
        :label="$t('businessTableHis.form.actionObjHis')"
        prop="actionObjHis"
      >
        <el-input v-model="form.actionObjHis" />
      </el-form-item>
      <el-form-item
        :label="$t('businessTableHis.form.businessTypeCd')"
        prop="businessTypeCd"
      >
        <el-input v-model="form.businessTypeCd" />
      </el-form-item>
      <el-form-item
        :label="$t('businessTableHis.form.remark')"
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
      <el-button @click="handleClose">{{ $t('businessTableHis.dialog.cancel') }}</el-button>
      <el-button type="primary" @click="handleSubmit">{{ $t('businessTableHis.dialog.confirm') }}</el-button>
    </span>
  </el-dialog>
</template>

<script>
import { updateBusinessTableHis } from '@/api/dev/businessTableHisApi'

export default {
  name: 'EditBusinessTableHis',
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
        hisId: '',
        action: '',
        actionObj: '',
        actionObjHis: '',
        businessTypeCd: '',
        remark: '',
        createTime: '',
      },
      rules: {
        hisId: [
          { required: true, message: this.$t('businessTableHis.validate.hisId'), trigger: 'blur' }
        ],
        actionObj: [
          { required: true, message: this.$t('businessTableHis.validate.actionObj'), trigger: 'blur' }
        ],
        actionObjHis: [
          { required: true, message: this.$t('businessTableHis.validate.actionObjHis'), trigger: 'blur' }
        ],
        businessTypeCd: [
          { required: true, message: this.$t('businessTableHis.validate.businessTypeCd'), trigger: 'blur' }
        ],
        createTime: [
          { required: true, message: this.$t('businessTableHis.validate.createTime'), trigger: 'blur' }
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
        await updateBusinessTableHis(this.form)
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