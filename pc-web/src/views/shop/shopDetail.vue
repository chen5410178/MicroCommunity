<template>
  <div class="white-bg padding-lg padding-top border-radius margin">
    <el-row type="flex" justify="space-between">
      <div class="text-title">{{ $t('shopDetail.shopInfo') }}</div>
      <div>
        <el-button v-if="shopDetailInfo.needBack" type="primary" size="small" style="margin-left:10px"
          @click="$router.go(-1)">
          <i class="el-icon-close"></i>{{ $t('common.back') }}
        </el-button>
      </div>
    </el-row>

    <div class="margin-top">
      <el-row style="min-height: 100px;">
        <el-col :span="24" class="text-left">
          <el-form>
            <el-row>
              <el-col :span="6">
                <el-form-item :label="$t('shopDetail.shopNumber')">
                  <div>{{ shopDetailInfo.shopNum }}</div>
                </el-form-item>
              </el-col>
              <el-col :span="6">
                <el-form-item :label="$t('shopDetail.location')">
                  <div>{{ shopDetailInfo.locationName }}</div>
                </el-form-item>
              </el-col>
              <el-col :span="6">
                <el-form-item :label="$t('shopDetail.builtUpArea')">
                  <div>{{ shopDetailInfo.builtUpArea }}㎡</div>
                </el-form-item>
              </el-col>
              <el-col :span="6">
                <el-form-item :label="$t('shopDetail.shopArea')">
                  <div>{{ shopDetailInfo.shopArea }}㎡</div>
                </el-form-item>
              </el-col>
              <el-col :span="6">
                <el-form-item :label="$t('shopDetail.rent')">
                  <div>{{ shopDetailInfo.shopRent }}</div>
                </el-form-item>
              </el-col>
              <el-col :span="6">
                <el-form-item :label="$t('shopDetail.state')">
                  <div>
                    <el-tag v-if="shopDetailInfo.state === '2008'" type="success" size="mini">{{ $t('shopDetail.idle') }}</el-tag>
                    <el-tag v-else-if="shopDetailInfo.state === '2006'" type="warning" size="mini">{{ $t('shopDetail.rented') }}</el-tag>
                    <el-tag v-else-if="shopDetailInfo.state === '2007'" type="info" size="mini">{{ $t('shopDetail.sold') }}</el-tag>
                    <span v-else>-</span>
                  </div>
                </el-form-item>
              </el-col>
              <el-col :span="6">
                <el-form-item :label="$t('shopDetail.leasePeriod')">
                  <div>{{ shopDetailInfo.startTime }} ~ {{ shopDetailInfo.endTime }}</div>
                </el-form-item>
              </el-col>
              <el-col :span="6">
                <el-form-item :label="$t('shopDetail.owner')">
                  <div v-if="shopDetailInfo.ownerId">
                    <a target="_blank" :href="'/#/views/owner/ownerDetail?ownerId=' + shopDetailInfo.ownerId">{{ shopDetailInfo.ownerName }}</a>
                  </div>
                  <div v-else>-</div>
                </el-form-item>
              </el-col>
              <el-col :span="6">
                <el-form-item :label="$t('shopDetail.contact')">
                  <div>{{ shopDetailInfo.link || '-' }}</div>
                </el-form-item>
              </el-col>
              <el-col :span="6">
                <el-form-item :label="$t('shopDetail.remark')">
                  <div>{{ shopDetailInfo.remark || '-' }}</div>
                </el-form-item>
              </el-col>
            </el-row>
          </el-form>
        </el-col>
      </el-row>
    </div>

    <divider />

    <div class="margin-top-sm">
      <el-tabs v-model="shopDetailInfo._currentTab" @tab-click="changeTab(shopDetailInfo._currentTab)">
        <el-tab-pane :label="$t('shopDetail.owner')" name="carDetailOwner"></el-tab-pane>
        <el-tab-pane :label="$t('shopDetail.room')" name="ownerDetailRoom"></el-tab-pane>
        <el-tab-pane :label="$t('shopDetail.member')" name="ownerDetailMember"></el-tab-pane>
        <el-tab-pane :label="$t('shopDetail.car')" name="ownerDetailCar"></el-tab-pane>
        <el-tab-pane :label="$t('shopDetail.shop')" name="shopDetailShop"></el-tab-pane>
      </el-tabs>
    </div>

    <car-detail-owner v-show="shopDetailInfo._currentTab === 'carDetailOwner'" ref="carDetailOwner"></car-detail-owner>
    <owner-detail-room v-show="shopDetailInfo._currentTab === 'ownerDetailRoom'" ref="ownerDetailRoom"></owner-detail-room>
    <owner-detail-member v-show="shopDetailInfo._currentTab === 'ownerDetailMember'" ref="ownerDetailMember"></owner-detail-member>
    <owner-detail-car v-show="shopDetailInfo._currentTab === 'ownerDetailCar'" ref="ownerDetailCar"></owner-detail-car>
    <shop-detail-shop v-show="shopDetailInfo._currentTab === 'shopDetailShop'" ref="shopDetailShop"></shop-detail-shop>
  </div>
