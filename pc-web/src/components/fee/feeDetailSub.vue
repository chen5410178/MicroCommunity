<template>
  <div>
    <el-row>
      <el-col :span="24" class="text-right">
        <el-button type="primary" size="small" style="margin-left: 10px"
          v-if="hasPrivilege('502020090427190001') && feeDetailSubInfo.fee.state === '2008001'" @click="_splitPayFee">
          {{ $t('feeDetailSub.split') }}
        </el-button>
      </el-col>
    </el-row>

    <el-table :data="feeDetailSubInfo.subs" border style="width: 100%" class="margin-top">
      <el-table-column prop="preFeeId" :label="$t('feeDetailSub.parentFeeId')" align="center">
        <template slot-scope="scope">
          <router-link :to="`/views/fee/feeDetail?feeId=${scope.row.preFeeId}`" target="_blank">
            {{ scope.row.preFeeId }}
          </router-link>
        </template>
      </el-table-column>
      <el-table-column prop="feeId" :label="$t('feeDetailSub.childFeeId')" align="center">
        <template slot-scope="scope">
          <router-link :to="`/views/fee/feeDetail?feeId=${scope.row.feeId}`" target="_blank">
            {{ scope.row.feeId }}
          </router-link>
        </template>
      </el-table-column>
      <el-table-column prop="feeName" :label="$t('feeDetailSub.feeName')" align="center" />
      <el-table-column prop="payerObjName" :label="$t('feeDetailSub.feeObject')" align="center" />
      <el-table-column :label="$t('feeDetailSub.billingPeriod')" align="center">
        <template slot-scope="scope">
          {{ dateFormat(new Date(scope.row.endTime)) }}<br />
          ~{{ dateFormat(new Date(scope.row.maxTime)) }}
        </template>
      </el-table-column>
      <el-table-column prop="createTime" :label="$t('feeDetailSub.splitTime')" align="center" />
      <el-table-column :label="$t('feeDetailSub.operation')" align="center">
        <template slot-scope="scope">
          <el-button size="mini" @click="_mergeFee(scope.row)">
            {{ $t('feeDetailSub.restoreMerge') }}
          </el-button>
        </template>
      </el-table-column>
    </el-table>

    <el-row>
      <el-col :span="24" class="text-right">
        <el-pagination @current-change="handleCurrentChange" :current-page="currentPage" :page-size="pageSize"
          :total="feeDetailSubInfo.total" layout="total, prev, pager, next" />
      </el-col>
    </el-row>

    <merge-fee ref="mergeFee" @merge-success="loadData" />
    <split-fee ref="splitFee" @split-success="loadData" />
  </div>
</template>

<script>
import { listPayFeeSub, listFee } from '@/api/fee/feeDetailSubApi'
import { getCommunityId } from '@/api/community/communityApi'
import MergeFee from './mergeFee'
import SplitFee from './splitFee'
import { dateFormat } from '@/utils/dateUtil'

export default {
  name: 'FeeDetailSub',
  components: {
    MergeFee,
    SplitFee
  },
  data() {
    return {
      feeDetailSubInfo: {
        subs: [],
        feeId: '',
        total: 0,
        records: 0,
        fee: {}
      },
      currentPage: 1,
      pageSize: 10,
      communityId: ''
    }
  },
  methods: {
    open(params) {
      if (!params.feeId) return
      Object.assign(this.feeDetailSubInfo, params)
      this.communityId = getCommunityId()
      this.loadData()
    },
    async loadData() {
      await this._listFeeDetailSub(this.currentPage, this.pageSize)
      await this._loadFeeDetailSubFeeInfo()
    },
    async _listFeeDetailSub(page, rows) {
      try {
        const params = {
          page,
          row: rows,
          pcFeeId: this.feeDetailSubInfo.feeId,
          communityId: this.communityId
        }
        const res = await listPayFeeSub(params)
        this.feeDetailSubInfo.total = res.total
        this.feeDetailSubInfo.records = res.records
        this.feeDetailSubInfo.subs = res.data
        this.currentPage = page
      } catch (error) {
        console.error('Failed to load fee sub list:', error)
      }
    },
    async _loadFeeDetailSubFeeInfo() {
      try {
        const params = {
          page: 1,
          row: 1,
          feeId: this.feeDetailSubInfo.feeId,
          communityId: this.communityId
        }
        const res = await listFee(params)
        this.feeDetailSubInfo.fee = res.fees[0] || {}
      } catch (error) {
        console.error('Failed to load fee info:', error)
      }
    },
    _mergeFee(fee) {
      this.$refs.mergeFee.open(fee)
    },
    _splitPayFee() {
      this.$refs.splitFee.open(this.feeDetailSubInfo.fee)
    },
    handleCurrentChange(page) {
      this._listFeeDetailSub(page, this.pageSize)
    },
    dateFormat
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