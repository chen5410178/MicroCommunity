<template>
  <div>
    <el-row>
      <el-col :span="24" class="text-right"></el-col>
    </el-row>
    <div class="margin-top">
      <el-table :data="aFeeDetailHisInfo.fees" border style="width: 100%">
        <el-table-column prop="feeName" :label="$t('aFeeDetailHis.feeName')" align="center">
          <template #default="{ row }">
            {{ row.feeName }}
            <span v-if="row.payerObjName">({{ row.payerObjName }})</span>
          </template>
        </el-table-column>
        <el-table-column prop="startTime" :label="$t('aFeeDetailHis.startTime')" align="center">
          <template #default="{ row }">{{ row.startTime || '-' }}</template>
        </el-table-column>
        <el-table-column prop="endTime" :label="$t('aFeeDetailHis.endTime')" align="center">
          <template #default="{ row }">{{ row.endTime || '-' }}</template>
        </el-table-column>
        <el-table-column :label="$t('aFeeDetailHis.operation')" align="center">
          <template #default="{ row }">{{ _getFeeHisOperate(row) }}</template>
        </el-table-column>
        <el-table-column prop="userName" :label="$t('aFeeDetailHis.operator')" align="center">
          <template #default="{ row }">{{ row.userName || '-' }}</template>
        </el-table-column>
        <el-table-column prop="createTime" :label="$t('aFeeDetailHis.operationTime')" align="center"></el-table-column>
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
  </div>
</template>

<script>
import { queryAdminHisFee } from '@/api/aCommunity/aFeeDetailHisApi'

export default {
  name: 'AFeeDetailHis',
  data() {
    return {
      aFeeDetailHisInfo: {
        fees: [],
        feeId: '',
        staffNameLike: '',
        feeNameLike: '',
        payerObjName: '',
        logStartTime: '',
        logEndTime: ''
      },
      currentPage: 1,
      pageSize: 10,
      total: 0
    }
  },
  methods: {
    open(data) {
      this.aFeeDetailHisInfo.feeId = data.feeId
      this.aFeeDetailHisInfo.staffNameLike = data.staffNameLike
      this.aFeeDetailHisInfo.feeNameLike = data.feeNameLike
      this.aFeeDetailHisInfo.payerObjName = data.payerObjName
      this.aFeeDetailHisInfo.logStartTime = data.logStartTime
      this.aFeeDetailHisInfo.logEndTime = data.logEndTime
      this._loadAFeeDetailHisData()
    },
    handleSwitch(data) {
      this.aFeeDetailHisInfo.feeId = data.feeId
      this.aFeeDetailHisInfo.staffNameLike = data.staffNameLike
      this.aFeeDetailHisInfo.feeNameLike = data.feeNameLike
      this.aFeeDetailHisInfo.payerObjName = data.payerObjName
      this.aFeeDetailHisInfo.logStartTime = data.logStartTime
      this.aFeeDetailHisInfo.logEndTime = data.logEndTime
      this._loadAFeeDetailHisData()
    },
    handleNotify() {
      this._loadAFeeDetailHisData()
    },
    handlePageChange(page) {
      this.currentPage = page
      this._loadAFeeDetailHisData()
    },
    async _loadAFeeDetailHisData() {
      try {
        const params = {
          feeId: this.aFeeDetailHisInfo.feeId,
          staffNameLike: this.aFeeDetailHisInfo.staffNameLike,
          feeNameLike: this.aFeeDetailHisInfo.feeNameLike,
          payerObjName: this.aFeeDetailHisInfo.payerObjName,
          logStartTime: this.aFeeDetailHisInfo.logStartTime,
          logEndTime: this.aFeeDetailHisInfo.logEndTime,
          page: this.currentPage,
          row: this.pageSize
        }

        const res = await queryAdminHisFee(params)
        this.aFeeDetailHisInfo.fees = res.data
        this.total = res.total
      } catch (error) {
        console.error('加载费用历史数据失败:', error)
      }
    },
    _getFeeHisOperate(fee) {
      let feeCount = 0
      this.aFeeDetailHisInfo.fees.forEach(item => {
        if (fee.bId === item.bId) {
          feeCount += 1
        }
      })

      if (feeCount <= 1) {
        if (fee.operate === 'ADD') return this.$t('aFeeDetailHis.add')
        if (fee.operate === 'DEL') return this.$t('aFeeDetailHis.delete')
        return '-'
      }

      if (fee.operate === 'ADD') return this.$t('aFeeDetailHis.modifyNew')
      if (fee.operate === 'DEL') return this.$t('aFeeDetailHis.modifyOld')
      return '-'
    }
  }
}
</script>

<style scoped>
.margin-top {
  margin-top: 20px;
}

.text-right {
  text-align: right;
}
</style>