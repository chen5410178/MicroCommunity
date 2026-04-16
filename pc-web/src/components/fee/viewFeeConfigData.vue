<template>
  <el-dialog
    :title="title"
    :visible.sync="dialogVisible"
    width="50%"
    @close="handleClose"
  >
    <el-table
      :data="tableData"
      border
      style="width: 100%"
    >
      <el-table-column
        prop="label"
        :label="$t('viewFeeConfigData.label')"
        width="180"
      />
      <el-table-column
        prop="value"
        :label="$t('viewFeeConfigData.value')"
      />
    </el-table>
  </el-dialog>
</template>

<script>
import { getCommunityId } from '@/api/community/communityApi'

export default {
  name: 'ViewFeeConfigData',
  data() {
    return {
      dialogVisible: false,
      title: '',
      tableData: [],
      configId: ''
    }
  },
  methods: {
    open(params) {
      this.configId = params.configId
      this.loadFeeConfigData()
      this.dialogVisible = true
    },
    close() {
      this.dialogVisible = false
    },
    async loadFeeConfigData() {
      try {
        const communityId = await getCommunityId()
        const params = {
          page: 1,
          row: 1,
          communityId,
          configId: this.configId
        }

        const response = await this.$http.get('/feeConfig.listFeeConfigs', { params })
        const feeConfig = response.data.feeConfigs[0]

        this.title = `${feeConfig.feeName} ${this.$t('viewFeeConfigData.feeItem')}`

        this.tableData = [
          { label: this.$t('viewFeeConfigData.configId'), value: feeConfig.configId },
          { label: this.$t('viewFeeConfigData.feeType'), value: feeConfig.feeTypeCdName },
          { label: this.$t('viewFeeConfigData.feeName'), value: feeConfig.feeName },
          { label: this.$t('viewFeeConfigData.feeFlag'), value: feeConfig.feeFlagName },
          { label: this.$t('viewFeeConfigData.billType'), value: feeConfig.billTypeName },
          { label: this.$t('viewFeeConfigData.paymentType'), value: feeConfig.paymentCd === '1200' ? this.$t('viewFeeConfigData.prepay') : this.$t('viewFeeConfigData.postpay') },
          { label: this.$t('viewFeeConfigData.paymentCycle'), value: feeConfig.paymentCycle },
          { label: this.$t('viewFeeConfigData.billingStartTime'), value: feeConfig.startTime },
          { label: this.$t('viewFeeConfigData.billingEndTime'), value: feeConfig.endTime },
          { label: this.$t('viewFeeConfigData.formula'), value: feeConfig.computingFormulaName },
          { label: this.$t('viewFeeConfigData.unitPrice'), value: feeConfig.computingFormula === '2002' ? '-' : feeConfig.squarePrice },
          { label: this.$t('viewFeeConfigData.additionalFee'), value: feeConfig.additionalAmount }
        ]
      } catch (error) {
        console.error('加载费用配置数据失败:', error)
      }
    },
    handleClose() {
      this.tableData = []
      this.title = ''
      this.configId = ''
    }
  }
}
</script>