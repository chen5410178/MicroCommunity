<template>
  <el-dialog :title="$t('selectNoPsCar.title')" :visible.sync="visible" width="800px" @close="handleClose">
    <el-form :inline="true" class="margin-bottom-sm">
      <el-form-item :label="$t('selectNoPsCar.carNum')">
        <el-input v-model="carQuery.carNum" clearable
          :placeholder="$t('selectNoPsCar.carNumQueryPlaceholder')" @keyup.enter.native="searchCars"></el-input>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="searchCars">{{ $t('common.search') }}</el-button>
      </el-form-item>
    </el-form>
    <el-table v-loading="carLoading" :data="carList" border highlight-current-row @row-click="selectCar">
      <el-table-column prop="carNum" :label="$t('selectNoPsCar.carNum')" align="center"></el-table-column>
      <el-table-column prop="ownerName" :label="$t('selectNoPsCar.owner')" align="center"></el-table-column>
      <el-table-column :label="$t('common.operation')" align="center" width="120">
        <template slot-scope="scope">
          <el-button type="text" @click.stop="selectCar(scope.row)">{{ $t('common.select') }}</el-button>
        </template>
      </el-table-column>
    </el-table>
    <el-pagination
      class="text-right margin-top-sm"
      :current-page="carPagination.page"
      :page-size="carPagination.row"
      :total="carPagination.total"
      layout="total, prev, pager, next"
      @current-change="handleCarPageChange"
    />
  </el-dialog>
</template>

<script>
import { queryCarsWithoutParkingSpace } from '@/api/car/listParkingSpaceApi'

export default {
  name: 'SelectNoPsCar',
  data() {
    return {
      visible: false,
      carLoading: false,
      carQuery: { carNum: '' },
      carList: [],
      carPagination: { page: 1, row: 10, total: 0 }
    }
  },
  methods: {
    open() {
      this.carQuery.carNum = ''
      this.carPagination.page = 1
      this.visible = true
      this.loadCars()
    },
    searchCars() {
      this.carPagination.page = 1
      this.loadCars()
    },
    loadCars() {
      this.carLoading = true
      const params = {
        page: this.carPagination.page,
        row: this.carPagination.row
      }
      if (this.carQuery.carNum) {
        params.carNumLike = this.carQuery.carNum
      }
      queryCarsWithoutParkingSpace(params).then(res => {
        this.carList = res.data || []
        this.carPagination.total = res.total || 0
      }).catch(error => {
        console.error('查询车辆列表失败:', error)
        this.$message.error(error.message || this.$t('selectNoPsCar.loadError'))
      }).finally(() => {
        this.carLoading = false
      })
    },
    selectCar(row) {
      this.$emit('select', row)
      this.visible = false
    },
    handleCarPageChange(page) {
      this.carPagination.page = page
      this.loadCars()
    },
    handleClose() {
      this.carQuery.carNum = ''
      this.carPagination.page = 1
      this.carPagination.total = 0
    }
  }
}
</script>
