<template>
  <div class="animated fadeInRight ecommerce">
    <div class="bg-white padding-sm text-left">
      <el-button type="primary" size="small" @click="openAddParkingAreaModal">
        <i class="el-icon-plus"></i>{{ $t('listParkingSpace.addParkingArea') }}
      </el-button>
      <el-button type="primary" size="small" @click="openEditParkingAreaModal" v-if="conditions.paId && !conditions.sectionId">
        <i class="el-icon-edit"></i>{{ $t('listParkingSpace.editParkingArea') }}
      </el-button>
      <el-button type="primary" size="small" @click="openDeleteParkingAreaModal" v-if="conditions.paId && !conditions.sectionId">
        <i class="el-icon-close"></i>{{ $t('listParkingSpace.deleteParkingArea') }}
      </el-button>
      <el-button type="primary" size="small" class="margin-left-sm" @click="openAddSectionModal" v-if="conditions.paId && !conditions.sectionId">
        <i class="el-icon-plus"></i>{{ $t('listParkingSpace.addSection') }}
      </el-button>
      <el-button type="primary" size="small" class="margin-left-sm" @click="openEditSectionModal" v-if="conditions.sectionId">
        <i class="el-icon-edit"></i>{{ $t('listParkingSpace.editSection') }}
      </el-button>
      <el-button type="primary" size="small" class="margin-left-sm" @click="openDeleteSectionModal" v-if="conditions.sectionId">
        <i class="el-icon-close"></i>{{ $t('listParkingSpace.deleteSection') }}
      </el-button>
    </div>

    <el-row :gutter="20" class="margin-top-xs">
      <el-col :span="3" class="left-panel">
        <el-card class="parking-area-card">
          <parking-area-tree ref="parkingAreaTree" @switchParkingAreaSection="switchParkingAreaSection" />
        </el-card>
      </el-col>
      <el-col :span="21" class="right-panel">
        <el-card class="box-card">
          <div slot="header" class="clearfix flex justify-between">
            <div>{{ $t('listParkingSpace.queryCondition') }}</div>
          </div>
          <div class="">
            <el-row :gutter="20">
              <el-col :span="6">
                <el-input v-model.trim="listParkingSpaceInfo.num"
                  :placeholder="$t('listParkingSpace.parkingSpaceNumPlaceholder')" clearable />
              </el-col>
              <el-col :span="6">
                <el-select v-model="listParkingSpaceInfo.conditions.state"
                  :placeholder="$t('listParkingSpace.parkingSpaceStatePlaceholder')" style="width:100%">
                  <el-option value="" :label="$t('listParkingSpace.parkingSpaceStatePlaceholder')"></el-option>
                  <el-option value="S" :label="$t('listParkingSpace.forSale')"></el-option>
                  <el-option value="H" :label="$t('listParkingSpace.forRent')"></el-option>
                  <el-option value="F" :label="$t('listParkingSpace.free')"></el-option>
                </el-select>
              </el-col>
              <el-col :span="6">
                <el-input v-model.trim="listParkingSpaceInfo.conditions.psId"
                  :placeholder="$t('listParkingSpace.parkingSpaceIdPlaceholder')" clearable />
              </el-col>
              <el-col :span="6">
                <el-button type="primary" size="small" @click="queryParkingSpaceMethod">
                  {{ $t('listParkingSpace.query') }}
                </el-button>
                <el-button type="info" size="small" @click="resetParkingSpaceMethod">
                  {{ $t('listParkingSpace.reset') }}
                </el-button>
              </el-col>
            </el-row>
          </div>
        </el-card>

        <el-card class="box-card mt-20">
          <div slot="header" class="clearfix flex justify-between">
            <div>{{ $t('listParkingSpace.parkingSpaceInfo') }}</div>
            <div>
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
          <div class="">
            <el-table :data="listParkingSpaceInfo.parkingSpaces" border stripe style="width: 100%" v-loading="loading">
              <el-table-column prop="areaNum" :label="$t('listParkingSpace.parkingLot')" align="center" />
              <el-table-column :label="$t('listParkingSpace.section')" align="center">
                <template slot-scope="scope">
                  {{ scope.row.sectionName || scope.row.sectionNum || '-' }}
                </template>
              </el-table-column>
              <el-table-column prop="num" :label="$t('listParkingSpace.parkingSpace')" align="center" />
              <el-table-column :label="$t('listParkingSpace.parkingSpaceState')" align="center">
                <template slot-scope="scope">
                  {{ viewParkingSpaceState(scope.row.state) }}
                </template>
              </el-table-column>
              <el-table-column prop="parkingTypeName" :label="$t('listParkingSpace.parkingSpaceType')" align="center" />
              <el-table-column prop="area" :label="$t('listParkingSpace.area')" align="center" />
              <el-table-column prop="ownerName" :label="$t('listParkingSpace.owner')" align="center">
                <template slot-scope="scope">
                  {{ scope.row.ownerName || '-' }}
                </template>
              </el-table-column>
              <el-table-column prop="carNum" :label="$t('listParkingSpace.carNum')" align="center">
                <template slot-scope="scope">
                  {{ scope.row.carNum || '-' }}
                </template>
              </el-table-column>
              <el-table-column :label="$t('listParkingSpace.rentTime')" align="center">
                <template slot-scope="scope">
                  <div v-if="scope.row.state === 'H'">
                    {{ scope.row.startTime || '-' }}<br>~{{ scope.row.endTime || '-' }}
                  </div>
                  <div v-else>-</div>
                </template>
              </el-table-column>
              <el-table-column prop="createTime" :label="$t('listParkingSpace.createTime')" align="center" />
              <el-table-column :label="$t('listParkingSpace.operation')" align="center" width="200">
                <template slot-scope="scope">
                  <div class="parking-space-op">
                    <el-button size="mini" v-if="scope.row.state === 'F'"
                      @click="openRentParkingSpace(scope.row)">
                      {{ $t('listParkingSpace.rent') }}
                    </el-button>
                    <el-button size="mini" v-if="scope.row.state === 'F'"
                      @click="openSellParkingSpace(scope.row)">
                      {{ $t('listParkingSpace.sell') }}
                    </el-button>
                    <el-button size="mini" v-if="scope.row.state === 'S' || scope.row.state === 'H'"
                      @click="handleReleaseParkingSpace(scope.row)">
                      {{ $t('listParkingSpace.release') }}
                    </el-button>
                    <el-button size="mini" v-if="scope.row.state === 'S' || scope.row.state === 'H'"
                      @click="openUpdateParkingSpaceCar(scope.row)">
                      {{ $t('listParkingSpace.updateCar') }}
                    </el-button>
                    <el-button size="mini" @click="openEditParkingSpaceModel(scope.row)">
                      {{ $t('listParkingSpace.modify') }}
                    </el-button>
                    <el-button size="mini" @click="openDelParkingSpaceModel(scope.row)">
                      {{ $t('listParkingSpace.delete') }}
                    </el-button>
                  </div>
                </template>
              </el-table-column>
            </el-table>

            <el-pagination class="mt-20" @size-change="handleSizeChange" @current-change="handlePageChange"
              :current-page="listParkingSpaceInfo.currentPage" :page-sizes="[10, 20, 30, 50]"
              :page-size="listParkingSpaceInfo.pageSize" layout="total, sizes, prev, pager, next, jumper"
              :total="listParkingSpaceInfo.total" />
          </div>
        </el-card>
      </el-col>
    </el-row>

    <add-parking-area ref="addParkingArea" @success="handleSuccess" />
    <edit-parking-area ref="editParkingArea" @success="handleSuccess" />
    <delete-parking-area ref="deleteParkingArea" @success="handleSuccess" />
    <add-section ref="addSection" @success="handleSuccess" />
    <edit-section ref="editSection" @success="handleSuccess" />
    <delete-section ref="deleteSection" @success="handleSuccess" />
    <add-parking-space ref="addParkingSpace" @success="handleSuccess" />
    <edit-parking-space ref="editParkingSpace" @success="handleSuccess" />
    <delete-parking-space ref="deleteParkingSpace" @success="handleSuccess" />
    <batch-add-parking-space ref="batchAddParkingSpace" @success="handleSuccess" />
  </div>
