<template>
  <div class="report-no-fee-room-container animated fadeInRight">

        <el-card class="search-card">
          <div slot="header" class="flex justify-between">
            <span>{{ $t('reportNoFeeRoom.search.title') }}</span>
          </div>
          <el-row :gutter="20">
            <el-col :span="4">
              <el-select v-model="searchForm.floorId" :placeholder="$t('reportNoFeeRoom.search.floor')"
                @change="handleFloorChange" style="width:100%">
                <el-option v-for="item in floors" :key="item.floorId" :label="item.floorName" :value="item.floorId" />
              </el-select>
            </el-col>
            <el-col :span="4">
              <el-select v-model="searchForm.unitId" :placeholder="$t('reportNoFeeRoom.search.unit')"
                @change="handleUnitChange" style="width:100%">
                <el-option v-for="item in units" :key="item.unitId"
                  :label="`${item.unitNum}`" :value="item.unitId" />
              </el-select>
            </el-col>
            <el-col :span="4">
              <el-select v-model="searchForm.roomId" :placeholder="$t('reportNoFeeRoom.search.room')" style="width:100%">
                <el-option v-for="item in rooms" :key="item.roomId"
                  :label="`${item.roomNum}`" :value="item.roomId" />
              </el-select>
            </el-col>
            <el-col :span="4">
              <el-input v-model="searchForm.ownerName" :placeholder="$t('reportNoFeeRoom.search.ownerName')" clearable />
            </el-col>
            <el-col :span="4">
              <el-input v-model="searchForm.link" :placeholder="$t('reportNoFeeRoom.search.link')" clearable />
            </el-col>
            <el-col :span="4">
              <el-button type="primary" @click="handleSearch">
                <i class="el-icon-search"></i>
                {{ $t('common.search') }}
              </el-button>
              <el-button @click="handleReset" style="margin-left:10px;">
                <i class="el-icon-refresh"></i>
                {{ $t('common.reset') }}
              </el-button>
            </el-col>
          </el-row>
        </el-card>


    <el-row :gutter="20" >
      <el-col :span="24">
        <el-card>
          <div slot="header" class="flex justify-between ">
            <div>
            <span>{{ $t('reportNoFeeRoom.list.title') }}</span>
            <el-tooltip class="item" effect="dark" :content="$t('reportNoFeeRoom.list.tooltip')" placement="top">
              <i class="el-icon-info" style="margin-left:10px;cursor:pointer;"></i>
            </el-tooltip>
          </div>
            <el-button type="primary" size="small" style="float:right;" @click="handleExport">
              <i class="el-icon-download"></i>
              {{ $t('common.export') }}
            </el-button>
          </div>

          <el-table :data="tableData" border style="width:100%" v-loading="loading">
            <el-table-column prop="index" :label="$t('reportNoFeeRoom.table.index')" width="80" align="center" />
            <el-table-column prop="floorNum" :label="$t('reportNoFeeRoom.table.floor')" align="center" />
            <el-table-column prop="unitNum" :label="$t('reportNoFeeRoom.table.unit')" align="center" />
            <el-table-column prop="roomNum" :label="$t('reportNoFeeRoom.table.room')" align="center" />
            <el-table-column prop="ownerName" :label="$t('reportNoFeeRoom.table.ownerName')" align="center" />
            <el-table-column prop="link" :label="$t('reportNoFeeRoom.table.link')" align="center" />
          </el-table>

          <el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange"
            :current-page="pagination.current" :page-sizes="[10, 20, 30, 50]" :page-size="pagination.size"
            layout="total, sizes, prev, pager, next, jumper" :total="pagination.total" style="margin-top:20px;" />
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import { queryNoFeeRooms, queryFloors, queryUnits, queryRooms, exportData } from '@/api/report/reportNoFeeRoomApi'
import { getCommunityId } from '@/api/community/communityApi'

export default {
  name: 'ReportNoFeeRoomList',
  data() {
    return {
      loading: false,
      searchForm: {
        floorId: '',
        unitId: '',
        roomId: '',
        ownerName: '',
        link: '',
        communityId: ''
      },
      floors: [],
      units: [],
      rooms: [],
      tableData: [],
      pagination: {
        current: 1,
        size: 10,
        total: 0
      }
    }
  },
  created() {
    this.searchForm.communityId = getCommunityId()
    this.loadFloors()
    this.loadData()
  },
  methods: {
    async loadData() {
      try {
        this.loading = true
        const params = {
          ...this.searchForm,
          page: this.pagination.current,
          row: this.pagination.size
        }
        const { data, total } = await queryNoFeeRooms(params)
        this.tableData = data.map((item, index) => ({
          ...item,
          index: index + 1
        }))
        this.pagination.total = total
      } catch (error) {
        console.error('加载数据失败:', error)
      } finally {
        this.loading = false
      }
    },
    async loadFloors() {
      try {
        const params = {
          page: 1,
          row: 100,
          communityId: this.searchForm.communityId
        }
        const data = await queryFloors(params)
        this.floors = data
      } catch (error) {
        console.error('加载楼栋数据失败:', error)
      }
    },
    async handleFloorChange(floorId) {
      try {
        this.searchForm.unitId = ''
        this.searchForm.roomId = ''
        this.units = []
        this.rooms = []

        if (!floorId) return

        const params = {
          floorId,
          communityId: this.searchForm.communityId,
          page: 1,
          row: 100
        }
        const data = await queryUnits(params)
        this.units = data
      } catch (error) {
        console.error('加载单元数据失败:', error)
      }
    },
    async handleUnitChange(unitId) {
      try {
        this.searchForm.roomId = ''
        this.rooms = []

        if (!unitId) return

        const params = {
          unitId,
          communityId: this.searchForm.communityId,
          page: 1,
          row: 100
        }
        const data = await queryRooms(params)
        this.rooms = data.rooms
      } catch (error) {
        console.error('加载房屋数据失败:', error)
      }
    },
    handleSearch() {
      this.pagination.current = 1
      this.loadData()
    },
    handleReset() {
      this.searchForm = {
        floorId: '',
        unitId: '',
        roomId: '',
        ownerName: '',
        link: '',
        communityId: this.searchForm.communityId
      }
      this.units = []
      this.rooms = []
      this.pagination.current = 1
      this.loadData()
    },
    async handleExport() {
      try {
        this.loading = true
        const params = {
          ...this.searchForm,
          pagePath: 'reportNoFeeRoom'
        }
        await exportData(params)
        this.$message.success(this.$t('common.operationSuccess'))
        this.$router.push('/pages/property/downloadTempFile?tab=下载中心')

      } catch (error) {
        console.error('导出失败:', error)
        this.$message.error(this.$t('reportNoFeeRoom.exportFailed'))
      } finally {
        this.loading = false
      }
    },
    handleSizeChange(size) {
      this.pagination.size = size
      this.loadData()
    },
    handleCurrentChange(current) {
      this.pagination.current = current
      this.loadData()
    }
  }
}
</script>

<style lang="scss" scoped>
.report-no-fee-room-container {
  padding: 20px;

  .search-card {
    margin-bottom: 20px;

    .el-select,
    .el-input {
      width: 100%;
    }
  }

  .el-icon-info {
    color: #409EFF;
  }
}
</style>