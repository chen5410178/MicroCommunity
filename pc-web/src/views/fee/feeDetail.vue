<template>
  <div class="fee-detail-container">
    <div class="white-bg padding-left padding-right padding-top border-radius-top">
      <div class="flex justify-between">
        <div class="text-title">
          {{ $t('feeDetail.title') }}
        </div>
      </div>

      <!-- 费用信息 -->
      <div class="margin-top">
        <el-row>
          <el-col :span="24">
            <el-row>
              <el-col :span="6">
                <div class="form-group">
                  <label class="col-form-label">
                    {{ $t('feeDetail.feeId') }}
                  </label>
                  <label>{{ feeDetailInfo.feeId }}</label>
                </div>
              </el-col>
              <el-col :span="6">
                <div class="form-group">
                  <label class="col-form-label">
                    {{ $t('feeDetail.feeFlag') }}
                  </label>
                  <label>{{ feeDetailInfo.feeFlagName || '-' }}</label>
                </div>
              </el-col>
              <el-col :span="6">
                <div class="form-group">
                  <label class="col-form-label">
                    {{ $t('feeDetail.feeType') }}
                  </label>
                  <label>{{ feeDetailInfo.feeTypeCdName }}</label>
                </div>
              </el-col>
              <el-col :span="6">
                <div class="form-group">
                  <label class="col-form-label">
                    {{ $t('feeDetail.payerObj') }}
                  </label>
                  <label>{{ feeDetailInfo.payerObjName || '-' }}</label>
                </div>
              </el-col>
            </el-row>

            <el-row>
              <el-col :span="6">
                <div class="form-group">
                  <label class="col-form-label">
                    {{ $t('feeDetail.feeItem') }}：
                  </label>
                  <label>{{ feeDetailInfo.feeName }}</label>
                </div>
              </el-col>
              <el-col :span="6">
                <div class="form-group">
                  <label class="col-form-label">
                    {{ $t('feeDetail.feeStatus') }}
                  </label>
                  <label>{{ feeDetailInfo.stateName }}</label>
                </div>
              </el-col>
              <el-col :span="6">
                <div class="form-group">
                  <label class="col-form-label">
                    {{ $t('feeDetail.createTime') }}
                  </label>
                  <label>{{ feeDetailInfo.startTime }}</label>
                </div>
              </el-col>
              <el-col :span="6">
                <div class="form-group">
                  <label class="col-form-label">
                    {{ $t('feeDetail.batchId') }}
                  </label>
                  <label>{{ feeDetailInfo.batchId }}</label>
                </div>
              </el-col>
            </el-row>

            <el-row>
              <el-col :span="6">
                <div class="form-group">
                  <label class="col-form-label">
                    {{ $t('feeDetail.startTime') }}
                  </label>
                  <label>{{ getEndTime(feeDetailInfo) }}</label>
                </div>
              </el-col>
              <el-col :span="6">
                <div class="form-group">
                  <label class="col-form-label">
                    {{ $t('feeDetail.endTime') }}
                  </label>
                  <label>{{ getDeadlineTime(feeDetailInfo) }}</label>
                </div>
              </el-col>
              <el-col :span="6">
                <div class="form-group">
                  <label class="col-form-label">
                    {{ $t('feeDetail.owedAmount') }}
                  </label>
                  <label>{{ feeDetailInfo.amountOwed }}</label>
                </div>
              </el-col>
            </el-row>

            <el-row>
              <el-col :span="6" v-for="(item, index) in feeDetailInfo.attrs" :key="index">
                <div class="form-group">
                  <label class="col-form-label">
                    {{ item.specCdName }}:
                  </label>
                  <label>{{ item.value }}</label>
                </div>
              </el-col>
            </el-row>
          </el-col>
        </el-row>
      </div>

      <div class="vc-line-primary margin-top"></div>

      <div class="margin-top-sm">
        <el-tabs v-model="feeDetailInfo._currentTab" @tab-click="changeTab(feeDetailInfo._currentTab)">
          <el-tab-pane :label="$t('feeDetail.paymentRecord')" name="feeDetailHisFee"></el-tab-pane>
          <el-tab-pane :label="$t('feeDetail.monthlyView')" name="feeDetailMonthFee"></el-tab-pane>
          <el-tab-pane v-if="feeDetailInfo.payerObjType == '3333'" :label="$t('feeDetail.relatedRoom')"
            name="feeDetailRoom">
          </el-tab-pane>
          <el-tab-pane v-if="feeDetailInfo.payerObjType == '6666'" :label="$t('feeDetail.relatedCar')"
            name="feeDetailCar">
          </el-tab-pane>
          <el-tab-pane v-if="feeDetailInfo.payerObjType == '7777'" :label="$t('feeDetail.relatedContract')"
            name="feeDetailContract">
          </el-tab-pane>
          <el-tab-pane :label="$t('feeDetail.modificationRecord')" name="feeDetailHis"></el-tab-pane>
          <el-tab-pane :label="$t('feeDetail.feeItem')" name="feeDetailConfig"></el-tab-pane>
          <el-tab-pane :label="$t('feeDetail.owner')" name="feeDetailOwner"></el-tab-pane>
          <el-tab-pane :label="$t('feeDetail.sameFeeObj')" name="feeDetailFeeObj"></el-tab-pane>
          <el-tab-pane :label="$t('feeDetail.relatedMeter')" name="feeDetailMeter"></el-tab-pane>
          <el-tab-pane :label="$t('feeDetail.relatedImport')" name="feeDetailImport"></el-tab-pane>
          <el-tab-pane :label="$t('feeDetail.discountApply')" name="feeDetailDiscount"></el-tab-pane>
          <el-tab-pane :label="$t('feeDetail.reprintReceipt')" name="feeDetailReceipt"></el-tab-pane>
          <el-tab-pane v-if="feeDetailInfo.feeFlag != '2006012'" :label="$t('feeDetail.feeSplit')"
            name="feeDetailSub"></el-tab-pane>
          <el-tab-pane v-if="feeDetailInfo.feeFlag == '2006012'" :label="$t('feeDetail.billRule')"
            name="feeDetailFeeRule"></el-tab-pane>
        </el-tabs>
      </div>
    </div>

    <div class="white-bg padding-left padding-right padding-top border-radius-bottom">
      <component :is="feeDetailInfo._currentTab" :key="feeDetailInfo._currentTab" :ref="feeDetailInfo._currentTab"
        :feeId="feeDetailInfo.feeId" :payerObjId="feeDetailInfo.payerObjId" :configId="feeDetailInfo.configId"
        :state="feeDetailInfo.state" :ownerId="ownerId">
      </component>
    </div>
  </div>
