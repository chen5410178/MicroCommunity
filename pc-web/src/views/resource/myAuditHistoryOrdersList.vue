<template>
  <div class="my-audit-history-orders animated fadeInRight">
    <el-card class="box-card">
      <div slot="header" class="flex justify-between">
        <span>{{ $t('myAuditHistoryOrders.title') }}</span>
        <div class="header-buttons">
          <el-button type="primary" size="small" @click="goBack()">
            <i class="el-icon-close"></i>
            {{ $t('myAuditHistoryOrders.back') }}
          </el-button>
          <el-button type="primary" size="small" @click="_queryHistory">
            <i class="el-icon-refresh"></i>
            {{ $t('myAuditHistoryOrders.refresh') }}
          </el-button>
        </div>
      </div>

      <el-table :data="auditOrderHistorysInfo.auditOrderHistorys" border style="width: 100%" v-loading="loading">
        <el-table-column prop="applyOrderId" :label="$t('myAuditHistoryOrders.orderNumber')" align="center" />
        <el-table-column prop="resOrderTypeName" :label="$t('myAuditHistoryOrders.orderType')" align="center" />
        <el-table-column prop="stateName" :label="$t('myAuditHistoryOrders.orderStatus')" align="center" />
        <el-table-column prop="userName" :label="$t('myAuditHistoryOrders.applicant')" align="center" />
        <el-table-column prop="createTime" :label="$t('myAuditHistoryOrders.createTime')" align="center" />
        <el-table-column :label="$t('myAuditHistoryOrders.operation')" align="center" width="150">
          <template slot-scope="scope">
            <el-button size="mini" @click="_openDetailPurchaseApplyModel(scope.row)">
              {{ $t('myAuditHistoryOrders.view') }}
            </el-button>
          </template>
        </el-table-column>
      </el-table>

      <el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange" :current-page="currentPage"
        :page-sizes="[10, 20, 30, 50]" :page-size="pageSize" layout="total, sizes, prev, pager, next, jumper"
        :total="auditOrderHistorysInfo.total" class="pagination" />
    </el-card>
  </div>
</template>

<script>
import { listAuditHistoryOrders } from '@/api/resource/myAuditHistoryOrdersApi'

export default {
  name: 'MyAuditHistoryOrdersList',
  data() {
    return {
      loading: false,
      currentPage: 1,
      pageSize: 10,
      auditOrderHistorysInfo: {
        auditOrderHistorys: [],
        total: 0,
        conditions: {
          userName: '',
          auditLink: ''
        }
      }
    }
  },
  created() {
    this._listAuditOrderHistorys(this.currentPage, this.pageSize)
  },
  methods: {
    async _listAuditOrderHistorys(page, size) {
      try {
        this.loading = true
        const params = {
          page: page,
          row: size,
          ...this.auditOrderHistorysInfo.conditions
        }
        const { data, total } = await listAuditHistoryOrders(params)
        this.auditOrderHistorysInfo.auditOrderHistorys = data
        this.auditOrderHistorysInfo.total = total
      } catch (error) {
        console.error('获取审核历史订单失败:', error)
      } finally {
        this.loading = false
      }
    },
    _queryHistory() {
      this.currentPage = 1
      this._listAuditOrderHistorys(this.currentPage, this.pageSize)
    },
    _openDetailPurchaseApplyModel(purchaseApply) {
      this.$router.push({
        path: '/pages/common/purchaseApplyDetail',
        query: {
          applyOrderId: purchaseApply.applyOrderId,
          resOrderType: purchaseApply.resOrderType
        }
      })
    },
    handleSizeChange(val) {
      this.pageSize = val
      this._listAuditOrderHistorys(this.currentPage, this.pageSize)
    },
    handleCurrentChange(val) {
      this.currentPage = val
      this._listAuditOrderHistorys(this.currentPage, this.pageSize)
    },
    goBack() {
      this.$router.go(-1)
    }
  }
}
</script>

<style lang="scss" scoped>
.my-audit-history-orders {
  padding: 20px;

  .box-card {
    margin-bottom: 20px;
  }

  .header-buttons {
    float: right;
  }

  .pagination {
    margin-top: 20px;
    text-align: right;
  }

  .el-table {
    margin-top: 20px;
  }
}
</style>