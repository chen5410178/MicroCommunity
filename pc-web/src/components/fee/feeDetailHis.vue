<template>
  <div>
    <el-row class="margin-top">
      <el-col :span="24" class="text-right"></el-col>
    </el-row>
    <div class="margin-top">
      <el-table :data="feeDetailHisInfo.fees" border style="width: 100%">
        <el-table-column prop="feeName" :label="$t('feeDetailHis.feeName')" align="center">
          <template #default="{ row }">
            {{ row.feeName }}
            <span v-if="row.payerObjName">({{ row.payerObjName }})</span>
          </template>
        </el-table-column>
        <el-table-column prop="startTime" :label="$t('feeDetailHis.startTime')" align="center"></el-table-column>
        <el-table-column prop="endTime" :label="$t('feeDetailHis.endTime')" align="center"></el-table-column>
        <el-table-column :label="$t('feeDetailHis.operation')" align="center">
          <template #default="{ row }">
            {{ _getFeeHisOperate(row) }}
          </template>
        </el-table-column>
        <el-table-column prop="userName" :label="$t('feeDetailHis.operator')" align="center"></el-table-column>
        <el-table-column prop="createTime" :label="$t('feeDetailHis.operationTime')" align="center"></el-table-column>
      </el-table>

      <el-row class="margin-top">
        <el-col :span="4"></el-col>
        <el-col :span="20">
          <el-pagination @current-change="handleCurrentChange" :current-page="currentPage" :page-size="pageSize"
            layout="total, prev, pager, next, jumper" :total="total"></el-pagination>
        </el-col>
      </el-row>
    </div>
  </div>
</template>

<script>
import { queryHisFee } from '@/api/fee/feeDetailHisApi'
import { getCommunityId } from '@/api/community/communityApi'

export default {
  name: 'FeeDetailHis',
  data() {
    return {
      feeDetailHisInfo: {
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
    open(params) {
      this.feeDetailHisInfo = Object.assign(this.feeDetailHisInfo, params)
      this._loadFeeDetailHisData(this.currentPage, this.pageSize)
    },
    handleCurrentChange(val) {
      this.currentPage = val
      this._loadFeeDetailHisData(this.currentPage, this.pageSize)
    },
    async _loadFeeDetailHisData(page, row) {
      try {
        const params = {
          communityId: getCommunityId(),
          feeId: this.feeDetailHisInfo.feeId,
          staffNameLike: this.feeDetailHisInfo.staffNameLike,
          feeNameLike: this.feeDetailHisInfo.feeNameLike,
          payerObjName: this.feeDetailHisInfo.payerObjName,
          logStartTime: this.feeDetailHisInfo.logStartTime,
          logEndTime: this.feeDetailHisInfo.logEndTime,
          page,
          row
        }

        const res = await queryHisFee(params)
        this.feeDetailHisInfo.fees = res.data
        this.total = res.total
      } catch (error) {
        console.error('Failed to load fee history data:', error)
      }
    },
    _getFeeHisOperate(fee) {
      let feeCount = 0
      this.feeDetailHisInfo.fees.forEach(item => {
        if (fee.bId === item.bId) {
          feeCount += 1
        }
      })

      if (feeCount <= 1) {
        if (fee.operate === 'ADD') return this.$t('feeDetailHis.add')
        if (fee.operate === 'DEL') return this.$t('feeDetailHis.delete')
        return '-'
      }

      if (fee.operate === 'ADD') return this.$t('feeDetailHis.modifyNew')
      if (fee.operate === 'DEL') return this.$t('feeDetailHis.modifyOld')
      return '-'
    }
  }
}
</script>