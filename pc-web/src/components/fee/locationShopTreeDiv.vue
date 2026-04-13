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
          {{ data.locationName }}
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
        label: 'locationName'
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
      let lastSelected = localStorage.getItem('lastSelectedShop')
      if (lastSelected) {
        try {
          this.lastSelected = JSON.parse(lastSelected)
        } catch (error) {
          console.error('解析lastSelectedShop失败:', error)
          localStorage.removeItem('lastSelectedShop')
        }
      }

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
        isLocation: true,
        children: (location.children && location.children.length > 0)
          ? location.children.map(child => processLocation(child))
          : []
      })

      this.treeData = locations.map(location => processLocation(location))
      this.$nextTick(() => {
        if (this.lastSelected && this.lastSelected.locationId && this.lastSelected.shopId) {
          this.expandAndSelectLastShop()
        } else {
          this.expandAndSelectFirstShop()
        }
      })
    },
    async expandAndSelectLastShop() {
      if (!this.lastSelected || !this.lastSelected.locationId || !this.lastSelected.shopId) {
        this.expandAndSelectFirstShop()
        return
      }

      const treeRef = this.$refs.tree
      if (!treeRef || !treeRef.store) {
        setTimeout(() => this.expandAndSelectLastShop(), 100)
        return
      }

      try {
        const locationNode = this.findLocationNode(this.treeData, this.lastSelected.locationId)
        if (!locationNode) {
          console.warn('未找到对应的位置:', this.lastSelected.locationId)
          this.expandAndSelectFirstShop()
          return
        }

        var locationTreeNode = treeRef.store.nodesMap[locationNode.id]
        if (locationTreeNode) {
          locationTreeNode.expanded = true
        }

        await this.loadShops(locationNode, { data: locationNode })

        this.$nextTick(() => {
          if (this.lastSelected.shopId) {
            this.selectSpecificShop(this.lastSelected.shopId)
          }
        })

        this.isInitialized = true
      } catch (error) {
        console.error('展开上次选择的商铺失败:', error)
        this.expandAndSelectFirstShop()
      }
    },
    findLocationNode(nodes, locationId) {
      for (var i = 0; i < nodes.length; i++) {
        if (nodes[i].locationId === locationId) {
          return nodes[i]
        }
        if (nodes[i].children && nodes[i].children.length > 0) {
          var found = this.findLocationNode(nodes[i].children, locationId)
          if (found) return found
        }
      }
      return null
    },
    selectSpecificShop(shopId) {
      const treeRef = this.$refs.tree
      if (!treeRef) return

      const shopNodeId = `shop_${shopId}`
      const shopTreeNode = treeRef.store.nodesMap[shopNodeId]

      if (shopTreeNode) {
        treeRef.setCurrentKey(shopNodeId)
        this.$emit('selectShop', {
          shopId: shopId,
          shopName: this.lastSelected.shopName || '',
          shopNum: this.lastSelected.shopNum || '',
          ownerId: this.lastSelected.ownerId || '',
          ownerName: this.lastSelected.ownerName || '',
          builtUpArea: this.lastSelected.builtUpArea || '',
          locationId: this.lastSelected.locationId || '',
          locationName: this.lastSelected.locationName || ''
        })
      } else {
        setTimeout(() => this.selectSpecificShop(shopId), 200)
      }
    },
    async expandAndSelectFirstShop() {
      if (!this.treeData.length) return

      const firstLocation = this.treeData[0]
      const treeRef = this.$refs.tree
      if (!treeRef || !treeRef.store) return

      var firstNode = treeRef.store.nodesMap[firstLocation.id]
      if (firstNode) {
        firstNode.expanded = true
      }

      await this.loadShops(firstLocation, { data: firstLocation })
      this.isInitialized = true
    },
    async handleNodeClick(data, node) {
      if (data.isLocation) {
        if (!node.expanded) {
          if (!data.children || data.children.length === 0) {
            await this.loadShops(data, node)
          }
          node.expanded = true
        } else {
          node.expanded = false
        }
      } else if (data.isShop) {
        var selectedData = {
          shopId: data.shopId,
          shopName: data.shopName,
          shopNum: data.shopNum,
          ownerId: data.ownerId,
          ownerName: data.ownerName,
          builtUpArea: data.builtUpArea,
          locationId: data.locationId,
          locationName: data.locationName
        }
        localStorage.setItem('lastSelectedShop', JSON.stringify(selectedData))
        this.lastSelected = selectedData
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
          locationName: shop.ownerName ? shop.shopNum + '(' + shop.ownerName + ')' : shop.shopNum,
          icon: '/img/shop.png',
          isShop: true
        }))
        this.$set(node.data, 'children', shopNodes)
        this.$nextTick(() => {
          var treeRef = this.$refs.tree
          if (treeRef) {
            treeRef.updateKeyChildren(locationData.id, shopNodes)
          }
        })
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