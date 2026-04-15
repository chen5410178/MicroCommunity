<template>
    <div class="pre-pay-fee-order-container">
          <el-form :inline="true" class="search-form text-left">
            <el-form-item>
              <el-date-picker v-model="prePayFeeOrderInfo.conditions.queryStartTime" :placeholder="$t('reportPayFeeDetail.selectStartTime')" type="datetime" value-format="yyyy-MM-dd HH:mm:ss" class="preStartTime" clearable></el-date-picker>
            </el-form-item>
            <el-form-item>
              <el-date-picker v-model="prePayFeeOrderInfo.conditions.queryEndTime" :placeholder="$t('reportPayFeeDetail.selectEndTime')" type="datetime" value-format="yyyy-MM-dd HH:mm:ss" class="preEndTime" clearable></el-date-picker>
            </el-form-item>
            <el-form-item>
              <el-select v-model="prePayFeeOrderInfo.conditions.primeRate" :placeholder="$t('reportPayFeeDetail.selectPrimeRate')" style="width: 100%;" clearable>
                <el-option v-for="(item, index) in prePayFeeOrderInfo.primeRates" :key="index" :label="item.name" :value="item.statusCd"></el-option>
              </el-select>
            </el-form-item>
            <el-form-item>
              <el-input v-model="prePayFeeOrderInfo.conditions.payerObjName" :placeholder="$t('reportPayFeeDetail.payerObjName')" clearable></el-input>
            </el-form-item>
            <el-form-item>
              <el-button type="primary" @click="_queryDetailMethod">{{ $t('reportPayFeeDetail.search') }}</el-button>
              <el-button type="info" @click="_resetPreMethod" class="reset-btn">{{ $t('reportPayFeeDetail.reset') }}</el-button>
            </el-form-item>
          </el-form>
  
      <el-row :gutter="20">
        <el-col :span="24">
          <el-table :data="prePayFeeOrderInfo.fees" border style="width: 100%">
            <el-table-column :label="$t('reportPayFeeDetail.feeName')" align="center">
              <template slot-scope="scope">
                {{ scope.row.ftName }}>{{ scope.row.feeName }}
              </template>
            </el-table-column>
            <el-table-column prop="payerObjName" :label="$t('reportPayFeeDetail.payObject')" align="center"></el-table-column>
            <el-table-column prop="ownerName" :label="$t('reportPayFeeDetail.ownerName')" align="center"></el-table-column>
            <el-table-column prop="cycles" :label="$t('reportPayFeeDetail.cycle')" align="center"></el-table-column>
            <el-table-column prop="receivableAmount" :label="$t('reportPayFeeDetail.receivableAmount')" align="center"></el-table-column>
            <el-table-column prop="receivedAmount" :label="$t('reportPayFeeDetail.receivedAmount')" align="center"></el-table-column>
            <el-table-column prop="remainAmount" :label="$t('reportPayFeeDetail.remainAmount')" align="center"></el-table-column>
            <el-table-column prop="primeRateName" :label="$t('reportPayFeeDetail.payWay')" align="center"></el-table-column>
            <el-table-column :label="$t('reportPayFeeDetail.prePayPeriod')" align="center">
              <template slot-scope="scope">
                {{ scope.row.startTime }}<br>~{{ scope.row.endTime }}
              </template>
            </el-table-column>
            <el-table-column prop="stateName" :label="$t('reportPayFeeDetail.state')" align="center"></el-table-column>
            <el-table-column prop="cashierName" :label="$t('reportPayFeeDetail.cashier')" align="center"></el-table-column>
            <el-table-column prop="discountAmount" :label="$t('reportPayFeeDetail.discountAmount')" align="center"></el-table-column>
            <el-table-column prop="createTime" :label="$t('reportPayFeeDetail.prePayTime')" align="center"></el-table-column>
            <el-table-column :label="$t('reportPayFeeDetail.remark')" align="center">
              <template slot-scope="scope">
                {{ scope.row.remark || '-' }}
              </template>
            </el-table-column>
          </el-table>
        </el-col>
      </el-row>
  
      <el-row :gutter="20">
        <el-col :span="24">
          <el-pagination
            :current-page="page.current"
            :page-sizes="[10, 20, 30, 50]"
            :page-size="page.size"
            :total="page.total"
            layout="total, sizes, prev, pager, next, jumper"
            @size-change="handleSizeChange"
            @current-change="handleCurrentChange">
          </el-pagination>
        </el-col>
      </el-row>
    </div>
  </template>
  
  <script>
  import { listPrePayFee } from '@/api/report/reportPayFeeDetailApi'
  import { getCommunityId } from '@/api/community/communityApi'
  import { getDict } from '@/api/community/communityApi'
  
  export default {
    name: 'PrePayFeeOrder',
    data() {
      return {
        prePayFeeOrderInfo: {
          fees: [],
          total: 0,
          records: 1,
          primeRates: [],
          conditions: {
            payerObjName: '',
            primeRate: '',
            queryStartTime: '',
            queryEndTime: ''
          }
        },
        page: {
          current: 1,
          size: 10,
          total: 0
        }
      }
    },
    mounted() {
    },
    methods: {
      async open(conditions) {
        this.prePayFeeOrderInfo.conditions = conditions
        await this.getDictData()
        await this._listPrePayFees()
      },
      async getDictData() {
        try {
          const primeRates = await getDict('pay_fee_detail', 'prime_rate')
          this.prePayFeeOrderInfo.primeRates = primeRates
        } catch (error) {
          console.error('获取字典数据失败:', error)
        }
      },
      async _queryDetailMethod() {
        this.page.current = 1
        await this._listPrePayFees()
      },
      async _listPrePayFees() {
        try {
          this.prePayFeeOrderInfo.conditions.page = this.page.current
          this.prePayFeeOrderInfo.conditions.row = this.page.size
          this.prePayFeeOrderInfo.conditions.communityId = getCommunityId()
          const params = this.prePayFeeOrderInfo.conditions
          const data = await listPrePayFee(params)
          this.prePayFeeOrderInfo.fees = data.data || []
          this.page.total = data.total || 0
          this.prePayFeeOrderInfo.total = data.total || 0
        } catch (error) {
          console.error('请求失败处理', error)
        }
      },
      async _resetPreMethod() {
        this.prePayFeeOrderInfo.conditions = {
          payerObjName: '',
          primeRate: '',
          queryStartTime: '',
          queryEndTime: ''
        }
        await this._listPrePayFees()
      },
      handleSizeChange(val) {
        this.page.size = val
        this._listPrePayFees()
      },
      handleCurrentChange(val) {
        this.page.current = val
        this._listPrePayFees()
      }
    }
  }
  </script>
  
  <style lang="scss" scoped>
  .pre-pay-fee-order-container {
    .search-form {
      .el-form-item {
        margin-bottom: 10px;
      }
      .reset-btn {
        margin-left: 10px;
      }
    }
  }
  </style>