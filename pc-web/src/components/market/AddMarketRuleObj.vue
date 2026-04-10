<template>
  <el-dialog :title="$t('marketRule.associateObject')" :visible.sync="visible" width="50%" @close="handleClose">
    <el-form ref="form" :model="form" :rules="rules" label-width="150px">
      <el-form-item :label="$t('marketRule.objectName')" prop="objType">
        <el-select v-model="form.objType" :placeholder="$t('marketRule.selectObject')" style="width: 100%;">
          <el-option v-for="item in objTypes" :key="item.statusCd" :label="item.name" :value="item.statusCd" />
        </el-select>
      </el-form-item>

      <el-form-item :label="$t('marketRule.remark')" prop="remark">
        <el-input type="textarea" v-model="form.remark" :placeholder="$t('marketRule.optional')" rows="4"></el-input>
      </el-form-item>
    </el-form>
    <span slot="footer" class="dialog-footer">
      <el-button @click="visible = false">{{ $t('marketRule.cancel') }}</el-button>
      <el-button type="primary" @click="handleSubmit">{{ $t('marketRule.save') }}</el-button>
    </span>
  </el-dialog>
</template>

<script>
import { saveMarketRuleObj } from '@/api/market/marketRuleApi'

export default {
  name: 'AddMarketRuleObj',
  props: {
    ruleId: {
      type: String,
      required: true
    }
  },
  data() {
    return {
      visible: false,
      form: {
        ruleId: '',
        objType: '',
        remark: ''
      },
      objTypes: [
        { statusCd: '1001', name: this.$t('marketRule.customer') },
        { statusCd: '2002', name: this.$t('marketRule.merchant') },
        { statusCd: '3003', name: this.$t('marketRule.staff') }
      ],
      rules: {
        objType: [
          { required: true, message: this.$t('marketRule.required'), trigger: 'change' }
        ],
        remark: [
          { max: 512, message: this.$t('marketRule.maxLength512'), trigger: 'blur' }
        ]
      }
    }
  },
  methods: {
    open() {
      this.form.ruleId = this.ruleId
      this.visible = true
    },
    handleClose() {
      this.$refs.form.resetFields()
      this.form = {
        ruleId: this.ruleId,
        objType: '',
        remark: ''
      }
    },
    handleSubmit() {
      this.$refs.form.validate(async valid => {
        if (valid) {
          try {
            await saveMarketRuleObj(this.form)
            this.$message.success(this.$t('common.operationSuccess'))
            this.visible = false
            this.$emit('success')
          } catch (error) {
            this.$message.error(error.message)
          }
        }
      })
    }
  }
}
</script>