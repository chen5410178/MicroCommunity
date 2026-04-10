<template>
  <div class="staff-apply-return-fee-container">
    <el-row :gutter="20">
      <el-col :span="4">
        <el-select v-model="aStaffDetailApplyReturnFeeInfo.state"
          :placeholder="$t('staffDetailApplyReturnFee.selectState')">
          <el-option v-for="(item, index) in aStaffDetailApplyReturnFeeInfo.returnPayFeeStates" :key="index"
            :value="item.statusCd" :label="item.name" />
        </el-select>
      </el-col>
      <el-col :span="4">
        <el-button type="primary" size="small" @click="queryApplyReturnFee">
          {{ $t('common.search') }}
        </el-button>
      </el-col>
    </el-row>
    <el-table :data="aStaffDetailApplyReturnFeeInfo.returnPayFees" border style="width: 100%" class="margin-top">
      <el-table-column prop="returnFeeId" :label="$t('staffDetailApplyReturnFee.returnFeeId')" align="center" />
      <el-table-column prop="detailId" :label="$t('staffDetailApplyReturnFee.detailId')" align="center" />
      <el-table-column prop="feeTypeCdName" :label="$t('staffDetailApplyReturnFee.feeTypeCdName')" align="center" />
      <el-table-column prop="payerObjName" :label="$t('staffDetailApplyReturnFee.payerObjName')" align="center" />
      <el-table-column :label="$t('staffDetailApplyReturnFee.cycles')" align="center">
        <template slot-scope="scope">
          {{ unum(scope.row.cycles) }}
        </template>
      </el-table-column>
      <el-table-column :label="$t('staffDetailApplyReturnFee.receivableAmount')" align="center">
        <template slot-scope="scope">
          {{ unum(scope.row.receivableAmount) }}
        </template>
      </el-table-column>
      <el-table-column :label="$t('staffDetailApplyReturnFee.receivedAmount')" align="center">
        <template slot-scope="scope">
          {{ unum(scope.row.receivedAmount) }}

          <div v-for="(item, index) in scope.row.feeAccountDetailDtoList" :key="index">
            <div v-if="item.state !== '1001'">
              {{ item.stateName }}: {{ item.amount }}
            </div>
          </div>
          <div v-for="(item, index) in scope.row.payFeeDetailDiscountDtoList" :key="index">
            {{ item.discountName }}: {{ Math.abs(item.discountPrice) }}
          </div>
        </template>
      </el-table-column>
      <el-table-column prop="createTime" :label="$t('staffDetailApplyReturnFee.createTime')" align="center" />
      <el-table-column prop="reason" :label="$t('staffDetailApplyReturnFee.reason')" align="center" />
      <el-table-column :label="$t('staffDetailApplyReturnFee.applyPersonName')" align="center">
        <template slot-scope="scope">
          {{ scope.row.applyPersonName || '-' }}
        </template>
      </el-table-column>
      <el-table-column :label="$t('staffDetailApplyReturnFee.stateName')" align="center">
        <template slot-scope="scope">
          <el-tag :type="scope.row.state === '1100' ? 'success' : scope.row.state === '1200' ? 'danger' : 'info'">
            {{ scope.row.stateName }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column :label="$t('staffDetailApplyReturnFee.auditPersonName')" align="center">
        <template slot-scope="scope">
          {{ scope.row.auditPersonName || '-' }}
        </template>
      </el-table-column>
      <el-table-column :label="$t('common.operation')" align="center">
        <template slot-scope="scope">
          <el-button size="mini" type="primary" @click="toReturnFeeDetail(scope.row)">
            {{ $t('common.detail') }}
          </el-button>
        </template>
      </el-table-column>
    </el-table>
    <el-pagination :current-page.sync="pagination.currentPage" :page-sizes="[10, 15, 20, 30]"
      :page-size="pagination.pageSize" :total="pagination.totalRecords" layout="total, sizes, prev, pager, next, jumper"
      @size-change="onPageSizeChange" @current-change="onPageChange" />
  </div>
</template>
  
<script>
import { listAdminReturnPayFees } from '@/api/staff/adminStaffDetailApi'
import {getDict} from '@/api/community/communityApi'


export default {
  name: 'AStaffDetailApplyReturnFee',
  data() {
    return {
      aStaffDetailApplyReturnFeeInfo: {
        returnPayFees: [],
        staffId: '',
        state: '',
        returnPayFeeStates: []
      },
      pagination: {
        currentPage: 1,
        pageSize: 10,
        totalRecords: 0
      }
    }
  },
  created() {
    this.loadDict()
  },
  methods: {
    switchTab(data) {
      this.aStaffDetailApplyReturnFeeInfo.staffId = data.staffId
      this.loadApplyReturnFeeData(1, this.pagination.pageSize)
    },
    async loadDict() {
      try {
        const  data  = await getDict('return_pay_fee', 'state')
        this.aStaffDetailApplyReturnFeeInfo.returnPayFeeStates = data
      } catch (error) {
        this.$message.error(this.$t('staffDetailApplyReturnFee.fetchDictError'))
      }
    },
    async loadApplyReturnFeeData(page, row) {
      try {
        const { data, total } = await listAdminReturnPayFees({
          applyPersonId: this.aStaffDetailApplyReturnFeeInfo.staffId,
          state: this.aStaffDetailApplyReturnFeeInfo.state,
          page,
          row
        })
        this.aStaffDetailApplyReturnFeeInfo.returnPayFees = data.returnPayFees
        this.pagination.totalRecords = total
      } catch (error) {
        this.$message.error(this.$t('staffDetailApplyReturnFee.fetchError'))
      }
    },
    queryApplyReturnFee() {
      this.pagination.currentPage = 1
      this.loadApplyReturnFeeData(this.pagination.currentPage, this.pagination.pageSize)
    },
    toReturnFeeDetail(returnFee) {
      this.$router.push(`/views/fee/adminReturnFeeDetail?returnFeeId=${returnFee.returnFeeId}`)
    },
    unum(value) {
      return value || '-'
    },
    onPageSizeChange(val) {
      this.pagination.pageSize = val
      this.loadApplyReturnFeeData(this.pagination.currentPage, this.pagination.pageSize)
    },
    onPageChange(val) {
      this.pagination.currentPage = val
      this.loadApplyReturnFeeData(this.pagination.currentPage, this.pagination.pageSize)
    }
  }
}
</script>
  
<style lang="scss" scoped>
.staff-apply-return-fee-container {
  padding: 20px;

  .margin-top {
    margin-top: 20px;
  }
}
</style>