</template>

<script>
import OwnerDetailMember from '@/components/owner/ownerDetailMember'
import OwnerDetailCar from '@/components/owner/ownerDetailCar'
import OwnerDetailRoom from '@/components/owner/ownerDetailRoom'
import ShopDetailShop from '@/components/shop/shopDetailShop'
import CarDetailOwner from '@/components/fee/carDetailOwner'
import { getShopDetail } from '@/api/shop/shopDetailApi'
import divider from '@/components/system/divider'

export default {
  name: 'ShopDetail',
  components: {
    OwnerDetailMember,
    OwnerDetailCar,
    OwnerDetailRoom,
    ShopDetailShop,
    CarDetailOwner,
    divider
  },
  data() {
    return {
      shopDetailInfo: {
        shopId: '',
        shopNum: '',
        locationName: '',
        builtUpArea: '',
        shopArea: '',
        shopRent: '',
        state: '',
        stateName: '',
        startTime: '',
        endTime: '',
        ownerId: '',
        ownerName: '',
        link: '',
        remark: '',
        _currentTab: 'carDetailOwner',
        needBack: false
      }
    }
  },
  created() {
    this.initData()
  },
  methods: {
    initData() {
      this.shopDetailInfo.shopId = this.$route.query.shopId
      this.shopDetailInfo.needBack = this.$route.query.needBack
      if (!this.shopDetailInfo.shopId) {
        return
      }
      const currentTab = this.$route.query.currentTab
      if (currentTab) {
        this.shopDetailInfo._currentTab = currentTab
      }
      this.loadShopInfo()
    },
    loadShopInfo() {
      getShopDetail({
        shopId: this.shopDetailInfo.shopId,
        page: 1,
        row: 1
      }).then(response => {
        const data = response.data && response.data[0] ? response.data[0] : {}
        Object.assign(this.shopDetailInfo, data)
        this.changeTab(this.shopDetailInfo._currentTab)
      }).catch(error => {
        console.error('Failed to load shop info:', error)
      })
    },
    changeTab(tab) {
      this.shopDetailInfo._currentTab = tab
      setTimeout(() => {
        if (this.shopDetailInfo._currentTab === 'carDetailOwner' && this.$refs.carDetailOwner) {
          this.$refs.carDetailOwner.open({ ownerId: this.shopDetailInfo.ownerId })
        } else if (this.shopDetailInfo._currentTab === 'ownerDetailRoom' && this.shopDetailInfo.ownerId && this.$refs.ownerDetailRoom) {
          this.$refs.ownerDetailRoom.open(
            this.shopDetailInfo.ownerId,
            this.shopDetailInfo.ownerName,
            this.shopDetailInfo.link
          )
        } else if (this.shopDetailInfo._currentTab === 'ownerDetailMember' && this.shopDetailInfo.ownerId && this.$refs.ownerDetailMember) {
          this.$refs.ownerDetailMember.open(
            this.shopDetailInfo.ownerId,
            this.shopDetailInfo.ownerName,
            this.shopDetailInfo.link
          )
        } else if (this.shopDetailInfo._currentTab === 'ownerDetailCar' && this.shopDetailInfo.ownerId && this.$refs.ownerDetailCar) {
          this.$refs.ownerDetailCar.open(
            this.shopDetailInfo.ownerId,
            this.shopDetailInfo.ownerName
          )
        } else if (this.shopDetailInfo._currentTab === 'shopDetailShop' && this.shopDetailInfo.ownerId && this.$refs.shopDetailShop) {
          this.$refs.shopDetailShop.open(this.shopDetailInfo.ownerId, this.shopDetailInfo.shopId)
        }
      }, 100)
    }
  }
}
</script>

<style scoped>
.white-bg {
  background-color: #fff;
}

.padding-lg {
  padding: 20px;
}

.padding-top {
  padding-top: 15px;
}

.border-radius {
  border-radius: 4px;
}

.margin {
  margin: 10px;
}

.margin-top {
  margin-top: 15px;
}

.margin-top-sm {
  margin-top: 10px;
}

.text-title {
  font-size: 18px;
  font-weight: bold;
}

.text-left {
  text-align: left;
}
</style>
