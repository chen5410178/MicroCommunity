<template>
  <el-card class="tree-card">
    <el-tree
      ref="tree"
      :data="treeData"
      :props="defaultProps"
      node-key="id"
      :default-expanded-keys="defaultExpandedKeys"
      :highlight-current="true"
      @node-click="handleNodeClick"
    >
      <template #default="{ node, data }">
        <span class="custom-tree-node">
          <img v-if="data.icon" :src="data.icon" class="tree-icon" />
          <span>{{ node.label }}</span>
        </span>
      </template>
    </el-tree>
  </el-card>
</template>

<script>
import { queryFloorAndUnits } from '@/api/room/roomStructureApi'
import { getCommunityId } from '@/api/community/communityApi'

export default {
  name: 'FloorUnitAllTree',
  props: {
    callBackListener: {
      type: String,
      default: ''
    }
  },
  data() {
    return {
      floorUnitTreeInfo: {
        units: [],
        floorId: ''
      },
      treeData: [],
      defaultProps: {
        children: 'children',
        label: 'text'
      },
      defaultExpandedKeys: [],
      communityId: ''
    }
  },
  created() {
    this.communityId = getCommunityId()
    this.loadFloorAndUnits()
  },
  methods: {
    async loadFloorAndUnits() {
      try {
        const params = {
          communityId: this.communityId
        }
        const data = await queryFloorAndUnits(params)
        this.floorUnitTreeInfo.units = data
        this.initTreeData()
      } catch (error) {
        console.error('Failed to load floor and units:', error)
      }
    },
    initTreeData() {
      this.treeData = this.doTreeData()
      if (this.treeData.length > 0 && this.treeData[0].children) {
        this.defaultExpandedKeys = [this.treeData[0].id]
      }
    },
    doTreeData() {
      const treeData = []
      const units = this.floorUnitTreeInfo.units
      
      // Build first level menu (floors)
      units.forEach(item => {
        const existingFloor = treeData.find(floor => floor.floorId === item.floorId)
        if (!existingFloor) {
          const floorItem = {
            id: `f_${item.floorId}`,
            floorId: item.floorId,
            floorNum: item.floorNum,
            icon: "/img/floor.png",
            text: `${item.floorNum}${this.$t('floorUnitTree.building')}(${item.floorName})`,
            children: this.doTreeChildrenData(item.floorId)
          }
          treeData.push(floorItem)
        }
      })
      
      return treeData
    },
    doTreeChildrenData(floorId) {
      const children = []
      const units = this.floorUnitTreeInfo.units
      
      units.forEach(item => {
        if (item.floorId === floorId && item.unitId) {
          children.push({
            id: `u_${item.unitId}`,
            unitId: item.unitId,
            text: `${item.unitNum}${this.$t('floorUnitTree.unit')}`,
            icon: "/img/unit.png"
          })
        }
      })
      
      return children
    },
    handleNodeClick(data) {
      if (data.id.startsWith('f_')) {
        this.$emit('switchFloor', {
          floorId: data.floorId
        })
      } else {
        this.$emit('switchUnit', {
          unitId: data.unitId
        })
      }
    },
    refreshTree(params) {
      if (params) {
        this.floorUnitTreeInfo.floorId = params.floorId
      }
      this.loadFloorAndUnits()
    }
  }
}
</script>

<style lang="scss" scoped>
.tree-card {
  height: 100%;
  
  .custom-tree-node {
    display: flex;
    align-items: center;
    
    .tree-icon {
      width: 16px;
      height: 16px;
      margin-right: 5px;
    }
  }
  
  :deep(.el-tree-node__content) {
    height: 36px;
  }
}
</style>