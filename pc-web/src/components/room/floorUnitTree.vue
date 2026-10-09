<template>
  <div class="bg-white margin-top-xs padding border-radius tree-div">
    <el-tree
      v-if="treeData.length > 0"
      :data="treeData"
      :props="defaultProps"
      node-key="id"
      :expand-on-click-node="false"
      @node-click="handleNodeClick"
    >
      <template #default="{ node, data }">
        <span class="custom-tree-node">
          <i :class="data.icon" style="margin-right:5px"></i>
          <span>{{ node.label }}</span>
        </span>
      </template>
    </el-tree>
    <div v-else class="no-data">
      {{ $t('room.floorUnitTree.noData') }}
    </div>
  </div>
</template>

<script>
import { getFloorAndUnits } from '@/api/room/floorUnitTreeApi'

export default {
  name: 'FloorUnitTree',
  props: {
    callBackListener: String
  },
  data() {
    return {
      treeData: [],
      defaultProps: {
        children: 'children',
        label: 'text',
      },
      currentFloorId: ''
    }
  },
  mounted() {
    this.loadData()
  },
  beforeDestroy() {
  },
  methods: {
    refreshTree(param) {
      this.handleRefreshTree(param)
    },
    selectFirstUnit(){
      if(this.treeData.length > 0){
        this.handleNodeClick(this.treeData[0].children[0])
        // 并且展开

      }
    },
    handleRefreshTree(param) {
      if (param) {
        this.currentFloorId = param.floorId
      }
      this.loadData()
    },
    async loadData() {
      try {
        const communityId = this.getCommunityId()
        const response = await getFloorAndUnits(communityId)
        this.treeData = this.transformData(response)
      } catch (error) {
        console.error('加载楼栋单元树失败', error)
      }
    },
    transformData(units) {
      const treeData = []
      const floorMap = {}
      
      units.forEach(item => {
        if (!floorMap[item.floorId]) {
          floorMap[item.floorId] = {
            id: `f_${item.floorId}`,
            floorId: item.floorId,
            floorNum: item.floorNum,
            icon: 'el-icon-office-building',
            text: `${item.floorNum}(${item.floorName})`,
            children: []
          }
          treeData.push(floorMap[item.floorId])
        }
        
        if (item.unitId) {
          floorMap[item.floorId].children.push({
            id: `u_${item.unitId}`,
            unitId: item.unitId,
            text: `${item.unitNum}`,
            icon: 'el-icon-house'
          })
        }
      })
      
      return treeData
    },
    handleNodeClick(data) {
      if (data.id.startsWith('f_')) {
        this.$emit('switchFloorUnit', { floorId: data.floorId,unitId: '' })
      } else {
        this.$emit('switchFloorUnit', { floorId:'',unitId: data.unitId })
      }
    },
  }
}
</script>

<style scoped>
.tree-div {
  min-height: 300px;
  padding-bottom: 40px;
}
.custom-tree-node {
  display: flex;
  align-items: center;
}
.no-data {
  text-align: center;
  padding: 20px;
  color: #909399;
}
.custom-tree-node {
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
  max-width: 200px; /* 根据你的布局调整 */
}
</style>