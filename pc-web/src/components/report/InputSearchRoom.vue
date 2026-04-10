<template>
  <div class="input-search-room">
    <el-popover placement="bottom" width="300" trigger="manual" v-model="visible">
      <div class="search-result-container">
        <div class="close-btn" @click="close">
          <i class="el-icon-close"></i>
        </div>
        <div class="search-item" v-for="(item, index) in rooms" :key="index" @click="chooseRoom(item)">
          <span>{{ item.floorNum }}-{{ item.unitNum }}-{{ item.roomNum }}</span>
        </div>
      </div>
      <el-button slot="reference" type="primary" icon="el-icon-search"></el-button>
    </el-popover>
  </div>
</template>

<script>
import { queryRooms } from '@/api/room/roomApi'
import { getCommunityId } from '@/api/community/communityApi'

export default {
  name: 'InputSearchRoom',
  data() {
    return {
      visible: false,
      rooms: [],
      searchParam: {
        roomName: '',
        callComponent: ''
      },
      communityId: ''
    }
  },
  created() {
    this.communityId = getCommunityId()
  },
  methods: {
    open(param) {
      this.searchParam = { ...param }
      this.loadRooms()
      this.visible = true
    },
    close() {
      this.visible = false
    },
    async loadRooms() {
      try {
        const params = {
          page: 1,
          row: 10,
          flag: 1,
          roomNum: this.searchParam.roomName,
          communityId: this.communityId
        }

        const { data } = await queryRooms(params)
        this.rooms = data.rooms || []
        if (this.rooms.length === 0) {
          this.$message.warning(this.$t('common.noData'))
        }
      } catch (error) {
        console.error('查询房屋失败:', error)
        this.$message.error(this.$t('common.queryFailed'))
      }
    },
    chooseRoom(room) {
      this.$emit('notifyRoom', room)
      this.$emit('choose', room)
      this.close()
    }
  }
}
</script>

<style lang="scss" scoped>
.input-search-room {
  display: inline-block;

  .search-result-container {
    max-height: 300px;
    overflow-y: auto;
    padding: 10px;

    .close-btn {
      text-align: right;
      margin-bottom: 10px;
      cursor: pointer;

      i {
        font-size: 16px;
        color: #999;

        &:hover {
          color: #333;
        }
      }
    }

    .search-item {
      padding: 8px 10px;
      border-bottom: 1px solid #eee;
      cursor: pointer;

      &:hover {
        background-color: #f5f7fa;
      }

      &:last-child {
        border-bottom: none;
      }
    }
  }

  .el-button {
    padding: 10px;
  }
}
</style>