<template>
  <div class="parking-area-manage animated fadeInRight">
    <el-row :gutter="20">
      <el-col :span="24">
        <el-card>
          <div slot="header" class="text-left">
            <span>{{ $t('parkingAreaManage.queryTitle') }}</span>
          </div>
          <div class="flex justify-start">
            <div class="width-100">
              <el-input v-model.trim="parkingAreaManageInfo.conditions.num"
                :placeholder="$t('parkingAreaManage.inputParkingNum')" clearable />
            </div>
            <div class="width-100">
              <el-select v-model="parkingAreaManageInfo.conditions.typeCd"
                :placeholder="$t('parkingAreaManage.selectParkingType')" clearable style="width: 100%">
                <el-option v-for="item in parkingTypes" :key="item.value" :label="item.label" :value="item.value" />
              </el-select>
            </div>
            <div class="width-100">
              <el-button type="primary" @click="_queryParkingAreaMethod">
                {{ $t('common.search') }}
              </el-button>
              <el-button @click="_resetParkingAreaMethod">
                {{ $t('common.reset') }}
              </el-button>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <el-row :gutter="20" style="margin-top: 20px">
      <el-col :span="24">
        <el-card>
          <div slot="header" class="flex justify-between">
            <span>{{ $t('parkingAreaManage.parkingInfo') }}</span>
            <el-button v-if="hasPrivilege('502022100147030003')" type="primary" size="small" class="float-right"
              @click="_openAddParkingAreaModal">
              {{ $t('common.add') }}
            </el-button>
          </div>

          <el-table :data="parkingAreaManageInfo.parkingAreas" border style="width: 100%" v-loading="loading">
            <el-table-column prop="paId" :label="$t('parkingAreaManage.parkingId')" align="center" />
            <el-table-column prop="num" :label="$t('parkingAreaManage.parkingNum')" align="center" />
            <el-table-column :label="$t('parkingAreaManage.parkingType')" align="center">
              <template slot-scope="scope">
                {{ scope.row.typeCd === '1001' ? $t('parkingAreaManage.aboveGround') :
                  $t('parkingAreaManage.underground')
                }}
              </template>
            </el-table-column>
            <el-table-column v-for="(item, index) in parkingAreaManageInfo.listColumns" :key="index"
              :label="item.specName" align="center">
              <template slot-scope="scope">
                {{ scope.row.listValues[index] }}
              </template>
            </el-table-column>
            <el-table-column prop="remark" :label="$t('parkingAreaManage.remark')" align="center" />
            <el-table-column prop="createTime" :label="$t('parkingAreaManage.createTime')" align="center" />
            <el-table-column :label="$t('common.operation')" align="center" width="250">
              <template slot-scope="scope">
                <el-button v-if="hasPrivilege('502022100103010004')" size="mini"
                  @click="_openEditParkingAreaModel(scope.row)">
                  {{ $t('common.edit') }}
                </el-button>
                <el-button v-if="hasPrivilege('502022100117490005')" size="mini" type="danger"
                  @click="_openDeleteParkingAreaModel(scope.row)">
                  {{ $t('common.delete') }}
                </el-button>
                <el-button v-if="hasPrivilege('502022100112510006')" size="mini" type="success"
                  @click="_openParkingAreaTotalControl(scope.row)">
                  {{ $t('parkingAreaManage.controlPanel') }}
                </el-button>
              </template>
            </el-table-column>
          </el-table>

          <el-pagination :current-page.sync="pagination.current" :page-sizes="[10, 20, 30, 50]"
            :page-size="pagination.size" :total="pagination.total" layout="total, sizes, prev, pager, next, jumper"
            @size-change="handleSizeChange" @current-change="handleCurrentChange" />
        </el-card>
      </el-col>
    </el-row>

    <add-parking-area ref="addModal" @success="handleAddSuccess" />
    <edit-parking-area ref="editModal" @success="handleEditSuccess" />
    <delete-parking-area ref="deleteModal" @success="handleDeleteSuccess" />
  </div>
</template>

