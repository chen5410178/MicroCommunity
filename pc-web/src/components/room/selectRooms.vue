<template>
  <el-row :gutter="20">
    <el-col :span="6" class="border-right">
      <div class="text-center margin-bottom">
        <span>{{ $t('selectRoom.buildingUnit') }}</span>
      </div>
      <div class="tree-container">
        <el-tree ref="floorUnitTree" :data="units" :props="treeProps" node-key="id" show-checkbox @check="handleNodeCheck"
          :default-expanded-keys="expandedKeys" />
      </div>
    </el-col>
    <el-col :span="16">
      <div class="text-center margin-bottom">
        <span>{{ $t('selectRoom.roomInfo') }}</span>
      </div>
      <div class="room-container">
        <el-row :gutter="10">
          <el-col v-for="(item, index) in rooms" :key="index" :span="6" class="margin-bottom">
            <el-checkbox v-model="roomIds" :label="item" @change="handleRoomSelect(item)">
              {{ item.floorNum }}-{{ item.unitNum }}-{{ item.roomNum }}({{ item.stateName }})
            </el-checkbox>
          </el-col>
        </el-row>
      </div>
    </el-col>
  </el-row>
</template>

<script>
import { getCommunityId } from '@/api/community/communityApi'
import { queryFloorAndUnits, queryRooms } from '@/api/oa/addQuestionAnswerApi'

export default {
  name: 'SelectRooms',
  props: {
    emitSelectRooms: {
      type: String,
      default: ''
    }
  },
  data() {
    return {
      units: [],
      rooms: [],
      roomIds: [],
      conditions: {
        floorId: '',
        unitId: '',
        communityId: getCommunityId()
      },
      treeProps: {
        label: 'text',
        children: 'children'
      },
      expandedKeys: []
    }
  },
  created() {
    this.loadFloorAndUnits()
  },
  methods: {
    open() {
      this.loadFloorAndUnits()
    },
    async loadFloorAndUnits() {
      try {
        const res = await queryFloorAndUnits({
          communityId: this.conditions.communityId
        })
        this.units = this.formatTreeData(res)
      } catch (error) {
        console.log(error)
        this.$message.error(this.$t('selectRoom.loadError'))
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
            text: `${unit.floorNum}${this.$t('room.floorUnitTree.building')}`,
            children: []
          }
        }

        const unitNode = {
          id: `u_${unit.unitId}`,
          unitId: unit.unitId,
          text: `${unit.unitNum}${this.$t('room.floorUnitTree.unit')}`,
          icon: "/img/unit.png",
          children: []
        }

        floorMap[unit.floorId].children.push(unitNode)
      })

      return Object.values(floorMap)
    },
    async listRooms() {
      try {
        const params = {
          ...this.conditions,
          page: 1,
          row: 500
        }
        const res = await queryRooms(params)
        this.rooms = res.rooms
        this.rooms.forEach(item => {
          this.roomIds.push(item)
        });
        this.handleRoomSelect()
      } catch (error) {
        this.$message.error(this.$t('selectRoom.loadRoomError'))
      }
    },
    handleNodeCheck(data, { checkedNodes }) {
      if (data.id.startsWith('f_')) {
        this.conditions.floorId = checkedNodes.some(node => node.id === data.id) ? data.floorId : ''
      } else if (data.id.startsWith('u_')) {
        this.conditions.unitId = checkedNodes.some(node => node.id === data.id) ? data.unitId : ''
      }
      this.listRooms()
    },
    handleRoomSelect(room) {
      console.log('选择房间', room)
      this.$emit('notifySelectRooms', this.roomIds)
    },
    refreshTree(params) {
      if (params) {
        this.conditions.floorId = params.floorId || ''
      }
      this.loadFloorAndUnits()
    }
  }
}
</script>

<style scoped>
.border-right {
  border-right: 1px solid #ebeef5;
}

.tree-container {
  height: 300px;
  overflow-y: auto;
}

.room-container {
  height: 300px;
  overflow-y: auto;
}

.margin-bottom {
  margin-bottom: 10px;
}

.text-center {
  text-align: center;
  padding: 10px 0;
}
</style>