<template>
  <el-card class="tree-card">
    <el-tree ref="tree" :data="treeData" node-key="id" :props="defaultProps" :highlight-current="true"
      :expand-on-click-node="false" @node-click="handleNodeClick" />
  </el-card>
</template>

<script>
import { queryCommunityFloorTree } from '@/api/fee/adminOweFeeDetailApi'

export default {
  name: 'CommunityFloorTree',
  data() {
    return {
      treeData: [],
      defaultProps: {
        children: 'children',
        label: 'text'
      },
      callName: ''
    }
  },
  mounted() {
    this.initCommunityFloorTree()
  },
  methods: {
    async initCommunityFloorTree() {
      await this.loadCommunityFloorTree()
    },
    async loadCommunityFloorTree() {
      try {
        // 这里应该是从API获取树形数据
       const {data} = await queryCommunityFloorTree()
       console.log(data)
        // 模拟数据
        this.treeData = data
      } catch (error) {
        console.error(error)
      }
    },
    handleNodeClick(data) {
      if (data.id.startsWith('c_')) {
        this.$emit('chooseFloor', {
          communityName: data.communityName,
          communityId: data.communityId,
          floorNum: '',
          floorId: ''
        })
      } else if (data.id.startsWith('f_')) {
        this.$emit('chooseFloor', {
          communityName:'',
          communityId: '',
          floorNum: data.floorNum,
          floorId: data.floorId
        })
      }
    }
  }
}
</script>

<style scoped>
.tree-card {
  max-height: 80vh;
  overflow-y: auto;
}
</style>