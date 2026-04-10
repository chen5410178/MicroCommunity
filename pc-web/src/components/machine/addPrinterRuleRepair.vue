<template>
  <el-dialog
    :title="$t('printerRuleRepair.addTitle')"
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
        :label="$t('printerRuleRepair.repairType')"
        prop="repairType"
      >
        <el-select
          v-model="form.repairType"
          :placeholder="$t('printerRuleRepair.repairTypePlaceholder')"
          style="width:100%"
        >
          <el-option
            v-for="item in repairSettings"
            :key="item.repairType"
            :label="item.repairTypeName"
            :value="item.repairType"
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
import { savePrinterRuleRepair, listRepairSettings } from '@/api/machine/printerRuleApi'
import { getCommunityId } from '@/api/community/communityApi'

export default {
  name: 'AddPrinterRuleRepair',
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
        repairType: '',
        ruleId: '',
        communityId: ''
      },
      repairSettings: [],
      rules: {
        repairType: [
          { required: true, message: this.$t('printerRuleRepair.repairTypeRequired'), trigger: 'change' }
        ]
      }
    }
  },
  created() {
    this.form.communityId = getCommunityId()
    this.getRepairSettings()
  },
  methods: {
    open() {
      this.visible = true
      this.form.ruleId = this.ruleId
    },
    handleClose() {
      this.$refs.form.resetFields()
    },
    async getRepairSettings() {
      try {
        const params = {
          page: 1,
          row: 100,
          communityId: this.form.communityId
        }
        const { data } = await listRepairSettings(params)
        this.repairSettings = data
      } catch (error) {
        console.error('获取报修设置列表失败:', error)
      }
    },
    async handleSubmit() {
      this.$refs.form.validate(async valid => {
        if (valid) {
          try {
            await savePrinterRuleRepair(this.form)
            this.$message.success(this.$t('common.operationSuccess'))
            this.visible = false
            this.$emit('success')
          } catch (error) {
            console.error('添加报修自动打印失败:', error)
          }
        }
      })
    }
  }
}
</script>