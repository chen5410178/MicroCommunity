<template>
  <div class="shop-tree-container">
    <el-tree
      ref="tree"
      :data="treeData"
      node-key="id"
      :props="defaultProps"
      :highlight-current="true"
      :expand-on-click-node="false"
      @node-click="handleNodeClick"
    >
      <span slot-scope="{ node, data }" class="custom-tree-node">
        <span>
          <i :class="data.icon" style="margin-right: 5px"></i>
          {{ node.label }}
        </span>
      </span>
    </el-tree>
  </div>
</template>

<script>
import { listShopLocations } from '@/api/room/shopLocationApi'
import { listShops } from '@/api/shop/shopCreateFeeApi'
import { getCommunityId } from '@/api/community/communityApi'

export default {
  name: 'LocationShopTreeDiv',
  data() {
    return {
      treeData: [],
      defaultProps: {
        children: 'children',
        label: 'text'
      },
      communityId: '',
      lastSelected: {},
      isInitialized: false
    }
  },
  created() {
    this.communityId = getCommunityId()
    this.loadTreeData()
  },
  methods: {
    async loadTreeData() {
      try {
        const res = await listShopLocations({
          communityId: this.communityId
        })
        this.buildTreeData(res.data || [])
      } catch (error) {
        console.error('Failed to load tree data:', error)
        this.$message.error(this.$t('shopCreateFee.loadLocationError'))
      }
    },
    buildTreeData(locations) {
      const processLocation = (location) => ({
        id: `loc_${location.locationId}`,
        locationId: location.locationId,
        locationName: location.locationName,
        icon: '/img/location.png',
        text: location.locationName,
        isLocation: true,
        children: (location.children && location.children.length > 0)
          ? location.children.map(child => processLocation(child))
          : []
      })

      this.treeData = locations.map(location => processLocation(location))
      this.$nextTick(() => {
        this.expandAndSelectFirstShop()
      })
    },
    async expandAndSelectFirstShop() {
      if (!this.treeData.length) return

      const firstLocation = this.treeData[0]
      const treeRef = this.$refs.tree
      if (treeRef && treeRef.store) {
        const locationNode = treeRef.store.nodesMap[firstLocation.id]
        if (locationNode) {
          locationNode.expanded = true
        }
      }
      await this.loadShops(firstLocation, { data: firstLocation })
      this.isInitialized = true
    },
    async handleNodeClick(data, node) {
      if (data.isLocation) {
        if (!node.expanded && (!data.children || data.children.length === 0)) {
          await this.loadShops(data, node)
          node.expanded = true
        } else if (node.expanded) {
          node.expanded = false
        }
      } else if (data.isShop) {
        localStorage.setItem('lastSelectedShop', JSON.stringify({
          shopId: data.shopId,
          shopName: data.shopName,
          locationId: data.locationId
        }))
        this.lastSelected = {
          shopId: data.shopId,
          shopName: data.shopName,
          locationId: data.locationId
        }
        this.$emit('selectShop', {
          shopId: data.shopId,
          shopName: data.shopName,
          shopNum: data.shopNum,
          ownerId: data.ownerId,
          ownerName: data.ownerName,
          builtUpArea: data.builtUpArea,
          locationId: data.locationId,
          locationName: data.locationName
        })
      }
    },
    async loadShops(locationData, node) {
      try {
        const res = await listShops({
          locationId: locationData.locationId,
          communityId: this.communityId,
          page: 1,
          row: 1000
        })
        let shops = res.data || []
        shops = shops.filter(shop => shop.state !== '2008')
        const shopNodes = shops.map(shop => ({
          id: `shop_${shop.shopId}`,
          shopId: shop.shopId,
          shopName: shop.shopNum,
          shopNum: shop.shopNum,
          ownerId: shop.ownerId || '',
          ownerName: shop.ownerName || '',
          builtUpArea: shop.builtUpArea || '',
          locationId: shop.locationId,
          locationName: shop.locationName,
          icon: '/img/shop.png',
          text: shop.ownerName ? `${shop.shopNum}(${shop.ownerName})` : shop.shopNum,
          isShop: true
        }))
        this.$set(node.data, 'children', shopNodes)

        if (!this.isInitialized && shopNodes.length > 0) {
          this.$emit('selectShop', {
            shopId: shopNodes[0].shopId,
            shopName: shopNodes[0].shopName,
            shopNum: shopNodes[0].shopNum,
            ownerId: shopNodes[0].ownerId,
            ownerName: shopNodes[0].ownerName,
            builtUpArea: shopNodes[0].builtUpArea,
            locationId: shopNodes[0].locationId,
            locationName: shopNodes[0].locationName
          })
        }
      } catch (error) {
        console.error('Failed to load shops:', error)
        this.$message.error(this.$t('shopTree.loadShopError'))
      }
    }
  }
}
</script>

<style lang="scss" scoped>
.shop-tree-container {
  height: 100%;
  overflow-y: auto;
  padding: 10px;

  .custom-tree-node {
    flex: 1;
    display: flex;
    align-items: center;
    font-size: 14px;
    padding: 5px 0;
  }
}
</style>