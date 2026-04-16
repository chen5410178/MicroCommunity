<template>
  <div class="animated fadeInRight ecommerce">
    <el-row>
      <el-col :span="24">
        <el-card>
          <div slot="header" class="flex justify-between">
            <div>{{ $t('myAuditOrders.todoList') }}</div>
            <div class="ibox-tools" style="float: right;">
              <el-button type="primary" size="small" @click="goBack">
                <i class="el-icon-close"></i>
                {{ $t('myAuditOrders.back') }}
              </el-button>
              <el-button type="primary" size="small" @click="_queryAuditOrdersMethod">
                <i class="el-icon-refresh"></i>
                {{ $t('myAuditOrders.refresh') }}
              </el-button>
            </div>
          </div>
          <div class="">
            <el-table :data="auditOrdersInfo.auditOrders" style="width: 100%" border stripe
              :default-sort="{ prop: 'createTime', order: 'descending' }">
              <el-table-column prop="applyOrderId" :label="$t('myAuditOrders.orderNumber')"
                align="center"></el-table-column>
              <el-table-column prop="resOrderTypeName" :label="$t('myAuditOrders.orderType')"
                align="center"></el-table-column>
              <el-table-column prop="stateName" :label="$t('myAuditOrders.orderStatus')"
                align="center"></el-table-column>
              <el-table-column prop="userName" :label="$t('myAuditOrders.applicant')" align="center"></el-table-column>
              <el-table-column prop="createTime" :label="$t('myAuditOrders.createTime')" align="center"
                sortable></el-table-column>
              <el-table-column :label="$t('myAuditOrders.operation')" align="center" width="300">
                <template slot-scope="scope">
                  <el-button-group>
                    <el-button size="mini" @click="_openDetailPurchaseApplyModel(scope.row)">
                      {{ $t('myAuditOrders.view') }}
                    </el-button>
                    <el-button size="mini" v-if="scope.row.createUserId == auditOrdersInfo.currentUserId"
                      @click="_openEditPurchaseModel(scope.row)">
                      {{ $t('myAuditOrders.edit') }}
                    </el-button>
                    <el-button size="mini" v-if="scope.row.curTaskName == '仓库管理员'"
                      @click="_procureEnterOrder(scope.row)">
                      {{ $t('myAuditOrders.procurement') }}
                    </el-button>
                    <el-button size="mini" v-else @click="_openAuditOrderModel(scope.row)">
                      {{ $t('myAuditOrders.audit') }}
                    </el-button>
                  </el-button-group>
                </template>
              </el-table-column>
            </el-table>
            <el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange"
              :current-page="currentPage" :page-sizes="[10, 20, 30, 50]" :page-size="pageSize"
              layout="total, sizes, prev, pager, next, jumper" :total="total" style="margin-top: 20px;"></el-pagination>
          </div>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import { listAuditOrders, listWorkflowStepStaffs } from '@/api/resource/myAuditOrdersApi'
//import { getCommunityId } from '@/api/community/communityApi'

export default {
  name: 'MyAuditOrdersList',
  data() {
    return {
      DEFAULT_PAGE: 1,
      DEFAULT_ROWS: 10,
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
      },
      currentPage: 1,
      pageSize: 10,
      total: 0
    }
  },
  created() {
    this._initMethod()
  },
  mounted() {
    this._initEvent()
  },
  methods: {
    _initMethod() {
      this._listAuditOrders(this.DEFAULT_PAGE, this.DEFAULT_ROWS)
      this._loadStepStaff()
    },
    _initEvent() {

    },
    async _listAuditOrders(_page, _rows) {
      this.auditOrdersInfo.audit = '1'
      this.auditOrdersInfo.conditions.page = _page
      this.auditOrdersInfo.conditions.row = _rows
      try {
        const res = await listAuditOrders(this.auditOrdersInfo.conditions)
        this.auditOrdersInfo.total = res.total
        this.auditOrdersInfo.records = res.records
        this.auditOrdersInfo.auditOrders = res.data
        console.log(res)
        this.total = res.total
        this.currentPage = _page
      } catch (error) {
        console.error('请求失败:', error)
      }
    },
    _openAuditOrderModel(_purchaseApply) {
      this.$router.push({
        path: '/views/resource/purchaseApplyDetail',
        query: {
          applyOrderId: _purchaseApply.applyOrderId,
          resOrderType: _purchaseApply.resOrderType,
          action: 'audit',
          taskId: _purchaseApply.taskId,
          flowId: _purchaseApply.flowId
        }
      })
    },
    _queryAuditOrdersMethod() {
      this._listAuditOrders(this.DEFAULT_PAGE, this.DEFAULT_ROWS)
    },
    _openDetailPurchaseApplyModel(_purchaseApply) {
      this.$router.push({
        path: '/views/resource/purchaseApplyDetail',
        query: {
          applyOrderId: _purchaseApply.applyOrderId,
          resOrderType: _purchaseApply.resOrderType
        }
      })
    },
    async _loadStepStaff() {
      try {
        const res = await listWorkflowStepStaffs({
          page: 1,
          row: 1,
          staffId: this.auditOrdersInfo.currentUserId,
          staffRole: '2002',
          requestType: 'purchaseHandle'
        })
        if (res.data.length > 0) {
          this.auditOrdersInfo.procure = true
        }
      } catch (error) {
        console.error('请求失败:', error)
      }
    },
    _procureEnterOrder(_purchaseApply) {
      this.$router.push({
        path: '/views/resource/resourceEnterManage',
        query: {
          applyOrderId: _purchaseApply.applyOrderId,
          resOrderType: _purchaseApply.resOrderType,
          taskId: _purchaseApply.taskId
        }
      })
    },
    _openEditPurchaseModel(_purchaseApply) {
      this.$router.push({
        path: '/pages/resource/editPurchaseApply',
        query: {
          applyOrderId: _purchaseApply.applyOrderId,
          resOrderType: _purchaseApply.resOrderType
        }
      })
    },
    handleSizeChange(val) {
      this.pageSize = val
      this._listAuditOrders(this.currentPage, val)
    },
    handleCurrentChange(val) {
      this.currentPage = val
      this._listAuditOrders(val, this.pageSize)
    },
    goBack() {
      this.$router.go(-1)
    }
  }
}
</script>

<style lang="scss" scoped>
.ecommerce {
  padding: 20px;
}

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
</style>