<template>
  <div class="parking-space-container">
    <el-row :gutter="20">
      <el-col :span="24">
        <el-card class="box-card">
          <div slot="header" class=" flex justify-between">
            <span>{{ $t('listParkingSpace.queryCondition') }}</span>
            <div class="ibox-tools" style="">
              <el-button type="text" @click="toggleMoreCondition">
                {{ listParkingSpaceInfo.moreCondition ? $t('common.hide') : $t('common.more') }}
              </el-button>
            </div>
          </div>
          <div class="search-content">
            <el-row :gutter="20">
              <el-col :sm="4">
                <el-input-group>
                  <el-input v-model="listParkingSpaceInfo.conditions.areaNum"
                    :placeholder="$t('listParkingSpace.parkingLotPlaceholder')"></el-input>
                </el-input-group>
              </el-col>
              <el-col :sm="2">
                <el-input-group>
                  <el-button slot="append" type="primary" @click="openChooseParkingArea">
                    <i class="el-icon-search"></i>
                    {{ $t('listParkingSpace.select') }}
                  </el-button>
                </el-input-group>
              </el-col>
              <el-col :sm="6">
                <el-input v-model.trim="listParkingSpaceInfo.num"
                  :placeholder="$t('listParkingSpace.parkingSpaceNumPlaceholder')"></el-input>
              </el-col>
              <el-col :sm="6">
                <el-select v-model="listParkingSpaceInfo.conditions.state"
                  :placeholder="$t('listParkingSpace.parkingSpaceStatePlaceholder')" style="width:100%">
                  <el-option value="" :label="$t('listParkingSpace.parkingSpaceStatePlaceholder')"></el-option>
                  <el-option value="S" :label="$t('listParkingSpace.forSale')"></el-option>
                  <el-option value="H" :label="$t('listParkingSpace.forRent')"></el-option>
                  <el-option value="F" :label="$t('listParkingSpace.free')"></el-option>
                </el-select>
              </el-col>
              <el-col :sm="6">
                <el-button type="primary" @click="queryParkingSpaceMethod">
                  <i class="el-icon-search"></i>
                  {{ $t('listParkingSpace.query') }}
                </el-button>
                <el-button @click="resetParkingSpaceMethod">
                  <i class="el-icon-refresh"></i>
                  {{ $t('listParkingSpace.reset') }}
                </el-button>
              </el-col>
            </el-row>

            <el-row v-show="listParkingSpaceInfo.moreCondition" :gutter="20" style="margin-top:15px">
              <el-col :sm="6">
                <el-input v-model.trim="listParkingSpaceInfo.conditions.psId"
                  :placeholder="$t('listParkingSpace.parkingSpaceIdPlaceholder')"></el-input>
              </el-col>
            </el-row>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <el-row :gutter="20" style="margin-top:20px">
      <el-col :span="24">
        <el-card class="box-card">
          <div slot="header" class="flex justify-between">
            <span>{{ $t('listParkingSpace.parkingSpaceInfo') }}</span>
            <div style="float: right;">
              <el-button type="primary" size="small" @click="openBatchAddParkingSpaceModal">
                <i class="el-icon-plus"></i>
                {{ $t('listParkingSpace.batchAdd') }}
              </el-button>
              <el-button type="primary" size="small" @click="openAddParkingSpaceModal">
                <i class="el-icon-plus"></i>
                {{ $t('listParkingSpace.add') }}
              </el-button>
            </div>
          </div>

          <el-table :data="listParkingSpaceInfo.parkingSpaces" border style="width: 100%" v-loading="loading">
            <el-table-column prop="areaNum" :label="$t('listParkingSpace.parkingLot')" align="center"></el-table-column>
            <el-table-column prop="num" :label="$t('listParkingSpace.parkingSpace')" align="center"></el-table-column>
            <el-table-column :label="$t('listParkingSpace.parkingSpaceState')" align="center">
              <template slot-scope="scope">
                {{ viewParkingSpaceState(scope.row.state) }}
              </template>
            </el-table-column>
            <el-table-column prop="parkingTypeName" :label="$t('listParkingSpace.parkingSpaceType')"
              align="center"></el-table-column>
            <el-table-column prop="area" :label="$t('listParkingSpace.area')" align="center"></el-table-column>
            <el-table-column prop="createTime" :label="$t('listParkingSpace.createTime')"
              align="center"></el-table-column>
            <el-table-column :label="$t('listParkingSpace.operation')" align="center" width="200">
              <template slot-scope="scope">
                <el-button-group>
                  <el-button size="mini" type="primary" @click="openEditParkingSpaceModel(scope.row)">
                    {{ $t('listParkingSpace.modify') }}
                  </el-button>
                  <el-button size="mini" type="danger" @click="openDelParkingSpaceModel(scope.row)">
                    {{ $t('listParkingSpace.delete') }}
                  </el-button>
                </el-button-group>
              </template>
            </el-table-column>
          </el-table>

          <el-pagination @size-change="handleSizeChange" @current-change="handlePageChange"
            :current-page="listParkingSpaceInfo.currentPage" :page-sizes="[10, 20, 30, 50]"
            :page-size="listParkingSpaceInfo.pageSize" layout="total, sizes, prev, pager, next, jumper"
            :total="listParkingSpaceInfo.total" style="margin-top:20px;text-align:right"></el-pagination>
        </el-card>
      </el-col>
    </el-row>

    <!-- 子组件 -->
    <add-parking-space ref="addParkingSpace" @success="handleAddSuccess" />
    <edit-parking-space ref="editParkingSpace" @success="handleEditSuccess" />
    <delete-parking-space ref="deleteParkingSpace" @success="handleDeleteSuccess" />
    <choose-parking-area ref="chooseParkingArea" @choose="handleChooseParkingArea" />
    <batch-add-parking-space ref="batchAddParkingSpace" @success="handleBatchAddSuccess" />
  </div>
