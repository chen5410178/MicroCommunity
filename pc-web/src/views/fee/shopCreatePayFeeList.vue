<template>
  <div class="shop-create-pay-fee-container">
    <el-card class="box-card">
      <div slot="header" class="flex justify-between">
        <span>{{ $t('shopCreatePayFee.title') }}</span>
      </div>

      <el-row :gutter="20">
        <el-col :span="24">
          <el-form ref="form" :model="shopCreatePayFeeInfo" label-width="120px">
            <el-row :gutter="20">
              <el-col :span="12">
                <el-form-item :label="$t('shopCreatePayFee.feeType')">
                  <el-select v-model="shopCreatePayFeeInfo.feeTypeCd" @change="changeFeeTypeCd" style="width:100%"
                    :placeholder="$t('shopCreatePayFee.selectFeeType')">
                    <template v-for="(item, index) in shopCreatePayFeeInfo.feeTypeCds">
                      <el-option :key="index" :label="item.name" :value="item.statusCd"
                        v-if="item.statusCd != '888800010017'">
                      </el-option>
                    </template>
                  </el-select>
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item :label="$t('shopCreatePayFee.feeItem')">
                  <el-select v-model="shopCreatePayFeeInfo.configId" @change="changeConfigId"
                    style="width:100%" :placeholder="$t('shopCreatePayFee.selectFeeItem')">
                    <template v-for="(item, index) in shopCreatePayFeeInfo.feeConfigs">
                      <el-option :key="index" :label="item.feeName" :value="item.configId">
                      </el-option>
                    </template>
                  </el-select>
                </el-form-item>
              </el-col>
            </el-row>

            <el-row :gutter="20">
              <el-col :span="12">
                <el-form-item :label="$t('shopCreatePayFee.startTime')">
                  <el-date-picker v-model="shopCreatePayFeeInfo.startTime" type="date" style="width:100%" value-format="yyyy-MM-dd"
                    :placeholder="$t('shopCreatePayFee.selectStartTime')">
                  </el-date-picker>
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item :label="$t('shopCreatePayFee.endTime')">
                  <el-date-picker v-model="shopCreatePayFeeInfo.endTime" type="date" style="width:100%" value-format="yyyy-MM-dd"
                    :placeholder="$t('shopCreatePayFee.selectEndTime')">
                  </el-date-picker>
                </el-form-item>
              </el-col>
            </el-row>

            <el-row :gutter="20" v-if="shopCreatePayFeeInfo.computingFormula == '4004'">
              <el-col :span="12">
                <el-form-item :label="$t('shopCreatePayFee.amount')">
                  <el-input v-model="shopCreatePayFeeInfo.amount" :placeholder="$t('shopCreatePayFee.inputAmount')">
                  </el-input>
                </el-form-item>
              </el-col>
            </el-row>

            <el-row :gutter="20" v-if="shopCreatePayFeeInfo.computingFormula == '1102'">
              <el-col :span="12">
                <el-form-item :label="$t('shopCreatePayFee.rateCycle')">
                  <el-input v-model="shopCreatePayFeeInfo.rateCycle"
                    :placeholder="$t('shopCreatePayFee.inputRateCycle')">
                  </el-input>
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item :label="$t('shopCreatePayFee.rate')">
                  <el-input v-model="shopCreatePayFeeInfo.rate" :placeholder="$t('shopCreatePayFee.inputRate')">
                  </el-input>
                </el-form-item>
              </el-col>
            </el-row>

            <el-row :gutter="20" v-show="shopCreatePayFeeInfo.computingFormula == '1102'">
              <el-col :span="12">
                <el-form-item :label="$t('shopCreatePayFee.rateStartTime')">
                  <el-date-picker v-model="shopCreatePayFeeInfo.rateStartTime" type="date" style="width:100%"
                    :placeholder="$t('shopCreatePayFee.selectRateStartTime')">
                  </el-date-picker>
                </el-form-item>
              </el-col>
            </el-row>

            <el-row :gutter="20">
              <el-col :span="12">
                <el-form-item :label="$t('shopCreatePayFee.location')">
                  <el-select v-model="shopCreatePayFeeInfo.locationId" @change="loadLocationShops" style="width:100%"
                    :placeholder="$t('shopCreatePayFee.selectLocation')">
                    <el-option v-for="(item, index) in shopCreatePayFeeInfo.locations" :key="index" :label="item.locationName"
                      :value="item.locationId">
                    </el-option>
                  </el-select>
                </el-form-item>
              </el-col>
            </el-row>

            <el-row :gutter="20">
              <el-col :span="24" class="text-left">
                <el-form-item :label="$t('shopCreatePayFee.shop')">
                  <div v-show="shopCreatePayFeeInfo.shops && shopCreatePayFeeInfo.shops.length > 0">
                    <el-checkbox v-model="checkAllShops" @change="checkAll">{{ $t('shopCreatePayFee.selectAll')
                    }}</el-checkbox>
                    <br />
                    <el-checkbox-group v-model="shopCreatePayFeeInfo.shopIds">
                      <el-checkbox v-for="(item, index) in shopCreatePayFeeInfo.shops" :key="index"
                        :label="item.shopId">
                        {{ item.shopNum }} ({{ item.stateName || '-' }})
                      </el-checkbox>
                    </el-checkbox-group>
                  </div>
                  <div v-show="!shopCreatePayFeeInfo.shops || shopCreatePayFeeInfo.shops.length < 1">
                    <span>{{ $t('shopCreatePayFee.noShop') }}</span>
                  </div>
                </el-form-item>
              </el-col>
            </el-row>

            <el-row :gutter="20">
              <el-col :span="24" class="text-right">
                <el-button type="warning" @click="goBack">{{ $t('common.back') }}</el-button>
                <el-button type="primary" @click="saveShopCreatePayFee">{{ $t('common.submit') }}</el-button>
              </el-col>
            </el-row>
          </el-form>
        </el-col>
      </el-row>
    </el-card>
  </div>
