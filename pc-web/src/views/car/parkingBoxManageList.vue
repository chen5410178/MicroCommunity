<template>
  <div class="parking-box-manage-container">
    <el-card class="search-card">
      <div slot="header" class="flex justify-between">
        <span>{{ $t('parkingBoxManage.searchTitle') }}</span>
      </div>
      <el-row :gutter="20">
        <el-col :span="6">
          <el-input v-model.trim="searchForm.boxId" :placeholder="$t('parkingBoxManage.boxIdPlaceholder')" clearable />
        </el-col>
        <el-col :span="6">
          <el-input v-model.trim="searchForm.boxName" :placeholder="$t('parkingBoxManage.boxNamePlaceholder')"
            clearable />
        </el-col>
        <el-col :span="6">
          <el-select v-model="searchForm.tempCarIn" :placeholder="$t('parkingBoxManage.tempCarInPlaceholder')" clearable
            style="width:100%">
            <el-option value="" :label="$t('parkingBoxManage.tempCarInPlaceholder')" />
            <el-option value="Y" :label="$t('parkingBoxManage.yes')" />
            <el-option value="N" :label="$t('parkingBoxManage.no')" />
          </el-select>
        </el-col>
        <el-col :span="6">
          <el-button type="primary" @click="handleSearch">
            {{ $t('parkingBoxManage.searchBtn') }}
          </el-button>
          <el-button @click="handleReset">
            {{ $t('parkingBoxManage.resetBtn') }}
          </el-button>
        </el-col>
      </el-row>
    </el-card>

    <el-card class="list-card">
      <div slot="header" class="flex justify-between">
        <span>{{ $t('parkingBoxManage.listTitle') }}</span>
      </div>

      <el-table v-loading="loading" :data="tableData" border style="width: 100%">
        <el-table-column prop="boxId" :label="$t('parkingBoxManage.boxId')" align="center" />
        <el-table-column prop="boxName" :label="$t('parkingBoxManage.boxName')" align="center" />
        <el-table-column prop="paNum" :label="$t('parkingBoxManage.parkingLot')" align="center" />
        <el-table-column prop="tempCarIn" :label="$t('parkingBoxManage.tempCarIn')" align="center">
          <template slot-scope="scope">
            {{ scope.row.tempCarIn === 'Y' ? $t('parkingBoxManage.yes') : $t('parkingBoxManage.no') }}
          </template>
        </el-table-column>
        <el-table-column prop="tempAuth" :label="$t('parkingBoxManage.tempAuth')" align="center">
          <template slot-scope="scope">
            {{ scope.row.tempAuth === 'Y' ? $t('parkingBoxManage.yes') : $t('parkingBoxManage.no') }}
          </template>
        </el-table-column>
        <el-table-column prop="fee" :label="$t('parkingBoxManage.fee')" align="center">
          <template slot-scope="scope">
            {{ scope.row.fee === 'Y' ? $t('parkingBoxManage.yes') : $t('parkingBoxManage.no') }}
          </template>
        </el-table-column>
        <el-table-column prop="blueCarIn" :label="$t('parkingBoxManage.blueCarIn')" align="center">
          <template slot-scope="scope">
            {{ scope.row.blueCarIn === 'Y' ? $t('parkingBoxManage.yes') : $t('parkingBoxManage.no') }}
          </template>
        </el-table-column>
        <el-table-column prop="yelowCarIn" :label="$t('parkingBoxManage.yelowCarIn')" align="center">
          <template slot-scope="scope">
            {{ scope.row.yelowCarIn === 'Y' ? $t('parkingBoxManage.yes') : $t('parkingBoxManage.no') }}
          </template>
        </el-table-column>
        <el-table-column prop="remark" :label="$t('parkingBoxManage.remark')" align="center" />
        <el-table-column :label="$t('parkingBoxManage.operation')" align="center" width="120">
          <template slot-scope="scope">
            <el-button v-if="hasPrivilege('502022090904120022')" type="text" size="small"
              @click="handleOpenConsole(scope.row)">
              {{ $t('parkingBoxManage.console') }}
            </el-button>
          </template>
        </el-table-column>
      </el-table>

      <el-row class="pagination-row">
        <el-col :span="12" class="data-source">
          {{ $t('parkingBoxManage.dataSource') }}
        </el-col>
        <el-col :span="12">
          <el-pagination :current-page="pagination.current" :page-sizes="[10, 20, 30, 50]" :page-size="pagination.size"
            layout="total, sizes, prev, pager, next, jumper" :total="pagination.total" @size-change="handleSizeChange"
            @current-change="handleCurrentChange" />
        </el-col>
      </el-row>
    </el-card>

  </div>
</template>

<script>
import { getParkingBoxList } from '@/api/car/parkingBoxManageApi'
import {jumpToIot} from '@/api/user/menuApi.js'

export default {
  name: 'ParkingBoxManageList',
  components: {
  },
  data() {
    return {
      loading: false,
      searchForm: {
        boxId: '',
        boxName: '',
        tempCarIn: ''
      },
      tableData: [],
      pagination: {
        current: 1,
        size: 10,
        total: 0
      }
    }
  },
  created() {
    this.fetchData()
  },
  methods: {

    async fetchData() {
      this.loading = true
      try {
        const params = {
          page: this.pagination.current,
          row: this.pagination.size,
          ...this.searchForm
        }

        const res = await getParkingBoxList(params)
        this.tableData = res.data
        this.pagination.total = res.total || 0
        this.pagination.current = res.page || 1
      } catch (error) {
        console.error('获取岗亭列表失败:', error)
        this.$message.error(this.$t('parkingBoxManage.fetchError'))
      } finally {
        this.loading = false
      }
    },

    handleSearch() {
      this.pagination.current = 1
      this.fetchData()
    },

    handleReset() {
      this.searchForm = {
        boxId: '',
        boxName: '',
        tempCarIn: ''
      }
      this.handleSearch()
    },

    handleSizeChange(size) {
      this.pagination.size = size
      this.fetchData()
    },

    handleCurrentChange(current) {
      this.pagination.current = current
      this.fetchData()
    },

    handleSuccess() {
      this.fetchData()
    },

    handleOpenAdd() {
      this.$refs.addDialog.open()
    },

    handleOpenEdit(row) {
      this.$refs.editDialog.open(row)
    },

    handleOpenDelete(row) {
      this.$refs.deleteDialog.open(row)
    },

    handleOpenConsole(row) {
      // 跳转到控制台页面
      jumpToIot(`/#/pages/car/parkingAreaControl?paId=${row.paId}&boxId=${row.boxId}`)

    }
  }
}
</script>

<style lang="scss" scoped>
.parking-box-manage-container {
  padding: 20px;

  .search-card {
    margin-bottom: 20px;
  }

  .list-card {
    margin-bottom: 20px;
  }

  .pagination-row {
    margin-top: 20px;
    display: flex;
    align-items: center;

    .data-source {
      display: flex;
      align-items: center;
      color: #999;
      font-size: 14px;
    }
  }
}
</style>