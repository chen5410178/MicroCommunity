<template>
  <div class="admin-fee-detail-container">
    <div class="white-bg padding-left padding-right padding-top border-radius-top">
      <div class="flex justify-between">
        <div class="text-title">
          {{ $t('adminFeeDetail.feeInfo') }}
        </div>
      </div>

      <!-- 费用信息 -->
      <div class="margin-top">
        <el-row :gutter="20">
          <el-col :span="24">
            <el-row :gutter="20">
              <el-col :span="6">
                <div class="form-group">
                  <label class="col-form-label">
                    {{ $t('adminFeeDetailInfo.feeId') }}:
                  </label>
                  <label>{{ adminFeeDetailInfo.feeId }}</label>
                </div>
              </el-col>
              <el-col :span="6">
                <div class="form-group">
                  <label class="col-form-label">
                    {{ $t('adminFeeDetailInfo.feeFlag') }}:
                  </label>
                  <label>{{ adminFeeDetailInfo.feeFlagName || '-' }}</label>
                </div>
              </el-col>
              <el-col :span="6">
                <div class="form-group">
                  <label class="col-form-label">
                    {{ $t('adminFeeDetailInfo.feeType') }}:
                  </label>
                  <label>{{ adminFeeDetailInfo.feeTypeCdName }}</label>
                </div>
              </el-col>
              <el-col :span="6">
                <div class="form-group">
                  <label class="col-form-label">
                    {{ $t('adminFeeDetailInfo.payerObj') }}:
                  </label>
                  <label>{{ adminFeeDetailInfo.payerObjName || '-' }}</label>
                </div>
              </el-col>
            </el-row>

            <el-row :gutter="20">
              <el-col :span="6">
                <div class="form-group">
                  <label class="col-form-label">
                    {{ $t('adminFeeDetailInfo.feeItem') }}:
                  </label>
                  <label>{{ adminFeeDetailInfo.feeName }}</label>
                </div>
              </el-col>
              <el-col :span="6">
                <div class="form-group">
                  <label class="col-form-label">
                    {{ $t('adminFeeDetailInfo.feeState') }}:
                  </label>
                  <label>{{ adminFeeDetailInfo.stateName }}</label>
                </div>
              </el-col>
              <el-col :span="6">
                <div class="form-group">
                  <label class="col-form-label">
                    {{ $t('adminFeeDetailInfo.createTime') }}:
                  </label>
                  <label>{{ adminFeeDetailInfo.startTime }}</label>
                </div>
              </el-col>
              <el-col :span="6">
                <div class="form-group">
                  <label class="col-form-label">
                    {{ $t('adminFeeDetailInfo.batchId') }}:
                  </label>
                  <label>{{ adminFeeDetailInfo.batchId }}</label>
                </div>
              </el-col>
            </el-row>

            <el-row :gutter="20">
              <el-col :span="6">
                <div class="form-group">
                  <label class="col-form-label">
                    {{ $t('adminFeeDetailInfo.startTime') }}:
                  </label>
                  <label>{{ getEndTime(adminFeeDetailInfo) }}</label>
                </div>
              </el-col>
              <el-col :span="6">
                <div class="form-group">
                  <label class="col-form-label">
                    {{ $t('adminFeeDetailInfo.endTime') }}:
                  </label>
                  <label>{{ getDeadlineTime(adminFeeDetailInfo) }}</label>
                </div>
              </el-col>
              <el-col :span="6">
                <div class="form-group">
                  <label class="col-form-label">
                    {{ $t('adminFeeDetailInfo.owedAmount') }}:
                  </label>
                  <label>{{ adminFeeDetailInfo.amountOwed }}</label>
                </div>
              </el-col>
            </el-row>

            <el-row :gutter="20">
              <el-col :span="6" v-for="(item, index) in adminFeeDetailInfo.attrs" :key="index">
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

      <divider />
      <div class="margin-top-sm">
        <el-tabs v-model="adminFeeDetailInfo._currentTab" @tab-click="changeTab(adminFeeDetailInfo._currentTab)">
          <el-tab-pane :label="$t('adminFeeDetail.paymentRecord')" name="aRoomDetailHisFee"></el-tab-pane>
          <el-tab-pane v-if="adminFeeDetailInfo.payerObjType == '3333'" :label="$t('adminFeeDetail.relatedRoom')"
            name="aRoomDetailRoom">
          </el-tab-pane>
          <el-tab-pane v-if="adminFeeDetailInfo.payerObjType == '6666'" :label="$t('adminFeeDetail.relatedCar')"
            name="aRoomDetailCar">
          </el-tab-pane>
          <el-tab-pane v-if="adminFeeDetailInfo.payerObjType == '7777'" :label="$t('adminFeeDetail.relatedContract')"
            name="adminFeeDetailContract">
          </el-tab-pane>
          <el-tab-pane :label="$t('adminFeeDetail.modifyRecord')" name="aFeeDetailHis"></el-tab-pane>
          <el-tab-pane :label="$t('adminFeeDetail.feeItem')" name="aFeeDetailConfig"></el-tab-pane>
          <el-tab-pane :label="$t('adminFeeDetail.owner')" name="aRoomDetailOwner"></el-tab-pane>
          <el-tab-pane :label="$t('adminFeeDetail.sameFeeObj')" name="aFeeDetailFeeObj"></el-tab-pane>
          <el-tab-pane :label="$t('adminFeeDetail.relatedMeter')" name="aRoomDetailMeterWaterLog"></el-tab-pane>
          <el-tab-pane :label="$t('adminFeeDetail.relatedImport')" name="aFeeDetailImport"></el-tab-pane>
          <el-tab-pane :label="$t('adminFeeDetail.discountApply')" name="aFeeDetailDiscount"></el-tab-pane>
          <el-tab-pane :label="$t('adminFeeDetail.reprintReceipt')" name="aRoomDetailReceipt"></el-tab-pane>
          <el-tab-pane v-if="adminFeeDetailInfo.feeFlag != '2006012'" :label="$t('adminFeeDetail.feeSplit')"
            name="aFeeDetailSub">
          </el-tab-pane>
        </el-tabs>
      </div>
    </div>

    <div class="white-bg padding-left padding-right padding-top border-radius-bottom">
      <component :is="adminFeeDetailInfo._currentTab" :ref="adminFeeDetailInfo._currentTab"
        @refresh="loadAdminFeeDetailInfo">
      </component>
    </div>
  </div>
