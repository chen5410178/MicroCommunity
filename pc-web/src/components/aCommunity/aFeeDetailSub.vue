<template>
  <div>
    <el-row>
      <el-col :span="24" class="text-right">
      </el-col>
    </el-row>
    <el-table :data="aFeeDetailSubInfo.subs" border style="width: 100%" class="margin-top">
      <el-table-column prop="preFeeId" :label="$t('aFeeDetailSub.parentFeeId')" align="center">
        <template slot-scope="scope">
          <router-link :to="{ path: '/pages/fee/adminFeeDetail', query: { feeId: scope.row.preFeeId } }"
            target="_blank">
            {{ scope.row.preFeeId }}
          </router-link>
        </template>
      </el-table-column>
      <el-table-column prop="feeId" :label="$t('aFeeDetailSub.childFeeId')" align="center">
        <template slot-scope="scope">
          <router-link :to="{ path: '/pages/fee/adminFeeDetail', query: { feeId: scope.row.feeId } }" target="_blank">
            {{ scope.row.feeId }}
          </router-link>
        </template>
      </el-table-column>
      <el-table-column prop="feeName" :label="$t('aFeeDetailSub.feeName')" align="center" />
      <el-table-column prop="payerObjName" :label="$t('aFeeDetailSub.feeObject')" align="center" />
      <el-table-column :label="$t('aFeeDetailSub.billingPeriod')" align="center">
        <template slot-scope="scope">
          {{ $dateFormat(scope.row.endTime) }}<br>
          ~{{ $dateFormat(scope.row.maxTime) }}
        </template>
      </el-table-column>
      <el-table-column prop="createTime" :label="$t('aFeeDetailSub.splitTime')" align="center" />
    </el-table>
    <el-row>
      <el-col :span="24" class="float-right">
        <el-pagination :current-page="currentPage" :page-size="pageSize" :total="aFeeDetailSubInfo.total"
          layout="total, prev, pager, next" @current-change="handlePageChange" />
      </el-col>
    </el-row>
  </div>
</template>

<script>
import { listAdminPayFeeSub, listAdminFee } from '@/api/aCommunity/aFeeDetailSubApi'
import { getCommunityId } from '@/api/community/communityApi'

export default {
  name: 'AFeeDetailSub',
  data() {
    return {
      aFeeDetailSubInfo: {
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
  created() {
    this.communityId = getCommunityId()
    this.initEvents()
  },
  methods: {
    open(data) {
      if (!data.feeId) return
      Object.assign(this.aFeeDetailSubInfo, data)
      this.listAFeeDetailSub(1, this.pageSize)
      this.loadAFeeDetailSubFeeInfo()
    },
    initEvents() {
      this.$on('switch', this.handleSwitch)
      this.$on('loadSub', this.handleLoadSub)
    },
    handleSwitch(param) {
      if (!param.feeId) return
      Object.assign(this.aFeeDetailSubInfo, param)
      this.listAFeeDetailSub(1, this.pageSize)
      this.loadAFeeDetailSubFeeInfo()
    },
    handleLoadSub() {
      this.listAFeeDetailSub(this.currentPage, this.pageSize)
      this.loadAFeeDetailSubFeeInfo()
    },
    handlePageChange(currentPage) {
      this.currentPage = currentPage
      this.listAFeeDetailSub(currentPage, this.pageSize)
    },
    async listAFeeDetailSub(page, rows) {
      try {
        const params = {
          page,
          row: rows,
          pcFeeId: this.aFeeDetailSubInfo.feeId,
          communityId: this.communityId
        }
        const res = await listAdminPayFeeSub(params)
        this.aFeeDetailSubInfo.total = res.total
        this.aFeeDetailSubInfo.records = res.records
        this.aFeeDetailSubInfo.subs = res.data
      } catch (error) {
        console.error('获取子费用列表失败:', error)
      }
    },
    async loadAFeeDetailSubFeeInfo() {
      try {
        const params = {
          page: 1,
          row: 1,
          feeId: this.aFeeDetailSubInfo.feeId,
          communityId: this.communityId
        }
        const res = await listAdminFee(params)
        this.aFeeDetailSubInfo.fee = res.fees[0]
      } catch (error) {
        console.error('获取费用信息失败:', error)
      }
    },
    mergeFee(fee) {
      this.$emit('openMergeFeeModal', fee)
    },
    splitPayFee(fee) {
      console.log(fee)
      this.$emit('openSplitFeeModal', this.aFeeDetailSubInfo.fee)
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

.float-right {
  float: right;
}
</style>