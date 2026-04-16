<template>
    <div v-if="orderPrePayFeeInfo.preFees.length > 0">
      <el-row :gutter="20">
        <el-col :span="24">
          <el-card class="">
            <div slot="header" class="flex justify-between">
              <div>
                {{ $t('orderPrePayFee.prePayment') }}
              </div>
              <div class="" >
                <el-button type="button" class="btn btn-primary btn-sm" style="margin-left:10px" @click="_queryOrderPrePayFee">
                  <i class="fa fa-refresh"></i>
                  {{ $t('orderPrePayFee.refresh') }}
                </el-button>
              </div>
            </div>
            <div class="">
              <el-table :data="orderPrePayFeeInfo.preFees" border style="width: 100%">
                <el-table-column :label="$t('orderPrePayFee.select')" width="50">
                  <template slot-scope="scope">
                    <input type="radio" name="selectAccount" @click="_computeFeeUserAmount(scope.row.ppfId)" class="i-checks checkAccountItem" :value="scope.row.ppfId">
                  </template>
                </el-table-column>
                <el-table-column :label="$t('orderPrePayFee.feeName')" prop="feeName"></el-table-column>
                <el-table-column :label="$t('orderPrePayFee.remainingAmount')" prop="remainAmount"></el-table-column>
                <el-table-column :label="$t('orderPrePayFee.prePayTime')" prop="createTime"></el-table-column>
              </el-table>
            </div>
          </el-card>
        </el-col>
      </el-row>
    </div>
  </template>
  
  <script>
  import { getCommunityId } from '@/api/community/communityApi'
  import { listPrePayFee } from '@/api/fee/payFeeOrderApi'
  
  export default {
    name: 'OrderPrePayFee',
    data() {
      return {
        communityId: '',
        orderPrePayFeeInfo: {
          preFees: [],
          feeId: '',
          payerObjId: '',
          payerObjType: '',
          quanAccount: false,
          selectPpfIds: [],
          integralAmount: '',
          cashAmount: '',
          couponAmount: ''
        }
      }
    },
    created() {
      this.communityId = getCommunityId()
    },
    methods: {
      // 父组件调用：加载账户数据
      loadPreFees(params) {
        this.orderPrePayFeeInfo.payerObjId = params.payerObjId || ''
        this.orderPrePayFeeInfo.payerObjType = params.payerObjType || ''
        this.orderPrePayFeeInfo.feeId = params.feeId || ''
        this._queryOrderPrePayFee()
      },
      _queryOrderPrePayFee() {
        this.orderPrePayFeeInfo.preFees = []
        this.orderPrePayFeeInfo.selectPpfIds = []
        this._listOrderPreFee(1, 20)
      },
      async _listOrderPreFee(page, row) {
        try {
          const params = {
            page,
            row,
            payerObjId: this.orderPrePayFeeInfo.payerObjId,
            communityId: this.communityId,
            way: 'N',
            hasMoney: 'Y'
          }
          const { data } = await listPrePayFee(params)
          this.orderPrePayFeeInfo.preFees = data
        } catch (error) {
          console.error('请求失败处理')
        }
      },
      checkAllAccount(e) {
        const checkObj = document.querySelectorAll('.checkAccountItem')
        if (e.target.checked) {
          for (let i = 0; i < checkObj.length; i++) {
            if (!checkObj[i].checked) {
              const value = checkObj[i].value
              this.orderPrePayFeeInfo.selectPpfIds.push(value)
            }
          }
        } else {
          this.orderPrePayFeeInfo.selectPpfIds = []
        }
      },
      _computeFeeUserAmount(ppfId) {
        let totalUserAmount = 0.0
        this.orderPrePayFeeInfo.preFees.forEach(disItem => {
          if (ppfId == disItem.ppfId && disItem.remainAmount != 0) {
            totalUserAmount += parseFloat(disItem.remainAmount)
            this.orderPrePayFeeInfo.cashAmount = disItem.remainAmount
          }
        })
        this.$emit('changeUserAmountPrice', {
          totalUserAmount,
          preFees: this.orderPrePayFeeInfo.preFees,
          selectPreFees: this.orderPrePayFeeInfo.preFees.filter(item => item.ppfId === ppfId)
        })
      }
    }
  }
  </script>
  
  <style lang="scss" scoped>
  .ibox {
    margin-bottom: 20px;
  }
  </style>