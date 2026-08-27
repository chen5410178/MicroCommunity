<template>
  <div class="car-detail-container padding">
    <div class="white-bg padding-left padding-right padding-top border-radius-top">
      <el-row type="flex" justify="space-between">
        <div class="text-title">
          {{ $t('carDetail.vehicleInfo') }}
        </div>
      </el-row>

      <!-- Vehicle Information -->
      <div class="margin-top">
        <el-form class="text-left">
          <el-row :gutter="20">
            <el-col :span="6">
              <el-form-item :label="$t('carDetail.licensePlate')">
                <label>{{ carDetailInfo.carNum || '-' }}</label>
              </el-form-item>
            </el-col>
            <el-col :span="6">
              <el-form-item :label="$t('carDetail.carType')">
                <label>{{ carDetailInfo.leaseTypeName || '-' }}</label>
              </el-form-item>
            </el-col>
            <el-col :span="6">
              <el-form-item :label="$t('carDetail.licenseType')">
                <label>{{ carDetailInfo.carTypeName || '-' }}</label>
              </el-form-item>
            </el-col>
            <el-col :span="6">
              <el-form-item :label="$t('carDetail.leaseStart')">
                <label>{{ carDetailInfo.startTime || '-' }}</label>
              </el-form-item>
            </el-col>
          </el-row>

          <el-row :gutter="20">
            <el-col :span="6">
              <el-form-item :label="$t('carDetail.leaseEnd')">
                <label>{{ carDetailInfo.endTime || '-' }}</label>
              </el-form-item>
            </el-col>
            <el-col :span="6">
              <el-form-item :label="$t('carDetail.remark')">
                <label>{{ carDetailInfo.remark || '-' }}</label>
              </el-form-item>
            </el-col>
            <el-col :span="6">
              <el-form-item :label="$t('carDetail.parkingLot')">
                <label>{{ carDetailInfo.areaNum || '-' }}</label>
              </el-form-item>
            </el-col>
            <el-col :span="6">
              <el-form-item :label="$t('carDetail.status')">
                <label>{{ carDetailInfo.stateName || '-' }}</label>
              </el-form-item>
            </el-col>
          </el-row>

          <el-row :gutter="20">
            <el-col :span="6">
              <el-form-item :label="$t('carDetail.relatedHouse')">
                <label>{{ carDetailInfo.roomName || '-' }}</label>
              </el-form-item>
            </el-col>
            <el-col :span="6">
              <el-form-item :label="$t('carDetail.contact')">
                <label>{{ carDetailInfo.contact || carDetailInfo.ownerName || '-' }}</label>
              </el-form-item>
            </el-col>
            <el-col :span="6">
              <el-form-item :label="$t('carDetail.tel')">
                <label>{{ carDetailInfo.tel || carDetailInfo.link || '-' }}</label>
              </el-form-item>
            </el-col>
          </el-row>
        </el-form>
      </div>

      <divider />

      <div class="margin-top-sm">
        <el-tabs v-model="carDetailInfo._currentTab" @tab-click="changeTab(carDetailInfo._currentTab)">
          <el-tab-pane :label="$t('carDetail.vehicleFee')" name="carDetailFee"></el-tab-pane>
          <el-tab-pane :label="$t('carDetail.paymentRecord')" name="ownerDetailHisFee"></el-tab-pane>
          <el-tab-pane :label="$t('carDetail.modificationRecord')" name="carDetailHis"></el-tab-pane>
          <el-tab-pane :label="$t('carDetail.memberVehicle')" name="carDetailMember"></el-tab-pane>
          <el-tab-pane :label="$t('carDetail.ownerInfo')" name="carDetailOwner"></el-tab-pane>
          <el-tab-pane :label="$t('carDetail.ownerHouse')" name="ownerDetailRoom"></el-tab-pane>
          <el-tab-pane :label="$t('carDetail.ownerVehicle')" name="ownerDetailCar"></el-tab-pane>
          <el-tab-pane :label="$t('carDetail.ownerArrears')" name="ownerDetailOweFee"></el-tab-pane>
          <el-tab-pane :label="$t('carDetail.licenseSync')" name="carDetailTransactionCar"></el-tab-pane>
          <el-tab-pane :label="$t('carDetail.parkingApplication')" name="carDetailApplyParkingSpace"></el-tab-pane>
          <el-tab-pane :label="$t('carDetail.accessRecord')" name="carDetailCarInout"></el-tab-pane>
          <el-tab-pane :label="$t('carDetail.coupon')" name="carDetailCoupon"></el-tab-pane>
          <el-tab-pane :label="$t('carDetail.couponVerification')" name="carDetailCouponOrder"></el-tab-pane>
        </el-tabs>
      </div>
    </div>

    <div class="white-bg padding-left padding-right padding-top border-radius-bottom">
      <car-detail-fee v-if="carDetailInfo._currentTab === 'carDetailFee'" ref="carDetailFee"></car-detail-fee>
      <owner-detail-room v-if="carDetailInfo._currentTab === 'ownerDetailRoom'"
        ref="ownerDetailRoom"></owner-detail-room>
      <owner-detail-car v-if="carDetailInfo._currentTab === 'ownerDetailCar'" ref="ownerDetailCar"></owner-detail-car>
      <owner-detail-owe-fee v-if="carDetailInfo._currentTab === 'ownerDetailOweFee'"
        ref="ownerDetailOweFee"></owner-detail-owe-fee>
      <owner-detail-his-fee v-if="carDetailInfo._currentTab === 'ownerDetailHisFee'"
        ref="ownerDetailHisFee"></owner-detail-his-fee>
      <car-detail-his v-if="carDetailInfo._currentTab === 'carDetailHis'" ref="carDetailHis"></car-detail-his>
      <car-detail-member v-if="carDetailInfo._currentTab === 'carDetailMember'"
        ref="carDetailMember"></car-detail-member>
      <car-detail-owner v-if="carDetailInfo._currentTab === 'carDetailOwner'" ref="carDetailOwner"></car-detail-owner>
      <car-detail-transaction-car v-if="carDetailInfo._currentTab === 'carDetailTransactionCar'"
        ref="carDetailTransactionCar"></car-detail-transaction-car>
      <car-detail-apply-parking-space v-if="carDetailInfo._currentTab === 'carDetailApplyParkingSpace'"
        ref="carDetailApplyParkingSpace"></car-detail-apply-parking-space>
      <car-detail-car-inout v-if="carDetailInfo._currentTab === 'carDetailCarInout'"
        ref="carDetailCarInout"></car-detail-car-inout>
      <car-detail-coupon v-if="carDetailInfo._currentTab === 'carDetailCoupon'"
        ref="carDetailCoupon"></car-detail-coupon>
      <car-detail-coupon-order v-if="carDetailInfo._currentTab === 'carDetailCouponOrder'"
        ref="carDetailCouponOrder"></car-detail-coupon-order>
    </div>

    <edit-car ref="editCar"></edit-car>
  </div>