</template>

<script>
import { getCommunityId, getDict } from '@/api/community/communityApi'
import { listShops } from '@/api/shop/shopCreateFeeApi'
import { listFeeConfigs } from '@/api/fee/simplifyRoomFeeApi'
import { listShopLocations } from '@/api/room/shopLocationApi'
import request from '@/utils/request'

export default {
  name: 'ShopCreatePayFeeList',
  data() {
    return {
      shopCreatePayFeeInfo: {
        feeTypeCds: [],
        feeConfigs: [],
        communityId: '',
        feeTypeCd: '',
        configId: '',
        shopIds: [],
        shops: [],
        startTime: '',
        endTime: '2050-01-01',
        feeFlag: '',
        computingFormula: '',
        amount: '',
        rateCycle: '',
        rate: '',
        rateStartTime: '',
        locations: [],
        locationId: ''
      },
      checkAllShops: false
    }
  },
  created() {
    this.initData()
  },
  methods: {
    async initData() {
      this.shopCreatePayFeeInfo.communityId = getCommunityId()
      await this.loadFeeTypeCds()
      await this.loadLocations()
    },
    async loadFeeTypeCds() {
      try {
        const data = await getDict('pay_fee_config', 'fee_type_cd')
        this.shopCreatePayFeeInfo.feeTypeCds = data.filter(item =>
          item.statusCd !== "888800010015" && item.statusCd !== "888800010016"
        )
      } catch (error) {
        console.error('Failed to load fee types:', error)
      }
    },
    async loadLocations() {
      try {
        const res = await listShopLocations({ page: 1, row: 100, communityId: this.shopCreatePayFeeInfo.communityId })
        this.shopCreatePayFeeInfo.locations = res.data || []
      } catch (error) {
        console.error('Failed to load locations:', error)
      }
    },
    async changeFeeTypeCd(feeTypeCd) {
      if (feeTypeCd === '888800010006' || feeTypeCd === '888800010014') {
        this.shopCreatePayFeeInfo.startTime = this.formatDate(new Date())
        this.shopCreatePayFeeInfo.endTime = this.addMonthDate(new Date(), 1)
      }
      this.shopCreatePayFeeInfo.configId = ''

      try {
        const params = {
          page: 1,
          row: 500,
          communityId: this.shopCreatePayFeeInfo.communityId,
          feeTypeCd: feeTypeCd,
          isDefault: 'F',
          valid: '1'
        }
        const res = await listFeeConfigs(params)
        this.shopCreatePayFeeInfo.feeConfigs = res.feeConfigs || []
      } catch (error) {
        console.error('Failed to load fee configs:', error)
      }
    },
    changeConfigId(configId) {
      const config = this.shopCreatePayFeeInfo.feeConfigs.find(item => item.configId === configId)
      if (config) {
        this.shopCreatePayFeeInfo.feeFlag = config.feeFlag
        this.shopCreatePayFeeInfo.computingFormula = config.computingFormula
      }
    },
    async loadLocationShops() {
      this.shopCreatePayFeeInfo.shops = []
      this.shopCreatePayFeeInfo.shopIds = []

      if (!this.shopCreatePayFeeInfo.locationId) return

      try {
        const params = {
          page: 1,
          row: 500,
          communityId: this.shopCreatePayFeeInfo.communityId,
          locationId: this.shopCreatePayFeeInfo.locationId
        }
        const res = await listShops(params)
        this.shopCreatePayFeeInfo.shops = res.data || []
        if (this.shopCreatePayFeeInfo.shops.length > 0) {
          this.shopCreatePayFeeInfo.shopIds = this.shopCreatePayFeeInfo.shops.map(shop => shop.shopId)
        }
      } catch (error) {
        console.error('Failed to load shops:', error)
      }
    },
    checkAll(checked) {
      if (checked) {
        this.shopCreatePayFeeInfo.shopIds = this.shopCreatePayFeeInfo.shops.map(shop => shop.shopId)
      } else {
        this.shopCreatePayFeeInfo.shopIds = []
      }
    },
    async saveShopCreatePayFee() {
      if (!this.validate()) {
        this.$message.error(this.$t('shopCreatePayFee.validateError'))
        return
      }

      if (!this.shopCreatePayFeeInfo.shopIds || this.shopCreatePayFeeInfo.shopIds.length < 1) {
        this.$message.error(this.$t('shopCreatePayFee.noShopSelected'))
        return
      }

      try {
        const dataList = this.shopCreatePayFeeInfo.shopIds.map(shopId => {
          const shop = this.shopCreatePayFeeInfo.shops.find(s => s.shopId === shopId)
          return {
            communityId: this.shopCreatePayFeeInfo.communityId,
            payerObjId: shopId,
            payerObjType: '4444',
            payerObjName: shop ? shop.shopNum : '',
            configId: this.shopCreatePayFeeInfo.configId,
            feeTypeCd: this.shopCreatePayFeeInfo.feeTypeCd,
            startTime: this.shopCreatePayFeeInfo.startTime,
            endTime: this.shopCreatePayFeeInfo.endTime,
            computingFormula: this.shopCreatePayFeeInfo.computingFormula,
            amount: this.shopCreatePayFeeInfo.amount,
            rateCycle: this.shopCreatePayFeeInfo.rateCycle,
            rate: this.shopCreatePayFeeInfo.rate,
            rateStartTime: this.shopCreatePayFeeInfo.rateStartTime
          }
        })

        await request({
          url: '/fee.batchCreateFee',
          method: 'post',
          data: dataList
        })

        this.$message.success(this.$t('common.operationSuccess'))
        this.goBack()
      } catch (error) {
        this.$message.error(error.message || this.$t('common.saveFailed'))
      }
    },
    validate() {
      if (!this.shopCreatePayFeeInfo.configId) {
        return false
      }
      return true
    },
    goBack() {
      this.$router.go(-1)
    },
    addMonthDate(date, months) {
      const newDate = new Date(date)
      newDate.setMonth(newDate.getMonth() + months)
      return this.formatDate(newDate)
    },
    formatDate(date) {
      const year = date.getFullYear()
      const month = String(date.getMonth() + 1).padStart(2, '0')
      const day = String(date.getDate()).padStart(2, '0')
      return `${year}-${month}-${day}`
    }
  }
}
</script>

<style lang="scss" scoped>
.shop-create-pay-fee-container {
  padding: 20px;

  .box-card {
    margin-bottom: 20px;
  }

  .text-right {
    text-align: right;
  }

  .el-checkbox-group {
    margin-top: 10px;

    .el-checkbox {
      margin-right: 15px;
      margin-bottom: 10px;
    }
  }
}
</style>
