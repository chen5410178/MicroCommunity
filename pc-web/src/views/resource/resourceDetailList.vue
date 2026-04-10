<template>
  <div class="resource-detail-container">
    <el-card class="box-card">
      <div slot="header" class="flex justify-between">
        <span>{{ $t('resourceDetail.itemInfo') }}</span>
      </div>
      <div class="white-bg border-radius-top">
        <div class="margin-top text-left">
          <el-row>
            <el-col :span="24">
              <el-row>
                <el-col :span="6">
                  <div class="form-group">
                    <label class="col-form-label">
                      {{ $t('resourceDetail.itemNameWithCode') }}
                    </label>
                    <label class="">{{ resourceDetailInfo.resName }}({{ resourceDetailInfo.resCode }})</label>
                  </div>
                </el-col>
                <el-col :span="6">
                  <div class="form-group">
                    <label class="col-form-label">
                      {{ $t('resourceDetail.warehouseName') }}
                    </label>
                    <label class="">{{ resourceDetailInfo.shName }}</label>
                  </div>
                </el-col>
                <el-col :span="6">
                  <div class="form-group">
                    <label class="col-form-label">
                      {{ $t('resourceDetail.itemType') }}
                    </label>
                    <label class="">{{ resourceDetailInfo.parentRstName }}
                      {{ resourceDetailInfo.rstName ? ' > ' + resourceDetailInfo.rstName : '' }}</label>
                  </div>
                </el-col>
                <el-col :span="6">
                  <div class="form-group">
                    <label class="col-form-label">
                      {{ $t('resourceDetail.itemSpec') }}
                    </label>
                    <label class="">
                      {{ resourceDetailInfo.rssName ? resourceDetailInfo.rssName : '-' }}
                    </label>
                  </div>
                </el-col>
              </el-row>

              <el-row>
                <el-col :span="6">
                  <div class="form-group">
                    <label class="col-form-label">
                      {{ $t('resourceDetail.fixedItem') }}
                    </label>
                    <label class="">{{ resourceDetailInfo.isFixedName }}</label>
                  </div>
                </el-col>
                <el-col :span="6">
                  <div class="form-group">
                    <label class="col-form-label">
                      {{ $t('resourceDetail.referencePrice') }}
                    </label>
                    <label class="">{{ '￥' + resourceDetailInfo.price }}</label>
                  </div>
                </el-col>
                <el-col :span="6">
                  <div class="form-group">
                    <label class="col-form-label">
                      {{ $t('resourceDetail.chargeStandard') }}
                    </label>
                    <label class="">{{ resourceDetailInfo.outHighPrice ==
                      resourceDetailInfo.outLowPrice ? '￥' +
                    resourceDetailInfo.outLowPrice : '￥' + resourceDetailInfo.outLowPrice + '-￥' +
                    resourceDetailInfo.outHighPrice }}</label>
                  </div>
                </el-col>
                <el-col :span="6">
                  <div class="form-group">
                    <label class="col-form-label">
                      {{ $t('resourceDetail.itemStock') }}
                    </label>
                    <label class="">{{ resourceDetailInfo.stock }}{{ resourceDetailInfo.unitCodeName }}</label>
                  </div>
                </el-col>
              </el-row>

              <el-row>
                <el-col :span="6">
                  <div class="form-group">
                    <label class="col-form-label">
                      {{ $t('resourceDetail.minMeasurement') }}
                    </label>
                    <label
                      class="">1{{ resourceDetailInfo.unitCodeName }}={{ resourceDetailInfo.miniUnitStock }}{{ resourceDetailInfo.miniUnitCodeName }}</label>
                  </div>
                </el-col>
                <el-col :span="6">
                  <div class="form-group">
                    <label class="col-form-label">
                      {{ $t('resourceDetail.minMeasurementTotal') }}
                    </label>
                    <label class="">{{ resourceDetailInfo.miniStock }}{{ resourceDetailInfo.miniUnitCodeName }}</label>
                  </div>
                </el-col>
                <el-col :span="6">
                  <div class="form-group">
                    <label class="col-form-label">
                      {{ $t('resourceDetail.itemAvgPrice') }}
                    </label>
                    <label class="">{{ resourceDetailInfo.averagePrice ? '￥' +
                      resourceDetailInfo.averagePrice : '￥0' }}</label>
                  </div>
                </el-col>
              </el-row>
            </el-col>
          </el-row>
        </div>

        <divider />

        <div class="margin-top-sm">
          <el-tabs v-model="resourceDetailInfo._currentTab" @tab-click="changeTab(resourceDetailInfo._currentTab)">
            <el-tab-pane :label="$t('resourceDetail.purchaseRecord')" name="resourceDetailPurchase">
              <resource-detail-purchase ref="resourceDetailPurchase"
                v-if="resourceDetailInfo._currentTab === 'resourceDetailPurchase'" />
            </el-tab-pane>
            <el-tab-pane :label="$t('resourceDetail.useRecordText')" name="resourceDetailItemOut">
              <resource-detail-item-out ref="resourceDetailItemOut"
                v-if="resourceDetailInfo._currentTab === 'resourceDetailItemOut'" />
            </el-tab-pane>
            <el-tab-pane :label="$t('resourceDetail.inventoryRecord')" name="resourceDetailInventory">
              <resource-detail-inventory ref="resourceDetailInventory"
                v-if="resourceDetailInfo._currentTab === 'resourceDetailInventory'" />
            </el-tab-pane>
            <el-tab-pane :label="$t('resourceDetail.allocationRecord')" name="resourceDetailAllocation">
              <resource-detail-allocation ref="resourceDetailAllocation"
                v-if="resourceDetailInfo._currentTab === 'resourceDetailAllocation'" />
            </el-tab-pane>
            <el-tab-pane :label="$t('resourceDetail.transferRecord')" name="resourceDetailAllocationUser">
              <resource-detail-allocation-user ref="resourceDetailAllocationUser"
                v-if="resourceDetailInfo._currentTab === 'resourceDetailAllocationUser'" />
            </el-tab-pane>
            <el-tab-pane :label="$t('resourceDetail.usageRecord')" name="resourceDetailUseRecord">
              <resource-detail-use-record ref="resourceDetailUseRecord"
                v-if="resourceDetailInfo._currentTab === 'resourceDetailUseRecord'" />
            </el-tab-pane>
          </el-tabs>
        </div>
      </div>
    </el-card>
  </div>
