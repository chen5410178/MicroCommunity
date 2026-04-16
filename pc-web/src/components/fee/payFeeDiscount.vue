<template>
  <div v-if="(payFeeDiscountInfo.feeDiscounts.length > 0) || (payFeeDiscountInfo.selectDiscountIds.length == 0 && payFeeDiscountInfo.feeDiscounts.length != 0) || (payFeeDiscountInfo.selectDiscountIds.length != payFeeDiscountInfo.feeDiscounts.length && payFeeDiscountInfo.totalDiscountMoney == 0)">
    <el-row :gutter="20">
      <el-col :span="24">
        <el-card class="ibox">
          <div slot="header" class="ibox-title">
            <h5>
              {{ $t('payFeeDiscount.discountInfo') }}
            </h5>
            <div class="ibox-tools" style="top:10px;">
              <!-- <el-button type="primary" size="mini" @click="_openAddFeeDiscountModal">
                <i class="glyphicon glyphicon-plus"></i> 特殊折扣
              </el-button> -->
            </div>
          </div>
          <div class="ibox-content">
            <el-table :data="payFeeDiscountInfo.feeDiscounts" border style="width:100%">
              <el-table-column :label="$t('payFeeDiscount.select')" width="50">
                <template >
                  <el-checkbox v-bind:checked="payFeeDiscountInfo.quanDiscount" @click="checkAllDiscount($event)"></el-checkbox>
                </template>
              </el-table-column>
              <el-table-column :label="$t('payFeeDiscount.discountType')" prop="discountType">
                <template slot-scope="scope">
                  {{ scope.row.discountType == '1001' ? $t('payFeeDiscount.discount') : (scope.row.discountType == '2002' ? $t('payFeeDiscount.lateFee') : $t('payFeeDiscount.discountSpecial')) }}
                </template>
              </el-table-column>
              <el-table-column :label="$t('payFeeDiscount.discountName')" prop="discountName"></el-table-column>
              <el-table-column :label="$t('payFeeDiscount.ruleName')" prop="ruleName"></el-table-column>
              <el-table-column :label="$t('payFeeDiscount.rule')">
                <template slot-scope="scope">
                  <div v-for="(item, index) in scope.row.feeDiscountSpecs" :key="index">
                    {{ item.specName }}：{{ item.specValue }}
                  </div>
                </template>
              </el-table-column>
              <el-table-column label="负数：打折减免的金额；<br>正数：需额外缴纳的滞纳金">
                <template slot-scope="scope">
                  <span v-if="scope.row.discountType == '1001'">
                    {{ scope.row.discountPrice  }}
                    <span>{{ $t('payFeeDiscount.yuan') }}</span>
                  </span>
                  <span v-if="scope.row.discountType == '2002'">
                    {{ scope.row.discountPrice }}
                    <span>{{ $t('payFeeDiscount.yuan') }}</span>
                  </span>
                  <span v-if="scope.row.discountType == '3003'">
                    {{ scope.row.discountPrice  }}
                    <span>{{ $t('payFeeDiscount.yuan') }}</span>
                  </span>
                </template>
              </el-table-column>
            </el-table>
          </div>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import { getCommunityId } from '@/api/community/communityApi'
import { computeFeeDiscount } from '@/api/fee/payFeeOrderApi'

