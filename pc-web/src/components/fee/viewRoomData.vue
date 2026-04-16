<template>
  <el-dialog
    :title="title"
    :visible.sync="dialogVisible"
    width="50%"
    @close="handleClose"
  >
    <el-table
      :data="tableData"
      border
      style="width: 100%"
    >
      <el-table-column
        prop="label"
        :label="$t('viewRoomData.label')"
        width="180"
      />
      <el-table-column
        prop="value"
        :label="$t('viewRoomData.value')"
      />
    </el-table>
  </el-dialog>
</template>

<script>
import { getCommunityId } from '@/api/community/communityApi'

export default {
  name: 'ViewRoomData',
  data() {
    return {
      dialogVisible: false,
      title: '',
      tableData: [],
      roomId: ''
    }
  },
  methods: {
    open(params) {
      this.roomId = params.roomId
      this.loadViewRoomData()
      this.dialogVisible = true
    },
    close() {
      this.dialogVisible = false
    },
    async loadViewRoomData() {
      try {
        const communityId = await getCommunityId()
        const params = {
          page: 1,
          row: 1,
          communityId,
          roomId: this.roomId
        }

        const response = await this.$http.get('/room.queryRooms', { params })
        const room = response.data.rooms[0]

        this.title = `${room.floorNum}-${room.unitNum}-${room.roomNum} ${this.$t('viewRoomData.details')}`
        
        const data = {
          [this.$t('viewRoomData.room')]: `${room.floorNum}-${room.unitNum}-${room.roomNum}`,
          [this.$t('viewRoomData.floor')]: room.layer,
          [this.$t('viewRoomData.owner')]: room.ownerName,
          [this.$t('viewRoomData.phone')]: room.link,
          [this.$t('viewRoomData.type')]: room.roomSubTypeName,
          [this.$t('viewRoomData.builtUpArea')]: room.builtUpArea,
          [this.$t('viewRoomData.roomArea')]: room.roomArea,
          [this.$t('viewRoomData.rent')]: room.roomRent,
          [this.$t('viewRoomData.state')]: room.stateName,
          [this.$t('viewRoomData.moveInTime')]: room.startTime
        }

        if (room.roomAttrDto) {
          room.roomAttrDto.forEach(attr => {
            data[attr.specName] = attr.valueName
          })
        }

        this.tableData = Object.keys(data).map(key => ({
          label: key,
          value: data[key]
        }))
      } catch (error) {
        console.error('加载房间数据失败:', error)
      }
    },
    handleClose() {
      this.tableData = []
      this.title = ''
      this.roomId = ''
    }
  }
}
</script>