<template>
  <div class="admin-car-detail padding">
    <div class="white-bg padding-left padding-right padding-top border-radius-top">
      <div class="flex justify-between">
        <div class="text-title">
          {{ $t('adminCarDetail.vehicleInfo') }}
        </div>
      </div>

      <!-- 车辆信息 -->
      <div class="margin-top text-left">
        <el-row>
          <el-col :span="24">
            <el-row>
              <el-col :span="6">
                <div class="form-group">
                  <label class="col-form-label">
                    {{ $t('adminCarDetailInfo.licensePlate') }}
                  </label>
                  <label>{{ adminCarDetailInfo.carNum }}</label>
                </div>
              </el-col>
              <el-col :span="6">
                <div class="form-group">
                  <label class="col-form-label">
                    {{ $t('adminCarDetailInfo.carBrand') }}
                  </label>
                  <label>{{ adminCarDetailInfo.carBrand || '-' }}</label>
                </div>
              </el-col>
              <el-col :span="6">
                <div class="form-group">
                  <label class="col-form-label">
                    {{ $t('adminCarDetailInfo.carType') }}
                  </label>
                  <label>{{ adminCarDetailInfo.leaseTypeName }}</label>
                </div>
              </el-col>
              <el-col :span="6">
                <div class="form-group">
                  <label class="col-form-label">
                    {{ $t('adminCarDetailInfo.color') }}
                  </label>
                  <label>{{ adminCarDetailInfo.carColor || '-' }}</label>
                </div>
              </el-col>
            </el-row>

            <el-row>
              <el-col :span="6">
                <div class="form-group">
                  <label class="col-form-label">
                    {{ $t('adminCarDetailInfo.plateType') }}
                  </label>
                  <label>{{ adminCarDetailInfo.carTypeName }}</label>
                </div>
              </el-col>
              <el-col :span="6">
                <div class="form-group">
                  <label class="col-form-label">
                    {{ $t('adminCarDetailInfo.leaseStartTime') }}
                  </label>
                  <label>{{ adminCarDetailInfo.startTime }}</label>
                </div>
              </el-col>
              <el-col :span="6">
                <div class="form-group">
                  <label class="col-form-label">
                    {{ $t('adminCarDetailInfo.leaseEndTime') }}
                  </label>
                  <label>{{ adminCarDetailInfo.endTime }}</label>
                </div>
              </el-col>
              <el-col :span="6">
                <div class="form-group">
                  <label class="col-form-label">
                    {{ $t('adminCarDetailInfo.remark') }}
                  </label>
                  <label>{{ adminCarDetailInfo.remark }}</label>
                </div>
              </el-col>
            </el-row>

            <el-row>
              <el-col :span="6">
                <div class="form-group">
                  <label class="col-form-label">
                    {{ $t('adminCarDetailInfo.parkingLot') }}
                  </label>
                  <label>{{ adminCarDetailInfo.areaNum }}</label>
                </div>
              </el-col>
              <el-col :span="6">
                <div class="form-group">
                  <label class="col-form-label">
                    {{ $t('adminCarDetailInfo.parkingSpace') }}
                  </label>
                  <label>{{ adminCarDetailInfo.num }}</label>
                </div>
              </el-col>
              <el-col :span="6">
                <div class="form-group">
                  <label class="col-form-label">
                    {{ $t('adminCarDetailInfo.status') }}
                  </label>
                  <label>{{ adminCarDetailInfo.stateName }}</label>
                </div>
              </el-col>
              <el-col :span="6">
                <div class="form-group">
                  <label class="col-form-label">
                    {{ $t('adminCarDetailInfo.relatedHouse') }}
                  </label>
                  <label>{{ adminCarDetailInfo.roomName }}</label>
                </div>
              </el-col>
            </el-row>
          </el-col>
        </el-row>
      </div>

      <divider></divider>


      <div class="margin-top-sm">
        <el-tabs v-model="adminCarDetailInfo._currentTab" @tab-click="handleTabClick(adminCarDetailInfo._currentTab)">
          <el-tab-pane :label="$t('adminCarDetail.vehicleFee')" name="aCarDetailFee"></el-tab-pane>
          <el-tab-pane :label="$t('adminCarDetail.paymentRecord')" name="aRoomDetailHisFee"></el-tab-pane>
          <el-tab-pane :label="$t('adminCarDetail.modificationRecord')" name="aCarDetailHis"></el-tab-pane>
          <el-tab-pane :label="$t('adminCarDetail.memberVehicle')" name="aCarDetailMember"></el-tab-pane>
          <el-tab-pane :label="$t('adminCarDetail.ownerInfo')" name="aCarDetailOwner"></el-tab-pane>
          <el-tab-pane :label="$t('adminCarDetail.ownerHouse')" name="aRoomDetailRoom"></el-tab-pane>
          <el-tab-pane :label="$t('adminCarDetail.ownerVehicle')" name="aRoomDetailCar"></el-tab-pane>
          <el-tab-pane :label="$t('adminCarDetail.licensePlateSync')" name="aCarDetailTransactionCar"></el-tab-pane>
          <el-tab-pane :label="$t('adminCarDetail.parkingSpaceApplication')" name="aCarDetailApplySpace"></el-tab-pane>
          <el-tab-pane :label="$t('adminCarDetail.accessRecord')" name="aCarDetailCarInout"></el-tab-pane>
        </el-tabs>
      </div>
    </div>

    <div class="white-bg padding-left padding-right padding-top border-radius-bottom">
      <a-car-detail-fee v-if="adminCarDetailInfo._currentTab === 'aCarDetailFee'" ref="aCarDetailFee"></a-car-detail-fee>
      <a-room-detail-his-fee v-if="adminCarDetailInfo._currentTab === 'aRoomDetailHisFee'"
        ref="aRoomDetailHisFee"></a-room-detail-his-fee>
      <a-car-detail-his v-if="adminCarDetailInfo._currentTab === 'aCarDetailHis'" ref="aCarDetailHis"></a-car-detail-his>
      <a-car-detail-member v-if="adminCarDetailInfo._currentTab === 'aCarDetailMember'"
        ref="aCarDetailMember"></a-car-detail-member>
      <a-car-detail-owner v-if="adminCarDetailInfo._currentTab === 'aCarDetailOwner'"
        ref="aCarDetailOwner"></a-car-detail-owner>
      <a-room-detail-room v-if="adminCarDetailInfo._currentTab === 'aRoomDetailRoom'"
        ref="aRoomDetailRoom"></a-room-detail-room>
      <a-room-detail-car v-if="adminCarDetailInfo._currentTab === 'aRoomDetailCar'"
        ref="aRoomDetailCar"></a-room-detail-car>
      <a-car-detail-transaction-car v-if="adminCarDetailInfo._currentTab === 'aCarDetailTransactionCar'"
        ref="aCarDetailTransactionCar"></a-car-detail-transaction-car>
      <a-car-detail-car-inout v-if="adminCarDetailInfo._currentTab === 'aCarDetailCarInout'"
        ref="aCarDetailCarInout"></a-car-detail-car-inout>
      <a-car-detail-apply-space v-if="adminCarDetailInfo._currentTab === 'aCarDetailApplySpace'"
        ref="aCarDetailApplySpace"></a-car-detail-apply-space>
    </div>
  </div>
