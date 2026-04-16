<template>
  <div class="fee-config-detail-his">
    <el-table :data="feeConfigDetailHisInfo.feeDetails" border style="width: 100%" v-loading="loading">
      <el-table-column prop="feeTypeCdName" :label="$t('feeConfigDetailHis.feeTypeCdName')" align="center">
      </el-table-column>
      <el-table-column prop="feeName" :label="$t('feeConfigDetailHis.feeName')" align="center">
      </el-table-column>
      <el-table-column prop="feeFlagName" :label="$t('feeConfigDetailHis.feeFlagName')" align="center">
      </el-table-column>
      <el-table-column prop="billTypeName" :label="$t('feeConfigDetailHis.billTypeName')" align="center">
      </el-table-column>
      <el-table-column :label="$t('feeConfigDetailHis.paymentCd')" align="center">
        <template slot-scope="scope">
          {{ scope.row.paymentCd === '1200' ? $t('feeConfigDetailHis.prePayment') : $t('feeConfigDetailHis.postPayment')
          }}
        </template>
      </el-table-column>
      <el-table-column prop="paymentCycle" :label="$t('feeConfigDetailHis.paymentCycle')" align="center">
      </el-table-column>
      <el-table-column :label="$t('feeConfigDetailHis.validityPeriod')" align="center">
        <template slot-scope="scope">
          <div>{{ scope.row.startTime }}</div>
          <div>{{ scope.row.endTime }}</div>
        </template>
      </el-table-column>
      <el-table-column :label="$t('feeConfigDetailHis.squarePrice')" align="center">
        <template slot-scope="scope">
          {{ scope.row.computingFormula === '2002' ? '-' : scope.row.squarePrice }}
        </template>
      </el-table-column>
      <el-table-column prop="additionalAmount" :label="$t('feeConfigDetailHis.additionalAmount')" align="center">
      </el-table-column>
      <el-table-column :label="$t('feeConfigDetailHis.deductFrom')" align="center">
        <template slot-scope="scope">
          {{ scope.row.deductFrom === 'Y' ? $t('common.yes') : $t('common.no') }}
        </template>
      </el-table-column>
      <el-table-column :label="$t('feeConfigDetailHis.payOnline')" align="center">
        <template slot-scope="scope">
          {{ scope.row.payOnline === 'Y' ? $t('common.yes') : $t('common.no') }}
        </template>
      </el-table-column>
      <el-table-column :label="$t('feeConfigDetailHis.scale')" align="center">
        <template slot-scope="scope">
          <div v-if="scope.row.scale === '1'">{{ $t('feeConfigDetailHis.round') }}</div>
          <div v-if="scope.row.scale === '3'">{{ $t('feeConfigDetailHis.roundUp') }}</div>
          <div v-if="scope.row.scale === '4'">{{ $t('feeConfigDetailHis.roundDown') }}</div>
        </template>
      </el-table-column>
      <el-table-column prop="decimalPlace" :label="$t('feeConfigDetailHis.decimalPlace')" align="center">
      </el-table-column>
      <el-table-column :label="$t('feeConfigDetailHis.operate')" align="center">
        <template slot-scope="scope">
          {{ _getHisConfigOperate(scope.row) }}
        </template>
      </el-table-column>
      <el-table-column prop="userName" :label="$t('feeConfigDetailHis.userName')" align="center">
        <template slot-scope="scope">
          {{ scope.row.userName || '-' }}
        </template>
      </el-table-column>
      <el-table-column prop="createTime" :label="$t('feeConfigDetailHis.createTime')" align="center">
        <template slot-scope="scope">
          {{ scope.row.createTime || '-' }}
        </template>
      </el-table-column>
    </el-table>

    <el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange" :current-page="page.current"
      :page-sizes="[10, 20, 30, 50]" :page-size="page.size" layout="total, sizes, prev, pager, next, jumper"
      :total="page.total">
    </el-pagination>
  </div>
</template>

<script>
import { queryHisFeeConfig } from '@/api/system/operateDataLogApi'
import { getCommunityId } from '@/api/community/communityApi'

export default {
  name: 'FeeConfigDetailHis',
  data() {
    return {
      loading: false,
      feeConfigDetailHisInfo: {
        feeDetails: [],
        configId: '',
        staffNameLike: '',
        feeNameLike: '',
        logStartTime: '',
        logEndTime: ''
      },
      page: {
        current: 1,
        size: 10,
        total: 0
      }
    }
  },
  methods: {
    open(conditions) {
      this.feeConfigDetailHisInfo = {
        ...this.feeConfigDetailHisInfo,
        ...conditions
      }
      this._loadFeeConfigDetailHisData()
    },
    async _loadFeeConfigDetailHisData() {
      try {
        this.loading = true
        const params = {
          page: this.page.current,
          row: this.page.size,
          configId: this.feeConfigDetailHisInfo.configId,
          staffNameLike: this.feeConfigDetailHisInfo.staffNameLike,
          feeNameLike: this.feeConfigDetailHisInfo.feeNameLike,
          logStartTime: this.feeConfigDetailHisInfo.logStartTime,
          logEndTime: this.feeConfigDetailHisInfo.logEndTime,
          communityId: getCommunityId() 
        }

        const { data, total } = await queryHisFeeConfig(params)
        this.feeConfigDetailHisInfo.feeDetails = data
        this.page.total = total
      } catch (error) {
        console.error('Failed to load fee config history:', error)
      } finally {
        this.loading = false
      }
    },
    _getHisConfigOperate(fee) {
      const feeCount = this.feeConfigDetailHisInfo.feeDetails.filter(item => item.bId === fee.bId).length

      if (feeCount <= 1) {
        if (fee.operate === 'ADD') return this.$t('feeConfigDetailHis.add')
        if (fee.operate === 'DEL') return this.$t('feeConfigDetailHis.delete')
        return '-'
      }

      if (fee.operate === 'ADD') return this.$t('feeConfigDetailHis.modifyNew')
      if (fee.operate === 'DEL') return this.$t('feeConfigDetailHis.modifyOld')
      return '-'
    },
    handleSizeChange(size) {
      this.page.size = size
      this._loadFeeConfigDetailHisData()
    },
    handleCurrentChange(current) {
      this.page.current = current
      this._loadFeeConfigDetailHisData()
    }
  }
}
</script>

<style scoped>
.fee-config-detail-his {
  padding: 20px;
}

.el-pagination {
  margin-top: 20px;
  text-align: right;
}
</style>