<template>
  <div class="admin-room-container">
    <div class="flex justify-start">
      <div class="room-floor-unit-tree">
        <community-unit-tree ref="communityUnitTree" @selectCommunity="handleSelectCommunity"
          @selectFloor="handleSelectFloor" @selectUnit="handleSelectUnit" />
      </div>
      <div class="margin-top-xs margin-left-sm flex-grow">
        <el-card class="box-card">
          <div slot="header" class="flex justify-between">
            <span>{{ $t('adminRoom.search.title') }}</span>
          </div>
          <el-row :gutter="20">
            <el-col :span="4">
              <el-input v-model.trim="searchForm.roomNum" :placeholder="$t('adminRoom.search.roomNum')" clearable />
            </el-col>
            <el-col :span="4">
              <el-input v-model.trim="searchForm.ownerNameLike" :placeholder="$t('adminRoom.search.ownerName')"
                clearable />
            </el-col>
            <el-col :span="4">
              <el-input v-model.trim="searchForm.ownerTel" :placeholder="$t('adminRoom.search.ownerTel')" clearable />
            </el-col>
            <el-col :span="4">
              <el-select v-model="searchForm.state" :placeholder="$t('adminRoom.search.state')" clearable>
                <el-option v-for="item in states" :key="item.statusCd" :label="item.name" :value="item.statusCd" />
              </el-select>
            </el-col>
            <el-col :span="4">
              <el-select v-model="searchForm.roomSubType" :placeholder="$t('adminRoom.search.roomType')" clearable>
                <el-option v-for="item in roomSubTypes" :key="item.statusCd" :label="item.name" :value="item.statusCd" />
              </el-select>
            </el-col>
            <el-col :span="4">
              <el-button type="primary" @click="handleSearch">{{ $t('common.search') }}</el-button>
            </el-col>
          </el-row>
        </el-card>

        <el-card class="box-card margin-top">
          <div slot="header" class="flex justify-between">
            <span>{{ $t('adminRoom.list.title') }}</span>
          </div>
          <div class="table-wrapper">
            <el-table :data="tableData" border style="width: 100%">
              <el-table-column prop="roomNum" :label="$t('adminRoom.table.room')" fixed="left" width="120" />
              <el-table-column prop="communityName" :label="$t('adminRoom.table.community')" />
              <el-table-column prop="layer" :label="$t('adminRoom.table.floor')">
                <template slot-scope="scope">{{ scope.row.layer }}{{ $t('adminRoom.table.floorUnit') }}</template>
              </el-table-column>
              <el-table-column prop="ownerName" :label="$t('adminRoom.table.owner')">
                <template slot-scope="scope">
                  <router-link v-if="scope.row.ownerName"
                    :to="`/views/aCommunity/adminOwnerDetail?ownerId=${scope.row.ownerId}`">
                    {{ scope.row.ownerName }}({{ scope.row.link }})
                  </router-link>
                  <span v-else>-</span>
                </template>
              </el-table-column>
              <el-table-column prop="roomSubTypeName" :label="$t('adminRoom.table.type')" />
              <el-table-column :label="$t('adminRoom.table.area')">
                <template slot-scope="scope">{{ scope.row.builtUpArea }}/{{ scope.row.roomArea }}{{
                  $t('adminRoom.table.areaUnit') }}</template>
              </el-table-column>
              <el-table-column prop="roomRent" :label="$t('adminRoom.table.rent')" />
              <el-table-column prop="stateName" :label="$t('adminRoom.table.state')" />
              <el-table-column :label="$t('adminRoom.table.validDate')">
                <template slot-scope="scope">
                  {{ scope.row.startTime }}<br>~{{ scope.row.endTime }}
                </template>
              </el-table-column>
              <el-table-column prop="memberCount" :label="$t('adminRoom.table.member')" />
              <el-table-column prop="carCount" :label="$t('adminRoom.table.car')" />
              <el-table-column prop="roomCount" :label="$t('adminRoom.table.ownerRoom')" />
              <el-table-column prop="contractCount" :label="$t('adminRoom.table.contract')" />
              <el-table-column :label="$t('common.operation')" fixed="right" width="100">
                <template slot-scope="scope">
                  <el-button size="mini" type="primary" @click="handleDetail(scope.row)">
                    {{ $t('common.detail') }}
                  </el-button>
                </template>
              </el-table-column>
            </el-table>
          </div>
          <el-pagination :current-page="pagination.current" :page-sizes="[10, 20, 30, 50]" :page-size="pagination.size"
            layout="total, sizes, prev, pager, next, jumper" :total="pagination.total" @size-change="handleSizeChange"
            @current-change="handleCurrentChange" />
        </el-card>
      </div>
    </div>
  </div>
