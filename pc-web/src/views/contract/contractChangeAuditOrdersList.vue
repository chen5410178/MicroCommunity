<template>
  <div class="animated fadeInRight ecommerce padding">
    <el-row v-if="contractChangeAuditOrdersInfo.audit === '1'">
      <el-col :span="24">
        <el-card>
          <div slot="header" class="flex justify-between">
            <div>{{ $t('contractChangeAuditOrders.title') }}</div>
          </div>
          <div class="">
            <el-table :data="contractChangeAuditOrdersInfo.contractChangeAuditOrders" border style="width: 100%">
              <el-table-column prop="contractCode" :label="$t('contractChangeAuditOrders.contractCode')"
                align="center" />
              <el-table-column prop="contractName" :label="$t('contractChangeAuditOrders.contractName')"
                align="center" />
              <el-table-column prop="contractTypeName" :label="$t('contractChangeAuditOrders.contractType')"
                align="center" />
              <el-table-column prop="stateName" :label="$t('contractChangeAuditOrders.contractStatus')"
                align="center" />
              <el-table-column prop="createTime" :label="$t('contractChangeAuditOrders.createTime')" align="center" />
              <el-table-column :label="$t('contractChangeAuditOrders.operation')" align="center">
                <template slot-scope="scope">
                  <el-button-group>
                    <el-button size="mini" @click="_viewChangeDetail(scope.row)">
                      {{ $t('contractChangeAuditOrders.view') }}
                    </el-button>
                    <el-button v-if="scope.row.hasAudit && !scope.row.hasEnd" size="mini" type="primary"
                      @click="_openAuditOrderModel(scope.row)">
                      {{ $t('contractChangeAuditOrders.audit') }}
                    </el-button>
                    <el-button v-if="scope.row.hasEnd" size="mini" type="warning" @click="_finishAuditOrder(scope.row)">
                      {{ $t('contractChangeAuditOrders.finish') }}
                    </el-button>
                  </el-button-group>
                </template>
              </el-table-column>
            </el-table>
            <el-pagination :current-page.sync="page.current" :page-sizes="[10, 20, 30, 50]" :page-size="page.size"
              :total="page.total" layout="total, sizes, prev, pager, next, jumper" @size-change="handleSizeChange"
              @current-change="handleCurrentChange" />
          </div>
        </el-card>
      </el-col>
    </el-row>
    <flow-audit v-else ref="flowAudit" :call-back-listener="'contractChangeAuditOrders'"
      :call-back-function="'notifyAudit'" />
  </div>
</template>

<script>
import { queryContractChangeTask, needAuditContractPlan } from '@/api/resource/contractChangeAuditOrdersApi'
import FlowAudit from '@/components/contract/flowAudit'
import { getUserId } from '@/api/user/userApi'

export default {
  name: 'ContractChangeAuditOrdersList',
  components: {
    FlowAudit
  },
  data() {
    return {
      contractChangeAuditOrdersInfo: {
        contractChangeAuditOrders: [],
        total: 0,
        records: 1,
        moreCondition: false,
        userName: '',
        currentUserId: '',
        conditions: {
          planId: '',
          userName: '',
          auditLink: ''
        },
        orderInfo: '',
        procure: false,
        audit: '1'
      },
      page: {
        current: 1,
        size: 10,
        total: 0
      }
    }
  },
  created() {
    this.contractChangeAuditOrdersInfo.currentUserId = getUserId()
    this._listAuditOrders(this.page.current, this.page.size)
  },
  methods: {
    async _listAuditOrders(page, rows) {
      this.contractChangeAuditOrdersInfo.audit = '1'
      this.contractChangeAuditOrdersInfo.conditions.page = page
      this.contractChangeAuditOrdersInfo.conditions.row = rows

      try {
        const { data, total } = await queryContractChangeTask(this.contractChangeAuditOrdersInfo.conditions)
        this.contractChangeAuditOrdersInfo.contractChangeAuditOrders = data
        this.contractChangeAuditOrdersInfo.total = total
        this.page.total = total
      } catch (error) {
        console.error('请求失败:', error)
      }
    },
    _openAuditOrderModel(auditOrder) {
      this.contractChangeAuditOrdersInfo.orderInfo = auditOrder
      this.contractChangeAuditOrdersInfo.audit = '2'
      auditOrder.startUserId = auditOrder.changePerson
      this.$refs.flowAudit.open(auditOrder)
    },
    async _auditOrderInfo(auditInfo) {
      auditInfo.taskId = this.contractChangeAuditOrdersInfo.orderInfo.taskId
      auditInfo.planId = this.contractChangeAuditOrdersInfo.orderInfo.planId
      auditInfo.contractId = this.contractChangeAuditOrdersInfo.orderInfo.contractId

      try {
        await needAuditContractPlan(auditInfo)
        this.$message.success(this.$t('common.operationSuccess'))
        this._listAuditOrders(this.page.current, this.page.size)
      } catch (error) {
        this.$message.error(this.$t('common.handleFailed') + error.message)
      }
    },
    async _finishAuditOrder(auditOrder) {
      const auditInfo = {
        taskId: auditOrder.taskId,
        planId: auditOrder.planId,
        state: '1200',
        remark: this.$t('contractChangeAuditOrders.processEnd'),
        contractId: auditOrder.contractId
      }

      try {
        await needAuditContractPlan(auditInfo)
        this.$message.success(this.$t('common.operationSuccess'))
        this._listAuditOrders(this.page.current, this.page.size)
      } catch (error) {
        this.$message.error(this.$t('common.handleFailed') + error.message)
      }
    },
    _viewChangeDetail(plan) {
      this.$router.push({
        path: '/views/contract/contractChangeDetails',
        query: { planId: plan.planId }
      })
    },
    handleSizeChange(val) {
      this.page.size = val
      this._listAuditOrders(this.page.current, val)
    },
    handleCurrentChange(val) {
      this.page.current = val
      this._listAuditOrders(val, this.page.size)
    }
  }
}
</script>

<style scoped>
.ibox-content {
  padding: 15px 20px 20px 20px;
}

.el-pagination {
  margin-top: 15px;
}
</style>