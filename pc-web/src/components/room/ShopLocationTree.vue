<template>
  <div class="shop-location-tree">
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
      {{ $t('room.floorUnitTree.noData') }}
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
      this.$emit('select', data)
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
.custom-tree-node {
  display: flex;
  align-items: center;
  justify-content: space-between;
  font-size: 14px;
  padding-right: 8px;
}
</style>
