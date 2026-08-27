<template>
  <div>
    <el-card class="box-card margin">
      <div slot="header" class="flex justify-between">
        <span>{{ $t('sellParkingSpace.title') }}</span>
        <div class="">
          <el-button type="primary" size="small" icon="el-icon-close" @click="goBack">
            {{ $t('common.back') }}
          </el-button>
        </div>
      </div>

      <el-form ref="form" :model="sellParkingSpaceInfo" :rules="rules" label-width="120px" label-position="right">
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item :label="$t('sellParkingSpace.parkingSpace')" prop="num">
              <el-input :value="sellParkingSpaceInfo.num" disabled></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item :label="$t('sellParkingSpace.parkingArea')">
              <el-input :value="sellParkingSpaceInfo.paId" disabled></el-input>
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item :label="$t('sellParkingSpace.carNum')" prop="carId">
              <div class="flex">
                <el-input :value="selectedCarNum" disabled
                  :placeholder="$t('sellParkingSpace.carNumPlaceholder')"></el-input>
                <el-button type="primary" class="car-select-btn" @click="openCarDialog">{{ $t('sellParkingSpace.selectCar') }}</el-button>
              </div>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item :label="$t('sellParkingSpace.owner')" prop="ownerName">
              <el-input :value="sellParkingSpaceInfo.ownerName" disabled
                :placeholder="$t('sellParkingSpace.ownerPlaceholder')"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
    </el-card>

    <div class="mt-20 text-right">
      <el-button @click="goBack">{{ $t('common.back') }}</el-button>
      <el-button type="primary" @click="submitSell" :loading="saving">
        {{ $t('common.submit') }}
      </el-button>
    </div>

    <el-dialog :title="$t('sellParkingSpace.selectCar')" :visible.sync="carDialogVisible" width="800px"
      @close="handleCarDialogClose">
      <el-form :inline="true" class="margin-bottom-sm">
        <el-form-item :label="$t('sellParkingSpace.carNum')">
          <el-input v-model="carQuery.carNum" clearable
            :placeholder="$t('sellParkingSpace.carNumQueryPlaceholder')" @keyup.enter.native="searchCars"></el-input>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="searchCars">{{ $t('common.search') }}</el-button>
        </el-form-item>
      </el-form>
      <el-table v-loading="carLoading" :data="carList" border highlight-current-row @row-click="selectCar">
        <el-table-column prop="carNum" :label="$t('sellParkingSpace.carNum')" align="center"></el-table-column>
        <el-table-column prop="ownerName" :label="$t('sellParkingSpace.owner')" align="center"></el-table-column>
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
  </div>
</template>

<script>
import { sellParkingSpace, queryOwnerCars } from '@/api/car/listParkingSpaceApi'
import { getDateStr } from '@/utils/dateUtil'

export default {
  name: 'SellParkingSpaceList',
  data() {
    return {
      saving: false,
      selectedCarNum: '',
      carDialogVisible: false,
      carLoading: false,
      carQuery: { carNum: '' },
      carList: [],
      carPagination: { page: 1, row: 10, total: 0 },
      sellParkingSpaceInfo: {
        psId: '',
        num: '',
        paId: '',
        carId: '',
        ownerId: '',
        ownerName: ''
      },
      rules: {
        carId: [
          { required: true, message: this.$t('sellParkingSpace.carNumRequired'), trigger: 'change' }
        ]
      }
    }
  },
  created() {
    const query = this.$route.query
    this.sellParkingSpaceInfo.psId = query.psId || ''
    this.sellParkingSpaceInfo.num = query.num || ''
    this.sellParkingSpaceInfo.paId = query.paId || ''
  },
  methods: {
    openCarDialog() {
      this.carQuery.carNum = ''
      this.carPagination.page = 1
      this.carDialogVisible = true
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
      queryOwnerCars(params).then(res => {
        this.carList = res.data || []
        this.carPagination.total = res.total || 0
      }).catch(error => {
        console.error('查询车辆列表失败:', error)
        this.$message.error(error.message || this.$t('sellParkingSpace.errorMessage'))
      }).finally(() => {
        this.carLoading = false
      })
    },
    selectCar(row) {
      this.sellParkingSpaceInfo.carId = row.carId || ''
      this.sellParkingSpaceInfo.ownerId = row.ownerId || ''
      this.sellParkingSpaceInfo.ownerName = row.ownerName || ''
      this.selectedCarNum = row.carNum || ''
      this.carDialogVisible = false
    },
    handleCarPageChange(page) {
      this.carPagination.page = page
      this.loadCars()
    },
    handleCarDialogClose() {
      this.carQuery.carNum = ''
      this.carPagination.page = 1
      this.carPagination.total = 0
    },
    goBack() {
      this.$router.go(-1)
    },
    submitSell() {
      this.$refs.form.validate(valid => {
        if (!valid) return
        this.saving = true
        const params = {
          psId: this.sellParkingSpaceInfo.psId,
          carId: this.sellParkingSpaceInfo.carId,
          ownerId: this.sellParkingSpaceInfo.ownerId,
          sellOrHire: 'S',
          startTime: getDateStr(),
          endTime: '2050-01-01 00:00:00',
          storeId: ''
        }
        sellParkingSpace(params).then(() => {
          this.$message.success(this.$t('common.operationSuccess'))
          this.goBack()
        }).catch(error => {
          console.error('出售车位失败:', error)
          this.$message.error(error.message || this.$t('sellParkingSpace.errorMessage'))
        }).finally(() => {
          this.saving = false
        })
      })
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

.car-select-btn {
  margin-left: 10px;
  flex-shrink: 0;
}
</style>
