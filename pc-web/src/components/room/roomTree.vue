<template>
    <div class="room-tree-container">
        <el-dialog :visible.sync="dialogVisible" :title="$t('roomList.selectRoom')" width="280px"
            custom-class="right-dialog" :modal="false">
            <el-tree ref="roomTree" :data="treeData" :props="defaultProps" node-key="id" highlight-current
                :expand-on-click-node="false" @node-click="handleNodeClick">
                <span slot-scope="{ node, data }" class="custom-tree-node">
                    <img :src="data.icon" class="tree-icon" v-if="data.icon">
                    <span>{{ node.label }}</span>
                </span>
            </el-tree>
        </el-dialog>
    </div>
</template>
  
<script>
import { getUnits, queryRoomsTree } from '@/api/room/roomApi'
import { getCommunityId } from '@/api/community/communityApi'

export default {
    name: 'RoomTreeList',
    data() {
        return {
            dialogVisible: false,
            callName: '',
            treeData: [],
            defaultProps: {
                children: 'children',
                label: 'text'
            }
        }
    },
    methods: {
        open(callName) {
            this.callName = callName
            this.dialogVisible = true
            this.loadRoomTreeFloorAndUnits()
        },
        async loadRoomTreeFloorAndUnits() {
            try {
                const communityId = getCommunityId()
                const data = await getUnits({ communityId })
                this.treeData = this.formatTreeData(data)
            } catch (error) {
                this.$message.error(this.$t('roomTree.loadError'))
            }
        },
        formatTreeData(units) {
            const floorMap = {}

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

                const unitNode = {
                    id: `u_${unit.unitId}`,
                    unitId: unit.unitId,
                    text: `${unit.unitNum}`,
                    icon: "/img/unit.png",
                    children: []
                }

                floorMap[unit.floorId].children.push(unitNode)
            })

            return Object.values(floorMap)
        },
        async handleNodeClick(data, node) {
            if (data.id.startsWith('u_')) {
                if (!node.expanded) {
                    await this.loadRooms(data.unitId, node)
                }
            } else if (data.id.startsWith('r_')) {
                this.dialogVisible = false
                this.$emit('selectRoom', {
                    roomName: data.roomName,
                    roomId: data.roomId
                })
            }
        },
        async loadRooms(unitId, node) {
            try {
                const communityId = getCommunityId()
                const { rooms } = await queryRoomsTree({
                    unitId,
                    communityId,
                    page: 1,
                    row: 1000
                })
                console.log(rooms);

                if (rooms && rooms.length > 0) {
                    const roomDatas = rooms.map(room => ({
                        id: `r_${room.roomId}`,
                        roomId: room.roomId,
                        roomName: `${room.floorNum}-${room.unitNum}-${room.roomNum}`,
                        text: room.ownerName ? `${room.roomNum}(${room.ownerName})` : room.roomNum,
                        icon: "/img/room.png"
                    }))

                    this.$set(node.data, 'children', roomDatas)
                }
            } catch (error) {
                console.error(this.$t('roomTree.loadRoomError'), error)
                this.$message.error(this.$t('roomTree.loadRoomError'))
            }
        }
    }
}
</script>
<style lang="scss" scoped>
/* 删除 scoped 改为全局样式 */
.room-tree-container ::v-deep .right-dialog {
  position: fixed !important;
  right: 0 !important;
  top: 0 !important;
  margin: 0 !important;
  height: 100vh !important;
  padding-bottom: 20px;
  
  .el-dialog {
    margin: 0 !important;
    height: 100% !important;
    border-radius: 0 !important;
  }
  
  .el-dialog__header {
    padding: 20px !important;
    border-bottom: 1px solid #eee;
  }
  
  .el-dialog__body {
    padding: 10px 20px !important;
    height: calc(100% - 61px) !important; /* 减去header高度 */
    overflow-y: auto;
  }
}
</style>