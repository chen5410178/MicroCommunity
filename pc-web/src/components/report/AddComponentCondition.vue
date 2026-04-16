<template>
  <el-dialog :title="$t('common.add')" :visible.sync="visible" width="50%" @close="handleClose">
    <el-form :model="form" :rules="rules" ref="form" label-width="120px">
      <el-form-item :label="$t('componentConditionManage.name')" prop="name">
        <el-input v-model="form.name" :placeholder="$t('componentConditionManage.requiredName')" />
      </el-form-item>
      <el-form-item :label="$t('componentConditionManage.holdpace')" prop="holdpace">
        <el-input v-model="form.holdpace" :placeholder="$t('componentConditionManage.requiredHoldpace')" />
      </el-form-item>
      <el-form-item :label="$t('componentConditionManage.param')" prop="param">
        <el-input v-model="form.param" :placeholder="$t('componentConditionManage.requiredParam')" />
      </el-form-item>
      <el-form-item :label="$t('componentConditionManage.type')" prop="type">
        <el-select v-model="form.type" :placeholder="$t('componentConditionManage.requiredType')" style="width: 100%">
          <el-option :label="$t('componentConditionManage.text')" value="text" />
          <el-option :label="$t('componentConditionManage.date')" value="date" />
        </el-select>
      </el-form-item>
      <el-form-item :label="$t('componentConditionManage.seq')" prop="seq">
        <el-input v-model="form.seq" :placeholder="$t('componentConditionManage.requiredSeq')" />
      </el-form-item>
      <el-form-item :label="$t('componentConditionManage.remark')">
        <el-input v-model="form.remark" type="textarea" :placeholder="$t('componentConditionManage.optionalRemark')" />
      </el-form-item>
    </el-form>
    <span slot="footer" class="dialog-footer">
      <el-button @click="visible = false">{{ $t('common.cancel') }}</el-button>
      <el-button type="primary" @click="handleSubmit">{{ $t('common.save') }}</el-button>
    </span>
  </el-dialog>
</template>

<script>
import { saveReportCustomComponentCondition } from '@/api/report/componentConditionManageApi'

export default {
  name: 'AddComponentCondition',
  data() {
    return {
      visible: false,
      form: {
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
        remark: [
          { max: 512, message: this.$t('componentConditionManage.maxRemark'), trigger: 'blur' }
        ]
      }
    }
  },
  methods: {
    open(params) {
      this.form = {
        componentId: params.componentId,
        name: '',
        holdpace: '',
        param: '',
        type: '',
        remark: '',
        seq: ''
      }
      this.visible = true
    },
    handleClose() {
      this.$refs.form.resetFields()
    },
    handleSubmit() {
      this.$refs.form.validate(async valid => {
        if (valid) {
          try {
            await saveReportCustomComponentCondition(this.form)
            this.$message.success(this.$t('common.operationSuccess'))
            this.visible = false
            this.$emit('success')
          } catch (error) {
            this.$message.error(error.message || this.$t('common.addFailed'))
          }
        }
      })
    }
  }
}
</script>