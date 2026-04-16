<template>
  <div class="animated fadeInRight ecommerce">
    <el-row>
      <el-col :span="24">
        <el-card class="box-card">
          <div slot="header" class="flex justify-between">
            <span>{{ $t('myItemOutAuditHistoryOrders.title') }}</span>
            <div class="ibox-tools" style="float: right;">
              <el-button type="primary" size="small" @click="goBack()">
                <i class="el-icon-close"></i>
                {{ $t('myItemOutAuditHistoryOrders.back') }}
              </el-button>
              <el-button type="primary" size="small" @click="_queryHistory()">
                <i class="el-icon-refresh"></i>
                {{ $t('myItemOutAuditHistoryOrders.refresh') }}
              </el-button>
            </div>
          </div>
          <div class="ibox-content">
            <el-table
              :data="auditOrderHistorysInfo.auditOrderHistorys"
              border
              style="width: 100%"
            >
              <el-table-column
                prop="applyOrderId"
                :label="$t('myItemOutAuditHistoryOrders.orderId')"
                align="center"
              />
              <el-table-column
                prop="resOrderTypeName"
                :label="$t('myItemOutAuditHistoryOrders.orderType')"
                align="center"
              />
              <el-table-column
                prop="stateName"
                :label="$t('myItemOutAuditHistoryOrders.orderStatus')"
                align="center"
              />
              <el-table-column
                prop="userName"
                :label="$t('myItemOutAuditHistoryOrders.applicant')"
                align="center"
              />
              <el-table-column
                prop="createTime"
                :label="$t('myItemOutAuditHistoryOrders.createTime')"
                align="center"
              />
              <el-table-column
                :label="$t('myItemOutAuditHistoryOrders.operation')"
                align="center"
              >
                <template slot-scope="scope">
                  <el-button
                    size="mini"
                    @click="_openDetailPurchaseApplyModel(scope.row)"
                  >
                    {{ $t('myItemOutAuditHistoryOrders.view') }}
                  </el-button>
                </template>
              </el-table-column>
            </el-table>
            <el-pagination
              :current-page.sync="currentPage"
              :page-size="pageSize"
              :total="auditOrderHistorysInfo.total"
              layout="total, prev, pager, next, jumper"
              @current-change="handlePageChange"
            />
          </div>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import { listItemOutAuditHistoryOrders } from '@/api/resource/myItemOutAuditHistoryOrdersApi'
import { getCommunityId } from '@/api/community/communityApi'

export default {
  name: 'MyItemOutAuditHistoryOrdersList',
  data() {
    return {
      auditOrderHistorysInfo: {
        auditOrderHistorys: [],
        total: 0,
        records: 1,
        moreCondition: false,
        userName: '',
        conditions: {
          AuditOrderHistorysId: '',
          userName: '',
          auditLink: '',
          page: 1,
          row: 10
        },
        orderInfo: ''
      },
      currentPage: 1,
      pageSize: 10,
      communityId: ''
    }
  },
  created() {
    this.communityId = getCommunityId()
    this._listAuditOrderHistorys(this.currentPage, this.pageSize)
  },
  methods: {
    async _listAuditOrderHistorys(page, rows) {
      try {
        this.auditOrderHistorysInfo.conditions.page = page
        this.auditOrderHistorysInfo.conditions.row = rows
        this.auditOrderHistorysInfo.conditions.communityId = this.communityId
        
        const { data, total } = await listItemOutAuditHistoryOrders(
          this.auditOrderHistorysInfo.conditions
        )
        this.auditOrderHistorysInfo.auditOrderHistorys = data
        this.auditOrderHistorysInfo.total = total
      } catch (error) {
        console.error('Failed to fetch audit history orders:', error)
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
    handlePageChange(currentPage) {
      this.currentPage = currentPage
      this._listAuditOrderHistorys(currentPage, this.pageSize)
    },
    goBack() {
      this.$router.go(-1)
    }
  }
}
</script>

<style scoped>
.ibox-tools {
  display: inline-block;
  float: right;
  margin-top: 0;
  position: relative;
  padding: 0;
}
.clearfix:before,
.clearfix:after {
  display: table;
  content: "";
}
.clearfix:after {
  clear: both;
}
.box-card {
  margin-bottom: 20px;
}
</style>