<template>
  <el-dialog :title="$t('common.edit')" :visible.sync="visible" width="50%" @close="handleClose">
    <el-form :model="form" :rules="rules" ref="form" label-width="120px">
      <el-form-item :label="$t('componentConditionManage.name')" prop="name">
        <el-input v-model="form.name" />
      </el-form-item>
      <el-form-item :label="$t('componentConditionManage.holdpace')" prop="holdpace">
        <el-input v-model="form.holdpace" />
      </el-form-item>
      <el-form-item :label="$t('componentConditionManage.param')" prop="param">
        <el-input v-model="form.param" />
      </el-form-item>
      <el-form-item :label="$t('componentConditionManage.type')" prop="type">
        <el-select v-model="form.type" style="width: 100%">
          <el-option :label="$t('componentConditionManage.text')" value="text" />
          <el-option :label="$t('componentConditionManage.date')" value="date" />
        </el-select>
      </el-form-item>
      <el-form-item :label="$t('componentConditionManage.seq')" prop="seq">
        <el-input v-model="form.seq" />
      </el-form-item>
      <el-form-item :label="$t('componentConditionManage.remark')">
        <el-input v-model="form.remark" type="textarea" />
      </el-form-item>
    </el-form>
    <span slot="footer" class="dialog-footer">
      <el-button @click="visible = false">{{ $t('common.cancel') }}</el-button>
      <el-button type="primary" @click="handleSubmit">{{ $t('common.save') }}</el-button>
    </span>
  </el-dialog>
</template>

<script>
import { updateReportCustomComponentCondition } from '@/api/report/componentConditionManageApi'

export default {
  name: 'EditComponentCondition',
  data() {
    return {
      visible: false,
      form: {
        conditionId: '',
        componentId: '',
        name: '',
        holdpace: '',
        param: '',
        type: '',
        remark: '',
        seq: ''
      },
      rules: {
        componentId: [
          { required: true, message: this.$t('componentConditionManage.requiredComponentId'), trigger: 'blur' },
          { max: 30, message: this.$t('componentConditionManage.maxComponentId'), trigger: 'blur' }
        ],
        name: [
          { required: true, message: this.$t('componentConditionManage.requiredName'), trigger: 'blur' },
          { max: 64, message: this.$t('componentConditionManage.maxName'), trigger: 'blur' }
        ],
        holdpace: [
          { required: true, message: this.$t('componentConditionManage.requiredHoldpace'), trigger: 'blur' },
          { max: 64, message: this.$t('componentConditionManage.maxHoldpace'), trigger: 'blur' }
        ],
        param: [
          { required: true, message: this.$t('componentConditionManage.requiredParam'), trigger: 'blur' },
          { max: 64, message: this.$t('componentConditionManage.maxParam'), trigger: 'blur' }
        ],
        type: [
          { required: true, message: this.$t('componentConditionManage.requiredType'), trigger: 'change' },
          { max: 12, message: this.$t('componentConditionManage.maxType'), trigger: 'blur' }
        ],
        seq: [
          { required: true, message: this.$t('componentConditionManage.requiredSeq'), trigger: 'blur' },
        ],
        remark: [
          { max: 512, message: this.$t('componentConditionManage.maxRemark'), trigger: 'blur' }
        ],
        conditionId: [
          { required: true, message: this.$t('componentConditionManage.requiredConditionId'), trigger: 'blur' }
        ]
      }
    }
  },
  methods: {
    open(params) {
      this.form = { ...params }
      this.visible = true
    },
    handleClose() {
      this.$refs.form.resetFields()
    },
    handleSubmit() {
      this.$refs.form.validate(async valid => {
        if (valid) {
          try {
            await updateReportCustomComponentCondition(this.form)
            this.$message.success(this.$t('common.operationSuccess'))
            this.visible = false
            this.$emit('success')
          } catch (error) {
            this.$message.error(error.message || this.$t('common.editFailed'))
          }
        }
      })
    }
  }
}
</script>