</template>

<script>
import { getFeeDetail } from '@/api/aCommunity/adminFeeDetailApi'
import { getCommunityId } from '@/api/community/communityApi'
//import { getDict } from '@/api/community/communityApi'

// 导入所有子组件
import ARoomDetailHisFee from '@/components/fee/aRoomDetailHisFee.vue'
import ARoomDetailRoom from '@/components/fee/aRoomDetailRoom.vue'
import ARoomDetailCar from '@/components/aCommunity/aRoomDetailCar.vue'
import AFeeDetailHis from '@/components/aCommunity/aFeeDetailHis.vue'
import AFeeDetailConfig from '@/components/aCommunity/aFeeDetailConfig.vue'
import ARoomDetailOwner from '@/components/fee/aRoomDetailOwner.vue'
import AFeeDetailFeeObj from '@/components/aCommunity/aFeeDetailFeeObj.vue'
import ARoomDetailMeterWaterLog from '@/components/fee/aRoomDetailMeterWaterLog.vue'
import AFeeDetailImport from '@/components/aCommunity/aFeeDetailImport.vue'
import AFeeDetailDiscount from '@/components/aCommunity/aFeeDetailDiscount.vue'
import ARoomDetailReceipt from '@/components/fee/aRoomDetailReceipt.vue'
import AFeeDetailSub from '@/components/aCommunity/aFeeDetailSub.vue'
import divider from '@/components/system/divider.vue'

