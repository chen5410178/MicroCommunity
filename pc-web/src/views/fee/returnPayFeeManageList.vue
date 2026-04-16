<template>
  <div class="return-pay-fee-manage-container animated fadeInRight">
    <!-- 查询条件 -->
    <el-card class="search-card">
      <div slot="header" class="flex justify-between">
        <span>{{ $t('returnPayFeeManage.search.title') }}</span>
        <div class="card-header-tools">
          <el-button type="text" @click="_moreCondition">
            {{ returnPayFeeManageInfo.moreCondition ? $t('returnPayFeeManage.search.hide') : $t('returnPayFeeManage.search.more') }}
          </el-button>
        </div>
      </div>
      <el-form :model="returnPayFeeManageInfo.conditions" label-width="0">
        <el-row :gutter="20">
          <el-col :span="6">
            <el-form-item>
              <el-input
                v-model="returnPayFeeManageInfo.conditions.detailId"
                :placeholder="$t('returnPayFeeManage.search.detailId')"
                clearable
              />
            </el-form-item>
          </el-col>
          <el-col :span="6">
            <el-form-item>
              <el-select
                v-model="returnPayFeeManageInfo.conditions.orderType"
                :placeholder="$t('returnPayFeeManage.search.orderType')"
                style="width: 100%"
                clearable
              >
                <el-option value="" :label="$t('returnPayFeeManage.search.orderType')" />
                <el-option value="2002" label="缴费单" />
                <el-option value="3003" label="押金" />
                <el-option value="1001" label="预付收款" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="6">
            <el-form-item>
              <el-select
                v-model="returnPayFeeManageInfo.conditions.state"
                :placeholder="$t('returnPayFeeManage.search.state')"
                style="width: 100%"
                clearable
              >
                <el-option value="" :label="$t('returnPayFeeManage.search.state')" />
                <el-option
                  v-for="(item, index) in returnPayFeeManageInfo.returnPayFeeStates"
                  :key="index"
                  :value="item.statusCd"
                  :label="item.name"
                />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="6">
            <el-form-item>
              <el-button type="primary" icon="el-icon-search" @click="_queryReturnPayFeeMethod">
                {{ $t('returnPayFeeManage.search.query') }}
              </el-button>
              <el-button icon="el-icon-refresh" @click="_resetReturnPayFeeMethod">
                {{ $t('returnPayFeeManage.search.reset') }}
              </el-button>
            </el-form-item>
          </el-col>
        </el-row>

        <!-- 更多查询条件 -->
        <div v-show="returnPayFeeManageInfo.moreCondition">
          <el-row :gutter="20">
            <el-col :span="6">
              <el-form-item>
                <el-input
                  v-model="returnPayFeeManageInfo.conditions.applyPersonName"
                  :placeholder="$t('returnPayFeeManage.search.applyPersonName')"
                  clearable
                />
              </el-form-item>
            </el-col>
            <el-col :span="6">
              <el-form-item>
                <el-input
                  v-model="returnPayFeeManageInfo.conditions.auditPersonName"
                  :placeholder="$t('returnPayFeeManage.search.auditPersonName')"
                  clearable
                />
              </el-form-item>
            </el-col>
            <el-col :span="6">
              <el-form-item>
                <el-input
                  v-model="returnPayFeeManageInfo.conditions.payerObjName"
                  :placeholder="$t('returnPayFeeManage.search.payerObjName')"
                  clearable
                />
              </el-form-item>
            </el-col>
          </el-row>
          <el-row :gutter="20">
            <el-col :span="6">
              <el-form-item>
                <el-date-picker
                  v-model="returnPayFeeManageInfo.conditions.startTime"
                  type="datetime"
                  :placeholder="$t('returnPayFeeManage.search.startTime')"
                  value-format="yyyy-MM-dd HH:mm:ss"
                  style="width: 100%"
                />
              </el-form-item>
            </el-col>
            <el-col :span="6">
              <el-form-item>
                <el-date-picker
                  v-model="returnPayFeeManageInfo.conditions.endTime"
                  type="datetime"
                  :placeholder="$t('returnPayFeeManage.search.endTime')"
                  value-format="yyyy-MM-dd HH:mm:ss"
                  style="width: 100%"
                />
              </el-form-item>
            </el-col>
          </el-row>
        </div>
      </el-form>
    </el-card>

    <!-- 退费申请单列表 -->
    <el-card class="list-card">
      <div slot="header" class="flex justify-between">
        <span>{{ $t('returnPayFeeManage.list.title') }}</span>
        <div class="card-header-tools">
          <el-button type="primary" size="small" @click="_exportFee">
            {{ $t('returnPayFeeManage.list.export') }}
          </el-button>
        </div>
      </div>

      <el-table
        v-loading="loading"
        :data="returnPayFeeManageInfo.returnPayFees"
        border
        style="width: 100%"
      >
        <el-table-column prop="refundOrderId" :label="$t('returnPayFeeManage.table.refundOrderId')" align="center" />
        <el-table-column :label="$t('returnPayFeeManage.table.orderType')" align="center">
          <template slot-scope="scope">
            <span v-if="scope.row.orderType === '2002'">退缴费单</span>
            <span v-else-if="scope.row.orderType === '3003'">退押金</span>
            <span v-else>退预付单</span>
          </template>
        </el-table-column>
        <el-table-column prop="payerObjName" :label="$t('returnPayFeeManage.table.payerObjName')" align="center" />
        <el-table-column prop="ownerName" :label="$t('returnPayFeeManage.table.ownerName')" align="center" />
        <el-table-column :label="$t('returnPayFeeManage.table.feeName')" align="center">
          <template slot-scope="scope">
            <div v-for="detail in scope.row.details" :key="detail.feeName" style="width: 110px;">
              {{ detail.feeName }}<br>
            </div>
          </template>
        </el-table-column>
        <el-table-column :label="$t('returnPayFeeManage.table.timeRange')" align="center">
          <template slot-scope="scope">
            <div v-for="detail in scope.row.details" :key="detail.startTime" style="width: 180px;">
              {{ dateFormat(detail.startTime) }}~{{ dateFormat(detail.endTime) }}<br>
            </div>
          </template>
        </el-table-column>
        <el-table-column :label="$t('returnPayFeeManage.table.amount')" align="center">
          <template slot-scope="scope">
            <div v-for="detail in scope.row.details" :key="detail.receivedAmount">
              {{ detail.receivedAmount }}<br>
            </div>
          </template>
        </el-table-column>
        <el-table-column prop="createTime" :label="$t('returnPayFeeManage.table.createTime')" align="center" />
        <el-table-column prop="reason" :label="$t('returnPayFeeManage.table.reason')" align="center" />
        <el-table-column :label="$t('returnPayFeeManage.table.applyPersonName')" align="center">
          <template slot-scope="scope">
            {{ scope.row.applyPersonName || '-' }}
          </template>
        </el-table-column>
        <el-table-column :label="$t('returnPayFeeManage.table.state')" align="center">
          <template slot-scope="scope">
            <el-tag
              v-if="scope.row.state === '1100'"
              type="success"
            >{{ scope.row.stateName }}</el-tag>
            <el-tag
              v-else-if="scope.row.state === '1200'"
              type="danger"
            >{{ scope.row.stateName }}</el-tag>
            <el-tag
              v-else
              type="info"
            >{{ scope.row.stateName }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column :label="$t('returnPayFeeManage.table.auditPersonName')" align="center">
          <template slot-scope="scope">
            {{ scope.row.auditPersonName || '-' }}
          </template>
        </el-table-column>
        <el-table-column :label="$t('returnPayFeeManage.table.retundRemark')" align="center">
          <template slot-scope="scope">
            <div class="hc-td">{{ scope.row.retundRemark || '-' }}</div>
          </template>
        </el-table-column>
        <el-table-column :label="$t('returnPayFeeManage.table.operation')" align="center" width="200" fixed="right">
          <template slot-scope="scope">
            <el-button-group>
              <el-button
                v-if="scope.row.state === '1000'"
                size="mini"
                type="primary"
                @click="_openReturnPayFeeAuditModel(scope.row)"
              >
                {{ $t('returnPayFeeManage.table.audit') }}
              </el-button>
              <el-button
                v-if="scope.row.refundState === 'FT'"
                size="mini"
                type="warning"
                @click="_tryRefundMoney(scope.row)"
              >
                {{ $t('returnPayFeeManage.table.retryRefund') }}
              </el-button>
              <el-button
                size="mini"
                @click="_toReturnFeeDetail(scope.row)"
              >
                {{ $t('returnPayFeeManage.table.detail') }}
              </el-button>
            </el-button-group>
          </template>
        </el-table-column>
      </el-table>

      <!-- 分页 -->
      <el-pagination
        :current-page.sync="page.current"
        :page-sizes="[10, 20, 30, 50]"
        :page-size="page.size"
        :total="page.total"
        layout="total, sizes, prev, pager, next, jumper"
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
      />
    </el-card>

    <!-- 审核组件 -->
    <audit-component
      ref="auditComponent"
      @notifyAuditInfo="notifyAuditInfo"
    />
  </div>
</template>

<script>
import { listReturnPayFees, updateReturnPayFee, auditRefundPrePayFee, auditRefundDeposit, tryRefundMoney } from '@/api/fee/returnPayFeeManageApi'
import { getDict } from '@/api/community/communityApi'
import { getCommunityId } from '@/api/community/communityApi'
import AuditComponent from '@/components/fee/audit'
import { dateFormat } from '@/utils/dateUtil'

export default {
  name: 'ReturnPayFeeManageList',
  components: {
    AuditComponent
  },
  data() {
    return {
      loading: false,
      returnPayFeeManageInfo: {
        returnPayFees: [],
        moreCondition: false,
        returnPayFeeStates: [],
        feeTypes: [],
        conditions: {
          communityId: '',
          detailId: '',
          state: '1000',
          orderType: '',
          payerObjName: '',
          startTime: '',
          endTime: '',
          auditPersonName: '',
          applyPersonName: ''
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
    this._initMethod()
  },
  methods: {
    async _initMethod() {
      this.returnPayFeeManageInfo.conditions.communityId = getCommunityId()
      await this._listReturnPayFees(this.page.current, this.page.size)
      
      try {
        const returnPayFeeStates = await getDict('return_pay_fee', 'state')
        this.returnPayFeeManageInfo.returnPayFeeStates = returnPayFeeStates
      } catch (error) {
        console.error('获取退费状态字典失败:', error)
      }
      
      try {
        const feeTypes = await getDict('pay_fee_config', 'fee_type_cd')
        this.returnPayFeeManageInfo.feeTypes = feeTypes
      } catch (error) {
        console.error('获取费用类型字典失败:', error)
      }
    },
    
    async _listReturnPayFees(currentPage, pageSize) {
      try {
        this.loading = true
        const params = {
          ...this.returnPayFeeManageInfo.conditions,
          page: currentPage,
          row: pageSize
        }
        
        // 去除参数中的空格
        if (params.detailId) params.detailId = params.detailId.trim()
        if (params.payerObjName) params.payerObjName = params.payerObjName.trim()
        if (params.applyPersonName) params.applyPersonName = params.applyPersonName.trim()
        if (params.auditPersonName) params.auditPersonName = params.auditPersonName.trim()
        
        const { data, total } = await listReturnPayFees(params)
        this.returnPayFeeManageInfo.returnPayFees = data
        this.page.total = total
      } catch (error) {
        this.$message.error(this.$t('returnPayFeeManage.fetchError'))
      } finally {
        this.loading = false
      }
    },
    
    _queryReturnPayFeeMethod() {
      this.page.current = 1
      this._listReturnPayFees(this.page.current, this.page.size)
    },
    
    _resetReturnPayFeeMethod() {
      this.returnPayFeeManageInfo.conditions = {
        communityId: getCommunityId(),
        detailId: '',
        feeTypeCd: '',
        state: '',
        orderType: '',
        payerObjName: '',
        startTime: '',
        endTime: '',
        applyPersonName: '',
        auditPersonName: ''
      }
      this._listReturnPayFees(1, this.page.size)
    },
    
    async notifyAuditInfo(auditInfo) {
      const refundFee = this.returnPayFeeManageInfo.returnPayFee
      if (!refundFee) return
      
      refundFee.state = auditInfo.state
      refundFee.payableAmount = 0.0
      
      try {
        if (refundFee.orderType === '2002') {
          await this._auditReturnPayFeeState(auditInfo)
        } else if (refundFee.orderType === '3003') {
          await this._auditRefundDepositState(auditInfo)
        } else {
          await this._auditRefundPrePayFeeState(auditInfo)
        }
      } catch (error) {
        this.$message.error(this.$t('returnPayFeeManage.auditError'))
      }
    },
    
    async _auditRefundPrePayFeeState(auditInfo) {
      const returnPayFee = { ...this.returnPayFeeManageInfo.returnPayFee }
      returnPayFee.state = auditInfo.state
      returnPayFee.payableAmount = 0.0
      
      await auditRefundPrePayFee(returnPayFee)
      this.$message.success(this.$t('common.operationSuccess'))
      this._listReturnPayFees(this.page.current, this.page.size)
    },
    
    async _auditReturnPayFeeState(auditInfo) {
      const returnPayFee = { ...this.returnPayFeeManageInfo.returnPayFee }
      returnPayFee.state = auditInfo.state
      returnPayFee.payableAmount = 0.0
      
      await updateReturnPayFee(returnPayFee)
      this.$message.success(this.$t('common.operationSuccess'))
      this._listReturnPayFees(this.page.current, this.page.size)
    },
    
    async _auditRefundDepositState(auditInfo) {
      const returnPayFee = { ...this.returnPayFeeManageInfo.returnPayFee }
      returnPayFee.state = auditInfo.state
      returnPayFee.payableAmount = 0.0
      
      await auditRefundDeposit(returnPayFee)
      this.$message.success(this.$t('common.operationSuccess'))
      this._listReturnPayFees(this.page.current, this.page.size)
    },
    
    async _tryRefundMoney(rPayFee) {
      try {
        const params = {
          detailId: rPayFee.detailId,
          communityId: getCommunityId()
        }
        await tryRefundMoney(params)
        this.$message.success(this.$t('common.operationSuccess'))
        this._listReturnPayFees(this.page.current, this.page.size)
      } catch (error) {
        this.$message.error(this.$t('returnPayFeeManage.refundError'))
      }
    },
    
    _moreCondition() {
      this.returnPayFeeManageInfo.moreCondition = !this.returnPayFeeManageInfo.moreCondition
    },
    
    _exportFee() {
      const params = new URLSearchParams(this.returnPayFeeManageInfo.conditions).toString()
      window.open(`/callComponent/exportReportFee/exportData?pagePath=returnPayFeeManage&${params}`, '_blank')
    },
    
    _openReturnPayFeeAuditModel(payFee) {
      this.returnPayFeeManageInfo.returnPayFee = payFee
      this.$refs.auditComponent.open()
    },
    
    _toReturnFeeDetail(payFee) {
      this.$router.push({ path: '/pages/property/propertyFee', query: { feeId: payFee.feeId }})
    },
    
    handleSizeChange(val) {
      this.page.size = val
      this._listReturnPayFees(this.page.current, this.page.size)
    },
    
    handleCurrentChange(val) {
      this.page.current = val
      this._listReturnPayFees(this.page.current, this.page.size)
    },
    
    dateFormat,
  }
}
</script>

<style lang="scss" scoped>
.return-pay-fee-manage-container {
  padding: 20px;
  
  .search-card, .list-card {
    margin-bottom: 20px;
  }
  
  .card-header-tools {
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
  
  .el-pagination {
    margin-top: 20px;
    text-align: right;
  }
  
  .hc-td {
    max-width: 150px;
    overflow: hidden;
    text-overflow: ellipsis;
    white-space: nowrap;
  }
}
</style>