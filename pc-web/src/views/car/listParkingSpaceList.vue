<template>
  <div class="animated fadeInRight ecommerce">
    <el-row :gutter="20">
      <el-col :span="3" class="left-panel">
        <el-card class="parking-area-card">
          <div class="parking-area-list-wrapper">
            <ul class="parking-area-list">
              <li @click="_switchParkingArea({ paId: '', num: '' })"
                :class="{ 'selected': selectedPaId === '' }"
                class="parking-area-item">
                {{ $t('listParkingSpace.all') }}
              </li>
              <li v-for="(item, index) in parkingAreas" :key="index" @click="_switchParkingArea(item)"
                :class="{ 'selected': selectedPaId === item.paId }"
                class="parking-area-item">
                {{ item.num }}
              </li>
            </ul>
          </div>
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
              <el-table-column prop="num" :label="$t('listParkingSpace.parkingSpace')" align="center" />
              <el-table-column :label="$t('listParkingSpace.parkingSpaceState')" align="center">
                <template slot-scope="scope">
                  {{ viewParkingSpaceState(scope.row.state) }}
                </template>
              </el-table-column>
              <el-table-column prop="parkingTypeName" :label="$t('listParkingSpace.parkingSpaceType')" align="center" />
              <el-table-column prop="area" :label="$t('listParkingSpace.area')" align="center" />
              <el-table-column prop="createTime" :label="$t('listParkingSpace.createTime')" align="center" />
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

            <el-pagination class="mt-20" @size-change="handleSizeChange" @current-change="handlePageChange"
              :current-page="listParkingSpaceInfo.currentPage" :page-sizes="[10, 20, 30, 50]"
              :page-size="listParkingSpaceInfo.pageSize" layout="total, sizes, prev, pager, next, jumper"
              :total="listParkingSpaceInfo.total" />
          </div>
        </el-card>
      </el-col>
    </el-row>

    <add-parking-space ref="addParkingSpace" @success="handleAddSuccess" />
    <edit-parking-space ref="editParkingSpace" @success="handleEditSuccess" />
    <delete-parking-space ref="deleteParkingSpace" @success="handleDeleteSuccess" />
    <batch-add-parking-space ref="batchAddParkingSpace" @success="handleBatchAddSuccess" />
  </div>
</template>

<script>
import { listParkingSpaces, listParkingAreas } from '@/api/car/listParkingSpaceApi'
import { getCommunityId } from '@/api/community/communityApi'
import AddParkingSpace from '@/components/car/addParkingSpace'
import EditParkingSpace from '@/components/car/editParkingSpace'
import DeleteParkingSpace from '@/components/car/deleteParkingSpace'
import BatchAddParkingSpace from '@/components/car/batchAddParkingSpace'

export default {
  name: 'ListParkingSpace',
  components: {
    AddParkingSpace,
    EditParkingSpace,
    DeleteParkingSpace,
    BatchAddParkingSpace
  },
  data() {
    return {
      loading: false,
      selectedPaId: '',
      parkingAreas: [],
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
    this._loadParkingAreas()
    this.loadParkingSpaceData()
  },
  methods: {
    async _loadParkingAreas() {
      try {
        const params = {
          page: 1,
          row: 100,
          communityId: getCommunityId()
        }
        const res = await listParkingAreas(params)
        this.parkingAreas = res.parkingAreas
      } catch (error) {
        console.error('获取停车场失败:', error)
      }
    },

    _switchParkingArea(item) {
      this.selectedPaId = item.paId
      this.listParkingSpaceInfo.conditions.paId = item.paId
      this.listParkingSpaceInfo.conditions.areaNum = ''
      this.listParkingSpaceInfo.currentPage = 1
      this.loadParkingSpaceData()
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

    handleAddSuccess() {
      this._loadParkingAreas()
      this.loadParkingSpaceData()
    },

    handleEditSuccess() {
      this._loadParkingAreas()
      this.loadParkingSpaceData()
    },

    handleDeleteSuccess() {
      this._loadParkingAreas()
      this.loadParkingSpaceData()
    },

    handleBatchAddSuccess() {
      this._loadParkingAreas()
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

.right-panel {
  padding-left: 5px;
}

.left-panel {
  .parking-area-card {
    height: 700px;

      .parking-area-list-wrapper {
      height: 630px;
      overflow-y: auto;

      &::-webkit-scrollbar {
        width: 4px;
      }

      &::-webkit-scrollbar-thumb {
        background: #dcdfe6;
        border-radius: 2px;
      }

      &::-webkit-scrollbar-track {
        background: transparent;
      }

      .parking-area-list {
        list-style: none;
        padding: 0 5px;
        margin: 0;

        .parking-area-item {
          padding: 10px;
          text-align: center;
          cursor: pointer;
          border-radius: 4px;
          margin-bottom: 5px;
          transition: all 0.3s;
          font-size: 14px;
          color: #606266;

          &:hover {
            background-color: #f5f7fa;
          }

          &.selected {
            background-color: #409eff;
            color: white;
          }
        }
      }
    }
  }
}
</style>