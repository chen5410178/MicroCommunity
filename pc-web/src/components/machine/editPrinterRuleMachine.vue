<template>
  <el-dialog :title="$t('printerRuleMachine.editTitle')" :visible.sync="visible" width="40%" @close="handleClose">
    <el-form ref="form" :model="form" :rules="rules" label-width="120px" class="text-left">
      <el-form-item :label="$t('printerRuleMachine.machine')" prop="machineId">
        <el-select v-model="form.machineId" :placeholder="$t('printerRuleMachine.machinePlaceholder')"
          style="width:100%">
          <el-option v-for="item in machines" :key="item.machineId" :label="`${item.machineName}(${item.machineCode})`"
            :value="item.machineId" />
        </el-select>
      </el-form-item>
      <el-form-item :label="$t('printerRuleMachine.quantity')" prop="quantity">
        <el-input-number v-model="form.quantity" :min="1" :max="100"
          :placeholder="$t('printerRuleMachine.quantityPlaceholder')" />
      </el-form-item>
    </el-form>

    <span slot="footer" class="dialog-footer">
      <el-button @click="visible = false">{{ $t('common.cancel') }}</el-button>
      <el-button type="primary" @click="handleSubmit">{{ $t('common.confirm') }}</el-button>
    </span>
  </el-dialog>
</template>

<script>
import { updatePrinterRuleMachine, listMachinePrinters } from '@/api/machine/printerRuleApi'
import { getCommunityId } from '@/api/community/communityApi'

export default {
  name: 'EditPrinterRuleMachine',
  data() {
    return {
      visible: false,
      form: {
        prmId: '',
        machineId: '',
        quantity: 1,
        communityId: ''
      },
      machines: [],
      rules: {
        machineId: [
          { required: true, message: this.$t('printerRuleMachine.machineRequired'), trigger: 'change' }
        ],
        quantity: [
          { required: true, message: this.$t('printerRuleMachine.quantityRequired'), trigger: 'blur' }
        ]
      }
    }
  },
  created() {
    this.form.communityId = getCommunityId()
    this.getMachines()
  },
  methods: {
    open(row) {
      this.visible = true
      this.form = {
        prmId: row.prmId,
        machineId: row.machineId,
        quantity: row.quantity,
        communityId: getCommunityId()
      }
    },
    handleClose() {
      this.$refs.form.resetFields()
    },
    async getMachines() {
      try {
        const params = {
          page: 1,
          row: 100,
          communityId: this.form.communityId
        }
        const { data } = await listMachinePrinters(params)
        this.machines = data
      } catch (error) {
        console.error('获取打印机列表失败:', error)
      }
    },
    async handleSubmit() {
      this.$refs.form.validate(async valid => {
        if (valid) {
          try {
            await updatePrinterRuleMachine(this.form)
            this.$message.success(this.$t('common.operationSuccess'))
            this.visible = false
            this.$emit('success')
          } catch (error) {
            console.error('修改打印机规则机器失败:', error)
          }
        }
      })
    }
  }
}
</script>