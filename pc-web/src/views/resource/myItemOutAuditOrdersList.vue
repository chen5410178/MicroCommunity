<template>
  <div class="animated fadeInRight ecommerce padding">
    <el-row>
      <el-col :span="24">
        <el-card class="box-card">
          <div slot="header" class="flex justify-between">
            <div>{{ $t('myItemOutAuditOrders.title') }}</div>
            <div class="ibox-tools">
              <el-button type="primary" size="small" @click="goBack">
                <i class="el-icon-close"></i>
                {{ $t('myItemOutAuditOrders.back') }}
              </el-button>
              <el-button type="primary" size="small" @click="_queryAuditOrdersMethod">
                <i class="el-icon-refresh"></i>
                {{ $t('myItemOutAuditOrders.refresh') }}
              </el-button>
            </div>
          </div>
          <div class="">
            <el-table :data="auditOrdersInfo.auditOrders" style="width: 100%" border stripe>
              <el-table-column prop="applyOrderId" :label="$t('myItemOutAuditOrders.orderNo')" align="center" />
              <el-table-column prop="resOrderTypeName" :label="$t('myItemOutAuditOrders.orderType')" align="center" />
              <el-table-column prop="stateName" :label="$t('myItemOutAuditOrders.orderStatus')" align="center" />
              <el-table-column prop="userName" :label="$t('myItemOutAuditOrders.applicant')" align="center" />
              <el-table-column prop="createTime" :label="$t('myItemOutAuditOrders.createTime')" align="center" />
              <el-table-column :label="$t('myItemOutAuditOrders.operation')" align="center" width="300">
                <template slot-scope="scope">
                  <el-button-group>
                    <el-button size="mini" @click="_openDetailPurchaseApplyModel(scope.row)">
                      {{ $t('myItemOutAuditOrders.view') }}
                    </el-button>
                    <el-button v-if="scope.row.createUserId === auditOrdersInfo.currentUserId" size="mini"
                      @click="_openEditPurchaseModel(scope.row)">
                      {{ $t('myItemOutAuditOrders.edit') }}
                    </el-button>
                    <el-button v-if="scope.row.curTaskName === '仓库管理员'" size="mini"
                      @click="_distributionOrder(scope.row)">
                      {{ $t('myItemOutAuditOrders.itemDistribution') }}
                    </el-button>
                    <el-button v-else size="mini" @click="_openAuditOrderModel(scope.row)">
                      {{ $t('myItemOutAuditOrders.audit') }}
                    </el-button>
                  </el-button-group>
                </template>
              </el-table-column>
            </el-table>
            <el-pagination :current-page.sync="currentPage" :page-size="pageSize" :total="total"
              layout="total, sizes, prev, pager, next, jumper" @size-change="handleSizeChange"
              @current-change="handleCurrentChange" />
          </div>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import { getCollectionAuditOrder } from '@/api/resource/myItemOutAuditOrdersApi'
import { getCommunityId } from '@/api/community/communityApi'
import { getUserId } from '@/api/user/userApi'

export default {
  name: 'MyItemOutAuditOrdersList',
  data() {
    return {
      DEFAULT_PAGE: 1,
      DEFAULT_ROWS: 10,
      currentPage: 1,
      pageSize: 10,
      total: 0,
      auditOrdersInfo: {
        auditOrders: [],
        total: 0,
        records: 1,
        moreCondition: false,
        userName: '',
        currentUserId: '',
        conditions: {
          AuditOrdersId: '',
          userName: '',
          auditLink: '',
          page: 1,
          row: 10
        },
        orderInfo: '',
        procure: false,
        audit: '1'
      }
    }
  },
  created() {
    this.auditOrdersInfo.currentUserId = getUserId()
    this._listAuditOrders(this.DEFAULT_PAGE, this.DEFAULT_ROWS)
  },
  methods: {
    async _listAuditOrders(page, rows) {
      this.auditOrdersInfo.audit = '1'
      this.auditOrdersInfo.conditions.page = page
      this.auditOrdersInfo.conditions.row = rows
      const params = {
        ...this.auditOrdersInfo.conditions,
        communityId: getCommunityId()
      }

      try {
        const { data, total } = await getCollectionAuditOrder(params)
        this.auditOrdersInfo.total = total
        this.auditOrdersInfo.records = total
        this.auditOrdersInfo.auditOrders = data
        this.total = total
      } catch (error) {
        console.error('请求失败:', error)
      }
    },
    _openAuditOrderModel(auditOrder) {
      this.$router.push({
        path: '/views/resource/purchaseApplyDetail',
        query: {
          applyOrderId: auditOrder.applyOrderId,
          resOrderType: auditOrder.resOrderType,
          flowId: auditOrder.flowId,
          action: 'audit',
          taskId: auditOrder.taskId
        }
      })
    },
    _queryAuditOrdersMethod() {
      this._listAuditOrders(this.DEFAULT_PAGE, this.DEFAULT_ROWS)
    },
    _openDetailPurchaseApplyModel(purchaseApply) {
      this.$router.push({
        path: '/views/resource/purchaseApplyDetail',
        query: {
          applyOrderId: purchaseApply.applyOrderId,
          resOrderType: purchaseApply.resOrderType
        }
      })
    },
    _openEditPurchaseModel(purchaseApply) {
      this.$router.push({
        path: '/pages/resource/editPurchaseApply',
        query: {
          applyOrderId: purchaseApply.applyOrderId,
          resOrderType: purchaseApply.resOrderType
        }
      })
    },
    _distributionOrder(purchaseApply) {
      this.$router.push({
        path: '/pages/admin/resourceOutManage',
        query: {
          applyOrderId: purchaseApply.applyOrderId,
          resOrderType: purchaseApply.resOrderType,
          taskId: purchaseApply.taskId,
          flowId: purchaseApply.flowId
        }
      })
    },
    goBack() {
      this.$router.go(-1)
    },
    handleSizeChange(val) {
      this.pageSize = val
      this._listAuditOrders(this.currentPage, val)
    },
    handleCurrentChange(val) {
      this.currentPage = val
      this._listAuditOrders(val, this.pageSize)
    }
  }
}
</script>

<style scoped>
.ibox-tools {
  float: right;
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

.el-pagination {
  margin-top: 15px;
  text-align: right;
}
</style>