</template>

<script>
import { listParkingSpaces } from '@/api/car/listParkingSpaceApi'
import { getCommunityId } from '@/api/community/communityApi'
import AddParkingSpace from '@/components/car/addParkingSpace'
import EditParkingSpace from '@/components/car/editParkingSpace'
import DeleteParkingSpace from '@/components/car/deleteParkingSpace'
import ChooseParkingArea from '@/components/car/chooseParkingArea'
import BatchAddParkingSpace from '@/components/car/batchAddParkingSpace'

export default {
  name: 'ListParkingSpace',
  components: {
    AddParkingSpace,
    EditParkingSpace,
    DeleteParkingSpace,
    ChooseParkingArea,
    BatchAddParkingSpace
  },
  data() {
    return {
      loading: false,
      listParkingSpaceInfo: {
        parkingSpaces: [],
        total: 0,
        currentPage: 1,
        pageSize: 10,
        num: '',
        moreCondition: false,
        conditions: {
          psId: '',
          area: '',
          paId: '',
          areaNum: '',
          state: ''
        }
      }
    }
  },
  created() {
    this.loadParkingSpaceData()
  },
  methods: {
    async loadParkingSpaceData() {
      try {
        this.loading = true
        const params = {
          page: this.listParkingSpaceInfo.currentPage,
          row: this.listParkingSpaceInfo.pageSize,
          communityId: getCommunityId(),
          num: this.listParkingSpaceInfo.num,
          psId: this.listParkingSpaceInfo.conditions.psId,
          area: this.listParkingSpaceInfo.conditions.area,
          paId: this.listParkingSpaceInfo.conditions.paId,
          state: this.listParkingSpaceInfo.conditions.state
        }

        const res = await listParkingSpaces(params)
        this.listParkingSpaceInfo.parkingSpaces = res.parkingSpaces
        this.listParkingSpaceInfo.total = res.total
      } catch (error) {
        this.$message.error(this.$t('common.loadError'))
      } finally {
        this.loading = false
      }
    },

    viewParkingSpaceState(state) {
      if (state === 'S') return this.$t('listParkingSpace.forSale')
      if (state === 'H') return this.$t('listParkingSpace.forRent')
      if (state === 'F') return this.$t('listParkingSpace.free')
      return 'Unknown'
    },

    toggleMoreCondition() {
      this.listParkingSpaceInfo.moreCondition = !this.listParkingSpaceInfo.moreCondition
    },

    queryParkingSpaceMethod() {
      this.listParkingSpaceInfo.currentPage = 1
      this.loadParkingSpaceData()
    },

    resetParkingSpaceMethod() {
      this.listParkingSpaceInfo.conditions = {
        psId: '',
        area: '',
        paId: '',
        areaNum: '',
        state: ''
      }
      this.listParkingSpaceInfo.num = ''
      this.loadParkingSpaceData()
    },

    openChooseParkingArea() {
      this.$refs.chooseParkingArea.open()
    },

    openAddParkingSpaceModal() {
      this.$refs.addParkingSpace.open()
    },

    openEditParkingSpaceModel(row) {
      this.$refs.editParkingSpace.open(row)
    },

    openDelParkingSpaceModel(row) {
      this.$refs.deleteParkingSpace.open(row)
    },

    openBatchAddParkingSpaceModal() {
      this.$refs.batchAddParkingSpace.open()
    },

    handleChooseParkingArea(parkingArea) {
      this.listParkingSpaceInfo.conditions.paId = parkingArea.paId
      this.listParkingSpaceInfo.conditions.areaNum = parkingArea.num
    },

    handleAddSuccess() {
      this.loadParkingSpaceData()
    },

    handleEditSuccess() {
      this.loadParkingSpaceData()
    },

    handleDeleteSuccess() {
      this.loadParkingSpaceData()
    },

    handleBatchAddSuccess() {
      this.loadParkingSpaceData()
    },

    handlePageChange(page) {
      this.listParkingSpaceInfo.currentPage = page
      this.loadParkingSpaceData()
    },

    handleSizeChange(size) {
      this.listParkingSpaceInfo.pageSize = size
      this.loadParkingSpaceData()
    }
  }
}
</script>

<style scoped>
.parking-space-container {
  padding: 20px;
}

.search-content {
  padding: 10px 0;
}

.ibox-tools {
  display: inline-block;
  float: right;
  margin-top: 0;
  position: relative;
  padding: 0;
}

.el-input-group {
  display: flex;
}
</style>