</template>

<script>
import { getCommunityId } from '@/api/community/communityApi'
import ResourceDetailPurchase from '@/components/resource/resourceDetailPurchase'
import ResourceDetailItemOut from '@/components/resource/resourceDetailItemOut'
import ResourceDetailInventory from '@/components/resource/resourceDetailInventory'
import ResourceDetailAllocation from '@/components/resource/resourceDetailAllocation'
import ResourceDetailAllocationUser from '@/components/resource/resourceDetailAllocationUser'
import ResourceDetailUseRecord from '@/components/resource/resourceDetailUseRecord'
import { getResourceStoreList } from '@/api/resource/resourceDetailApi'
import divider from '@/components/system/divider'

export default {
  name: 'ResourceDetailList',
  components: {
    ResourceDetailPurchase,
    ResourceDetailItemOut,
    ResourceDetailInventory,
    ResourceDetailAllocation,
    ResourceDetailAllocationUser,
    ResourceDetailUseRecord,
    divider
  },
  data() {
    return {
      resourceDetailInfo: {
        resId: '',
        shName: '',
        parentRstName: '',
        rstName: '',
        resName: '',
        resCode: '',
        rssName: '',
        isFixedName: '',
        price: '',
        outHighPrice: '',
        outLowPrice: '',
        stock: '',
        unitCodeName: '',
        miniUnitStock: '',
        miniUnitCodeName: '',
        miniStock: '',
        averagePrice: '',
        _currentTab: 'resourceDetailPurchase',
        needBack: false
      },
      communityId: ''
    }
  },
  created() {
    this.communityId = getCommunityId()
    this.resourceDetailInfo.resId = this.$route.query.resId
    if (!this.resourceDetailInfo.resId) {
      return
    }
    this.loadResourceDetailInfo()
  },
  methods: {
    async loadResourceDetailInfo() {
      try {
        const params = {
          page: 1,
          row: 1,
          resId: this.resourceDetailInfo.resId,
          communityId: this.communityId
        }

        const { resourceStores } = await getResourceStoreList(params)
        if (resourceStores && resourceStores.length > 0) {
          Object.assign(this.resourceDetailInfo, resourceStores[0])
          this.changeTab(this.resourceDetailInfo._currentTab)
        }
      } catch (error) {
        console.error('Failed to load resource detail:', error)
      }
    },
    changeTab(tabName) {
      this.resourceDetailInfo._currentTab = tabName
      setTimeout(() => {
        const tabRef = this.$refs[`${tabName}`]
        if (tabRef && tabRef.loadData) {
          tabRef.loadData({ resId: this.resourceDetailInfo.resId })
        }
      }, 500)
    }
  }
}
</script>

<style lang="scss" scoped>
.resource-detail-container {
  padding: 20px;

  .box-card {
    margin-bottom: 20px;
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
    font-size: 16px;
    font-weight: bold;
  }

  .vc-line-primary {
    height: 1px;
    background-color: #ebeef5;
  }

  .form-group {
    margin-bottom: 15px;

    .col-form-label {
      margin-bottom: 5px;
    }
  }

  .flex {
    display: flex;
  }

  .justify-between {
    justify-content: space-between;
  }
}
</style>