</template>

<script>
import { listParkingSpaces, releaseParkingSpace } from '@/api/car/listParkingSpaceApi'
import { getParkingAreaList } from '@/api/car/parkingAreaManageApi'
import { getCommunityId } from '@/api/community/communityApi'
import ParkingAreaTree from '@/components/car/parkingAreaTree'
import AddParkingArea from '@/components/car/addParkingArea'
import EditParkingArea from '@/components/car/editParkingArea'
import DeleteParkingArea from '@/components/car/deleteParkingArea'
import AddSection from '@/components/car/addSection'
import EditSection from '@/components/car/editSection'
import DeleteSection from '@/components/car/deleteSection'
import AddParkingSpace from '@/components/car/addParkingSpace'
import EditParkingSpace from '@/components/car/editParkingSpace'
import DeleteParkingSpace from '@/components/car/deleteParkingSpace'
import BatchAddParkingSpace from '@/components/car/batchAddParkingSpace'

export default {
  name: 'ListParkingSpace',
  components: {
    ParkingAreaTree,
    AddParkingArea,
    EditParkingArea,
    DeleteParkingArea,
    AddSection,
    EditSection,
    DeleteSection,
    AddParkingSpace,
    EditParkingSpace,
    DeleteParkingSpace,
    BatchAddParkingSpace
  },
  data() {
    return {
      loading: false,
      selectedNode: {},
      conditions: {
        paId: '',
        sectionId: ''
      },
      listParkingSpaceInfo: {
        parkingSpaces: [],
        total: 0,
        currentPage: 1,
        pageSize: 10,
        num: '',
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
    switchParkingAreaSection(data) {
      this.selectedNode = data
      this.conditions.paId = data.paId
      this.conditions.sectionId = data.sectionId
      this.listParkingSpaceInfo.conditions.paId = data.paId
      this.listParkingSpaceInfo.conditions.sectionId = data.sectionId
      this.listParkingSpaceInfo.currentPage = 1
      this.loadParkingSpaceData()
    },

    refreshTree() {
      if (this.$refs.parkingAreaTree) {
        this.$refs.parkingAreaTree.refreshTree()
      }
    },

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
          sectionId: this.listParkingSpaceInfo.conditions.sectionId,
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

    queryParkingSpaceMethod() {
      this.listParkingSpaceInfo.currentPage = 1
      this.loadParkingSpaceData()
    },

    resetParkingSpaceMethod() {
      this.listParkingSpaceInfo.conditions.psId = ''
      this.listParkingSpaceInfo.conditions.state = ''
      this.listParkingSpaceInfo.num = ''
      this.loadParkingSpaceData()
    },

    openAddParkingAreaModal() {
      this.$refs.addParkingArea.open()
    },

    async openEditParkingAreaModal() {
      try {
        const params = {
          page: 1,
          row: 1,
          communityId: getCommunityId(),
          paId: this.conditions.paId
        }
        const res = await getParkingAreaList(params)
        if (res.parkingAreas && res.parkingAreas.length > 0) {
          this.$refs.editParkingArea.open(res.parkingAreas[0])
        } else {
          this.$message.warning(this.$t('listParkingSpace.parkingAreaNotFound'))
        }
      } catch (error) {
        console.error('加载停车场信息失败:', error)
        this.$message.error(this.$t('common.loadError'))
      }
    },

    openDeleteParkingAreaModal() {
      this.$refs.deleteParkingArea.open({ paId: this.conditions.paId })
    },

    openAddSectionModal() {
      this.$refs.addSection.open({ paId: this.conditions.paId, paName: this.selectedNode.num })
    },

    openEditSectionModal() {
      this.$refs.editSection.open(this.selectedNode)
    },

    openDeleteSectionModal() {
      this.$refs.deleteSection.open(this.selectedNode)
    },

    openAddParkingSpaceModal() {
      this.$refs.addParkingSpace.open({ paId: this.conditions.paId, sectionId: this.conditions.sectionId })
    },

    openEditParkingSpaceModel(row) {
      this.$refs.editParkingSpace.open(row)
    },

    openDelParkingSpaceModel(row) {
      this.$refs.deleteParkingSpace.open(row)
    },

    openBatchAddParkingSpaceModal() {
      this.$refs.batchAddParkingSpace.open({ paId: this.conditions.paId, sectionId: this.conditions.sectionId })
    },

    openRentParkingSpace(row) {
      this.$router.push({
        path: '/views/car/rentParkingSpace',
        query: { psId: row.psId, num: row.num, paId: row.paId, state: row.state }
      })
    },

    openSellParkingSpace(row) {
      this.$router.push({
        path: '/views/car/sellParkingSpace',
        query: { psId: row.psId, num: row.num, paId: row.paId, state: row.state }
      })
    },

    openUpdateParkingSpaceCar(row) {
      this.$router.push({
        path: '/views/car/updateParkingSpaceCar',
        query: { psId: row.psId }
      })
    },

    handleReleaseParkingSpace(row) {
      this.$confirm(this.$t('listParkingSpace.releaseConfirm'), this.$t('listParkingSpace.release'), {
        confirmButtonText: this.$t('common.confirm'),
        cancelButtonText: this.$t('common.cancel'),
        type: 'warning'
      }).then(() => {
        return releaseParkingSpace({ psId: row.psId })
      }).then(() => {
        this.$message.success(this.$t('listParkingSpace.releaseSuccess'))
        this.handleSuccess()
      }).catch(error => {
        if (error && error.message) {
          this.$message.error(error.message || this.$t('listParkingSpace.releaseError'))
        }
      })
    },

    handleSuccess() {
      this.refreshTree()
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
.animated {
  padding: 20px;
}

.mt-20 {
  margin-top: 20px;
}

.margin-top-xs {
  margin-top: 10px;
}

.margin-left-sm {
  margin-left: 10px;
}

.parking-space-op {
  display: flex;
  flex-wrap: wrap;
}

.parking-space-op .el-button {
  margin: 0 8px 8px 0;
}

.right-panel {
  padding-left: 5px;
}

.left-panel {
  .parking-area-card {
    height: 500px;

    .parking-area-list-wrapper {
      height: 630px;
      overflow-y: auto;
    }
  }
}
</style>
