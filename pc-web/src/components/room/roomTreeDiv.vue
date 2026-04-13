<template>
  <div class="room-tree-container">
    <el-tree ref="tree" :data="treeData" node-key="id" :props="defaultProps" :highlight-current="true"
      :expand-on-click-node="false" @node-click="handleNodeClick">
      <span slot-scope="{ node, data }" class="custom-tree-node">
        <span>
          <i :class="data.icon" style="margin-right: 5px"></i>
          {{ node.label }}
        </span>
      </span>
    </el-tree>
  </div>
</template>

<script>
import { queryUnits, queryRoomsTree } from '@/api/fee/meterWaterManageApi'
import { getCommunityId } from '@/api/community/communityApi'

export default {
  name: 'RoomTreeDiv',
  data() {
    return {
      treeData: [],
      defaultProps: {
        children: 'children',
        label: 'text'
      },
      communityId: '',
      lastSelected: {},
      isInitialized: false // 添加初始化标志
    }
  },
  created() {
    this.communityId = getCommunityId()
    this.loadTreeData()
  },
  methods: {
    async loadTreeData() {
      // 加载上次选择的房屋信息
      let lastSelected = localStorage.getItem('lastSelectedRoom');
      if (lastSelected) {
        try {
          this.lastSelected = JSON.parse(lastSelected);
        } catch (error) {
          console.error('解析lastSelected失败:', error);
          localStorage.removeItem('lastSelectedRoom');
        }
      }
      
      try {
        const units = await queryUnits({
          communityId: this.communityId
        })
        this.buildTreeData(units)
      } catch (error) {
        console.error('Failed to load tree data:', error)
        this.$message.error(this.$t('roomTree.loadError'))
      }
    },
    buildTreeData(units) {
      const floorMap = {}

      // Build floor nodes and unit nodes
      units.forEach(unit => {
        if (!floorMap[unit.floorId]) {
          floorMap[unit.floorId] = {
            id: `f_${unit.floorId}`,
            floorId: unit.floorId,
            floorNum: unit.floorNum,
            icon: "/img/floor.png",
            text: `${unit.floorNum}`,
            children: []
          }
        }

        floorMap[unit.floorId].children.push({
          id: `u_${unit.unitId}`,
          unitId: unit.unitId,
          unitNum: unit.unitNum,
          floorId: unit.floorId, // Add floorId reference
          icon: "/img/unit.png",
          text: `${unit.unitNum}`,
          children: []
        })
      })

      this.treeData = Object.values(floorMap)
      this.$nextTick(() => {
        if (this.treeData.length === 0) {
          this.$emit('treeEmpty')
        }
        // 如果有上次选择的房屋，则展开并选择；否则选择第一个房屋
        if (this.lastSelected && this.lastSelected.floorId && this.lastSelected.unitId) {
          this.expandAndSelectLastRoom()
        } else {
          this.expandAndSelectFirstRoom()
        }
      })
    },
    // 新增：展开并选择上次选择的房屋
    async expandAndSelectLastRoom() {
      if (!this.lastSelected || !this.lastSelected.floorId || !this.lastSelected.unitId) {
        this.expandAndSelectFirstRoom()
        return
      }

      const treeRef = this.$refs.tree
      if (!treeRef || !treeRef.store) {
        // 如果树还没有完全初始化，延迟执行
        setTimeout(() => this.expandAndSelectLastRoom(), 100)
        return
      }

      try {
        // 查找对应的楼栋和单元
        const floorNode = this.treeData.find(floor => floor.floorId === this.lastSelected.floorId)
        if (!floorNode) {
          console.warn('未找到对应的楼栋:', this.lastSelected.floorId)
          this.expandAndSelectFirstRoom()
          return
        }

        const unitNode = floorNode.children.find(unit => unit.unitId === this.lastSelected.unitId)
        if (!unitNode) {
          console.warn('未找到对应的单元:', this.lastSelected.unitId)
          this.expandAndSelectFirstRoom()
          return
        }

        // 展开楼栋和单元
        const floorTreeNode = treeRef.store.nodesMap[floorNode.id]
        const unitTreeNode = treeRef.store.nodesMap[unitNode.id]
        
        if (floorTreeNode) {
          floorTreeNode.expanded = true
        }
        if (unitTreeNode) {
          unitTreeNode.expanded = true
        }

        // 加载房屋数据
        await this.loadRooms(unitNode, { data: unitNode })

        // 选择上次选择的房屋
        if (this.lastSelected.roomId) {
          this.selectSpecificRoom(this.lastSelected.roomId)
        }

        this.isInitialized = true
      } catch (error) {
        console.error('展开上次选择的房屋失败:', error)
        this.expandAndSelectFirstRoom()
      }
    },
    // 新增：选择特定的房屋
    selectSpecificRoom(roomId) {
      const treeRef = this.$refs.tree
      if (!treeRef) return

      const roomNodeId = `r_${roomId}`
      const roomTreeNode = treeRef.store.nodesMap[roomNodeId]
      
      if (roomTreeNode) {
        // 设置当前选中节点
        treeRef.setCurrentKey(roomNodeId)
        
        // 触发选择事件
        this.$emit('selectRoom', {
          roomId: roomId,
          roomName: this.lastSelected.roomName || ''
        })
      } else {
        // 如果房屋节点还没有加载，等待加载完成后再选择
        setTimeout(() => this.selectSpecificRoom(roomId), 200)
      }
    },
    async expandAndSelectFirstRoom() {
      if (!this.treeData.length) return
      
      const firstFloor = this.treeData[0]
      if (!firstFloor.children || !firstFloor.children.length) return
      const firstUnit = firstFloor.children[0]
      const treeRef = this.$refs.tree
      if (treeRef && treeRef.store) {
        treeRef.store.nodesMap[firstFloor.id] && (treeRef.store.nodesMap[firstFloor.id].expanded = true)
        treeRef.store.nodesMap[firstUnit.id] && (treeRef.store.nodesMap[firstUnit.id].expanded = true)
      }
      await this.loadRooms(firstUnit, { data: firstUnit })
      this.isInitialized = true
    },
    async handleNodeClick(data, node) {
      if (data.id.startsWith('u_')) {
        if (!node.expanded) {
          await this.loadRooms(data, node)
          node.expanded = true
        }
      } else if (data.id.startsWith('r_')) {
        // 获取父节点信息
        const parentNodes = this.getParentNodes(data)
        let selectedData = {
          floorId: parentNodes.floorId,
          unitId: parentNodes.unitId,
          roomId: data.roomId,
          roomName: data.roomName
        };
        
        // 保存到localStorage
        localStorage.setItem('lastSelectedRoom', JSON.stringify(selectedData));
        this.lastSelected = selectedData;

        this.$emit('selectRoom', {
          roomId: data.roomId,
          roomName: data.roomName
        })
      }
    },
    // 新增：获取父节点信息
    getParentNodes(data) {
      const treeRef = this.$refs.tree
      if (!treeRef || !treeRef.store) return { floorId: '', unitId: '' }

      const node = treeRef.store.nodesMap[data.id]
      if (!node || !node.parent) return { floorId: '', unitId: '' }

      const unitNode = node.parent
      const floorNode = unitNode.parent

      return {
        floorId: floorNode ? floorNode.data.floorId : '',
        unitId: unitNode ? unitNode.data.unitId : ''
      }
    },
    async loadRooms(unitData, node) {
      try {
        const { rooms } = await queryRoomsTree({
          unitId: unitData.unitId,
          communityId: this.communityId,
          page: 1,
          row: 1000
        })

        if (rooms && rooms.length > 0) {
          const roomNodes = rooms.map(room => ({
            id: `r_${room.roomId}`,
            roomId: room.roomId,
            roomName: `${room.floorNum}-${room.unitNum}-${room.roomNum}`,
            icon: "/img/room.png",
            text: room.ownerName
              ? `${room.roomNum}(${room.ownerName})`
              : `${room.roomNum}`
          }))

          // Update the node's children
          this.$set(node.data, 'children', roomNodes)
          
          // 只有在初始化时才自动选择第一个房屋
          if (!this.isInitialized) {
            this.$emit('selectRoom', {
              roomId: roomNodes[0].roomId,
              roomName: roomNodes[0].roomName
            })
          }
        }
      } catch (error) {
        console.error('Failed to load rooms:', error)
        this.$message.error(this.$t('roomTree.loadRoomError'))
      }
    },
    // 新增：清除保存的选择记录
    clearLastSelected() {
      localStorage.removeItem('lastSelectedRoom')
      this.lastSelected = {}
    }
  }
}
</script>

<style lang="scss" scoped>
.room-tree-container {
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