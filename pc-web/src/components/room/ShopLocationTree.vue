<template>
  <div class="shop-location-tree">
    <el-tree
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
  </div>
</template>

<script>
import { listShopLocations } from '@/api/room/shopLocationApi'
import {getCommunityId} from '@/api/community/communityApi'

export default {
  name: 'ShopLocationTree',
  data() {
    return {
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
      try {
        const res = await listShopLocations({ page: 1, row: 1000, communityId: getCommunityId() })
        if (res.code === 0) {
          this.treeData = res.data || []
        }
      } catch (error) {
        console.error(error)
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