</template>

<script>
import CarDetailFee from '@/components/car/carDetailFee'
import OwnerDetailRoom from '@/components/owner/ownerDetailRoom'
import OwnerDetailCar from '@/components/owner/ownerDetailCar'
import OwnerDetailOweFee from '@/components/car/ownerDetailOweFee'
import OwnerDetailHisFee from '@/components/owner/ownerDetailHisFee'
import CarDetailHis from '@/components/system/carDetailHis'
import CarDetailMember from '@/components/car/carDetailMember'
import CarDetailOwner from '@/components/fee/carDetailOwner'
import CarDetailTransactionCar from '@/components/car/carDetailTransactionCar'
import CarDetailApplyParkingSpace from '@/components/car/carDetailApplyParkingSpace'
import CarDetailCarInout from '@/components/car/carDetailCarInout'
import CarDetailCoupon from '@/components/car/carDetailCoupon'
import CarDetailCouponOrder from '@/components/car/carDetailCouponOrder'
import EditCar from '@/components/car/editCar'
import { getCommunityId } from '@/api/community/communityApi'
import { getOwnerCarDetail } from '@/api/car/carDetailApi'
import Divider from '@/components/system/divider'

export default {
  name: 'CarDetail',
  components: {
    CarDetailFee,
    OwnerDetailRoom,
    OwnerDetailCar,
    OwnerDetailOweFee,
    OwnerDetailHisFee,
    CarDetailHis,
    CarDetailMember,
    CarDetailOwner,
    CarDetailTransactionCar,
    CarDetailApplyParkingSpace,
    CarDetailCarInout,
    CarDetailCoupon,
    CarDetailCouponOrder,
    EditCar,
    Divider
  },
  data() {
    return {
      carDetailInfo: {
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
        ownerId: '',
        roomName: '',
        carNumType: '',
        paId: '',
        _currentTab: 'carDetailFee',
        needBack: false
      },
      communityId: ''
    }
  },
  created() {
    this.communityId = getCommunityId()
    this.carDetailInfo.memberId = this.$route.query.memberId
    let tab = this.$route.query.tab
    if (tab) {
      this.carDetailInfo._currentTab = tab
    }
    if (this.carDetailInfo.memberId) {
      this._loadCarDetailInfo()
    }
  },
  methods: {
    _loadCarDetailInfo() {
      const params = {
        page: 1,
        row: 1,
        memberId: this.carDetailInfo.memberId,
        communityId: this.communityId
      }

      getOwnerCarDetail(params)
        .then(response => {
          const carInfo = response.data[0]
          Object.assign(this.carDetailInfo, carInfo)
          this.changeTab(this.carDetailInfo._currentTab)
        })
        .catch(error => {
          console.error('Failed to load car detail:', error)
        })
    },
    changeTab(tab) {
      this.carDetailInfo._currentTab = tab
      const tabData = {
        memberId: this.carDetailInfo.memberId,
        carId: this.carDetailInfo.carId,
        ownerId: this.carDetailInfo.ownerId,
        carNum: this.carDetailInfo.carNum,
        paId: this.carDetailInfo.paId,
        areaNum: this.carDetailInfo.areaNum
      }
      setTimeout(() => {
        if (this.$refs[tab]) {
          if (tab === 'ownerDetailHisFee' || tab == 'ownerDetailRoom' || tab == 'ownerDetailCar') {
            this.$refs[tab].open(this.carDetailInfo.ownerId)
          } else {
            this.$refs[tab].open(tabData)
          }
        }
      }, 100)

    },
    _openEditDetailOwnerCar() {
      this.$refs.editCar.open(this.carDetailInfo)
    },
  }
}
</script>

<style scoped>
.white-bg {
  background: #fff;
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
</style>