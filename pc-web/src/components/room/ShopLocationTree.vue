<template>
  <div class="shop-location-tree">
    <div v-if="treeData.length > 0" class="tree-all" :class="{ active: !currentSelectedId }" @click="handleAllClick">
      <span>{{ $t('shops.floorUnitTree.all') }}</span>
    </div>
    <el-tree
      v-if="treeData.length > 0"
      ref="tree"
      :data="treeData"
      node-key="locationId"
      :props="defaultProps"
      :highlight-current="true"
      :expand-on-click-node="false"
      @node-click="handleNodeClick"
    >
      <span slot-scope="{ node }" class="custom-tree-node">
        <span>{{ node.label }}</span>
      </span>
    </el-tree>
    <div v-else-if="!loading" class="no-data">
      {{ $t('shops.floorUnitTree.noData') }}
    </div>
  </div>
</template>

<script>
import { listShopLocations } from '@/api/room/shopLocationApi'
import {getCommunityId} from '@/api/community/communityApi'

export default {
  name: 'ShopLocationTree',
  data() {
    return {
      loading: false,
      treeData: [],
      currentSelectedId: '',
      defaultProps: {
        children: 'children',
        label: 'locationName'
      }
    }
  },
  mounted() {
    this.loadTreeData()
  },
  methods: {
    loadData() {
      this.loadTreeData()
    },
    getLocations() {
      return Promise.resolve(this.treeData)
    },
    getFullPath(locationId) {
      var fullPath = ''
      var node = this.findNode(this.treeData, locationId)
      if (node) {
        fullPath = node.locationName || ''
      }
      return fullPath
    },
    findNode(nodes, locationId) {
      for (var i = 0; i < nodes.length; i++) {
        if (nodes[i].locationId === locationId) {
          return nodes[i]
        }
        if (nodes[i].children && nodes[i].children.length > 0) {
          var found = this.findNode(nodes[i].children, locationId)
          if (found) {
            return found
          }
        }
      }
      return null
    },
    async loadTreeData() {
      this.loading = true
      try {
        const communityId = getCommunityId()
        const res = await listShopLocations({ page: 1, row: 1000, communityId: communityId })
        this.treeData =  res.data 
      } catch (error) {
        console.error(error)
      } finally {
        this.loading = false
      }
    },
    handleNodeClick(data) {
      this.currentSelectedId = data.locationId
      this.$emit('select', data)
    },
    handleAllClick() {
      this.currentSelectedId = ''
      this.$emit('select', { locationId: '', locationName: '', parentId: '-1' })
    },
    open() {
      this.loadTreeData()
    }
  }
}
</script>

<style scoped>
.shop-location-tree {
  min-height: 200px;
}
.tree-all {
  padding: 8px 10px;
  cursor: pointer;
  color: #606266;
  font-size: 14px;
  display: flex;
  align-items: center;
}
.tree-all:hover {
  background-color: #f5f7fa;
}
.tree-all.active {
  color: #409eff;
  background-color: #ecf5ff;
}
.custom-tree-node {
  display: flex;
  align-items: center;
  justify-content: space-between;
  font-size: 14px;
  padding-right: 8px;
}
</style>
