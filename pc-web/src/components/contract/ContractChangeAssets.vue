<template>
  <el-card class="box-card">
    <div slot="header" class="flex justify-between">
      <span>{{ $t('contractChangeAssets.title') }}</span>
      <el-button type="primary" size="small" style="float: right" @click="openSeachRoom">
        <i class="el-icon-plus"></i>
        {{ $t('common.add') }}
      </el-button>
    </div>

    <el-table :data="contractChangeAssetsInfo.rooms" border style="width: 100%">
      <el-table-column prop="roomNum" :label="$t('contractChangeAssets.room')" align="center">
        <template slot-scope="scope">
          {{ scope.row.floorNum }}-{{ scope.row.unitNum }}-{{ scope.row.roomNum }}
        </template>
      </el-table-column>

      <el-table-column prop="ownerName" :label="$t('contractChangeAssets.owner')" align="center" />

      <el-table-column prop="link" :label="$t('contractChangeAssets.phone')" align="center" />

      <el-table-column prop="builtUpArea" :label="$t('contractChangeAssets.area')" align="center">
        <template slot-scope="scope">
          {{ scope.row.builtUpArea }} {{ $t('contractChangeAssets.squareMeters') }}
        </template>
      </el-table-column>

      <el-table-column prop="stateName" :label="$t('contractChangeAssets.status')" align="center" />

      <el-table-column :label="$t('common.operation')" align="center" width="120">
        <template slot-scope="scope">
          <el-button type="danger" size="mini" @click="openDelRoomModel(scope.row)">
            {{ $t('common.delete') }}
          </el-button>
        </template>
      </el-table-column>
    </el-table>

    <search-room ref="searchRoom" @chooseRoom="chooseRoom" />
  </el-card>
</template>

<script>
import SearchRoom from '@/components/room/searchRoom'
import { queryContractRoom } from '@/api/contract/addContractApi'
export default {
  name: 'ContractChangeAssets',
  components: {
    SearchRoom
  },
  data() {
    return {
      contractChangeAssetsInfo: {
        rooms: [],
        contractId: '',
        planType: '3003'
      }
    }
  },
  watch: {
    contractChangeAssetsInfo: {
      deep: true,
      handler(newVal) {
        this.$emit('changeNotify', newVal)
      }
    }
  },
  methods: {
    open(param) {
      this.contractChangeAssetsInfo.contractId = param.contractId
      this.loadContractRooms()
    },
    openSeachRoom() {
      this.$refs.searchRoom.open()
    },
    openDelRoomModel(room) {
      this.$confirm(
        this.$t('contractChangeAssets.confirmDelete'),
        this.$t('common.tip'),
        {
          confirmButtonText: this.$t('common.confirm'),
          cancelButtonText: this.$t('common.cancel'),
          type: 'warning'
        }
      ).then(() => {
        this.removeRoom(room)
      })
    },
    removeRoom(room) {
      this.contractChangeAssetsInfo.rooms = this.contractChangeAssetsInfo.rooms.filter(
        item => item.roomId !== room.roomId
      )
    },
    async loadContractRooms() {
      const res = await queryContractRoom({ contractId: this.contractChangeAssetsInfo.contractId, page: 1, row: 500 })
      this.contractChangeAssetsInfo.rooms = res.data
    },
    chooseRoom(room) {
      const exists = this.contractChangeAssetsInfo.rooms.some(
        item => item.roomId === room.roomId
      )
      if (!exists) {
        this.contractChangeAssetsInfo.rooms.push(room)
      }
    }
  },
}
</script>

<style scoped>
.box-card {
  margin-bottom: 20px;
}
</style>