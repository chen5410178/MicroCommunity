<template>
  <div class="pay-fee-audit-manage-container">
    <el-card class="search-card">
      <div slot="header" class="flex justify-between">
        <span>{{ $t('payFeeAuditManage.searchTitle') }}</span>
      </div>
      <el-row :gutter="20">
        <el-col :span="6">
          <el-select v-model="searchForm.payObjType" :placeholder="$t('payFeeAuditManage.selectPayObj')"
            style="width:100%">
            <el-option v-for="item in payObjTypes" :key="item.statusCd" :label="item.name" :value="item.statusCd" />
          </el-select>
        </el-col>
        <el-col :span="6">
          <el-select v-model="searchForm.state" :placeholder="$t('payFeeAuditManage.selectStatus')" style="width:100%">
            <el-option :label="$t('payFeeAuditManage.pendingReview')" value="1010" />
            <el-option :label="$t('payFeeAuditManage.approved')" value="2020" />
            <el-option :label="$t('payFeeAuditManage.rejected')" value="3030" />
          </el-select>
        </el-col>
        <el-col :span="6">
          <el-input v-model="searchForm.payerObjId" :placeholder="searchForm.payObjType === '666'
            ? $t('payFeeAuditManage.inputPlateNumber')
            : $t('payFeeAuditManage.inputRoomInfo')" />
        </el-col>
        <el-col :span="6">
          <el-button type="primary" @click="handleSearch">
            <i class="el-icon-search"></i>
            {{ $t('common.search') }}
          </el-button>
          <el-button @click="handleReset">
            <i class="el-icon-refresh"></i>
            {{ $t('common.reset') }}
          </el-button>
        </el-col>
      </el-row>
    </el-card>

    <el-card class="table-card">
      <div slot="header" class="flex justify-between">
        <span>{{ $t('payFeeAuditManage.tableTitle') }}</span>
        <el-button type="primary" size="small" style="float:right" @click="handleBatchAudit">
          <i class="el-icon-plus"></i>
          {{ $t('payFeeAuditManage.batchAudit') }}
        </el-button>
      </div>

      <el-table :data="tableData" border style="width:100%" @selection-change="handleSelectionChange">
        <el-table-column type="selection" width="55" align="center" />
        <el-table-column prop="payerObjName" :label="$t('payFeeAuditManage.house')" align="center" />
        <el-table-column prop="feeName" :label="$t('payFeeAuditManage.feeItem')" align="center" />
        <el-table-column prop="cycles" :label="$t('payFeeAuditManage.payCycle')" align="center">
          <template slot-scope="scope">
            {{ scope.row.cycles }} {{ $t('payFeeAuditManage.month') }}
          </template>
        </el-table-column>
        <el-table-column prop="startTime" :label="$t('payFeeAuditManage.startTime')" align="center" />
        <el-table-column prop="endTime" :label="$t('payFeeAuditManage.endTime')" align="center" />
        <el-table-column prop="receivableAmount" :label="$t('payFeeAuditManage.receivableAmount')" align="center" />
        <el-table-column prop="receivedAmount" :label="$t('payFeeAuditManage.receivedAmount')" align="center" />
        <el-table-column prop="userName" :label="$t('payFeeAuditManage.operator')" align="center" />
        <el-table-column prop="createTime" :label="$t('payFeeAuditManage.payTime')" align="center" />
        <el-table-column prop="state" :label="$t('payFeeAuditManage.auditStatus')" align="center">
          <template slot-scope="scope">
            <el-tag :type="getStatusTagType(scope.row.state)">
              {{ getStatusText(scope.row.state) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="message" :label="$t('payFeeAuditManage.auditRemark')" align="center" />
        <el-table-column prop="remark" :label="$t('payFeeAuditManage.payRemark')" align="center" />
        <el-table-column :label="$t('common.operation')" align="center" width="200">
          <template slot-scope="scope">
            <el-button type="text" size="small" @click="handleDetail(scope.row)">
              {{ $t('common.detail') }}
            </el-button>
            <el-button v-if="scope.row.state !== '2020' && scope.row.state !== '3030'" type="text" size="small"
              @click="handleAudit(scope.row)">
              {{ $t('payFeeAuditManage.auditFee') }}
            </el-button>
            <el-button v-if="scope.row.state === '3030'" type="text" size="small" @click="handleRefund(scope.row)">
              {{ $t('payFeeAuditManage.refund') }}
            </el-button>
          </template>
        </el-table-column>
      </el-table>

      <div class="pagination-wrapper">
        <div class="tip-text">
          {{ $t('payFeeAuditManage.auditTip') }}
        </div>
        <el-pagination :current-page="pagination.current" :page-sizes="[10, 20, 30, 50]" :page-size="pagination.size"
          :total="pagination.total" layout="total, sizes, prev, pager, next, jumper" @size-change="handleSizeChange"
          @current-change="handleCurrentChange" />
      </div>
    </el-card>

    <audit-modal ref="auditModal" @success="handleAuditSuccess" />
    <return-pay-fee-modal ref="returnPayFeeModal" @success="handleRefundSuccess" />
  </div>
</template>

<script>
import { getPayFeeAuditList, savePayFeeAudit } from '@/api/fee/payFeeAuditManageApi'
import { getDict } from '@/api/community/communityApi'
import { getCommunityId } from '@/api/community/communityApi'
import AuditModal from '@/components/fee/audit'
import ReturnPayFeeModal from '@/components/fee/returnPayFee'

export default {
  name: 'PayFeeAuditManageList',
  components: {
    AuditModal,
    ReturnPayFeeModal
  },
  data() {
    return {
      searchForm: {
        communityId: '',
        payObjType: '',
        state: '1010',
        payerObjId: ''
      },
      tableData: [],
      multipleSelection: [],
      payObjTypes: [],
      pagination: {
        current: 1,
        size: 10,
        total: 0
      },
      currentRow: null
    }
  },
  created() {
    this.searchForm.communityId = getCommunityId()
    this.getList()
    this.getPayObjTypes()
  },
  methods: {
    async getList() {
      try {
        const params = {
          ...this.searchForm,
          page: this.pagination.current,
          row: this.pagination.size
        }
        const { data, total } = await getPayFeeAuditList(params)
        this.tableData = data
        this.pagination.total = total
      } catch (error) {
        this.$message.error(this.$t('payFeeAuditManage.fetchError'))
      }
    },
    async getPayObjTypes() {
      try {
        this.payObjTypes = await getDict('pay_fee', 'payer_obj_type')
      } catch (error) {
        console.error('获取付费对象类型失败:', error)
      }
    },
    handleSearch() {
      this.pagination.current = 1
      this.getList()
    },
    handleReset() {
      this.searchForm = {
        communityId: getCommunityId(),
        payObjType: '',
        state: '1010',
        payerObjId: ''
      }
      this.handleSearch()
    },
    handleSizeChange(val) {
      this.pagination.size = val
      this.getList()
    },
    handleCurrentChange(val) {
      this.pagination.current = val
      this.getList()
    },
    handleSelectionChange(val) {
      this.multipleSelection = val
    },
    handleDetail(row) {
      this.$router.push({
        path: '/views/fee/propertyFee',
        query: row
      })
    },
    handleAudit(row) {
      this.currentRow = row
      this.$refs.auditModal.open()
    },
    handleBatchAudit() {
      if (this.multipleSelection.length === 0) {
        this.$message.warning(this.$t('payFeeAuditManage.selectFeeTip'))
        return
      }
      this.currentRow = null
      this.$refs.auditModal.open()
    },
    handleRefund(row) {
      this.$refs.returnPayFeeModal.open(row)
    },
    async handleAuditSuccess(auditInfo) {
      try {
        const params = {
          state: auditInfo.state === '1100' ? '2020' : '3030',
          message: auditInfo.remark,
          feeDetailId: this.currentRow
            ? this.currentRow.detailId
            : this.multipleSelection.map(item => item.detailId).join(','),
          communityId: this.searchForm.communityId
        }
        await savePayFeeAudit(params)
        this.$message.success(this.$t('common.operationSuccess'))
        this.getList()
      } catch (error) {
        this.$message.error(error.message || this.$t('common.operateFailed'))
      }
    },
    handleRefundSuccess() {
      this.getList()
    },
    getStatusTagType(state) {
      switch (state) {
        case '2020': return 'success'
        case '3030': return 'danger'
        default: return 'info'
      }
    },
    getStatusText(state) {
      switch (state) {
        case '2020': return this.$t('payFeeAuditManage.approved')
        case '3030': return this.$t('payFeeAuditManage.rejected')
        default: return this.$t('payFeeAuditManage.pendingReview')
      }
    }
  }
}
</script>

<style lang="scss" scoped>
.pay-fee-audit-manage-container {
  padding: 20px;

  .search-card {
    margin-bottom: 20px;
  }

  .pagination-wrapper {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-top: 20px;

    .tip-text {
      color: #999;
      font-size: 12px;
    }
  }
}
</style>