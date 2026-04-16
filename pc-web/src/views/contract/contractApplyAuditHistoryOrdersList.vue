<template>
  <div class="contract-apply-audit-history-orders-container animated fadeInRight">
    <el-card class="box-card">
      <div slot="header" class="flex justify-between">
        <span>{{ $t('contractApplyAuditHistoryOrders.title') }}</span>
      </div>

      <el-table v-loading="loading" :data="contractApplyAuditOrdersInfo.contractApplyAuditOrders" border
        style="width: 100%">
        <el-table-column prop="contractCode" :label="$t('contractApplyAuditHistoryOrders.contractCode')"
          align="center" />
        <el-table-column prop="contractName" :label="$t('contractApplyAuditHistoryOrders.contractName')"
          align="center" />
        <el-table-column prop="contractTypeName" :label="$t('contractApplyAuditHistoryOrders.contractType')"
          align="center" />
        <el-table-column prop="stateName" :label="$t('contractApplyAuditHistoryOrders.contractStatus')"
          align="center" />
        <el-table-column prop="signingTime" :label="$t('contractApplyAuditHistoryOrders.createTime')" align="center" />
        <el-table-column :label="$t('contractApplyAuditHistoryOrders.operation')" align="center" width="150">
          <template slot-scope="scope">
            <el-button size="mini" type="primary" @click="_openDetailPurchaseApplyModel(scope.row)">
              {{ $t('contractApplyAuditHistoryOrders.view') }}
            </el-button>
          </template>
        </el-table-column>
      </el-table>

      <el-pagination :current-page.sync="page.current" :page-sizes="[10, 20, 30, 50]" :page-size="page.size"
        :total="page.total" layout="total, sizes, prev, pager, next, jumper" @size-change="handleSizeChange"
        @current-change="handleCurrentChange" />
    </el-card>

    <!-- <audit-component ref="auditComponent" @notifyAudit="_auditOrderInfo" /> -->
  </div>
</template>

<script>
import { queryContractHistoryTask } from '@/api/contract/contractApplyAuditHistoryOrdersApi'
// import AuditComponent from '@/components/contract/audit'

export default {
  name: 'ContractApplyAuditHistoryOrdersList',
  components: {
    //AuditComponent
  },
  data() {
    return {
      loading: false,
      contractApplyAuditOrdersInfo: {
        contractApplyAuditOrders: [],
        conditions: {
          AuditOrdersId: '',
          userName: '',
          auditLink: ''
        }
      },
      page: {
        current: 1,
        size: 10,
        total: 0
      }
    }
  },
  created() {
    this._listAuditOrders(this.page.current, this.page.size)
  },
  methods: {
    async _listAuditOrders(page, size) {
      try {
        this.loading = true
        const params = {
          page,
          row: size,
          ...this.contractApplyAuditOrdersInfo.conditions
        }
        const { data, total } = await queryContractHistoryTask(params)
        this.contractApplyAuditOrdersInfo.contractApplyAuditOrders = data
        this.page.total = total
      } catch (error) {
        this.$message.error(this.$t('contractApplyAuditHistoryOrders.fetchError'))
      } finally {
        this.loading = false
      }
    },
    _openDetailPurchaseApplyModel(auditOrder) {
      this.$router.push({
        path: '/pages/common/contractApplyDetail',
        query: { contractId: auditOrder.contractId }
      })
    },
    _openAuditOrderModel(auditOrder) {
      this.contractApplyAuditOrdersInfo.orderInfo = auditOrder
      this.$refs.auditComponent.open()
    },
    _auditOrderInfo(auditInfo) {
      // 处理审核信息
      console.log(auditInfo)
    },
    handleSizeChange(val) {
      this.page.size = val
      this._listAuditOrders(this.page.current, val)
    },
    handleCurrentChange(val) {
      this._listAuditOrders(val, this.page.size)
    }
  }
}
</script>

<style lang="scss" scoped>
.contract-apply-audit-history-orders-container {
  padding: 20px;

  .box-card {
    margin-bottom: 20px;
  }

  .el-pagination {
    margin-top: 20px;
    text-align: right;
  }
}
</style>