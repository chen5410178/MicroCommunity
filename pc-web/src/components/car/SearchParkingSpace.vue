<template>
  <el-dialog :title="$t('searchParkingSpace.title')" :visible.sync="visible" width="80%" @close="handleClose">

      <el-row v-if="showSearchCondition" :gutter="20">
        <el-col :span="8">
          <el-select v-model="searchParkingSpaceInfo.areaNum" class="w-full"
            :placeholder="$t('searchParkingSpace.parkingAreaPlaceholder')">
            <el-option v-for="item in parkingAreas" :key="item.num" :label="item.num" :value="item.num" />
          </el-select>
        </el-col>
        <el-col :span="8">
          <el-input v-model="searchParkingSpaceInfo.num" :placeholder="$t('searchParkingSpace.numPlaceholder')" />
        </el-col>
        <el-col :span="8">
          <el-button type="primary" @click="searchParkingSpaces">
            {{ $t('common.search') }}
          </el-button>
          <el-button @click="resetParkingSpaces">
            {{ $t('common.reset') }}
          </el-button>
        </el-col>
      </el-row>

      <el-table :data="searchParkingSpaceInfo.parkingSpaces" class="mt-20">
        <el-table-column prop="areaNum" :label="$t('searchParkingSpace.areaNum')" align="center" />
        <el-table-column prop="num" :label="$t('searchParkingSpace.num')" align="center" />
        <el-table-column :label="$t('searchParkingSpace.type')" align="center">
          <template slot-scope="scope">
            {{ scope.row.typeCd === '1001' ? $t('searchParkingSpace.ground') : $t('searchParkingSpace.underground') }}
          </template>
        </el-table-column>
        <el-table-column prop="parkingTypeName" :label="$t('searchParkingSpace.parkingType')" align="center" />
        <el-table-column prop="stateName" :label="$t('searchParkingSpace.state')" align="center" />
        <el-table-column prop="area" :label="$t('searchParkingSpace.area')" align="center" />
        <el-table-column :label="$t('common.operation')" align="center" width="120">
          <template slot-scope="scope">
            <el-button type="primary" size="small" @click="chooseParkingSpace(scope.row)">
              {{ $t('common.select') }}
            </el-button>
          </template>
        </el-table-column>
      </el-table>

      <el-pagination class="mt-20" :current-page="pagination.currentPage" :page-sizes="[10, 20, 30, 50]"
        :page-size="pagination.pageSize" :total="pagination.total" layout="total, sizes, prev, pager, next, jumper"
        @size-change="handleSizeChange" @current-change="handleCurrentChange" />

  </el-dialog>
</template>

<script>
import { queryParkingSpaces, listParkingAreas } from '@/api/car/hireParkingSpaceApi'
import { getCommunityId } from '@/api/community/communityApi'

export default {
  name: 'SearchParkingSpace',
  props: {
    parkingSpaceFlag: {
      type: String,
      default: 'F'
    },
    showSearchCondition: {
      type: Boolean,
      default: true
    }
  },
  data() {
    return {
      visible: false,
      searchParkingSpaceInfo: {
        parkingSpaces: [],
        areaNum: '',
        num: '',
        carNum: ''
      },
      parkingAreas: [],
      pagination: {
        currentPage: 1,
        pageSize: 10,
        total: 0
      }
    }
  },
  async created() {
    await this.listParkingAreas()
  },
  methods: {
    open() {
      this.visible = true
      this.resetParkingSpaces()
      this.loadAllParkingSpaceInfo()
    },

    handleClose() {
      this.visible = false
    },

    async listParkingAreas() {
      try {
        const params = {
          page: 1,
          row: 50,
          communityId: getCommunityId()
        }

        const { data } = await listParkingAreas(params)
        this.parkingAreas = data
      } catch (error) {
        console.error('加载停车场区域失败:', error)
      }
    },

    async loadAllParkingSpaceInfo() {
      try {
        const params = {
          page: this.pagination.currentPage,
          row: this.pagination.pageSize,
          communityId: getCommunityId(),
          num: this.searchParkingSpaceInfo.num,
          areaNum: this.searchParkingSpaceInfo.areaNum,
          carNum: this.searchParkingSpaceInfo.carNum,
          state: this.parkingSpaceFlag
        }

        const { parkingSpaces, total } = await queryParkingSpaces(params)
        this.searchParkingSpaceInfo.parkingSpaces = parkingSpaces
        this.pagination.total = total
      } catch (error) {
        console.error('加载停车位信息失败:', error)
        this.$message.error(this.$t('searchParkingSpace.loadError'))
      }
    },

    chooseParkingSpace(parkingSpace) {
      this.$emit('choose-parking-space', parkingSpace)
      this.visible = false
    },

    searchParkingSpaces() {
      this.pagination.currentPage = 1
      this.loadAllParkingSpaceInfo()
    },

    resetParkingSpaces() {
      this.searchParkingSpaceInfo.carNum = ''
      this.searchParkingSpaceInfo.num = ''
      this.searchParkingSpaceInfo.areaNum = ''
      this.pagination.currentPage = 1
      this.loadAllParkingSpaceInfo()
    },

    handleSizeChange(size) {
      this.pagination.pageSize = size
      this.loadAllParkingSpaceInfo()
    },

    handleCurrentChange(page) {
      this.pagination.currentPage = page
      this.loadAllParkingSpaceInfo()
    }
  }
}
</script>

<style scoped>
.w-full {
  width: 100%;
}

.mt-20 {
  margin-top: 20px;
}
</style>