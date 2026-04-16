<template>
  <el-card class="tree-card">
    <el-tree
      ref="parkingTree"
      :data="treeData"
      node-key="id"
      :props="defaultProps"
      :highlight-current="true"
      :expand-on-click-node="false"
      @node-click="handleNodeClick"
    />
  </el-card>
</template>

<script>
import { queryCommunityParkingTree } from '@/api/car/adminCarApi'

export default {
  name: 'CommunityParkingTree',
  data() {
    return {
      treeData: [],
      defaultProps: {
        children: 'children',
        label: 'text'
      }
    }
  },
  methods: {
    async initTree() {
      try {
        const { data } = await queryCommunityParkingTree({ hc: 1.8 })
        this.treeData = data
      
      } catch (error) {
        this.$message.error(this.$t('adminCar.tree.fetchError'))
      }
    },
    handleNodeClick(data, node) {
      console.log(node)
      if (data.id.startsWith('c_')) {
        this.$emit('selectCommunity', {
          communityName: data.communityName,
          communityId: data.communityId
        })
      } else if (data.id.startsWith('p_')) {
        this.$emit('selectParkingArea', {
          paNum: data.num,
          paId: data.paId
        })
      } else if (data.id.startsWith('l_')) {
        this.$emit('selectLeaseType', {
          leaseTypeName: data.leaseTypeName,
          leaseType: data.leaseType
        })
      }
    },
    open() {
      this.initTree()
    }
  }
}
</script>

<style scoped>
.tree-card {
  height: 100%;
}
</style>