<script>
import { getParkingAreaList } from '@/api/car/parkingAreaManageApi'
import AddParkingArea from '@/components/car/addParkingArea'
import EditParkingArea from '@/components/car/editParkingArea'
import DeleteParkingArea from '@/components/car/deleteParkingArea'
import { getCommunityId } from '@/api/community/communityApi'
import {jumpToIot} from '@/api/user/menuApi.js'

export default {
  name: 'ParkingAreaManageList',
  components: {
    AddParkingArea,
    EditParkingArea,
    DeleteParkingArea
  },
  data() {
    return {
      loading: false,
      parkingAreaManageInfo: {
        parkingAreas: [],
        listColumns: [],
        conditions: {
          num: '',
          typeCd: '',
          paId: ''
        }
      },
      pagination: {
        current: 1,
        size: 10,
        total: 0
      },
      parkingTypes: [
        { value: '1001', label: this.$t('parkingAreaManage.aboveGround') },
        { value: '2001', label: this.$t('parkingAreaManage.underground') }
      ]
    }
  },
  created() {
    this._getColumns()
    this._listParkingAreas()
  },
  methods: {
    async _listParkingAreas() {
      this.loading = true
      try {
        const params = {
          page: this.pagination.current,
          row: this.pagination.size,
          ...this.parkingAreaManageInfo.conditions,
          communityId: getCommunityId()
        }

        const res = await getParkingAreaList(params)
        this.parkingAreaManageInfo.parkingAreas = res.parkingAreas
        this.pagination.total = res.total
        this.dealParkingAreaAttr(res.parkingAreas)
      } catch (error) {
        console.error('获取停车场列表失败:', error)
        this.$message.error(this.$t('parkingAreaManage.fetchError'))
      } finally {
        this.loading = false
      }
    },

    _queryParkingAreaMethod() {
      this.pagination.current = 1
      this._listParkingAreas()
    },

    _resetParkingAreaMethod() {
      this.parkingAreaManageInfo.conditions = {
        num: '',
        typeCd: '',
        paId: ''
      }
      this._listParkingAreas()
    },

    _openAddParkingAreaModal() {
      this.$refs.addModal.open()
    },

    _openEditParkingAreaModel(row) {
      this.$refs.editModal.open(row)
    },

    _openDeleteParkingAreaModel(row) {
      this.$refs.deleteModal.open(row)
    },

    handleAddSuccess() {
      this._listParkingAreas()
    },

    handleEditSuccess() {
      this._listParkingAreas()
    },

    handleDeleteSuccess() {
      this._listParkingAreas()
    },

    handleSizeChange(size) {
      this.pagination.size = size
      this._listParkingAreas()
    },

    handleCurrentChange(current) {
      this.pagination.current = current
      this._listParkingAreas()
    },

    dealParkingAreaAttr(parkingAreas) {
      parkingAreas.forEach(item => {
        this._getColumnsValue(item)
      })
    },

    _getColumnsValue(parkingArea) {
      parkingArea.listValues = []
      if (!parkingArea.attrs || parkingArea.attrs.length === 0) {
        this.parkingAreaManageInfo.listColumns.forEach(() => {
          parkingArea.listValues.push('')
        })
        return
      }

      this.parkingAreaManageInfo.listColumns.forEach(column => {
        const attr = parkingArea.attrs.find(a => a.specCd === column.specCd)
        parkingArea.listValues.push(attr ? attr.value : '')
      })
    },

    async _getColumns() {
      try {
        // 这里需要根据实际接口获取列配置
        // 示例代码，实际需要替换为API调用
        this.parkingAreaManageInfo.listColumns = [
        ]
      } catch (error) {
        console.error('获取列配置失败:', error)
      }
    },

    _openParkingAreaTotalControl(parkingArea) {
      // 跳转到控制台逻辑
      console.log('打开控制台', parkingArea)
      jumpToIot(`/#/pages/car/parkingAreaTotalControl?paId=${parkingArea.paId}`)
    }
  }
}
</script>

<style scoped>
.parking-area-manage {
  padding: 20px;
}

.float-right {
  float: right;
}

.width-100 {
  width: 250px;
  margin-right: 10px;
}
</style>