</template>

<script>
import { getCommunityId } from '@/api/community/communityApi'
import { getOwnerCarInfo } from '@/api/aCommunity/adminCarDetailApi'
import ACarDetailFee from '@/components/aCommunity/aCarDetailFee'
import ARoomDetailHisFee from '@/components/fee/aRoomDetailHisFee'
import ACarDetailHis from '@/components/aCommunity/aCarDetailHis'
import ACarDetailMember from '@/components/aCommunity/aCarDetailMember'
import ACarDetailOwner from '@/components/aCommunity/aCarDetailOwner'
import ARoomDetailRoom from '@/components/fee/aRoomDetailRoom'
import ARoomDetailCar from '@/components/aCommunity/aRoomDetailCar'
import ACarDetailTransactionCar from '@/components/aCommunity/aCarDetailTransactionCar'
import ACarDetailApplySpace from '@/components/aCommunity/aCarDetailApplySpace'
import ACarDetailCarInout from '@/components/aCommunity/aCarDetailCarInout'
import divider from '@/components/system/divider'

export default {
  name: 'AdminCarDetailList',
  components: {
    ACarDetailFee,
    ARoomDetailHisFee,
    ACarDetailHis,
    ACarDetailMember,
    ACarDetailOwner,
    ARoomDetailRoom,
    ARoomDetailCar,
    ACarDetailTransactionCar,
    ACarDetailApplySpace,
    ACarDetailCarInout,
    divider
  },
  data() {
    return {
      adminCarDetailInfo: {
        memberId: '',
        carId: '',
        carNum: '',
        carTypeCd: '',
        carTypeCdName: '',
        carType: '',
        carTypeName: '',
        startTime: '',
        endTime: '',
        leaseType: '',
        leaseTypeName: '',
        areaNum: '',
        num: '',
        remark: '',
        stateName: '',
        carColor: '',
        carBrand: '',
        ownerId: '',
        roomName: '',
        carNumType: '',
        paId: '',
        _currentTab: 'aCarDetailFee',
        needBack: false
      },
      communityId: ''
    }
  },
  created() {
    this.communityId = getCommunityId()
    this.adminCarDetailInfo.memberId = this.$route.query.memberId
    if (this.adminCarDetailInfo.memberId) {
      this.loadCarDetailInfo()
    }
  },
  methods: {
    async loadCarDetailInfo() {
      const params = {
        page: 1,
        row: 1,
        memberId: this.adminCarDetailInfo.memberId
      }

      const response = await getOwnerCarInfo(params)
      const carInfo = response.data[0]
      Object.assign(this.adminCarDetailInfo, carInfo)
      this.handleTabClick(this.adminCarDetailInfo._currentTab)
    },
    handleTabClick(tab) {
      this.adminCarDetailInfo._currentTab = tab
      const tabData = {
        memberId: this.adminCarDetailInfo.memberId,
        carId: this.adminCarDetailInfo.carId,
        ownerId: this.adminCarDetailInfo.ownerId,
        carNum: this.adminCarDetailInfo.carNum,
        paId: this.adminCarDetailInfo.paId,
        areaNum: this.adminCarDetailInfo.areaNum
      }

      setTimeout(() => {
        this.$refs[this.adminCarDetailInfo._currentTab].open(tabData)
      }, 1000)
    }
  }
}
</script>

<style scoped>
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

}

.col-form-label {
  margin-bottom: 5px;
  color: #606266;
}
</style>