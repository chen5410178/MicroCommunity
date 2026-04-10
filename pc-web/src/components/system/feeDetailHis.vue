<template>
  <div class="fee-detail-his">
    <el-table :data="feeDetailHisInfo.fees" border style="width: 100%" v-loading="loading">
      <el-table-column :label="$t('feeDetailHis.feeName')" align="center">
        <template slot-scope="scope">
          {{ scope.row.feeName }}
          <span v-if="scope.row.payerObjName">({{ scope.row.payerObjName }})</span>
        </template>
      </el-table-column>
      <el-table-column :label="$t('feeDetailHis.startTime')" align="center">
        <template slot-scope="scope">
          {{ scope.row.startTime || '-' }}
        </template>
      </el-table-column>
      <el-table-column :label="$t('feeDetailHis.endTime')" align="center">
        <template slot-scope="scope">
          {{ scope.row.endTime || '-' }}
        </template>
      </el-table-column>
      <el-table-column :label="$t('feeDetailHis.operate')" align="center">
        <template slot-scope="scope">
          {{ _getFeeHisOperate(scope.row) }}
        </template>
      </el-table-column>
      <el-table-column :label="$t('feeDetailHis.userName')" align="center">
        <template slot-scope="scope">
          {{ scope.row.userName || '-' }}
        </template>
      </el-table-column>
      <el-table-column :label="$t('feeDetailHis.createTime')" align="center">
        <template slot-scope="scope">
          {{ scope.row.createTime }}
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
import { queryHisFee } from '@/api/system/operateDataLogApi'
import { getCommunityId } from '@/api/community/communityApi'

export default {
  name: 'FeeDetailHis',
  data() {
    return {
      loading: false,
      feeDetailHisInfo: {
        fees: [],
        feeId: '',
        staffNameLike: '',
        feeNameLike: '',
        payerObjName: '',
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
      this.feeDetailHisInfo = {
        ...this.feeDetailHisInfo,
        ...conditions
      }
      this._loadFeeDetailHisData()
    },
    async _loadFeeDetailHisData() {
      try {
        this.loading = true
        const params = {
          page: this.page.current,
          row: this.page.size,
          feeId: this.feeDetailHisInfo.feeId,
          staffNameLike: this.feeDetailHisInfo.staffNameLike,
          feeNameLike: this.feeDetailHisInfo.feeNameLike,
          payerObjName: this.feeDetailHisInfo.payerObjName,
          logStartTime: this.feeDetailHisInfo.logStartTime,
          logEndTime: this.feeDetailHisInfo.logEndTime,
          communityId: getCommunityId() 
        }

        const { data, total } = await queryHisFee(params)
        this.feeDetailHisInfo.fees = data
        this.page.total = total
      } catch (error) {
        console.error('Failed to load fee history:', error)
      } finally {
        this.loading = false
      }
    },
    _getFeeHisOperate(fee) {
      const feeCount = this.feeDetailHisInfo.fees.filter(item => item.bId === fee.bId).length

      if (feeCount <= 1) {
        if (fee.operate === 'ADD') return this.$t('feeDetailHis.add')
        if (fee.operate === 'DEL') return this.$t('feeDetailHis.delete')
        return '-'
      }

      if (fee.operate === 'ADD') return this.$t('feeDetailHis.modifyNew')
      if (fee.operate === 'DEL') return this.$t('feeDetailHis.modifyOld')
      return '-'
    },
    handleSizeChange(size) {
      this.page.size = size
      this._loadFeeDetailHisData()
    },
    handleCurrentChange(current) {
      this.page.current = current
      this._loadFeeDetailHisData()
    }
  }
}
</script>

<style scoped>
.fee-detail-his {
  padding: 20px;
}

.el-pagination {
  margin-top: 20px;
  text-align: right;
}
</style>