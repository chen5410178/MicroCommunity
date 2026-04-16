<template>
  <el-card class="tree-card">
    <el-tree
      ref="tree"
      :data="treeData"
      :props="defaultProps"
      node-key="id"
      highlight-current
      @node-click="handleNodeClick"
    />
  </el-card>
</template>

<script>
import { queryCommunityUnitTree, queryAdminRoomsTree } from '@/api/fee/adminRoomFeeApi'

export default {
  name: 'CommunityRoomTree',
  data() {
    return {
      treeData: [],
      defaultProps: {
        children: 'children',
        label: 'text'
      },
      loadedUnits: new Set() // Track which units have loaded their rooms
    }
  },
  mounted() {
    this.initCommunityRoomTree()
  },
  methods: {
    async initCommunityRoomTree() {
      try {
        const res = await queryCommunityUnitTree({ a: 1 })
        if (res.code === 0) {
          this.treeData = res.data
        }
      } catch (error) {
        console.error('Failed to load community tree:', error)
      }
    },

    async handleNodeClick(data) {
      // Only handle unit nodes (u_ prefix) that haven't been loaded yet
      if (data.id.startsWith('u_') && !this.loadedUnits.has(data.id)) {
        await this.loadRooms(data.unitId || data.id.replace('u_', ''))
        this.loadedUnits.add(data.id)
      }
      
      // Emit event for room selection
      if (data.id.startsWith('r_')) {
        this.$emit('select-room', {
          roomName: data.roomName,
          roomId: data.roomId
        })
      }
    },

    async loadRooms(unitId) {
      try {
        const param = {
          page: 1,
          row: 1000,
          unitId: unitId
        }

        const res = await queryAdminRoomsTree(param)
        if (res.rooms && res.rooms.length > 0) {
          const rooms = res.rooms.map(room => ({
            id: `r_${room.roomId}`,
            roomId: room.roomId,
            roomName: `${room.floorNum}-${room.unitNum}-${room.roomNum}`,
            text: room.ownerName ? `${room.roomNum}(${room.ownerName})` : room.roomNum
          }))

          // Find and update the unit node
          const unitNode = this.findNodeById(`u_${unitId}`, this.treeData)
          if (unitNode) {
            this.$set(unitNode, 'children', rooms)
            
            // Auto-select the first room if needed
            if (rooms.length > 0) {
              this.$nextTick(() => {
                this.$refs.tree.setCurrentKey(rooms[0].id)
                this.$emit('select-room', {
                  roomName: rooms[0].roomName,
                  roomId: rooms[0].roomId
                })
              })
            }
          }
        }
      } catch (error) {
        console.error('Failed to load rooms:', error)
      }
    },

    findNodeById(id, nodes) {
      if (!nodes) return null
      
      for (const node of nodes) {
        if (node.id === id) return node
        if (node.children) {
          const found = this.findNodeById(id, node.children)
          if (found) return found
        }
      }
      return null
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