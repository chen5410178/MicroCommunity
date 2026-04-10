<template>
  <div class="margin-top">
    <el-table :data="aFeeDetailConfigInfo.feeConfigs" border style="width: 100%">
      <el-table-column prop="feeTypeCdName" :label="$t('aFeeDetailConfig.feeType')" align="center"></el-table-column>
      <el-table-column prop="feeName" :label="$t('aFeeDetailConfig.feeItem')" align="center"></el-table-column>
      <el-table-column prop="feeFlagName" :label="$t('aFeeDetailConfig.feeFlag')" align="center"></el-table-column>
      <el-table-column prop="billTypeName" :label="$t('aFeeDetailConfig.billType')" align="center"></el-table-column>
      <el-table-column :label="$t('aFeeDetailConfig.paymentType')" align="center">
        <template #default="{ row }">{{ row.paymentCd === '1200' ? $t('aFeeDetailConfig.prepay') :
          $t('aFeeDetailConfig.postpay')}}</template>
      </el-table-column>
      <el-table-column prop="paymentCycle" :label="$t('aFeeDetailConfig.paymentCycle')"
        align="center"></el-table-column>
      <el-table-column prop="units" :label="$t('aFeeDetailConfig.unit')" align="center"></el-table-column>
      <el-table-column prop="computingFormulaName" :label="$t('aFeeDetailConfig.formula')"
        align="center"></el-table-column>
      <el-table-column :label="$t('aFeeDetailConfig.unitPrice')" align="center">
        <template #default="{ row }">{{ row.computingFormula === '2002' ? '-' : row.squarePrice }}</template>
      </el-table-column>
      <el-table-column prop="additionalAmount" :label="$t('aFeeDetailConfig.additionalFee')"
        align="center"></el-table-column>
      <el-table-column :label="$t('aFeeDetailConfig.accountDeduction')" align="center">
        <template #default="{ row }">{{ row.deductFrom === 'Y' ? $t('aFeeDetailConfig.yes') :
          $t('aFeeDetailConfig.no')}}</template>
      </el-table-column>
      <el-table-column :label="$t('aFeeDetailConfig.mobilePayment')" align="center">
        <template #default="{ row }">{{ row.payOnline === 'Y' ? $t('aFeeDetailConfig.yes') :
          $t('aFeeDetailConfig.no')}}</template>
      </el-table-column>
      <el-table-column prop="scale" :label="$t('aFeeDetailConfig.decimalPlaces')" align="center"></el-table-column>
    </el-table>

      <el-row class="margin-top">
        <el-col :span="12"></el-col>
        <el-col :span="12">
          <el-pagination @current-change="handlePageChange" :current-page="currentPage" :page-size="pageSize"
            layout="total, prev, pager, next, jumper" :total="total">
          </el-pagination>
        </el-col>
      </el-row>
  </div>
</template>

<script>
import { queryAdminFeeConfigs } from '@/api/aCommunity/aFeeDetailConfigApi'

export default {
  name: 'AFeeDetailConfig',
  data() {
    return {
      aFeeDetailConfigInfo: {
        feeConfigs: [],
        configId: ''
      },
      currentPage: 1,
      pageSize: 10,
      total: 0
    }
  },
  methods: {
    open(data) {
      this.aFeeDetailConfigInfo.configId = data.configId
      this._loadAFeeDetailConfigData()
    },
    handleSwitch(data) {
      this.aFeeDetailConfigInfo.configId = data.configId
      this._loadAFeeDetailConfigData()
    },
    handleNotify() {
      this._loadAFeeDetailConfigData()
    },
    handlePageChange(page) {
      this.currentPage = page
      this._loadAFeeDetailConfigData()
    },
    async _loadAFeeDetailConfigData() {
      try {
        const params = {
          configId: this.aFeeDetailConfigInfo.configId,
          page: this.currentPage,
          row: this.pageSize
        }

        const res = await queryAdminFeeConfigs(params)
        this.aFeeDetailConfigInfo.feeConfigs = res.feeConfigs
        this.total = res.total
      } catch (error) {
        console.error('加载费用配置数据失败:', error)
      }
    }
  }
}
</script>

<style scoped>
.margin-top {
  margin-top: 20px;
}
</style>