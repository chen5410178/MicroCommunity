<template>
  <div>
    <div class="margin-top">
      <el-table :data="feeDetailConfigInfo.feeConfigs" style="width: 100%">
        <el-table-column prop="feeTypeCdName" :label="$t('feeDetailConfig.feeType')" align="center"></el-table-column>
        <el-table-column prop="feeName" :label="$t('feeDetailConfig.feeItem')" align="center"></el-table-column>
        <el-table-column prop="feeFlagName" :label="$t('feeDetailConfig.feeFlag')" align="center"></el-table-column>
        <el-table-column prop="billTypeName" :label="$t('feeDetailConfig.reminderType')"
          align="center"></el-table-column>
        <el-table-column :label="$t('feeDetailConfig.paymentType')" align="center">
          <template slot-scope="scope">
            {{ scope.row.paymentCd == '1200' ? $t('feeDetailConfig.prepaid') : $t('feeDetailConfig.postpaid') }}
          </template>
        </el-table-column>
        <el-table-column prop="paymentCycle" :label="$t('feeDetailConfig.paymentCycle')"
          align="center"></el-table-column>
        <el-table-column prop="units" :label="$t('feeDetailConfig.unit')" align="center"></el-table-column>
        <el-table-column prop="computingFormulaName" :label="$t('feeDetailConfig.formula')"
          align="center"></el-table-column>
        <el-table-column :label="$t('feeDetailConfig.unitPrice')" align="center">
          <template slot-scope="scope">
            {{ scope.row.computingFormula == '2002' ? '-' : scope.row.squarePrice }}
          </template>
        </el-table-column>
        <el-table-column prop="additionalAmount" :label="$t('feeDetailConfig.additionalFee')"
          align="center"></el-table-column>
        <el-table-column :label="$t('feeDetailConfig.accountDeduction')" align="center">
          <template slot-scope="scope">
            {{ scope.row.deductFrom == 'Y' ? $t('feeDetailConfig.yes') : $t('feeDetailConfig.no') }}
          </template>
        </el-table-column>
        <el-table-column :label="$t('feeDetailConfig.mobilePayment')" align="center">
          <template slot-scope="scope">
            {{ scope.row.payOnline == 'Y' ? $t('feeDetailConfig.yes') : $t('feeDetailConfig.no') }}
          </template>
        </el-table-column>
        <el-table-column prop="scale" :label="$t('feeDetailConfig.decimalPlaces')" align="center"></el-table-column>
      </el-table>

      <el-row class="margin-top">
        <el-col :span="12"></el-col>
        <el-col :span="12">
          <el-pagination @current-change="handleCurrentChange" :current-page="pagination.currentPage"
            :page-size="pagination.pageSize" layout="total, prev, pager, next" :total="pagination.total">
          </el-pagination>
        </el-col>
      </el-row>
    </div>
  </div>
</template>

<script>
import { listFeeConfigs } from '@/api/fee/feeDetailConfigApi'
import { getCommunityId } from '@/api/community/communityApi'

export default {
  name: 'FeeDetailConfig',
  data() {
    return {
      feeDetailConfigInfo: {
        feeConfigs: [],
        configId: ''
      },
      pagination: {
        currentPage: 1,
        pageSize: 10,
        total: 0
      }
    }
  },
  methods: {
    open(params) {
      this.feeDetailConfigInfo.configId = params.configId
      this._loadFeeDetailConfigData()
    },
    _loadFeeDetailConfigData() {
      const params = {
        communityId: getCommunityId(),
        configId: this.feeDetailConfigInfo.configId,
        page: this.pagination.currentPage,
        row: this.pagination.pageSize
      }

      listFeeConfigs(params).then(res => {
        this.feeDetailConfigInfo.feeConfigs = res.feeConfigs
        this.pagination.total = res.total
      }).catch(error => {
        console.error('Failed to load fee config details:', error)
      })
    },
    handleCurrentChange(val) {
      this.pagination.currentPage = val
      this._loadFeeDetailConfigData()
    }
  }
}
</script>