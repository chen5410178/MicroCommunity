<template>
  <el-dialog
    :title="$t('layout.machineEdit')"
    :visible.sync="visible"
    width="500px"
    @close="handleClose"
  >
    <el-form ref="form" :model="form" :rules="rules" label-width="100px">
      <el-form-item :label="$t('layout.machineCode')" prop="machineCode">
        <el-input v-model="form.machineCode"  />
      </el-form-item>
      <el-form-item :label="$t('layout.machineName')" prop="machineName">
        <el-input v-model="form.machineName" />
      </el-form-item>
      <el-form-item :label="$t('layout.machineIp')" prop="machineIp">
        <el-input v-model="form.machineIp" />
      </el-form-item>
      <el-form-item :label="$t('layout.machineMac')" prop="machineMac">
        <el-input v-model="form.machineMac" />
      </el-form-item>
      <el-form-item :label="$t('layout.implBean')" prop="implBean">
        <el-select v-model="form.implBean" style="width: 100%">
          <el-option label="厂家A" value="A" />
          <el-option label="厂家B" value="B" />
          <el-option label="厂家C" value="C" />
        </el-select>
      </el-form-item>
      <el-form-item :label="$t('layout.status')" prop="statusCd">
        <el-radio-group v-model="form.statusCd">
          <el-radio label="0">{{ $t('layout.inUse') }}</el-radio>
          <el-radio label="1">{{ $t('layout.invalid') }}</el-radio>
        </el-radio-group>
      </el-form-item>
      <el-form-item :label="$t('layout.remark')" prop="remark">
        <el-input type="textarea" v-model="form.remark" />
      </el-form-item>
    </el-form>
    <div slot="footer" class="dialog-footer">
      <el-button @click="handleClose">{{ $t('layout.cancel') }}</el-button>
      <el-button type="primary" @click="handleSubmit">{{ $t('layout.confirm') }}</el-button>
    </div>
  </el-dialog>
</template>

<script>
import { updateMachine } from '@/api/machine/machineApi'

export default {
  name: 'EditMachine',
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
      form: {
        machineCode: '',
        machineName: '',
        machineIp: '',
        machineMac: '',
        implBean: '',
        statusCd: '0',
        remark: ''
      },
      rules: {
        machineName: [
          { required: true, message: this.$t('layout.required'), trigger: 'blur' }
        ],
        machineIp: [
          { required: true, message: this.$t('layout.required'), trigger: 'blur' }
        ],
        implBean: [
          { required: true, message: this.$t('layout.required'), trigger: 'change' }
        ]
      }
    }
  },
  watch: {
    formData: {
      handler(val) {
        if (val) {
          this.form = { ...val }
        }
      },
      immediate: true
    }
  },
  methods: {
    handleClose() {
      this.$emit('update:visible', false)
      this.$refs.form.resetFields()
    },
    async handleSubmit() {
      try {
        await this.$refs.form.validate()
        await updateMachine(this.form)
        this.$message.success(this.$t('common.operationSuccess'))
        this.$emit('success')
        this.handleClose()
      } catch (error) {
        if (error !== 'cancel') {
          this.$message.error(this.$t('layout.submitError'))
        }
      }
    }
  }
}
</script> 