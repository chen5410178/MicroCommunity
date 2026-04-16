<template>
  <el-dialog :title="$t('printerRule.addTitle')" :visible.sync="visible" width="50%" @close="handleClose">
    <el-form ref="form" :model="form" :rules="rules" label-width="120px">
      <el-form-item :label="$t('printerRule.ruleName')" prop="ruleName">
        <el-input v-model="form.ruleName" :placeholder="$t('printerRule.ruleNamePlaceholder')"></el-input>
      </el-form-item>
      <el-form-item :label="$t('printerRule.remark')" prop="remark">
        <el-input type="textarea" v-model="form.remark" :placeholder="$t('printerRule.remarkPlaceholder')"></el-input>
      </el-form-item>
      <!-- <el-form-item :label="$t('printerRule.state')" prop="state">
        <el-select v-model="form.state" style="width:100%">
          <el-option v-for="item in stateOptions" :key="item.statusCd" :label="item.name" :value="item.statusCd"></el-option>
        </el-select>
      </el-form-item> -->
    </el-form>

    <span slot="footer" class="dialog-footer">
      <el-button @click="visible = false">{{ $t('common.cancel') }}</el-button>
      <el-button type="primary" @click="handleSubmit">{{ $t('common.confirm') }}</el-button>
    </span>
  </el-dialog>
</template>

<script>
import { savePrinterRule } from '@/api/machine/printerRuleApi'
import { getCommunityId } from '@/api/community/communityApi'
import { getDict } from '@/api/community/communityApi'

export default {
  name: 'AddPrinterRule',
  data() {
    return {
      visible: false,
      form: {
        ruleName: '',
        remark: '',
        state: '1001',
        communityId: ''
      },
      rules: {
        ruleName: [
          { required: true, message: this.$t('printerRule.ruleNameRequired'), trigger: 'blur' },
          { max: 64, message: this.$t('printerRule.ruleNameMaxLength'), trigger: 'blur' }
        ],
        remark: [
          { max: 512, message: this.$t('printerRule.remarkMaxLength'), trigger: 'blur' }
        ]
      },
      stateOptions: []
    }
  },
  created() {
    this.form.communityId = getCommunityId()
  },
  methods: {
    async getStateOptions() {
      try {
        const {data} = await getDict('printer_rule', 'state')
        this.stateOptions = data
      } catch (error) {
        console.error('获取状态字典失败:', error)
      }
    },
    open() {
      this.visible = true
      this.resetForm()
      this.getStateOptions()

    },
    handleClose() {
      this.resetForm()
    },
    resetForm() {
      this.$refs.form && this.$refs.form.resetFields()
      this.form = {
        ruleName: '',
        remark: '',
        state: '1001',
        communityId: getCommunityId()
      }
    },
    handleSubmit() {
      this.$refs.form.validate(async valid => {
        if (valid) {
          try {
            await savePrinterRule(this.form)
            this.$message.success(this.$t('common.operationSuccess'))
            this.visible = false
            this.$emit('success')
          } catch (error) {
            console.error('添加打印机规则失败:', error)
          }
        }
      })
    }
  }
}
</script>