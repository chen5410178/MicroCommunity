<template>
  <el-dialog :title="$t('attrSpec.dialog.addTitle')" :visible.sync="dialogVisible" width="500px" @close="handleClose">
    <el-form ref="form" :model="form" :rules="rules" label-width="100px">
      <el-form-item :label="$t('attrSpec.form.domain')" prop="domain">
        <el-input v-model="form.domain" />
      </el-form-item>
      <el-form-item :label="$t('attrSpec.form.specCd')" prop="specCd">
        <el-input v-model="form.specCd" />
      </el-form-item>
      <el-form-item :label="$t('attrSpec.form.specName')" prop="specName">
        <el-input v-model="form.specName" />
      </el-form-item>
      <el-form-item :label="$t('attrSpec.form.specHoldplace')" prop="specHoldplace">
        <el-input v-model="form.specHoldplace" />
      </el-form-item>
      <el-form-item :label="$t('attrSpec.form.required')" prop="required">
        <el-select v-model="form.required" :placeholder="$t('attrSpec.form.required')" style="width: 100%">
          <el-option label="YES" value="Y" />
          <el-option label="NO" value="N" />
        </el-select>
      </el-form-item>
      <el-form-item :label="$t('attrSpec.form.specShow')" prop="specShow">
        <el-select v-model="form.specShow" :placeholder="$t('attrSpec.form.specShow')" style="width: 100%">
          <el-option label="YES" value="Y" />
          <el-option label="NO" value="N" />
        </el-select>
      </el-form-item>
      <el-form-item :label="$t('attrSpec.form.specValueType')" prop="specValueType">
        <el-select v-model="form.specValueType" :placeholder="$t('attrSpec.form.specValueType')" style="width: 100%">
          <el-option label="string" value="1001" />
          <el-option label="int" value="2002" />
          <el-option label="money" value="3003" />
        </el-select>
      </el-form-item>
      <el-form-item :label="$t('attrSpec.form.specType')" prop="specType">
        <el-select v-model="form.specType" :placeholder="$t('attrSpec.form.specType')" style="width: 100%">
          <el-option label="input" value="2233" />
          <el-option label="select" value="3344" />
        </el-select>
      </el-form-item>
      <el-form-item :label="$t('attrSpec.form.listShow')" prop="listShow">
        <el-select v-model="form.listShow" :placeholder="$t('attrSpec.form.listShow')" style="width: 100%">
          <el-option label="YES" value="Y" />
          <el-option label="NO" value="N" />
        </el-select>
      </el-form-item>
    </el-form>
    <span slot="footer" class="dialog-footer">
      <el-button @click="handleClose">{{ $t('attrSpec.dialog.cancel') }}</el-button>
      <el-button type="primary" @click="handleSubmit">{{ $t('attrSpec.dialog.confirm') }}</el-button>
    </span>
  </el-dialog>
</template>

<script>
import { addAttrSpec } from '@/api/dev/attrSpecApi'

export default {
  name: 'AddAttrSpec',
  props: {
    visible: {
      type: Boolean,
      default: false
    },
    tableName: {
      type: String,
      default: ""
    }
  },
  data() {
    return {
      dialogVisible: false,
      form: {
        domain: 'COMMON',
        specCd: '',
        tableName: '',
        specName: '',
        specHoldplace: '',
        required: '',
        specShow: '',
        specValueType: '',
        specType: '',
        listShow: '',
        createTime: '',
      },
      rules: {
        domain: [
          { required: true, message: this.$t('attrSpec.validate.domain'), trigger: 'blur' }
        ],
        specCd: [
          { required: true, message: this.$t('attrSpec.validate.specCd'), trigger: 'blur' }
        ],
        tableName: [
          { required: true, message: this.$t('attrSpec.validate.tableName'), trigger: 'blur' }
        ],
        specName: [
          { required: true, message: this.$t('attrSpec.validate.specName'), trigger: 'blur' }
        ],
        specHoldplace: [
          { required: true, message: this.$t('attrSpec.validate.specHoldplace'), trigger: 'blur' }
        ],
        required: [
          { required: true, message: this.$t('attrSpec.validate.required'), trigger: 'blur' }
        ],
        specShow: [
          { required: true, message: this.$t('attrSpec.validate.specShow'), trigger: 'blur' }
        ],
        specValueType: [
          { required: true, message: this.$t('attrSpec.validate.specValueType'), trigger: 'blur' }
        ],
        specType: [
          { required: true, message: this.$t('attrSpec.validate.specType'), trigger: 'blur' }
        ],
        listShow: [
          { required: true, message: this.$t('attrSpec.validate.listShow'), trigger: 'blur' }
        ],
        createTime: [
          { required: true, message: this.$t('attrSpec.validate.createTime'), trigger: 'blur' }
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
        this.form.tableName = this.tableName
        await addAttrSpec(this.form)
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