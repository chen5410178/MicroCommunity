<template>
  <div class="refund-deposit-fee-container">
    <el-row :gutter="20">
      <el-col :span="4">
        <el-card class="state-card">
          <ul class="state-list">
            <li v-for="(item, index) in refundDepositFeeInfo.states" :key="index" @click="switchDepositState(item)"
              :class="{ 'active-state': refundDepositFeeInfo.state === item.state }">
              {{ item.name }}
            </li>
          </ul>
        </el-card>
      </el-col>
      <el-col :span="20">
        <el-card>
          <div slot="header" class="flex justify-between">
            <span>{{ $t('refundDepositFee.title') }}</span>
            <div class="card-header-actions">
              <el-button size="small" @click="printRefundFeeReceipt">
                {{ $t('refundDepositFee.printReceipt') }}
              </el-button>
              <el-button size="small" @click="$router.go(-1)">
                {{ $t('common.back') }}
              </el-button>
            </div>
          </div>

          <el-table :data="refundDepositFeeInfo.fees" border style="width: 100%">
            <el-table-column prop="payerObjName" :label="$t('payFeeDeposit.payerObject')" align="center" />
            <el-table-column prop="feeName" :label="$t('payFeeDeposit.feeItem')" align="center" />
            <el-table-column :label="$t('payFeeDeposit.timePeriod')" align="center">
              <template slot-scope="scope">
                {{ scope.row.startTime }}~{{ scope.row.endTime }}
              </template>
            </el-table-column>
            <el-table-column prop="receivedAmount" :label="$t('payFeeDeposit.amount')" align="center" />
            <el-table-column prop="createTime" :label="$t('payFeeDeposit.paymentTime')" align="center" />
            <el-table-column prop="stateName" :label="$t('payFeeDeposit.status')" align="center" />
            <el-table-column :label="$t('payFeeDeposit.operation')" align="center" width="180">
              <template slot-scope="scope">
                <el-button v-if="scope.row.state === '1400'" size="mini" type="primary"
                  @click="openRefundModel(scope.row)">
                  {{ $t('payFeeDeposit.refundDeposit') }}
                </el-button>
                <el-button size="mini" @click="toFeeDetail(scope.row)">
                  {{ $t('payFeeDeposit.detail') }}
                </el-button>
              </template>
            </el-table-column>
          </el-table>

          <el-pagination :current-page.sync="pagination.current" :page-sizes="[10, 20, 30, 50]"
            :page-size="pagination.size" :total="pagination.total" layout="total, sizes, prev, pager, next, jumper"
            @size-change="handleSizeChange" @current-change="handleCurrentChange" />
        </el-card>
      </el-col>
    </el-row>

    <apply-refund-deposit ref="applyRefundDeposit" @success="handleSuccess" />
  </div>
</template>

<script>
import { getCommunityId } from '@/api/community/communityApi'
import { queryFeeDeposit } from '@/api/fee/refundDepositFeeApi'
import applyRefundDeposit from '@/components/fee/applyRefundDeposit'

export default {
  name: 'RefundDepositFeeList',
  components: {
    applyRefundDeposit
  },
  data() {
    return {
      refundDepositFeeInfo: {
        fees: [],
        states: [
          { name: this.$t('refundDepositFee.unrefunded'), state: '1400' },
          { name: this.$t('refundDepositFee.refunded'), state: '1100' },
          { name: this.$t('refundDepositFee.pendingReview'), state: '1000' }
        ],
        state: '1400',
        roomId: '',
        ownerId: ''
      },
      pagination: {
        current: 1,
        size: 10,
        total: 0
      },
      communityId: ''
    }
  },
  created() {
    this.communityId = getCommunityId()
    this.refundDepositFeeInfo.roomId = this.$route.query.roomId
    this.refundDepositFeeInfo.ownerId = this.$route.query.ownerId
    this.listFeeDeposit()
  },
  methods: {
    async listFeeDeposit() {
      try {
        const params = {
          page: this.pagination.current,
          row: this.pagination.size,
          payerObjId: this.refundDepositFeeInfo.roomId,
          ownerId: this.refundDepositFeeInfo.ownerId,
          communityId: this.communityId,
          state: this.refundDepositFeeInfo.state
        }
        const { data, total } = await queryFeeDeposit(params)
        this.refundDepositFeeInfo.fees = data
        this.pagination.total = total
      } catch (error) {
        this.$message.error(this.$t('common.fetchError'))
      }
    },
    switchDepositState(state) {
      this.refundDepositFeeInfo.state = state.state
      this.pagination.current = 1
      this.listFeeDeposit()
    },
    toFeeDetail(fee) {
      this.$router.push(`/views/fee/propertyFee?feeId=${fee.feeId}`)
    },
    printRefundFeeReceipt() {
      window.open(`/#/views/owner/ownerDetail?ownerId=${this.refundDepositFeeInfo.ownerId}&currentTab=ownerDetailReceipt`)
    },
    openRefundModel(fee) {
      const feeData = {
        ...fee,
        mainFeeInfo: {
          feeId: fee.feeId,
          feeTypeCd: fee.feeTypeCd,
          configId: fee.configId
        },
        cycles: 1,
        receivableAmount: fee.receivedAmount,
        communityId: this.communityId
      }
      this.$refs.applyRefundDeposit.open(feeData)
    },
    handleSuccess() {
      this.listFeeDeposit()
    },
    handleSizeChange(val) {
      this.pagination.size = val
      this.listFeeDeposit()
    },
    handleCurrentChange(val) {
      this.pagination.current = val
      this.listFeeDeposit()
    }
  }
}
</script>

<style lang="scss" scoped>
.refund-deposit-fee-container {
  padding: 20px;

  .state-card {
    height: 100%;

    .state-list {
      list-style: none;
      padding: 0;
      margin: 0;

      li {
        padding: 10px;
        text-align: center;
        cursor: pointer;
        margin-bottom: 5px;
        border-radius: 4px;

        &:hover {
          background-color: #f5f7fa;
        }

        &.active-state {
          background-color: #409eff;
          color: white;
        }
      }
    }
  }

  .card-header-actions {
    float: right;
  }

  .el-pagination {
    margin-top: 20px;
    text-align: right;
  }
}
</style>