</template>

<script>
import { getCommunityId } from '@/api/community/communityApi'
import { getFeeDetail } from '@/api/fee/feeDetailApi'
import FeeDetailHisFee from '@/components/fee/feeDetailHisFee'
import FeeDetailMonthFee from '@/components/fee/feeDetailMonthFee'
import FeeDetailRoom from '@/components/fee/feeDetailRoom'
import FeeDetailCar from '@/components/fee/feeDetailCar'
import FeeDetailContract from '@/components/fee/feeDetailContract'
import FeeDetailConfig from '@/components/fee/feeDetailConfig'
import FeeDetailHis from '@/components/fee/feeDetailHis'
import FeeDetailOwner from '@/components/fee/feeDetailOwner'
import FeeDetailFeeObj from '@/components/fee/FeeDetailFeeObj'
import FeeDetailMeter from '@/components/fee/feeDetailMeter'
import FeeDetailImport from '@/components/fee/feeDetailImport'
import FeeDetailDiscount from '@/components/fee/feeDetailDiscount'
import FeeDetailReceipt from '@/components/fee/feeDetailReceipt'
import FeeDetailSub from '@/components/fee/feeDetailSub'
import FeeDetailFeeRule from '@/components/fee/feeDetailFeeRule'

export default {
  name: 'FeeDetail',
  components: {
    FeeDetailHisFee,
    FeeDetailMonthFee,
    FeeDetailRoom,
    FeeDetailCar,
    FeeDetailContract,
    FeeDetailConfig,
    FeeDetailHis,
    FeeDetailOwner,
    FeeDetailFeeObj,
    FeeDetailMeter,
    FeeDetailImport,
    FeeDetailDiscount,
    FeeDetailReceipt,
    FeeDetailSub,
    FeeDetailFeeRule,
  },
  data() {
    return {
      feeDetailInfo: {
        feeId: '',
        configId: '',
        feeFlag: '',
        feeFlagName: '',
        feeTypeCdName: '',
        payerObjName: '',
        payerObjId: '',
        payerObjType: '',
        feeName: '',
        stateName: '',
        state: '',
        startTime: '',
        batchId: '',
        endTime: '',
        deadlineTime: '',
        amountOwed: '',
        attrs: [],
        _currentTab: 'feeDetailHisFee',
        needBack: false
      },
      communityId: ''
    }
  },
  computed: {
    ownerId() {
      const ownerAttr = this.feeDetailInfo.attrs.find(item => item.specCd === '390007')
      return ownerAttr ? ownerAttr.value : ''
    }
  },
  created() {
    this.communityId = getCommunityId()
    this.feeDetailInfo.feeId = this.$route.query.feeId
    if (this.feeDetailInfo.feeId) {
      this.loadFeeDetailInfo()
    }
  },
  methods: {
    async loadFeeDetailInfo() {
      try {
        const params = {
          page: 1,
          row: 1,
          feeId: this.feeDetailInfo.feeId,
          communityId: this.communityId
        }
        const response = await getFeeDetail(params)
        const feeInfo = response.fees[0]
        Object.assign(this.feeDetailInfo, feeInfo)
        this.feeDetailInfo.attrs = feeInfo.feeAttrs || []
        this.changeTab(this.feeDetailInfo._currentTab)
      } catch (error) {
        console.error('Failed to load fee detail:', error)
      }
    },
    changeTab(tab) {
      this.feeDetailInfo._currentTab = tab
      setTimeout(() => {
        this.$refs[tab].open({
          feeId: this.feeDetailInfo.feeId,
          payerObjId: this.feeDetailInfo.payerObjId,
          configId: this.feeDetailInfo.configId,
          state: this.feeDetailInfo.state,
          ownerId: this.ownerId
        })
      }, 500)
    },
    getDeadlineTime(fee) {
      if (fee.amountOwed == 0 && fee.endTime == fee.deadlineTime) {
        return "-"
      }
      if (fee.state == '2009001') {
        return "-"
      }
      return fee.deadlineTime
    },
    getEndTime(fee) {
      if (fee.state == '2009001') {
        return "-"
      }
      return fee.endTime
    }
  }
}
</script>

<style scoped>
.fee-detail-container {
  background-color: #f5f5f5;
  padding: 20px;
}

.white-bg {
  background-color: white;
}

.padding-left {
  padding-left: 15px;
}

.padding-right {
  padding-right: 15px;
}

.padding-top {
  padding-top: 15px;
}

.border-radius-top {
  border-radius: 4px 4px 0 0;
}

.border-radius-bottom {
  border-radius: 0 0 4px 4px;
}

.flex {
  display: flex;
}

.justify-between {
  justify-content: space-between;
}

.text-title {
  font-size: 18px;
  font-weight: bold;
}

.margin-top {
  margin-top: 15px;
}

.margin-top-sm {
  margin-top: 10px;
}

.form-group {
  margin-bottom: 15px;
  text-align: left;
}

.col-form-label {
  margin-bottom: 5px;
}

.vc-line-primary {
  height: 1px;
  background-color: #e8e8e8;
}
</style>