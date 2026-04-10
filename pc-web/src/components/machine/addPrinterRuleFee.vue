<template>
  <el-dialog
    :title="$t('printerRuleFees.addTitle')"
    :visible.sync="visible"
    width="50%"
    @close="handleClose"
  >
    <el-form
      ref="form"
      :model="form"
      :rules="rules"
      label-width="120px"
    >
      <el-form-item
        :label="$t('printerRuleFees.fee')"
        prop="feeId"
      >
        <el-select
          v-model="form.feeId"
          :placeholder="$t('printerRuleFees.feePlaceholder')"
          style="width:100%"
        >
          <el-option
            v-for="item in feeConfigs"
            :key="item.configId"
            :label="item.feeName"
            :value="item.configId"
          />
        </el-select>
      </el-form-item>
    </el-form>

    <span slot="footer" class="dialog-footer">
      <el-button @click="visible = false">{{ $t('common.cancel') }}</el-button>
      <el-button type="primary" @click="handleSubmit">{{ $t('common.confirm') }}</el-button>
    </span>
  </el-dialog>
</template>

<script>
import { savePrinterRuleFee, listFeeConfigs } from '@/api/machine/printerRuleApi'
import { getCommunityId } from '@/api/community/communityApi'

export default {
  name: 'AddPrinterRuleFee',
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
        feeId: '',
        ruleId: '',
        communityId: ''
      },
      feeConfigs: [],
      rules: {
        feeId: [
          { required: true, message: this.$t('printerRuleFees.feeRequired'), trigger: 'change' }
        ]
      }
    }
  },
  created() {
    this.form.communityId = getCommunityId()
    this.getFeeConfigs()
  },
  methods: {
    open() {
      this.visible = true
      this.form.ruleId = this.ruleId
    },
    handleClose() {
      this.$refs.form.resetFields()
    },
    async getFeeConfigs() {
      try {
        const params = {
          page: 1,
          row: 100,
          communityId: this.form.communityId
        }
        const { feeConfigs } = await listFeeConfigs(params)
        this.feeConfigs = feeConfigs
      } catch (error) {
        console.error('获取费用配置列表失败:', error)
      }
    },
    async handleSubmit() {
      this.$refs.form.validate(async valid => {
        if (valid) {
          try {
            await savePrinterRuleFee(this.form)
            this.$message.success(this.$t('common.operationSuccess'))
            this.visible = false
            this.$emit('success')
          } catch (error) {
            console.error('添加缴费自动打印失败:', error)
          }
        }
      })
    }
  }
}
</script>