<template>
  <div class="animated fadeInRight ecommerce padding">
    <el-row v-if="contractApplyAuditOrdersInfo.audit === '1'">
      <el-col :span="24">
        <el-card>
          <div slot="header" class="flex justify-between">
            <div>{{ $t('contractApplyAuditOrders.draftPendingList') }}</div>
          </div>
          <div class="">
            <el-table :data="contractApplyAuditOrdersInfo.contractApplyAuditOrders" style="width: 100%" border stripe>
              <el-table-column prop="contractCode" :label="$t('contractApplyAuditOrders.contractCode')"
                align="center" />
              <el-table-column prop="contractName" :label="$t('contractApplyAuditOrders.contractName')"
                align="center" />
              <el-table-column prop="contractTypeName" :label="$t('contractApplyAuditOrders.contractType')"
                align="center" />
              <el-table-column prop="stateName" :label="$t('contractApplyAuditOrders.contractStatus')" align="center" />
              <el-table-column prop="signingTime" :label="$t('contractApplyAuditOrders.createTime')" align="center" />
              <el-table-column :label="$t('contractApplyAuditOrders.operation')" align="center" width="200">
                <template slot-scope="scope">
                  <el-button size="mini" type="primary" @click="_openDetailContractApplyModel(scope.row)">
                    {{ $t('contractApplyAuditOrders.view') }}
                  </el-button>
                  <el-button v-if="scope.row.hasAudit && !scope.row.hasEnd" size="mini" type="success"
                    @click="_openAuditOrderModel(scope.row)">
                    {{ $t('contractApplyAuditOrders.audit') }}
                  </el-button>
                  <el-button v-if="scope.row.hasEnd" size="mini" type="warning" @click="_finishAuditOrder(scope.row)">
                    {{ $t('contractApplyAuditOrders.finish') }}
                  </el-button>
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
    <flow-audit v-else ref="flowAudit" :call-back-listener="'contractApplyAuditOrders'"
      :call-back-function="'notifyAudit'" />
  </div>
</template>

<script>
import { queryContractTask, needAuditContract,listWorkflowStepStaffs } from '@/api/contract/contractApplyAuditOrdersApi'
import { getCommunityId } from '@/api/community/communityApi'
import FlowAudit from '@/components/contract/flowAudit'
import { getUserId } from '@/api/user/userApi'  

export default {
  name: 'ContractApplyAuditOrdersList',
  components: {
    FlowAudit
  },
  data() {
    return {
      contractApplyAuditOrdersInfo: {
        contractApplyAuditOrders: [],
        total: 0,
        records: 1,
        currentUserId: '',
        conditions: {
          AuditOrdersId: '',
          userName: '',
          auditLink: ''
        },
        orderInfo: {},
        procure: false,
        audit: '1'
      },
      page: {
        current: 1,
        size: 10,
        total: 0
      },
      communityId: ''
    }
  },
  created() {
    this.communityId = getCommunityId()
    this.contractApplyAuditOrdersInfo.currentUserId = getUserId()
    this._listAuditOrders(this.page.current, this.page.size)
    this._loadStepStaff()
  },
  methods: {
    async _listAuditOrders(page, rows) {
      try {
        this.contractApplyAuditOrdersInfo.audit = '1'
        const params = {
          page: page,
          row: rows,
          ...this.contractApplyAuditOrdersInfo.conditions
        }
        const { data, total, records } = await queryContractTask(params)
        this.contractApplyAuditOrdersInfo.contractApplyAuditOrders = data
        this.contractApplyAuditOrdersInfo.total = total
        this.contractApplyAuditOrdersInfo.records = records
        this.page.total = total
      } catch (error) {
        console.error('请求失败:', error)
      }
    },
    _openAuditOrderModel(auditOrder) {
      this.contractApplyAuditOrdersInfo.orderInfo = auditOrder
      this.contractApplyAuditOrdersInfo.audit = '2'
      this.$refs.flowAudit.open(auditOrder)
    },
    _openDetailContractApplyModel(auditOrder) {
      this.$router.push(`/views/contract/contractApplyDetail?contractId=${auditOrder.contractId}`)
    },
    async _auditOrderInfo(auditInfo) {
      try {
        auditInfo.taskId = this.contractApplyAuditOrdersInfo.orderInfo.taskId
        auditInfo.contractId = this.contractApplyAuditOrdersInfo.orderInfo.contractId
        await needAuditContract(auditInfo)
        this.$message.success(this.$t('common.operationSuccess'))
        this._listAuditOrders(this.page.current, this.page.size)
      } catch (error) {
        this.$message.error(this.$t('common.handleFailed') + error.message)
      }
    },
    async _finishAuditOrder(auditOrder) {
      try {
        const auditInfo = {
          taskId: auditOrder.taskId,
          contractId: auditOrder.contractId,
          state: '1200',
          remark: this.$t('contractApplyAuditOrders.processEnd')
        }
        await needAuditContract(auditInfo)
        this.$message.success(this.$t('common.operationSuccess'))
        this._listAuditOrders(this.page.current, this.page.size)
      } catch (error) {
        this.$message.error(this.$t('common.handleFailed') + error.message)
      }
    },
    async _loadStepStaff() {
      try {
        const params = {
          page: 1,
          row: 1,
          staffId: this.contractApplyAuditOrdersInfo.currentUserId,
          staffRole: '2002'
        }
        const { data } = await listWorkflowStepStaffs(params)
        if (data.length > 0) {
          this.contractApplyAuditOrdersInfo.procure = true
        }
      } catch (error) {
        console.error('请求失败:', error)
      }
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

.clearfix {
  display: flex;
  justify-content: space-between;
  align-items: center;
}
</style>