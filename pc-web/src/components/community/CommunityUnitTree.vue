<template>
  <el-card class="tree-card">
    <el-tree
      ref="tree"
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
import { queryCommunityUnitTree } from '@/api/community/adminRoomApi'

export default {
  name: 'CommunityUnitTree',
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
    async loadTreeData() {
      try {
        const { data } = await queryCommunityUnitTree({ hc: 1.8 })
        console.log( data)
        this.treeData = data
        //this.processTreeData(data)
      } catch (error) {
        this.$message.error(this.$t('common.fetchError'))
      }
    },
    processTreeData(data) {
      this.treeData = data.map(community => ({
        id: `c_${community.communityId}`,
        label: community.communityName,
        communityId: community.communityId,
        communityName: community.communityName,
        children: community.floors.map(floor => ({
          id: `f_${floor.floorId}`,
          label: floor.floorNum,
          floorId: floor.floorId,
          floorNum: floor.floorNum,
          children: floor.units.map(unit => ({
            id: `u_${unit.unitId}`,
            label: unit.unitNum,
            unitId: unit.unitId,
            unitNum: unit.unitNum
          }))
        }))
      }))
    },
    handleNodeClick(data) {
      if (data.id.startsWith('c_')) {
        this.$emit('selectCommunity', {
          communityId: data.communityId,
          communityName: data.communityName
        })
      } else if (data.id.startsWith('f_')) {
        this.$emit('selectFloor', {
          floorId: data.floorId,
          floorNum: data.floorNum
        })
      } else if (data.id.startsWith('u_')) {
        this.$emit('selectUnit', {
          unitId: data.unitId,
          unitNum: data.unitNum
        })
      }
    },
    open() {
      this.loadTreeData()
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