export default {
  name: 'PayFeeDiscount',
  data() {
    return {
      communityId: '',
      payFeeDiscountInfo: {
        feeDiscounts: [],
        feeId: '',
        payerObjId: '',
        payerObjType: '',
        endTime: '',
        custEndTime: '',
        communityId: '',
        cycles: 1,
        quanDiscount: false,
        totalDiscountMoney: 0.0,
        selectDiscountIds: []
      }
    }
  },
  watch: {
    payFeeDiscountInfo: {
      handler() {
        if (this.payFeeDiscountInfo.selectDiscountIds.length == this.payFeeDiscountInfo.feeDiscounts.length) {
          this.payFeeDiscountInfo.quanDiscount = true
        } else {
          this.payFeeDiscountInfo.quanDiscount = false
        }
        this._computeFeeDiscount()
      },
      deep: true
    }
  },
  created() {
    this.payFeeDiscountInfo.communityId = getCommunityId()
  },
  methods: {
    // 父组件调用：计算折扣并渲染
    computeFeeDiscount(params) {
      this.payFeeDiscountInfo.feeId = params.feeId
      this.payFeeDiscountInfo.cycles = params.cycles || 1
      this.payFeeDiscountInfo.payerObjId = params.payerObjId
      this.payFeeDiscountInfo.payerObjType = params.payerObjType
      this.payFeeDiscountInfo.endTime = params.endTime
      this.payFeeDiscountInfo.custEndTime = params.custEndTime
      this.payFeeDiscountInfo.selectDiscountIds = []
      this._listFeeDiscounts(1, 20)
    },
    popOverShowMsg() {
      // 假设有popover
    },
    popOverHideMsg() {
      // 假设有popover
    },
    async _listFeeDiscounts(page, row) {
      let cycles = this.payFeeDiscountInfo.cycles
      const params = {
        page,
        row,
        feeId: this.payFeeDiscountInfo.feeId,
        communityId: this.communityId,
        cycles,
        payerObjId: this.payFeeDiscountInfo.payerObjId,
        payerObjType: this.payFeeDiscountInfo.payerObjType,
        endTime: this.payFeeDiscountInfo.endTime,
        custEndTime: this.payFeeDiscountInfo.custEndTime
      }
      try {
        const { data } = await computeFeeDiscount(params)
        this.payFeeDiscountInfo.feeDiscounts = data
        this.payFeeDiscountInfo.feeDiscounts.forEach(item => {
          this.payFeeDiscountInfo.selectDiscountIds.push(item.discountId)
        })
      } catch (error) {
        console.error('请求失败处理')
      }
    },
    _mathCeil(price) {
      return Math.ceil(price)
    },
    _mathFloor(price) {
      return Math.floor(price)
    },
    _mathRound(price) {
      return Math.round(price)
    },
    _mathToFixed1(price) {
      return parseFloat(price).toFixed(1)
    },
    _mathToFixed2(price) {
      return parseFloat(price).toFixed(2)
    },
    _openAddFeeDiscountModal() {
      // this.$emit('openAddFeeDiscountModal', {})
    },
    checkAllDiscount(e) {
      const checkObj = document.querySelectorAll('.checkDiscountItem')
      if (e.target.checked) {
        for (let i = 0; i < checkObj.length; i++) {
          if (!checkObj[i].checked) {
            const value = checkObj[i].value
            this.payFeeDiscountInfo.selectDiscountIds.push(value)
          }
        }
      } else {
        this.payFeeDiscountInfo.selectDiscountIds = []
      }
    },
    _computeFeeDiscount() {
      let totalDiscountMoney = 0.0
      const selectDiscount = []
      this.payFeeDiscountInfo.selectDiscountIds.forEach(item => {
        this.payFeeDiscountInfo.feeDiscounts.forEach(disItem => {
          if (disItem.feeDiscountSpecs && disItem.feeDiscountSpecs.length > 0) {
            let specValue = ''
            disItem.feeDiscountSpecs.forEach(feeItem => {
              if (feeItem.specName == '月份') {
                specValue = feeItem.specValue
              }
            })
            if ((disItem.discountType == '1001' || disItem.discountType == '3003') && parseFloat(this.payFeeDiscountInfo.cycles) < parseFloat(specValue)) {
              return
            }
          }
          if (item == disItem.discountId && disItem.discountPrice != 0 && disItem.ruleId != '102020008') {
            if (disItem.feeDiscountSpecs && disItem.feeDiscountSpecs.length > 0) {
              let specValue = ''
              disItem.feeDiscountSpecs.forEach(feeItem => {
                if (feeItem.specName == '月份') {
                  specValue = feeItem.specValue
                }
              })
              if (specValue && parseFloat(this.payFeeDiscountInfo.cycles) >= parseFloat(specValue)) {
                totalDiscountMoney += parseFloat(disItem.discountPrice)
                selectDiscount.push(disItem)
              }
              specValue = ''
              disItem.feeDiscountSpecs.forEach(feeItem => {
                if (feeItem.specName == '滞纳金') {
                  specValue = feeItem.specValue
                }
              })
              if (specValue) {
                totalDiscountMoney += parseFloat(disItem.discountPrice)
                selectDiscount.push(disItem)
              }
            }
          } else if (item == disItem.discountId && disItem.ruleId == '102020008') {
            if (disItem.feeDiscountSpecs != null && disItem.feeDiscountSpecs != undefined && disItem.feeDiscountSpecs.length > 0) {
              let specValue = ''
              disItem.feeDiscountSpecs.forEach(feeItem => {
                if (feeItem.specName == '月份') {
                  specValue = feeItem.specValue
                }
              })
              if (specValue != '' && parseFloat(this.payFeeDiscountInfo.cycles) >= parseFloat(specValue)) {
                selectDiscount.push(disItem)
              }
            }
          }
        })
      })
      this.payFeeDiscountInfo.totalDiscountMoney = totalDiscountMoney
      this.$emit('changeDiscountPrice', {
        totalDiscountMoney,
        selectDiscount
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