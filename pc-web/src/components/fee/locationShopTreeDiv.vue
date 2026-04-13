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
          this.$emit('selectShop', {
          shopId:  this.lastSelected.shopId,
          shopName:  this.lastSelected.shopName,
          shopNum:  this.lastSelected.shopNum,
          ownerId:  this.lastSelected.ownerId,
          ownerName:  this.lastSelected.ownerName,
          builtUpArea:  this.lastSelected.builtUpArea,
          locationId:  this.lastSelected.locationId,
          locationName:  this.lastSelected.locationName
        })
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
        if (this.treeData.length === 0) {
          this.$emit('treeEmpty')
        }
        this.expandAndSelectFirstShop()
      })
    },

    async expandAndSelectFirstShop() {
      if (!this.treeData.length) return

      const path = this.findDeepestLeafPath(this.treeData)
      if (!path || path.length === 0) return

      const lastNode = path[path.length - 1]
   
      console.log('Expanding path to first shop:', lastNode)

      await this.loadShops(lastNode, { data: lastNode })
      this.$nextTick(() => {
        this.selectFirstShop()
      })
    },
    findDeepestLeafPath(nodes, currentPath) {
      currentPath = currentPath || []
      for (var i = 0; i < nodes.length; i++) {
        var node = nodes[i]
        var path = currentPath.concat([node])
        if (!node.children || node.children.length === 0) {
          return path
        }
        var childPath = this.findDeepestLeafPath(node.children, path)
        if (childPath && childPath.length > 0) {
          return childPath
        }
      }
      return []
    },

    async handleNodeClick(data, node) {
      if (data.isLocation) {
            await this.loadShops(data, node)
      } else if (data.isShop) {
        let selectedData = {
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