export default {
  name: 'AdminFeeDetailList',
  components: {
    ARoomDetailHisFee,
    ARoomDetailRoom,
    ARoomDetailCar,
    AFeeDetailHis,
    AFeeDetailConfig,
    ARoomDetailOwner,
    AFeeDetailFeeObj,
    ARoomDetailMeterWaterLog,
    AFeeDetailImport,
    AFeeDetailDiscount,
    ARoomDetailReceipt,
    AFeeDetailSub,
    divider
  },
  data() {
    return {
      adminFeeDetailInfo: {
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
        _currentTab: 'aRoomDetailHisFee',
        needBack: false
      },
      communityId: ''
    }
  },
  created() {
    this.communityId = getCommunityId()
    this.adminFeeDetailInfo.feeId = this.$route.query.feeId || ''
    if (this.adminFeeDetailInfo.feeId) {
      this.loadAdminFeeDetailInfo()
    }
  },
  methods: {
    async loadAdminFeeDetailInfo() {
      try {
        const params = {
          page: 1,
          row: 1,
          feeId: this.adminFeeDetailInfo.feeId
        }
        const response = await getFeeDetail(params)
        const feeInfo = response.fees[0]
        Object.assign(this.adminFeeDetailInfo, feeInfo)
        this.adminFeeDetailInfo.attrs = feeInfo.feeAttrs
        this.changeTab(this.adminFeeDetailInfo._currentTab)
      } catch (error) {
        console.error('加载费用详情失败:', error)
      }
    },
    changeTab(tab) {
      this.adminFeeDetailInfo._currentTab = tab
      let ownerId = ''
      this.adminFeeDetailInfo.attrs.forEach(item => {
        if (item.specCd === '390007') {
          ownerId = item.value
        }
      })

      setTimeout(() => {
        if (this.$refs[tab] && this.$refs[tab].open) {
          this.$refs[tab].open({
            feeId: this.adminFeeDetailInfo.feeId,
            payerObjId: this.adminFeeDetailInfo.payerObjId,
            roomId: this.adminFeeDetailInfo.payerObjId,
            carId: this.adminFeeDetailInfo.payerObjId,
            configId: this.adminFeeDetailInfo.configId,
            state: this.adminFeeDetailInfo.state,
            ownerId: ownerId
          })
        }
      }, 500)
    },
    getDeadlineTime(fee) {
      if (fee.amountOwed == 0 && fee.endTime == fee.deadlineTime) {
        return '-'
      }
      if (fee.state == '2009001') {
        return '-'
      }
      return fee.deadlineTime
    },
    getEndTime(fee) {
      if (fee.state == '2009001') {
        return '-'
      }
      return fee.endTime
    }
  }
}
</script>

<style scoped>
.admin-fee-detail-container {
  background-color: #f5f5f5;
  padding: 20px;
}

.white-bg {
  background-color: #fff;
}

.padding-left {
  padding-left: 20px;
}

.padding-right {
  padding-right: 20px;
}

.padding-top {
  padding-top: 20px;
}

.border-radius-top {
  border-radius: 4px 4px 0 0;
}

.border-radius-bottom {
  border-radius: 0 0 4px 4px;
}

.margin-top {
  margin-top: 20px;
}

.margin-top-sm {
  margin-top: 10px;
}

.text-title {
  font-size: 18px;
  font-weight: bold;
}

.flex {
  display: flex;
}

.justify-between {
  justify-content: space-between;
}

.vc-line-primary {
  height: 1px;
  background-color: #ebeef5;
}

.form-group {
  margin-bottom: 15px;
  text-align: left;
}

.col-form-label {
  margin-bottom: 5px;
  color: #606266;
  margin-right: 10px;
}
</style>