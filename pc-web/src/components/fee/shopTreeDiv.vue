<template>
  <div class="shop-tree-div">
    <shop-location-tree ref="shopLocationTree" @select="handleLocationSelect"></shop-location-tree>
    <div class="shop-list-container" v-if="currentLocationId">
      <div class="shop-list-header">
        <span>{{ $t('shopCreateFee.shopList') }}</span>
        <el-button type="text" size="small" @click="loadShops">{{ $t('common.refresh') }}</el-button>
      </div>
      <el-scrollbar class="shop-list-scrollbar">
        <div
          v-for="shop in shopList"
          :key="shop.shopId"
          class="shop-item"
          :class="{ active: currentShopId === shop.shopId }"
          @click="handleShopSelect(shop)"
        >
          <div class="shop-info">
            <span class="shop-name">{{ shop.shopNum }}</span>
            <span class="shop-area" v-if="shop.builtUpArea">{{ shop.builtUpArea }}㎡</span>
          </div>
          <div class="shop-owner" v-if="shop.ownerName">{{ shop.ownerName }}</div>
        </div>
        <div v-if="shopList.length === 0" class="no-data">
          {{ $t('common.noData') }}
        </div>
      </el-scrollbar>
    </div>
    <div class="no-location-tip" v-else>
      {{ $t('shopCreateFee.selectLocationFirst') }}
    </div>
  </div>
</template>

<script>
import ShopLocationTree from '@/components/room/ShopLocationTree'
import { listShops } from '@/api/shop/shopCreateFeeApi'
import { getCommunityId } from '@/api/community/communityApi'

export default {
  name: 'ShopTreeDiv',
  components: {
    ShopLocationTree
  },
  data() {
    return {
      shopList: [],
      currentLocationId: '',
      currentShopId: '',
      communityId: ''
    }
  },
  created() {
    this.communityId = getCommunityId()
  },
  methods: {
    handleLocationSelect(data) {
      this.currentLocationId = data.locationId
      this.currentShopId = ''
      this.loadShops()
    },
    loadShops() {
      if (!this.currentLocationId) {
        this.shopList = []
        return
      }
      const params = {
        page: 1,
        row: 500,
        communityId: this.communityId,
        locationId: this.currentLocationId
      }
      listShops(params).then(res => {
        this.shopList = res.data || []
      }).catch(error => {
        console.error('Failed to load shops:', error)
      })
    },
    handleShopSelect(shop) {
      this.currentShopId = shop.shopId
      this.$emit('selectShop', {
        shopId: shop.shopId,
        shopName: shop.shopNum,
        shopNum: shop.shopNum,
        ownerId: shop.ownerId,
        ownerName: shop.ownerName,
        builtUpArea: shop.builtUpArea,
        locationId: shop.locationId,
        locationName: shop.locationName
      })
    },
    initShopTree() {
      if (this.$refs.shopLocationTree) {
        this.$refs.shopLocationTree.loadData()
      }
    }
  }
}
</script>

<style scoped>
.shop-tree-div {
  height: 100%;
  display: flex;
  flex-direction: column;
}

.shop-list-container {
  flex: 1;
  display: flex;
  flex-direction: column;
  margin-top: 10px;
  border-top: 1px solid #ebeef5;
  padding-top: 10px;
}

.shop-list-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 0 10px 10px;
  font-weight: 500;
}

.shop-list-scrollbar {
  flex: 1;
  overflow-y: auto;
}

.shop-item {
  padding: 10px;
  cursor: pointer;
  border-bottom: 1px solid #f5f7fa;
  transition: background-color 0.2s;
}

.shop-item:hover {
  background-color: #f5f7fa;
}

.shop-item.active {
  background-color: #ecf5ff;
  border-left: 3px solid #409eff;
}

.shop-info {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.shop-name {
  font-weight: 500;
  color: #303133;
}

.shop-area {
  font-size: 12px;
  color: #909399;
}

.shop-owner {
  margin-top: 4px;
  font-size: 12px;
  color: #606266;
}

.no-data {
  padding: 20px;
  text-align: center;
  color: #909399;
}

.no-location-tip {
  padding: 20px;
  text-align: center;
  color: #909399;
}
</style>