</template>

<script>
import { queryAdminRoom, getRoomStates, getRoomSubTypes } from '@/api/community/adminRoomApi'
import CommunityUnitTree from '@/components/community/CommunityUnitTree'

export default {
  name: 'AdminRoomList',
  components: {
    CommunityUnitTree
  },
  data() {
    return {
      searchForm: {
        communityId: '',
        floorId: '',
        unitId: '',
        ownerNameLike: '',
        ownerTel: '',
        roomNum: '',
        state: '',
        roomSubType: '',
        page: 1,
        row: 10
      },
      tableData: [],
      states: [],
      roomSubTypes: [],
      pagination: {
        current: 1,
        size: 10,
        total: 0
      },
      loading: false
    }
  },
  computed: {
    // tableWidth() {
    //   const bodyWidth = document.body.clientWidth
    //   const menuWidth = document.getElementById('menu-nav').offsetWidth || 0
    //   const treeWidth = document.querySelector('.room-floor-unit-tree').offsetWidth || 200
    //   return `${bodyWidth - menuWidth - treeWidth - 60}px`
    // }
  },
  created() {
    this.initData()
    this.getList()
  },
  methods: {
    async initData() {
      try {
        const [statesRes, typesRes] = await Promise.all([
          getRoomStates(),
          getRoomSubTypes()
        ])
        this.states = statesRes
        this.roomSubTypes = typesRes
        this.$refs.communityUnitTree.open()
      } catch (error) {
        this.$message.error(this.$t('common.fetchError'))
      }
    },
    async getList() {
      try {
        this.loading = true
        const params = {
          ...this.searchForm,
          page: this.pagination.current,
          row: this.pagination.size
        }
        const { data, total } = await queryAdminRoom(params)
        this.tableData = data
        this.pagination.total = total
      } catch (error) {
        this.$message.error(this.$t('adminRoom.fetchError'))
      } finally {
        this.loading = false
      }
    },
    handleSearch() {
      this.pagination.current = 1
      this.getList()
    },
    handleSelectCommunity({ communityId }) {
      this.searchForm.communityId = communityId
      this.searchForm.floorId = ''
      this.searchForm.unitId = ''
      this.getList()
    },
    handleSelectFloor({ floorId }) {
      this.searchForm.communityId = ''
      this.searchForm.floorId = floorId
      this.searchForm.unitId = ''
      this.getList()
    },
    handleSelectUnit({ unitId }) {
      this.searchForm.communityId = ''
      this.searchForm.floorId = ''
      this.searchForm.unitId = unitId
      this.getList()
    },
    handleDetail(row) {
      this.$router.push(`/pages/community/adminRoomDetail?roomId=${row.roomId}`)
    },
    handleSizeChange(val) {
      this.pagination.size = val
      this.getList()
    },
    handleCurrentChange(val) {
      this.pagination.current = val
      this.getList()
    }
  }
}
</script>

<style lang="scss" scoped>
.admin-room-container {
  padding: 20px;

  .room-floor-unit-tree {
    width: 200px;
    padding-right: 0;
  }

  .flex-grow {
    flex-grow: 1;
  }

  .margin-top {
    margin-top: 20px;
  }

  .table-wrapper {
    overflow-x: auto;
  }

  .el-card {
    margin-bottom: 20px;
  }
}
</style>